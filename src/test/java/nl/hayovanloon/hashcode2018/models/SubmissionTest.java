package nl.hayovanloon.hashcode2018.models;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class SubmissionTest {

  @Test
  public void testFactory() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    String data = "1 0\n2 2 1";

    int[][] expected = {new int[]{}, new int[]{0}, new int[]{2, 1}};

    Submission submission = Submission.from(data, rules);
    Assert.assertTrue(Arrays.deepEquals(submission.cabRides, expected));
  }

  @Test
  public void testFactoryInvalidInput() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    String data = "1 0\n2  2 1";

    Submission submission = Submission.from(data, rules);
    Assert.assertTrue(Arrays.deepEquals(submission.cabRides, null));
    Assert.assertEquals("Incorrect line format (consecutive spaces)",
        submission.getMessage());
  }

  @Test
  public void testFactoryInvalidInputTooManyLines() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    String data = "1 0\n2 2 1\n3 3";

    Submission submission = Submission.from(data, rules);
    Assert.assertTrue(Arrays.deepEquals(submission.cabRides, null));
    Assert.assertEquals("Too many lines (3), not enough cabs (2).",
        submission.getMessage());
  }

  @Test
  public void testFactoryDoubleCab() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    String data = "1 0\n1 2 1";

    Submission submission = Submission.from(data, rules);
    Assert.assertTrue(Arrays.deepEquals(submission.cabRides, null));
    Assert.assertEquals("Incorrect number of rides: 1 != 2",
        submission.getMessage());
  }

  @Test
  public void testFactoryDoubleRide() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    String data = "1 0\n2 0 1";

    Submission submission = Submission.from(data, rules);
    Assert.assertTrue(Arrays.deepEquals(submission.cabRides, null));
    Assert.assertEquals("Some rides have been taken (at least) twice.",
        submission.getMessage());
  }

  @Test
  public void testExample() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    Ride[] rides = new Ride[3];

    rides[0] = new Ride(0, 0, 1, 3, 2, 9);
    rides[1] = new Ride(1, 2, 1, 0, 0, 9);
    rides[2] = new Ride(2, 0, 2, 2, 0, 9);

    int[][] cabRides = {new int[]{}, new int[]{0}, new int[]{2, 1}};

    Submission submission = new Submission(cabRides);
    submission.doScoring(rules, rides);

    Assert.assertEquals(10, submission.getScore());
  }

  @Test
  public void testExamplePastT() {
    Rules rules = new Rules(3, 4, 2, 3, 2, 10);
    Ride[] rides = new Ride[3];

    rides[0] = new Ride(0, 0, 1, 3, 2, 9);
    rides[1] = new Ride(1, 2, 1, 0, 0, 9);
    rides[2] = new Ride(2, 0, 2, 2, 0, 9);

    int[][] cabRides = {new int[]{}, new int[]{0, 2, 1}, new int[]{}};

    Submission submission = new Submission(cabRides);
    submission.doScoring(rules, rides);

    Assert.assertEquals(6, submission.getScore());
    Assert.assertEquals("Some cab(s) arrived past simulation time",
        submission.getMessage());
  }
}
