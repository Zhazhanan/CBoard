package org.cboard.cboardservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.cboard.cboardservice.security.HuhaPasswordEncoder;
import org.cboard.cboardservice.security.handler.LoginHandler;
import org.cboard.cboardservice.security.handler.LogoutHandler;
import org.cboard.cboardservice.security.service.DbUserDetailService;
import org.cboard.cboardservice.security.service.DefaultAuthenticationService;
import org.cboard.cboardservice.security.service.ShareAuthenticationProviderDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author WangKun
 * @create 2018-07-25
 * @desc
 **/
@EnableWebSecurity
public class SecurityJDBCConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityJDBCConfig.class);

    @Autowired
    private DruidDataSource dataSource;

    @Bean
    public DefaultAuthenticationService defaultAuthenticationService() {
        return new DefaultAuthenticationService();
    }

    @Bean
    public HuhaPasswordEncoder huhaPasswordEncoder() {
        return new HuhaPasswordEncoder();
    }

    @Bean
    public DbUserDetailService dbUserDetailService() {
        DbUserDetailService dbUserDetailService = new DbUserDetailService();
        dbUserDetailService.setDataSource(dataSource);
        dbUserDetailService.setAuthoritiesByUsernameQuery("SELECT login_name username, 'admin' AS authority FROM dashboard_user WHERE login_name = ?");
        dbUserDetailService.setUsersByUsernameQuery("SELECT user_id,user_name,login_name, user_password, 1 AS enabled FROM dashboard_user WHERE login_name = ? ");
        return dbUserDetailService;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(dbUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(huhaPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public ShareAuthenticationProviderDecorator shareAuthenticationProviderDecorator() {
        ShareAuthenticationProviderDecorator share = new ShareAuthenticationProviderDecorator();
        share.setAuthenticationProvider(daoAuthenticationProvider());
        return share;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(shareAuthenticationProviderDecorator());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/render",
                "/h2/*",
                "/lib/**",
                "/dist/**",
                "/bootstrap/**",
                "/plugins/**",
                "/js/**",
                "/fonts/**",
                "/imgs/**",
                "/css/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated();
        http.sessionManagement()
                .sessionFixation().migrateSession()
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
//                .expiredUrl("/login?expired-session")
//                .and()
                .invalidSessionUrl("/login?invalid-session");
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new LoginHandler())
                .permitAll();
        http.logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutUrl("/j_spring_cas_security_logout")
                .logoutSuccessHandler(new LogoutHandler())
                .permitAll();
        http.httpBasic();
        http.rememberMe().rememberMeParameter("remember_me");
        http.csrf().disable();
    }

}
