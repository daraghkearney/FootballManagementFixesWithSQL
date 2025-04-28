package ie.atu.Football_Management;

import java.sql.*;

public class Playertracking {
    public static void main(String[] args) {

        String playerName = null;
        String playerAge = null;

        try (Connection connection = DatabaseCentral.getConnection()) {
            // Updated query to use the correct column names
            String query = "SELECT p.player_name, p.age FROM players p";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                playerName = resultSet.getString("player_name");
                playerAge = resultSet.getString("age");
            }

            // Print player name and age
            System.out.println(playerName + " Aged: " + playerAge);

        } catch (SQLException e) {
            throw new RuntimeException("Database error during query execution.", e);
        } finally {
            DatabaseCentral.close();
        }
    }
}
