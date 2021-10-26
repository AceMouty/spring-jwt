package io.acemouty.springjwt.config;

import io.acemouty.springjwt.config.httpfilter.CustomAuthenticationFilter;
import io.acemouty.springjwt.config.httpfilter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
  // beans created with overrides
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean()
          throws Exception
  {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception
  {
    // tell spring how to look for users
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    // change the login url from the default /login to /api/login
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
    customAuthenticationFilter.setFilterProcessesUrl("/api/login");

    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // allow custom login url to be used
    http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/login").permitAll();
    http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
    http.authorizeRequests()
            .anyRequest().authenticated();
    // http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));

    // run this filter first before anything else in the application
    http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    // use our filter with configured setting vs the default
    http.addFilter(customAuthenticationFilter);
  }
}
