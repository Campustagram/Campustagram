package com.campustagram.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.campustagram.core.model.Notification;
import com.campustagram.core.model.User;

@Entity
@Table(name = "images")
public class Image implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7328086949586556022L;

	@Id
	@SequenceGenerator(name = "images_seq", sequenceName = "images_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_seq")
	@Column(nullable = false)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String imageURL = "img/image_not_found.jpg";
	@Column(columnDefinition = "TEXT")
	private String imageDescription = "...";

	// kişinin fotoğrafları
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	// fotoğrafı beğenen kişiler
	/*
	 * @OneToMany(mappedBy = "likedImage", targetEntity = User.class, fetch =
	 * FetchType.LAZY, cascade = { CascadeType.ALL }) private List<User>
	 * usersWhoLikedTheImage = new ArrayList<>();
	 */
	@OneToMany(mappedBy = "image", targetEntity = LikedImage.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<LikedImage> likedImages;

	private Date creationDate = new Date(System.currentTimeMillis());
	private Date dateOfUpdate = new Date(System.currentTimeMillis());
	private boolean isDeleted = false;

	public Image() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getImageDescription() {
		return imageDescription;
	}

	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDateOfUpdate() {
		return dateOfUpdate;
	}

	public void setDateOfUpdate(Date dateOfUpdate) {
		this.dateOfUpdate = dateOfUpdate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	/*
	 * public List<User> getUsersWhoLikedTheImage() { return usersWhoLikedTheImage;
	 * }
	 * 
	 * public void setUsersWhoLikedTheImage(List<User> usersWhoLikedTheImage) {
	 * this.usersWhoLikedTheImage = usersWhoLikedTheImage; }
	 */

	public Set<LikedImage> getLikedImages() {
		return likedImages;
	}

	public void setLikedImages(Set<LikedImage> likedImages) {
		this.likedImages = likedImages;
	}

}
