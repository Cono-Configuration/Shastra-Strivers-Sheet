# Editorial: Word Search

## Difficulty

Medium

## Prerequisites

* 2D Arrays / Matrices
* Recursion
* Depth First Search (DFS)
* Backtracking

## Problem Statement

Given a grid of characters `grid` and a string `target`, determine whether `target` can be formed by moving through horizontally or vertically adjacent cells, using each cell at most once.

## Input Format

* The first line contains `rows` and `cols`.
* The next `rows` lines contain the rows of the grid.
* The last line contains `target`.

## Constraints

* `1 <= rows, cols <= 6`
* `1 <= target.length <= 15`
* `grid` and `target` consist of only lowercase and uppercase English letters.
* Comparison is case-sensitive.

## Output Format

Print `true` if `target` exists in the grid, otherwise `false`.

## Explanation & Approach

### Step 1: Try Every Starting Cell

The word can begin at any cell, so we start a search from each of the `rows * cols` cells. As soon as one search succeeds, the answer is `true`.

### Step 2: Define the Recursive Search

Let `search(r, c, index)` answer the question: "Can the part of `target` starting at position `index` be matched starting at cell `(r, c)`?"

* If `index == target.length()`, the whole word has been matched, so return `true`.
* If `(r, c)` is outside the grid, return `false`.
* If `grid[r][c] != target[index]`, return `false`.

### Step 3: Mark the Cell as Used

The same cell cannot be used twice. Before exploring further, temporarily replace the cell's character with a symbol that never appears in the input, such as `'#'`:

```text
saved = grid[r][c]
grid[r][c] = '#'
```

### Step 4: Explore the Four Neighbours

Try moving down, up, right and left, each with `index + 1`. If any of the four calls returns `true`, the word has been found.

### Step 5: Backtrack

After the four calls, restore the original character:

```text
grid[r][c] = saved
```

This lets other paths use the cell again, which is why it is called backtracking.

### Step 6: Pruning

* If `target.length() > rows * cols`, the word cannot fit, so the answer is `false` immediately.
* The search stops at the first mismatching character, so wrong branches are cut off early.
* For bigger grids, one can also compare letter frequencies, or search the reversed word when its last character is rarer than its first.

## Example

### Input

```text
3 4
ABCE
SFCS
ADEE
ABCCED
```

### Analysis

* Start at `A(0,0)`: it matches `A`.
* Move right to `B(0,1)`: matches `B`.
* Move right to `C(0,2)`: matches `C`.
* Move down to `C(1,2)`: matches the second `C`.
* Move down to `E(2,2)`: matches `E`.
* Move left to `D(2,1)`: matches `D`.
* All six characters are matched, so the search returns `true`.

### Output

```text
true
```

## Java Solution

```java
class Solution {
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
```

## Time Complexity

There are `rows * cols` starting cells. From each one the search can branch into at most 3 new directions at every step (the cell we came from is already used), and it goes at most `L` levels deep, where `L = target.length()`.

**Time Complexity: `O(rows * cols * 3^L)`**

In practice it is much faster because mismatching characters stop a branch immediately.

## Space Complexity

The recursion goes at most `L` levels deep, and the grid is modified in place.

**Space Complexity: `O(L)`**

## Key Takeaway

> Word Search is a classic DFS with backtracking: try every start cell, match one character at a time, mark the current cell as used, explore the four neighbours, then restore the cell. Pruning (length check, early mismatch) keeps the search fast.
