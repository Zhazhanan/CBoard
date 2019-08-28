package org.cboard.cboardservice.modules.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cboard.cboardservice.modules.pojo.DashboardJob;

import java.util.Date;
import java.util.List;

/**
 * Created by yfyuan on 2017/2/17.
 */
@Mapper
public interface JobDao {
    int save(DashboardJob job);

    int update(DashboardJob job);

    List<DashboardJob> getJobList(String userId);

    List<DashboardJob> getJobListAdmin(String userId);

    int delete(Long jobId);

    int updateLastExecTime(@Param("jobId") Long jobId, @Param("date") Date date);

    int updateStatus(@Param("jobId") Long jobId, @Param("status") Long status, @Param("log") String log);

    DashboardJob getJob(Long jobId);

    long checkJobRole(@Param("userId") String userId, @Param("jobId") Long jobId, @Param("permissionPattern") String permissionPattern);

}
