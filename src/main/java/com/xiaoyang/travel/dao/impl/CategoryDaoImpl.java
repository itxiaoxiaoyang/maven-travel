package com.xiaoyang.travel.dao.impl;

import com.xiaoyang.travel.dao.CategoryDao;
import com.xiaoyang.travel.model.Category;
import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.utils.C3p0Utils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/21/0021 0:10
 * @description TODO
 **/
public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate jdbcTemplate =  new JdbcTemplate(C3p0Utils.getDataSource());


    /**
     * 查询出所有的标题栏数据
     * @return
     */
    @Override
    public List<Category> queryAll() {
        String sql = "SELECT * FROM tab_category ORDER BY cid";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    /**
     * 查询人气旅游的线路 4条数据
     * @return
     */
    @Override
    public List<Route> popularity() {
        String sql = "SELECT * FROM tab_route WHERE rflag = 1 ORDER BY count DESC LIMIT 0,4;";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Route.class));
    }

    /**
     * 查询最新旅游的线路 4条数据
     * @return
     */
    @Override
    public List<Route> newest() {
        String sql = "SELECT * FROM tab_route WHERE rflag = 1 ORDER BY rdate DESC LIMIT 0,4;";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Route.class));
    }

    /**
     * 查询主题旅游的线路 4条数据
     * @return
     */
    @Override
    public List<Route> theme() {
        String sql = "SELECT * FROM tab_route WHERE rflag = 1 ORDER BY isThemeTour DESC LIMIT 0,4;";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Route.class));
    }
}
