package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.cboard.cboardservice.modules.pojo.DashboardCategory;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/26.
 */
@Mapper
public interface CategoryDao {

    List<DashboardCategory> getCategoryList();

    int save(DashboardCategory dashboardCategory);

    long countExistCategoryName(Map<String, Object> map);

    int update(DashboardCategory dashboardCategory);

    int delete(Long id);
}
