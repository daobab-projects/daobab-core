# DAOBAB #

[Daobab](http://www.daobab.io) is a Java ORM (Object Relational Mapping) library, allowing to build an SQL as fully
object-oriented Java code,
which may be used on:

* database
* collections
* buffers
* remotely

Daobab uses Java8 features to recreate the relations between entities into object-oriented code.

### Example query

    public List<Customer> example() {
        return db.select(tabCustomer)
                .where(and()
                        .equal(tabCustomer.colActive(), true)
                        .equal(tabCustomer.colLastName(), "WILSON"))
                .limitBy(100)
                .orderAscBy(tabCustomer.colLastName())
                .findMany();
    }


### Building from sources ###

The build requires at least Java 8 JDK as JAVA_HOME.

### Repository content ###

* daobab-core - [Daobab](http://www.daobab.io) ORM sources

### License Apache License 2.0 ###

http://www.apache.org/licenses/LICENSE-2.0

### detailed information and examples ###

* http://www.daobab.io
* contact@daobab.io
