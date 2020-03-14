package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Logger logger = LogManager.getLogger(DBManager.class);
    private Connection connection;
    private Statement statement;

    public DBManager() throws ClassNotFoundException, SQLException {
        String schemaName = "web_crawler";
        String MySQLServerURL= String.format("jdbc:mysql://127.0.0.1:3306/%s?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT", schemaName);
        String username = "root";
        String password = "1234";

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(MySQLServerURL, username, password);
        statement = connection.createStatement();
        logger.debug("DBManager initialized, connection established");
    }

    public void saveToDatabase(Headline headline) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO headlines (url, content, descr, lang, timeRangeStart, timeRangeStop) VALUES (?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, headline.getURL());
            preparedStatement.setString(2, headline.getContent());
            preparedStatement.setString(3, headline.getDescription());
            preparedStatement.setString(4, headline.getLanguage());

            try {
                preparedStatement.setDate(5, Date.valueOf(headline.getTimeRangeStart()));
                preparedStatement.setDate(6, Date.valueOf(headline.getTimeRangeStop().toString()));
            }catch (NullPointerException e){
                preparedStatement.setDate(5, null);
                preparedStatement.setDate(6, null);
            }

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

    public List<Headline> readFromDatabase(){
        List<Headline> headlines = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * from headlines");

            while (resultSet.next()){
                String url = resultSet.getString("url");
                String content = resultSet.getString("content");
                String description = resultSet.getString("descr");
                String language = resultSet.getString("lang");
                LocalDate timeRangeStart = null;
                LocalDate timeRangeStop = null;

                try {
                    timeRangeStart = resultSet.getDate("timeRangeStart").toLocalDate();
                    timeRangeStop = resultSet.getDate("timeRangeStop").toLocalDate();
                } catch (NullPointerException e){

                }

                Headline receivedHeadline = new Headline(url, content, description, language, timeRangeStart, timeRangeStop);
                headlines.add(receivedHeadline);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return headlines;
    }

    public void closeConnection(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
