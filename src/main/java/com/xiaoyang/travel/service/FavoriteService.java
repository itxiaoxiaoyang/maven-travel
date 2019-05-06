package com.xiaoyang.travel.service;

import com.xiaoyang.travel.model.User;

public interface FavoriteService {
    String queryIsCollect(String rid, User user);

    String addFavorite(String rid, User loginUser);

    String pageQueryByMyFavorite(User user, String pageNum, String pageSize);

    String pageQueryByRank(User user, String pageNum, String pageSize, String rname, String startPrice, String endPrice);

    String pageQueryByRank(String pageNum, String pageSize, String rname, String startPrice, String endPrice);
}
