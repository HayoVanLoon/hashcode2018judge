package nl.hayovanloon.hashcode2018.models;

public class Submission {

  final int[][] cabRides;
  private int score = -1;
  protected String message;
  private int validRides = -1;
  private int missedRides = -1;
  private int lateRides = -1;
  private int bonusRides = -1;

  public Submission(int[][] cabRides) {
    this.cabRides = cabRides;
  }

  public int getScore() {
    return score;
  }

  public String getMessage() {
    return message;
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

  public static class Invalid extends Submission {
    private Invalid(String message) {
      super(null);
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  public static Submission from(String s, Rules rules) {
    final int[][] cabRides = new int[rules.F + 1][];
    final boolean rides[] = new boolean[rules.N];

    final String[] lines = s.split("\n");

    int cab = 1;
    int lineCount = 0;
    for (String untrimmed: lines) {
      final String line = untrimmed.trim();
      if (!line.isEmpty()) {
        lineCount += 1;
        if (lineCount > rules.F) {
          return new Invalid(String.format(
              "Too many lines (%s), not enough cabs (%s).",
              lineCount, rules.F));
        }

        if (line.matches(".*\\s{2,}.*")) {
          return new Invalid("Incorrect line format (consecutive spaces)");
        }

        String[] cols = line.split(" ");
        if (cols.length > 1) {
          try {
            int numRides = Integer.parseInt(cols[0]);

            if (cabRides.length < cab) {
              return new Invalid(String.format(
                  "Cab number %s outside of range [1, %s).",
                  cab, rules.N));
            } else if (cabRides[cab] != null) {
              return new Invalid(String.format(
                  "Cab %s was used (at least) twice", cab));
            } else if (numRides != cols.length - 1) {
              return new Invalid(String.format(
                  "Incorrect number of rides: %s != %s", numRides,
                  cols.length - 1));
            } else {
              cabRides[cab] = new int[cols.length - 1];
              for (int i = 1; i < cols.length; i += 1) {
                int ride = Integer.parseInt(cols[i]);
                if (rides[ride]) {
                  return new Invalid("Some rides have been taken (at least) twice.");
                } else {
                  cabRides[cab][i-1] = Integer.parseInt(cols[i]);
                  rides[ride] = true;
                }
              }
            }
          } catch (NumberFormatException ex) {
            return new Invalid("Error parsing input line: " + line);
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

    return new Submission(cabRides);
  }

  public void doScoring(Rules rules, Ride[] rides) {
    validRides = 0;
    bonusRides = 0;
    lateRides = 0;
    missedRides = 0;

    int score = 0;
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
        final int start = Math.max(t + preTravel, ride.s);
        t = start + length;

        // update scoring
        if (t <= rules.T) {
          if (start <= ride.s) {
            score += rules.B;
            bonusRides += 1;
          }

          if (t <= ride.f) {
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

    this.score = score;
  }
}
