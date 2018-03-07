package nl.hayovanloon.hashcode2018.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Rules implements Serializable {
  public final int R;
  public final int C;
  public final int F;
  public final int N;
  public final int B;
  public final int T;

  public Rules(int r, int c, int f, int n, int b, int t) {
    R = r;
    C = c;
    F = f;
    N = n;
    B = b;
    T = t;
  }

  public static Rules from(InputStreamReader reader) throws IOException {
    final StringBuilder sb = new StringBuilder();

    int read = reader.read();
    while (read != -1 && read != '\n') {
      sb.append((char) read);
      read = reader.read();
    }

    return from(sb.toString());
  }

  public static Rules from(String xs) {
    int[] ys = new int[6];
    int i = 0;
    for (String x : xs.split(" ")) {
      ys[i] = Integer.parseInt(x);
      i += 1;
    }
    return new Rules(ys[0], ys[1], ys[2], ys[3], ys[4], ys[5]);
  }

  @Override
  public String toString() {
    return "Rules{" +
        "R=" + R +
        ", C=" + C +
        ", F=" + F +
        ", N=" + N +
        ", B=" + B +
        ", T=" + T +
        '}';
  }
}
