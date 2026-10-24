import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final int MAX_LEN = 100;

    static final String DIGITS = "0123456789";
    static final String OPERATORS = "+-*/";
    static final String FILLER = DIGITS + OPERATORS;

    static final Random random = new Random(1614);

    // Prevents writing the same input twice.
    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // ------------------------------------------------------------
        // 1. Minimum length: one digit and every operator (no parentheses)
        // ------------------------------------------------------------
        String[] single = {"0", "7", "+", "-", "*", "/"};
        for (String s : single) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 2. No parentheses at all (answer must be 0), incl. max length
        // ------------------------------------------------------------
        String[] noParens = {"12", "007", "+-*/", "8*5-2+9", "10/2*3-4+5"};
        for (String s : noParens) {
            testCase = generateCase(testCase, s);
        }
        testCase = generateCase(testCase, repeat("9", 100));
        testCase = generateCase(testCase, repeat("+-*/", 25));
        testCase = generateCase(testCase, repeat("0123456789", 10));

        // ------------------------------------------------------------
        // 3. Smallest strings containing parentheses
        //    (empty pair, operator-only pair, pair at either end, siblings)
        // ------------------------------------------------------------
        String[] small = {
                "()", "(1)", "(+)", "(-)", "(*)", "(/)", "(12)",
                "1()", "()1", "1()2", "()()", "(1)(2)(3)", "(())", "((1))",
                "(()())", "4-(5)-6"
        };
        for (String s : small) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 4. Flat sequences (depth 1 however long the string is)
        // ------------------------------------------------------------
        testCase = generateCase(testCase, repeat("()", 50));
        testCase = generateCase(testCase, repeat("(1)", 33));
        testCase = generateCase(testCase, "(1)+(2)-(3)*(4)/(5)");
        testCase = generateCase(testCase, "5" + repeat("()", 49) + "5");

        // ------------------------------------------------------------
        // 5. Pure nesting chains (small, medium, near-max and max depth)
        // ------------------------------------------------------------
        int[] chainDepths = {2, 3, 4, 5, 10, 25, 49, 50};
        for (int d : chainDepths) {
            testCase = generateCase(testCase, nested(d));
        }

        // ------------------------------------------------------------
        // 6. Chains with a core and with characters outside
        // ------------------------------------------------------------
        int[] coreDepths = {2, 3, 10, 25, 49};
        for (int d : coreDepths) {
            testCase = generateCase(testCase, nestedWith(d, String.valueOf(d % 10)));
        }
        testCase = generateCase(testCase, nestedWith(49, "1+"));
        testCase = generateCase(testCase, nestedWith(47, "1+2*3"));
        testCase = generateCase(testCase, nestedWith(2, "-"));
        testCase = generateCase(testCase, "1+" + nested(10) + "+2");
        testCase = generateCase(testCase, "-" + nested(49) + "+");

        // ------------------------------------------------------------
        // 7. Examples (3 original LeetCode + 3 new) and classic shapes
        // ------------------------------------------------------------
        String[] classic = {
                "(1+(2*3)+((8)/4))+1",
                "(1)+((2))+(((3)))",
                "()(())((()()))",
                "(7+(2*(4-1)))/3",
                "()(()())(())",
                "8*5-2+9",
                "(((1)+2)+3)+((4))",
                "((1+2)*(3-4))/5",
                "(-(-1))",
                "(1+(2+(3+(4+(5+(6+7))))))",
                "1+(2+(3+(4+(5))))"
        };
        for (String s : classic) {
            testCase = generateCase(testCase, s);
        }

        // ------------------------------------------------------------
        // 8. Where the deepest point sits: start / middle / end / ties
        // ------------------------------------------------------------
        testCase = generateCase(testCase, "((()))+(1)+(2)");
        testCase = generateCase(testCase, "(1)+(2)+((()))");
        testCase = generateCase(testCase, "(1)+(((2)))+(3)");
        testCase = generateCase(testCase, "((1))+((2))");
        testCase = generateCase(testCase, nested(40) + repeat("()", 10));
        testCase = generateCase(testCase, repeat("()", 10) + nested(40));
        testCase = generateCase(testCase, repeat("()", 5) + nested(40) + repeat("()", 5));

        // ------------------------------------------------------------
        // 9. Several peaks: different heights, equal heights, valley,
        //    ascending and descending staircases
        // ------------------------------------------------------------
        testCase = generateCase(testCase, nested(3) + nested(7) + nested(5));
        testCase = generateCase(testCase, nested(25) + nested(25));
        testCase = generateCase(testCase, nested(8) + "()" + nested(8));

        StringBuilder ascending = new StringBuilder();
        StringBuilder descending = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            ascending.append(nested(i));
            descending.append(nested(10 - i));
        }
        testCase = generateCase(testCase, ascending.toString());
        testCase = generateCase(testCase, descending.toString());

        // ------------------------------------------------------------
        // 10. Combs and branching shapes (depth goes down and up again)
        // ------------------------------------------------------------
        testCase = generateCase(testCase, comb(2));
        testCase = generateCase(testCase, comb(49));
        testCase = generateCase(testCase, branchy(5));
        testCase = generateCase(testCase, branchy(16));
        testCase = generateCase(testCase, "(" + nested(10) + nested(3) + ")");

        // ------------------------------------------------------------
        // 11. Maximum length (100) strings of different shapes
        // ------------------------------------------------------------
        testCase = generateCase(testCase, repeat("(())", 25));
        testCase = generateCase(testCase, repeat("((()))", 16) + "1+2*");
        testCase = generateCase(testCase, "1" + nested(49) + "2");
        testCase = generateCase(testCase, repeat("(1+)", 25));

        // ------------------------------------------------------------
        // 12. Random strings, any length 1..100
        // ------------------------------------------------------------
        for (int i = 0; i < 8; i++) {

            int len = 1 + random.nextInt(MAX_LEN);
            int pairs = random.nextInt(len / 2 + 1);

            String skeleton = pairs == 0 ? "" : randomDyck(pairs, 1 + random.nextInt(pairs));

            testCase = generateCase(testCase, decorate(skeleton, len));
        }

        // ------------------------------------------------------------
        // 13. Random strings with an exact, known depth
        // ------------------------------------------------------------
        int[] exactDepths = {2, 4, 6, 7, 8, 9, 12, 15, 20, 30, 40, 48};
        for (int d : exactDepths) {

            int extra = random.nextInt(50 - d + 1);

            String skeleton = randomExactDepth(d, extra);

            int target = skeleton.length() + random.nextInt(MAX_LEN - skeleton.length() + 1);

            testCase = generateCase(testCase, decorate(skeleton, target));
        }

        // ------------------------------------------------------------
        // 14. Random strings of length exactly 100
        // ------------------------------------------------------------
        for (int i = 0; i < 8; i++) {

            int pairs = 1 + random.nextInt(50);

            String skeleton = randomDyck(pairs, 1 + random.nextInt(pairs));

            testCase = generateCase(testCase, decorate(skeleton, MAX_LEN));
        }

        // ------------------------------------------------------------
        // 15. Mostly digits/operators with only a few parentheses
        // ------------------------------------------------------------
        for (int i = 0; i < 4; i++) {

            int pairs = 1 + random.nextInt(5);

            String skeleton = randomDyck(pairs, 1 + random.nextInt(pairs));

            testCase = generateCase(testCase, decorate(skeleton, 50 + random.nextInt(51)));
        }

        // ------------------------------------------------------------
        // 16. Mostly parentheses with very few other characters
        // ------------------------------------------------------------
        for (int i = 0; i < 4; i++) {

            int pairs = 46 + random.nextInt(5);

            String skeleton = randomDyck(pairs, 1 + random.nextInt(pairs));

            int target = Math.min(MAX_LEN, skeleton.length() + random.nextInt(9));

            testCase = generateCase(testCase, decorate(skeleton, target));
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // ----------------------------------------------------------------
    // Writes one test case after validating it and cross-checking the
    // answer with an independent brute-force method.
    // ----------------------------------------------------------------
    static int generateCase(int id, String input) throws Exception {

        if (!used.add(input)) {
            return id;
        }

        validate(input);

        int output = solve(input);

        if (output != bruteForce(input)) {
            throw new IllegalStateException("Solution mismatch for: " + input);
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

    // Optimal solution: single pass, O(1) extra space.
    static int solve(String s) {

        int current = 0;
        int maxDepth = 0;

        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            if (ch == '(') {
                current++;
                if (current > maxDepth) {
                    maxDepth = current;
                }
            } else if (ch == ')') {
                current--;
            }
        }

        return maxDepth;
    }

    // Independent check: delete all "()" pairs round by round.
    // The number of rounds needed equals the nesting depth.
    static int bruteForce(String s) {

        StringBuilder only = new StringBuilder();

        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == ')') {
                only.append(ch);
            }
        }

        String t = only.toString();
        int rounds = 0;

        while (!t.isEmpty()) {

            String next = t.replace("()", "");

            if (next.length() == t.length()) {
                throw new IllegalStateException("Not a valid parentheses string: " + s);
            }

            t = next;
            rounds++;
        }

        return rounds;
    }

    // Checks every constraint from the problem statement.
    static void validate(String s) {

        if (s.length() < 1 || s.length() > MAX_LEN) {
            throw new IllegalStateException("Bad length " + s.length() + ": " + s);
        }

        int balance = 0;

        for (char ch : s.toCharArray()) {

            if (ch == '(') {
                balance++;
            } else if (ch == ')') {
                balance--;
                if (balance < 0) {
                    throw new IllegalStateException("Unbalanced: " + s);
                }
            } else if (FILLER.indexOf(ch) < 0) {
                throw new IllegalStateException("Illegal character '" + ch + "': " + s);
            }
        }

        if (balance != 0) {
            throw new IllegalStateException("Unbalanced: " + s);
        }
    }

    // "((...))" with the given number of pairs.
    static String nested(int depth) {
        return repeat("(", depth) + repeat(")", depth);
    }

    // "((...core...))" with the given number of pairs around core.
    static String nestedWith(int depth, String core) {
        return repeat("(", depth) + core + repeat(")", depth);
    }

    // "(" + "()" * k + ")"  -> depth 2, k inner pairs.
    static String comb(int k) {
        return "(" + repeat("()", k) + ")";
    }

    // "(()(()(()...)))" style chain with a leaf pair at every level.
    static String branchy(int levels) {
        return repeat("(()", levels) + repeat(")", levels);
    }

    // Random valid parentheses skeleton with exactly 'pairs' pairs
    // and depth never larger than maxDepth (maxDepth >= 1).
    static String randomDyck(int pairs, int maxDepth) {

        StringBuilder s = new StringBuilder();

        int opensLeft = pairs;
        int depth = 0;

        while (opensLeft > 0 || depth > 0) {

            boolean canOpen = opensLeft > 0 && depth < maxDepth;
            boolean canClose = depth > 0;

            if (canOpen && (!canClose || random.nextBoolean())) {
                s.append('(');
                opensLeft--;
                depth++;
            } else {
                s.append(')');
                depth--;
            }
        }

        return s.toString();
    }

    // Skeleton whose depth is exactly 'depth': one full chain plus
    // 'extraPairs' further pairs that never go deeper than the chain.
    static String randomExactDepth(int depth, int extraPairs) {

        List<String> parts = new ArrayList<>();
        parts.add(nested(depth));

        int left = extraPairs;

        while (left > 0) {

            int p = 1 + random.nextInt(left);
            int md = 1 + random.nextInt(depth);

            parts.add(randomDyck(p, md));
            left -= p;
        }

        Collections.shuffle(parts, random);

        StringBuilder s = new StringBuilder();

        for (String part : parts) {
            s.append(part);
        }

        return s.toString();
    }

    // Inserts random digits/operators at random positions until the
    // string reaches targetLen. Such insertions never break validity.
    static String decorate(String skeleton, int targetLen) {

        StringBuilder s = new StringBuilder(skeleton);

        while (s.length() < targetLen) {

            int pos = random.nextInt(s.length() + 1);

            s.insert(pos, FILLER.charAt(random.nextInt(FILLER.length())));
        }

        return s.toString();
    }

    static String repeat(String str, int count) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(str);
        }

        return result.toString();
    }
}
