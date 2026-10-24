import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final int MIN_LENGTH = 1;
    static final int MAX_LENGTH = 18;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // The input space is tiny (length = 1 .. 18), so every possible input is a test case.
        // Test 00 and 01 are the two examples (length 3 and length 2); the rest follow in order.
        List<Integer> order = new ArrayList<>();
        order.add(3);
        order.add(2);

        for (int n = MIN_LENGTH; n <= MAX_LENGTH; n++) {
            if (n != 3 && n != 2) {
                order.add(n);
            }
        }

        int testCase = 0;

        for (int n : order) {

            List<String> strings = solve(n);

            // sanity checks: Fibonacci count, sorted, no "11", all distinct, all of length n
            long expectedCount = fib(n + 2);
            if (strings.size() != expectedCount) {
                throw new IllegalStateException("count mismatch for n = " + n);
            }
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                if (s.length() != n || s.contains("11")) {
                    throw new IllegalStateException("bad string " + s);
                }
                if (i > 0 && strings.get(i - 1).compareTo(s) >= 0) {
                    throw new IllegalStateException("not strictly increasing at n = " + n);
                }
            }

            String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
            String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

            try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
                 PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

                in.print(n + "\r\n");
                out.print(format(strings) + "\r\n");
            }

            testCase++;
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // Different technique from the backtracking solution: test every number
    // 0 .. 2^n - 1 and keep those without two adjacent set bits. Going through the
    // numbers in increasing order gives the strings in lexicographic order.
    static List<String> solve(int n) {

        List<String> result = new ArrayList<>();

        for (int mask = 0; mask < (1 << n); mask++) {

            if ((mask & (mask >> 1)) != 0) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            for (int bit = n - 1; bit >= 0; bit--) {
                sb.append(((mask >> bit) & 1) == 1 ? '1' : '0');
            }
            result.add(sb.toString());
        }

        return result;
    }

    static long fib(int n) {

        long a = 1, b = 1;

        for (int i = 3; i <= n; i++) {
            long next = a + b;
            a = b;
            b = next;
        }

        return b;
    }

    static String format(List<String> strings) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < strings.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append('"').append(strings.get(i)).append('"');
        }

        sb.append(']');
        return sb.toString();
    }
}
