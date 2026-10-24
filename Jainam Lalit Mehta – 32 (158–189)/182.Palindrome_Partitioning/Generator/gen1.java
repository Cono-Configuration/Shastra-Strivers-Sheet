import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1811);

    static final String LOWER = "abcdefghijklmnopqrstuvwxyz";

    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // ---------- 1. Hand-written edge cases ----------
        String[] fixed = {
            // single / double / triple characters
            "a", "b", "z", "aa", "ab", "zz", "yz", "aaa", "aab", "aba", "abc", "abb", "baa",
            "bab", "cdc", "efe", "xyz", "zzy", "yzz",
            // four characters
            "aaaa", "aaab", "baaa", "abab", "baba", "abba", "abca", "abcd", "aabb", "bbaa",
            "abaa", "aaba", "abcc", "ccba", "abbc", "bccb",
            // classic palindromes
            "racecar", "level", "noon", "madam", "banana", "abacaba", "abcba", "aabaa",
            "abaaba", "xyzzyx", "aabbaa", "abbabba", "rotator", "deified", "civic", "radar",
            "refer", "kayak", "redivider", "tattarrattat",
            // no palindrome longer than one character
            "abcdef", "abcdefg", "abcdefghijklmnop", "zyxwvutsrqponmlk", "acegikmo",
            "qwertyuiopasdfgh",
            // repeated blocks
            "abcabc", "abcabcabc", "ababab", "abababab", "aabbaabb", "aabbaabbaabb",
            "abcdabcd", "abcabcabcabcabca",
            // single different char at ends / middle
            "aaaaaab", "baaaaaa", "aaabaaa", "aaaabaaaa", "baaaaaaaaaab", "abaaaaaaaaaaaaba",
            "aaaaaaaabaaaaaaa", "bbbbbbbbbbbbbbba", "abbbbbbbbbbbbbbb",
            // palindromic with distinct middle
            "abccba", "abcddcba", "abcdedcba", "abcdeffedcba", "abcdefggfedcba", "abcdefghgfedcba",
            "abcdefghhgfedcba",
            // length 16 mixed
            "aabbccddeeffgghh", "abababababababab", "abcbabcbabcbabcb", "aabaabaabaabaaba",
            "abbaabbaabbaabba", "abcdcbaabcdcbaab", "mississippimiss", "zzzzzzzzyyyyyyyy"
        };

        for (String s : fixed) {
            testCase = generateCase(testCase, s);
        }

        // ---------- 2. All same character, every length ----------
        for (int len = 1; len <= 16; len++) {
            testCase = generateCase(testCase, repeat("a", len));
        }
        testCase = generateCase(testCase, repeat("z", 16));
        testCase = generateCase(testCase, repeat("q", 12));

        // ---------- 3. Alternating / periodic strings ----------
        for (int len = 2; len <= 16; len += 2) {
            testCase = generateCase(testCase, repeat("ab", len / 2));
        }
        for (int len = 3; len <= 15; len += 2) {
            testCase = generateCase(testCase, repeat("ab", len / 2) + "a");
        }
        testCase = generateCase(testCase, repeat("abc", 5));
        testCase = generateCase(testCase, repeat("aab", 5));
        testCase = generateCase(testCase, repeat("abb", 5));

        // ---------- 4. Whole string is a palindrome ----------
        for (int len = 2; len <= 16; len++) {
            testCase = generateCase(testCase, randomPalindrome(len, "abc"));
        }
        for (int len = 5; len <= 16; len += 3) {
            testCase = generateCase(testCase, randomPalindrome(len, "ab"));
            testCase = generateCase(testCase, randomPalindrome(len, LOWER));
        }

        // ---------- 5. Random strings over tiny alphabets ----------
        for (int i = 0; i < 25; i++) {
            testCase = generateCase(testCase, randomString(1 + random.nextInt(16), "ab"));
        }
        for (int i = 0; i < 20; i++) {
            testCase = generateCase(testCase, randomString(1 + random.nextInt(16), "abc"));
        }
        for (int i = 0; i < 10; i++) {
            testCase = generateCase(testCase, randomString(10 + random.nextInt(7), "abcd"));
        }

        // ---------- 6. Random strings over the full alphabet ----------
        for (int i = 0; i < 15; i++) {
            testCase = generateCase(testCase, randomString(1 + random.nextInt(16), LOWER));
        }
        for (int i = 0; i < 5; i++) {
            testCase = generateCase(testCase, randomString(16, LOWER));
        }

        // ---------- 7. Palindrome planted in random noise ----------
        for (int i = 0; i < 10; i++) {
            int palLen = 3 + random.nextInt(6);
            String pal = randomPalindrome(palLen, "ab");
            int left = random.nextInt(16 - palLen + 1);
            int right = 16 - palLen - left;
            testCase = generateCase(testCase,
                    randomString(left, "cdefg") + pal + randomString(right, "cdefg"));
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, String input) throws Exception {

        if (!used.add(input)) {
            return id;
        }

        String output = format(solve(input));

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", id);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(input + "\r\n");
            out.print(output + "\r\n");
        }

        return id + 1;
    }

    static List<List<String>> solve(String text) {

        int n = text.length();
        boolean[][] isPal = new boolean[n][n];

        for (int right = 0; right < n; right++) {
            for (int left = 0; left <= right; left++) {
                if (text.charAt(left) == text.charAt(right)
                        && (right - left <= 2 || isPal[left + 1][right - 1])) {
                    isPal[left][right] = true;
                }
            }
        }

        List<List<String>> answer = new ArrayList<>();
        explore(text, 0, isPal, new ArrayList<>(), answer);

        return answer;
    }

    static void explore(String text, int start, boolean[][] isPal,
                        List<String> current, List<List<String>> answer) {

        if (start == text.length()) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int end = start; end < text.length(); end++) {

            if (isPal[start][end]) {
                current.add(text.substring(start, end + 1));
                explore(text, end + 1, isPal, current, answer);
                current.remove(current.size() - 1);
            }
        }
    }

    // Output format: [["a","a","b"],["aa","b"]]
    static String format(List<List<String>> result) {

        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (int i = 0; i < result.size(); i++) {

            if (i > 0) {
                sb.append(',');
            }

            sb.append('[');

            List<String> part = result.get(i);

            for (int j = 0; j < part.size(); j++) {

                if (j > 0) {
                    sb.append(',');
                }

                sb.append('"').append(part.get(j)).append('"');
            }

            sb.append(']');
        }

        sb.append(']');

        return sb.toString();
    }

    static String repeat(String str, int count) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(str);
        }

        return result.toString();
    }

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static String randomString(int len, String alphabet) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < len; i++) {
            s.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return s.toString();
    }

    static String randomPalindrome(int len, String alphabet) {

        String half = randomString(len / 2, alphabet);
        String middle = (len % 2 == 1) ? randomString(1, alphabet) : "";

        return half + middle + reverse(half);
    }
}
