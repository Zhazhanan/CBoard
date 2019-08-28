package org.cboard.cboardservice.config;

import org.cboard.cboardservice.modules.dao.JobDao;
import org.cboard.cboardservice.modules.pojo.DashboardJob;
import org.cboard.cboardservice.modules.services.job.JobService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WangKun
 * @create 2019-03-02
 * @desc Initialize project data
 **/
@Component
public class InitData implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitData.class);

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobDao jobDao;

    @Autowired
    private JobService jobService;

    /**
     * db Scheduler job init
     */
    public void configScheduler() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            LOGGER.error("", e);
        }
        List<DashboardJob> jobList = jobDao.getJobList(adminUserId);
        for (DashboardJob job : jobList) {
            jobService.scheduleJob(job);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**db job init*/
        configScheduler();
    }
}
