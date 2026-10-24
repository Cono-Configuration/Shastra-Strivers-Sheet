# Editorial: 187. Rat in a Maze

## Difficulty

Medium

## Prerequisites

* Backtracking
* Depth-first search on grids
* Visited-state marking and unmarking

## Problem Statement

Given a square maze of `0`s and `1`s, find every path from the top-left cell to the bottom-right cell that uses only open cells, never visits a cell twice on the same path, and moves only up, down, left, or right.

## Input Format

Line 1: `size`. Next `size` lines: the rows of the maze as digit strings.

## Constraints

* `2 <= size <= 5`
* `0 <= maze[i][j] <= 1`

## Output Format

One path per line, written as moves `U`, `D`, `L`, `R`, in lexicographic order. An empty file means no path exists.

## Explanation & Approach

### Step 1: Handle Blocked Endpoints

If the start cell or the end cell is `0`, no path can exist, so return an empty list.

### Step 2: Search Every Route

Start at `(0, 0)` with an empty path. At each cell:

* If it is the target `(size - 1, size - 1)`, record the path and stop this branch.
* Otherwise, try each of the four moves. A move is allowed only if the new cell is inside the grid, is open, and has not been visited on the current path.

### Step 3: Mark and Unmark

Set `visited[row][col] = true` on entry. Before returning, set it back to `false`. The marks represent only the current path, not every cell ever seen. Without the unmark step, cells used by one route would block other valid routes.

### Step 4: Sort

Collect all paths, sort them once, and return them. Sorting produces the same order as the test files.

### Walkthrough

```text
Example 1, first moves from (0,0):
  D -> (1,0)  open
    D -> (2,0)  open
      R -> (2,1)  open
        D -> (3,1) -> R -> (3,2) -> R -> (3,3)   path "DDRDRR"
    R -> (1,1)  open
      D -> (2,1) -> D -> (3,1) -> R -> (3,2) -> R -> (3,3)   path "DRDDRR"
  R -> (0,1)  blocked, skip
```

## Example

### Input

```text
4
1000
1101
1100
0111
```

### Output

```text
DDRDRR
DRDDRR
```

## Java Solution

```java
import java.util.*;

public class Solution {

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
```

## Time Complexity

Each cell can be entered at most once per path, and there are at most 4 moves from each cell. The search explores every simple path, so the worst case is exponential in the number of cells. For a grid of at most 25 cells this is small in practice, and the number of simple paths in a 5 x 5 grid is at most a few thousand.

**Time Complexity: `O(4^(N*N))`** in the theoretical worst case, where `N = size`. Because `N <= 5`, the search is bounded in practice. Each recorded path costs `O(N*N)` to build.

## Space Complexity

The recursion depth is at most `N * N = 25`, the visited grid uses `O(N*N)` space, and the output is not counted.

**Space Complexity: `O(N*N)`**

## Key Takeaway

> Use DFS with a visited grid that represents only the current path. Mark a cell on entry and unmark it on exit so every valid route is found, and no cell is used twice on one path.
