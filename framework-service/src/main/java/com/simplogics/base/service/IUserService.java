package com.simplogics.base.service;

import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.exception.FrameworkException;

import com.simplogics.base.dto.UserResponse;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface IUserService {

	UserResponse findById(long id) throws FrameworkException;

	User findUserByUsername(String username) throws FrameworkException;

	void updatePassword(String email, String encodedPassword) throws FrameworkException;

	void createDefaultUser(String encodedPassword, Role role);

	List<UserResponse> getUsers(String search, List<Long> roleIds, Boolean isActive, Long pageSize, Long pageNumber, Sort sort);
}
