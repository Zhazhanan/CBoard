package org.cboard.cboardservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangKun
 * @create 2018-07-31
 * @desc
 **/
@Configuration
@Data
public class PropertiesConfig {

    @Value("${spring.datasource.druidDataSource.validationQuery:SELECT 1}")
    private String validationQuery;

    @Value("${spring.datasource.druidDataSource.username:root}")
    private String jdbcUsername;

    @Value("${spring.datasource.druidDataSource.password:root}")
    private String jdbcPassword;

    @Value("${spring.datasource.druidDataSource.url:jdbc:mysql://127.0.0.1:3306/cboard}")
    private String jdbcUrl;

    @Value("${spring.h2.url:jdbc:h2:~/H2Data/cboard;AUTO_SERVER=TRUE;LOG=0;UNDO_LOG=0}")
    private String h2Url;

    @Value("${spring.h2.user.name:sa}")
    private String h2UserName;

    @Value("${spring.h2.database.name:cboard}")
    private String h2DatabaseName;

    @Value("${spring.h2.cleanjob.quarz:0 1 0 * * ?}")
    private String h2Quarz;

}
