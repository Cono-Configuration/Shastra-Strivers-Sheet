# Problem 185: Sudoku Solver

**Difficulty:** Hard | **Tags:** Array, Backtracking, Matrix

## Problem Statement

You are given a 9 x 9 grid partly filled with digits. Fill every empty cell so that the completed grid follows these rules:

1. Every row contains each digit from `1` to `9` exactly once.
2. Every column contains each digit from `1` to `9` exactly once.
3. Each of the nine 3 x 3 sub-boxes contains each digit from `1` to `9` exactly once.

Empty cells are marked with the character `.`. The puzzle is guaranteed to have exactly one solution. Fill the grid in place.

## Example 1

```text
Input:
5 3 . | . 7 . | . . .
6 . . | 1 9 5 | . . .
. 9 8 | . . . | . 6 .
------+-------+------
8 . . | . 6 . | . . 3
4 . . | 8 . 3 | . . 1
7 . . | . 2 . | . . 6
------+-------+------
. 6 . | . . . | 2 8 .
. . . | 4 1 9 | . . 5
. . . | . 8 . | . 7 9

Output:
5 3 4 | 6 7 8 | 9 1 2
6 7 2 | 1 9 5 | 3 4 8
1 9 8 | 3 4 2 | 5 6 7
8 5 9 | 7 6 1 | 4 2 3
4 2 6 | 8 5 3 | 7 9 1
7 1 3 | 9 2 4 | 8 5 6
9 6 1 | 5 3 7 | 2 8 4
2 8 7 | 4 1 9 | 6 3 5
3 4 5 | 2 8 6 | 1 7 9
```

## Example 2

```text
Input:
. . . | . . . | 7 . .
7 . 5 | . . . | 9 . .
. . . | 9 7 5 | 4 3 1
------+-------+------
9 . . | . 4 1 | . . 7
. 5 . | 8 . 7 | 6 4 .
. 7 . | . 2 . | . . .
------+-------+------
. 4 . | . . . | . 6 9
1 6 . | 4 3 . | . . .
. . . | . 6 2 | 3 . 4

Output:
4 1 9 | 3 8 6 | 7 5 2
7 3 5 | 2 1 4 | 9 8 6
8 2 6 | 9 7 5 | 4 3 1
9 8 3 | 6 4 1 | 5 2 7
2 5 1 | 8 9 7 | 6 4 3
6 7 4 | 5 2 3 | 1 9 8
3 4 7 | 1 5 8 | 2 6 9
1 6 2 | 4 3 9 | 8 7 5
5 9 8 | 7 6 2 | 3 1 4
```

(The vertical and horizontal bars are only for readability. The actual input is nine lines of nine characters each, with no spaces.)

## Constraints

* `board.length == 9` and `board[i].length == 9`
* Each cell is a digit `'1'` to `'9'` or `'.'`.
* The input has exactly one solution.

## Input Format

Nine lines, each containing nine characters. Each character is a digit `1` to `9` or `.` for an empty cell. There are no spaces.

## Output Format

Nine lines, each containing the nine digits of the completed grid, with no spaces.

For Example 1 the output file is:

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

## Approach

Process the cells in order, left to right and top to bottom. For each empty cell, try the digits `1` to `9`. A digit is allowed only if it does not already appear in the cell's row, column, or 3 x 3 box. Place it, move to the next cell, and continue. If no digit works, undo the placement and try the next digit for the previous cell (backtracking).

Bitmasks record which digits each row, column, and box already contains, so each check takes constant time.
