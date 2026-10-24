import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1657);

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // Examples from the statement
        testCase = generateCase(testCase, "aba");
        testCase = generateCase(testCase, "aabb");
        testCase = generateCase(testCase, "abca");

        // Minimum length: every letter from a to j
        for (char c = (char) 97; c <= (char) 106; c++) {
            testCase = generateCase(testCase, String.valueOf(c));
        }

        // Length 2 and 3 handpicked
        String[] small = {
            "aa", "ab", "ja", "jj", "aj", "ba", "ij", "ee",
            "aaa", "aab", "aba", "baa", "abc", "jij", "jaj", "abb",
            "bab", "cba", "ccc", "jja", "ajj", "iji", "fgh", "hgf"
        };
        for (String s : small) {
            testCase = generateCase(testCase, s);
        }

        // Alphabet boundary and all distinct letters
        testCase = generateCase(testCase, "abcdefghij");
        testCase = generateCase(testCase, "jihgfedcba");
        testCase = generateCase(testCase, "abcdefghijabcdefghij");
        testCase = generateCase(testCase, "abcdefghijjihgfedcba");
        testCase = generateCase(testCase, "ajaj");
        testCase = generateCase(testCase, "abcabc");
        testCase = generateCase(testCase, "abcabcabc");
        testCase = generateCase(testCase, "aabbcc");
        testCase = generateCase(testCase, "abcddcba");
        testCase = generateCase(testCase, "abcdedcba");
        testCase = generateCase(testCase, "ababababab");
        testCase = generateCase(testCase, "abababababa");
        testCase = generateCase(testCase, "aabbaabbaabb");
        testCase = generateCase(testCase, "abcdefghijj");
        testCase = generateCase(testCase, "jabcdefghi");

        // Same character repeated (all substrings valid), various lengths
        int[] sameLens = {2, 3, 4, 5, 10, 11, 50, 99, 100, 1000};
        for (int len : sameLens) {
            testCase = generateCase(testCase, repeat("a", len));
        }
        testCase = generateCase(testCase, repeat("j", 7));
        testCase = generateCase(testCase, repeat("e", 1001));

        // Repeating patterns
        testCase = generateCase(testCase, repeat("ab", 5));
        testCase = generateCase(testCase, repeat("ab", 50));
        testCase = generateCase(testCase, repeat("ab", 500));
        testCase = generateCase(testCase, repeat("aab", 100));
        testCase = generateCase(testCase, repeat("aabb", 100));
        testCase = generateCase(testCase, repeat("ja", 250));
        testCase = generateCase(testCase, repeat("abc", 100));
        testCase = generateCase(testCase, repeat("abcdefghij", 10));
        testCase = generateCase(testCase, repeat("abcdefghij", 100));
        testCase = generateCase(testCase, repeat("jihgfedcba", 100));
        testCase = generateCase(testCase, repeat("aabbccddeeffgghhiijj", 20));

        // Palindromes
        for (int len : new int[]{5, 10, 51, 200, 1001}) {
            testCase = generateCase(testCase, palindrome(len, 10));
        }

        // Random strings, short, alphabet size 1..10
        for (int alpha = 1; alpha <= 10; alpha++) {
            testCase = generateCase(testCase, randomString(20, alpha));
        }

        // Random strings, medium, alphabet size 1..10
        for (int alpha = 1; alpha <= 10; alpha++) {
            testCase = generateCase(testCase, randomString(300, alpha));
        }

        // Random strings with random size
        for (int i = 0; i < 15; i++) {
            int len = 1 + random.nextInt(100);
            int alpha = 1 + random.nextInt(10);
            testCase = generateCase(testCase, randomString(len, alpha));
        }

        // Random strings, larger
        for (int i = 0; i < 10; i++) {
            int len = 1000 + random.nextInt(9000);
            int alpha = 1 + random.nextInt(10);
            testCase = generateCase(testCase, randomString(len, alpha));
        }

        // Blocks of repeated letters
        for (int i = 0; i < 5; i++) {
            testCase = generateCase(testCase, blocks(2000 + random.nextInt(3000), 10));
        }

        // Maximum length cases (n = 100000); answers exceed int range
        testCase = generateCase(testCase, repeat("a", 100000));
        testCase = generateCase(testCase, repeat("j", 100000));
        testCase = generateCase(testCase, repeat("ab", 50000));
        testCase = generateCase(testCase, repeat("abcdefghij", 10000));
        testCase = generateCase(testCase, repeat("aabb", 25000));
        testCase = generateCase(testCase, palindrome(100000, 10));
        testCase = generateCase(testCase, randomString(100000, 10));
        testCase = generateCase(testCase, randomString(100000, 2));
        testCase = generateCase(testCase, randomString(100000, 1));
        testCase = generateCase(testCase, randomString(100000, 5));
        testCase = generateCase(testCase, blocks(100000, 10));
        testCase = generateCase(testCase, repeat("a", 99999) + "b");
        testCase = generateCase(testCase, "b" + repeat("a", 99999));
        testCase = generateCase(testCase, repeat("a", 50000) + repeat("b", 50000));
        testCase = generateCase(testCase, repeat("abc", 33333) + "a");

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, String input) throws Exception {

        long answer = solve(input);

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        try (Writer in = new BufferedWriter(new FileWriter(inputFile));
             Writer out = new BufferedWriter(new FileWriter(outputFile))) {

            in.write(input + "\r\n");
            out.write(answer + "\r\n");
        }

        return id + 1;
    }

    static long solve(String word) {

        long[] seen = new long[1 << 10];
        seen[0] = 1;

        int mask = 0;
        long total = 0;

        for (int i = 0; i < word.length(); i++) {

            mask ^= 1 << (word.charAt(i) - (char) 97);

            total += seen[mask];

            for (int bit = 0; bit < 10; bit++) {
                total += seen[mask ^ (1 << bit)];
            }

            seen[mask]++;
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

    static String randomString(int length, int alphabet) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length; i++) {
            s.append((char) (97 + random.nextInt(alphabet)));
        }

        return s.toString();
    }

    static String palindrome(int length, int alphabet) {

        char[] arr = new char[length];

        for (int i = 0; i < (length + 1) / 2; i++) {
            char c = (char) (97 + random.nextInt(alphabet));
            arr[i] = c;
            arr[length - 1 - i] = c;
        }

        return new String(arr);
    }

    static String blocks(int length, int alphabet) {

        StringBuilder s = new StringBuilder();

        while (s.length() < length) {

            char c = (char) (97 + random.nextInt(alphabet));
            int run = 1 + random.nextInt(50);

            for (int i = 0; i < run && s.length() < length; i++) {
                s.append(c);
            }
        }

        return s.toString();
    }
}
