# CLAUDE.md — daobab-core

Guidance for Claude Code when working in this repository.

## What this project is

**Daobab** (`io.daobab:daobab-core`, http://www.daobab.io) is a **Java ORM** that lets you build SQL as
fully object-oriented Java code. The same query API runs against several *targets*:

- a **database** (JDBC),
- in-memory **collections**,
- **buffers**,
- **remotely**.

Query style (object-oriented, no strings):

```java
db.select(tabCustomer)
  .

where(and()
      .

equal(tabCustomer.colActive(), true)
        .

equal(tabCustomer.colLastName(), "WILSON"))
        .

orderAscBy(tabCustomer.colLastName())
        .

findMany();
```

- **Java 21**, Maven, Apache License 2.0. Single author: Klaudiusz Wojtkowiak (Elephant Software).
- Current working branch: `1.8.3-preview` (pom version `1.8.3`). Main branch: `main`.

## Core domain model (`io.daobab.model`)

- An **entity** is a class extending `Table<E>` (or `DtoTable<E, D>` when it has a DTO), annotated
  `@TableInformation(name = "...")`.
- Each **column** is a shared **interface** with `default` getter/setter/`col<Name>()` methods
  (`get<Name>()`, `set<Name>()`, `col<Name>()`). Entities `implements` the column interfaces they use,
  so the SAME column (same name + type) is one interface reused across many tables — that is the central
  Daobab idea. A `Column<E, F, C>` is the typed column handle; `PrimaryKey<E, F, C>` adds `getId()`/`colID()`.
- `DtoTable<E, D>` connects an entity to an immutable DTO via `toDto()` / `fromDto(dto)`.

## The generator + annotation processor (recent focus area)

Two independent ways to produce entities from a database schema. **They must stay behaviourally aligned.**

### 1. Runtime generator — `io.daobab.generator.*`

Reads JDBC metadata and writes Java/Kotlin/TypeScript sources directly.

- `DaobabGenerator` — entry point; `getUniqueColumn()` **deduplicates columns by name + Java type**
  (same name+type → one shared column; same name, different type → separate columns).
- `ColumnAnalysator.compileNames()` — resolves final field names; on a **name clash with different types**
  it disambiguates: `Name` → `NameTypeString` / `NameTypeInteger` (`byte[]` → `NameTypeByteArray`), then a
  numeric counter for any residual collisions.
- `GenerateColumn` — per-column model; `getTableTypeDescription()` renders the **HTML Javadoc table**
  (Table / Type / Size / Nullable) placed above `col<Name>()`.
- `GenerateDefinition` — "definitions-only" mode: emits annotated `XxxDef` interfaces (input for the processor).
- `Writer` + `generator/template/{JavaTemplates,KotlinTemplates,TypeScriptTemplates}` — templating via
  `Replacer` and `GenKeys` placeholders (e.g. `TABLES_AND_TYPE`, `COLUMN_METHODS`).

### 2. Annotation processor — `io.daobab.processor.DaobabEntityProcessor`

Pure JDK annotation-processing API (no third-party libs). Turns `@DaobabTable` definition interfaces into
entity + column interfaces + DTO **at compile time**. Registered via
`META-INF/services/javax.annotation.processing.Processor`.

- `process()` runs in **two phases**: `prepare()` (validate each definition → `EntityContext`), then a
  usage-collection pass building `columnUsage` (key `columnPackage|baseFieldName|fieldType`), then
  `writeSources()` (`ensureColumnInterface` → `writeDto` → `writeEntity`).
- `ensureColumnInterface()` mirrors the generator's disambiguation: on a type clash it appends
  `typeSuffix` (`Integer`→`TypeInteger`, `byte[]`→`TypeByteArray`) instead of failing. Difference vs the
  generator: the **first** occurrence keeps the plain name; only later ones get the suffix (processing order
  decides which — non-deterministic across builds but internally consistent per compilation).
- `columnDoc()` renders the same HTML Javadoc table above `col<Name>()`, listing every table using the
  column (Table / Java Type / Size / Not-null). It's keyed by base name so shared interfaces list all tables.
  Caveat: usage is collected per compilation round.

### `@DaobabDataBase` — the database interface (same processor)

`io.daobab.annotation.DaobabDataBase` (source retention, `@Target(TYPE)`) gathers a set of `@DaobabTable`
definitions into one **`Tables` interface** — the compile-time counterpart of the hand-written
`io.daobab.target.database.meta.MetaDataTables` and of the `Tables` interface the generator emits from JDBC
metadata (`GenerateTarget`). Attributes: `name()` (→ interface named `name + "Tables"`), `tables()`
(`Class<?>[]` of the `@DaobabTable` definition interfaces, optional), `tablesPackage()` (scan a whole package,
optional), `targetPackage()` (default: the annotated element's package). At least one of `tables`/`tablesPackage`
must select a table.

- The **same** `DaobabEntityProcessor` handles it (added to `getSupportedAnnotationTypes()`). After the entity
  pass, `process()` calls `writeTablesInterface()` for each `@DaobabDataBase` element, passing a
  `Map<package, List<definition>>` index of the round's `@DaobabTable` elements it built in the first loop.
- `tables()` is read via **`MirroredTypesException`** (`tableMirrors()`) — the referenced defs are being
  compiled, so they aren't available as `Class` objects. Each must be an interface annotated `@DaobabTable`,
  else compilation fails.
- `tablesPackage()` picks up every `@DaobabTable` interface of that package **from the round index** (not from
  the classpath — source-retention means only same-compilation defs are visible), added after the explicit
  `tables()` and **sorted alphabetically** for a stable output; duplicates across the two are deduplicated by
  FQN. An empty scan is an error, not a silent no-op.
- Emitted interface `extends QueryWhisperer` (generator parity) with one field per entity:
  `BookEntity tabBook = new BookEntity();`. Field name is `tab` + entity name **stripped of the `Entity`
  suffix** (`tabBook`, matching the `tabCustomer` ergonomics and `SakilaTables`/`MetaDataTables`).
- Each field is preceded by a **Javadoc `<pre>` schema table** (`tableDoc()`): Name (with `(PK)`) / Type /
  Size / DBName, columns sorted by field name and aligned with the generator's `pad()` rule — the same doc
  the generator's `TableDescriptionGenerator` renders above its `tab...` fields, minus DBType and remarks
  (a definition carries neither).
- `resolveEntityRef()` (shared with `prepare()`) computes each entity's package + name, so the field
  types and the entity generation always agree on where an entity lands.
- Constraint of the source retention: the referenced defs must be compiled **in the same compilation** as
  the `@DaobabDataBase` element (`@DaobabTable` is invisible on already-compiled classes).

### DTO generation (both paths)

Entities get an `Entity` suffix and extend `DtoTable<E, D>`; a plain-Java immutable DTO with a builder is
generated (deliberately **no Lombok** — Daobab must not force the dependency), with `toDto()`/`fromDto()`.
Kotlin path emits a `data class` DTO. TypeScript path unchanged. Composite primary keys are **not** supported
by the processor (the generator warns and omits PK markers). Reference pattern: `ModelItemEntity`/`ModelItem`
in the sibling project `E:\IdeaProjects\item-collector`.

## Build & test

```bash
mvn test                                   # full test suite
mvn -Dtest=TestDaobabEntityProcessor test  # processor tests (compile Defs with a real JavaCompiler)
mvn -Dtest="io.daobab.generator.*Test" test
```

Main compile uses `-proc:none` (Daobab ships its own processor, so it must not self-apply during its own build).

### Key tests

- `io.daobab.processor.TestDaobabEntityProcessor` — drives the processor via the JDK `JavaCompiler` API and
  loads the generated classes reflectively (disambiguation, shared interfaces, doc tables, DTO round-trip,
  and the `@DaobabDataBase` `Tables` interface).
- `io.daobab.generator.{JavaTableAndDtoGenerationTest, KotlinTableAndDtoGenerationTest,
  JavaDefinitionGenerationTest}` — Java tests compile the generated code; Kotlin test asserts on content
  (no Kotlin compiler in deps).

### JMH benchmarks

Behind the `benchmark` Maven profile (`src/benchmark/java`, not compiled in normal builds):

```bash
mvn -Pbenchmark clean test-compile exec:exec   # subset: -Dbenchmark.include=regex
mvn clean                                       # REQUIRED afterwards — leftover JMH classes break JUnit discovery
```

Uses the sakila test entities (`io.daobab.test.dao`) and `MockDataBase`. `exec` runs `${java.home}/bin/java`
(plain `java` on PATH may be an old JRE).

## Conventions

- Match the surrounding style: tabs in generated-source string builders, existing Javadoc density, author tag
  `@author Klaudiusz Wojtkowiak, (C) Elephant Software`.
- **When you change entity generation, change it in both the generator and the processor** and keep the
  emitted output consistent; add/adjust tests in both `TestDaobabEntityProcessor` and the generator tests.
- Windows dev machine; shell is PowerShell (Bash tool also available).
