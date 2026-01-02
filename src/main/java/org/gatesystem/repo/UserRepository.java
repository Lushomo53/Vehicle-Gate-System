package org.gatesystem.repo;

import org.gatesystem.model.User;
import org.gatesystem.util.DatabaseConnector;

import java.sql.*;

public class UserRepository {

    public UserRepository() {
        // no dummy data
    }

    public User findByUsername(String username) {
        String sql = """
                SELECT *
                FROM users
                WHERE username = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findByEmail(String email) {
        String sql = """
                SELECT *
                FROM users
                WHERE email = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findByUsernameOrEmail(String username, String email) {
        String sql = """
                SELECT *
                FROM users
                WHERE username = ?
                   OR email = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean usernameExists(String username) {
        return exists("username", username);
    }

    public boolean emailExists(String email) {
        return exists("email", email);
    }

    private boolean exists(String column, String value) {
        String sql = "SELECT 1 FROM users WHERE " + column + " = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addUser(User user) {
        String sql = """
                INSERT INTO users
                (username, email, role, password_hash, first_name, last_name, nrc)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole().name());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getFirstName());
            ps.setString(6, user.getLastName());
            ps.setString(7, user.getNrc());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setUserID(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean resetPasswordByUsername(String username, String newPasswordHash) {
        String sql =
                "UPDATE users " +
                        "SET password_hash = ? " +
                        "WHERE username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPasswordHash);
            ps.setString(2, username);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("username"),
                rs.getString("email"),
                User.Role.valueOf(rs.getString("role")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("nrc")
        );

        user.setUserID(rs.getInt("user_id"));
        user.setPassword(rs.getString("password_hash"));

        return user;
    }
}
