package com.campustagram;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.campustagram.app.model.Image;
import com.campustagram.app.repository.ImageRepository;
import com.campustagram.core.demo.controller.CreateLanguageDemo;
import com.campustagram.core.demo.controller.CreateUserDemo;
import com.campustagram.core.enums.SystemProcessType;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.SystemInformation;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.EmailTemplateRepository;
import com.campustagram.core.persistence.LanguageRepository;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.persistence.SystemInformationRepository;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.util.RolePermissionEnumsUtils;

@EnableAsync
@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableScheduling
@ComponentScan({ "com.campustagram" })
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationMain extends SpringBootServletInitializer {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	@Autowired
	private SystemInformationRepository systemInformationRepository;
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
	@Autowired
	private ImageRepository imageRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationMain.class);
	}

	@Bean
	public ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		return new ServletRegistrationBean<>(servlet, "*.jsf");
	}

	@Bean
	public FilterRegistrationBean<RewriteFilter> rewriteFilter() {
		FilterRegistrationBean<RewriteFilter> rwFilter = new FilterRegistrationBean<>(new RewriteFilter());
		rwFilter.setDispatcherTypes(
				EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR));
		rwFilter.addUrlPatterns("/*");
		return rwFilter;
	}

	@Bean
	InitializingBean startup() {
		return () -> {
			System.out.println("MainThreadName: " + Thread.currentThread().getName());
			System.out.println("MainThreadId: " + Thread.currentThread().getId());

			SystemInformation systemInformation = new SystemInformation();
			systemInformation.setProcessType(SystemProcessType.START.toString());
			systemInformation = systemInformationRepository.save(systemInformation);

			// TODO send mail something goes wrong
			EmailTemplate emailTemplate = new EmailTemplate();
			emailTemplate.setTo("dodgehellcat3478@gmail.com");
			emailTemplate.setFrom("WebScraper");
			emailTemplate.setSubject("System is " + SystemProcessType.START.toString());
			emailTemplate.setContent("systemInformation Info: " + systemInformation);

			emailTemplateRepository.save(emailTemplate);

			// For Regular Usage
			CreateLanguageDemo createLanguageDemo = new CreateLanguageDemo(languageRepository);
			createLanguageDemo.createLanguage("tr", "tr");
			createLanguageDemo.createLanguage("en", "en");

			List<Role> demoRoleToBeSaved = new ArrayList<>();

			Role systemMaintainer = new Role();
			systemMaintainer.setName("systemMaintainer");
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SYSTEM_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_USER_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_USER_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_NOTIFICATION_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_NOTIFICATION_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_LOG_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_LOG_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SYSTEM_DB_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_DB_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT));
			systemMaintainer.getPrivileges().put(RoleType.ROLE_SUPPORT_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SUPPORT_MANAGEMENT));
			demoRoleToBeSaved.add(systemMaintainer);

			Role regularUser = new Role();
			regularUser.setName("regularUser");
			demoRoleToBeSaved.add(regularUser);

			Role humanResources = new Role();
			humanResources.setName("humanResources");
			humanResources.getPrivileges().put(RoleType.ROLE_USER_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_USER_MANAGEMENT));
			humanResources.getPrivileges().put(RoleType.ROLE_ROLE_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_ROLE_MANAGEMENT));
			demoRoleToBeSaved.add(humanResources);

			Role systemRegulator = new Role();
			systemRegulator.setName("systemRegulator");
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_ROLE_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_ROLE_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_USER_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_USER_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_NOTIFICATION_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_NOTIFICATION_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_LOG_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_LOG_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SUPPORT_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SUPPORT_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_LOG_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_LOG_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_SETTINGS_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_SETTINGS_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PROXY_LIST_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PROXY_LIST_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PROXY_GROUP_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PROXY_GROUP_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PROXY_CHECK_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PROXY_CHECK_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_DB_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_DB_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PERFORMANCE_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PERFORMANCE_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PERFORMANCE_CPU_MANAGEMENT,
					RolePermissionEnumsUtils
							.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PERFORMANCE_CPU_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PERFORMANCE_MEMORY_MANAGEMENT,
					RolePermissionEnumsUtils
							.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PERFORMANCE_MEMORY_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_PERFORMANCE_DISC_MANAGEMENT,
					RolePermissionEnumsUtils
							.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_PERFORMANCE_DISC_MANAGEMENT));
			systemRegulator.getPrivileges().put(RoleType.ROLE_SYSTEM_DOCUMENTATION_MANAGEMENT,
					RolePermissionEnumsUtils.availablePermissionsOfRole(RoleType.ROLE_SYSTEM_DOCUMENTATION_MANAGEMENT));
			demoRoleToBeSaved.add(systemRegulator);

			for (Role demoRole : demoRoleToBeSaved) {

				Role tmpRole = roleRepository.findByNameNotDeleted(demoRole.getName());
				if ((null == tmpRole) || (null == tmpRole.getId())) {
					roleRepository.save(demoRole);
				}
			}

			SystemProperties systemProperties = new SystemProperties();
			if (systemPropertiesRepository.count() < 1) {
				systemProperties.setDefaultNewUserRole(roleRepository.findByNameNotDeleted("regularUser"));
				systemPropertiesRepository.save(systemProperties);
				System.out.println("SystemProperties oluşturuldu.");
			} else {
				System.out.println("SystemProperties zaten mevcut oluşturulmadı.");
			}

			CreateUserDemo createUserDemo = new CreateUserDemo(userRepository, languageRepository,
					bCryptEncoderService);

			createUserDemo.createUser("a@a.com", "a", "a", systemRegulator);
			
			createUserDemo.createUser("sekuru@gtu.edu.tr", "salih emre", "kuru", systemRegulator,"img/user_profile_img/1.jpg");
			createUserDemo.createUser("zafer.altay2016@gtu.edu.tr", "zafer", "altay", systemRegulator,"img/user_profile_img/2.jpg");
			createUserDemo.createUser("ahmeteren.incir2016@gtu.edu.tr", "ahmet eren", "incir", systemRegulator,"img/user_profile_img/3.jpg");
			createUserDemo.createUser("mineisik@gtu.edu.tr", "mine", "ışık", systemRegulator,"img/user_profile_img/4.jpg");
			createUserDemo.createUser("miray.yildiz2016@gtu.edu.tr", "miray", "yıldız", systemRegulator,"img/user_profile_img/5.jpg");

			
			User user1 = userRepository.findByEmailNotDeleted("zafer.altay2016@gtu.edu.tr");
			User user2 = userRepository.findByEmailNotDeleted("sekuru@gtu.edu.tr");
			Image image1 = new Image();
			image1.setImageURL("img/gtu/1.jpg");
			image1.setUser(user1);
			imageRepository.save(image1);

			Image image2 = new Image();
			image2.setImageURL("img/gtu/2.jpg");
			image2.setUser(user1);
			imageRepository.save(image2);

			Image image3 = new Image();
			image3.setImageURL("img/gtu/3.jpg");
			image3.setUser(user1);
			imageRepository.save(image3);

			Image image4 = new Image();
			image4.setImageURL("img/gtu/4.jpg");
			image4.setUser(user1);
			imageRepository.save(image4);

			Image image5 = new Image();
			image5.setImageURL("img/gtu/5.jpg");
			image5.setUser(user1);
			imageRepository.save(image5);
		
			
			Image image6 = new Image();
			image6.setImageURL("img/gtu/6.jpg");
			image6.setUser(user2);
			imageRepository.save(image6);

			Image image7 = new Image();
			image7.setImageURL("img/gtu/7.jpg");
			image7.setUser(user2);
			imageRepository.save(image7);

			Image image8 = new Image();
			image8.setImageURL("img/gtu/8.jpg");
			image8.setUser(user2);
			imageRepository.save(image8);

			Image image9 = new Image();
			image9.setImageURL("img/gtu/9.jpg");
			image9.setUser(user2);
			imageRepository.save(image9);

			Image image10 = new Image();
			image10.setImageURL("img/gtu/10.jpg");
			image10.setUser(user2);
			imageRepository.save(image10);
			
			Image image11 = new Image();
			image11.setImageURL("img/gtu/11.jpg");
			image11.setUser(user2);
			imageRepository.save(image11);
		};
	}
}