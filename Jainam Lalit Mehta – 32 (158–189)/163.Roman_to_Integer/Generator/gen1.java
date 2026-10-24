import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final int MAX_LEN = 15;

    static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    // Only canonical Roman numerals in [1, 3999] match this pattern.
    static final String VALID_PATTERN =
            "M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})";

    static final Random random = new Random(13);

    // Prevents writing the same input twice.
    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // ------------------------------------------------------------
        // 1. Every digit pattern in every decimal position
        //    ones     : I II III IV V VI VII VIII IX
        //    tens     : X XX XXX XL L LX LXX LXXX XC
        //    hundreds : C CC CCC CD D DC DCC DCCC CM
        //    thousands: M MM MMM
        //    (covers each single symbol, every repeat count and all
        //     six subtractive pairs on their own)
        // ------------------------------------------------------------
        for (int unit = 1; unit <= 100; unit *= 10) {
            for (int digit = 1; digit <= 9; digit++) {
                testCase = generateCase(testCase, digit * unit);
            }
        }
        testCase = generateCase(testCase, 1000);
        testCase = generateCase(testCase, 2000);
        testCase = generateCase(testCase, 3000);

        // ------------------------------------------------------------
        // 2. Smallest and largest values, and maximum length (15)
        // ------------------------------------------------------------
        testCase = generateCase(testCase, 3999);   // MMMCMXCIX, largest value
        testCase = generateCase(testCase, 3888);   // MMMDCCCLXXXVIII, longest numeral (15)
        testCase = generateCase(testCase, 3788);   // length 14
        testCase = generateCase(testCase, 3878);   // length 14
        testCase = generateCase(testCase, 2888);   // length 14
        testCase = generateCase(testCase, 2788);   // length 13
        testCase = generateCase(testCase, 788);    // length 11
        testCase = generateCase(testCase, 888);
        testCase = generateCase(testCase, 88);
        testCase = generateCase(testCase, 38);
        testCase = generateCase(testCase, 1666);   // M D C L X V I, every symbol once

        // ------------------------------------------------------------
        // 3. Subtractive pair inside a longer numeral
        //    (start / middle / end, several pairs together)
        // ------------------------------------------------------------
        int[] subtractive = {
                14, 19, 24, 29, 44, 49, 94, 99,
                444, 449, 494, 499, 944, 949, 994, 999,
                1444, 1994, 1999, 3994, 3949, 3499, 3444
        };
        for (int n : subtractive) {
            testCase = generateCase(testCase, n);
        }

        // ------------------------------------------------------------
        // 4. Missing decimal places (zero digits between symbols)
        // ------------------------------------------------------------
        int[] zeros = {
                1001, 1004, 1009, 1010, 1040, 1090, 1100, 1400, 1900,
                2002, 3003, 3030, 3300, 3400, 3900
        };
        for (int n : zeros) {
            testCase = generateCase(testCase, n);
        }

        // ------------------------------------------------------------
        // 5. Values right next to a symbol boundary
        // ------------------------------------------------------------
        int[] boundaries = {399, 401, 499, 501, 899, 901, 999, 1001};
        for (int n : boundaries) {
            testCase = generateCase(testCase, n);
        }

        // ------------------------------------------------------------
        // 6. Examples (3 original LeetCode + 3 new) and well-known values
        // ------------------------------------------------------------
        int[] examples = {3, 58, 1994, 24, 789, 2494, 12, 27, 2024, 2026, 1987};
        for (int n : examples) {
            testCase = generateCase(testCase, n);
        }

        // ------------------------------------------------------------
        // 7. Random values in different ranges
        // ------------------------------------------------------------
        for (int i = 0; i < 3; i++) {
            testCase = generateCase(testCase, 1 + random.nextInt(99));        // 1 - 99
        }
        for (int i = 0; i < 3; i++) {
            testCase = generateCase(testCase, 100 + random.nextInt(900));     // 100 - 999
        }
        for (int i = 0; i < 6; i++) {
            testCase = generateCase(testCase, 1000 + random.nextInt(3000));   // 1000 - 3999
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // ----------------------------------------------------------------
    // Converts n to a numeral, validates it, solves it, cross-checks the
    // answer two ways and writes the test case.
    // ----------------------------------------------------------------
    static int generateCase(int id, int n) throws Exception {

        String input = toRoman(n);

        if (!used.add(input)) {
            return id;
        }

        validate(input);

        int output = solve(input);

        if (output != n) {
            throw new IllegalStateException("Solution gave " + output + " but expected " + n + " for " + input);
        }

        if (output != parseByTokens(input)) {
            throw new IllegalStateException("Token parser mismatch for " + input);
        }

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", id);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.println(input);
            out.println(output);
        }

        return id + 1;
    }

    // Optimal solution: right to left, single pass, O(1) extra space.
    static int solve(String s) {

        int total = 0;
        int prev = 0;

        for (int i = s.length() - 1; i >= 0; i--) {

            int cur = value(s.charAt(i));

            if (cur < prev) {
                total -= cur;
            } else {
                total += cur;
            }

            prev = cur;
        }

        return total;
    }

    static int value(char ch) {

        switch (ch) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default:  return 0;
        }
    }

    // Independent check: consume the numeral greedily token by token
    // (M, CM, D, CD, C, XC, L, XL, X, IX, V, IV, I).
    static int parseByTokens(String s) {

        int pos = 0;
        int total = 0;

        for (int k = 0; k < SYMBOLS.length; k++) {
            while (s.startsWith(SYMBOLS[k], pos)) {
                total += VALUES[k];
                pos += SYMBOLS[k].length();
            }
        }

        if (pos != s.length()) {
            throw new IllegalStateException("Token parser could not consume: " + s);
        }

        return total;
    }

    // Standard integer -> canonical Roman numeral conversion.
    static String toRoman(int n) {

        if (n < 1 || n > 3999) {
            throw new IllegalArgumentException("Out of range: " + n);
        }

        StringBuilder sb = new StringBuilder();

        for (int k = 0; k < VALUES.length; k++) {
            while (n >= VALUES[k]) {
                sb.append(SYMBOLS[k]);
                n -= VALUES[k];
            }
        }

        return sb.toString();
    }

    // Checks every constraint from the problem statement.
    static void validate(String s) {

        if (s.length() < 1 || s.length() > MAX_LEN) {
            throw new IllegalStateException("Bad length " + s.length() + ": " + s);
        }

        if (!s.matches(VALID_PATTERN)) {
            throw new IllegalStateException("Not a valid Roman numeral: " + s);
        }
    }
}
