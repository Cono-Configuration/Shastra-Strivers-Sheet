# Editorial: Problem 173 - Subsequence With Target Sum

## Difficulty

Medium

## Prerequisites

* Arrays
* Recursion / Take-or-Skip Choices
* Basic Dynamic Programming (Subset Sum)
* Boolean Arrays

## Problem Statement

Given an array `values` and an integer `target`, decide whether some subsequence of `values` sums to exactly `target`. Each position may be used at most once. Print `Yes` if it exists and `No` otherwise.

## Input Format

The first line contains `n`. The second line contains the `n` elements of `values`, separated by spaces. The third line contains `target`.

## Constraints

* `1 <= values.length <= 20`
* `1 <= values[i] <= 100`
* `1 <= target <= 2000`

## Output Format

Print `Yes` if a subsequence with sum `target` exists, otherwise `No`.

## Explanation & Approach

### Step 1: Think in Terms of Choices

Every element is either taken or skipped. Trying all choices gives `2^n` subsequences. For `n = 20` that is about one million, which is fine, but a smarter method is easy and scales better.

### Step 2: Track Which Sums Are Possible

Keep a boolean array `reachable[0..target]`:

```text
reachable[s] = true  if some chosen subset of the processed elements sums to s
```

Initially only `reachable[0]` is `true`, because the empty subsequence has sum `0`.

### Step 3: Add One Element at a Time

For an element `value`, any sum `s - value` that was already reachable becomes reachable as `s` after taking this element:

```text
for sum from target down to value:
    if reachable[sum - value]: reachable[sum] = true
```

The loop runs **downward** so `reachable[sum - value]` still describes the state *before* this element. Running it upward would let the same element be used several times.

### Step 4: Read the Answer

After all elements are processed, the answer is `reachable[target]`.

## Example

### Input

```text
4
4 3 9 2
10
```

### Analysis

Reachable sums after each element (only sums up to `10`):

| Element | Reachable sums                  |
| ------- | ------------------------------- |
| start   | 0                               |
| 4       | 0, 4                            |
| 3       | 0, 3, 4, 7                      |
| 9       | 0, 3, 4, 7, 9                   |
| 2       | 0, 2, 3, 4, 5, 6, 7, 9          |

`10` is not reachable.

### Output

```text
No
```

## Java Solution

```java
class Solution {

    public boolean canReachTarget(int[] values, int target) {

        boolean[] reachable = new boolean[target + 1];
        reachable[0] = true;

        for (int value : values) {

            for (int sum = target; sum >= value; sum--) {
                if (reachable[sum - value]) {
                    reachable[sum] = true;
                }
            }
        }

        return reachable[target];
    }
}
```

The driver prints `Yes` when the method returns `true` and `No` otherwise.

## Alternative: Recursion

`canMake(index, remaining)`: if `remaining == 0` return `true`; if `index == n` or `remaining < 0` return `false`; otherwise try taking `values[index]` or skipping it. This is `O(2^n)` time.

## Time Complexity

For every element we scan at most `target` sums.

**Time Complexity: `O(n * target)`**

## Space Complexity

One boolean array of size `target + 1`.

**Space Complexity: `O(target)`**

## Key Takeaway

> Subset-sum questions can be solved by remembering which totals are reachable. Process the elements one by one and update the array from high sums to low sums so every element is used at most once.
