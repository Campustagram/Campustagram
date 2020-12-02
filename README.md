## JavaServer Faces with Spring Boot
#jQuery
-find version of the jQuery : console.log(jQuery().jquery);
#PostgreSQL Configuration
- PostgreSQL 11 Configuration File:
- Configuration tool: https://www.pgconfig.org/#/tuning?total_ram=8&max_connections=100&environment_name=OLTP&pg_version=10&os_type=Windows&arch=x86-64&drive_type=HDD&share_link=true
- Windows:
-	Path: C:\PostgreSQL\data\pg11
-	File: postgresql.conf

#Memory Configuration	
- shared_buffers = 128MB => shared_buffers = 512MB
- #effective_cache_size = 4GB => effective_cache_size = 6GB
- #work_mem = 4MB	 => work_mem = 205MB
- maintenance_work_mem = 64MB => maintenance_work_mem = 512MB

#Checkpoint Related Configuration
- min_wal_size = 80MB => min_wal_size = 1GB
- max_wal_size = 1GB => max_wal_size = 3GB
- #checkpoint_completion_target = 0.5 => checkpoint_completion_target = 0.7
- #wal_buffers = -1	# min 32kB, -1 sets based on shared_buffers => wal_buffers = 16MB	#3% of shared_buffers or 64KB at the minimum
 
#Network Related Configuration 
- listen_addresses = 'localhost' => listen_addresses = '*'
- max_connections = 300 => max_connections = 40			# (change requires restart)

#Hard Drive Configuration
- #random_page_cost = 4.0	=> #random_page_cost = 4
- #effective_io_concurrency = 0 => effective_io_concurrency = 2 # 1-1000; 0 disables prefetching

#Schedule
- The pattern is
- second, minute, hour, day, month, weekday
- 0 0 * * * * = the top of every hour of every day.
- */10 * * * * * = every ten seconds.
- 0 0 8-10 * * * = 8, 9 and 10 o'clock of every day.
- 0 0 6,19 * * * = 6:00 AM and 7:00 PM every day.
- 0 0/30 8-10 * * * = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.
- 0 0 9-17 * * MON-FRI = on the hour nine-to-five weekdays
- 0 0 0 25 12 ? = every Christmas Day at midnight

#Active User
-Access is 
	@Autowired
	private ActiveUserService activeUserService;
-Usage is
	activeUserService.fetchActiveUser()

#Logging
-Access is
	@Autowired
	private LoggerService loggerService;
-Usage is
	loggerSerevice.writeInfo(...)

#Encoding
-BCrypt algorithm is one way(reversing not possible)
-Access is 
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
-Usage is
	For encoding use "encode" method
		updateSecurityContextService.updateSecurityContextForPasswordChange(bCryptEncoderService.encode(user.getPassword()));
	For comparing use "matches"
		bCryptEncoderService.matches(password , activeUserService.fetchActiveUser().getPassword()))

#Safely iterating list(method "safeList" in CommonFunctions)
-This method prevents NullPointerException in loops
-Usage is
	for (User informUser : CommonFunctions.safeList(userList)) {
		//.....
	}

#Remember Me
-Spring "remember-me" as id by default
-Use CommonConstants.REMEMBER_ME_COOKIE_NAME

#User Roles
-One user can have one role
-A Role has role types(enum RoleType)--------(may be zero or more)
-A RoleType has permission types(enum PermissionType)--------(may be zero or more)
-This is done by map(EnumMap<RoleType , Set<PermissionType>) in the Role class 
	public class Role implements Serializable {
		....
		private EnumMap<RoleType, Set<PermissionType>> privileges = new EnumMap<>(RoleType.class);
		...
	}
		
#Email and Password Change
-When active user's email or password changes Security Context must be updated
-Access
	@Autowired
	private UpdateSecurityContextService updateSecurityContextService;
-Usage	
	For password:
		updateSecurityContextService.updateSecurityContextForPasswordChange(bCryptEncoderService.encode(user.getPassword()));
	For email:
		updateSecurityContextService.updateSecurityContextForEmailChange(tmpUser.getEmail());

#Logout
-For logout , no need for a controller or a method
	Just go to "/logout" URL
-CustomLogoutSuccessHandler is called automatically

#Logging in
-CustomUserDetailsService.loadByUserName is automatically called for looking up user
-CustomAuthenticationtSuccessHandler is called automatically

