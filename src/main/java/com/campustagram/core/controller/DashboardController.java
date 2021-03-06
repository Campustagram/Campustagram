package com.campustagram.core.controller;

import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.app.controller.OtherProfilePageController;
import com.campustagram.app.controller.ProfilePageController;
import com.campustagram.app.model.Image;
import com.campustagram.app.model.LikedImage;
import com.campustagram.app.repository.ImageRepository;
import com.campustagram.app.repository.LikedImageRepository;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "dashboardController")
@Scope(value = "session")
@Component(value = "dashboardController")
@Join(path = "/dashboard", to = "/pages/core/dashboard.jsf")
public class DashboardController {

	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private LikedImageRepository likedImageRepository;
	@Autowired
	private OtherProfilePageController otherProfilePageController;
	
	private static final String ACTIVE_CLASS_NAME = "DashboardController";

	private User activeUser;
	private List<Image> images;
	private Image selectedImage;

	@PostConstruct
	public void init() {
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		images = imageRepository.findAllNotDeleted();
		activeUser = activeUserService.fetchActiveUser();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}
	
	public String goToUserProfilePage(Image image) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "goToUserProfilePage", null, CommonConstants.START);
		
		otherProfilePageController.setUserToView(image.getUser());
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "goToUserProfilePage", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/otherprofilepage");
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Image getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Image selectedImage) {
		this.selectedImage = selectedImage;
	}

	private void showMessage(String clientId) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
	}

	public String likeButtonImageOfImage(Image image) {
		boolean status = doesTheActiveUserLikeThePhoto(image);
		if (!status) {
			return "img/gtu/like.jpg";
		} else {
			return "img/gtu/liked.jpg";
		}
	}

	public void clickLikeButton(Image image) {
		final String ACTIVE_METHOD_NAME = "clickLikeButton";
		System.out.println("clickLikeButton");
		boolean status = doesTheActiveUserLikeThePhoto(image);

		if (!status) {
			LikedImage likedImage = new LikedImage();
			likedImage.setImage(image);
			likedImage.setUser(activeUser);
			likedImageRepository.save(likedImage);

		} else {
			LikedImage likedImage = likedImageRepository.findByUserIdAndImageId(activeUser.getId(), image.getId());
			likedImage.setImage(null);
			likedImage.setUser(null);
			likedImageRepository.save(likedImage);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "likeStatusChanged", CommonConstants.END);
	}

	public boolean doesTheActiveUserLikeThePhoto(Image image) {
		if (null == image) {
			return false;
		}
		LikedImage likedImage = likedImageRepository.findByUserIdAndImageId(activeUser.getId(), image.getId());
		
		if (null == likedImage) {
			return false;
		} else {
			return true;
		}
	}

	public int randomInt(Image image) {
		return (int) (1 + (Math.random() * (99999 - 1)));
	}

	public int numberOfLikes(Image image) {
		return likedImageRepository.findAllByImageId(image.getId()).size();
	}

	public int getTotalUsers() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTotalUsers", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTotalUsers", null, CommonConstants.END);
		return userRepository.countAllNotDeleted();
	}

	public int getOnlineUsers() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getOnlineUsers", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getOnlineUsers", null, CommonConstants.END);
		return userRepository.countAllOnline();
	}

	public boolean randomBoolean() {
		return new Random().nextBoolean();
	}

	public User getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}

}