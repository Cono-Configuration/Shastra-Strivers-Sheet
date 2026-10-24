import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1711);

    static final long MOD = 1_000_000_007L;
    static final long MAX_LENGTH = 1_000_000_000_000_000L;

    static final Set<Long> used = new HashSet<>();
    static int testCase = 0;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // ---------- Examples ----------
        add(1);
        add(4);
        add(50);

        // ---------- every small length (odd / even, tiny counts) ----------
        for (long n = 2; n <= 30; n++) {
            add(n);
        }

        // ---------- around round numbers ----------
        long[] roundBases = {100, 1000, 10000, 100000, 1000000, 10000000L, 100000000L,
                1000000000L, 10000000000L, 100000000000L, 1000000000000L,
                10000000000000L, 100000000000000L};
        for (long r : roundBases) {
            add(r - 1);
            add(r);
            add(r + 1);
        }
        add(49);
        add(51);

        // ---------- powers of two (and neighbours) ----------
        int[] shifts = {5, 10, 16, 20, 30, 31, 32, 40, 49};
        for (int k : shifts) {
            long p = 1L << k;
            add(p - 1);
            add(p);
            add(p + 1);
        }

        // ---------- values beyond 32-bit int ----------
        add(2147483647L);
        add(2147483648L);
        add(2147483649L);
        add(4294967295L);
        add(4294967296L);
        add(10000000000L + 7);

        // ---------- lengths tied to the modulus (exponent hits 10^9+6) ----------
        add(MOD - 1);
        add(MOD);
        add(MOD + 1);
        add(2 * (MOD - 1) - 1);
        add(2 * (MOD - 1));
        add(2 * (MOD - 1) + 1);
        add(2 * (MOD - 1) + 2);
        add(2 * MOD);
        add(4 * (MOD - 1));
        add(1000 * (MOD - 1));
        add(1000 * (MOD - 1) + 1);

        // ---------- maximum and near-maximum ----------
        add(MAX_LENGTH);
        add(MAX_LENGTH - 1);
        add(MAX_LENGTH - 2);
        add(MAX_LENGTH / 2);
        add(MAX_LENGTH / 2 + 1);
        add(MAX_LENGTH / 3);
        add(123456789012345L);
        add(987654321098765L);
        add(999999999999999L);

        // ---------- random cases for every magnitude ----------
        long low = 10;
        for (int digits = 2; digits <= 15; digits++) {
            long high = digits == 15 ? MAX_LENGTH : low * 10;
            for (int i = 0; i < 3; i++) {
                randomIn(low, high);
            }
            low = low * 10;
        }
        for (int i = 0; i < 6; i++) {
            randomIn(1, 200);
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static void add(long n) throws Exception {

        if (n < 1 || n > MAX_LENGTH) {
            throw new IllegalStateException("length out of range: " + n);
        }
        if (!used.add(n)) {
            return;
        }
        write(n);
    }

    static void randomIn(long lo, long hi) throws Exception {

        while (true) {
            long n = lo + (long) (random.nextDouble() * (hi - lo));
            if (n > hi) {
                n = hi;
            }
            if (n >= 1 && n <= MAX_LENGTH && used.add(n)) {
                write(n);
                return;
            }
        }
    }

    static void write(long n) throws Exception {

        long answer = solve(n);

        // cross-check with two independent methods
        if (answer != solveBig(n)) {
            throw new IllegalStateException("BigInteger mismatch for " + n);
        }
        if (n <= 6 && answer != brute(n)) {
            throw new IllegalStateException("brute force mismatch for " + n);
        }

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(n + "\r\n");
            out.print(answer + "\r\n");
        }

        testCase++;
    }

    // 20^(n/2) * (5 if n is odd) -- a different grouping from the reference solution
    static long solve(long n) {

        long result = powMod(20, n / 2);

        if (n % 2 == 1) {
            result = result * 5 % MOD;
        }

        return result;
    }

    static long powMod(long base, long exp) {

        if (exp == 0) {
            return 1;
        }

        long half = powMod(base, exp / 2);
        long result = half * half % MOD;

        if (exp % 2 == 1) {
            result = result * base % MOD;
        }

        return result;
    }

    static long solveBig(long n) {

        BigInteger m = BigInteger.valueOf(MOD);

        BigInteger a = BigInteger.valueOf(5).modPow(BigInteger.valueOf((n + 1) / 2), m);
        BigInteger b = BigInteger.valueOf(4).modPow(BigInteger.valueOf(n / 2), m);

        return a.multiply(b).mod(m).longValue();
    }

    // enumerates every digit string of length n (only used for tiny n)
    static long brute(long n) {

        int len = (int) n;
        long limit = 1;
        for (int i = 0; i < len; i++) {
            limit *= 10;
        }

        long count = 0;

        for (long value = 0; value < limit; value++) {

            long v = value;
            boolean good = true;

            for (int pos = len - 1; pos >= 0; pos--) {
                int digit = (int) (v % 10);
                v /= 10;

                if (pos % 2 == 0) {
                    if (digit % 2 != 0) {
                        good = false;
                        break;
                    }
                } else {
                    if (digit != 2 && digit != 3 && digit != 5 && digit != 7) {
                        good = false;
                        break;
                    }
                }
            }

            if (good) {
                count++;
            }
        }

        return count % MOD;
    }
}
