package com.omfgdevelop.jiratelegrambot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.security.enabled}")
    private boolean securityEnabled;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(passwordEncoder.encode(adminPassword)).roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(HttpMethod.POST, "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (securityEnabled) {
            http.anonymous().and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/").anonymous()
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/").hasRole("ADMIN")
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/display_project").hasRole("ADMIN")
                    .and()
                    .formLogin()
                    .permitAll()
                    .defaultSuccessUrl("/admin", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                    .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .key("secured")
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login")
            ;
        } else {
            http
                    .csrf().disable().sessionManagement().disable().formLogin().disable().httpBasic().disable().anonymous();
        }
    }
}
