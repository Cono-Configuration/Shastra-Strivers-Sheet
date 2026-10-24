import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // Every valid input is included: picks from 2 to 9, goal from 1 to 60.
        // The input space is small enough to cover completely.
        int id = 0;
        for (int picks = 2; picks <= 9; picks++) {
            for (int goal = 1; goal <= 60; goal++) {
                id = writeCase(id, picks, goal);
            }
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static int writeCase(int id, int picks, int goal) throws Exception {

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        // Independent reference: check every subset of {1..9} (bitmasks 1..511).
        List<List<Integer>> combos = new ArrayList<>();
        for (int mask = 1; mask < (1 << 9); mask++) {
            List<Integer> c = new ArrayList<>();
            int sum = 0;
            for (int b = 0; b < 9; b++) {
                if ((mask & (1 << b)) != 0) {
                    c.add(b + 1);
                    sum += b + 1;
                }
            }
            if (c.size() == picks && sum == goal) {
                combos.add(c);
            }
        }
        combos.sort(gen1::compareSeq);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(picks + " " + goal + "\r\n");

            for (List<Integer> c : combos) {
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < c.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(c.get(i));
                }
                sb.append(']');
                out.print(sb + "\r\n");
            }
        }

        return id + 1;
    }

    // Lexicographic comparison; a proper prefix sorts first.
    static int compareSeq(List<Integer> a, List<Integer> b) {
        int m = Math.min(a.size(), b.size());
        for (int i = 0; i < m; i++) {
            int c = Integer.compare(a.get(i), b.get(i));
            if (c != 0) return c;
        }
        return Integer.compare(a.size(), b.size());
    }
}
