package daobab.test.dao;import io.daobab.parser.ParserGeneral;import io.daobab.test.dao.table.*;import io.daobab.test.dao.table.enhanced.CountryCity;import io.daobab.test.dao.table.enhanced.EnglishFilm;public interface SakilaTables extends ParserGeneral {    /**     * Comment:<br>     *     * <pre>     *     * <u> Name         Type       Size  DBName       DBType     Description </u>     *  ActorId(PK)  Integer    5   ACTOR_ID     SMALLINT     *  FirstName    String     45  FIRST_NAME   VARCHAR     *  LastName     String     45  LAST_NAME    VARCHAR     *  LastUpdate   Timestamp  26  LAST_UPDATE  TIMESTAMP     * </pre>     */    Actor tabActor = new Actor();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name           Type       Size  DBName       DBType     Description </u>     *  Address        String     50  ADDRESS      VARCHAR     *  Address2       String     50  ADDRESS2     VARCHAR     *  AddressId(PK)  Integer    5   ADDRESS_ID   SMALLINT     *  CityId         Integer    5   CITY_ID      SMALLINT     *  District       String     20  DISTRICT     VARCHAR     *  LastUpdate     Timestamp  26  LAST_UPDATE  TIMESTAMP     *  Phone          String     20  PHONE        VARCHAR     *  PostalCode     String     10  POSTAL_CODE  VARCHAR     * </pre>     */    Address tabAddress = new Address();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name            Type       Size  DBName       DBType     Description </u>     *  CategoryId(PK)  Integer    3   CATEGORY_ID  TINYINT     *  LastUpdate      Timestamp  26  LAST_UPDATE  TIMESTAMP     *  Name            String     25  NAME         VARCHAR     * </pre>     */    Category tabCategory = new Category();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name        Type       Size  DBName       DBType     Description </u>     *  City        String     50  CITY         VARCHAR     *  CityId(PK)  Integer    5   CITY_ID      SMALLINT     *  CountryId   Integer    5   COUNTRY_ID   SMALLINT     *  LastUpdate  Timestamp  26  LAST_UPDATE  TIMESTAMP     * </pre>     */    City tabCity = new City();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name           Type       Size  DBName       DBType     Description </u>     *  Country        String     50  COUNTRY      VARCHAR     *  CountryId(PK)  Integer    5   COUNTRY_ID   SMALLINT     *  LastUpdate     Timestamp  26  LAST_UPDATE  TIMESTAMP     * </pre>     */    Country tabCountry = new Country();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name            Type       Size  DBName       DBType     Description </u>     *  Active          Boolean    1   ACTIVE       BOOLEAN     *  AddressId       Integer    5   ADDRESS_ID   SMALLINT     *  CreateDate      Timestamp  26  CREATE_DATE  TIMESTAMP     *  CustomerId(PK)  Integer    5   CUSTOMER_ID  SMALLINT     *  Email           String     50  EMAIL        VARCHAR     *  FirstName       String     45  FIRST_NAME   VARCHAR     *  LastName        String     45  LAST_NAME    VARCHAR     *  LastUpdate      Timestamp  26  LAST_UPDATE  TIMESTAMP     *  StoreId         Integer    3   STORE_ID     TINYINT     * </pre>     */    Customer tabCustomer = new Customer();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name                Type        Size        DBName                DBType     Description </u>     *  Description         String      2147483647  DESCRIPTION           VARCHAR     *  FilmId(PK)          Integer     5           FILM_ID               SMALLINT     *  LanguageId          Integer     3           LANGUAGE_ID           TINYINT     *  LastUpdate          Timestamp   26          LAST_UPDATE           TIMESTAMP     *  Length              Integer     5           LENGTH                SMALLINT     *  OriginalLanguageId  Integer     3           ORIGINAL_LANGUAGE_ID  TINYINT     *  Rating              String      5           RATING                VARCHAR     *  ReleaseYear         Date        10          RELEASE_YEAR          DATE     *  RentalDuration      Integer     3           RENTAL_DURATION       TINYINT     *  RentalRate          BigDecimal  4           RENTAL_RATE           DECIMAL     *  ReplacementCost     BigDecimal  5           REPLACEMENT_COST      DECIMAL     *  SpecialFeatures     String      54          SPECIAL_FEATURES      VARCHAR     *  Title               String      255         TITLE                 VARCHAR     * </pre>     */    Film tabFilm = new Film();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name        Type       Size  DBName       DBType     Description </u>     *  ActorId     Integer    5   ACTOR_ID     SMALLINT     *  FilmId(PK)  Integer    5   FILM_ID      SMALLINT     *  LastUpdate  Timestamp  26  LAST_UPDATE  TIMESTAMP     * </pre>     */    FilmActor tabFilmActor = new FilmActor();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name        Type       Size  DBName       DBType     Description </u>     *  CategoryId  Integer    3   CATEGORY_ID  TINYINT     *  FilmId(PK)  Integer    5   FILM_ID      SMALLINT     *  LastUpdate  Timestamp  26  LAST_UPDATE  TIMESTAMP     * </pre>     */    FilmCategory tabFilmCategory = new FilmCategory();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name         Type     Size        DBName       DBType    Description </u>     *  Description  String   2147483647  DESCRIPTION  VARCHAR     *  FilmId(PK)   Integer  5           FILM_ID      SMALLINT     *  Title        String   255         TITLE        VARCHAR     * </pre>     */    FilmText tabFilmText = new FilmText();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name             Type        Size  DBName        DBType     Description </u>     *  FilmId           Integer     5   FILM_ID       SMALLINT     *  InventoryId(PK)  BigDecimal  10  INVENTORY_ID  INTEGER     *  LastUpdate       Timestamp   26  LAST_UPDATE   TIMESTAMP     *  StoreId          Integer     3   STORE_ID      TINYINT     * </pre>     */    Inventory tabInventory = new Inventory();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name            Type       Size  DBName       DBType     Description </u>     *  LanguageId(PK)  Integer    3   LANGUAGE_ID  TINYINT     *  LastUpdate      Timestamp  26  LAST_UPDATE  TIMESTAMP     *  Name            String     20  NAME         VARCHAR     * </pre>     */    Language tabLanguage = new Language();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name           Type        Size  DBName        DBType     Description </u>     *  Amount         BigDecimal  5   AMOUNT        DECIMAL     *  CustomerId     Integer     5   CUSTOMER_ID   SMALLINT     *  LastUpdate     Timestamp   26  LAST_UPDATE   TIMESTAMP     *  PaymentDate    Timestamp   26  PAYMENT_DATE  TIMESTAMP     *  PaymentId(PK)  Integer     5   PAYMENT_ID    SMALLINT     *  RentalId       BigDecimal  10  RENTAL_ID     INTEGER     *  StaffId        Integer     3   STAFF_ID      TINYINT     * </pre>     */    Payment tabPayment = new Payment();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name          Type        Size  DBName        DBType     Description </u>     *  CustomerId    Integer     5   CUSTOMER_ID   SMALLINT     *  InventoryId   BigDecimal  10  INVENTORY_ID  INTEGER     *  LastUpdate    Timestamp   26  LAST_UPDATE   TIMESTAMP     *  RentalDate    Timestamp   26  RENTAL_DATE   TIMESTAMP     *  RentalId(PK)  BigDecimal  10  RENTAL_ID     INTEGER     *  ReturnDate    Timestamp   26  RETURN_DATE   TIMESTAMP     *  StaffId       Integer     3   STAFF_ID      TINYINT     * </pre>     */    Rental tabRental = new Rental();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name         Type       Size        DBName       DBType     Description </u>     *  Active       Boolean    1           ACTIVE       BOOLEAN     *  AddressId    Integer    5           ADDRESS_ID   SMALLINT     *  Email        String     50          EMAIL        VARCHAR     *  FirstName    String     45          FIRST_NAME   VARCHAR     *  LastName     String     45          LAST_NAME    VARCHAR     *  LastUpdate   Timestamp  26          LAST_UPDATE  TIMESTAMP     *  Password     String     40          PASSWORD     VARCHAR     *  Picture      Object     2147483647  PICTURE      VARBINARY     *  StaffId(PK)  Integer    3           STAFF_ID     TINYINT     *  StoreId      Integer    3           STORE_ID     TINYINT     *  Username     String     16          USERNAME     VARCHAR     * </pre>     */    Staff tabStaff = new Staff();    /**     * Comment:<br>     *     * <pre>     *     * <u> Name            Type       Size  DBName            DBType     Description </u>     *  AddressId       Integer    5   ADDRESS_ID        SMALLINT     *  LastUpdate      Timestamp  26  LAST_UPDATE       TIMESTAMP     *  ManagerStaffId  Integer    3   MANAGER_STAFF_ID  TINYINT     *  StoreId(PK)     Integer    3   STORE_ID          TINYINT     * </pre>     */    Store tabStore = new Store();    //===================================    /**     * Enhanced Entity     */    EnglishFilm tabEnglishFilm = new EnglishFilm();    /**     * Enhanced Entity     */    CountryCity tabCountryCity = new CountryCity();}