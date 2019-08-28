package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cboard.cboardservice.modules.pojo.DashboardBoard;
import org.cboard.cboardservice.modules.pojo.DashboardBoardParam;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/23.
 */
@Mapper
public interface BoardDao {

    int save(DashboardBoard board);

    List<DashboardBoard> getBoardList(String userId);

    List<DashboardBoard> getBoardListAdmin(String userId);

    long countExistBoardName(Map<String, Object> map);

    int update(DashboardBoard board);

    int delete(@Param("id") Long id, @Param("userId") String userId);

    DashboardBoard getBoard(Long id);

    long checkBoardRole(@Param("userId") String userId, @Param("boardId") Long boardId, @Param("permissionPattern") String permissionPattern);

    DashboardBoardParam getBoardParam(@Param("boardId") Long boardId, @Param("userId") String userId);

    int saveBoardParam(DashboardBoardParam boardParam);

    int deleteBoardParam(@Param("boardId") Long boardId, @Param("userId") String userId);
}
