# Word Search

**Difficulty:** Medium | **Tags:** Matrix, Backtracking, Depth First Search

## Problem Statement

You are given a rectangular grid of characters `grid` with `rows` rows and `cols` columns, and a string `target`.

Return `true` if `target` can be spelled out by walking through the grid, otherwise return `false`.

The walk must follow these rules:

* The walk starts at any cell and visits the characters of `target` one by one, in order.
* Each next cell must be a **horizontal or vertical neighbour** (up, down, left or right) of the previous cell. Diagonal moves are not allowed.
* A single cell **cannot be used more than once** in the same walk.
* Letters are **case-sensitive**, so `'a'` and `'A'` are different characters.

## Example 1

```text
Input: grid = [["A","B","C","E"],
               ["S","F","C","S"],
               ["A","D","E","E"]], target = "ABCCED"
Output: true
```

**Explanation:**

The word can be traced through these cells (row, column), starting from the top-left corner:

```text
A(0,0) -> B(0,1) -> C(0,2) -> C(1,2) -> E(2,2) -> D(2,1)
```

Every step moves to an up, down, left or right neighbour, and no cell is repeated.

## Example 2

```text
Input: grid = [["A","B","C","E"],
               ["S","F","C","S"],
               ["A","D","E","E"]], target = "SEE"
Output: true
```

**Explanation:**

One valid walk is:

```text
S(1,3) -> E(2,3) -> E(2,2)
```

## Example 3

```text
Input: grid = [["A","B","C","E"],
               ["S","F","C","S"],
               ["A","D","E","E"]], target = "ABCB"
Output: false
```

**Explanation:**

The first three letters `A -> B -> C` can be matched, but the last `B` would need the cell `(0,1)` again. A cell cannot be reused, so `"ABCB"` cannot be formed.

## Constraints

* `rows == grid.length`
* `cols == grid[i].length`
* `1 <= rows, cols <= 6`
* `1 <= target.length <= 15`
* `grid` and `target` consist of only lowercase and uppercase English letters.

## Input Format

* The first line contains two integers `rows` and `cols`.
* The next `rows` lines each contain a string of exactly `cols` characters, describing one row of the grid.
* The last line contains the string `target`.

## Output Format

Print `true` if `target` exists in the grid, otherwise print `false`.

## Follow Up

Can you use search pruning to make your solution faster for a larger grid?

## Approach

Use Depth First Search with backtracking.

* Try every cell as the starting point of the walk.
* For a cell `(r, c)` and an index `index` into `target`:
  * If `index == target.length()`, every character has been matched, so return `true`.
  * If the cell is outside the grid, or `grid[r][c] != target[index]`, return `false`.
  * Temporarily mark the cell as used (for example, replace it with `'#'`).
  * Recursively try the four neighbours with `index + 1`.
  * Restore the original character (backtrack) and return the result.

**Pruning ideas:** stop immediately if `target` is longer than `rows * cols`, and stop as soon as the first mismatch occurs. For larger grids, you can also check that the grid has enough copies of each letter, or search the word in reverse if its last letter is rarer than its first.
