# Daobab

**Object-oriented SQL for Java 21.** Daobab is an ORM in which a query *is* Java code — no JPQL strings, no
`@Query`, no Criteri
a builders, no XML. The same query object runs against a JDBC database, an in-memory
collection, an off-heap buffer, or a remote target. One API, four back-ends, zero magic.

```java
List<Customer> wilsons = db.select(tabCustomer)
        .where(and()
                .equal(tabCustomer.colActive(), true)
                .equal(tabCustomer.colLastName(), "WILSON"))
        .orderAscBy(tabCustomer.colLastName())
        .limitBy(100)
        .findMany();
```

- **Java 21**, Maven, Apache License 2.0.
- **Essentially no runtime dependencies** — the only compile-scope dependency is `slf4j-api`. No Jakarta stack,
  no Hibernate transitive tree, no bytecode weaving agent.
- **No runtime reflection for data access, no proxies, no lazy-loading surprises.** Entities are plain
  map-backed objects; column handles are cached once. What you write is what runs.
- Two code-generation paths (a pure-JDK annotation processor **and** a JDBC reverse-engineering generator that
  also emits Kotlin and TypeScript) that are kept behaviourally identical.

---

## Why another ORM?

If you have shipped enough JPA, you know the failure modes: queries hidden in strings that no refactor touches,
`LazyInitializationException`, first-level-cache aliasing, N+1 explosions from innocent getters, a Criteria API
so verbose nobody uses it, and a runtime that reflects and proxies everything. jOOQ fixes type safety but is
generated-code-heavy and database-only; MyBatis pushes you back into SQL-in-XML.

Daobab takes a different bet: **make the column the unit of reuse, and make the whole query an ordinary,
refactor-safe Java expression.** The result is a query language your IDE fully understands — autocomplete,
find-usages, rename, and *compile errors* when a column or type is wrong.

|                                                    | Daobab                          | JPA / Hibernate             | jOOQ      | MyBatis  |
|----------------------------------------------------|---------------------------------|-----------------------------|-----------|----------|
| Query is type-checked Java (no strings)            | ✅ whole query                   | ⚠️ Criteria only (verbose)  | ✅         | ❌        |
| Runs unchanged on DB **and** in-memory collections | ✅                               | ❌ DB only                   | ❌ DB only | ❌        |
| Runtime reflection / proxies for entities          | ❌ none                          | ✅ heavy                     | ❌         | ⚠️       |
| Runtime dependencies                               | slf4j-api only                  | large                       | moderate  | moderate |
| Lazy-loading / N+1 by accident                     | impossible (joins are explicit) | common                      | n/a       | n/a      |
| Pre-compiled ("frozen") queries                    | ✅                               | ⚠️ prepared-statement cache | ❌         | ❌        |
| Generates Java **+ Kotlin + TypeScript**           | ✅                               | ❌                           | ⚠️ Java   | ❌        |
| Off-heap storage for millions of rows              | ✅                               | ❌                           | ❌         | ❌        |

---

## The core idea: a column is a shared interface

This is the design decision everything else follows from.

In JPA, a `firstName` field is redeclared inside every entity that has one; the mapping metadata is duplicated
and the two `firstName`s are unrelated. In Daobab a column is a **Java interface**, defined once, and every
entity that has that column simply `implements` it:

```java
public interface FirstName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {
  default String getFirstName() {
    return readParam("FirstName");
  }

  default E setFirstName(String v) {
    return storeParam("FirstName", v);
  }
    default Column<E, String, FirstName> colFirstName() { /* cached column handle */ }
}
```

An entity is the intersection of the columns it carries plus a primary-key marker:

```java
@TableInformation(name = "ACTOR")
public class Actor extends Table<Actor> implements
        ActorId<Actor>, FirstName<Actor>, LastName<Actor>, LastUpdate<Actor>,
        PrimaryKey<Actor, Integer, ActorId> {
    // columns() lists the TableColumn metadata; generated for you
}
```

Because the *same* `FirstName` interface is shared by `Actor`, `Customer`, `Staff`, … you get:

- **one place** to define a column's name, Java type and accessors;
- **cross-entity, type-safe column references** — a method that takes `Column<?, String, FirstName>` accepts the
  first name of any table that has one;
- entities that are **structurally typed** by their columns, which is exactly what makes the generic query
  builder possible.

`Table<E>` is a map under the hood, so reads and writes are `HashMap` operations — there is no reflection and no
generated bytecode on the hot path. `DaobabCache` interns each `Column` handle once per entity class.

---

## Queries

### Filtering, ordering, paging

```java
db.select(tabFilm.colTitle(),tabFilm.

colLength())
        .

where(and()
          .

greater(tabFilm.colLength(), 90)
        .

in(tabFilm.colRating(), "PG","PG-13"))
        .

orderDescBy(tabFilm.colLength())
        .

limitBy(25)
  .

findMany();
```

Convenience shorthands exist for the common single-condition cases — every one of them binds parameters as `?`
placeholders in the produced SQL, so injection is not on the table:

```java
db.select(tabFilm.colTitle()).

whereEqual(tabFilm.colTitle(), "MATRIX").

findMany();
db.

select(tabFilm.colTitle()).

whereLike(tabFilm.colTitle(), "%MAT%").

findFirst();
db.

select(tabFilm.colTitle()).

whereIn(tabFilm.colLength(), 90,120,150).

findMany();
db.

select(tabFilm.colTitle()).

whereIsNull(tabFilm.colDescription()).

findMany();
db.

select(tabFilm.colTitle()).

whereOr(or ->or
        .

equal(tabFilm.colRating(), "PG")
        .

equal(tabFilm.colRating(), "G")).

findMany();
```

### Joins — always explicit, never accidental

There is no lazy proxy that fires a query when you call a getter. You state the join, and Daobab can also infer
it from the columns involved:

```java
// join on an explicit foreign-key column
db.select(tabRental)
  .

join(tabInventory, tabRental.colInventoryId())
        .

findMany();

// join straight onto the joined table's primary key
db.

select(tabRental).

joinPk(tabInventory).

findMany();
```

### Aggregates, functions and subqueries

```java
db.select(tabFilm.colRating(),count(tabFilm.

colID()).

as("cnt",Long .class))
        .

groupBy(tabFilm.colRating())
        .

findMany();

db.

select(sumRows(tabFilm.colRentalRate(),tabFilm.

colReplacementCost()).

as("total"))
        .

groupBy(tabFilm.colLanguageId())
        .

findMany();

// subqueries are just queries used as columns
db.

select(
        db.select(count(tabFilm)).

as("films"),
        db.

select(count(tabCustomer)).

as("customers"))
        .

findFirst();
```

### CRUD — the entity is its own repository

A `PrimaryKey` entity carries its own persistence methods, so you rarely need a DAO layer:

```java
Actor a = new Actor().setFirstName("NICK").setLastName("WAHLBERG");
a.

insert(db);                       // INSERT
a.

setLastName("MARK").

update(db);   // UPDATE by id
a.

delete(db);                       // DELETE by id

Actor loaded = new Actor().findById(db, 5);
```

### Read the SQL any query would run

Every query can render its own SQL without executing it — priceless for logging, review, migration and debugging.
You build the statement object-orientedly and read out the string:

```java
String sql = db.select(tabFilm.colTitle())
        .whereEqual(tabFilm.colRating(), "PG")
        .toSqlQuery();
// select ihs1.TITLE from FILM ihs1 where ihs1.RATING = ?
```

The producer is dialect-aware, so the very same query renders correctly against MySQL, H2, PostgreSQL, SQL
Server, and the rest — you switch dialects by switching the target, not by editing SQL.

---

## Projections with `Plate`

Not every read maps cleanly onto one entity. When you select a handful of columns — possibly across several
joined tables — Daobab returns **`Plates`**: lightweight, typed projection rows. A `Plate` is one result row keyed
by entity, so the *same* column name can coexist across joined tables without clashing.

```java
Plates rows = db.select(tabPayment.colAmount(), tabCustomer.colLastName())
        .join(tabCustomer, tabPayment.colCustomerId())
        .findMany();

for(
Plate row :rows){
BigDecimal amt = row.getValue(tabPayment.colAmount());     // typed, no casting
String name = row.getValue(tabCustomer.colLastName());
}
```

A `Plate` can also **rebuild whole entities** from its cells, or be projected straight into any entity type — a
clean way to load exactly the columns you need and still work with objects:

```java
Customer c = row.getEntity(Customer.class);                     // reconstruct an entity from the row

// project the selected columns straight into a (possibly different) entity type
List<Customer> slim = db.select(tabCustomer.colCustomerId(), tabCustomer.colLastName())
        .findManyAs(Customer.class);
```

Plates serialize to JSON out of the box and can be flattened (`toFlat()`), which makes them a natural fit for
REST responses without an extra mapping layer.

---

## Frozen queries — build the SQL once, run it forever

Building SQL from an object graph costs a little on every call. For your hottest statements, **freeze** them:
Daobab renders the SQL a single time, inlines the constant parts, and leaves typed placeholders
(`DaoParam.param(n)`) for the values you supply at run time. Executing a frozen query skips the whole
build/analyse pipeline — it is essentially a pre-baked prepared statement with a Daobab-typed API.

```java
// build once (e.g. at startup) — the SQL is rendered and cached on the query object
FrozenDataBaseQueryField<Film, String> topFilms = db.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colRating(), "PG")
                .limitBy(DaoParam.param(1))
                .freezeQuery();

// run many times on the hot path — just inject the parameters, no SQL rebuilding
List<String> firstPage = topFilms.withParameters(List.of(25)).findMany();
List<String> hugePage = topFilms.withParameters(List.of(1000)).findMany();
```

Frozen queries can also **cache their own results** for a period — a per-query, opt-in read cache with none of the
surprises of a global first-level cache:

```java
FrozenDataBaseQueryEntity<Language> languages = db.select(tabLanguage)
        .freezeQuery()
        .cacheResultsForPeriod(Duration.ofMinutes(10));

Entities<Language> cached = languages.findMany();   // served from cache within the window
```

---

## DTOs and automatic conversion — without Lombok

Entities can be paired with an immutable DTO (`DtoTable<E, D>`). Daobab generates a plain-Java, final, builder-
backed DTO **and** the two-way conversion bridge — deliberately **without** Lombok, ModelMapper or MapStruct,
because a library should not force a dependency or a reflective mapper on you. Conversion is generated,
straight-line field assignment:

```java
Book dto = bookEntity.toDto();                 // entity  -> immutable DTO (equals/hashCode on the PK)
BookEntity back = BookEntity.fromDto(dto);      // DTO      -> entity
Book built = Book.builder().title("Dune").pages(412).build();
```

Because the bridge is just methods, turning a query result into DTOs is a one-liner — no mapper configuration:

```java
List<Book> catalog = db.select(tabBook)
        .where(and().equal(tabBook.colActive(), true))
        .findMany()
        .stream().map(BookEntity::toDto).toList();
```

The Kotlin generator emits a `data class` DTO instead; the TypeScript generator emits an interface, so your
front-end and back-end can share one source of truth for the schema.

---

## Code generation — two aligned paths

You never hand-write the column interfaces or entities. Daobab produces them two ways, kept behaviourally
identical (change one, change the other — enforced by mirrored tests).

### 1. Compile-time annotation processor (pure JDK, zero third-party libs)

Describe a table as an annotated interface; the processor generates the entity, the shared column interfaces and
the DTO **during compilation**:

```java
@DaobabTable(tableName = "BOOK")
public interface BookDef {

    @DaobabColumn(primaryKey = true)
    Integer bookId();

    @DaobabColumn(name = "TITLE", size = 256, notNull = true)
    String title();

    Timestamp printDate();
    int pages();
}
```

Gather the tables of a schema into one initialized `Tables` interface — the ergonomic `tabBook` /
`tabCustomer` handles the queries above use:

```java
@DaobabDataBase(name = "Library", tablesPackage = "com.acme.library.dao")
public interface LibraryDataBase {
}
// generates LibraryTables with `BookEntity tabBook = new BookEntity();`, documented with the table schema
```

Highlights of the processor:

- **Shared column interfaces** are reused across tables; a name clash with a *different* type is disambiguated
  with a type suffix (`Title` → `TitleTypeInteger`) instead of failing.
- **Composite keys are first-class**: several `primaryKey = true` columns generate a `XxxKey` grouping interface
  plus `PrimaryCompositeKey` wiring on the entity.
- **Per-column type converters** — pin a `DatabaseTypeConverter` to a column and Daobab validates *at compile
  time* that the converter's type matches the field (an `Integer` converter on a `LocalDateTime` column will not
  compile):

  ```java
  @DaobabColumn(typeConverterClass = MoneyConverter.class)
  BigDecimal amount();
  ```

### 2. Runtime generator (JDBC reverse-engineering → Java / Kotlin / TypeScript)

Point it at a live `DataSource` and it reads the JDBC metadata and writes ready sources — including a rich
Javadoc schema table above every column and every table handle. Great for bootstrapping an existing database.
A "definitions-only" mode emits the `@DaobabTable` interfaces above, so you can start from JDBC and continue with
the compile-time processor.

---

## Getting started

### Maven

```xml
<dependency>
    <groupId>io.daobab</groupId>
    <artifactId>daobab-core</artifactId>
    <version>1.8.3</version>
</dependency>
```

The annotation processor ships inside the same jar and registers itself via `META-INF/services`. (Under the
Spring Boot parent, which pins `annotationProcessorPaths`, declare `io.daobab:daobab-core` there explicitly.)

### Define a database target

A target binds the query engine to a `DataSource` and the set of entities it manages:

```java
public class LibraryDb extends DataBaseTarget {

    private final DataSource dataSource;

  public LibraryDb(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  protected DataSource initDataSource() {
    return dataSource;
  }

  @Override
  protected List<Entity> initTables() {
        return List.of(new Book(), new Author());
    }
}
```

Then query:

```java
LibraryDb db = new LibraryDb(myDataSource);
List<Book> active = db.select(tabBook)
        .where(and().equal(tabBook.colActive(), true))
        .orderAscBy(tabBook.colTitle())
        .findMany();
```

### Spring Boot

There is a companion **`spring-boot-starter-daobab`** that auto-configures a `DataBaseTarget` on top of the
Spring-managed `DataSource`, discovers your entities, and can even reverse-engineer an existing schema into
`@DaobabTable` definitions on first run.

---

## One query API, four targets

`db` above is any `Target`. The identical query object runs against every back-end because the database target
(`QueryTarget`) and the buffer target (`BufferQueryTarget`) expose the same `select / where / findMany` surface.

```java
// (1) a JDBC database
List<Film> fromDb = db.select(tabFilm).whereEqual(tabFilm.colRating(), "PG").findMany();

// (2) an in-memory collection — SAME query code, no SQL involved
Entities<Film> films = loadFilms();               // an ordinary List<Film>, and a Target
List<Film> fromRam = films.select(tabFilm)
        .whereEqual(tabFilm.colRating(), "PG")
        .findMany();
```

- **Database** — JDBC, dialect-aware SQL producer, prepared statements, transactions with propagation.
- **Collections** — run the query engine over a `List<E>` in memory; ideal for tests and caches without a second
  query language.
- **Off-heap buffers** (`target.buffer.nonheap`) — store millions of entities *outside* the JVM heap in
  bit-packed columns, so they never enter a GC cycle. Genuinely rare for an ORM, and a real option when a heap
  full of entities would be a problem.
- **Remote** — the same query executed against a remote target.

Switching where a query runs is a matter of which `Target` you hand it — the query is unchanged.

---

## Building from source

```bash
mvn test                                   # full suite
mvn -Dtest=TestDaobabEntityProcessor test  # the annotation processor (compiled with a real JavaCompiler)
mvn -Dtest="io.daobab.generator.*Test" test
```

Requires a Java 21 JDK. The main compile runs with `-proc:none` — Daobab ships its own processor and must not
apply it to itself.

---

## License & links

Apache License 2.0 — <http://www.apache.org/licenses/LICENSE-2.0>

- Website & full documentation: <http://www.daobab.io>
- Contact: contact@daobab.io
- Author: Klaudiusz Wojtkowiak, (C) Elephant Software
