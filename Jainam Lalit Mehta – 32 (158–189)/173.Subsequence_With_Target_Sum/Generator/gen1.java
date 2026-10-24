import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1731);

    static final int MAX_K = 2000;

    static final Set<String> used = new HashSet<>();
    static int testCase = 0;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // ---------- Examples ----------
        add(8, 1, 2, 3, 4, 5);
        add(10, 4, 3, 9, 2);
        add(16, 1, 10, 4, 5);

        // ---------- n = 1 ----------
        add(5, 5);
        add(4, 5);
        add(6, 5);
        add(10, 5);
        add(1, 1);
        add(2, 1);
        add(100, 100);
        add(1, 100);
        add(2000, 100);

        // ---------- n = 2, duplicates ----------
        add(3, 3, 3);
        add(6, 3, 3);
        add(9, 3, 3);
        add(1, 1, 2);
        add(2, 1, 2);
        add(3, 1, 2);
        add(4, 1, 2);
        add(200, 100, 100);
        add(199, 100, 100);
        add(100, 100, 100);
        add(110, 50, 60);
        add(111, 50, 60);

        // ---------- greedy traps ----------
        add(8, 6, 4, 4);
        add(11, 9, 7, 6, 5);
        add(17, 10, 9, 8);
        add(19, 10, 9, 8);
        add(20, 10, 9, 8);
        add(12, 7, 5, 6, 6);
        add(30, 25, 11, 10, 10, 9);

        // ---------- sum boundaries ----------
        add(15, 1, 2, 3, 4, 5);
        add(14, 1, 2, 3, 4, 5);
        add(16, 1, 2, 3, 4, 5);
        add(1, 1, 2, 3, 4, 5);
        add(1, 2, 3, 4, 5);
        add(2000, 1, 2, 3, 4, 5);
        add(180, 50, 60, 70);
        add(181, 50, 60, 70);
        add(1000, 50, 60, 70);
        add(120, 50, 60, 70);
        add(130, 50, 60, 70);

        // ---------- parity / divisibility traps ----------
        add(5, 2, 4, 6, 8);
        add(20, 2, 4, 6, 8);
        add(21, 2, 4, 6, 8);
        add(33, 5, 10, 15, 20, 25);
        add(35, 5, 10, 15, 20, 25);
        add(75, 5, 10, 15, 20, 25);
        add(76, 5, 10, 15, 20, 25);
        add(8, 1, 3, 5, 7);
        add(2, 1, 3, 5, 7);
        add(25, 10, 20, 30);
        add(6, 12, 18, 30);
        add(42, 12, 18, 30);
        add(60, 12, 18, 30);
        add(14, 7, 7, 7);
        add(21, 7, 7, 7);
        add(22, 7, 7, 7);
        add(15, 7, 7, 7);

        // ---------- ordering / position traps ----------
        add(100, 100, 1, 99);
        add(100, 1, 99, 100);
        add(11, 3, 5, 7, 11);
        add(3, 3, 5, 7, 11);
        add(12, 3, 5, 7, 11);
        add(26, 3, 5, 7, 11);
        add(27, 3, 5, 7, 11);

        // ---------- binary weights ----------
        add(127, 1, 2, 4, 8, 16, 32, 64);
        add(128, 1, 2, 4, 8, 16, 32, 64);
        add(63, 1, 2, 4, 8, 16, 32, 64);
        add(65, 1, 2, 4, 8, 16, 32, 64);
        add(100, 64, 32, 16, 8, 4, 2, 1);

        // ---------- maximum size arrays ----------
        add(2000, repeat(100, 20));
        add(1999, repeat(100, 20));
        add(1900, repeat(100, 20));
        add(1950, repeat(100, 20));
        add(100, repeat(100, 20));
        add(1, repeat(100, 20));
        add(101, repeat(100, 20));
        add(20, repeat(1, 20));
        add(21, repeat(1, 20));
        add(1, repeat(1, 20));
        add(10, repeat(1, 20));
        add(2000, repeat(1, 20));

        int[] oneToTwenty = new int[20];
        for (int i = 0; i < 20; i++) {
            oneToTwenty[i] = i + 1;
        }
        add(210, oneToTwenty);
        add(209, oneToTwenty);
        add(211, oneToTwenty);
        add(105, oneToTwenty);
        add(2000, oneToTwenty);
        add(1, oneToTwenty);

        int[] eightyOneToHundred = new int[20];
        for (int i = 0; i < 20; i++) {
            eightyOneToHundred[i] = 100 - i;
        }
        add(1810, eightyOneToHundred);
        add(1809, eightyOneToHundred);
        add(1729, eightyOneToHundred);
        add(1, eightyOneToHundred);
        add(81, eightyOneToHundred);
        add(82, eightyOneToHundred);
        add(2000, eightyOneToHundred);
        add(181, eightyOneToHundred);
        add(180, eightyOneToHundred);

        add(150, 50, 50, 50, 50);
        add(175, 50, 50, 50, 50);
        add(200, 50, 50, 50, 50);
        add(201, 50, 50, 50, 50);

        // ---------- random cases (half Yes, half No) ----------
        boolean wantYes = true;

        for (int i = 0; i < 25; i++) {
            int n = 1 + random.nextInt(20);
            randomCase(randomArray(n, 1, 100), wantYes);
            wantYes = !wantYes;
        }
        for (int i = 0; i < 10; i++) {
            int n = 10 + random.nextInt(11);
            randomCase(randomArray(n, 1, 5), wantYes);
            wantYes = !wantYes;
        }
        for (int i = 0; i < 10; i++) {
            int n = 5 + random.nextInt(16);
            randomCase(randomArray(n, 60, 100), wantYes);
            wantYes = !wantYes;
        }
        for (int i = 0; i < 10; i++) {
            int step = i % 2 == 0 ? 7 : 10;
            int n = 4 + random.nextInt(17);
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = step * (1 + random.nextInt(100 / step));
            }
            randomCase(arr, wantYes);
            wantYes = !wantYes;
        }
        for (int i = 0; i < 10; i++) {
            int n = 8 + random.nextInt(13);
            int[] pool = {1 + random.nextInt(100), 1 + random.nextInt(100), 1 + random.nextInt(100)};
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = pool[random.nextInt(3)];
            }
            randomCase(arr, wantYes);
            wantYes = !wantYes;
        }
        for (int i = 0; i < 10; i++) {
            randomCase(randomArray(20, 1, 100), wantYes);
            wantYes = !wantYes;
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // ================= helpers =================

    static int[] repeat(int value, int count) {
        int[] arr = new int[count];
        Arrays.fill(arr, value);
        return arr;
    }

    static int[] randomArray(int n, int lo, int hi) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = lo + random.nextInt(hi - lo + 1);
        }
        return arr;
    }

    // add(k, values...)
    static void add(int k, int... values) throws Exception {
        if (!used.add(Arrays.toString(values) + "|" + k)) {
            return;
        }
        write(values, k);
    }

    static void randomCase(int[] values, boolean wantYes) throws Exception {

        Set<Integer> sums = allSubsetSums(values);

        List<Integer> yes = new ArrayList<>();
        List<Integer> no = new ArrayList<>();
        int total = 0;
        for (int v : values) {
            total += v;
        }

        for (int k = 1; k <= MAX_K; k++) {
            if (sums.contains(k)) {
                yes.add(k);
            } else if (k <= total + 5 || random.nextInt(20) == 0) {
                no.add(k);
            }
        }

        List<Integer> pick = wantYes ? yes : no;
        if (pick.isEmpty()) {
            pick = wantYes ? no : yes;
        }
        if (pick.isEmpty()) {
            return;
        }

        for (int attempt = 0; attempt < 200; attempt++) {
            int k = pick.get(random.nextInt(pick.size()));
            if (used.add(Arrays.toString(values) + "|" + k)) {
                write(values, k);
                return;
            }
        }
    }

    // brute force: grow the set of achievable sums one element at a time
    // (different technique from the boolean-array DP used in the solution)
    static Set<Integer> allSubsetSums(int[] values) {

        Set<Integer> sums = new HashSet<>();
        sums.add(0);

        for (int v : values) {
            Set<Integer> next = new HashSet<>(sums);
            for (int s : sums) {
                next.add(s + v);
            }
            sums = next;
        }

        return sums;
    }

    static void write(int[] values, int k) throws Exception {

        // constraint checks
        if (values.length < 1 || values.length > 20 || k < 1 || k > MAX_K) {
            throw new IllegalStateException("constraint violated");
        }
        for (int v : values) {
            if (v < 1 || v > 100) {
                throw new IllegalStateException("value out of range");
            }
        }

        boolean exists = allSubsetSums(values).contains(k);

        // second independent check: plain recursion (take / skip)
        if (exists != recurse(values, 0, k)) {
            throw new IllegalStateException("recursion mismatch");
        }

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

        StringBuilder in = new StringBuilder();
        in.append(values.length).append("\r\n");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                in.append(' ');
            }
            in.append(values[i]);
        }
        in.append("\r\n").append(k).append("\r\n");

        try (PrintWriter inWriter = new PrintWriter(new FileWriter(inputFile));
             PrintWriter outWriter = new PrintWriter(new FileWriter(outputFile))) {

            inWriter.print(in.toString());
            outWriter.print((exists ? "Yes" : "No") + "\r\n");
        }

        testCase++;
    }

    static boolean recurse(int[] values, int index, int remaining) {

        if (remaining == 0) {
            return true;
        }
        if (remaining < 0 || index == values.length) {
            return false;
        }

        return recurse(values, index + 1, remaining - values[index])
                || recurse(values, index + 1, remaining);
    }
}
