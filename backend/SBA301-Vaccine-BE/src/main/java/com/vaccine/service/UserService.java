package com.vaccine.service;

import com.vaccine.entity.User;

import java.util.List;

public interface UserService {

    public List<User> findAll(String role);

    public void save(User user);

    public User getUserByID(int userID);

    public User getUserByEmail(String email);

    public boolean lockedUser(int id);

    public boolean unLockedUser(int id);

    public boolean getUserByPhone(String phone);

    public void lockedUserByEmail(String email);

    public boolean checkEmailOrPhone(String s);

}
