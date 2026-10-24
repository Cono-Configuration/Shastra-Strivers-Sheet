public class Solution {

    public boolean wordExists(char[][] grid, String target) {

        int rows = grid.length;
        int cols = grid[0].length;

        if (target.length() > rows * cols) {
            return false;
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (search(grid, target, r, c, 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean search(char[][] grid, String target, int r, int c, int index) {

        if (index == target.length()) {
            return true;
        }

        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length) {
            return false;
        }

        if (grid[r][c] != target.charAt(index)) {
            return false;
        }

        char saved = grid[r][c];
        grid[r][c] = '#';

        boolean found = search(grid, target, r + 1, c, index + 1)
                || search(grid, target, r - 1, c, index + 1)
                || search(grid, target, r, c + 1, index + 1)
                || search(grid, target, r, c - 1, index + 1);

        grid[r][c] = saved;

        return found;
    }

}
