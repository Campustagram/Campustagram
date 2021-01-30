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
				systemProperties.setDefaultNewUserRole(roleRepository.findByNameNotDeleted("systemRegulator"));
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

			
			User user1 = userRepository.findByEmailNotDeleted("sekuru@gtu.edu.tr");
			User user2 = userRepository.findByEmailNotDeleted("zafer.altay2016@gtu.edu.tr");
			User user3 = userRepository.findByEmailNotDeleted("ahmeteren.incir2016@gtu.edu.tr");
			User user4 = userRepository.findByEmailNotDeleted("mineisik@gtu.edu.tr");
			User user5 = userRepository.findByEmailNotDeleted("miray.yildiz2016@gtu.edu.tr");
			
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
			image6.setUser(user1);
			imageRepository.save(image6);

			Image image7 = new Image();
			image7.setImageURL("img/gtu/7.jpg");
			image7.setUser(user1);
			imageRepository.save(image7);

			Image image8 = new Image();
			image8.setImageURL("img/gtu/8.jpg");
			image8.setUser(user1);
			imageRepository.save(image8);

			Image image9 = new Image();
			image9.setImageURL("img/gtu/9.jpg");
			image9.setUser(user1);
			imageRepository.save(image9);

			Image image10 = new Image();
			image10.setImageURL("img/gtu/10.jpg");
			image10.setUser(user1);
			imageRepository.save(image10);
			
			Image image11 = new Image();
			image11.setImageURL("img/gtu/11.jpg");
			image11.setUser(user4);
			imageRepository.save(image11);
			

			Image image12 = new Image();
			image12.setImageURL("img/gtu/12.jpg");
			image12.setUser(user3);
			imageRepository.save(image12);

			Image image13 = new Image();
			image13.setImageURL("img/gtu/13.jpg");
			image13.setUser(user3);
			imageRepository.save(image13);

			Image image14 = new Image();
			image14.setImageURL("img/gtu/14.jpg");
			image14.setUser(user3);
			imageRepository.save(image14);

			Image image15 = new Image();
			image15.setImageURL("img/gtu/15.jpg");
			image15.setUser(user3);
			imageRepository.save(image15);
		
			
			Image image16 = new Image();
			image16.setImageURL("img/gtu/16.jpg");
			image16.setUser(user3);
			imageRepository.save(image16);

			Image image17 = new Image();
			image17.setImageURL("img/gtu/17.jpg");
			image17.setUser(user3);
			imageRepository.save(image17);

			Image image18 = new Image();
			image18.setImageURL("img/gtu/18.jpg");
			image18.setUser(user3);
			imageRepository.save(image18);

			Image image19 = new Image();
			image19.setImageURL("img/gtu/19.jpg");
			image19.setUser(user3);
			imageRepository.save(image19);
			
			Image image20 = new Image();
			image20.setImageURL("img/gtu/20.jpg");
			image20.setUser(user4);
			imageRepository.save(image20);
			
			Image image21 = new Image();
			image21.setImageURL("img/gtu/21.jpg");
			image21.setUser(user4);
			imageRepository.save(image21);

			Image image22 = new Image();
			image22.setImageURL("img/gtu/22.jpg");
			image22.setUser(user4);
			imageRepository.save(image22);

			Image image23 = new Image();
			image23.setImageURL("img/gtu/23.jpg");
			image23.setUser(user4);
			imageRepository.save(image23);

			Image image24 = new Image();
			image24.setImageURL("img/gtu/24.jpg");
			image24.setUser(user4);
			imageRepository.save(image24);

			Image image25 = new Image();
			image25.setImageURL("img/gtu/25.jpg");
			image25.setUser(user4);
			imageRepository.save(image25);
		
			
			Image image26 = new Image();
			image26.setImageURL("img/gtu/26.jpg");
			image26.setUser(user4);
			imageRepository.save(image26);

			Image image27 = new Image();
			image27.setImageURL("img/gtu/27.jpg");
			image27.setUser(user4);
			imageRepository.save(image27);

			Image image28 = new Image();
			image28.setImageURL("img/gtu/28.jpg");
			image28.setUser(user4);
			imageRepository.save(image28);

			Image image29 = new Image();
			image29.setImageURL("img/gtu/29.jpg");
			image29.setUser(user4);
			imageRepository.save(image29);

			Image image30 = new Image();
			image30.setImageURL("img/gtu/30.jpg");
			image30.setUser(user5);
			imageRepository.save(image30);
			
			Image image31 = new Image();
			image31.setImageURL("img/gtu/31.jpg");
			image31.setUser(user5);
			imageRepository.save(image31);

			Image image32 = new Image();
			image32.setImageURL("img/gtu/32.jpg");
			image32.setUser(user5);
			imageRepository.save(image32);

			Image image33 = new Image();
			image33.setImageURL("img/gtu/33.jpg");
			image33.setUser(user5);
			imageRepository.save(image33);

			Image image34 = new Image();
			image34.setImageURL("img/gtu/34.jpg");
			image34.setUser(user5);
			imageRepository.save(image34);

			Image image35 = new Image();
			image35.setImageURL("img/gtu/35.jpg");
			image35.setUser(user5);
			imageRepository.save(image35);
		
			
			Image image36 = new Image();
			image36.setImageURL("img/gtu/36.jpg");
			image36.setUser(user5);
			imageRepository.save(image36);

			Image image37 = new Image();
			image37.setImageURL("img/gtu/37.jpg");
			image37.setUser(user5);
			imageRepository.save(image37);

			Image image38 = new Image();
			image38.setImageURL("img/gtu/38.jpg");
			image38.setUser(user5);
			imageRepository.save(image38);

			Image image39 = new Image();
			image39.setImageURL("img/gtu/39.jpg");
			image39.setUser(user5);
			imageRepository.save(image39);
			
			Image image40 = new Image();
			image40.setImageURL("img/gtu/40.jpg");
			image40.setUser(user2);
			imageRepository.save(image2);
			
			Image image41 = new Image();
			image41.setImageURL("img/gtu/41.jpg");
			image41.setUser(user2);
			imageRepository.save(image41);

			Image image42 = new Image();
			image42.setImageURL("img/gtu/42.jpg");
			image42.setUser(user2);
			imageRepository.save(image42);

			Image image43 = new Image();
			image43.setImageURL("img/gtu/43.jpg");
			image43.setUser(user2);
			imageRepository.save(image43);

			Image image44 = new Image();
			image44.setImageURL("img/gtu/44.jpg");
			image44.setUser(user2);
			imageRepository.save(image44);

			Image image45 = new Image();
			image45.setImageURL("img/gtu/45.jpg");
			image45.setUser(user2);
			imageRepository.save(image45);
		
			
			Image image46 = new Image();
			image46.setImageURL("img/gtu/46.jpg");
			image46.setUser(user2);
			imageRepository.save(image46);

			Image image47 = new Image();
			image47.setImageURL("img/gtu/47.jpg");
			image47.setUser(user2);
			imageRepository.save(image47);

			Image image48 = new Image();
			image48.setImageURL("img/gtu/48.jpg");
			image48.setUser(user2);
			imageRepository.save(image48);

		};
	}
}