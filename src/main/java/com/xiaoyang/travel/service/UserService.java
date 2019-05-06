package com.xiaoyang.travel.service;

import com.xiaoyang.travel.model.User;

public interface UserService {
    boolean checkEmail(String email);

    boolean register(User user) throws Exception;

    boolean emailActive(String code);

    boolean checkEmailAndPwd(User user) throws Exception;

    boolean isActiveEmail(String email);

    User addUser(String email);
}
