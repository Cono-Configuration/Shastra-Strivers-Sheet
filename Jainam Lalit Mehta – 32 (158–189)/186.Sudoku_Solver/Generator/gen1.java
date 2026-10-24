import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static int id = 0;

    // Filled by count(): the most recent complete grid reached during the search.
    static int[][] lastSolved;

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // The two examples from the statement
        int[][] ex1 = parse(new String[]{
            "53..7....", "6..195...", ".98....6.", "8...6...3", "4..8.3..1",
            "7...2...6", ".6....28.", "...419..5", "....8..79"});
        int[][] ex2 = parse(new String[]{
            "......7..", "7.5...9..", "...975431", "9...41..7", ".5.8.764.",
            ".7..2....", ".4.....69", "16.43....", "....623.4"});
        writeCase(ex1);
        writeCase(ex2);

        // Random puzzles: build a full valid grid, then remove cells while the
        // solution stays unique. Stop after a random number of removals.
        Random rnd = new Random(185);
        for (int t = 0; t < 118; t++) {
            int[][] full = new int[9][9];
            fill(full, rnd);

            int[][] puzzle = copy(full);
            int target = 25 + rnd.nextInt(45);
            List<Integer> cells = new ArrayList<>();
            for (int i = 0; i < 81; i++) cells.add(i);
            Collections.shuffle(cells, rnd);

            int removed = 0;
            for (int idx : cells) {
                if (removed >= target) break;
                int r = idx / 9, c = idx % 9;
                int saved = puzzle[r][c];
                puzzle[r][c] = 0;
                if (count(copy(puzzle), 2) == 1) {
                    removed++;
                } else {
                    puzzle[r][c] = saved;
                }
            }
            writeCase(puzzle);
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static void writeCase(int[][] puzzle) throws Exception {

        if (count(copy(puzzle), 2) != 1) {
            throw new IllegalStateException("puzzle does not have exactly one solution");
        }
        int[][] solution = lastSolved;

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            for (int r = 0; r < 9; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < 9; c++) {
                    sb.append(puzzle[r][c] == 0 ? '.' : (char) ('0' + puzzle[r][c]));
                }
                in.print(sb + "\r\n");
            }
            for (int r = 0; r < 9; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < 9; c++) {
                    sb.append((char) ('0' + solution[r][c]));
                }
                out.print(sb + "\r\n");
            }
        }

        id++;
    }

    // Counts solutions up to `limit`. Sets lastSolved when a complete grid is reached.
    static int count(int[][] b, int limit) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (b[r][c] != 0) continue;
                int total = 0;
                for (int d = 1; d <= 9; d++) {
                    if (valid(b, r, c, d)) {
                        b[r][c] = d;
                        total += count(b, limit);
                        b[r][c] = 0;
                        if (total >= limit) return total;
                    }
                }
                return total;
            }
        }
        lastSolved = copy(b);
        return 1;
    }

    // Fills an empty grid with a random valid complete solution.
    static boolean fill(int[][] b, Random rnd) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (b[r][c] != 0) continue;
                List<Integer> digits = new ArrayList<>();
                for (int d = 1; d <= 9; d++) digits.add(d);
                Collections.shuffle(digits, rnd);
                for (int d : digits) {
                    if (valid(b, r, c, d)) {
                        b[r][c] = d;
                        if (fill(b, rnd)) return true;
                        b[r][c] = 0;
                    }
                }
                return false;
            }
        }
        return true;
    }

    static boolean valid(int[][] b, int r, int c, int d) {
        for (int i = 0; i < 9; i++) {
            if (b[r][i] == d || b[i][c] == d) return false;
        }
        int br = r / 3 * 3, bc = c / 3 * 3;
        for (int i = br; i < br + 3; i++)
            for (int j = bc; j < bc + 3; j++)
                if (b[i][j] == d) return false;
        return true;
    }

    static int[][] parse(String[] rows) {
        int[][] b = new int[9][9];
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++) {
                char ch = rows[r].charAt(c);
                b[r][c] = ch == '.' ? 0 : ch - '0';
            }
        return b;
    }

    static int[][] copy(int[][] b) {
        int[][] n = new int[9][];
        for (int r = 0; r < 9; r++) n[r] = b[r].clone();
        return n;
    }
}
