package nl.hayovanloon.hashcode2018.filters;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import nl.hayovanloon.hashcode2018.models.Settings;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_OK;


public class AuthFilter implements Filter {

  private static Settings settings;

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    try {
      settings = Settings.read(this);
    } catch (IOException e) {
      throw new ServletException(e);
    }
  }

  @Override
  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest req = (HttpServletRequest) request;
    final String path = req.getServletPath();

    final UserService userService = UserServiceFactory.getUserService();
    final User user = userService.getCurrentUser();

    if (path.startsWith("/_ah/")) {
      // appengine service calls
      if ("/_ah/start".equals(path)) {
        // startup call, immediately respond with OK
        ((HttpServletResponse) response).setStatus(SC_OK);
      } else {
        chain.doFilter(request, response);
      }
    } else if (!userService.isUserLoggedIn()) {
      final String url = userService.createLoginURL(req.getRequestURI());
      ((HttpServletResponse) response).sendRedirect(url);
    } else {
      req.setAttribute("logoutUrl", userService.createLogoutURL("/"));

      if ("/logout.jsp".equals(path)) {
        chain.doFilter(req, response);
      } else {
        final String email = user.getEmail();
        final boolean allow =
            settings.getAllowByHost().fold(() -> false, email::endsWith)
                || settings.getAllowByPattern()
                .fold(() -> false, x -> x.matcher(email).matches())
                || settings.getAllowExact().contains(email);

        if (allow) {
          chain.doFilter(req, response);
        } else {
          ((HttpServletResponse) response).setStatus(SC_FORBIDDEN);
          req.getRequestDispatcher("/logout.jsp").forward(req, response);
        }
      }
    }
  }

  @Override
  public void destroy() {
  }
}

