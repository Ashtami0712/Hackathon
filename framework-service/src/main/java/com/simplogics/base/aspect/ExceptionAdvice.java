package com.simplogics.base.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.simplogics.base.dto.FrameworkBaseResponse;
import com.simplogics.base.utils.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<FrameworkBaseResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getAllErrors().stream().map(msg -> Translator.translateToLocale(msg.getDefaultMessage())).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FrameworkBaseResponse.builder()
				.data(null)
				.message(Translator.translateToLocale("validation.errors"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<FrameworkBaseResponse> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(FrameworkBaseResponse.builder()
				.data(null)
				.message(Translator.translateToLocale("access.denied"))
				.hasErrors(true)
				.build());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<FrameworkBaseResponse> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FrameworkBaseResponse.builder()
				.data(null)
				.message(Translator.translateToLocale("validation.errors"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<FrameworkBaseResponse> handleNotFoundExceptions(NoResourceFoundException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		FrameworkBaseResponse baseResponse = FrameworkBaseResponse.builder()
				.status(false)
				.data(null)
				.hasErrors(true)
				.message(Translator.translateToLocale("api.not.found"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
	}




	@ExceptionHandler(Exception.class)
	public ResponseEntity<FrameworkBaseResponse> handleAllExceptions(Exception ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		FrameworkBaseResponse baseResponse = FrameworkBaseResponse.builder()
				.status(false)
				.data(null)
				.hasErrors(true)
				.message(Translator.translateToLocale("internal.server.error"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
