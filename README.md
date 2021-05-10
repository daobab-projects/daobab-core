# DAOBAB #

Daobab is a Java ORM (Object Relational Mapping) library, allowing to build an SQL as fully object-oriented Java code, which may be used on:

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

* daobab-core - Daobab ORM sources

### License CC-BY-NC-ND ###

* Producer keeps the author rights.
* You may use if for any non-commercial purposes.
* You may modify the source code, but for your own purposes only.
  Modified library cannot be shared.

### detailed information and contact ###

* http://www.daobab.io
* contact@daobab.io