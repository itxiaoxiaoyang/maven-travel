package com.xiaoyang.travel.dao.impl;

import com.xiaoyang.travel.dao.UserDao;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.utils.C3p0Utils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/17/0017 23:42
 * @description TODO
 **/
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(C3p0Utils.getDataSource());

    @Override
    public List<User> checkEmail(String email) {
        String sql = "SELECT * FROM tab_user WHERE email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
    }


    /**
     * 处理注册业务
     *
     * @param user
     * @return
     */
    @Override
    public int register(User user) {
        String sql = "INSERT INTO tab_user VALUES(null,?,?,?,?,?,?,?,? ,? )";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 激活用户邮箱
     *
     * @param code
     * @return
     */
    @Override
    public int updateActiveStatusByCode(String code) {
        String sql = "UPDATE tab_user SET status = 1 WHERE code= ? AND status=0";
        return jdbcTemplate.update(sql, code);
    }

    /**
     * 查询用户是否存在
     *
     * @param user
     * @return
     */
    @Override
    public List<User> checkEmailAndPwd(User user) {
        String sql = "SELECT * FROM tab_user WHERE password = ? AND email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), user.getPassword(), user.getEmail());
    }

    /**
     * 查询邮箱是否激活
     *
     * @return
     */
    @Override
    public List<User> isActiveEmail(String email) {
        String sql = "SELECT * FROM tab_user WHERE email = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
    }

    /**
     * 查询出匹配的email所有数据添加在user中
     */
    @Override
    public User addUser(String email) {
        String sql = "SELECT * FROM tab_user WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email);
            return user;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
