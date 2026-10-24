import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        List<int[]> cases = new ArrayList<>();

        // Both examples from the statement
        cases.add(new int[]{1, 2, 2});
        cases.add(new int[]{0});

        // Length 1: every value in the allowed range, including both boundaries
        for (int v = -10; v <= 10; v++) {
            cases.add(new int[]{v});
        }

        // Length 2: equal pairs, boundary pairs, both orders of unequal pairs
        int[][] pairs = {{-10, -10}, {0, 0}, {10, 10}, {-10, 10}, {10, -10},
                         {1, 2}, {2, 1}, {-1, 1}, {3, -3}};
        for (int[] p : pairs) {
            cases.add(p);
        }

        // All-equal arrays of every length 2..10 (maximum duplication)
        for (int len = 2; len <= 10; len++) {
            for (int v : new int[]{-10, 0, 10}) {
                int[] a = new int[len];
                Arrays.fill(a, v);
                cases.add(a);
            }
        }

        // Duplicate groups at the start, middle, and end; sorted and unsorted
        cases.add(new int[]{1, 1, 2});
        cases.add(new int[]{2, 1, 1});
        cases.add(new int[]{1, 2, 1});
        cases.add(new int[]{3, 3, 3, 1});
        cases.add(new int[]{1, 3, 3, 3});
        cases.add(new int[]{-2, -2, 5, 5, 5});
        cases.add(new int[]{2, 2, 3, 3, 3, 3});
        cases.add(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2});
        cases.add(new int[]{2, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        cases.add(new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2});
        cases.add(new int[]{-10, -10, -9, -9, 10, 10, 9, 9, 0, 0});
        cases.add(new int[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1});
        cases.add(new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1, 2});
        cases.add(new int[]{0, -10, 10, 0, -10, 10, 0, -10, 10, 0});
        cases.add(new int[]{7, 7, 8, 8, 9, 9, 10, 10, -1, -1});
        cases.add(new int[]{5, -5, 5, -5, 5, -5, 5, -5, 5, -5});

        // Length 10 with all distinct values (largest power set, 1024 subsets)
        cases.add(new int[]{10, -10, 5, -5, 0, 3, -3, 7, -7, 1});
        cases.add(new int[]{-10, -9, -8, -7, -6, -5, -4, -3, -2, -1});
        cases.add(new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        cases.add(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        cases.add(new int[]{-1, -2, -3, -4, -5, -6, -7, -8, -9, -10});

        // Random cases with the full value range
        Random rnd = new Random(178);
        for (int t = 0; t < 40; t++) {
            int n = 1 + rnd.nextInt(10);
            cases.add(randomArray(rnd, n, -10, 10));
        }

        // Random cases with a narrow value window, so duplicates are frequent
        for (int t = 0; t < 40; t++) {
            int n = 1 + rnd.nextInt(10);
            int lo = -10 + rnd.nextInt(18);
            cases.add(randomArray(rnd, n, lo, lo + 2));
        }

        // Random length-10 arrays (largest inputs)
        for (int t = 0; t < 20; t++) {
            cases.add(randomArray(rnd, 10, -10, 10));
        }

        int id = 0;
        for (int[] arr : cases) {
            id = writeCase(id, arr);
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static int[] randomArray(Random rnd, int n, int lo, int hi) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = lo + rnd.nextInt(hi - lo + 1);
        }
        return a;
    }

    static int writeCase(int id, int[] arr) throws Exception {

        if (arr.length < 1 || arr.length > 10) {
            throw new IllegalStateException("length out of range");
        }
        for (int v : arr) {
            if (v < -10 || v > 10) {
                throw new IllegalStateException("value out of range");
            }
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            StringBuilder inLine = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) inLine.append(' ');
                inLine.append(arr[i]);
            }
            in.print(arr.length + "\r\n" + inLine + "\r\n");

            // Independent reference: enumerate every bitmask, sort each subset,
            // then keep distinct subsets in lexicographic order.
            TreeSet<List<Integer>> subsets = new TreeSet<>(gen1::compareSeq);
            int n = arr.length;
            for (int mask = 0; mask < (1 << n); mask++) {
                List<Integer> s = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) s.add(arr[i]);
                }
                Collections.sort(s);
                subsets.add(s);
            }

            for (List<Integer> s : subsets) {
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < s.size(); i++) {
                    if (i > 0) sb.append(',');
                    sb.append(s.get(i));
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
