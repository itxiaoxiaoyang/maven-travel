package com.xiaoyang.travel.dao.impl;

import com.xiaoyang.travel.dao.FavoriteDao;
import com.xiaoyang.travel.model.Favorite;
import com.xiaoyang.travel.utils.C3p0Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 小帅杨
 * @version v1.0
 * @date 2019/3/24/0024 10:37
 * @description TODO
 **/
public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(C3p0Utils.getDataSource());

    /**
     * 根据rid和uid查询相应路线收藏信息
     *
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite queryIsCollect(String rid, int uid) {
        String sql = "SELECT * FROM tab_favorite WHERE rid = ? AND uid = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * 修改收藏路线总数
     *
     * @param rid
     * @return
     */
    @Override
    public int getFavoriteCount(String rid) {
        String sql = "UPDATE tab_route SET count = count + 1 WHERE rid = ?;";
        return jdbcTemplate.update(sql, rid);
    }


    /**
     * 添加收藏数据
     *
     * @param jdbcTemplate
     * @param rid
     * @param uid
     * @param date
     * @return
     */
    @Override
    public int addFavorite(JdbcTemplate jdbcTemplate, String rid, int uid, Date date) {
        String sql = "INSERT INTO tab_favorite VALUES(?,?,?)";
        //此处存储在数据库中的date可以写为NOW()
        return this.jdbcTemplate.update(sql, rid, date, uid);
    }


    /**
     * 查询所有的收藏条数
     *
     * @param uid
     * @return
     */
    @Override
    public int queryFavoriteTotalCount(int uid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rflag = 1 AND uid = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, uid);
        } catch (DataAccessException e) {
            //默认最少返回一条数据 便于显示最少一页
            return 1;
        }
    }

    /**
     * 查询登录用户的收藏条数排序
     *
     * @param uid
     * @return
     */
    @Override
    public int queryFavoriteRankTotalCount(int uid, String rname, String startPrice, String endPrice) {

        StrBuilder sb = new StrBuilder("SELECT COUNT(*) FROM tab_favorite tbf INNER JOIN tab_route tbr ON tbf.rid = tbr.rid WHERE rflag = 1  ");
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


        sb.append(" AND tbf.uid = ?");
        list.add(uid);

        try {
            return jdbcTemplate.queryForObject(sb.toString(), Integer.class, list.toArray());
        } catch (DataAccessException e) {
            return 0;
        }
    }


    /**
     * 查询所有用户的线路排行榜
     * @param rname
     * @param startPrice
     * @param endPrice
     * @return
     */
    @Override
    public int queryFavoriteRankTotalCount(String rname, String startPrice, String endPrice) {
        StrBuilder sb = new StrBuilder("SELECT COUNT(*) FROM tab_route WHERE rflag = 1  ");
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

        try {
            return jdbcTemplate.queryForObject(sb.toString(), Integer.class, list.toArray());
        } catch (DataAccessException e) {
            return 0;
        }
    }
}
