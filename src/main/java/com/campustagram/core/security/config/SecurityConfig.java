package com.campustagram.core.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.handlers.CustomAccessDeniedHandler;
import com.campustagram.core.security.handlers.CustomAuthenticationFailureHandler;
import com.campustagram.core.security.handlers.CustomAuthenticationSuccessHandler;
import com.campustagram.core.security.handlers.CustomLogoutSuccessHandler;
import com.campustagram.core.security.requestmatchers.MaintenanceRequestMatcher;
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.security.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private BCryptEncoderService bCryptEncoderService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(retrieveCustomUserDetailsService()).passwordEncoder(bCryptEncoderService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.securityContext()
			.securityContextRepository(databaseSecurityContextRepository())
		.and()
			.csrf().disable().authorizeRequests()			
			.antMatchers("/javax.faces.resource/**").permitAll()
			.antMatchers("/dashboard/**").authenticated()

			.antMatchers("/createnewpassword/**").permitAll()
			.antMatchers("/resetpassword/**").permitAll()
			.antMatchers("/verifycode/**").permitAll()
			.antMatchers("/proxycheck/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PROXY_CHECK_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/proxygroup/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PROXY_GROUP_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/proxylist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PROXY_LIST_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/useragentlist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/ticket/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SUPPORT_MANAGEMENT, PermissionType.PERMISSION_TICKET_OPEN))
			.antMatchers("/viewticket/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SUPPORT_MANAGEMENT, PermissionType.PERMISSION_TICKET_OPEN))
			.antMatchers("/loglist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_LOG_MANAGEMENT, PermissionType.PERMISSION_VIEW_PROCESS_LOGS))
			.antMatchers("/pagevisitloglist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_LOG_MANAGEMENT, PermissionType.PERMISSION_VIEW_PAGE_VISIT_LOGS))
			.antMatchers("/notification/**").authenticated()
			.antMatchers("/rolelist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_ROLE_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/createrole/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_ROLE_MANAGEMENT, PermissionType.PERMISSION_ADD))
			.antMatchers("/editrole/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_ROLE_MANAGEMENT, PermissionType.PERMISSION_EDIT))
			.antMatchers("/performance/cpu/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PERFORMANCE_CPU_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/performance/disc/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PERFORMANCE_DISC_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/performance/memory/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PERFORMANCE_MEMORY_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/performance/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_PERFORMANCE_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/system/settings/generalsettings/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_SETTINGS_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/system/settings/systememail/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/system/settings/**").fullyAuthenticated()
			.antMatchers("/system/settings/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_SETTINGS_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/system/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/dbdeadtupleschecker/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_DB_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/dbsizechecker/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_SYSTEM_DB_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/profile/security/**").fullyAuthenticated()
			.antMatchers("/profile/**").authenticated()
			.antMatchers("/adduserprofile/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_USER_MANAGEMENT, PermissionType.PERMISSION_ADD))
			.antMatchers("/userlist/**").hasAnyAuthority(buildPrivilegeString(RoleType.ROLE_USER_MANAGEMENT, PermissionType.PERMISSION_VIEW))
			.antMatchers("/userprofileedit/**").authenticated()
			.antMatchers("/login").permitAll()
			.antMatchers("/permissiondenied").permitAll()
			.antMatchers("/errorpage").permitAll()
			.antMatchers("/logout").permitAll()
			.antMatchers("/signup").permitAll()
			.antMatchers("/signupverifycode").permitAll()
			.antMatchers("/maintenance").permitAll()
			.antMatchers("/").permitAll()
		.and()
			.formLogin()
			.loginPage("/login")
			.successHandler(customAuthenticationSuccessHandler())
			.failureHandler(customAuthenticationFailureHandler())
		.and()
			.logout()
				.logoutUrl("/logout")
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID", CommonConstants.REMEMBER_ME_COOKIE_NAME)
				.logoutSuccessHandler(customLogoutSuccessHandler())	    	
		.and()
			.exceptionHandling()
				.accessDeniedHandler(customAccessDeniedHandler());
		//.and()
			//.hea
			//.headers("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
			  ///.header("Pragma", "no-cache");;
		//.and()
			//.rememberMe()
			// .rememberMeServices(rememberMeServices()).key("theKey")
			//.key("unique-and-secret").rememberMeCookieName(CommonConstants.REMEMBER_ME_COOKIE_NAME)
			//.rememberMeParameter("remember-me")
			// .rememberMeServices(rememberMeServices())
			// .authenticationSuccessHandler(customRememberAuthenticationSuccessHandler())
			//.tokenValiditySeconds(CommonConstants.COOKIE_LIFE);
		// .and()
		// .sessionManagement()
		// .sessionFixation().migrateSession()
		// .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		// .maximumSessions(1)
		// .expiredSessionStrategy(expiredSessionStrategy());
		// @formatter:on

	}
	
	private String buildPrivilegeString(RoleType roleType  , PermissionType permissionType) {
		return String.format("%s-%s", roleType.name() , permissionType.name());
	}
	
	@Bean
	public RequestMatcher maintenanceRequestMatcher() {
		return new MaintenanceRequestMatcher();
	}

	@Bean
	public SecurityContextRepository databaseSecurityContextRepository() {
		UserDetailsService userDetailsService = retrieveCustomUserDetailsService();
		SecurityContextRepository delegatingSecurityContextRepository = new HttpSessionSecurityContextRepository();

		return new DatabaseSecurityContextRepository(delegatingSecurityContextRepository, userDetailsService);
	}

	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public LogoutSuccessHandler customLogoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public UserDetailsService retrieveCustomUserDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
}