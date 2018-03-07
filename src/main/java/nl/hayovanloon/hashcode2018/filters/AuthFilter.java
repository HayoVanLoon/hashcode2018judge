package nl.hayovanloon.hashcode2018.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import io.atlassian.fugue.Option;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class AuthFilter implements Filter {

  private static Option<String> userEmailHost = Option.none();
  private static boolean noAuth;

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    final JsonNode yaml;
    try {
      final InputStream is = this.getClass().getClassLoader()
          .getResourceAsStream("auth.yaml");
      if (is != null) {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        yaml = mapper.readTree(is);
      } else {
        yaml = JsonNodeFactory.instance.objectNode();
        noAuth = true;
      }
    } catch (IOException ex) {
      throw new ServletException(ex);
    }

    userEmailHost = Option.option(yaml.get("email-filter"))
        .map(x -> x.get("by-host"))
        .map(JsonNode::asText);
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
    } else if (!userService.isUserLoggedIn() && !noAuth) {
      final String url = userService.createLoginURL(req.getRequestURI());
      ((HttpServletResponse) response).sendRedirect(url);
    } else {
      req.setAttribute("logoutUrl", userService.createLogoutURL("/"));

      if ("/logout.jsp".equals(path)) {
        chain.doFilter(req, response);
      } else if (userEmailHost.isDefined()) {
        if (userEmailHost.fold(() -> true, x -> user.getEmail().endsWith(x))) {
          chain.doFilter(req, response);
        } else {
          ((HttpServletResponse) response).setStatus(SC_FORBIDDEN);
          req.getRequestDispatcher("/logout.jsp").forward(req, response);
        }
      } else {
        chain.doFilter(req, response);
      }
    }
  }

  @Override
  public void destroy() {
  }
}

