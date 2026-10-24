import java.util.*;

public class Solution {

    // Moves in the order D, L, R, U
    private static final int[] MOVE_ROW = {1, 0, 0, -1};
    private static final int[] MOVE_COL = {0, -1, 1, 0};
    private static final char[] MOVE_NAME = {'D', 'L', 'R', 'U'};

    public List<String> findRoutes(int[][] maze) {

        int size = maze.length;
        List<String> routes = new ArrayList<>();

        if (maze[0][0] == 0 || maze[size - 1][size - 1] == 0) {
            return routes;
        }

        boolean[][] visited = new boolean[size][size];
        walk(maze, visited, 0, 0, new StringBuilder(), routes);

        Collections.sort(routes);
        return routes;
    }

    private void walk(int[][] maze, boolean[][] visited, int row, int col,
                      StringBuilder path, List<String> routes) {

        int size = maze.length;

        if (row == size - 1 && col == size - 1) {
            routes.add(path.toString());
            return;
        }

        visited[row][col] = true;

        for (int k = 0; k < 4; k++) {
            int nextRow = row + MOVE_ROW[k];
            int nextCol = col + MOVE_COL[k];

            if (nextRow < 0 || nextCol < 0 || nextRow >= size || nextCol >= size) {
                continue;
            }
            if (visited[nextRow][nextCol] || maze[nextRow][nextCol] == 0) {
                continue;
            }

            path.append(MOVE_NAME[k]);
            walk(maze, visited, nextRow, nextCol, path, routes);
            path.setLength(path.length() - 1);
        }

        visited[row][col] = false;
    }

}
