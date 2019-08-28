package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by yfyuan on 2016/12/21.
 */
@Mapper
public interface MenuDao {
    List<Long> getMenuIdByUserRole(String userId);

    List<Long> getMenuIdByRoleAdmin(String userId);
}
