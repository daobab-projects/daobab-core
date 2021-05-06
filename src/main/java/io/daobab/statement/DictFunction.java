package io.daobab.statement;

//TODO: enum?
public interface DictFunction {

    //STRING
    String SUBSTRING = "SUBSTRING";
    String UPPER = "UPPER";
    String LOWER = "LOWER";
    String LENGTH = "LENGTH";
    String CONCAT = "CONCAT";
    String TRIM = "TRIM";
    String REVERSE = "REVERSE";
    String REPLACE = "REPLACE";
    String REPLICATE = "REPLICATE";
    String RIGHT = "RIGHT";
    String LEFT = "LEFT";


    //MATH
    String SUM = "SUM";
    String MIN = "MIN";
    String MAX = "MAX";
    String AVG = "AVG";
    String COUNT = "COUNT";
    String COUNTDISTICT = "COUNT DISTINCT";

    //DATE

    String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    String DATEADD = "DATEADD";
    String DATEDIFF = "DATEDIFF";
    String DATEFROMPARTS = "DATEFROMPARTS";
    String DATENAME = "DATENAME";
    String DATEPART = "DATEPART";
    String DAY = "DAY";
    String GETDATE = "GETDATE";
    String GETUTCDATE = "GETUTCDATE";
    String ISDATE = "ISDATE";
    String MONTH = "MONTH";
    String SYSDATETIME = "SYSDATETIME";
    String YEAR = "YEAR";


    //OTHER

    String CAST = "CAST";
    String DISTINCT = "DISTINCT";

    //INTERNAL
    String INNER_QUERY = "INNER_QUERY";
}
