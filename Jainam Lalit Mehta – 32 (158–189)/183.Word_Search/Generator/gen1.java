import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    static final Random random = new Random(1822);

    static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String LETTERS = LOWER + UPPER;

    static final Set<String> used = new HashSet<>();

    static final int[] DR = {1, -1, 0, 0};
    static final int[] DC = {0, 0, 1, -1};

    static int testCase = 0;

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // ---------- 1. Problem examples ----------
        String[] sample = {"ABCE", "SFCS", "ADEE"};

        add("ABCCED", sample);
        add("SEE", sample);
        add("ABCB", sample);

        // more words on the sample grid
        String[] sampleWords = {
            "A", "Z", "S", "E", "ABCESEEEFS", "ABFSA", "ADEE", "CCED", "ABCEESEE", "SEEE",
            "SFCS", "ASADFB", "FSA", "ASA", "ESEEC", "ABCCEDAS", "ABCCEDASF", "FCCEE", "FCES",
            "ECCBAS", "ECCBASDE", "SEEDACB", "SEEDAS", "AB", "BA", "ABFD", "ABFDA", "a", "abcced"
        };
        for (String w : sampleWords) {
            add(w, sample);
        }

        // ---------- 2. Single cell ----------
        add("a", "a");
        add("b", "a");
        add("A", "a");
        add("a", "A");
        add("Z", "Z");
        add("aa", "a");
        add("ab", "a");

        // ---------- 3. Single row ----------
        add("abcde", "abcde");
        add("edcba", "abcde");
        add("ace", "abcde");
        add("cd", "abcde");
        add("abcdef", "abcde");
        add("bcb", "abcde");
        add("e", "abcde");
        add("ea", "abcde");
        add("aba", "aba");
        add("abab", "aba");
        add("aaa", "aaa");
        add("aaaa", "aaa");
        add("AbCdEf", "AbCdEf");
        add("fEdCbA", "AbCdEf");
        add("abcdef", "AbCdEf");

        // ---------- 4. Single column ----------
        add("abcd", "a", "b", "c", "d");
        add("dcba", "a", "b", "c", "d");
        add("bc", "a", "b", "c", "d");
        add("bd", "a", "b", "c", "d");
        add("abca", "a", "b", "c", "d");
        add("cdc", "a", "b", "c", "d");
        add("aaaaa", "a", "a", "a", "a", "a");
        add("aaaaaa", "a", "a", "a", "a", "a");

        // ---------- 5. 2 x 2 ----------
        add("ABDC", "AB", "CD");
        add("ACDB", "AB", "CD");
        add("ADBC", "AB", "CD");
        add("ABCD", "AB", "CD");
        add("ABA", "AB", "CD");
        add("DCAB", "AB", "CD");
        add("ABDCA", "AB", "CD");
        add("aaaa", "aa", "aa");
        add("aaaaa", "aa", "aa");
        add("abba", "ab", "ba");
        add("abab", "ab", "ba");

        // ---------- 6. Case sensitivity ----------
        add("aA", "aA", "Aa");
        add("AA", "aA", "Aa");
        add("aa", "aA", "Aa");
        add("aAaA", "aA", "Aa");
        add("aAAa", "aA", "Aa");
        add("Hello", "hello");
        add("hello", "hello");
        add("HELLO", "hElLo", "lLeHo");
        add("hElLo", "hElLo", "lLeHo");

        // ---------- 7. A cell cannot be reused ----------
        add("ABCB", "ABC");
        add("ABA", "AB");
        add("AAA", "AA");
        add("AAAA", "AA", "AA");
        add("AAAAA", "AA", "AA");
        add("ABAB", "AB", "BA");
        add("abcba", "abc", "xyz");
        add("abcbc", "abc", "xbc");

        // ---------- 8. Word longer than total cells (always false) ----------
        add("abcdefghijklmno", "abc", "def");
        add("aaaaaaaaaaaaaaa", "aaa", "aaa");
        add("aaaaaaaaaa", "aaa", "aaa");

        // ---------- 9. Grids of one repeated letter ----------
        String[] all6 = repeatRows("A", 6, 6);
        add("AAAAAAAAAAAAAAA", all6);
        add("AAAAAAAAAAAAAAB", all6);
        add("AAAAAAACAAAAAAA", all6);
        add("A", all6);
        add("a", all6);
        String[] all3 = repeatRows("A", 3, 3);
        add("AAAAAAAAA", all3);
        add("AAAAAAAAAA", all3);
        add("AAAAAAAAB", all3);
        String[] all2x5 = repeatRows("z", 2, 5);
        add("zzzzzzzzzz", all2x5);
        add("zzzzzzzzzzz", all2x5);
        add("zzzzzzzzzy", all2x5);

        String[] oneInAll = repeatRows("A", 6, 6);
        oneInAll[0] = "BAAAAA";
        add("AAAAAAAAAAAAAAB", oneInAll);
        add("BAAAAAAAAAAAAAA", oneInAll);
        add("AAAAAAABAAAAAAA", oneInAll);
        oneInAll[3] = "AABAAA";
        add("AAAAAAAAAAAAAAB", oneInAll);
        add("AABAAAAAAAAAAAB", oneInAll);

        // ---------- 10. Spiral and snake paths on 6 x 6 ----------
        String[] spiral = spiralGrid(6, 6);
        String spiralPath = spiralWord(6, 6, 15);
        add(spiralPath, spiral);
        add(spiralPath.substring(0, 14), spiral);
        add(new StringBuilder(spiralPath).reverse().toString(), spiral);
        add(spiralPath.substring(0, 14) + "!".replace('!', 'Q'), spiral);
        add(spiralPath.substring(3, 15), spiral);
        add(spiralPath.substring(0, 7) + spiralPath.charAt(5) + spiralPath.charAt(4), spiral);

        String[] snake = snakeGrid(6, 6);
        String snakePath = snakeWord(6, 6, 15);
        add(snakePath, snake);
        add(snakePath.substring(0, 13), snake);
        add(new StringBuilder(snakePath).reverse().toString(), snake);
        add(snakePath.substring(0, 14) + "Q", snake);

        // ---------- 11. Random cases ----------
        String[] alphabets = {"AB", "ABC", "abc", "aAbB", "abcd", "ABCDE", LOWER, UPPER, LETTERS, "xyz"};

        int target = 0;
        int guard = 0;
        while (testCase < 260 && guard < 100000) {

            guard++;

            int rows = 1 + random.nextInt(6);
            int cols = 1 + random.nextInt(6);
            String alphabet = alphabets[random.nextInt(alphabets.length)];

            String[] g = randomGrid(rows, cols, alphabet);
            String word = null;

            int kind = random.nextInt(6);

            if (kind <= 2) {
                // word taken from a real path (answer true)
                word = walkWord(g, 1 + random.nextInt(15));
            } else if (kind == 3) {
                // real path with one character changed
                String w = walkWord(g, 2 + random.nextInt(14));
                if (w.length() >= 2) {
                    int pos = random.nextInt(w.length());
                    char[] arr = w.toCharArray();
                    arr[pos] = alphabet.charAt(random.nextInt(alphabet.length()));
                    word = new String(arr);
                }
            } else if (kind == 4) {
                // real path followed by a step back onto an earlier cell
                String w = walkWord(g, 3 + random.nextInt(10));
                if (w.length() >= 3) {
                    word = w + w.charAt(random.nextInt(w.length() - 1));
                }
            } else {
                // completely random word
                word = randomString(1 + random.nextInt(15), alphabet);
            }

            if (word == null || word.length() < 1 || word.length() > 15) {
                continue;
            }

            add(word, g);
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    // ---------------- helpers ----------------

    static void add(String word, String... rows) throws Exception {

        String input = rows.length + " " + rows[0].length() + "\r\n";

        for (String row : rows) {
            input += row + "\r\n";
        }

        input += word + "\r\n";

        if (!used.add(input)) {
            return;
        }

        boolean answer = solve(rows, word);

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", testCase);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", testCase);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(input);
            out.print(answer + "\r\n");
        }

        testCase++;
    }

    static boolean solve(String[] rowsText, String word) {

        int rows = rowsText.length;
        int cols = rowsText[0].length();

        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = rowsText[i].toCharArray();
        }

        if (word.length() > rows * cols) {
            return false;
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (search(grid, word, r, c, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean search(char[][] grid, String word, int r, int c, int index) {

        if (index == word.length()) {
            return true;
        }

        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length) {
            return false;
        }

        if (grid[r][c] != word.charAt(index)) {
            return false;
        }

        char saved = grid[r][c];
        grid[r][c] = '#';

        boolean found = search(grid, word, r + 1, c, index + 1)
                || search(grid, word, r - 1, c, index + 1)
                || search(grid, word, r, c + 1, index + 1)
                || search(grid, word, r, c - 1, index + 1);

        grid[r][c] = saved;

        return found;
    }

    static String[] repeatRows(String ch, int rows, int cols) {

        String[] result = new String[rows];
        StringBuilder row = new StringBuilder();

        for (int i = 0; i < cols; i++) {
            row.append(ch);
        }

        for (int i = 0; i < rows; i++) {
            result[i] = row.toString();
        }

        return result;
    }

    static String[] randomGrid(int rows, int cols, String alphabet) {

        String[] result = new String[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = randomString(cols, alphabet);
        }

        return result;
    }

    static String randomString(int len, String alphabet) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < len; i++) {
            s.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return s.toString();
    }

    // random self-avoiding walk on the grid, returns the letters visited
    static String walkWord(String[] g, int maxLen) {

        int rows = g.length;
        int cols = g[0].length();

        boolean[][] seen = new boolean[rows][cols];

        int r = random.nextInt(rows);
        int c = random.nextInt(cols);

        StringBuilder word = new StringBuilder();

        while (word.length() < maxLen) {

            seen[r][c] = true;
            word.append(g[r].charAt(c));

            List<int[]> options = new ArrayList<>();

            for (int d = 0; d < 4; d++) {

                int nr = r + DR[d];
                int nc = c + DC[d];

                if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && !seen[nr][nc]) {
                    options.add(new int[]{nr, nc});
                }
            }

            if (options.isEmpty()) {
                break;
            }

            int[] next = options.get(random.nextInt(options.size()));
            r = next[0];
            c = next[1];
        }

        return word.toString();
    }

    // cells of a clockwise spiral
    static List<int[]> spiralCells(int rows, int cols) {

        List<int[]> cells = new ArrayList<>();

        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;

        while (top <= bottom && left <= right) {

            for (int c = left; c <= right; c++) cells.add(new int[]{top, c});
            top++;

            for (int r = top; r <= bottom; r++) cells.add(new int[]{r, right});
            right--;

            if (top <= bottom) {
                for (int c = right; c >= left; c--) cells.add(new int[]{bottom, c});
                bottom--;
            }

            if (left <= right) {
                for (int r = bottom; r >= top; r--) cells.add(new int[]{r, left});
                left++;
            }
        }

        return cells;
    }

    static String[] spiralGrid(int rows, int cols) {

        char[][] g = new char[rows][cols];
        List<int[]> cells = spiralCells(rows, cols);

        for (int i = 0; i < cells.size(); i++) {
            int[] cell = cells.get(i);
            g[cell[0]][cell[1]] = LETTERS.charAt(i % LETTERS.length());
        }

        String[] result = new String[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = new String(g[i]);
        }

        return result;
    }

    static String spiralWord(int rows, int cols, int len) {

        StringBuilder w = new StringBuilder();

        for (int i = 0; i < len; i++) {
            w.append(LETTERS.charAt(i % LETTERS.length()));
        }

        return w.toString();
    }

    static List<int[]> snakeCells(int rows, int cols) {

        List<int[]> cells = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            if (r % 2 == 0) {
                for (int c = 0; c < cols; c++) cells.add(new int[]{r, c});
            } else {
                for (int c = cols - 1; c >= 0; c--) cells.add(new int[]{r, c});
            }
        }

        return cells;
    }

    static String[] snakeGrid(int rows, int cols) {

        char[][] g = new char[rows][cols];
        List<int[]> cells = snakeCells(rows, cols);

        for (int i = 0; i < cells.size(); i++) {
            int[] cell = cells.get(i);
            g[cell[0]][cell[1]] = LETTERS.charAt(i % LETTERS.length());
        }

        String[] result = new String[rows];

        for (int i = 0; i < rows; i++) {
            result[i] = new String(g[i]);
        }

        return result;
    }

    static String snakeWord(int rows, int cols, int len) {
        return spiralWord(rows, cols, len);
    }
}
