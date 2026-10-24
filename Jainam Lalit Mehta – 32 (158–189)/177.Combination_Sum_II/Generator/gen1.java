import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1111);
    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // ---- Samples ----
        testCase = generateCase(testCase, new int[]{2, 1, 2, 7, 6, 1, 5}, 8);
        testCase = generateCase(testCase, new int[]{2, 5, 2, 1, 2}, 5);
        testCase = generateCase(testCase, new int[]{2, 1, 2}, 5);

        // ---- Single element ----
        testCase = generateCase(testCase, new int[]{1}, 1);
        testCase = generateCase(testCase, new int[]{1}, 2);
        testCase = generateCase(testCase, new int[]{5}, 5);
        testCase = generateCase(testCase, new int[]{5}, 4);
        testCase = generateCase(testCase, new int[]{5}, 30);
        testCase = generateCase(testCase, new int[]{30}, 30);
        testCase = generateCase(testCase, new int[]{31}, 30);
        testCase = generateCase(testCase, new int[]{50}, 30);
        testCase = generateCase(testCase, new int[]{50}, 1);

        // ---- Small cases ----
        testCase = generateCase(testCase, new int[]{1, 1}, 1);
        testCase = generateCase(testCase, new int[]{1, 1}, 2);
        testCase = generateCase(testCase, new int[]{1, 1}, 3);
        testCase = generateCase(testCase, new int[]{1, 2}, 3);
        testCase = generateCase(testCase, new int[]{2, 1}, 3);
        testCase = generateCase(testCase, new int[]{3, 3, 3}, 6);
        testCase = generateCase(testCase, new int[]{3, 3, 3}, 9);
        testCase = generateCase(testCase, new int[]{3, 3, 3}, 3);
        testCase = generateCase(testCase, new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
        testCase = generateCase(testCase, new int[]{1, 1, 1, 1, 1}, 3);
        testCase = generateCase(testCase, new int[]{1, 1, 1, 2, 2}, 4);
        testCase = generateCase(testCase, new int[]{4, 4, 2, 1, 4}, 9);
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5}, 15);
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5}, 16);
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5}, 5);

        // ---- Targets that cannot be reached ----
        testCase = generateCase(testCase, new int[]{2, 4, 6, 8}, 7);
        testCase = generateCase(testCase, new int[]{31, 32, 33, 50, 40}, 30);
        testCase = generateCase(testCase, new int[]{1, 1, 1}, 10);
        testCase = generateCase(testCase, new int[]{20, 25, 29}, 30);

        // ---- All elements equal ----
        testCase = generateCase(testCase, constant(100, 1), 30);
        testCase = generateCase(testCase, constant(30, 1), 30);
        testCase = generateCase(testCase, constant(29, 1), 30);
        testCase = generateCase(testCase, constant(100, 1), 1);
        testCase = generateCase(testCase, constant(100, 2), 30);
        testCase = generateCase(testCase, constant(100, 2), 29);
        testCase = generateCase(testCase, constant(100, 3), 30);
        testCase = generateCase(testCase, constant(100, 7), 28);
        testCase = generateCase(testCase, constant(100, 30), 30);
        testCase = generateCase(testCase, constant(100, 50), 30);
        testCase = generateCase(testCase, constant(10, 3), 30);
        testCase = generateCase(testCase, constant(9, 3), 30);

        // ---- Maximum size n = 100 ----
        testCase = generateCase(testCase, twice(50), 30);
        testCase = generateCase(testCase, twice(50), 1);
        testCase = generateCase(testCase, twice(50), 2);
        testCase = generateCase(testCase, twice(50), 3);
        testCase = generateCase(testCase, twice(50), 10);
        testCase = generateCase(testCase, twice(50), 20);
        testCase = generateCase(testCase, twice(50), 25);
        testCase = generateCase(testCase, reverse(twice(50)), 30);
        testCase = generateCase(testCase, shuffled(twice(50)), 30);
        testCase = generateCase(testCase, shuffled(twice(50)), 28);

        // ---- Heavy duplicate cases, lots of unique groups ----
        testCase = generateCase(testCase, repeatRange(1, 30, 3), 30);
        testCase = generateCase(testCase, repeatRange(1, 30, 3), 25);
        testCase = generateCase(testCase, repeatRange(1, 30, 1), 30);
        testCase = generateCase(testCase, repeatRange(1, 15, 6), 30);
        testCase = generateCase(testCase, repeatRange(1, 10, 10), 30);
        testCase = generateCase(testCase, repeatRange(1, 5, 20), 30);
        testCase = generateCase(testCase, repeatRange(1, 5, 20), 7);
        testCase = generateCase(testCase, repeatRange(2, 6, 10), 29);
        testCase = generateCase(testCase, shuffled(repeatRange(1, 20, 5)), 30);

        // ---- Total sum below / equal to the goal ----
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5, 6}, 30);
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5, 6, 7}, 28);
        testCase = generateCase(testCase, new int[]{1, 2, 3, 4, 5, 6, 7}, 29);
        testCase = generateCase(testCase, new int[]{10, 10, 10}, 30);
        testCase = generateCase(testCase, new int[]{10, 10, 10}, 29);

        // ---- Mixed large values mostly above the goal ----
        testCase = generateCase(testCase, new int[]{50, 49, 48, 1, 29, 30, 2, 28}, 30);
        testCase = generateCase(testCase, new int[]{50, 50, 50, 15, 15, 15, 14, 16}, 30);
        testCase = generateCase(testCase, new int[]{29, 1, 29, 1, 30, 30}, 30);

        // ---- Random cases ----
        int[] highs = {3, 5, 8, 12, 20, 30, 50};
        int made = 0;
        while (made < 60) {
            int n = 1 + random.nextInt(100);
            if (random.nextInt(3) == 0) {
                n = 1 + random.nextInt(12);
            }
            int high = highs[random.nextInt(highs.length)];
            int[] arr = randomArray(n, 1, high);
            int goal = 1 + random.nextInt(30);
            int before = testCase;
            testCase = generateCase(testCase, arr, goal);
            if (testCase > before) {
                made++;
            }
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, int[] pool, int goal) throws Exception {

        int[] sortedKey = pool.clone();
        Arrays.sort(sortedKey);
        String key = Arrays.toString(pool) + "|" + goal;
        if (!used.add(key)) {
            return id;
        }

        List<List<Integer>> answer = combinations(pool, goal);

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(pool.length + "\r\n");
            in.print(join(pool) + "\r\n");
            in.print(goal + "\r\n");

            out.print(answer.size() + "\r\n");
            for (List<Integer> combo : answer) {
                out.print(joinList(combo) + "\r\n");
            }
        }

        return id + 1;
    }

    // Independent method: group equal values, then for every distinct value decide how many copies (0..count) to use.
    static List<List<Integer>> combinations(int[] pool, int goal) {

        TreeMap<Integer, Integer> counts = new TreeMap<>();
        for (int v : pool) {
            counts.merge(v, 1, Integer::sum);
        }

        List<List<List<Integer>>> ways = new ArrayList<>();
        for (int s = 0; s <= goal; s++) {
            ways.add(new ArrayList<>());
        }
        ways.get(0).add(new ArrayList<>());

        for (Map.Entry<Integer, Integer> e : counts.entrySet()) {

            int value = e.getKey();
            int count = e.getValue();

            List<List<List<Integer>>> next = new ArrayList<>();
            for (int s = 0; s <= goal; s++) {
                next.add(new ArrayList<>(ways.get(s)));
            }

            for (int s = 0; s <= goal; s++) {
                for (List<Integer> base : ways.get(s)) {
                    for (int k = 1; k <= count && s + k * value <= goal; k++) {
                        List<Integer> combo = new ArrayList<>(base);
                        for (int t = 0; t < k; t++) {
                            combo.add(value);
                        }
                        next.get(s + k * value).add(combo);
                    }
                }
            }

            ways = next;
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

    static int[] constant(int n, int value) {

        int[] a = new int[n];
        Arrays.fill(a, value);

        return a;
    }

    // every value from 1..top appears exactly twice
    static int[] twice(int top) {

        return repeatRange(1, top, 2);
    }

    static int[] repeatRange(int from, int to, int times) {

        int[] a = new int[(to - from + 1) * times];
        int idx = 0;

        for (int v = from; v <= to; v++) {
            for (int t = 0; t < times; t++) {
                a[idx++] = v;
            }
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

    static int[] randomArray(int n, int low, int high) {

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = low + random.nextInt(high - low + 1);
        }

        return a;
    }
}
