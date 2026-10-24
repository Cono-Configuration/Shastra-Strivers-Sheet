# Editorial: 174. Count All Subsequences with Sum K

## Difficulty

Medium

## Prerequisites

* Subset-sum style dynamic programming
* 1D DP arrays
* Iterating in reverse to avoid reuse

## Problem Statement

Given an array of positive integers and a target, count the non-empty subsequences whose sum equals the target. Subsequences are counted by position, so equal values at different positions give different subsequences.

## Input Format

Line 1: `n`. Line 2: `n` integers. Line 3: `target`.

## Constraints

* `1 <= n <= 20`
* `1 <= values[i] <= 100`
* `1 <= target <= 2000`

## Output Format

A single integer.

## Explanation & Approach

### Step 1: Define the DP State

Let `ways[s]` be the number of subsequences (among the values processed so far) with sum `s`.

### Step 2: Base Case

Before any values are processed, the only subsequence is the empty one, which has sum `0`. So `ways[0] = 1` and every other entry is `0`.

### Step 3: Process Each Value

For a value `x`, every existing subsequence with sum `s - x` can be extended by `x` to form one with sum `s`. So the update is:

`ways[s] += ways[s - x]` for `s` from `target` down to `x`.

### Step 4: Why the Loop Goes Downward

If the loop went upward, `ways[s - x]` might already include `x` from this same pass, which would use the value twice. Going downward reads only the values from before this pass.

### Step 5: Answer

After all values are processed, `ways[target]` counts every subsequence with sum `target`. The empty subsequence has sum `0`, so it is never counted, because `target >= 1`.

### Walkthrough

```text
values = [4, 9, 2, 5, 1], target = 10 (only sums up to 10 are tracked)

Start:      sums with ways > 0: {0}
After 4:    {0, 4}
After 9:    {0, 4, 9}           (4+9=13 is beyond 10, ignored)
After 2:    {0, 2, 4, 6, 9}     (9+2=11 is beyond 10, ignored)
After 5:    {0, 2, 4, 5, 6, 7, 9}  ways[9] = 2 (the subsets {9} and {4, 5})
After 1:    ways[10] += ways[9] = 2, so the answer is 2
```

## Example

### Input

```text
5
4 9 2 5 1
10
```

### Output

```text
2
```

## Java Solution

```java
public class Solution {

    public long countSumSubsequences(int[] values, int target) {
        long[] ways = new long[target + 1];
        ways[0] = 1;

        for (int value : values) {
            for (int s = target; s >= value; s--) {
                ways[s] += ways[s - value];
            }
        }

        return ways[target];
    }
}
```

## Time Complexity

For each of the `n` values, the inner loop runs at most `target` times.

**Time Complexity: `O(n * target)`**, which is at most `20 * 2000 = 40000` operations.

## Space Complexity

The DP array has `target + 1` entries.

**Space Complexity: `O(target)`**

## Key Takeaway

> Count subsequences with a 1D DP over sums. Iterate the sums in reverse so each element contributes at most once.
