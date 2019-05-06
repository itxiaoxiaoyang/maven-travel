package com.xiaoyang.travel.dao;

import com.xiaoyang.travel.model.Category;
import com.xiaoyang.travel.model.Route;

import java.util.List;

public interface CategoryDao {
    List<Category> queryAll();

    List<Route> popularity();

    List<Route> newest();

    List<Route> theme();
}
