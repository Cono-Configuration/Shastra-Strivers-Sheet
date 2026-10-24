# Editorial: 176. Combination Sum II

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Sorting
* Handling duplicates in recursion

## Problem Statement

Given an array `pool` that may contain duplicates and an integer `goal`, return all unique combinations whose sum equals `goal`. Each element of `pool` can be used at most once, and the answer must be in sorted order.

## Input Format

* The first line contains an integer `n`.
* The second line contains `n` space-separated integers.
* The third line contains the integer `goal`.

## Constraints

* `1 <= n <= 100`
* `1 <= pool[i] <= 50`
* `1 <= goal <= 30`

## Output Format

* The first line contains `m`, the number of unique combinations.
* Then `m` lines follow, each holding one combination in non-decreasing order, listed in lexicographic order. If there are none, only `0` is printed.

## Explanation & Approach

### Step 1: Sort the Array

Sorting does three jobs:

* Equal values become neighbours, which makes duplicate skipping easy.
* Each combination is built in non-decreasing order, so `[1, 2, 1]` is never formed separately from `[1, 1, 2]`.
* Once a value exceeds the remaining amount, all later values do too, so the loop can stop early.

### Step 2: Pick Each Element at Most Once

Define `explore(start, remaining, path)`:

* If `remaining == 0`, save a copy of `path`.
* Otherwise, for each index `i` from `start`:
  * If `i > start` and `pool[i] == pool[i - 1]`, skip it. This value was already tried at this level, and trying it again would only repeat the same combinations.
  * If `pool[i] > remaining`, stop.
  * Add `pool[i]`, recurse with `explore(i + 1, remaining - pool[i], path)`, then remove it (backtrack).

Recursing with `i + 1` is what prevents the same element from being used twice.

### Step 3: Why the Skip Condition Is `i > start`

Two equal values may both be used in one combination, but only as consecutive picks at different levels of the recursion. At a single level, choosing the second copy as the "first pick" of that level would duplicate what the first copy already produced. The check `i > start` skips only that case and still allows `[1, 1, 6]`.

### Walkthrough

```text
pool = [2, 5, 2, 1, 2] -> sorted [1, 2, 2, 2, 5], goal = 5
```

```text
[]                          remaining 5
├── [1]                     remaining 4
│   └── [1,2]               remaining 2
│       └── [1,2,2]         remaining 0  ✔
│           (second and third 2 at this level are skipped as duplicates)
├── [2]                     remaining 3
│   └── [2,2]               remaining 1  (dead end)
│       (the remaining 2s at the top level are skipped)
└── [5]                     remaining 0  ✔
```

The result is `[1, 2, 2]` and `[5]`.

## Example

### Input

```text
7
2 1 2 7 6 1 5
8
```

### Output

```text
4
1 1 6
1 2 5
1 7
2 6
```

## Java Solution

```java
class Solution {
    public List<List<Integer>> combinationSum2(int[] pool, int goal) {

        List<List<Integer>> result = new ArrayList<>();

        int[] sorted = pool.clone();
        Arrays.sort(sorted);

        explore(sorted, 0, goal, new ArrayList<>(), result);

        return result;
    }

    private void explore(int[] pool, int start, int remaining, List<Integer> path, List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < pool.length; i++) {

            if (i > start && pool[i] == pool[i - 1]) {
                continue;
            }

            if (pool[i] > remaining) {
                break;
            }

            path.add(pool[i]);
            explore(pool, i + 1, remaining - pool[i], path, result);
            path.remove(path.size() - 1);
        }
    }
}
```

## Time Complexity

Sorting costs `O(N log N)`. In the worst case the backtracking explores `O(2^N)` subsets, but pruning with the remaining sum and skipping duplicates reduces this a lot, and the target is at most 30.

**Time Complexity: `O(N log N + 2^N)`** in the worst case.

## Space Complexity

The recursion depth is at most the target (every number is at least 1), and the current path holds at most that many numbers (the output is not counted).

**Space Complexity: `O(N)`** at most, for the sorted copy and the recursion.

## Frequently Occurring Doubts

* **Why sort the array?** It groups equal values together for duplicate skipping and lets us stop the loop early.
* **How is the output order ensured?** Combinations are built in non-decreasing order, and values are tried from small to large, so the combinations appear in lexicographic order.

## Follow-ups

* **If numbers could be reused many times:** recurse with `i` instead of `i + 1` and drop the duplicate-skipping rule (this is Combination Sum).
* **To return only the number of unique combinations:** keep the same recursion but return a count instead of storing the paths, or count with a DP over `(value, count)` pairs.

## Key Takeaway

> Sort, recurse with `i + 1` so every element is used once, and skip a value at the same recursion level when it equals the previous one. That gives each unique combination exactly once, already in sorted order.
