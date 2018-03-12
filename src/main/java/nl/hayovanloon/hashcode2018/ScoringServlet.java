package nl.hayovanloon.hashcode2018;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import nl.hayovanloon.hashcode2018.models.Ride;
import nl.hayovanloon.hashcode2018.models.Rules;
import nl.hayovanloon.hashcode2018.models.Settings;
import nl.hayovanloon.hashcode2018.models.Submission;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@WebServlet(name = "scoring", urlPatterns = "/score")
public class ScoringServlet extends HttpServlet {

  private static Map<String, Integer> PROBLEMS = ImmutableMap.of(
      "a_example.in", 0, "b_should_be_easy.in", 1, "c_no_hurry.in", 2,
      "d_metropolis.in", 3, "e_high_bonus.in", 4);

  private static final Logger LOG = Logger
      .getLogger(ScoringServlet.class.getName());

  private static final Map<String, Rules> rules = new ConcurrentHashMap<>();
  private static final Ride[][] rides = new Ride[PROBLEMS.size()][];

  private static Settings settings;

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

        final int oldScore = updatePersonalScore(problem, submission);
        req.setAttribute("oldScore", oldScore);

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

  private int updatePersonalScore(String problem,
                                  Submission submission) throws IOException {
    final DatastoreService datastore = DatastoreServiceFactory
        .getDatastoreService();
    final UserService userService = UserServiceFactory.getUserService();

    final String userId = userService.getCurrentUser().getUserId();
    final Key key = KeyFactory.createKey("hashcodejudge_score", userId);

    final String probProp = problem.replace(".in", "") + "_score";

    final Entity highScores = getHighScore(datastore, key);

    final long oldScore = (long) highScores.getProperties()
        .getOrDefault(probProp, 0L);

    if (oldScore < submission.getScore()) {
      highScores.setProperty(probProp, submission.getScore());
      highScores.setProperty("hash", createHash(highScores));
      datastore.put(highScores);
    }

    return (int) oldScore;
  }

  private Entity getHighScore(DatastoreService datastore,
                              Key key) throws IOException {
    final Iterator<Entity> scoreOpt =  datastore.get(ImmutableList.of(key))
        .values().iterator();
    if (scoreOpt.hasNext()) {
      final Entity highScore = scoreOpt.next();
      if (getSettings().getScoreSecret().isDefined()) {
        if (verify(highScore)) {
          return highScore;
        } else {
          LOG.warning("invalid hash, scores reset for" + key.getName());
          return new Entity(key);
        }
      } else {
        return highScore;
      }
    } else {
      return new Entity(key);
    }
  }

  private boolean verify(Entity highScore) throws IOException {
    final String hash = (String) highScore.getProperty("hash");
    return createHash(highScore).equals(hash);
  }

  private String createHash(Entity highScore) throws IOException {
    final String scoreSecret = getSettings().getScoreSecret().get();

    final StringBuilder sb = new StringBuilder(scoreSecret);
    sb.append(",").append(highScore.getProperties()
        .getOrDefault("a_example_score", 0L));
    sb.append(",").append(highScore.getProperties()
        .getOrDefault("b_should_be_easy_score", 0L));
    sb.append(",").append(highScore.getProperties()
        .getOrDefault("c_no_hurry_score", 0L));
    sb.append(",").append(highScore.getProperties()
        .getOrDefault("d_metropolis_score", 0L));
    sb.append(",").append(highScore.getProperties()
        .getOrDefault("e_high_bonus_score", 0L));

    return "" + sb.toString().hashCode();
  }

  private Settings getSettings() throws IOException {
    if (settings == null) {
      settings = Settings.read(this);
    }

    return settings;
  }
}
