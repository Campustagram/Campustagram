package com.campustagram.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.campustagram.core.model.User;

@Entity
public class LikedImage {
	private static final long serialVersionUID = 7328086949586556022L;

	@Id
	@SequenceGenerator(name = "images_seq", sequenceName = "images_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_seq")
	@Column(nullable = false)
	private Long id;

    @ManyToOne(targetEntity = Image.class)
    @JoinColumn(name = "image_id")
    Image image;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}