package edu.wustl.common.querysuite.utils;

/**
 * Represents database specific settings that are used while building SQL.
 * 
 * @author srinath_k
 */
public class DatabaseSQLSettings {
    private DatabaseType databaseType;

    private String dateFormat;

    /**
     * @param databaseType the database type
     * @param dateFormat the format in which date literals are specified.
     */
    public DatabaseSQLSettings(DatabaseType databaseType, String dateFormat) {
        this.databaseType = databaseType;
        this.dateFormat = dateFormat;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    /**
     * @return the format in which date literals are specified.
     */
    public String getDateFormat() {
        return dateFormat;
    }

}
