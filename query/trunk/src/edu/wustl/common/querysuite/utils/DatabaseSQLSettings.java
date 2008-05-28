package edu.wustl.common.querysuite.utils;

public class DatabaseSQLSettings {
    private DatabaseType databaseType;

    private String dateFormat;

    public DatabaseSQLSettings(DatabaseType databaseType, String dateFormat) {
        this.databaseType = databaseType;
        this.dateFormat = dateFormat;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public String getDateFormat() {
        return dateFormat;
    }

}
