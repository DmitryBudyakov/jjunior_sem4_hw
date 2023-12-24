package jjunior.sem4.hw;

import java.sql.*;

public class DbOperation {


    /**
     * Создание базы данных
     * @param connection
     * @param database
     * @throws SQLException
     */
    public static void createDatabase(Connection connection, String database) throws SQLException {
        String createDatabaseSQL = String.format("CREATE DATABASE IF NOT EXISTS %s;", database);
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)){
            statement.execute();
        }
    }

    /**
     * Создание таблицы в базе данных
     * @param connection
     * @param database
     * @param table
     * @param tableSQL
     * @throws SQLException
     */
    public static void createTable(Connection connection, String database, String table, String tableSQL) throws SQLException {
        String createTableSQL =  String.format("CREATE TABLE IF NOT EXISTS %s.%s (%s);", database, table, tableSQL);
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    public static void useDatabase(Connection connection, String database) throws SQLException {
        String useDatabaseSQL = String.format("USE %s;", database);
        try (PreparedStatement statement = connection.prepareStatement(useDatabaseSQL)) {
            statement.execute();
        }
    }




}
