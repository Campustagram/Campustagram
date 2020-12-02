package com.campustagram.core.security.service;

import com.campustagram.core.model.User;

public interface IActiveUserService {
	User fetchActiveUser();
}
