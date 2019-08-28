package org.cboard.cboardservice.modules.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;
import org.cboard.cboardservice.modules.pojo.DashboardJob;
import org.cboard.cboardservice.modules.services.role.RolePermission;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by yfyuan on 2017/2/20.
 */
@Data
public class ViewDashboardJob implements Serializable {

    private static final long serialVersionUID = -729174594455921007L;
    private Long id;
    private String name;
    private String cronExp;
    private Map<String, Object> daterange;
    private String jobType;
    private Map<String, Object> config;
    private String userId;
    private Date lastExecTime;
    private String userName;
    private Long jobStatus;
    private String execLog;
    private boolean edit;
    private boolean delete;

    public static final Long STATUS_PROCESSING = 2L;
    public static final Long STATUS_FINISH = 1L;
    public static final Long STATUS_FAIL = 0L;

    public ViewDashboardJob(DashboardJob job) {
        this.id = job.getId();
        this.userId = job.getUserId();
        this.name = job.getName();
        this.cronExp = job.getCronExp();
        this.daterange = new HashedMap();
        this.daterange.put("startDate", job.getStartDate());
        this.daterange.put("endDate", job.getEndDate());
        this.jobType = job.getJobType();
        this.config = JSONObject.parseObject(job.getConfig());
        this.lastExecTime = job.getLastExecTime();
        this.userName = job.getUserName();
        this.jobStatus = job.getJobStatus();
        this.execLog = job.getExecLog();
        this.edit = RolePermission.isEdit(job.getPermission());
        this.delete = RolePermission.isDelete(job.getPermission());
    }

}
