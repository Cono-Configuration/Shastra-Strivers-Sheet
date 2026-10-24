import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1771);

    static final Set<String> used = new HashSet<>();
    static int testCase = 0;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // ---------- Examples ----------
        add(1, 2, 3);
        add(0);

        // ---------- n = 1 ----------
        add(1);
        add(-1);
        add(10);
        add(-10);
        add(5);
        add(-7);

        // ---------- n = 2 ----------
        add(1, 2);
        add(2, 1);
        add(0, 1);
        add(-1, 0);
        add(-10, 10);
        add(10, -10);
        add(0, -10);
        add(3, -3);
        add(-1, 1);
        add(7, 0);
        add(-5, -4);
        add(9, 8);

        // ---------- n = 3 ----------
        add(3, 2, 1);
        add(0, 1, 2);
        add(-1, 0, 1);
        add(-3, -2, -1);
        add(10, 0, -10);
        add(5, -5, 0);
        add(2, -1, 4);
        add(-10, -9, -8);
        add(8, 9, 10);

        // ---------- n = 4 .. 9 ----------
        add(1, 2, 3, 4);
        add(4, 3, 2, 1);
        add(0, -1, -2, -3);
        add(-4, 4, 0, 2);

        add(1, 2, 3, 4, 5);
        add(5, 4, 3, 2, 1);
        add(-2, -1, 0, 1, 2);
        add(10, -10, 5, -5, 0);

        add(-3, 3, -2, 2, -1, 1);
        add(6, 5, 4, 3, 2, 1);
        add(0, 1, 2, 3, 4, 5);

        add(0, 1, 2, 3, 4, 5, 6);
        add(-7, -6, -5, -4, -3, -2, -1);
        add(7, -7, 0, 3, -3, 1, -1);

        add(1, 2, 3, 4, 5, 6, 7, 8);
        add(-4, -3, -2, -1, 0, 1, 2, 3);
        add(10, 9, 8, 7, -7, -8, -9, -10);

        add(1, 2, 3, 4, 5, 6, 7, 8, 9);
        add(-9, -8, -7, -6, -5, -4, -3, -2, -1);
        add(-4, -3, -2, -1, 0, 1, 2, 3, 4);
        add(0, 10, -10, 5, -5, 1, -1, 2, -2);

        // ---------- n = 10 (largest allowed: 1024 subsets) ----------
        add(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        add(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        add(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1);
        add(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10);
        add(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        add(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        add(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4);
        add(-4, -3, -2, -1, 0, 1, 2, 3, 4, 5);
        add(-10, -8, -6, -4, -2, 0, 2, 4, 6, 8);
        add(-10, 10, -9, 9, -8, 8, -7, 7, -6, 6);
        add(0, 10, -10, 5, -5, 3, -3, 1, -1, 2);
        add(10, -9, 8, -7, 6, -5, 4, -3, 2, -1);
        add(1, 3, 5, 7, 9, -1, -3, -5, -7, -9);
        add(-10, 10, 0, 1, -1, 2, -2, 3, -3, 4);

        // ---------- random cases ----------
        for (int n = 2; n <= 9; n++) {
            for (int i = 0; i < 4; i++) {
                randomCase(n);
            }
        }
        for (int i = 0; i < 20; i++) {
            randomCase(10);
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static void add(int... elements) throws Exception {
        if (!used.add(Arrays.toString(elements))) {
            return;
        }
        write(elements);
    }

    static void randomCase(int n) throws Exception {

        while (true) {

            List<Integer> pool = new ArrayList<>();
            for (int v = -10; v <= 10; v++) {
                pool.add(v);
            }
            Collections.shuffle(pool, random);

            int[] elements = new int[n];
            for (int i = 0; i < n; i++) {
                elements[i] = pool.get(i);
            }

            if (used.add(Arrays.toString(elements))) {
                write(elements);
                return;
            }
        }
    }

    static void write(int[] elements) throws Exception {

        // sanity checks on the constraints
        if (elements.length < 1 || elements.length > 10) {
            throw new IllegalStateException("bad length");
        }
        Set<Integer> seen = new HashSet<>();
        for (int v : elements) {
            if (v < -10 || v > 10 || !seen.add(v)) {
                throw new IllegalStateException("bad element");
            }
        }

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

        StringBuilder in = new StringBuilder();
        in.append(elements.length).append("\r\n");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                in.append(' ');
            }
            in.append(elements[i]);
        }
        in.append("\r\n");

        try (PrintWriter inWriter = new PrintWriter(new FileWriter(inputFile));
             PrintWriter outWriter = new PrintWriter(new FileWriter(outputFile))) {

            inWriter.print(in.toString());
            outWriter.print(format(solve(elements)) + "\r\n");
        }

        testCase++;
    }

    // Different method from the bitmask solution: start from [[]] and, for every
    // element, append a copy of every existing subset with that element added.
    static List<List<Integer>> solve(int[] elements) {

        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (int value : elements) {

            int size = result.size();

            for (int i = 0; i < size; i++) {
                List<Integer> copy = new ArrayList<>(result.get(i));
                copy.add(value);
                result.add(copy);
            }
        }

        return result;
    }

    static String format(List<List<Integer>> subsets) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < subsets.size(); i++) {

            if (i > 0) {
                sb.append(',');
            }

            sb.append('[');
            List<Integer> s = subsets.get(i);
            for (int j = 0; j < s.size(); j++) {
                if (j > 0) {
                    sb.append(',');
                }
                sb.append(s.get(j));
            }
            sb.append(']');
        }

        sb.append(']');
        return sb.toString();
    }
}
