import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static int id = 0;

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // The examples from the statement, plus the "Now Your Turn" input
        write(new int[]{4, 9, 2, 5, 1}, 10);
        write(new int[]{4, 2, 10, 5, 1, 3}, 5);
        write(new int[]{1, 10, 4, 5}, 16);

        // Smallest inputs and the boundaries of the constraints
        write(new int[]{1}, 1);
        write(new int[]{1}, 2);
        write(new int[]{100}, 100);
        write(new int[]{100}, 1);
        write(new int[]{100}, 2000);

        // Duplicates: each equal value is a different subsequence
        write(new int[]{2, 2, 2}, 4);
        write(new int[]{5, 5, 5, 5}, 10);
        write(new int[]{3, 3, 3, 3, 3}, 3);

        // k larger than the total sum (answer 0), and k equal to the total sum (answer 1)
        write(new int[]{1, 2, 3}, 7);
        write(new int[]{1, 2, 3}, 6);
        write(new int[]{7, 8, 9}, 1);

        // Maximum length, with all values equal: answer is a binomial coefficient
        int[] ones = new int[20];
        Arrays.fill(ones, 1);
        write(ones, 1);
        write(ones, 10);
        write(ones, 20);
        write(ones, 21);
        write(ones, 2000);

        int[] hundreds = new int[20];
        Arrays.fill(hundreds, 100);
        write(hundreds, 100);
        write(hundreds, 2000);
        write(hundreds, 1);

        // Mixed values with 20 elements
        write(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, 50);
        write(new int[]{100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81}, 1000);

        // Random cases: length 1..20, values 1..100, k either a real subset sum or random
        Random rnd = new Random(174);
        for (int t = 0; t < 110; t++) {
            int n = 1 + rnd.nextInt(20);
            int[] v = new int[n];
            for (int i = 0; i < n; i++) v[i] = 1 + rnd.nextInt(100);

            int k;
            if (rnd.nextBoolean()) {
                // pick a random non-empty subset and use its sum, so the answer is at least 1
                int sum = 0;
                do {
                    sum = 0;
                    for (int i = 0; i < n; i++) {
                        if (rnd.nextBoolean()) sum += v[i];
                    }
                } while (sum == 0);
                k = Math.min(sum, 2000);
            } else {
                k = 1 + rnd.nextInt(2000);
            }
            write(v, k);
        }

        System.out.println("Generated " + id + " test cases.");
    }

    // Independent reference: check every non-empty subset of indices.
    static long brute(int[] v, int k) {
        int n = v.length;
        long count = 0;
        for (int mask = 1; mask < (1 << n); mask++) {
            long sum = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) sum += v[i];
            }
            if (sum == k) count++;
        }
        return count;
    }

    static void write(int[] v, int k) throws Exception {

        if (v.length < 1 || v.length > 20) {
            throw new IllegalStateException("length out of range");
        }
        for (int x : v) {
            if (x < 1 || x > 100) throw new IllegalStateException("value out of range");
        }
        if (k < 1 || k > 2000) throw new IllegalStateException("k out of range");

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            if (i > 0) line.append(' ');
            line.append(v[i]);
        }

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            in.print(v.length + "\r\n" + line + "\r\n" + k + "\r\n");
            out.print(brute(v, k) + "\r\n");
        }

        id++;
    }
}
