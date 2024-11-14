package com.simplogics.base.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplogics.base.dto.FrameworkBaseResponse;
import com.simplogics.base.security.authDetails.UserAuthDetailsService;
import com.simplogics.base.security.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.simplogics.base.utils.Constants.AUTH_HEADER_KEY;
import static com.simplogics.base.utils.Constants.TOKEN_PREFIX;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserAuthDetailsService userAuthDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
		String jwt = parseJwt(req);
		try {
			if (jwt != null && jwtTokenUtil.validateJwtToken(jwt)) {
				String username = jwtTokenUtil.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = userAuthDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else if(jwt == null) {
				jwtTokenUtil.validateJwtToken(null);
			}
		} catch (IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
			sendErrorWithStatus(HttpStatus.FORBIDDEN.value(), "Unauthorized", res);
			return;
		} catch (ExpiredJwtException e) {
			sendErrorWithStatus(HttpStatus.UNAUTHORIZED.value(), "Session expired", res);
			return;
		}
		chain.doFilter(req, res);
	}

	private void sendErrorWithStatus(int status, String message, HttpServletResponse response) throws IOException {
		response.setStatus(status);
		FrameworkBaseResponse baseResponse = FrameworkBaseResponse.builder()
				.data(null)
				.status(false)
				.hasErrors(true)
				.message(message)
				.errors(Collections.singletonList(message))
				.build();
		response.setContentType("application/json");
		response.getOutputStream().println(new ObjectMapper().writeValueAsString(baseResponse));
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(AUTH_HEADER_KEY);

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
			return headerAuth.substring(7);
		}

		return null;
	}

}
