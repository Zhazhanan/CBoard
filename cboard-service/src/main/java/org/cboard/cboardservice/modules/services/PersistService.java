package org.cboard.cboardservice.modules.services;

import com.alibaba.fastjson.JSONObject;
import org.cboard.cboardservice.exception.CBoardException;
import org.cboard.cboardservice.modules.dao.BoardDao;
import org.cboard.cboardservice.modules.services.persist.PersistContext;
import org.cboard.cboardservice.security.service.LocalSecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yfyuan on 2017/2/10.
 */
@Service
public class PersistService {

    private static final Logger LOG = LoggerFactory.getLogger(PersistService.class);

    @Value("${phantom_path}")
    private String phantomPath;

    @Value("${phantomjs_path}")
    private String phantomjsPath;

    @Autowired
    private BoardDao boardDao;

    private static int POOL_SIZE = 2;
    private static int cpuNums = Runtime.getRuntime().availableProcessors();
    private ExecutorService exePool = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);

    private static final ConcurrentMap<String, PersistContext> TASK_MAP = new ConcurrentHashMap<>();

    public PersistContext persist(Long dashboardId, String userId) {

        String persistId = UUID.randomUUID().toString().replaceAll("-", "");
        Process process = null;
        try {
            if (boardDao.getBoard(dashboardId) == null) {
                throw new CBoardException(String.format("Dashbaord ID [%s] doesn't exist!", dashboardId));
            }
            PersistContext context = new PersistContext(dashboardId);
            TASK_MAP.put(persistId, context);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            LocalSecurityFilter.put(uuid, userId);
            String phantomUrl = new StringBuffer(LocalSecurityFilter.getSchema())
                    .append("://127.0.0.1:")
                    .append(LocalSecurityFilter.getContext())
                    .append("/render")
                    .append("?sid=").append(uuid)
                    .append("#?id=").append(dashboardId)
                    .append("&pid=").append(persistId)
                    .toString();

            List cmd = Arrays.asList(phantomPath, phantomjsPath, phantomUrl);
            LOG.info("Run phantomjs command: {}", cmd);
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            process = builder.start();
            final Process p = process;

            exePool.execute(new Thread(() -> {
                InputStreamReader ir = new InputStreamReader(p.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String line;
                try {
                    while (null != (line = input.readLine())) {
                        LOG.info(line);
                    }
                    int rs = p.waitFor();
                    LOG.info("Finished command = {}, waitForResult = {}", cmd, rs);
                } catch (Exception e) {
                    LOG.error("Error", e);
                } finally {
                    p.destroy();
                    try {
                        ir.close();
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }));
            synchronized (context) {
                context.wait(10 * 60 * 1000);
            }
            TASK_MAP.remove(persistId);
            return context;
        } catch (Exception e) {
            LOG.error(getClass().getName(), e);
            throw new CBoardException(e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public String persistCallback(String persistId, JSONObject data) {
        PersistContext context = TASK_MAP.get(persistId);
        synchronized (context) {
            context.setData(data);
            context.notify();
        }
        return "1";
    }
}
