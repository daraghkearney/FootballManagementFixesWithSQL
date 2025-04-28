import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;
import ie.atu.Football_Management.DatabaseCentral;

public class FPD_Main {
    public static void main(String[] args) {
        // Hardcoded credentials
        Scanner scanner = new Scanner(System.in);

        // Prompt user for credentials
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (Connection connection = DatabaseCentral.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                if (BCrypt.checkpw(password, storedHash)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid username or password.");
                    return;
                }
            } else {
                System.out.println("Invalid username or password.");
                return;
            }

            // Removed duplicate declaration of stmt

        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }

        // Prompt user for club ID and player name
        System.out.println("Enter in a Club ID: ");
        int clubId = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        System.out.println("Enter in a player name: ");
        String playerName = scanner.nextLine();

        // Trim player name to avoid issues with leading/trailing whitespace
        String playerNameTrimmed = playerName.trim();

        // SQL query with case-insensitive search for player name
        String selectSQL = "SELECT * FROM players WHERE club_id = ? AND LOWER(player_name) = LOWER(?)";

        try (Connection connection = DatabaseCentral.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectSQL)) {

            selectStmt.setInt(1, clubId);
            selectStmt.setString(2, playerNameTrimmed); // trimmed player name

            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int playerId = rs.getInt("player_id");
                String fetchedClubId = rs.getString("club_id");
                String fetchedPlayerName = rs.getString("player_name");

                System.out.println("Player Found:");
                System.out.println("Player ID: " + playerId);
                System.out.println("Club ID: " + fetchedClubId);
                System.out.println("Name: " + fetchedPlayerName);

            } else {
                System.out.println("Player not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error during retrieval.");
            e.printStackTrace();
        }

        // Menu options
        System.out.println("Choose the following Options: ");
        System.out.println("1. Performance");
        System.out.println("2. Disciplinary");
        System.out.println("3. PlayerID");
        System.out.println("4. Player Rating");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        try (Connection connection = DatabaseCentral.getConnection()) {
            switch (option) {
                case 1:
                    System.out.println("Enter performance note:");
                    String performance = scanner.nextLine();

                    String insertPerformance = "INSERT INTO player_stats (club_id, player_name, performance) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(insertPerformance)) {
                        ps.setInt(1, clubId);
                        ps.setString(2, playerNameTrimmed);
                        ps.setString(3, performance);
                        ps.executeUpdate();
                        System.out.println("Performance data saved.");
                    }
                    break;

                case 2:
                    System.out.println("Enter disciplinary note:");
                    String disciplinary = scanner.nextLine();

                    String insertDisciplinary = "UPDATE player_stats SET disciplinary = ? WHERE club_id = ? AND player_name = ?";
                    try (PreparedStatement ps = connection.prepareStatement(insertDisciplinary)) {
                        ps.setString(1, disciplinary);
                        ps.setInt(2, clubId);
                        ps.setString(3, playerNameTrimmed);
                        int updated = ps.executeUpdate();
                        if (updated > 0) {
                            System.out.println("Disciplinary record updated.");
                        } else {
                            System.out.println("Player not found. Please add performance first.");
                        }
                    }
                    break;

                case 3:
                    String getPlayerId = "SELECT player_id FROM player_stats WHERE club_id = ? AND player_name = ?";
                    try (PreparedStatement ps = connection.prepareStatement(getPlayerId)) {
                        ps.setInt(1, clubId);
                        ps.setString(2, playerNameTrimmed);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            System.out.println("Player ID: " + rs.getInt("player_id"));
                        } else {
                            System.out.println("Player not found.");
                        }
                    }
                    break;

                case 4:
                    System.out.println("Enter player rating (1–10):");
                    int rating = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    String updateRating = "UPDATE player_stats SET performance = CONCAT(performance, '\nRating: ', ?) WHERE club_id = ? AND player_name = ?";
                    try (PreparedStatement ps = connection.prepareStatement(updateRating)) {
                        ps.setInt(1, rating);
                        ps.setInt(2, clubId);
                        ps.setString(3, playerNameTrimmed);
                        ps.executeUpdate();
                        System.out.println("Rating added to performance notes.");
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }

        scanner.close();
    }
}
