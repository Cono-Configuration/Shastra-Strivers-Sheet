import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // Input presses are strings of length 1..4 over keys 2..9.
        // Every string of length 1 and 2 is included (8 + 64 = 72 cases).
        Set<String> presses = new LinkedHashSet<>();
        for (char a = '2'; a <= '9'; a++) {
            presses.add(String.valueOf(a));
            for (char b = '2'; b <= '9'; b++) {
                presses.add("" + a + b);
            }
        }

        // Length 3 and 4: keys with 3 letters and 4 letters, repeated keys,
        // the longest possible input (4 keys of 4 letters = 256 words), and random cases.
        String[] handPicked = {
            "222", "333", "444", "555", "666", "777", "888", "999",
            "234", "297", "979", "789", "456", "678", "927", "345",
            "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999",
            "7979", "9797", "2345", "6789", "7272", "9292", "7777", "2789"
        };
        for (String s : handPicked) {
            presses.add(s);
        }

        Random rnd = new Random(180);
        for (int t = 0; t < 40; t++) {
            presses.add(randomPresses(rnd, 3));
        }
        for (int t = 0; t < 40; t++) {
            presses.add(randomPresses(rnd, 4));
        }

        int id = 0;
        for (String s : presses) {
            id = writeCase(id, s);
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static String randomPresses(Random rnd, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('2' + rnd.nextInt(8)));
        }
        return sb.toString();
    }

    static int writeCase(int id, String presses) throws Exception {

        if (presses.length() < 1 || presses.length() > 4) {
            throw new IllegalStateException("length out of range");
        }
        for (char c : presses.toCharArray()) {
            if (c < '2' || c > '9') {
                throw new IllegalStateException("digit out of range");
            }
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        // Independent reference: build the product of letter sets one key at a time.
        List<String> words = new ArrayList<>();
        words.add("");
        for (char c : presses.toCharArray()) {
            List<String> next = new ArrayList<>();
            for (String w : words) {
                for (char letter : KEYS[c - '0'].toCharArray()) {
                    next.add(w + letter);
                }
            }
            words = next;
        }
        Collections.sort(words);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(presses + "\r\n");
            for (String w : words) {
                out.print(w + "\r\n");
            }
        }

        return id + 1;
    }
}
