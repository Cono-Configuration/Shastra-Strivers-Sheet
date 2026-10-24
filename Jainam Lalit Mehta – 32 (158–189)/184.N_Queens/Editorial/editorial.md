# Editorial: N Queens

## Difficulty

Hard

## Prerequisites

* Recursion
* Backtracking
* 2D Boards / Matrices
* Arrays and Strings

## Problem Statement

Given an integer `size`, place `size` queens on a `size x size` chessboard so that no two queens attack each other, and return every distinct arrangement. Solutions are listed in the order obtained by filling rows from top to bottom and trying columns from left to right.

## Input Format

The input contains a single integer `size`.

## Constraints

* `1 <= size <= 9`

## Output Format

Print every board as a list of strings (`'Q'` for a queen, `'.'` for an empty square), all collected in one list. Print `[[]]` if no arrangement exists.

## Explanation & Approach

### Step 1: One Queen per Row

Two queens cannot share a row, and a board with `size` rows must hold `size` queens. So exactly one queen goes in each row. We only need to decide the **column** of the queen in every row.

### Step 2: Track Attacked Lines

Besides rows, a new queen at `(row, col)` must avoid three kinds of lines already used by earlier queens:

* **Column:** `col`
* **Diagonal (top-left to bottom-right):** all squares share the same `row - col`. To avoid negative indices we store it as `row - col + size - 1`, which ranges from `0` to `2 * size - 2`.
* **Anti-diagonal (top-right to bottom-left):** all squares share the same `row + col`, which ranges from `0` to `2 * size - 2`.

Three boolean arrays tell us in `O(1)` whether a square is safe.

### Step 3: Backtracking

Define `place(row)`:

* If `row == size`, every row has a queen, so build the board from the stored columns and save it.
* Otherwise, for each `col` from `0` to `size - 1`:
  * If the column, diagonal or anti-diagonal is already used, skip it.
  * Place the queen: remember `queenCol[row] = col` and mark the three arrays.
  * Call `place(row + 1)`.
  * Remove the queen (set the three marks back to `false`) and try the next column.

Trying columns from left to right gives the solutions in the required order.

### Step 4: Build the Board

For a stored list `queenCol`, row `r` is a string of `size` dots with a `'Q'` at index `queenCol[r]`.

## Example

### Input

```text
4
```

### Analysis

* Row 0, column 0: the queen is placed, but later rows quickly run out of safe squares, so this branch fails.
* Row 0, column 1: row 1 can only use column 3, row 2 can only use column 0, and row 3 can use column 2. This gives `[".Q..","...Q","Q...","..Q."]`.
* Row 0, column 2: by symmetry, this gives `["..Q.","Q...","...Q",".Q.."]`.
* Row 0, column 3: no complete arrangement exists.

### Output

```text
[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
```

## Java Solution

```java
class Solution {
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
```

## Time Complexity

In the worst case the search tries up to `N` columns in the first row, fewer in the next rows, and so on, which is bounded by `O(N!)`. Pruning with the three boolean arrays removes most of these branches. Building a board costs `O(N^2)` and is done once per solution.

**Time Complexity: `O(N!)`**

where `N` is the value of `size`.

## Space Complexity

The three boolean arrays and `queenCol` use `O(N)` space and the recursion depth is `N`. The list of boards is the output and is not counted as auxiliary space.

**Space Complexity: `O(N)`**

## Follow-up Answers

* **k-queens (fewer than N queens):** stop the recursion once `k` queens have been placed, and allow a row to be skipped without a queen (an extra recursive call that moves to `row + 1` without placing anything).
* **Counting solutions only:** do not build the boards. Keep an integer counter and increase it whenever `row == size`.

## Key Takeaway

> Place one queen per row and only decide the column. Use three boolean arrays (column, diagonal `row - col`, anti-diagonal `row + col`) to check safety in `O(1)`, recurse to the next row, and undo the choice afterwards. This backtracking prunes invalid placements early and solves every `size` up to `9` instantly.
