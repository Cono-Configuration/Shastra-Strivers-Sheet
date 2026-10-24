import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1066);

    static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String DIGITS = "0123456789";
    static final String ALNUM = LOWER + UPPER + DIGITS;

    static final Set<String> used = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // ---------- 1. Hand-written edge cases ----------
        String[] fixed = {
            // single characters (lower, upper, digit)
            "a", "Z", "7", "0", "z", "A",
            // two characters
            "aa", "ab", "AA", "Aa", "aA", "11", "12", "ba", "99", "a1", "1a",
            // three characters
            "aaa", "aba", "abc", "aab", "baa", "abb", "bba", "121", "123", "A1A", "a1A",
            // problem examples
            "babad", "cbbd",
            // classic cases
            "abcd", "abcba", "abccba", "racecar", "forgeeksskeegfor", "bananas", "noon",
            "level", "abacdfgdcaba", "abacdedcaba", "aacabdkacaa", "ac", "bb", "tattarrattat",
            "cabbad", "dabcbae", "xabba", "abbax", "xabbay", "xabcbay",
            // digit palindromes
            "12321", "1221", "0000", "010", "1001001", "9876556789", "100200100",
            "0123456789", "1000000001", "2002", "12344321", "909", "9009", "00100",
            // case sensitivity
            "AbBa", "aBBA", "RaceCar", "Madam", "Noon", "NoOn", "aAaAaA", "AaaA", "aAAa",
            "Abba", "ABBA", "aBcCBa", "aBcCbA",
            // ties: leftmost wins
            "abcdefg", "abcdd", "aabbcc", "abaxyzzyxf", "abcbaxyzyx", "xyzyxabcba",
            "abcdefgg", "ggabcdef", "abbacddc", "abacdc", "cdcaba", "aabcc", "abcdeedcbaXYZZYX",
            // palindrome at start / middle / end
            "abacxyz", "xyzabacq", "xyzaba", "pqrstuvwxyz", "mnoppoqrs", "abcdeffedxyz",
            // every distinct char
            ALNUM, ALNUM.substring(0, 5), ALNUM.substring(0, 26), ALNUM.substring(0, 52),
            // digit/letter mixes
            "a1b2b1a", "a1b22b1a", "1a2a1", "1a22a1", "Z9Z", "9Z9", "z9Zz9", "0a0b0",
            // inner palindrome, mismatching ends
            "xabcdcbay", "xabcdcbaz", "1abccba2", "Q121W", "kkabcbakk1",
            // repeated blocks
            "abcabcabc", "abcabcabcabc", "abababab", "ababababa", "aabbaabb", "aabbaabbaa",
            "abcdabcdabcd", "121212", "1212121", "0101010", "01010101"
        };

        for (String s : fixed) {
            testCase = generateCase(testCase, s);
        }

        // ---------- 2. All same character, various lengths ----------
        int[] sameLens = {4, 5, 10, 11, 50, 100, 101, 500, 999, 1000};
        for (int len : sameLens) {
            testCase = generateCase(testCase, repeat("a", len));
        }
        testCase = generateCase(testCase, repeat("Z", 1000));
        testCase = generateCase(testCase, repeat("7", 999));

        // ---------- 3. Alternating / periodic strings ----------
        testCase = generateCase(testCase, repeat("ab", 500));
        testCase = generateCase(testCase, repeat("ab", 499) + "a");
        testCase = generateCase(testCase, repeat("abc", 333));
        testCase = generateCase(testCase, repeat("abc", 333) + "a");
        testCase = generateCase(testCase, repeat("aA", 500));
        testCase = generateCase(testCase, repeat("01", 500));
        testCase = generateCase(testCase, repeat("12345", 200));
        testCase = generateCase(testCase, repeat("a", 300) + "bc" + repeat("a", 300));
        testCase = generateCase(testCase, repeat("a", 499) + "b" + repeat("a", 500));
        testCase = generateCase(testCase, repeat("a", 500) + "b" + repeat("a", 499));
        testCase = generateCase(testCase, repeat("a", 500) + repeat("b", 500));
        testCase = generateCase(testCase, repeat("b", 500) + repeat("a", 500));
        testCase = generateCase(testCase, repeat("a", 250) + repeat("b", 250) + repeat("c", 250) + repeat("d", 250));

        // ---------- 4. Whole string is a palindrome ----------
        int[] palLens = {6, 7, 10, 21, 50, 51, 100, 255, 500, 501, 998, 999, 1000};
        for (int len : palLens) {
            testCase = generateCase(testCase, randomPalindrome(len, ALNUM));
        }
        testCase = generateCase(testCase, randomPalindrome(1000, DIGITS));
        testCase = generateCase(testCase, randomPalindrome(999, "ab"));

        // ---------- 5. Palindrome hidden at start / middle / end ----------
        int[] hiddenLens = {5, 10, 30, 100, 300};
        for (int len : hiddenLens) {
            String pal = distinctPalindrome(len);
            String noiseA = distinctNoise(50);
            String noiseB = distinctNoise(80);
            testCase = generateCase(testCase, pal + noiseA);
            testCase = generateCase(testCase, noiseA + pal);
            testCase = generateCase(testCase, noiseA + pal + noiseB);
        }

        // ---------- 6. Two palindromes of equal length (leftmost must win) ----------
        for (int len = 3; len <= 9; len += 2) {
            String p1 = distinctPalindrome(len);
            String p2 = reverse(p1).toUpperCase();
            testCase = generateCase(testCase, p1 + "-".replace('-', '0') + p2);
        }
        testCase = generateCase(testCase, randomPalindrome(200, LOWER) + "0" + randomPalindrome(200, UPPER));
        testCase = generateCase(testCase, randomPalindrome(201, UPPER) + "0" + randomPalindrome(201, LOWER));
        testCase = generateCase(testCase, randomPalindrome(150, DIGITS) + "x" + randomPalindrome(150, DIGITS));

        // ---------- 7. Almost palindromes ----------
        String almost = randomPalindrome(1000, LOWER);
        testCase = generateCase(testCase, changeChar(almost, 500, '9'));
        testCase = generateCase(testCase, changeChar(almost, 0, '9'));
        testCase = generateCase(testCase, changeChar(almost, 999, '9'));
        testCase = generateCase(testCase, changeChar(almost, 250, '9'));
        String almostOdd = randomPalindrome(999, LOWER);
        testCase = generateCase(testCase, changeChar(almostOdd, 499, '9'));
        testCase = generateCase(testCase, changeChar(almostOdd, 3, '9'));

        // ---------- 8. Random binary strings (many palindromes, many ties) ----------
        int[] binLens = {8, 12, 16, 20, 30, 40, 60, 100, 200, 500, 1000};
        for (int len : binLens) {
            testCase = generateCase(testCase, randomString(len, "ab"));
        }
        for (int i = 0; i < 5; i++) {
            testCase = generateCase(testCase, randomString(1 + random.nextInt(1000), "01"));
        }

        // ---------- 9. Random alphanumeric, short ----------
        for (int i = 0; i < 20; i++) {
            testCase = generateCase(testCase, randomString(1 + random.nextInt(30), ALNUM));
        }

        // ---------- 10. Random with tiny alphabets, medium ----------
        for (int i = 0; i < 15; i++) {
            testCase = generateCase(testCase, randomString(30 + random.nextInt(270), "abc"));
        }

        // ---------- 11. Random alphanumeric, medium / long ----------
        for (int i = 0; i < 10; i++) {
            testCase = generateCase(testCase, randomString(30 + random.nextInt(270), ALNUM));
        }
        for (int i = 0; i < 10; i++) {
            testCase = generateCase(testCase, randomString(900 + random.nextInt(101), ALNUM));
        }
        testCase = generateCase(testCase, randomString(1000, ALNUM));
        testCase = generateCase(testCase, randomString(1000, DIGITS));
        testCase = generateCase(testCase, randomString(1000, "aAbB"));

        // ---------- 12. Random strings with a planted palindrome ----------
        for (int i = 0; i < 10; i++) {
            int palLen = 20 + random.nextInt(200);
            String pal = randomPalindrome(palLen, ALNUM);
            int leftLen = random.nextInt(400);
            int rightLen = random.nextInt(400);
            testCase = generateCase(testCase,
                    randomString(leftLen, ALNUM) + pal + randomString(rightLen, ALNUM));
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, String input) throws Exception {

        if (!used.add(input)) {
            return id;
        }

        String output = solve(input);

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", id);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(input + "\r\n");
            out.print(output + "\r\n");
        }

        return id + 1;
    }

    static String solve(String text) {

        int n = text.length();
        int bestStart = 0;
        int bestLength = 1;

        for (int center = 0; center < n; center++) {

            int oddLength = expand(text, center, center);
            if (oddLength > bestLength) {
                bestLength = oddLength;
                bestStart = center - (oddLength - 1) / 2;
            }

            int evenLength = expand(text, center, center + 1);
            if (evenLength > bestLength) {
                bestLength = evenLength;
                bestStart = center - (evenLength - 2) / 2;
            }
        }

        return text.substring(bestStart, bestStart + bestLength);
    }

    static int expand(String text, int left, int right) {

        while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
            left--;
            right++;
        }

        return right - left - 1;
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

    // Palindrome built from characters that rarely appear in the noise strings
    static String distinctPalindrome(int len) {
        return randomPalindrome(len, "ABCDEFGH");
    }

    static String distinctNoise(int len) {
        return randomString(len, "pqrstuvwxyz");
    }

    static String changeChar(String s, int index, char c) {

        char[] arr = s.toCharArray();

        arr[index] = (arr[index] == c) ? '8' : c;

        return new String(arr);
    }
}
