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

### `@DaobabColumn(typeConverterClass = ...)` — a per-column type converter (processor-only)

`@DaobabColumn` takes an optional `typeConverterClass` (a `Class<? extends DatabaseTypeConverter>`, default the
raw `DatabaseTypeConverter` interface as a "none" sentinel). When set, the generated column interface's
`col<Name>()` calls `DaobabCache.getColumnWithConverter(...)` instead of `getColumn(...)`, passing the converter
class; that factory (→ `ColumnCreator.createColumn(..., converterClass)`) returns a `Column` overriding
`getColumnTypeConverter()`, which is exactly where `DatabaseConverterManager.getConverter()` looks first. So the
annotation is the compile-time equivalent of hand-overriding `Column.getColumnTypeConverter()`.

- **Validation** (`validateConverter` / `converterColumnType`): the converter's column type — the `T` of
  `DatabaseTypeConverter<F, T>`, resolved by walking `directSupertypes` (so an intermediate base like
  `TypeConverterIntegerBased<T>` is followed to `DatabaseTypeConverter<Integer, T>`) — must equal the annotated
  method's (boxed) return type, else compilation fails. An unresolvable `T` (bound to a type variable) is skipped.
- **Shared interfaces**: a column interface is shared by name+type, and it carries **one** converter
  (`generatedColumnConverters`); two definitions reusing the same interface with different converters is an error.
- **Read via `MirroredTypeException`** (single type), the counterpart of `tableMirrors()`'s `MirroredTypesException`.
- **Generator parity note**: this is processor-only — the runtime generator reverse-engineers JDBC metadata, which
  carries no converter info, so `GenerateDefinition` never emits `typeConverterClass` and the column templates are
  unchanged. The wiring (`DaobabCache.getColumnWithConverter`, `ColumnCreator.createColumn` overload) lives in core.

### `@DaobabDataBase` — the database interface (same processor)

`io.daobab.annotation.DaobabDataBase` (source retention, `@Target(TYPE)`) gathers a set of `@DaobabTable`
definitions into one **`Tables` interface** — the compile-time counterpart of the hand-written
`io.daobab.target.database.meta.MetaDataTables` and of the `Tables` interface the generator emits from JDBC
metadata (`GenerateTarget`). Attributes: `name()` (→ interface named `name + "Tables"`), `tables()`
(`Class<?>[]` of the `@DaobabTable` definition interfaces, optional), `tablesPackage()` (scan a whole package,
optional), `targetPackage()` (default: the annotated element's package). When **neither** `tables` nor
`tablesPackage` is set, the annotated element's own package is scanned by default and a `NOTE` is logged
(`note()` → `Diagnostic.Kind.NOTE`) — handy when the config sits next to its definitions.

- The **same** `DaobabEntityProcessor` handles it (added to `getSupportedAnnotationTypes()`). After the entity
  pass, `process()` calls `writeTablesInterface()` for each `@DaobabDataBase` element, passing a
  `Map<package, List<definition>>` index of the round's `@DaobabTable` elements it built in the first loop.
- `tables()` is read via **`MirroredTypesException`** (`tableMirrors()`) — the referenced defs are being
  compiled, so they aren't available as `Class` objects. Each must be an interface annotated `@DaobabTable`,
  else compilation fails.
- `tablesPackage()` (or the current-package default) picks up every `@DaobabTable` interface of that package
  **from the round index** (not from the classpath — source-retention means only same-compilation defs are
  visible), added after the explicit `tables()` and **sorted alphabetically** for a stable output; duplicates
  across the sources are deduplicated by FQN (`definitionsInPackage()` + `addNew()`). An **explicit** empty
  `tablesPackage` is an error; the current-package default finding nothing is also an error (distinct message).
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

Entities get an `Entity` suffix and extend `DtoTable<E, D>`; the Java DTO is generated as a **`record`** — one
component per column, **in column order**, so its canonical constructor lines up with a query's selected columns
and the DTO can be read straight through `DataBaseTargetLogic.readRecord`/`readRecordList` (deliberately **no
Lombok** — Daobab must not force the dependency). For backward compatibility the record still carries `getXxx()`
getters and a static `builder()` (the entity's `fromDto` uses the getters, `toDto` uses the builder), and keeps
equality on the **single** primary key (a composite key or none uses the record's default all-component equality,
so no `equals`/`hashCode` override is emitted). Kotlin path emits a `data class` DTO (its primary constructor is
the all-args ctor); TypeScript path unchanged. Reference pattern: `ModelItemEntity`/`ModelItem` in the sibling
project `E:\IdeaProjects\item-collector`.

- Generator side: `GenerateDto` fills `JavaTemplates.DTO_CLASS_TEMP` (now a `record` shape) — note the template
  is a compile-time constant inlined into `TemplateProvider`, so after editing it **`mvn clean`** is needed
  (incremental builds keep the old inlined value). Processor side: `DaobabEntityProcessor.writeDto` emits the
  same record shape directly.

### Composite primary keys (both paths)

Several `primaryKey = true` columns (or a multi-column JDBC PK) produce, in **both** the generator and the
processor, an extra **`XxxKey` interface** next to the entity: it extends every key column interface plus the
`Composite<E>` marker, bounds its type parameter (`<E extends Entity & ColA<E> & ColB<E>>`) and groups the key
columns in `default CompositeColumns<XxxKey<E>> compositeXxxKey()`. The entity then implements
`XxxKey<Entity>` (first in the list) and `PrimaryCompositeKey<Entity, XxxKey<Entity>>` instead of
`PrimaryKey`, overriding `colCompositeId()` to return that group. Key naming: base name + `Key`, numeric
counter on a clash (`Writer.createCompositeKeyName` / processor `compositeKeyName()`). The DTO of a
composite-key table bases equality on **all fields** (no single id). `GenerateDefinition` marks every key
column with `primaryKey = true`, so the definitions-only mode round-trips through the processor.

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
