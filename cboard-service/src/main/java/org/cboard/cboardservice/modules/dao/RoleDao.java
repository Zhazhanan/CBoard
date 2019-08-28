package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cboard.cboardservice.modules.pojo.DashboardRole;
import org.cboard.cboardservice.modules.pojo.DashboardRoleRes;

import java.util.List;

/**
 * Created by yfyuan on 2016/12/6.
 */
@Mapper
public interface RoleDao {
    int save(DashboardRole role);

    List<DashboardRole> getRoleList(String userId);

    List<DashboardRole> getCurrentRoleList(String userId);

    List<DashboardRole> getRoleListAll();

    int update(DashboardRole role);

    List<DashboardRoleRes> getRoleResList();

    int saveRoleRes(DashboardRoleRes item);

    int deleteRoleRes(String roleId);

    int deleteRoleResByResId(@Param("resId") Long resId, @Param("resType") String resType);

    List<Long> getRoleResByResIds(@Param("userId") String userId, @Param("resType") String resType);

    DashboardRole getRole(String roleId);

    int deleteRole(String roleId);

    List<DashboardRoleRes> getUserRoleResList(@Param("userId") String userId, @Param("resType") String resType);
}
