package org.gatesystem.model;

public class User {
    public enum Role {
        ADMIN,
        GUARD,
        GUEST
    }

    private int userID;
    private String username;
    private String email;
    private final Role role;
    private String passwordHash;
    private final String firstName;
    private final String lastName;
    private final String nrc;

    public User(String username, String email, Role role, String firstName, String lastName, String nrc) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nrc = nrc;
    }

    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String  getEmail() { return email; }
    public Role getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNrc() { return nrc; }

    public void setUserID(int userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String passwordHash) { this.passwordHash = passwordHash; }
}
