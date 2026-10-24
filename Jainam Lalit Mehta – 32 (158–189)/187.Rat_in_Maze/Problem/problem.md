# Problem 187: Rat in a Maze

**Difficulty:** Medium | **Tags:** Array, Backtracking, Matrix

## Problem Statement

You are given an `size x size` maze, stored as a grid of `0`s and `1`s. A rat starts at cell `(0, 0)` and wants to reach cell `(size - 1, size - 1)`.

A cell with value `1` can be used. A cell with value `0` is blocked. If the start cell is blocked, the rat cannot move at all.

The rat moves one cell at a time in one of four directions:

* `U` (up): row minus 1
* `D` (down): row plus 1
* `L` (left): column minus 1
* `R` (right): column plus 1

Find every path from the start to the end. A path is written as the string of its moves. No cell may be visited more than once on the same path. Return all paths in any order. If there is no path, return an empty list.

## Example 1

```text
Input: size = 4, maze = [[1,0,0,0],[1,1,0,1],[1,1,0,0],[0,1,1,1]]
Output: ["DDRDRR","DRDDRR"]
Explanation: The rat has two different paths to reach (3, 3).
The first path is (0,0) -> (1,0) -> (2,0) -> (2,1) -> (3,1) -> (3,2) -> (3,3).
The second path is (0,0) -> (1,0) -> (1,1) -> (2,1) -> (3,1) -> (3,2) -> (3,3).
```

## Example 2

```text
Input: size = 2, maze = [[1,0],[1,0]]
Output: []
Explanation: There is no path that the rat can take from (0,0) to (1,1).
```

## Constraints

* `2 <= size <= 5`
* `0 <= maze[i][j] <= 1`

## Input Format

The first line contains the integer `size`. Each of the next `size` lines contains one row of the maze as a string of `size` digits, with no spaces.

## Output Format

Print each path on its own line. Paths appear in lexicographic order.

If there is no path, the output file is empty (zero lines).

For Example 1 the input file is:

```text
4
1000
1101
1100
0111
```

and the output file is:

```text
DDRDRR
DRDDRR
```

## Approach

Run a depth-first search from `(0, 0)`. At each cell, try the four moves. Skip a move if it leaves the grid, goes to a blocked cell, or goes to a cell already on the current path. When the rat reaches `(size - 1, size - 1)`, record the moves made so far.

Mark a cell as visited when entering it and unmark it when leaving it. This lets the search find every path, not only the first one, while still preventing a cell from being used twice on the same path.

Sorting the paths at the end gives the lexicographic order used in the test files.
