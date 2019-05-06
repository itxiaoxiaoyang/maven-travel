package com.xiaoyang.travel.dao;

import com.xiaoyang.travel.model.User;

import java.util.List;

public interface UserDao {
    List<User> checkEmail(String email);

    int register(User user);

    int updateActiveStatusByCode(String code);

    List<User> checkEmailAndPwd(User user);

    List<User> isActiveEmail(String email);

    User addUser(String email);
}
