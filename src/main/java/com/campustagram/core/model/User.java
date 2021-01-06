package com.campustagram.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.campustagram.app.model.*;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7328086949586556022L;

	@Id
	@SequenceGenerator(name = "users_seq", sequenceName = "users_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
	@Column(nullable = false)
	private Long id;

	@Size(max = 65)
	@NotNull
	private String name;
	@Size(max = 65)
	private String lastname;
	private String email;
	private String password;
	@Column(length = 512)
	private String address;
	@Column(columnDefinition = "TEXT")
	private String profileImageURL = "img/user_profile_img/user.png";
	@Column(columnDefinition = "TEXT")
	private String profileImageDescription;
	@Column(length = 12)

	private Date birthDate;
	private Date lastSeen = CommonDate.currentDate();
	private Date createDate = CommonDate.currentDate();

	private boolean isActive = true;
	private boolean isDeleted = false;
	private boolean isOnline = false;
	private boolean isEmailAllowed = false;
	private boolean isPasswordOld = false;

	// ====================================================================================================================
	// THEME :
	// ====================================================================================================================
	private boolean theme_darkMenu = false;
	private boolean theme_horizontal = false;
	private boolean theme_orientationRTL = false;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "user")
	private ResetPassword resetPassword;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	@OneToMany(mappedBy = "user", targetEntity = RememberMe.class, fetch = FetchType.LAZY, cascade = {
			CascadeType.ALL })
	private List<RememberMe> rememberMe = new ArrayList<>();

	@OneToMany(mappedBy = "user", targetEntity = Log.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Log> log = new ArrayList<>();

	@OneToMany(mappedBy = "processUser", targetEntity = Log.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Log> processLogs = new ArrayList<>();

	@OneToMany(mappedBy = "updateUser", targetEntity = SystemProperties.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SystemProperties> systemProperties = new ArrayList<>();

	@OneToMany(mappedBy = "user", targetEntity = Notification.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(mappedBy = "processUser", targetEntity = Notification.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Notification> processNotifications = new ArrayList<>();

	@OneToMany(mappedBy = "processedUser", targetEntity = Notification.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Notification> processedNotifications = new ArrayList<>();

	@ManyToOne(targetEntity = Language.class)
	@JoinColumn(name = "language_id")
	private Language language;

	/*
	 * @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
	 * 
	 * @JoinTable(name = "users_liked", joinColumns = @JoinColumn(name = "user_id"),
	 * inverseJoinColumns = @JoinColumn(name = "image_id")) private Set<Image>
	 * likedPhotos = new HashSet<>();
	 */
	// kişinin fotoğrafları
	@OneToMany(mappedBy = "user", targetEntity = Image.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private List<Image> images = new ArrayList<>();

	// kişinin beğendiği fotoğraf
/*	@ManyToOne(targetEntity = Image.class)
	@JoinColumn(name = "image_id")
	private Image likedImage;
*/
	@OneToMany(mappedBy = "user", targetEntity = LikedImage.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<LikedImage> likedImages ;
	
	public User() {
		super();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		for (RoleType roleType : getRole().getPrivileges().keySet()) {

			for (PermissionType permissionType : getRole().getPrivileges().get(roleType)) {
				authorities.add(new SimpleGrantedAuthority(roleType.name() + "-" + permissionType.name()));
			}

			authorities.add(new SimpleGrantedAuthority(roleType.name()));
		}

		return authorities;
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return !isDeleted();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isActive();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !isPasswordOld();
	}

	@Override
	public boolean isEnabled() {
		return isActive();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email + ", address="
				+ address + ", birthDate=" + birthDate + ", lastSeen=" + lastSeen + ", createDate=" + createDate
				+ ", isEmailAllowed=" + isEmailAllowed + ", roles=" + role + ", language=" + language + "]";
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof User) && (id != null) ? id.equals(((User) other).id) : (other == this);
	}

	@Override
	public int hashCode() {
		return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
	}

	public String getFullName() {
		return name + " " + lastname;
	}

	public boolean checkOnlineStatus() {
		if (this.isOnline && (CommonDate.computeDiffBetween2Minutes(CommonDate.currentDate(), this.lastSeen) < 5)) {
			return true;
		}
		return false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public ResetPassword getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
	}

	public List<RememberMe> getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(List<RememberMe> rememberMe) {
		this.rememberMe = rememberMe;
	}

	public List<Log> getLog() {
		return log;
	}

	public void setLog(List<Log> log) {
		this.log = log;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isTheme_horizontal() {
		return theme_horizontal;
	}

	public void setTheme_horizontal(boolean theme_horizontal) {
		this.theme_horizontal = theme_horizontal;
	}

	public boolean isTheme_darkMenu() {
		return theme_darkMenu;
	}

	public void setTheme_darkMenu(boolean theme_darkMenu) {
		this.theme_darkMenu = theme_darkMenu;
	}

	public boolean isTheme_orientationRTL() {
		return theme_orientationRTL;
	}

	public void setTheme_orientationRTL(boolean theme_orientationRTL) {
		this.theme_orientationRTL = theme_orientationRTL;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isEmailAllowed() {
		return isEmailAllowed;
	}

	public void setEmailAllowed(boolean isEmailAllowed) {
		this.isEmailAllowed = isEmailAllowed;
	}

	public List<SystemProperties> getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(List<SystemProperties> systemProperties) {
		this.systemProperties = systemProperties;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Notification> getProcessNotifications() {
		return processNotifications;
	}

	public void setProcessNotifications(List<Notification> processNotifications) {
		this.processNotifications = processNotifications;
	}

	public List<Notification> getProcessedNotifications() {
		return processedNotifications;
	}

	public void setProcessedNotifications(List<Notification> processedNotifications) {
		this.processedNotifications = processedNotifications;
	}

	public boolean isPasswordOld() {
		return isPasswordOld;
	}

	public void setPasswordOld(boolean isPasswordOld) {
		this.isPasswordOld = isPasswordOld;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	/*
	 * public Set<Image> getLikedPhotos() { return likedPhotos; }
	 * 
	 * public void setLikedPhotos(Set<Image> likedPhotos) { this.likedPhotos =
	 * likedPhotos; }
	 */
	public List<Log> getProcessLogs() {
		return processLogs;
	}

	public void setProcessLogs(List<Log> processLogs) {
		this.processLogs = processLogs;
	}

	public String getProfileImageDescription() {
		return profileImageDescription;
	}

	public void setProfileImageDescription(String profileImageDescription) {
		this.profileImageDescription = profileImageDescription;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
/*
	public Image getLikedImage() {
		return likedImage;
	}

	public void setLikedImage(Image likedImage) {
		this.likedImage = likedImage;
	}
*/
}
