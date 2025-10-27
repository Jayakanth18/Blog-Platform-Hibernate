package com.blog.services;

import com.blog.dao.UserDao;
import com.blog.entity.User;

public class UserService {
    private UserDao userDao = new UserDao();

    public boolean registerUser(String username, String password) {
        if (userDao.getUserByUsername(username) != null) {
            System.out.println("Username already exists.");
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In a real app, hash this password!
        userDao.saveUser(user);
        return true;
    }

    public User loginUser(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Invalid credentials
    }
}