package io.daobab.target.database.converter.dateformat;

import io.daobab.dict.DictDatabaseType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UniversalDateConverter {

    private final static String APOSTROPHE = "'";


    public static StringBuilder toSQLDate(String dataBaseType, Date value) {
        StringBuilder sb = new StringBuilder();
        String dateAsString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (DictDatabaseType.ORACLE.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            sb.append(" TO_DATE('").append(dateAsString).append("','YYYY-MM-DD HH24:MI:SS') ");
            return sb;
        } else if (DictDatabaseType.MYSQL.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            sb.append(" STR_TO_DATE('").append(dateAsString).append("', '%Y-%m-%d %H:%i:%s')");
            return sb;
        } else if (DictDatabaseType.H2.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            sb.append(APOSTROPHE).append(dateAsString).append(APOSTROPHE);
            return sb;
        } else if (DictDatabaseType.PostgreSQL.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            sb.append(" to_date('").append(dateAsString).append("', 'YYYY-MM-DD HH24:MI:SS')");
            return sb;
        } else if (dataBaseType.startsWith(DictDatabaseType.MicrosoftSQL)) {
            SimpleDateFormat dateFormatMsSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dateAsString = dateFormatMsSql.format(value);
            sb.append(" convert(DATETIME,'").append(dateAsString).append("')");
            return sb;
        } else {
            dateAsString = dateFormat.format(value);
            sb.append(APOSTROPHE).append(dateAsString).append(APOSTROPHE);
            return sb;
        }
    }

    public static StringBuilder toTimestampDate(String dataBaseType, Date value) {
        StringBuilder sb = new StringBuilder();
        String dateAsString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (DictDatabaseType.ORACLE.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            return sb.append(" TO_DATE('").append(dateAsString).append("','YYYY-MM-DD HH24:MI:SS') ");
        } else if (DictDatabaseType.MYSQL.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            return sb.append(" STR_TO_DATE('").append(dateAsString).append("', '%Y-%m-%d %H:%i:%s')");
        } else if (DictDatabaseType.H2.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            return sb.append(APOSTROPHE).append(dateAsString).append(APOSTROPHE);
        } else if (DictDatabaseType.PostgreSQL.equals(dataBaseType)) {
            dateAsString = dateFormat.format(value);
            return sb.append(" to_date('").append(dateAsString).append("', 'YYYY-MM-DD HH24:MI:SS')");
        } else if (dataBaseType.startsWith(DictDatabaseType.MicrosoftSQL)) {
            SimpleDateFormat dateFormatMsSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dateAsString = dateFormatMsSql.format(value);
            return sb.append(" convert(DATETIME,'").append(dateAsString).append("')");
        } else {
            dateAsString = dateFormat.format(value);
            return sb.append(APOSTROPHE).append(dateAsString).append(APOSTROPHE);
        }
    }
}
