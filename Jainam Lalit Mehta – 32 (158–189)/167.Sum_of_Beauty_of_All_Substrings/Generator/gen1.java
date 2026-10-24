import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1781);
    static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // ---- Samples and tiny strings ----
        String[] fixed = {
            "aabcb", "aabcbaa", "abaacc",
            "a", "z", "aa", "ab", "ba", "zz",
            "abc", "aab", "abb", "aba", "bab", "aaa",
            "abab", "abcabc", "aaab", "baaa", "aaba", "abbbba",
            "abcd", "aabb", "aabbcc", "abcba", "racecar",
            "zzzzzyyyyyxxxxx", "xyzzyx", "abacabadabacaba",
            "mississippi", "banana", "abcdefghijklmnopqrstuvwxyz",
            "zyxwvutsrqponmlkjihgfedcba", "abcdefghijklmnopqrstuvwxyza",
            "aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz",
            "azazazaz", "aaaaaaaaaz", "zaaaaaaaaa", "aaaaazaaaaa"
        };
        for (String f : fixed) {
            testCase = generateCase(testCase, f);
        }

        // ---- All same character, various lengths (beauty always 0) ----
        int[] sameLens = {2, 3, 10, 26, 100, 499, 500};
        for (int len : sameLens) {
            testCase = generateCase(testCase, repeat("a", len));
        }
        testCase = generateCase(testCase, repeat("z", 500));

        // ---- Alternating two letters ----
        testCase = generateCase(testCase, alternating("ab", 500));
        testCase = generateCase(testCase, alternating("ab", 499));
        testCase = generateCase(testCase, alternating("az", 500));

        // ---- Cyclic alphabet (all 26 letters repeating) ----
        testCase = generateCase(testCase, alternating(ALPHA, 500));
        testCase = generateCase(testCase, alternating(ALPHA, 26));
        testCase = generateCase(testCase, alternating(ALPHA, 27));
        testCase = generateCase(testCase, alternating(new StringBuilder(ALPHA).reverse().toString(), 500));
        testCase = generateCase(testCase, alternating("abc", 500));

        // ---- One different character in a long run ----
        testCase = generateCase(testCase, repeat("a", 499) + "b");
        testCase = generateCase(testCase, "b" + repeat("a", 499));
        testCase = generateCase(testCase, repeat("a", 250) + "b" + repeat("a", 249));
        testCase = generateCase(testCase, repeat("a", 100) + "b" + repeat("a", 100) + "c" + repeat("a", 100));

        // ---- Sorted / block structured strings ----
        testCase = generateCase(testCase, repeat("a", 250) + repeat("b", 250));
        testCase = generateCase(testCase, repeat("a", 400) + repeat("b", 100));
        testCase = generateCase(testCase, repeat("a", 1) + repeat("b", 2) + repeat("c", 3) + repeat("d", 4)
                + repeat("e", 5) + repeat("f", 6) + repeat("g", 7) + repeat("h", 8));
        testCase = generateCase(testCase, sortedBlocks(500));
        testCase = generateCase(testCase, blocks(20, 25));
        testCase = generateCase(testCase, blocks(26, 19));

        // ---- Palindromes ----
        testCase = generateCase(testCase, palindrome(500, 3));
        testCase = generateCase(testCase, palindrome(499, 26));

        // ---- Random strings with different alphabet sizes, max length ----
        int[] alphabets = {1, 2, 3, 4, 5, 8, 13, 20, 26};
        for (int a : alphabets) {
            testCase = generateCase(testCase, randomString(500, a));
        }

        // ---- Random strings with different alphabet sizes, varied lengths ----
        for (int a : alphabets) {
            for (int i = 0; i < 3; i++) {
                int len = 1 + random.nextInt(500);
                testCase = generateCase(testCase, randomString(len, a));
            }
        }

        // ---- Random short strings (exhaustive-ish small coverage) ----
        for (int i = 0; i < 12; i++) {
            int len = 1 + random.nextInt(8);
            testCase = generateCase(testCase, randomString(len, 1 + random.nextInt(4)));
        }

        // ---- Skewed distributions ----
        for (int i = 0; i < 4; i++) {
            testCase = generateCase(testCase, skewed(500, 0.9));
        }
        for (int i = 0; i < 3; i++) {
            testCase = generateCase(testCase, skewed(200 + random.nextInt(300), 0.6));
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, String input) throws Exception {

        if (!used.add(input)) {
            return id;
        }

        String output = String.valueOf(solve(input));

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(input + "\r\n");
            out.print(output + "\r\n");
        }

        return id + 1;
    }

    static int solve(String text) {

        int n = text.length();
        int total = 0;

        for (int start = 0; start < n; start++) {

            int[] freq = new int[26];

            for (int end = start; end < n; end++) {

                freq[text.charAt(end) - 'a']++;

                int highest = 0;
                int lowest = Integer.MAX_VALUE;

                for (int count : freq) {
                    if (count > 0) {
                        highest = Math.max(highest, count);
                        lowest = Math.min(lowest, count);
                    }
                }

                total += highest - lowest;
            }
        }

        return total;
    }

    static String repeat(String str, int count) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(str);
        }

        return result.toString();
    }

    static String alternating(String pattern, int length) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length; i++) {
            s.append(pattern.charAt(i % pattern.length()));
        }

        return s.toString();
    }

    static String randomString(int length, int alphabetSize) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length; i++) {
            s.append(ALPHA.charAt(random.nextInt(alphabetSize)));
        }

        return s.toString();
    }

    static String sortedBlocks(int length) {

        char[] arr = randomString(length, 26).toCharArray();
        Arrays.sort(arr);

        return new String(arr);
    }

    static String blocks(int blockCount, int blockSize) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < blockCount; i++) {
            s.append(repeat(String.valueOf(ALPHA.charAt(i % 26)), blockSize));
        }

        return s.toString();
    }

    static String palindrome(int length, int alphabetSize) {

        char[] arr = new char[length];

        for (int i = 0; i <= (length - 1) / 2; i++) {
            char c = ALPHA.charAt(random.nextInt(alphabetSize));
            arr[i] = c;
            arr[length - 1 - i] = c;
        }

        return new String(arr);
    }

    static String skewed(int length, double heavyProbability) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < heavyProbability) {
                s.append('a');
            } else {
                s.append(ALPHA.charAt(random.nextInt(26)));
            }
        }

        return s.toString();
    }
}
