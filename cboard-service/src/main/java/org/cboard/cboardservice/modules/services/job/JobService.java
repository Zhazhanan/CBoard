package org.cboard.cboardservice.modules.services.job;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.cboard.cboardservice.modules.dao.JobDao;
import org.cboard.cboardservice.modules.dto.ViewDashboardJob;
import org.cboard.cboardservice.modules.pojo.DashboardJob;
import org.cboard.cboardservice.modules.services.MailService;
import org.cboard.cboardservice.modules.services.ServiceStatus;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yfyuan on 2017/2/17.
 */
@Service
public class JobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private MailService mailService;

    private static Logger LOG = LoggerFactory.getLogger(JobService.class);

    public static final String JOB_TYPE_MAIL = "mail";

    public void scheduleJob(DashboardJob job) {
        try {
            long startTimeStamp = job.getStartDate().getTime();
            long endTimeStamp = job.getEndDate().getTime();
            if (endTimeStamp < System.currentTimeMillis()) {
                // Skip expired job
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(getJobExecutor(job))
                    .withIdentity(job.getId().toString(), job.getJobType())
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .startAt(System.currentTimeMillis() < startTimeStamp ? job.getStartDate() : new Date())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExp()))
                    .endAt(job.getEndDate())
                    .build();
            jobDetail.getJobDataMap().put("job", job);
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("{} Job id: {}", e.getMessage(), job.getId());
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    private Class<? extends Job> getJobExecutor(DashboardJob job) {
        switch (job.getJobType()) {
            case JOB_TYPE_MAIL:
                return MailJobExecutor.class;
            default:
                return null;
        }
    }

    protected void sendMail(DashboardJob job) {
        jobDao.updateLastExecTime(job.getId(), new Date());
        try {
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_PROCESSING, "");
            mailService.sendDashboard(job);
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_FINISH, "");
        } catch (Exception e) {
            LOG.error("", e);
            jobDao.updateStatus(job.getId(), ViewDashboardJob.STATUS_FAIL, ExceptionUtils.getStackTrace(e));
        }
    }

    public ServiceStatus save(String userId, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardJob job = new DashboardJob();
        job.setUserId(userId);
        job.setName(jsonObject.getString("name"));
        job.setConfig(jsonObject.getString("config"));
        job.setCronExp(jsonObject.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jsonObject.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jsonObject.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            LOG.error("", e);
        }
        job.setJobType(jsonObject.getString("jobType"));
        jobDao.save(job);

        scheduleJob(job);
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus update(String userId, String json) throws SchedulerException {
        JSONObject jsonObject = JSONObject.parseObject(json);
        DashboardJob job = new DashboardJob();
        job.setId(jsonObject.getLong("id"));
        job.setName(jsonObject.getString("name"));
        job.setConfig(jsonObject.getString("config"));
        job.setCronExp(jsonObject.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jsonObject.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jsonObject.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            LOG.error("", e);
        }
        job.setJobType(jsonObject.getString("jobType"));
        jobDao.update(job);
        schedulerFactoryBean.getScheduler().deleteJob(new JobKey(String.valueOf(job.getId()), job.getJobType()));
        scheduleJob(job);
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus delete(String userId, Long id, String type) throws SchedulerException {
        jobDao.delete(id);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(new JobKey(String.valueOf(id), type));
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

    public ServiceStatus exec(String userId, Long id) {
        DashboardJob job = jobDao.getJob(id);
        new Thread(() ->
                sendMail(job)
        ).start();
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

}
