package com.simplogics.base.controller;

import com.simplogics.base.annotation.APIResult;
import com.simplogics.base.dto.FrameworkBaseResponse;
import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.utils.ApiRoutes;
import com.simplogics.base.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplogics.base.service.IUserService;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.USER)
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@APIResult(error_message = "user.fetch.fail")
	public ResponseEntity<FrameworkBaseResponse> getUsers(@RequestParam(required = false, name = "search") String search,
														  @RequestParam(required = false, name = "isActive") Boolean isActive,
														  @RequestParam(required = false, name = "roleIds") List<Long> roleIds,
														  @RequestParam(required = false, name = "sortBy") String sortBy,
														  @RequestParam(required = false, name = "sortDir") String sortDir,
														  @RequestParam(required = false, name = "pageSize", defaultValue = "10") Long pageSize,
														  @RequestParam(required = false, name = "pageNumber", defaultValue = "0") Long pageNumber) {
		Sort sort = Sort.by(Sort.Order.by(sortBy != null ? sortBy : "id").with(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC")));
		List<UserResponse> response = userService.getUsers(search, roleIds, isActive, pageSize, pageNumber, sort);
		return ResponseUtil.getStatusOkResponseEntity(response, "user.fetch.success");
	}


}
