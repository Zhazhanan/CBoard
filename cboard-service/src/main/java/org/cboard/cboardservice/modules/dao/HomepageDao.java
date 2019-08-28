package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by february on 2018/12/20.
 */
@Mapper
public interface HomepageDao {

    int saveHomepage(@Param("boardId") Long boardId, @Param("userId") String userId);

    int resetHomepage(String userId);

    Long selectHomepage(String userId);
}
