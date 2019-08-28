package org.cboard.cboardservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;

/**
 * @author WangKun
 * @create 2018-07-25
 * @desc
 **/
@Import(PropertiesConfig.class)
@Configuration
public class DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private PropertiesConfig propertiesConfig;

    @Bean(name = "h2DataSource")
    public BasicDataSource basicDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl(propertiesConfig.getH2Url());
        basicDataSource.setUsername(propertiesConfig.getH2UserName());
        basicDataSource.setPassword("");
        basicDataSource.setMaxTotal(20);
        LOGGER.info("----------------------h2DataSource init ok----------------------");
        return basicDataSource;
    }

    @Primary
    @Bean(name = "druidDataSource")
    public DruidDataSource druidDataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setName("CBoard Meta Data");
        ds.setUrl(propertiesConfig.getJdbcUrl());
        ds.setUsername(propertiesConfig.getJdbcUsername());
        ds.setPassword(propertiesConfig.getJdbcPassword());
        ds.setInitialSize(0);
        ds.setMaxActive(20);
        ds.setMinIdle(0);
        ds.setMaxWait(60000);
        ds.setValidationQuery(propertiesConfig.getValidationQuery());
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(25200000);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(1800);
        ds.setLogAbandoned(true);
        try {
//            ds.setFilters("mergeStat,log4j");
            ds.setFilters("mergeStat");
        } catch (SQLException e) {
            LOGGER.error("dataSourceConfig init error", e);
        }
        LOGGER.info("----------------------dataSourceConfig init ok----------------------");
        return ds;
    }

//    @Bean(name = "sqlSessionFactory")
//    public SqlSessionFactoryBean sqlSessionFactoryBean(DruidDataSource druidDataSource) {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(druidDataSource);
//        try {
//            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
//            Properties properties = new Properties();
//            InputStream in = DataSourceConfig.class.getClassLoader().getResourceAsStream("application.yml");
//            properties.load(in);
//            sqlSessionFactoryBean.setConfigurationProperties(properties);
//        } catch (IOException e) {
//            LOGGER.error("sqlSessionFactoryBean setMapperLocations is error", e);
//        }
//        return sqlSessionFactoryBean;
//    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(DruidDataSource druidDataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(druidDataSource);
        return dataSourceTransactionManager;
    }

    @Bean(name = "druid-stat-interceptor")
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean(name = "druid-stat-pointcut")
    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
        JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
        jdkRegexpMethodPointcut.setPattern("org.cboard.dao.*");
        return jdkRegexpMethodPointcut;
    }

    @Bean
    public Advisor druidStatAdvisor(JdkRegexpMethodPointcut jdkRegexpMethodPointcut, DruidStatInterceptor druidStatInterceptor) {
        return new DefaultPointcutAdvisor(jdkRegexpMethodPointcut, druidStatInterceptor);
    }
}
