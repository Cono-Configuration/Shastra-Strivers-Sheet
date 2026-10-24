# Editorial: 178. Subsets II

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Sorting
* Handling duplicates in combinatorial search

## Problem Statement

Given a list `items` that may contain repeated values, return every distinct sub-collection of it. No sub-collection may appear twice in the result.

## Input Format

The first line contains `n`. The second line contains `n` integers separated by spaces.

## Constraints

* `1 <= items.length <= 10`
* `-10 <= items[i] <= 10`

## Output Format

Each distinct sub-collection is printed on its own line in bracket notation, with values in non-decreasing order. The empty sub-collection is printed as `[]`. In the test files, lines are in lexicographic order.

## Explanation & Approach

### Step 1: Sort First

Sort the array. Equal values become adjacent, which makes duplicates easy to detect during the search.

### Step 2: Build Sub-collections by Choosing a Start Index

Each recursive call receives a `start` index. The call records the current sub-collection (every node of the search tree is a valid subset), then tries adding each value at position `i >= start`. Passing `i + 1` as the next start guarantees each set of positions is visited once, in increasing order.

### Step 3: Skip Duplicates at the Same Depth

Inside the loop, if `i > start` and `items[i] == items[i - 1]`, skip the value. The earlier copy has already produced every sub-collection that starts with this value at this depth, so reusing it would only repeat results.

The condition `i > start` matters: the first copy of a value at a given depth must still be allowed, otherwise a sub-collection like `[2,2]` could never be built.

### Walkthrough

```text
items sorted = [1, 2, 2]

""
├── [1]
│   └── [1,2]
│       └── [1,2,2]
│   (the second 2 is skipped here: same depth, equal to the previous value)
└── [2]
    └── [2,2]
```

The recorded results, in order, are `[]`, `[1]`, `[1,2]`, `[1,2,2]`, `[2]`, `[2,2]`.

## Example

### Input

```text
3
1 2 2
```

### Output

```text
[]
[1]
[1,2]
[1,2,2]
[2]
[2,2]
```

## Java Solution

```java
import java.util.*;

class Solution {
    public List<List<Integer>> collectUniqueSubsets(int[] items) {
        int[] sorted = items.clone();
        Arrays.sort(sorted);

        List<List<Integer>> result = new ArrayList<>();
        explore(result, new ArrayList<>(), sorted, 0);
        return result;
    }

    private void explore(List<List<Integer>> result, List<Integer> current, int[] sorted, int start) {
        result.add(new ArrayList<>(current));

        for (int i = start; i < sorted.length; i++) {
            if (i > start && sorted[i] == sorted[i - 1]) {
                continue;
            }
            current.add(sorted[i]);
            explore(result, current, sorted, i + 1);
            current.remove(current.size() - 1);
        }
    }
}
```

## Time Complexity

With `n` entries, there are at most `2^n` distinct sub-collections, and each one is copied in `O(n)` time when recorded.

**Time Complexity: `O(n * 2^n)`** for the worst case, plus `O(n log n)` for sorting. Duplicates are pruned, so the search never explores more than the number of distinct sub-collections (plus a constant number of rejected branches per node).

## Space Complexity

The recursion depth is at most `n`, and the current sub-collection uses `O(n)` space. The output itself is not counted.

**Space Complexity: `O(n)`** auxiliary space.

## Key Takeaway

> Sort the values, then do a standard subset backtrack. Skip any value that equals the previous one at the same depth (`i > start`). That single check removes all duplicate sub-collections while keeping every distinct one.
