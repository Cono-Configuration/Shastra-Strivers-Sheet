# N Queens

**Difficulty:** Hard | **Tags:** Backtracking, Recursion, Matrix

## Problem Statement

The **n-queens puzzle** asks you to place `size` chess queens on a `size x size` chessboard so that no two queens attack each other.

Given an integer `size`, return **every distinct solution** to the puzzle.

Each solution is a board arrangement, written as a list of `size` strings. In these strings, `'Q'` marks a queen and `'.'` marks an empty square.

Two queens attack each other according to these rules:

1. **Same Row** - No two queens can be in the same row.
2. **Same Column** - No two queens can be in the same column.
3. **Same Diagonal (top-left to bottom-right)** - No two queens can share a diagonal on which `row - col` is equal.
4. **Same Anti-Diagonal (top-right to bottom-left)** - No two queens can share an anti-diagonal on which `row + col` is equal.

The solutions must be listed in the order produced by placing queens **row by row, trying columns from left to right** (so the solution whose first-row queen is further left comes first, then the second row is compared, and so on). If no arrangement exists, the output is `[[]]`.

## Example 1

```text
Input: size = 4
Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
```

**Explanation:**

There are two possible arrangements:

```text
Solution 1        Solution 2
. Q . .           . . Q .
. . . Q           Q . . .
Q . . .           . . . Q
. . Q .           . Q . .
```

## Example 2

```text
Input: size = 2
Output: [[]]
```

**Explanation:**

There is no way to place two queens on a `2 x 2` board without them attacking each other, so there are no valid arrangements.

## Example 3

```text
Input: size = 1
Output: [["Q"]]
```

**Explanation:**

A `1 x 1` board has one square, so the only arrangement places the single queen there.

## Constraints

* `1 <= size <= 9`

## Input Format

A single line containing the integer `size`.

## Output Format

Print all solutions in the format shown in the examples, for example `[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]`. Print `[[]]` if there is no solution.

## Approach

Use backtracking and place one queen in each row.

* Keep three boolean arrays: `colUsed[col]`, `diagUsed[row - col + size - 1]` and `antiDiagUsed[row + col]`.
* Start with `row = 0`. For each column in the current row, skip the square if its column, diagonal or anti-diagonal is already used.
* Otherwise place the queen, mark the column, diagonal and anti-diagonal as used, and move on to `row + 1`.
* When `row == size`, all queens are placed, so build the board from the recorded columns and add it to the answer.
* After returning from the recursive call, unmark the three arrays (backtrack) and try the next column.

## Follow-up Questions

* How would you modify the solution for the k-queens problem (fewer than `size` queens)?
* How would you count the total number of solutions instead of returning them?
