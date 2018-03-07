package nl.hayovanloon.hashcode2018.models;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Ride {

  private final int a;
  private final int b;
  private final int x;
  private final int y;
  public final int s;
  public final int f;

  public Ride(int a, int b, int x, int y, int s, int f) {
    this.a = a;
    this.b = b;
    this.x = x;
    this.y = y;
    this.s = s;
    this.f = f;
  }

  public static Ride from(String xs) {
    int[] ys = new int[6];
    int i = 0;
    for (String x : xs.split(" ")) {
      ys[i] = Integer.parseInt(x);
      i += 1;
    }
    return new Ride(ys[0], ys[1], ys[2], ys[3], ys[4], ys[5]);
  }

  public static Ride[] fromStream(InputStreamReader reader) throws IOException {
    // read rules line just to get number of rides (and advance reader)
    final Rules rules = Rules.from(reader);
    Ride[] rs = new Ride[rules.N];

    int read = 0; // unused

    int i = 0;
    while (read != -1) {
      read = reader.read();

      final StringBuilder sb = new StringBuilder();
      while(read != -1 && read != '\n') {
        sb.append((char) read);
        read = reader.read();
      }

      final String line = sb.toString();
      if (!line.isEmpty()) {
        final Ride ride = Ride.from(line);
        rs[i] = ride;
        i += 1;
      }
    }

    return rs;
  }


  public int distFrom(Ride ride) {
    return distFrom(ride.x, ride.y);
  }

  public int distFrom(int x, int y) {
    return manhattan(x, y, this.a, this.b);
  }

  public int getLength() {
    return manhattan(a, b, x, y);
  }

  private static int manhattan(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }

  @Override
  public String toString() {
    return "Ride{" +
        "a=" + a +
        ", b=" + b +
        ", x=" + x +
        ", y=" + y +
        ", s=" + s +
        ", f=" + f +
        '}';
  }
}
