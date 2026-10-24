import java.util.*;

public class Solution {

    public List<List<String>> placeQueens(int size) {

        List<List<String>> answer = new ArrayList<>();

        int[] queenCol = new int[size];
        boolean[] colUsed = new boolean[size];
        boolean[] diagUsed = new boolean[2 * size - 1];
        boolean[] antiDiagUsed = new boolean[2 * size - 1];

        place(0, size, queenCol, colUsed, diagUsed, antiDiagUsed, answer);

        return answer;
    }

    private void place(int row, int size, int[] queenCol, boolean[] colUsed,
                       boolean[] diagUsed, boolean[] antiDiagUsed, List<List<String>> answer) {

        if (row == size) {
            answer.add(buildBoard(queenCol, size));
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

    private List<String> buildBoard(int[] queenCol, int size) {

        List<String> board = new ArrayList<>();

        for (int row = 0; row < size; row++) {

            char[] line = new char[size];
            Arrays.fill(line, '.');
            line[queenCol[row]] = 'Q';

            board.add(new String(line));
        }

        return board;
    }

}
