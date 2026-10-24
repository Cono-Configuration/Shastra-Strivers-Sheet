import java.io.*;
import java.math.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1681);

    static final int MAX_EXP = Integer.MAX_VALUE;
    static final int MIN_EXP = Integer.MIN_VALUE;

    // base is stored as an integer number of 1e-5 units: 200000 -> 2.00000
    static final long MAX_UNITS = 9999999L;

    static final Set<String> used = new HashSet<>();
    static int skippedFixed = 0;

    static int testCase = 0;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // ---------- Examples ----------
        add("2.00000", 10);
        add("2.10000", 3);
        add("2.00000", -2);

        // ---------- exp = 0 ----------
        String[] zeroExpBases = {
                "1", "-1", "2.5", "-2.5", "99.99999", "-99.99999",
                "0.00001", "-0.00001", "50.5", "0.5", "-0.12345"
        };
        for (String b : zeroExpBases) {
            add(b, 0);
        }

        // ---------- exp = 1 ----------
        String[] oneExpBases = {
                "0.00001", "-0.00001", "99.99999", "-99.99999", "1", "-1",
                "3.14159", "0.5", "0", "-42.42424", "7"
        };
        for (String b : oneExpBases) {
            add(b, 1);
        }

        // ---------- exp = -1 ----------
        String[] minusOneBases = {
                "2", "-2", "4", "0.5", "-0.5", "0.0001", "-0.0001",
                "99.99999", "-99.99999", "3", "7", "0.3", "-0.7",
                "1", "-1", "0.25", "8"
        };
        for (String b : minusOneBases) {
            add(b, -1);
        }

        // ---------- exp = 2 ----------
        String[] twoExpBases = {
                "99.99999", "-99.99999", "0.5", "-1.5", "0.00001", "-0.00001",
                "3.3", "0.1", "12.5", "-7.77777", "0"
        };
        for (String b : twoExpBases) {
            add(b, 2);
        }

        // ---------- exp = 3 (odd) ----------
        String[] threeExpBases = {
                "-2", "2.1", "-2.1", "21.5", "-21.5", "0.5", "-0.5",
                "10", "-10", "0.99999", "1.00001", "-0.99999"
        };
        for (String b : threeExpBases) {
            add(b, 3);
        }

        // ---------- base = 0 (exp must be positive) ----------
        int[] zeroBaseExps = {1, 2, 3, 10, 100, 1000000, MAX_EXP};
        for (int e : zeroBaseExps) {
            add("0", e);
        }

        // ---------- base = 1 ----------
        int[] oneBaseExps = {0, 1, -1, 2, -2, 12345, 1000000, -1000000, MAX_EXP, MIN_EXP};
        for (int e : oneBaseExps) {
            add("1", e);
        }

        // ---------- base = -1 (parity of exp matters) ----------
        int[] minusOneBaseExps = {
                1, 2, -1, -2, 99, 100, -99, -100, 1000001,
                MAX_EXP, MAX_EXP - 1, MIN_EXP, MIN_EXP + 1
        };
        for (int e : minusOneBaseExps) {
            add("-1", e);
        }

        // ---------- extreme exponents (underflow to zero, int overflow on -exp) ----------
        add("2", MIN_EXP);
        add("2", MIN_EXP + 1);
        add("-2", MIN_EXP);
        add("-2", MIN_EXP + 1);
        add("1.5", MIN_EXP);
        add("-1.5", MIN_EXP + 1);
        add("99.99999", MIN_EXP);
        add("-99.99999", MIN_EXP + 1);
        add("1.00001", MIN_EXP);
        add("-1.00001", MIN_EXP + 1);
        add("0.5", MAX_EXP);
        add("-0.5", MAX_EXP);
        add("-0.5", MAX_EXP - 1);
        add("0.00001", MAX_EXP);
        add("-0.00001", MAX_EXP);
        add("0.99999", MAX_EXP);
        add("-0.99999", MAX_EXP - 1);
        add("0.99999", 1000000000);
        add("2", -1000000000);
        add("-3", -1000000001);

        // ---------- results sitting on or just below the +-10^4 limit ----------
        add("10", 4);
        add("-10", 4);
        add("10", -4);
        add("-10", -4);
        add("10", -5);
        add("10", -6);
        add("-10", -5);
        add("0.0001", -1);
        add("-0.0001", -1);
        add("0.001", -1);
        add("0.01", -2);
        add("0.1", -4);
        add("-0.1", -4);
        add("0.1", -3);
        add("99.99999", 2);
        add("-99.99999", 2);
        add("99.99999", -2);
        add("9.99999", 4);
        add("21.5", 3);
        add("21.54434", 3);
        add("0.0001", 1);
        add("0.01", 2);
        add("3", 8);
        add("3", -8);
        add("-3", 8);
        add("-3", -9);

        // ---------- bases very close to 1 with large exponents ----------
        add("1.00001", 900000);
        add("1.00001", -900000);
        add("0.99999", 900000);
        add("0.99999", -900000);
        add("1.00001", 920000);
        add("0.99999", -920000);
        add("1.00001", 100000);
        add("0.99999", -100000);
        add("1.00001", -1000000);
        add("0.99999", 1000000);
        add("-0.99999", 999999);
        add("-1.00001", -999999);
        add("1.00001", 500000);
        add("0.99991", -90000);
        add("1.0001", 90000);
        add("1.0001", -90000);
        add("0.9999", -92000);
        add("1.01", 900);
        add("0.99", -900);
        add("1.001", 9000);
        add("0.999", -9000);

        // ---------- small fractional base, many exponents ----------
        for (int e = 1; e <= 13; e++) {
            add("0.5", e);
            add("0.5", -e);
        }
        add("-0.5", 7);
        add("-0.5", -7);
        add("-0.5", 10);
        add("-0.5", -11);

        // ---------- assorted moderate cases ----------
        add("1.1", 1);
        add("1.1", 10);
        add("1.1", 50);
        add("1.1", 96);
        add("0.9", -87);
        add("0.9", 100);
        add("1.5", 22);
        add("1.5", -22);
        add("-1.5", 21);
        add("-1.5", -21);
        add("7", 4);
        add("-7", 4);
        add("-7", 3);
        add("2.5", 9);
        add("-2.5", 9);
        add("0.3", 7);
        add("-0.3", -7);
        add("0.75", -20);
        add("12.34567", 3);
        add("-12.34567", 3);
        add("-12.34567", -3);
        add("5.55555", 5);
        add("0.12345", 2);
        add("0.12345", -2);
        add("-0.00001", 2);
        add("0.00001", 5);

        // ---------- random: small base, small exponent ----------
        for (int i = 0; i < 10; i++) {
            randomCase(-1000000, 1000000, -12, 12, 5000);
        }

        // ---------- random: any base, exponent in a moderate range ----------
        for (int i = 0; i < 10; i++) {
            randomCase(-MAX_UNITS, MAX_UNITS, -200, 200, 5000);
        }

        // ---------- random: |base| > 1, large negative exponent ----------
        for (int i = 0; i < 8; i++) {
            long mag = 100001 + random.nextInt(9899999);
            long units = random.nextBoolean() ? mag : -mag;
            long exp = -(1000L + (long) (random.nextDouble() * (2147483648L - 1000L)));
            addUnits(units, (int) exp);
        }

        // ---------- random: |base| < 1, large positive exponent ----------
        for (int i = 0; i < 8; i++) {
            long mag = 1 + random.nextInt(99998);
            long units = random.nextBoolean() ? mag : -mag;
            long exp = 1000L + (long) (random.nextDouble() * (MAX_EXP - 1000L));
            addUnits(units, (int) exp);
        }

        // ---------- random: base near 1, big exponent of either sign ----------
        for (int i = 0; i < 12; i++) {
            long delta = 1 + random.nextInt(300);
            long units = random.nextBoolean() ? 100000 + delta : 100000 - delta;
            if (random.nextBoolean()) {
                units = -units;
            }
            randomCase(units, units, -1000000, 1000000, 5000);
        }

        // ---------- random: negative bases ----------
        for (int i = 0; i < 6; i++) {
            randomCase(-MAX_UNITS, -1, -60, 60, 5000);
        }

        // ---------- random: tiny bases ----------
        for (int i = 0; i < 6; i++) {
            randomCase(-1000, 1000, -3, 400, 5000);
        }

        System.out.println("Generated " + testCase + " test cases.");
        System.out.println("Skipped fixed candidates (invalid or unstable): " + skippedFixed);
    }

    // ================= helpers for registering test cases =================

    static void add(String base, int exp) throws Exception {
        long units = new BigDecimal(base).movePointRight(5).longValueExact();
        String expected = expectedFor(units, exp);
        String key = units + " " + exp;

        if (expected == null) {
            skippedFixed++;
            System.out.println("SKIPPED fixed case: " + formatBase(units) + " " + exp);
            return;
        }
        if (!used.add(key)) {
            return;
        }
        write(formatBase(units) + " " + exp, expected);
    }

    static void addUnits(long units, int exp) throws Exception {
        String expected = expectedFor(units, exp);
        String key = units + " " + exp;

        if (expected == null || !used.add(key)) {
            // pick a fresh candidate for the same style of case
            for (int attempt = 0; attempt < 5000; attempt++) {
                int newExp = exp > 0
                        ? (int) Math.max(1, exp - random.nextInt(1000000))
                        : (int) Math.min(-1, exp + random.nextInt(1000000));
                expected = expectedFor(units, newExp);
                key = units + " " + newExp;
                if (expected != null && used.add(key)) {
                    write(formatBase(units) + " " + newExp, expected);
                    return;
                }
                units = units > 0 ? units - 1 : units + 1;
                if (units == 0) {
                    units = 12345;
                }
            }
            throw new IllegalStateException("could not place random case");
        }
        write(formatBase(units) + " " + exp, expected);
    }

    // random base in [lo, hi] (units), random exponent in [expLo, expHi]
    static void randomCase(long lo, long hi, int expLo, int expHi, int tries) throws Exception {
        for (int attempt = 0; attempt < tries; attempt++) {

            long units = lo + (long) (random.nextDouble() * (hi - lo + 1));
            if (units > hi) {
                units = hi;
            }
            int exp = expLo + random.nextInt(expHi - expLo + 1);

            String expected = expectedFor(units, exp);
            String key = units + " " + exp;

            if (expected != null && used.add(key)) {
                write(formatBase(units) + " " + exp, expected);
                return;
            }
        }
        throw new IllegalStateException("could not place random case in range");
    }

    static void write(String input, String output) throws Exception {

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(input + "\r\n");
            out.print(output + "\r\n");
        }

        testCase++;
    }

    static String formatBase(long units) {
        String sign = units < 0 ? "-" : "";
        long abs = Math.abs(units);
        return sign + (abs / 100000) + "." + String.format("%05d", abs % 100000);
    }

    // ================= validation + expected answer =================

    // Returns the expected output line, or null if the case is invalid / numerically unstable.
    static String expectedFor(long units, int exp) {

        if (Math.abs(units) > MAX_UNITS) {
            return null;
        }
        if (units == 0 && exp <= 0) {
            return null;
        }

        String truth;

        if (units == 0) {
            truth = "0.00000";
        } else {
            double magnitude = Math.abs(units) / 100000.0;
            double log10Value = exp * Math.log10(magnitude);

            if (log10Value > 4.0000001) {
                return null;
            }

            if (log10Value < -12) {
                truth = "0.00000";
            } else {
                BigDecimal exact = exactPower(units, exp);

                if (exact.abs().compareTo(BigDecimal.valueOf(10000)) > 0) {
                    return null;
                }

                // reject values too close to a rounding boundary (ties / near ties)
                BigDecimal scaled = exact.movePointRight(5).abs();
                BigDecimal frac = scaled.subtract(scaled.setScale(0, RoundingMode.FLOOR));
                if (frac.subtract(new BigDecimal("0.5")).abs()
                        .compareTo(new BigDecimal("0.000001")) < 0) {
                    return null;
                }

                truth = exact.setScale(5, RoundingMode.HALF_UP).toPlainString();
            }
        }

        // every common floating-point implementation must print the same text
        double base = Double.parseDouble(formatBase(units));

        String[] variants = {
                format(powReciprocalAfter(base, exp)),
                format(powReciprocalBefore(base, exp)),
                format(powRecursive(base, exp)),
                format(Math.pow(base, exp))
        };

        for (String v : variants) {
            if (!v.equals(truth)) {
                return null;
            }
        }

        return truth;
    }

    static BigDecimal exactPower(long units, int exp) {

        MathContext mc = new MathContext(80);

        BigDecimal b = BigDecimal.valueOf(units, 5);
        long p = Math.abs((long) exp);
        BigDecimal r = BigDecimal.ONE;

        while (p > 0) {
            if ((p & 1) == 1) {
                r = r.multiply(b, mc);
            }
            p >>= 1;
            if (p > 0) {
                b = b.multiply(b, mc);
            }
        }

        if (exp < 0) {
            r = BigDecimal.ONE.divide(r, mc);
        }

        return r;
    }

    // final answer format: 5 decimals, never "-0.00000"
    static String format(double d) {

        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return "INVALID";
        }

        String s = String.format(Locale.US, "%.5f", d);

        if (s.equals("-0.00000")) {
            return "0.00000";
        }

        return s;
    }

    // ---- reference implementations used only to cross-check the expected output ----

    static double powReciprocalAfter(double x, int n) {

        long p = Math.abs((long) n);
        double result = 1.0;
        double b = x;

        while (p > 0) {
            if ((p & 1) == 1) {
                result *= b;
            }
            b *= b;
            p >>= 1;
        }

        return n < 0 ? 1.0 / result : result;
    }

    static double powReciprocalBefore(double x, int n) {

        long p = n;
        double b = x;

        if (p < 0) {
            b = 1.0 / b;
            p = -p;
        }

        double result = 1.0;

        while (p > 0) {
            if ((p & 1) == 1) {
                result *= b;
            }
            b *= b;
            p >>= 1;
        }

        return result;
    }

    static double powRecursive(double x, int n) {

        long p = n;

        if (p < 0) {
            return 1.0 / recursive(x, -p);
        }

        return recursive(x, p);
    }

    static double recursive(double x, long n) {

        if (n == 0) {
            return 1.0;
        }

        double half = recursive(x, n / 2);

        if (n % 2 == 0) {
            return half * half;
        }

        return half * half * x;
    }
}
