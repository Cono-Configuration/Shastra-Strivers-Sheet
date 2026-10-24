import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1027);
    static final int MAX = 1000000000;

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // ---- Samples ----
        testCase = generateCase(testCase, new int[]{4, 1, 3, 2});
        testCase = generateCase(testCase, new int[]{10, 20, -5, 7, 15});

        // ---- Minimum size ----
        testCase = generateCase(testCase, new int[]{0});
        testCase = generateCase(testCase, new int[]{1});
        testCase = generateCase(testCase, new int[]{-1});
        testCase = generateCase(testCase, new int[]{MAX});
        testCase = generateCase(testCase, new int[]{-MAX});

        // ---- Size 2 and 3 ----
        testCase = generateCase(testCase, new int[]{1, 2});
        testCase = generateCase(testCase, new int[]{2, 1});
        testCase = generateCase(testCase, new int[]{5, 5});
        testCase = generateCase(testCase, new int[]{-MAX, MAX});
        testCase = generateCase(testCase, new int[]{MAX, -MAX});
        testCase = generateCase(testCase, new int[]{1, 2, 3});
        testCase = generateCase(testCase, new int[]{3, 2, 1});
        testCase = generateCase(testCase, new int[]{1, 2, 1});
        testCase = generateCase(testCase, new int[]{7, 7, 7});
        testCase = generateCase(testCase, new int[]{0, 0, 1});
        testCase = generateCase(testCase, new int[]{-3, 0, 3});

        // ---- Small mixed cases ----
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5});
        testCase = generateCase(testCase, new int[]{5, 4, 3, 2, 1});
        testCase = generateCase(testCase, new int[]{1, 2, 3, 2, 1});
        testCase = generateCase(testCase, new int[]{-1, -2, -3, -4});
        testCase = generateCase(testCase, new int[]{0, 0, 0, 0, 0, 0});
        testCase = generateCase(testCase, new int[]{1, -1, 1, -1, 1, -1});
        testCase = generateCase(testCase, new int[]{MAX, -MAX, 0, MAX, -MAX});
        testCase = generateCase(testCase, new int[]{100, 200, 300, 400, 500, 600, 700, 800, 900, 1000});
        testCase = generateCase(testCase, new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2});

        // ---- Structured, maximum size 1000 ----
        testCase = generateCase(testCase, ascending(1000));
        testCase = generateCase(testCase, descending(1000));
        testCase = generateCase(testCase, constant(1000, 42));
        testCase = generateCase(testCase, constant(1000, 0));
        testCase = generateCase(testCase, constant(1000, -MAX));
        testCase = generateCase(testCase, constant(1000, MAX));
        testCase = generateCase(testCase, alternatingExtremes(1000));
        testCase = generateCase(testCase, alternatingExtremes(999));
        testCase = generateCase(testCase, palindrome(1000));
        testCase = generateCase(testCase, palindrome(999));

        // ---- Random, varying sizes and value ranges ----
        int[] sizes = {4, 6, 8, 10, 15, 20, 31, 50, 64, 100, 128, 250, 500, 777, 999, 1000};
        for (int size : sizes) {
            testCase = generateCase(testCase, randomArray(size, -10, 10));
            testCase = generateCase(testCase, randomArray(size, -MAX, MAX));
        }

        // ---- Few distinct values (lots of duplicates) ----
        testCase = generateCase(testCase, randomArray(1000, 0, 1));
        testCase = generateCase(testCase, randomArray(1000, -2, 2));
        testCase = generateCase(testCase, randomArray(500, 0, 3));

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, int[] values) throws Exception {

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        int[] answer = solve(values);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(values.length + "\r\n");
            in.print(join(values) + "\r\n");
            out.print(join(answer) + "\r\n");
        }

        return id + 1;
    }

    static int[] solve(int[] values) {

        int n = values.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            result[i] = values[n - 1 - i];
        }

        return result;
    }

    static String join(int[] values) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                s.append(' ');
            }
            s.append(values[i]);
        }

        return s.toString();
    }

    static int[] ascending(int n) {

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = i - n / 2;
        }

        return a;
    }

    static int[] descending(int n) {

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = n / 2 - i;
        }

        return a;
    }

    static int[] constant(int n, int value) {

        int[] a = new int[n];
        Arrays.fill(a, value);

        return a;
    }

    static int[] alternatingExtremes(int n) {

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = (i % 2 == 0) ? MAX : -MAX;
        }

        return a;
    }

    static int[] palindrome(int n) {

        int[] a = new int[n];

        for (int i = 0; i <= (n - 1) / 2; i++) {
            int v = random.nextInt(2 * MAX + 1) - MAX;
            a[i] = v;
            a[n - 1 - i] = v;
        }

        return a;
    }

    static int[] randomArray(int n, int low, int high) {

        int[] a = new int[n];
        long range = (long) high - low + 1;

        for (int i = 0; i < n; i++) {
            a[i] = (int) (low + (long) (random.nextDouble() * range));
        }

        return a;
    }
}
