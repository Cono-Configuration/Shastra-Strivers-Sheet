import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1026);
    static final Set<String> used = new HashSet<>();
    static final int LIMIT = 150;

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // ---- Samples ----
        testCase = generateCase(testCase, new int[]{2, 3, 5, 4}, 7);
        testCase = generateCase(testCase, new int[]{2}, 1);
        testCase = generateCase(testCase, new int[]{3, 4, 5, 6}, 10);

        // ---- Single candidate ----
        testCase = generateCase(testCase, new int[]{2}, 2);
        testCase = generateCase(testCase, new int[]{2}, 4);
        testCase = generateCase(testCase, new int[]{2}, 40);
        testCase = generateCase(testCase, new int[]{2}, 39);
        testCase = generateCase(testCase, new int[]{2}, 3);
        testCase = generateCase(testCase, new int[]{3}, 40);
        testCase = generateCase(testCase, new int[]{3}, 39);
        testCase = generateCase(testCase, new int[]{40}, 40);
        testCase = generateCase(testCase, new int[]{40}, 39);
        testCase = generateCase(testCase, new int[]{40}, 1);
        testCase = generateCase(testCase, new int[]{7}, 35);
        testCase = generateCase(testCase, new int[]{7}, 36);
        testCase = generateCase(testCase, new int[]{20}, 40);
        testCase = generateCase(testCase, new int[]{21}, 40);

        // ---- Goal smaller than every candidate / goal = 1 / goal = 2 ----
        testCase = generateCase(testCase, new int[]{5, 6, 7}, 4);
        testCase = generateCase(testCase, new int[]{2, 3}, 1);
        testCase = generateCase(testCase, new int[]{2, 3}, 2);
        testCase = generateCase(testCase, new int[]{2, 3}, 3);
        testCase = generateCase(testCase, new int[]{40, 39, 38}, 1);
        testCase = generateCase(testCase, new int[]{40, 39, 38}, 37);

        // ---- Goal equals a candidate / the sum of two / is unreachable ----
        testCase = generateCase(testCase, new int[]{2, 4, 8}, 8);
        testCase = generateCase(testCase, new int[]{4, 8, 2}, 16);
        testCase = generateCase(testCase, new int[]{3, 6, 9}, 10);
        testCase = generateCase(testCase, new int[]{4, 6, 8}, 9);
        testCase = generateCase(testCase, new int[]{5, 10, 20}, 40);
        testCase = generateCase(testCase, new int[]{39, 40}, 40);
        testCase = generateCase(testCase, new int[]{20, 19, 21}, 40);

        // ---- Small, well known cases ----
        testCase = generateCase(testCase, new int[]{2, 3, 6, 7}, 7);
        testCase = generateCase(testCase, new int[]{2, 3, 5}, 8);
        testCase = generateCase(testCase, new int[]{2, 3, 5}, 11);
        testCase = generateCase(testCase, new int[]{3, 5, 7}, 12);
        testCase = generateCase(testCase, new int[]{2, 5}, 40);
        testCase = generateCase(testCase, new int[]{3, 7}, 40);
        testCase = generateCase(testCase, new int[]{2, 4, 6, 8}, 7);
        testCase = generateCase(testCase, new int[]{2, 4, 6, 8}, 20);
        testCase = generateCase(testCase, new int[]{5, 3, 2}, 40);
        testCase = generateCase(testCase, new int[]{6, 10, 15}, 30);

        // ---- Maximum candidate count n = 30 ----
        testCase = generateCase(testCase, range(11, 40), 40);
        testCase = generateCase(testCase, range(11, 40), 22);
        testCase = generateCase(testCase, range(11, 40), 33);
        testCase = generateCase(testCase, range(11, 40), 10);
        testCase = generateCase(testCase, range(11, 40), 1);
        testCase = generateCase(testCase, range(11, 40), 11);
        testCase = generateCase(testCase, range(11, 40), 21);
        testCase = generateCase(testCase, reverse(range(11, 40)), 40);
        testCase = generateCase(testCase, range(2, 31), 10);
        testCase = generateCase(testCase, range(2, 31), 15);
        testCase = generateCase(testCase, range(2, 31), 20);
        testCase = generateCase(testCase, range(2, 31), 2);
        testCase = generateCase(testCase, range(2, 31), 1);
        testCase = generateCase(testCase, range(2, 31), 3);
        testCase = generateCase(testCase, range(3, 32), 17);
        testCase = generateCase(testCase, range(10, 39), 40);
        testCase = generateCase(testCase, range(10, 39), 30);
        testCase = generateCase(testCase, shuffled(range(11, 40)), 36);

        // ---- Larger sets with larger goals (many combinations, but under the limit) ----
        testCase = generateCase(testCase, new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37}, 20);
        testCase = generateCase(testCase, new int[]{7, 8, 9, 10, 11, 12, 13, 14}, 40);
        testCase = generateCase(testCase, new int[]{6, 7, 8, 9, 10}, 40);
        testCase = generateCase(testCase, new int[]{8, 9, 10, 11, 12, 13, 14, 15}, 40);
        testCase = generateCase(testCase, new int[]{5, 7, 9, 11}, 40);
        testCase = generateCase(testCase, new int[]{6, 9, 20}, 40);

        // ---- Cases close to the 149 combination limit ----
        int found = 0;
        int attempts = 0;
        while (found < 5 && attempts < 200000) {
            attempts++;
            int n = 4 + random.nextInt(12);
            int[] arr = randomDistinct(n, 2, 40);
            int goal = 25 + random.nextInt(16);
            int count = combinations(arr, goal).size();
            if (count >= 125 && count < LIMIT) {
                int before = testCase;
                testCase = generateCase(testCase, arr, goal);
                if (testCase > before) {
                    found++;
                }
            }
        }

        // ---- Random cases ----
        int made = 0;
        while (made < 45) {
            int n = 1 + random.nextInt(30);
            int[] arr = randomDistinct(n, 2, 40);
            int goal = 1 + random.nextInt(40);
            if (combinations(arr, goal).size() >= LIMIT) {
                continue;
            }
            int before = testCase;
            testCase = generateCase(testCase, arr, goal);
            if (testCase > before) {
                made++;
            }
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, int[] vals, int goal) throws Exception {

        String key = Arrays.toString(vals) + "|" + goal;
        if (!used.add(key)) {
            return id;
        }

        List<List<Integer>> answer = combinations(vals, goal);

        if (answer.size() >= LIMIT) {
            throw new RuntimeException("Too many combinations for " + key + ": " + answer.size());
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(vals.length + "\r\n");
            in.print(join(vals) + "\r\n");
            in.print(goal + "\r\n");

            out.print(answer.size() + "\r\n");
            for (List<Integer> combo : answer) {
                out.print(joinList(combo) + "\r\n");
            }
        }

        return id + 1;
    }

    // Bottom-up unbounded knapsack: ways[s] holds every combination (non-decreasing) that sums to s
    static List<List<Integer>> combinations(int[] vals, int goal) {

        int[] sorted = vals.clone();
        Arrays.sort(sorted);

        List<List<List<Integer>>> ways = new ArrayList<>();
        for (int s = 0; s <= goal; s++) {
            ways.add(new ArrayList<>());
        }
        ways.get(0).add(new ArrayList<>());

        for (int v : sorted) {
            for (int s = v; s <= goal; s++) {
                for (List<Integer> base : ways.get(s - v)) {
                    List<Integer> next = new ArrayList<>(base);
                    next.add(v);
                    ways.get(s).add(next);
                }
            }
        }

        List<List<Integer>> result = new ArrayList<>(ways.get(goal));

        result.sort((a, b) -> {
            int len = Math.min(a.size(), b.size());
            for (int i = 0; i < len; i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return Integer.compare(a.get(i), b.get(i));
                }
            }
            return Integer.compare(a.size(), b.size());
        });

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

    static String joinList(List<Integer> values) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                s.append(' ');
            }
            s.append(values.get(i));
        }

        return s.toString();
    }

    static int[] range(int from, int to) {

        int[] a = new int[to - from + 1];

        for (int i = 0; i < a.length; i++) {
            a[i] = from + i;
        }

        return a;
    }

    static int[] reverse(int[] a) {

        int[] r = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            r[i] = a[a.length - 1 - i];
        }

        return r;
    }

    static int[] shuffled(int[] a) {

        int[] r = a.clone();

        for (int i = r.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int t = r[i];
            r[i] = r[j];
            r[j] = t;
        }

        return r;
    }

    static int[] randomDistinct(int n, int low, int high) {

        int[] pool = range(low, high);
        int[] mixed = shuffled(pool);

        return Arrays.copyOf(mixed, n);
    }
}
