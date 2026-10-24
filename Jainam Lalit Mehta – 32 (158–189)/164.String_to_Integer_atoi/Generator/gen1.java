import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final int MAX_LEN = 200;

    static final String ALPHABET =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 +-.";

    static final Random random = new Random(8);

    // Prevents writing the same input twice.
    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // ------------------------------------------------------------
        // 1. Empty string and whitespace-only strings
        // ------------------------------------------------------------
        String[] blank = {"", " ", "   ", repeat(" ", 10), repeat(" ", 200)};
        for (String s : blank) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 2. Sign handling: lone sign, two signs, sign followed by space
        // ------------------------------------------------------------
        String[] signs = {
                "+", "-", "+-", "-+", "++1", "--1", "+-12", "-+4",
                "+ 1", "- 1", " + 1"
        };
        for (String s : signs) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 3. Plain numbers and leading zeros
        // ------------------------------------------------------------
        String[] plain = {
                "0", "1", "42", "-42", "+42", "007", "-007", "-0",
                "0000", "00001234", "-00001234"
        };
        for (String s : plain) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 4. Leading whitespace
        // ------------------------------------------------------------
        String[] leading = {
                " 42", "   -42", "   +42",
                repeat(" ", 199) + "7", repeat(" ", 194) + "-12345"
        };
        for (String s : leading) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 5. Trailing and embedded whitespace
        // ------------------------------------------------------------
        String[] spaces = {
                "42 ", "42   ", "4 2", "  4 193 with words", "42 abc", "12  34"
        };
        for (String s : spaces) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 6. Reading stops at the first non-digit
        // ------------------------------------------------------------
        String[] stops = {
                "1337c0d3", "0-1", "42abc", "4193 with words", "3.14159",
                "-3.9", "1e5", "1+2", "123.", "12+34", "0abc", "000abc12",
                "00-1", "9z9"
        };
        for (String s : stops) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 7. First non-space character is not a digit (answer 0)
        // ------------------------------------------------------------
        String[] noDigits = {
                ".5", "-.5", "+.1", ".", "..5", "a1", "abc", "ABC123",
                "z-1", "words and 987", "ice 77", "- -1"
        };
        for (String s : noDigits) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 8. 32-bit boundaries and overflow rounding
        // ------------------------------------------------------------
        String[] limits = {
                "2147483647", "2147483648", "2147483649",
                "2147483646", "21474836470", "214748364",
                "-2147483647", "-2147483648", "-2147483649",
                "9999999999", "-9999999999",
                "92233720368547758070", "-92233720368547758070",
                "2147483647abc", "2147483648abc", "2147483647 8",
                "-2147483648.5", "-91283472332", "99999999999 x"
        };
        for (String s : limits) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 9. Very long strings (up to the maximum length of 200)
        // ------------------------------------------------------------
        testCase = generateCase(testCase, repeat("9", 200));
        testCase = generateCase(testCase, "-" + repeat("9", 199));
        testCase = generateCase(testCase, repeat("0", 198) + "42");
        testCase = generateCase(testCase, repeat(" ", 198) + "42");
        testCase = generateCase(testCase, repeat(" ", 100) + "-" + repeat("0", 50) + "2147483648");
        testCase = generateCase(testCase, "+" + repeat("0", 100) + "2147483648");
        testCase = generateCase(testCase, "-" + repeat("0", 100) + "2147483647");
        testCase = generateCase(testCase, repeat("0", 40) + "2147483647");
        testCase = generateCase(testCase, "123" + repeat("a", 197));

        // ------------------------------------------------------------
        // 10. Examples (5 original LeetCode + 5 new)
        // ------------------------------------------------------------
        String[] examples = {
                "42", "   -042", "1337c0d3", "0-1", "words and 987",
                "315", "   +0072", "908xyz12"
        };
        for (String s : examples) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 11. Random structured strings:
        //     spaces, optional sign, zeros, digits, then junk
        // ------------------------------------------------------------
        for (int i = 0; i < 8; i++) {
            testCase = generateCase(testCase, randomStructured());
        }

        // ------------------------------------------------------------
        // 12. Fully random strings over the whole alphabet
        // ------------------------------------------------------------
        for (int i = 0; i < 4; i++) {

            int len = random.nextInt(MAX_LEN + 1);
            StringBuilder sb = new StringBuilder();

            for (int k = 0; k < len; k++) {
                sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            }

            testCase = generateCase(testCase, sb.toString());
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // ----------------------------------------------------------------
    // Validates the input, solves it, cross-checks the answer with an
    // independent oracle and writes the test case.
    // ----------------------------------------------------------------
    static int generateCase(int id, String input) throws Exception {

        if (!used.add(input)) {
            return id;
        }

        validate(input);

        int output = solve(input);

        if (output != bruteForce(input)) {
            throw new IllegalStateException("Solution mismatch for: [" + input + "]");
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

    // Optimal solution: single pass, O(1) extra space, int arithmetic only.
    static int solve(String s) {

        int n = s.length();
        int i = 0;

        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        int sign = 1;

        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            if (s.charAt(i) == '-') {
                sign = -1;
            }
            i++;
        }

        int result = 0;

        while (i < n) {

            char ch = s.charAt(i);

            if (ch < '0' || ch > '9') {
                break;
            }

            int digit = ch - '0';

            if (result > Integer.MAX_VALUE / 10
                    || (result == Integer.MAX_VALUE / 10 && digit > 7)) {
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            result = result * 10 + digit;
            i++;
        }

        return sign * result;
    }

    // Independent check: regex to cut out "spaces, sign, digits",
    // then BigInteger arithmetic and explicit clamping.
    static int bruteForce(String s) {

        Matcher m = Pattern.compile("^ *([+-]?)([0-9]*)").matcher(s);
        m.find();

        String sign = m.group(1);
        String digits = m.group(2);

        if (digits.isEmpty()) {
            return 0;
        }

        BigInteger value = new BigInteger(digits);

        if (sign.equals("-")) {
            value = value.negate();
        }

        BigInteger max = BigInteger.valueOf(Integer.MAX_VALUE);
        BigInteger min = BigInteger.valueOf(Integer.MIN_VALUE);

        if (value.compareTo(max) > 0) {
            return Integer.MAX_VALUE;
        }

        if (value.compareTo(min) < 0) {
            return Integer.MIN_VALUE;
        }

        return value.intValue();
    }

    // Checks every constraint from the problem statement.
    static void validate(String s) {

        if (s.length() > MAX_LEN) {
            throw new IllegalStateException("Too long (" + s.length() + ")");
        }

        for (char ch : s.toCharArray()) {
            if (ALPHABET.indexOf(ch) < 0) {
                throw new IllegalStateException("Illegal character '" + ch + "' in [" + s + "]");
            }
        }
    }

    // spaces + optional sign + zeros + digits + junk
    static String randomStructured() {

        StringBuilder sb = new StringBuilder();

        sb.append(repeat(" ", random.nextInt(6)));

        int sign = random.nextInt(3);
        if (sign == 1) {
            sb.append('+');
        } else if (sign == 2) {
            sb.append('-');
        }

        sb.append(repeat("0", random.nextInt(4)));

        int[] lengths = {0, 1, 2, 5, 9, 10, 10, 11, 12, 15, 25};
        int digitCount = lengths[random.nextInt(lengths.length)];

        for (int i = 0; i < digitCount; i++) {
            sb.append((char) ('0' + random.nextInt(10)));
        }

        int junk = random.nextInt(8);

        for (int i = 0; i < junk; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return sb.toString();
    }

    static String repeat(String str, int count) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(str);
        }

        return result.toString();
    }
}
