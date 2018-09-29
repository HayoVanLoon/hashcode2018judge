package nl.hayovanloon.hashcode2018.models;

public class Submission {

  private final int[][] cabRides;
  private boolean invalid;
  private int score = -1;
  private int validRides = -1;
  private int missedRides = -1;
  private int lateRides = -1;
  private int bonusRides = -1;
  private String message;

  Submission(int[][] cabRides, String message) {
    this.cabRides = cabRides;
    this.message = message;
  }

  public static Submission from(int[][] cabRides) {
    return new Submission(cabRides, null);
  }

  public static Submission invalid(String error) {
    return new Submission(null, error);
  }

  public static Submission from(String s, Rules rules) {
    final int[][] cabRides = new int[rules.F + 1][];
    final boolean rides[] = new boolean[rules.N];

    final String[] lines = s.split("\n");

    int cab = 1;
    int lineCount = 0;
    for (String untrimmed : lines) {
      final String line = untrimmed.trim();
      if (!line.isEmpty()) {
        lineCount += 1;
        if (lineCount > rules.F) {
          return invalid(String.format(
              "Too many lines (%s), not enough cabs (%s).",
              lineCount, rules.F));
        }

        if (line.matches(".*\\s{2,}.*")) {
          return invalid("Incorrect line format (consecutive spaces)");
        }

        String[] cols = line.split(" ");
        if (cols.length > 1) {
          try {
            int numRides = Integer.parseInt(cols[0]);

            if (cabRides.length < cab) {
              return invalid(String.format(
                  "Cab number %s outside of range [1, %s).",
                  cab, rules.N));
            } else if (cabRides[cab] != null) {
              return invalid(String.format(
                  "Cab %s was used (at least) twice", cab));
            } else if (numRides != cols.length - 1) {
              return invalid(String.format(
                  "Incorrect number of rides: %s != %s", numRides,
                  cols.length - 1));
            } else {
              cabRides[cab] = new int[cols.length - 1];
              for (int i = 1; i < cols.length; i += 1) {
                int ride = Integer.parseInt(cols[i]);
                if (rides[ride]) {
                  return invalid(
                      "Some rides have been taken (at least) twice.");
                } else {
                  cabRides[cab][i - 1] = Integer.parseInt(cols[i]);
                  rides[ride] = true;
                }
              }
            }
          } catch (NumberFormatException ex) {
            return invalid("Error parsing input line: " + line);
          }
        }

        cab += 1;
      }
    }

    for (int i = 0; i < cabRides.length; i += 1) {
      if (cabRides[i] == null) {
        cabRides[i] = new int[0];
      }
    }

    return from(cabRides);
  }

  public int getScore() {
    return score;
  }

  public int getMissedRides() {
    return missedRides;
  }

  public int getLateRides() {
    return lateRides;
  }

  public int getBonusRides() {
    return bonusRides;
  }

  public int getValidRides() {
    return validRides;
  }

  int[][] getCabRides() {
    return cabRides == null ? null : cabRides.clone();
  }

  public boolean isInvalid() {
    return invalid;
  }

  public String getMessage() {
    return message;
  }

  /**
   * Scores this submission. Modifies internal state.
   *
   * @param rules  rules to calculate score with
   * @param rides  ride scheme to score
   */
  public void doScoring(Rules rules, Ride[] rides) {
    validRides = 0;
    bonusRides = 0;
    lateRides = 0;
    missedRides = 0;

    score = 0;
    final boolean[] done = new boolean[rules.N];

    for (int cab = 1; cab < cabRides.length; cab += 1) {
      int t = 0;

      Ride prev = new Ride(0, 0, 0, 0, 0, 0);
      for (int i = 0; i < cabRides[cab].length; i += 1) {
        final Ride ride = rides[cabRides[cab][i]];

        // calculate travel times / lengths
        final int preTravel = ride.distFrom(prev);
        final int length = ride.getLength();

        // calculate time table
        final int start = Math.max(t + preTravel, ride.getLatestStart());
        t = start + length;

        // update scoring
        if (t <= rules.T) {
          if (start <= ride.getLatestStart()) {
            score += rules.B;
            bonusRides += 1;
          }

          if (t <= ride.getLatestFinish()) {
            score += length;
          } else {
            lateRides += 1;
          }

          validRides += 1;
          done[cabRides[cab][i]] = true;
        }

        prev = ride;

        if (t > rules.T) {
          message = "Some cab(s) arrived past simulation time";
        }
      }
    }

    for (boolean b : done) missedRides += b ? 0 : 1;
  }
}
