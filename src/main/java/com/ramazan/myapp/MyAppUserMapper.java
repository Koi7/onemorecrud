package com.ramazan.myapp;

import java.util.List;

public interface MyAppUserMapper {
    MyAppUser getUserById(Integer id);
    void createUser(MyAppUser user);
    void updateUser(MyAppUser user);
    void deleteUser(MyAppUser user);
    List getAllUsersExcept(Integer id);
    MyAppUser getUserByUserName(String name);
}
