# Editorial: 179. Combination Sum III

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Combinations (choosing a set without order)
* Pruning a search by bounds

## Problem Statement

Given `picks` and `goal`, return every group of `picks` distinct numbers from `1` to `9` whose sum is `goal`.

## Input Format

A single line with two integers, `picks` and `goal`.

## Constraints

* `2 <= picks <= 9`
* `1 <= goal <= 60`

## Output Format

Each valid group is printed on its own line in bracket notation, values ascending, no spaces. Lines are in lexicographic order. If no group exists, the file is empty.

## Explanation & Approach

### Step 1: Only Ascending Groups Matter

A group is a set, so `[1,2,4]` and `[4,2,1]` are the same group. Build each group in ascending order so every group is generated exactly once.

### Step 2: Track a Start Value

Each recursive call receives `from`, the smallest value it may still use. After choosing `value`, the next call starts at `value + 1`. This keeps values distinct and ascending.

### Step 3: Track What Is Left

Each call carries `picks` (how many values are still needed) and `goal` (how much sum is still needed). Choosing `value` reduces them to `picks - 1` and `goal - value`.

### Step 4: Base Case

When `picks` reaches `0`, the group is complete. It is recorded only if `goal` is exactly `0`.

### Step 5: Prune

Values are tried in increasing order. If `value > goal`, every later value is also too large, so the loop stops.

### Walkthrough

```text
picks = 3, goal = 9

start with from = 1, choose 1  -> need 2 more, sum 8 left
    choose 2  -> need 1 more, sum 6 left
        choose 6 -> complete, sum 0   ✔  [1,2,6]
    choose 3  -> need 1 more, sum 5 left
        choose 5 -> complete, sum 0   ✔  [1,3,5]
...
```

## Example

### Input

```text
3 9
```

### Output

```text
[1,2,6]
[1,3,5]
[2,3,4]
```

## Java Solution

```java
import java.util.*;

class Solution {
    public List<List<Integer>> findGroups(int picks, int goal) {
        List<List<Integer>> result = new ArrayList<>();
        search(result, new ArrayList<>(), picks, goal, 1);
        return result;
    }

    private void search(List<List<Integer>> result, List<Integer> current, int picks, int goal, int from) {
        if (picks == 0) {
            if (goal == 0) {
                result.add(new ArrayList<>(current));
            }
            return;
        }

        for (int value = from; value <= 9; value++) {
            if (value > goal) {
                break;
            }
            current.add(value);
            search(result, current, picks - 1, goal - value, value + 1);
            current.remove(current.size() - 1);
        }
    }
}
```

## Time Complexity

There are only 9 values, so the search visits at most the subsets of `{1..9}`, which is `2^9 = 512`. Each recorded group costs `O(picks)` to copy.

**Time Complexity: `O(2^9 * 9)`**, which is a constant for this problem.

## Space Complexity

The recursion depth is at most `picks`, and the current group uses `O(picks)` space. The output is not counted.

**Space Complexity: `O(picks)`**

## Key Takeaway

> Build groups in ascending order with a start value, so each set is produced exactly once. Stop a loop as soon as the next value exceeds what remains of the goal.
