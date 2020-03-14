package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DBManager {
    private static Logger logger = LogManager.getLogger(DBManager.class);
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DBManager() throws ClassNotFoundException, SQLException {
        String URL= "jdbc:mysql://127.0.0.1:3306/web_crawler?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";
        String username = "root";
        String password = "1234";

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(URL, username, password);
        statement = connection.createStatement();
        logger.debug("DBManager initialized, connection established");
    }

    public void saveToDatabase(Headline headline) {
        try {
            //statement.executeUpdate("INSERT INTO headlines (url, content, descr) VALUES ('"+headline.getURL()+"','"+headline.getContent()+"','"+headline.getDescription()+"');");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO headlines (url, content, descr) VALUES (?, ?, ?);");
            preparedStatement.setString(1, headline.getURL());
            preparedStatement.setString(2, headline.getContent());
            preparedStatement.setString(3, headline.getDescription());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase(){
        try {
            statement.executeUpdate("DELETE FROM headlines;");
            logger.info("Data from table successfully deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //todo: clearTable()
    //todo: search how to make values literal meaning that VARCHAR content does not interfere with sql statement

}
