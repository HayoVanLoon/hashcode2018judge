package nl.hayovanloon.hashcode2018;

import com.google.common.collect.ImmutableMap;
import nl.hayovanloon.hashcode2018.models.Ride;
import nl.hayovanloon.hashcode2018.models.Rules;
import nl.hayovanloon.hashcode2018.models.Submission;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "scoring", urlPatterns = "/score")
public class ScoringServlet extends HttpServlet {

  private static Map<String, Integer> PROBLEMS = ImmutableMap.of(
      "a_example.in", 0, "b_should_be_easy.in", 1, "c_no_hurry.in", 2,
      "d_metropolis.in", 3, "e_high_bonus.in", 4);

  private static final Map<String, Rules> rules = new ConcurrentHashMap<>();
  private static final Ride[][] rides = new Ride[PROBLEMS.size()][];

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {

    final String problem = req.getParameter("problem");
    final String data = req.getParameter("data");
    if (problem == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().println("missing problem field");
    } else if (!PROBLEMS.containsKey(problem)) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().println("unknown problem " + problem);
    } else if (data == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().println("missing data field");
    } else {
      final Rules rules = getRules(problem);
      final Submission submission = Submission.from(data, rules);

      if (submission instanceof Submission.Invalid) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        req.setAttribute("submission", submission);
        req.setAttribute("problem", problem);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
      } else {
        submission.doScoring(getRules(problem), getRides(problem));

        req.setAttribute("submission", submission);
        req.setAttribute("problem", problem);
        req.getRequestDispatcher("/result.jsp").forward(req, resp);
      }
    }
  }

  private Rules getRules(String problem) throws IOException {
    if (!rules.containsKey(problem)) {
      try (final InputStreamReader reader = new InputStreamReader(this
          .getClass().getClassLoader().getResourceAsStream(problem))) {
        rules.put(problem, Rules.from(reader));
      }
    }

    return rules.get(problem);
  }

  private Ride[] getRides(String problem) throws IOException {
    int probNum = PROBLEMS.get(problem);

    if (rides[probNum] == null) {
      try (final InputStreamReader reader = new InputStreamReader(this
          .getClass().getClassLoader().getResourceAsStream(problem))) {
        final Ride[] rs = Ride.fromStream(reader);
        rides[probNum] = rs;
      }
    }

    return rides[probNum];
  }
}
