import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static int id = 0;

    // Signatures already written. Two grids share a signature when they have the
    // same paths and the same start/end blocking, so they test the same behaviour.
    static Set<String> seen = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // The two examples and the "Now Your Turn" grid
        add(parse(new String[]{"1000", "1101", "1100", "0111"}));
        add(parse(new String[]{"10", "10"}));
        add(parse(new String[]{"100", "110", "011"}));

        // Every grid for n = 2 and n = 3, keeping one per distinct behaviour
        for (int n = 2; n <= 3; n++) {
            int cells = n * n;
            for (int mask = 0; mask < (1 << cells); mask++) {
                int[][] g = new int[n][n];
                for (int i = 0; i < cells; i++) {
                    g[i / n][i % n] = (mask >> i) & 1;
                }
                add(g);
            }
        }

        // Fully open grids and hand-made shapes for n = 2..5
        for (int n = 2; n <= 5; n++) {
            int[][] open = new int[n][n];
            for (int[] row : open) Arrays.fill(row, 1);
            add(open);
        }
        add(parse(new String[]{"11111", "00001", "11111", "10000", "11111"}));
        add(parse(new String[]{"11111", "11110", "11111", "01111", "11111"}));
        add(parse(new String[]{"10000", "11111", "00001", "11111", "10000"}));

        // Random grids for n = 4 and n = 5, stopping once a few new behaviours are found
        Random rnd = new Random(187);
        for (int n = 4; n <= 5; n++) {
            int accepted = 0;
            for (int t = 0; t < 2000 && accepted < 12; t++) {
                int density = 50 + rnd.nextInt(45);
                int[][] g = new int[n][n];
                for (int r = 0; r < n; r++)
                    for (int c = 0; c < n; c++)
                        g[r][c] = rnd.nextInt(100) < density ? 1 : 0;
                if (add(g)) accepted++;
            }
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static int[][] parse(String[] rows) {
        int n = rows.length;
        int[][] g = new int[n][n];
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                g[r][c] = rows[r].charAt(c) - '0';
        return g;
    }

    // Independent reference: depth-first search over all simple paths, trying
    // directions in a different order from the solution, then sorted.
    static List<String> reference(int[][] g) {
        int n = g.length;
        List<String> paths = new ArrayList<>();
        if (g[0][0] == 0 || g[n - 1][n - 1] == 0) {
            return paths;
        }
        int[] dr = {-1, 0, 0, 1};
        int[] dc = {0, 1, -1, 0};
        char[] name = {'U', 'R', 'L', 'D'};
        boolean[][] used = new boolean[n][n];
        search(g, used, 0, 0, new StringBuilder(), paths, dr, dc, name);
        Collections.sort(paths);
        return paths;
    }

    static void search(int[][] g, boolean[][] used, int r, int c, StringBuilder path,
                       List<String> paths, int[] dr, int[] dc, char[] name) {
        int n = g.length;
        if (r == n - 1 && c == n - 1) {
            paths.add(path.toString());
            return;
        }
        used[r][c] = true;
        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k], nc = c + dc[k];
            if (nr < 0 || nc < 0 || nr >= n || nc >= n) continue;
            if (used[nr][nc] || g[nr][nc] == 0) continue;
            path.append(name[k]);
            search(g, used, nr, nc, path, paths, dr, dc, name);
            path.setLength(path.length() - 1);
        }
        used[r][c] = false;
    }

    // Writes the grid as a test unless an equivalent one was already written.
    static boolean add(int[][] g) throws Exception {

        int n = g.length;
        if (n < 2 || n > 5) throw new IllegalStateException("n out of range");
        for (int[] row : g) {
            if (row.length != n) throw new IllegalStateException("grid not square");
            for (int x : row) {
                if (x != 0 && x != 1) throw new IllegalStateException("cell out of range");
            }
        }

        List<String> paths = reference(g);
        String signature = n + "|" + g[0][0] + g[n - 1][n - 1] + "|" + String.join(",", paths);
        if (!seen.add(signature)) {
            return false;
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(n + "\r\n");
            for (int[] row : g) {
                StringBuilder sb = new StringBuilder();
                for (int x : row) sb.append((char) ('0' + x));
                in.print(sb + "\r\n");
            }
            for (String p : paths) {
                out.print(p + "\r\n");
            }
        }

        id++;
        return true;
    }
}
