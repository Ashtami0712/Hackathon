package com.simplogics.base.service;

import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.exception.FrameworkException;
import com.simplogics.base.mapper.UserMapper;
import com.simplogics.base.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserResponse findById(long id) throws FrameworkException {
		User user = userRepository.findById(id).orElseThrow(()->
				new FrameworkException("user.id.not.found", HttpStatus.BAD_REQUEST));
		return UserMapper.convertEntityToDto(user);
	}

	@Override
	public User findUserByUsername(String username) throws FrameworkException {
		return userRepository.findByEmail(username).orElseThrow(()->
				new FrameworkException("user.email.not.found", HttpStatus.BAD_REQUEST));
	}

	@Override
	public void updatePassword(String email, String encodedPassword) throws FrameworkException {
		User user = findUserByUsername(email);
		user.setPassword(encodedPassword);
		userRepository.save(user);
	}

	@Override
	public void createDefaultUser(String encodedPassword, Role role) {
		if (userRepository.count() == 0) {
			List<Role> roles = new ArrayList<>();
			roles.add(role);

			User defaultUser = new User();
			defaultUser.setName("Super Admin Framework");
			defaultUser.setEmail("super.admin@simplogics.com");
			defaultUser.setPassword(encodedPassword);
			defaultUser.setUserRoles(roles);
			userRepository.save(defaultUser);
		}
	}

	@Override
	public List<UserResponse> getUsers(String search, List<Long> roleIds, Boolean isActive, Long pageSize, Long pageNumber, Sort sort) {
		Specification<User> spec = UserSpecification.filterUsers(search, roleIds, isActive);
		Page<User> users = userRepository.findAll(spec, PageRequest.of(pageNumber.intValue(), pageSize.intValue(), sort));
		return users.stream().map(UserMapper::convertEntityToDto).collect(Collectors.toList());
	}

}
