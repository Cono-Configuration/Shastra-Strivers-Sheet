import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static int id = 0;

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // Both examples from the statement
        write(4, 3, edges(new int[][]{{0,1},{1,2},{2,3},{3,0},{0,2}}));
        write(3, 2, edges(new int[][]{{0,1},{1,2},{0,2}}));

        // Complete graphs K_n: needs exactly n colours, so M = n-1 is false, M = n is true
        for (int n = 2; n <= 10; n++) {
            write(n, n - 1, complete(n));
            write(n, n, complete(n));
        }

        // Cycles C_n: even n is 2-colourable, odd n is not; M = 3 is always enough
        for (int n = 3; n <= 20; n++) {
            write(n, 2, cycle(n));
            write(n, 3, cycle(n));
        }

        // Paths and stars: trees are 2-colourable
        for (int n = 2; n <= 20; n++) {
            write(n, 2, path(n));
            write(n, 2, star(n));
        }

        // Extra hand-made graphs: two triangles sharing a vertex, a single edge
        write(5, 3, edges(new int[][]{{0,1},{1,2},{0,2},{2,3},{2,4},{3,4}}));
        write(5, 2, edges(new int[][]{{0,1},{1,2},{0,2},{2,3},{2,4},{3,4}}));
        write(2, 1, edges(new int[][]{{0,1}}));
        write(2, 2, edges(new int[][]{{0,1}}));

        // Random graphs over the full range of N and M
        Random rnd = new Random(184);
        for (int t = 0; t < 90; t++) {
            int n = 2 + rnd.nextInt(19);
            int m = 1 + rnd.nextInt(n);
            write(n, m, randomGraph(rnd, n));
        }

        System.out.println("Generated " + id + " test cases.");
    }

    static void write(int n, int m, int[][] e) throws Exception {

        if (n < 2 || n > 20 || m < 1 || m > n) {
            throw new IllegalStateException("N or M out of range");
        }
        if (e.length < 1 || e.length > n * (n - 1) / 2) {
            throw new IllegalStateException("E out of range");
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        boolean answer = colourable(n, m, e);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(n + " " + m + " " + e.length + "\r\n");
            for (int[] edge : e) {
                in.print(edge[0] + " " + edge[1] + "\r\n");
            }
            out.print((answer ? "true" : "false") + "\r\n");
        }

        id++;
    }

    // Independent reference: try colours on vertices in reverse order (the solution goes forward).
    static boolean colourable(int n, int m, int[][] e) {
        boolean[][] adj = new boolean[n][n];
        for (int[] edge : e) {
            adj[edge[0]][edge[1]] = true;
            adj[edge[1]][edge[0]] = true;
        }
        int[] col = new int[n];
        Arrays.fill(col, -1);
        return reverseFill(n - 1, n, m, adj, col);
    }

    static boolean reverseFill(int v, int n, int m, boolean[][] adj, int[] col) {
        if (v < 0) return true;
        for (int c = 0; c < m; c++) {
            boolean ok = true;
            for (int u = 0; u < n; u++) {
                if (adj[v][u] && col[u] == c) { ok = false; break; }
            }
            if (!ok) continue;
            col[v] = c;
            if (reverseFill(v - 1, n, m, adj, col)) return true;
            col[v] = -1;
        }
        return false;
    }

    static int[][] edges(int[][] e) {
        return e;
    }

    static int[][] complete(int n) {
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                list.add(new int[]{i, j});
        return list.toArray(new int[0][]);
    }

    static int[][] cycle(int n) {
        int[][] e = new int[n][];
        for (int i = 0; i < n - 1; i++) e[i] = new int[]{i, i + 1};
        e[n - 1] = new int[]{0, n - 1};
        return e;
    }

    static int[][] path(int n) {
        int[][] e = new int[n - 1][];
        for (int i = 0; i < n - 1; i++) e[i] = new int[]{i, i + 1};
        return e;
    }

    static int[][] star(int n) {
        int[][] e = new int[n - 1][];
        for (int i = 1; i < n; i++) e[i - 1] = new int[]{0, i};
        return e;
    }

    static int[][] randomGraph(Random rnd, int n) {
        double p = 0.1 + rnd.nextDouble() * 0.6;
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (rnd.nextDouble() < p) list.add(new int[]{i, j});
        if (list.isEmpty()) list.add(new int[]{0, 1});
        return list.toArray(new int[0][]);
    }
}
