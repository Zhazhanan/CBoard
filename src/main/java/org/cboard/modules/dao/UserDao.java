package org.cboard.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cboard.modules.pojo.DashboardUser;
import org.cboard.modules.pojo.DashboardUserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/12/2.
 */
@Mapper
public interface UserDao {
    int save(DashboardUser user);

    int deleteUserById(String userId);

    List<DashboardUser> getUserList();

    int update(DashboardUser user);

    int saveUserRole(List<DashboardUserRole> list);

    int deleteUserRole(Map<String, Object> param);

    List<DashboardUserRole> getUserRoleList();

    DashboardUser getUserByLoginName(String loginName);

    int saveNewUser(@Param("userId") String userId,@Param("user_name")  String user_name,@Param("loginName")  String loginName);

    int updateUserPassword(@Param("userId") String userId,@Param("passowrd")  String passowrd,@Param("newPassword")  String newPassword);

    int deleteUserRoleByRoleId(String roleId);

    int deleteUserRoles(Map<String, Object> param);

}
