package ua.edu.ucu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CachedDocument extends SmartDocument {
    private static String path;

    public CachedDocument(String gcsPath) {
        super(gcsPath);
        path = gcsPath;
    }

    public static void checkCache(String newText) {
        ArrayList<String> text = new ArrayList<>();
        Connection connection = null;

        try {
            // find connection with database
            connection = DriverManager.getConnection("jdbc:sqlite:/lab10/documents.db");
            String query = "SELECT * FROM docs WHERE content = ";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query + path);
            
            // write all texts that we have in db
            while (resultSet.next()) {
                text.add(resultSet.getString("content"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        if (!text.isEmpty()) {
            int numRows = 0;
            for (String elem : text) {
                if (elem == newText) {
                    System.out.println("This text is already in database. No need to write it again.");
                    break;
                }
                numRows++;
            }

            // if thereis no such text in database
            String newQuery = "INSERT INTO docs (id, content, gcsPath) VALUES (?, ?, ?)";
            try (
                PreparedStatement preparedStatement = connection.prepareStatement(newQuery);
            ) {
                preparedStatement.setInt(1,numRows++);
                preparedStatement.setString(2, newText);
                preparedStatement.setString(3, path);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
