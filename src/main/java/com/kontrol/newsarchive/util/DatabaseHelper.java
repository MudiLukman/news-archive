package com.kontrol.newsarchive.util;

import javafx.application.Platform;

import java.sql.*;
import java.util.concurrent.CountDownLatch;

public class DatabaseHelper {

    private static final String Jdbc_driver = "com.mysql.cj.jdbc.Driver";
    private static final String Connection_string = "jdbc:mysql://localhost/newsarchive?useUnicode=true&useJDBC"
            + "CompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static final String CREATE_DESK_OFFICER_TABLE = "CREATE TABLE if not exists deskofficer(username VARCHAR(512) not null, "
            + "password VARCHAR(512), intervals VARCHAR(20), aggregatorname VARCHAR(512), "
            + "aggregatorurl VARCHAR(512), constraint pk_deskofficer primary key (username));";

    public static final String CREATE_OLD_URL_TABLE = "CREATE TABLE if not exists oldurl(url VARCHAR(512) not null, date VARCHAR(512), "
            + "constraint pk_oldurl primary key (url));";

    public static final String CREATE_NEWS_WIRE_TABLE = "CREATE TABLE if not exists newswire(url VARCHAR(512) not null, "
            + "constraint pk_newswire primary key (url));";

    public static final String CREATE_KEYWORD_TABLE = "CREATE TABLE if not exists keyword(value VARCHAR(512) not null, "
            + "constraint pk_keyword primary key (value));";

    static Connection con = null;
    static Statement stmt = null;

    public static void connect(){
        try {
            Class.forName(Jdbc_driver);
            System.out.println("Driver Loaded");
            con = DriverManager.getConnection(Connection_string, "kontrol", "smallcode");
            System.out.println("Connection established");
            stmt = con.createStatement();

        }catch (ClassNotFoundException | SQLException e){
            Platform.runLater(() -> AlertMaker.showErrorMessage("Error", "Unable to establish connection"));
        }
    }

    public static void create_table(String table_name){
        try{

            stmt.executeUpdate(table_name);

        }catch (SQLException e){
            Platform.runLater(() -> AlertMaker.showErrorMessage("Error", e.getMessage()));
        }catch (NullPointerException e){
            Platform.runLater(() -> {
                AlertMaker.showErrorMessage("Error", e.getMessage());
                System.exit(1);
            });
        }
    }

    public static void create_all_tables(){
        connect();
        create_table(CREATE_DESK_OFFICER_TABLE);
        create_table(CREATE_OLD_URL_TABLE);
        create_table(CREATE_NEWS_WIRE_TABLE);
        create_table(CREATE_KEYWORD_TABLE);
        disconnect();
    }

    public static void disconnect(){
        if(con != null){
            try {

                stmt.close();
                con.close();

            }catch (SQLException e){
                Platform.runLater(() -> {
                    AlertMaker.showErrorMessage("Database Error", "Could not disconnect");
                });
            }
        }
    }

    public static ResultSet getUserNamePassword_admin()
    {
        String sql="Select * from deskofficer";
        return executeQuery(sql);
    }

    public static ResultSet executeQuery(String sql)
    {
        connect();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery(sql);
        }catch (SQLSyntaxErrorException e){
            System.out.println(e);
        }
        catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

        return rs;
    }

    public static int delete_record(String sql){
        int val = 0;
        try {
            connect();
            val = stmt.executeUpdate(sql);
        } catch( SQLException e ) {
            System.out.println(e.getMessage());
        }
        return val;
    }

    public static int insertNewswire(String url, String iconPath, String name){

        String insertNewswireSource = "INSERT INTO newswire values(?,?,?)";

        PreparedStatement ps = null;
        int val = 0;

        try{
            connect();

            ps = con.prepareStatement(insertNewswireSource);
            ps.setString(1, url);
            ps.setString(2, iconPath);
            ps.setString(3, name);

            val = ps.executeUpdate();

            disconnect();
        }catch(SQLException e){
            Platform.runLater(()-> AlertMaker.showErrorMessage(e));
        }

        return val;
    }

    public static int insert_record(String sql) {
        int val = 0;
        try {
            connect();
            val = stmt.executeUpdate(sql);
        } catch ( SQLException e ) {
            System.out.println(e.getMessage());
        }
        return val;
    }

}
