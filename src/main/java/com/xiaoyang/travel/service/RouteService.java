package com.xiaoyang.travel.service;

import com.xiaoyang.travel.model.User;

public interface RouteService {
    String pageQuery(String strPageNum, String strPageSize, String cid, String rname);

    String queryDetail(String rid);

    String queryByKey(String rname);
}
