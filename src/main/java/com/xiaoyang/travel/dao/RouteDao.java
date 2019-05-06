package com.xiaoyang.travel.dao;

import com.xiaoyang.travel.model.Route;
import com.xiaoyang.travel.model.RouteImg;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface RouteDao {
    List<Route> routeQuery(int pageCount, int pageSize, String cid, String rname);

    int totalCount(String cid, String rname);

    Map<String,Object> queryDetail(String rid);

    List<RouteImg> queryDetailImg(String rid);

    int updateCountByRid(JdbcTemplate jdbcTemplate, String rid);

    Route queryByRid(String rid);

    List<Map<String,Object>> queryPageRoute(int uid, int startPage, int pageSize);

    List<Map<String,Object>> pageQueryByRank(int uid, int startPage, int pageSize,String rname, String startPrice, String endPrice);

    List<String> queryByKey(String rname);

    List<Map<String,Object>> pageQueryByRank(int startPage, int pageSize, String rname, String startPrice, String endPrice);
}
