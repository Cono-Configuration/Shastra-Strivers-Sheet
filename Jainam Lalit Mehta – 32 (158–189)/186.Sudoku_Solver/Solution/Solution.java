public class Solution {

    public void fillGrid(char[][] grid) {

        // Bitmasks of digits already used in each row, column, and 3x3 box.
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] boxes = new int[9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] != '.') {
                    int bit = 1 << (grid[r][c] - '1');
                    rows[r] |= bit;
                    cols[c] |= bit;
                    boxes[(r / 3) * 3 + c / 3] |= bit;
                }
            }
        }

        solve(grid, rows, cols, boxes, 0);
    }

    private boolean solve(char[][] grid, int[] rows, int[] cols, int[] boxes, int position) {

        if (position == 81) {
            return true;
        }

        int r = position / 9;
        int c = position % 9;

        if (grid[r][c] != '.') {
            return solve(grid, rows, cols, boxes, position + 1);
        }

        int box = (r / 3) * 3 + c / 3;

        for (int d = 0; d < 9; d++) {
            int bit = 1 << d;
            if ((rows[r] & bit) != 0 || (cols[c] & bit) != 0 || (boxes[box] & bit) != 0) {
                continue;
            }

            grid[r][c] = (char) ('1' + d);
            rows[r] |= bit;
            cols[c] |= bit;
            boxes[box] |= bit;

            if (solve(grid, rows, cols, boxes, position + 1)) {
                return true;
            }

            grid[r][c] = '.';
            rows[r] &= ~bit;
            cols[c] &= ~bit;
            boxes[box] &= ~bit;
        }

        return false;
    }

}
