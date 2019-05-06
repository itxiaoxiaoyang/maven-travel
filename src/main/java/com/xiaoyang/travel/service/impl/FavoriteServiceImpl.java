package com.xiaoyang.travel.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaoyang.travel.dao.FavoriteDao;
import com.xiaoyang.travel.dao.RouteDao;
import com.xiaoyang.travel.dao.impl.FavoriteDaoImpl;
import com.xiaoyang.travel.dao.impl.RouteDaoImpl;
import com.xiaoyang.travel.model.Favorite;
import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.model.User;
import com.xiaoyang.travel.service.FavoriteService;
import com.xiaoyang.travel.utils.C3p0Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/24/0024 10:34
 * @description TODO
 **/
public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    private RouteDao routeDao = new RouteDaoImpl();

    /**
     * 通过rid和uid查询展示业务
     *
     * @param rid
     * @param user
     * @return
     */
    @Override
    public String queryIsCollect(String rid, User user) {
        //创建map集存储信息
        Map<String, Object> map = new HashMap<>();
        //首先判断用户是否已经登录
        if (user != null) {
            //用户已经登录 获取用户cid
            map.put("loginFlag", true);
            //根据rid和cid查询收藏信息
            Favorite favorite = favoriteDao.queryIsCollect(rid, user.getUid());
            //判断登录后用户是否已经收藏 查到数据即已经收藏
            if (favorite != null) {
                //已收藏
                map.put("collectFlag", false);
            } else {
                //未收藏
                map.put("collectFlag", true);
            }
        } else {
            //用户未登录
            map.put("loginFlag", false);
        }
        return JSON.toJSONString(map);
    }

    /**
     * @param rid
     * @param loginUser
     * @return
     */
    @Override
    public String addFavorite(String rid, User loginUser) {
        //创建map封装结果
        Map<String, Object> result = new HashMap<>();

        if (null == loginUser) {
            //未登录：不能收藏数据--loginFlag==false
            result.put("loginFlag", false);
        } else {
            //已登录：添加收藏数据
            result.put("loginFlag", true);

            //初始化数据源
            DataSource dataSource = C3p0Utils.getDataSource();
            //初始化jdbctemplate模板
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            //开启sping的事务管理器 把当前的线程和conn绑定
            TransactionSynchronizationManager.initSynchronization();
            //获取conn
            Connection conn = DataSourceUtils.getConnection(dataSource);
            int addNum = 0;
            int updateNum = 0;
            try {
                //开启事务
                conn.setAutoCommit(false);

                //1、添加收藏数据
                //调用favoriteDao插入数据
                addNum = favoriteDao.addFavorite(jdbcTemplate, rid, loginUser.getUid(), new Date());

                int i = 1 / 0;
                //2、更新收藏次数：把tab_route表中的count更新为 count+1
                updateNum = routeDao.updateCountByRid(jdbcTemplate, rid);

                //成功 提交事务
                conn.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    //失败 回滚事务
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            //3、查询最新的收藏数据：查询tab_route
            Route route = routeDao.queryByRid(rid);
            result.put("count", route.getCount());
            result.put("commitFlag", addNum != 0 && updateNum != 0);

        }

        return JSON.toJSONString(result);
    }


    /**
     * 处理用户收藏线路的业务
     *
     * @param user
     * @param strPageNum
     * @param strPageSize
     * @return
     */
    @Override
    public String pageQueryByMyFavorite(User user, String strPageNum, String strPageSize) {

        //定义map集合存储数据
        Map<String, Object> map = new HashMap<>();
        //设置默认值
        int pageNum = 1;
        int pageSize = 12;
        //路线总数
        int pageCount = 0;
        //设置查询的起始页
        int startPage = 0;
        //设置总条数
        int totalCount = 1;
        //设置总页数
        int totalPage;

        if (user == null) {
            //用户未登录
            map.put("loginFlag", false);
        } else {
            //用户已经登录
            map.put("loginFlag", true);
            //设置pageNum pageSize
            if (StringUtils.isNotBlank(strPageNum)) {
                pageNum = Integer.parseInt(strPageNum);
            }
            if (StringUtils.isNotBlank(strPageSize)) {
                pageSize = Integer.parseInt(strPageSize);
            }
            //设置查询的当前页
            startPage = (pageNum - 1) * pageSize;
        }

        // 查询数据 用户所有的收藏数据 分页展示
        List<Map<String, Object>> routeList = routeDao.queryPageRoute(user.getUid(), startPage, pageSize);
        //查询总条数
        totalCount = favoriteDao.queryFavoriteTotalCount(user.getUid());
        //查询总页数
        totalPage = (int) Math.ceil((totalCount * 1.0) / pageSize);

        map.put("routeList", routeList);
        map.put("pageNum", pageNum);
        map.put("totalPage", totalPage);
        map.put("pageSize", pageSize);
        map.put("prePage", pageNum <= 1 ? 1 : (pageNum - 1));
        map.put("nextPage", pageNum >= totalPage ? totalPage : (pageNum + 1));
        return JSON.toJSONString(map);
    }

    /**
     * 处理登录用户收藏排行榜的业务
     *
     * @param user
     * @param strPageNum
     * @param strPageSize
     * @param rname
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public String pageQueryByRank(User user, String strPageNum, String strPageSize, String rname, String startPrice, String endPrice) {
        //定义map集合存储数据
        Map<String, Object> map = new HashMap<>();
        //设置默认值
        int pageNum = 1;
        int pageSize = 8;
        //路线总数
        int pageCount = 0;
        //设置查询的起始页
        int startPage = 0;
        //设置总条数
        int totalCount = 1;
        //设置总页数
        int totalPage;
        int startPrice_int = 0;
        //设置页面刷新的初始化数值 默认是什么?后期优化
        int endPrice_int = 99999999;
        if (StringUtils.isNotBlank(startPrice)) {
            startPrice_int = Integer.parseInt(startPrice);
        }
        if (StringUtils.isNotBlank(endPrice)) {
            endPrice_int = Integer.parseInt(endPrice);
        }
        if (startPrice_int > endPrice_int) {
            int temp = startPrice_int;
            startPrice_int = endPrice_int;
            endPrice_int = temp;
        }
        //将原数据转换为string类型
        startPrice = startPrice_int+"";
        endPrice = endPrice_int+"";

        if (user == null) {
            //用户未登录
            map.put("loginFlag", false);
        } else {
            //用户已经登录
            map.put("loginFlag", true);
            //设置pageNum pageSize
            if (StringUtils.isNotBlank(strPageNum)) {
                pageNum = Integer.parseInt(strPageNum);
            }
            if (StringUtils.isNotBlank(strPageSize)) {
                pageSize = Integer.parseInt(strPageSize);
            }

            //设置查询的当前页
            startPage = (pageNum - 1) * pageSize;
        }

        // 查询数据 用户所有的收藏数据 分页展示
        List<Map<String, Object>> routeList = routeDao.pageQueryByRank(user.getUid(), startPage, pageSize, rname, startPrice, endPrice);
        //查询总条数
        totalCount = favoriteDao.queryFavoriteRankTotalCount(user.getUid(), rname, startPrice, endPrice);
        //查询总页数
        totalPage = (int) Math.ceil((totalCount * 1.0) / pageSize);

        map.put("routeList", routeList);
        map.put("pageNum", pageNum);
        map.put("totalPage", totalPage);
        map.put("pageSize", pageSize);
        map.put("prePage", pageNum <= 1 ? 1 : (pageNum - 1));
        map.put("nextPage", pageNum >= totalPage ? totalPage : (pageNum + 1));

/*
        if (StringUtils.isNotBlank(rname)) {
            map.put("rname", rname);
        }
        if (StringUtils.isNotBlank(startPrice)) {
            map.put("startPrice", Integer.parseInt(startPrice));
        }
        if (StringUtils.isNotBlank(endPrice)) {
            map.put("endPrice", Integer.parseInt(endPrice));
        }*/


        return JSON.toJSONString(map);
    }

    /**
     * 查询所有用户收藏排行榜
     * @param strPageNum
     * @param strPageSize
     * @param rname
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public String pageQueryByRank(String strPageNum, String strPageSize, String rname, String startPrice, String endPrice) {
        //定义map集合存储数据
        Map<String, Object> map = new HashMap<>();
        //设置默认值
        int pageNum = 1;
        int pageSize = 8;
        //路线总数
        int pageCount = 0;
        //设置查询的起始页
        int startPage = 0;
        //设置总条数
        int totalCount = 1;
        //设置总页数
        int totalPage;
        int startPrice_int = 0;
        //设置页面刷新的初始化数值 默认是什么?后期优化
        int endPrice_int = 99999999;
        if (StringUtils.isNotBlank(startPrice)) {
            startPrice_int = Integer.parseInt(startPrice);
        }
        if (StringUtils.isNotBlank(endPrice)) {
            endPrice_int = Integer.parseInt(endPrice);
        }
        if (startPrice_int > endPrice_int) {
            int temp = startPrice_int;
            startPrice_int = endPrice_int;
            endPrice_int = temp;
        }
        //将原数据转换为string类型
        startPrice = startPrice_int+"";
        endPrice = endPrice_int+"";


            //设置pageNum pageSize
            if (StringUtils.isNotBlank(strPageNum)) {
                pageNum = Integer.parseInt(strPageNum);
            }
            if (StringUtils.isNotBlank(strPageSize)) {
                pageSize = Integer.parseInt(strPageSize);
            }

            //设置查询的当前页
            startPage = (pageNum - 1) * pageSize;

        // 查询数据 用户所有的收藏数据 分页展示
        List<Map<String, Object>> routeList = routeDao.pageQueryByRank(startPage, pageSize, rname, startPrice, endPrice);
        //查询总条数
        totalCount = favoriteDao.queryFavoriteRankTotalCount(rname, startPrice, endPrice);
        //查询总页数
        totalPage = (int) Math.ceil((totalCount * 1.0) / pageSize);

        map.put("routeList", routeList);
        map.put("pageNum", pageNum);
        map.put("totalPage", totalPage);
        map.put("pageSize", pageSize);
        map.put("prePage", pageNum <= 1 ? 1 : (pageNum - 1));
        map.put("nextPage", pageNum >= totalPage ? totalPage : (pageNum + 1));

        return JSON.toJSONString(map);
    }
}
