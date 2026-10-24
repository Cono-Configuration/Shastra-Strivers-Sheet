# Editorial: 185. Sudoku Solver

## Difficulty

Hard

## Prerequisites

* Backtracking
* Bitmasks
* 2D arrays

## Problem Statement

Fill the empty cells of a 9 x 9 Sudoku grid so that every row, column, and 3 x 3 box contains the digits 1 to 9 exactly once.

## Input Format

Nine lines of nine characters. Digits `1` to `9` are given cells, and `.` marks an empty cell.

## Constraints

* The grid is 9 x 9.
* The puzzle has exactly one solution.

## Output Format

Nine lines of nine digits, with no spaces.

## Explanation & Approach

### Step 1: Record What Is Already Used

For each row, column, and box, keep a bitmask. Bit `d - 1` is set when digit `d` already appears there. Fill the masks from the given digits.

### Step 2: Walk the Cells in Order

Use one position counter from `0` to `80`. Row is `pos / 9` and column is `pos % 9`. Box index is `(row / 3) * 3 + col / 3`.

### Step 3: Try Digits

For an empty cell, try each digit `1` to `9`. Skip a digit if its bit is set in the row, column, or box mask. Otherwise place it, set the three bits, and recurse on the next position.

### Step 4: Backtrack

If the recursion fails, clear the digit from the cell and clear its three bits, then try the next digit. If no digit works, return `false` to the previous cell.

### Step 5: Base Case

When the position reaches `81`, every cell is filled and every constraint holds, so return `true`.

### Walkthrough

```text
Cell (0,2) is empty. Row 0 has {5,3,7}, column 2 has {8}, box 0 has {5,3,6,9,8}.
Allowed digits: 1, 2, 4.
Try 1: continue to (0,3). Later branches fail, so undo and try 2, then 4.
With 4, the rest of the grid completes, and the solver returns true.
```

## Example

### Input

```text
53..7....
6..195...
.98....6.
8...6...3
4..8.3..1
7...2...6
.6....28.
...419..5
....8..79
```

### Output

```text
534678912
672195348
198342567
859761423
426853791
713924856
961537284
287419635
345286179
```

## Java Solution

```java
public class Solution {

    public void fillGrid(char[][] grid) {
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
```

## Time Complexity

In the worst case, the search tries up to 9 digits in each of the empty cells. That bound is huge, but the constraints prune almost every branch. Each check uses bitmasks, so it takes constant time.

**Time Complexity: `O(9^K)`** worst case, where `K` is the number of empty cells. In practice, valid puzzles solve quickly.

## Space Complexity

The recursion depth is at most `81`, and the three mask arrays use constant space.

**Space Complexity: `O(1)`** auxiliary space, not counting the recursion stack, which is at most `81` frames.

## Key Takeaway

> Track used digits per row, column, and box with bitmasks, fill cells in order, and backtrack on dead ends. Bitmask checks keep each step constant-time.
