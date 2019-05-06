package com.xiaoyang.travel.dao;

import com.xiaoyang.travel.model.Favorite;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public interface FavoriteDao {
    Favorite queryIsCollect(String rid, int uid);

    int getFavoriteCount(String rid);


    int addFavorite(JdbcTemplate jdbcTemplate, String rid, int uid, Date date);

    int queryFavoriteTotalCount(int uid);

    int queryFavoriteRankTotalCount(int uid, String rname, String startPrice, String endPrice);

    int queryFavoriteRankTotalCount(String rname, String startPrice, String endPrice);
}
