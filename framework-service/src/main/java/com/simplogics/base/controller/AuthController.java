package com.simplogics.base.controller;


import com.simplogics.base.dto.ForgetPasswordRequest;
import com.simplogics.base.dto.FrameworkBaseResponse;
import com.simplogics.base.dto.LoginResponse;
import com.simplogics.base.dto.ResetPasswordRequest;
import com.simplogics.base.entity.User;
import com.simplogics.base.service.IPasswordService;
import com.simplogics.base.service.IUserService;
import com.simplogics.base.utils.ApiRoutes;
import com.simplogics.base.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simplogics.base.annotation.APIResult;
import com.simplogics.base.service.IAuthService;
import com.simplogics.base.dto.LoginRequest;

@RestController
@RequestMapping(ApiRoutes.AUTH)
public class AuthController {

	@Autowired
	private IAuthService authService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IPasswordService passwordService;
	
	@PostMapping(value = ApiRoutes.LOGIN)
	@APIResult(error_message = "user.auth.fail")
	public ResponseEntity<FrameworkBaseResponse> login(@Valid @RequestBody LoginRequest loginDto) throws  Exception {
		User user = userService.findUserByUsername(loginDto.getEmail());
		LoginResponse response = authService.login(loginDto, user);
		return ResponseUtil.getStatusOkResponseEntity(response, "user.auth.success");
	}

	@PostMapping(value = ApiRoutes.FORGOT_PASSWORD)
	@APIResult(error_message = "forgot.password.fail")
	public ResponseEntity<FrameworkBaseResponse> forgotPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) throws  Exception {
		User user = userService.findUserByUsername(forgetPasswordRequest.getEmail());
		authService.forgetPassword(user);
		return ResponseUtil.getStatusOkResponseEntity(null, "forgot.password.success");
	}

	@RequestMapping(value = ApiRoutes.RESET_PASSWORD, method = RequestMethod.POST)
	@APIResult(error_message = "reset.password.fail")
	public ResponseEntity<FrameworkBaseResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) throws  Exception {
		String email = authService.validateTokenAndGetSubject(resetPasswordRequest.getToken());
		String encodedPassword = passwordService.validateAndGenerateEncodedPassword(resetPasswordRequest.getNewPassword());
		userService.updatePassword(email, encodedPassword);
		return ResponseUtil.getStatusOkResponseEntity(null, "reset.password.success");
	}

}
