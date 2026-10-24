# Editorial: 175. Combination Sum

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Sorting

## Problem Statement

Given an array `vals` of distinct integers and an integer `goal`, return all unique combinations of numbers from `vals` that add up to `goal`. Each number can be used unlimited times, and two combinations are different if the frequency of at least one number differs.

## Input Format

* The first line contains an integer `n`.
* The second line contains `n` space-separated integers.
* The third line contains the integer `goal`.

## Constraints

* `1 <= n <= 30`
* `2 <= vals[i] <= 40`
* All elements of `vals` are distinct.
* `1 <= goal <= 40`
* The number of valid combinations is less than `150`.

## Output Format

* The first line contains `m`, the number of combinations.
* Then `m` lines follow, each holding one combination in non-decreasing order, listed in lexicographic order.

## Explanation & Approach

### Step 1: Sort the Array

Sorting lets us stop early: once a number is bigger than the remaining amount, every number after it is bigger too.

### Step 2: Choose a Number, Stay or Move Forward

Define `explore(start, remaining, path)`:

* If `remaining == 0`, the current `path` is a valid combination. Save a copy.
* Otherwise, for each index `i` from `start` to the end:
  * If `vals[i] > remaining`, stop (all later values are larger).
  * Add `vals[i]` to `path`.
  * Recurse with `explore(i, remaining - vals[i], path)`. Passing `i` (not `i + 1`) lets the same number be used again.
  * Remove `vals[i]` from `path` to backtrack.

### Step 3: Why There Are No Duplicate Combinations

The recursion only ever picks numbers at indices greater than or equal to the last chosen index. So a combination is always built in non-decreasing order, and the same multiset of numbers can only be generated in one way.

### Walkthrough

```text
vals = [2, 3, 5, 4] -> sorted [2, 3, 4, 5], goal = 7
```

```text
[]                       remaining 7
├── [2]                  remaining 5
│   ├── [2,2]            remaining 3
│   │   └── [2,2,3]      remaining 0  ✔
│   ├── [2,3]            remaining 2   (next numbers are all > 2, dead end)
│   └── [2,5]            remaining 0  ✔
├── [3]                  remaining 4
│   └── [3,4]            remaining 0  ✔
└── [4], [5]             dead ends
```

The result is `[2, 2, 3]`, `[2, 5]` and `[3, 4]`.

## Example

### Input

```text
4
3 4 5 6
10
```

### Output

```text
3
3 3 4
4 6
5 5
```

## Java Solution

```java
class Solution {
    public List<List<Integer>> combinationSum(int[] vals, int goal) {

        List<List<Integer>> result = new ArrayList<>();

        int[] sorted = vals.clone();
        Arrays.sort(sorted);

        explore(sorted, 0, goal, new ArrayList<>(), result);

        return result;
    }

    private void explore(int[] vals, int start, int remaining, List<Integer> path, List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < vals.length; i++) {

            if (vals[i] > remaining) {
                break;
            }

            path.add(vals[i]);
            explore(vals, i, remaining - vals[i], path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

## Time Complexity

The recursion tree has depth at most `goal / min(vals)`, and each node branches over the candidates. Since the number of answers is limited to fewer than `150` and `goal <= 40`, it runs very quickly. In general it is bounded by `O(N^(goal / min))`.

**Time Complexity: `O(N^(T / M))`** where `N = vals.length`, `T = goal` and `M = min(vals)`.

## Space Complexity

The recursion depth is at most `T / M`, and the current path holds at most that many numbers (the output is not counted).

**Space Complexity: `O(T / M)`**

## Key Takeaway

> Sort the numbers, then build combinations in non-decreasing order. Reusing the same index allows unlimited use of a number, and never moving back to a smaller index guarantees each combination appears exactly once.
