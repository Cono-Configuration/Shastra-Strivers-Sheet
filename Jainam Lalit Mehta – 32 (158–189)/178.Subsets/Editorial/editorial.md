# Editorial: Problem 177 - Subsets

## Difficulty

Medium

## Prerequisites

* Arrays
* Bit Manipulation
* Backtracking / Recursion
* Lists

## Problem Statement

Given an array `elements` of distinct integers, return every subset of it (the power set), including the empty subset and the subset with all the elements. A subset must not appear twice.

## Input Format

The first line contains the integer `n`. The second line contains `n` space-separated integers, the elements of `elements`.

## Constraints

* `1 <= elements.length <= 10`
* `-10 <= elements[i] <= 10`
* All the values in `elements` are distinct.

## Output Format

Return all subsets. When printing, list them in bitmask order (mask `0` to `2^n - 1`, bit `i` selects `elements[i]`) on a single line such as `[[],[1],[2],[1,2]]`, with no spaces.

## Explanation & Approach

### Step 1: Count the Subsets

Each element has two choices: it is either in the subset or not. For `n` elements this gives:

```text
2 * 2 * ... * 2  (n times) = 2^n subsets
```

With `n <= 10` there are at most `1024` subsets, so listing all of them is cheap.

### Step 2: Encode a Choice as a Bitmask

Use an integer `mask` with `n` bits. Bit `i` of `mask` tells us whether `elements[i]` is chosen:

```text
bit i = 1  ->  elements[i] is in the subset
bit i = 0  ->  elements[i] is not in the subset
```

Every number from `0` to `2^n - 1` corresponds to exactly one subset, and different numbers give different subsets, so duplicates cannot occur.

### Step 3: Build Each Subset

For every `mask`:

* Start with an empty list.
* For `i` from `0` to `n - 1`, if `(mask >> i) & 1 == 1`, append `elements[i]`.
* Add the list to the result.

### Step 4: Why the Order Matches the Examples

Mask `0` gives `[]`, mask `1` gives `[elements[0]]`, mask `2` gives `[elements[1]]`, mask `3` gives `[elements[0], elements[1]]`, and so on. This is exactly the order shown in Example 1, which is why it is used as the printing order.

## Example

### Input

```text
3
1 2 3
```

### Analysis

| mask (binary) | bits set | subset  |
| ------------- | -------- | ------- |
| 000           | none     | []      |
| 001           | 0        | [1]     |
| 010           | 1        | [2]     |
| 011           | 0, 1     | [1,2]   |
| 100           | 2        | [3]     |
| 101           | 0, 2     | [1,3]   |
| 110           | 1, 2     | [2,3]   |
| 111           | 0, 1, 2  | [1,2,3] |

### Output

```text
[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

## Java Solution

```java
import java.util.*;

class Solution {
    public List<List<Integer>> listAllSubsets(int[] elements) {

        int n = elements.length;
        int total = 1 << n;

        List<List<Integer>> result = new ArrayList<>(total);

        for (int mask = 0; mask < total; mask++) {

            List<Integer> current = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    current.add(elements[i]);
                }
            }

            result.add(current);
        }

        return result;
    }
}
```

## Alternative: Backtracking

At index `i`, either skip `elements[i]` or take it, then recurse to `i + 1`. When `i == n`, record the current subset. This produces the same `2^n` subsets, only in a different order.

## Time Complexity

There are `2^n` subsets, and building each one takes up to `O(n)` work.

**Time Complexity: `O(n * 2^n)`**

## Space Complexity

Apart from the output, only a few variables are used.

**Space Complexity: `O(1)` auxiliary** (the output itself takes `O(n * 2^n)`).

## Key Takeaway

> A set of `n` distinct elements has `2^n` subsets, and each one corresponds to a unique `n`-bit mask. Loop over all masks and pick the elements whose bits are set to enumerate the power set without duplicates.
