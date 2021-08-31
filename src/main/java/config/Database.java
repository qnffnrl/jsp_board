package config;

import java.sql.Connection;
import java.sql.DriverManager;


public class Database
{
  private static String dbHost = "db.asin21.com";
  private static String dbUser = "demo07";
  private static String dbPass = "demo07!";
  private static String dbName = "db_demo07";
  
  private static String dbDriver = "com.mysql.jdbc.Driver";
  private static String dbUrl = "jdbc:mysql://" + dbHost + "/" + dbName + "?characterEncoding=UTF-8";

  
  public static Connection getConnection() {
    try {
      Class.forName(dbDriver);
      return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
}
