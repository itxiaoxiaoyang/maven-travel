package com.xiaoyang.travel.service.impl;

import com.xiaoyang.travel.dao.UserDao;
import com.xiaoyang.travel.dao.impl.UserDaoImpl;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.UserService;
import com.xiaoyang.travel.utils.Md5Util;
import com.xiaoyang.travel.utils.UuidUtil;

import java.util.List;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/17/0017 23:40
 * @description TODO
 **/
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean checkEmail(String email) {
        List<User> userList = userDao.checkEmail(email);
        if (userList != null && userList.size() != 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean register(User user) throws Exception {
        user.setStatus(0);
        user.setCode(UuidUtil.getUuid());
        String md5Pwd = Md5Util.encodeByMd5(user.getPassword());
        user.setPassword(md5Pwd);
        return userDao.register(user) != 0;
    }

    /**
     * 邮件激活
     *
     * @param code
     * @return
     */
    @Override
    public boolean emailActive(String code) {
        //根据code跟新激活状态
        int updateFlag = userDao.updateActiveStatusByCode(code);
        return updateFlag != 0;
    }

    /**
     * 处理用户登录业务
     *
     * @param user
     * @return
     */
    @Override
    public boolean checkEmailAndPwd(User user) throws Exception {

        //处理密码加密
        String md5Pwd = Md5Util.encodeByMd5(user.getPassword());
        user.setPassword(md5Pwd);
        List<User> userList = userDao.checkEmailAndPwd(user);
        if (null != userList && userList.size() > 0) {
            //查到数据
            return true;
        }
        return false;
    }

    /**
     * 处理邮箱是否激活业务
     *
     * @param email
     * @return
     */
    @Override
    public boolean isActiveEmail(String email) {
        List<User> userList = userDao.isActiveEmail(email);
        if (null != userList && userList.size() > 0) {
            //有数据
            return true;
        }
        return false;
    }

    /**
     * 将数据库所有数据存储到user中
     * @param email
     * @return
     */
    @Override
    public User addUser(String email) {
        return userDao.addUser(email);
    }
}
