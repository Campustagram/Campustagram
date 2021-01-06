package com.campustagram.app.controller;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.app.model.Image;
import com.campustagram.app.model.LikedImage;
import com.campustagram.app.repository.ImageRepository;
import com.campustagram.app.repository.LikedImageRepository;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "otherProfilePageController")
@Scope(value = "session")
@Component(value = "otherProfilePageController")
@Join(path = "/otherprofilepage", to = "/pages/app/otherprofilepage.jsf")
public class OtherProfilePageController {
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

	private static final String ACTIVE_CLASS_NAME = "DashboardController";

	private User activeUser;
	private User userToView = null;
	private List<Image> images;
	private Image selectedImage;

	@PostConstruct
	public void init() {
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		activeUser = activeUserService.fetchActiveUser();
		if (null == userToView) {
			userToView = activeUser;
		}
		images = imageRepository.findAllByUserIdNotDeleted(userToView.getId());

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
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

	public User getUserToView() {
		return userToView;
	}

	public void setUserToView(User userToView) {
		this.userToView = userToView;
	}

}
