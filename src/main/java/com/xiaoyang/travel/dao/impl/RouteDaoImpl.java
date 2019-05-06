package com.xiaoyang.travel.dao.impl;

import com.xiaoyang.travel.dao.RouteDao;
import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.model.RouteImg;
import com.xiaoyang.travel.utils.C3p0Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/21/0021 21:29
 * @description TODO
 **/
public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(C3p0Utils.getDataSource());

    /**
     * 查询出分页的路线数据
     * <p>
     * 加入cid之后需要进行字符串拼接 条件拼接
     *
     * @param pageCount
     * @param pageSize
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public List<Route> routeQuery(int pageCount, int pageSize, String cid, String rname) {
//        String sql = "SELECT * FROM tab_route WHERE rflag = 1 LIMIT ?,?";
        //定义字符串拼接
        StringBuilder sb = new StringBuilder("SELECT * FROM tab_route WHERE rflag = 1 ");
        //定义list集合用于存储长度未知的数据
        List<Object> list = new ArrayList<>();
        //判断cid是否为空
        if (StringUtils.isNotBlank(cid)) {
            //不为空 拼接字符串
            sb.append(" AND cid = ? ");
            //存储在集合中
            list.add(cid);
        }
        if (StringUtils.isNotBlank(rname)) {
            sb.append(" AND rname like ?");
            list.add("%" + rname + "%");
        }
        sb.append(" LIMIT ?,?;");
        list.add(pageCount);
        list.add(pageSize);

        return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<>(Route.class), list.toArray());
    }

    /**
     * 查询出数据总条数
     *
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int totalCount(String cid, String rname) {
//        String sql = "SELECT COUNT(*) FROM tab_route WHERE rflag = 1;";
        //定义字符串拼接
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM tab_route WHERE rflag = 1 ");
        //定义list集合用于存储长度未知的数据
        List<Object> list = new ArrayList<>();
        //判断cid是否为空
        if (StringUtils.isNotBlank(cid)) {
            //不为空 拼接字符串
            sb.append(" AND cid = ? ");
            //存储在集合中
            list.add(cid);
        }
        if (StringUtils.isNotBlank(rname)) {
            sb.append(" AND rname like ?");
            //java内部会自动加上 '%字符串%'
            list.add("%" + rname + "%");
        }
        try {
            return jdbcTemplate.queryForObject(sb.toString(), Integer.class, list.toArray());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    /**
     * 查询路线详细信息 rid cid sid
     *
     * @param rid
     * @return
     */
    @Override
    public Map<String, Object> queryDetail(String rid) {
        String sql = "SELECT * FROM tab_route tbr " +
                " INNER JOIN tab_category tbc " +
                " ON tbr.cid = tbc.cid " +
                " INNER JOIN tab_seller tbs " +
                " ON tbr.sid = tbs.sid " +
                " WHERE rid = ?";
        try {
            return jdbcTemplate.queryForMap(sql, rid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * 根据rid查询出详细路线中大小图片
     *
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> queryDetailImg(String rid) {
        String sql = "SELECT * FROM tab_route_img WHERE rid = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
    }

    /**
     * 把count更新为count+1
     *
     * @param jdbcTemplate
     * @param rid
     * @return
     */
    @Override
    public int updateCountByRid(JdbcTemplate jdbcTemplate, String rid) {
        String sql = "UPDATE tab_route SET count=count+1 WHERE rid = ?";

        return this.jdbcTemplate.update(sql,rid);
    }


    /**
     * 根据rid查询route数据
     * @param rid
     * @return
     */
    @Override
    public Route queryByRid(String rid) {

        String sql = "SELECT * FROM tab_route WHERE rid = ?";

        try {
            return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * 查询收藏路线的信息
     * @param uid
     * @param startPage
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPageRoute(int uid, int startPage, int pageSize) {
        String sql = "SELECT tbf.rid,tbr.rname,tbr.price,tbr.rimage,tbr.count " +
                "FROM tab_route tbr INNER JOIN tab_favorite tbf ON tbr.rid = tbf.rid " +
                "WHERE tbr.rflag = 1 AND uid = ? LIMIT ?,?";
        return jdbcTemplate.queryForList(sql, uid, startPage, pageSize);
    }

    /**
     * 查询登录用户收藏排行榜的信息
     * @param uid
     * @param startPage
     * @param pageSize
     * @param rname
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public List<Map<String, Object>> pageQueryByRank(int uid, int startPage, int pageSize, String rname, String startPrice, String endPrice) {
        StrBuilder sb = new StrBuilder("SELECT * FROM tab_favorite tbf INNER JOIN tab_route tbr ON tbf.rid = tbr.rid WHERE tbr.rflag = 1 ");
        List<Object> list = new ArrayList<>();

        if (StringUtils.isNotBlank(rname)) {
            sb.append(" AND tbr.rname LIKE ? ");
            list.add("%"+rname+"%");
        }
        if (StringUtils.isNotBlank(startPrice)) {
            sb.append(" AND tbr.price>= ?  ");
            list.add(Integer.parseInt(startPrice));
        }
        if (StringUtils.isNotBlank(endPrice)) {
            sb.append(" AND tbr.price<= ?  ");
            list.add(Integer.parseInt(endPrice));
        }


        sb.append(" AND tbf.uid = ? ORDER BY tbr.count desc LIMIT ?,?;");
        list.add(uid);
        list.add(startPage);
        list.add(pageSize);


        return jdbcTemplate.queryForList(sb.toString(), list.toArray());
    }

    /**
     * 插件查询线路
     * @param rname
     * @return
     */
    @Override
    public List<String> queryByKey(String rname) {
        String sql = "SELECT rname FROM tab_route WHERE rname LIKE ? LIMIT 0,6;";
        return jdbcTemplate.queryForList(sql, String.class, "%" + rname + "%");
    }


    /**
     * 查询所有用户收藏排行榜的信息
     * @param startPage
     * @param pageSize
     * @param rname
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public List<Map<String, Object>> pageQueryByRank(int startPage, int pageSize, String rname, String startPrice, String endPrice) {
        StrBuilder sb = new StrBuilder("SELECT * FROM tab_route WHERE rflag = 1 ");
        List<Object> list = new ArrayList<>();

        if (StringUtils.isNotBlank(rname)) {
            sb.append(" AND rname LIKE ? ");
            list.add("%"+rname+"%");
        }
        if (StringUtils.isNotBlank(startPrice)) {
            sb.append(" AND price>= ?  ");
            list.add(Integer.parseInt(startPrice));
        }
        if (StringUtils.isNotBlank(endPrice)) {
            sb.append(" AND price<= ?  ");
            list.add(Integer.parseInt(endPrice));
        }

        sb.append(" ORDER BY count DESC LIMIT ?,?;");
        list.add(startPage);
        list.add(pageSize);


        return jdbcTemplate.queryForList(sb.toString(), list.toArray());
    }
}
