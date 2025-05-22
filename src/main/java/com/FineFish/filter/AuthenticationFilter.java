package com.FineFish.filter;

import com.FineFish.model.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Combined authentication filter that handles:
 * 1. Protecting admin pages from unauthorized access
 * 2. Redirecting logged-in users away from the login page
 */
@WebFilter(urlPatterns = {"/pages/admin/*", "/pages/user/Login.jsp"})
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		
		// Get the requested URI path
		String requestURI = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();
		
		// Check if user is logged in
		boolean isAuthenticated = false;
		boolean isAdmin = false;
		User user = null;
		
		if (session != null) {
			user = (User) session.getAttribute("user");
			isAuthenticated = (user != null);
			isAdmin = (isAuthenticated && "admin".equals(user.getRole()));
		}
		
		// Case 1: User accessing Login.jsp while already logged in
		if (requestURI.contains("/pages/user/Login.jsp") && isAuthenticated) {
			if (isAdmin) {
				httpResponse.sendRedirect(contextPath + "/pages/admin/Dashboard.jsp");
			} else {
				httpResponse.sendRedirect(contextPath + "/index.jsp");
			}
			return;
		}
		
		// Case 2: User accessing admin pages
		if (requestURI.contains("/pages/admin/")) {
			if (isAuthenticated && isAdmin) {
				// Admin user accessing admin page - allow
				chain.doFilter(request, response);
			} else {
				// Not authenticated as admin - redirect to login
				String errorMessage = "You must be logged in as an administrator to access this page.";
				httpRequest.setAttribute("errorMessage", errorMessage);
				httpRequest.getRequestDispatcher("/pages/user/Login.jsp").forward(httpRequest, httpResponse);
			}
			return;
		}
		
		// All other cases - allow the request to proceed
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}