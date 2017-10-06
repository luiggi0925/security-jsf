package edu.ltmj.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/login.xhtml");

        http.authorizeRequests()
            // Some filters enabling url regex:
            // http://stackoverflow.com/a/8911284/1199132
            .regexMatchers(
                    "\\A/page1.xhtml\\?param1=true\\Z",
                    "\\A/page2.xhtml.*")
            .permitAll()
            //Permit access for all to error and denied views
            .antMatchers("/500.xhtml", "/denied.xhtml")
            .permitAll()
            // Only access with admin role
            .antMatchers("/config/**")
            .hasRole("ADMIN")
            //Permit access only for some roles
            .antMatchers("/page3.xhtml")
            .hasAnyRole("ADMIN", "MANAGEMENT")
            //If user doesn't have permission, forward him to login page
            .and()
            .formLogin();
    }
}
