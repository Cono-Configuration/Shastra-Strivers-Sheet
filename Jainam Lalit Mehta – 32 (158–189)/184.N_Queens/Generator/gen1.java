import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final String INPUT_DIR = BASE_DIR + File.separator + "Input";
    static final String OUTPUT_DIR = BASE_DIR + File.separator + "Output";

    public static void main(String[] args) throws Exception {

        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        int testCase = 0;

        // Constraint is 1 <= n <= 9, so there are only 9 distinct inputs.
        for (int n = 1; n <= 9; n++) {
            testCase = generateCase(testCase, n);
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, int n) throws Exception {

        String output = format(solve(n));

        String inputFile = INPUT_DIR + File.separator + String.format("input%02d.txt", id);
        String outputFile = OUTPUT_DIR + File.separator + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(n + "\r\n");
            out.print(output + "\r\n");
        }

        return id + 1;
    }

    static List<List<String>> solve(int size) {

        List<List<String>> answer = new ArrayList<>();

        int[] queenCol = new int[size];
        boolean[] colUsed = new boolean[size];
        boolean[] diagUsed = new boolean[2 * size - 1];
        boolean[] antiDiagUsed = new boolean[2 * size - 1];

        place(0, size, queenCol, colUsed, diagUsed, antiDiagUsed, answer);

        return answer;
    }

    static void place(int row, int size, int[] queenCol, boolean[] colUsed,
                      boolean[] diagUsed, boolean[] antiDiagUsed, List<List<String>> answer) {

        if (row == size) {

            List<String> board = new ArrayList<>();

            for (int r = 0; r < size; r++) {

                char[] line = new char[size];
                Arrays.fill(line, '.');
                line[queenCol[r]] = 'Q';

                board.add(new String(line));
            }

            answer.add(board);
            return;
        }

        for (int col = 0; col < size; col++) {

            int diag = row - col + size - 1;
            int antiDiag = row + col;

            if (colUsed[col] || diagUsed[diag] || antiDiagUsed[antiDiag]) {
                continue;
            }

            queenCol[row] = col;
            colUsed[col] = true;
            diagUsed[diag] = true;
            antiDiagUsed[antiDiag] = true;

            place(row + 1, size, queenCol, colUsed, diagUsed, antiDiagUsed, answer);

            colUsed[col] = false;
            diagUsed[diag] = false;
            antiDiagUsed[antiDiag] = false;
        }
    }

    // Output format: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
    // When no arrangement exists the output is [[]]
    static String format(List<List<String>> result) {

        if (result.isEmpty()) {
            return "[[]]";
        }

        StringBuilder sb = new StringBuilder();

        sb.append('[');

        for (int i = 0; i < result.size(); i++) {

            if (i > 0) {
                sb.append(',');
            }

            sb.append('[');

            List<String> board = result.get(i);

            for (int j = 0; j < board.size(); j++) {

                if (j > 0) {
                    sb.append(',');
                }

                sb.append('"').append(board.get(j)).append('"');
            }

            sb.append(']');
        }

        sb.append(']');

        return sb.toString();
    }
}
