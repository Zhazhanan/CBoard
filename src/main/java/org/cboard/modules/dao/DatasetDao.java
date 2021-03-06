package org.cboard.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cboard.modules.pojo.DashboardDataset;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Mapper
public interface DatasetDao {

    List<String> getCategoryList();

    List<DashboardDataset> getAllDatasetList();

    List<DashboardDataset> getDatasetList(@Param("userId") String userId);

    List<DashboardDataset> getDatasetListAdmin(@Param("userId")String userId);

    int save(DashboardDataset dataset);

    long countExistDatasetName(Map<String, Object> map);

    int update(DashboardDataset dataset);

    int delete(@Param("id")Long id,@Param("userId") String userId);

    DashboardDataset getDataset(Long id);

    long checkDatasetRole(@Param("userId")String userId,@Param("widgetId") Long widgetId,@Param("permissionPattern") String permissionPattern);

}
