package potato.onetake.domain.Auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
gpublic class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure (
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception)
		throws IOException, ServletException {
		response.sendRedirect(request.getContextPath() + "/login-fail");
	}
}
