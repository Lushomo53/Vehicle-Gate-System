package org.example.repo;

import org.example.model.User;
import org.example.util.PasswordUtil;

import java.util.*;

public class UserRepository {
    private final List<User> dummyData = new ArrayList<>();

    public UserRepository() {
        fillDummyData();
    }

    public void fillDummyData() {
        dummyData.add(new User("lushomo23", "lushomolungo21@gmail.com", User.Role.GUEST, "Lushomo", "Lungo", "486517/16/1"));
        dummyData.add(new User("john_doe", "jdo9@gmail.com", User.Role.GUEST, "John", "Doe", "163548/10/1"));
        dummyData.add(new User("feligojr", "migishakwizera@gmail.com", User.Role.GUEST, "Migisha", "Kwizera", "947204/09/1"));
        dummyData.add(new User("admin", "gwashington@gmail.com", User.Role.ADMIN, "George", "Washington", "546281/12/1"));

        dummyData.forEach(user -> {
            if (user.getRole() == User.Role.GUEST) {
                user.setPassword(PasswordUtil.hashPassword("12345"));
            } else if (user.getUsername().equals("admin")) {
                user.setPassword(PasswordUtil.hashPassword("admin"));
            }
        });
    }

    public User findByUsername(String username) {
        for (User user : dummyData) {
            if (username.equalsIgnoreCase(user.getUsername())) return user;
        }
        return null;
    }

    public User findByEmail(String email) {
        for (User user : dummyData) {
            if (email.equalsIgnoreCase(user.getEmail())) return user;
        }
        return null;
    }

    public boolean usernameExists(String username) {
        for (User user : dummyData) {
            if (username.equalsIgnoreCase(user.getUsername())) return true;
        }
        return false;
    }

    public boolean emailExists(String email) {
        for (User user : dummyData) {
            if (email.equalsIgnoreCase(user.getEmail())) return true;
        }
        return false;
    }

    public void addUser(User user) {
        dummyData.add(user);
    }
}
