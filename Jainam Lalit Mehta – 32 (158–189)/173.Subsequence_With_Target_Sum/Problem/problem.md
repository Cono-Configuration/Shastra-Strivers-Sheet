# Problem 173: Subsequence With Target Sum

**Difficulty:** Medium | **Tags:** Subset Sum, Recursion, Dynamic Programming, Array

## Problem Statement

You are given an array `values` and an integer `target`. Determine whether some subsequence of `values` has elements that add up to exactly `target`.

A subsequence is obtained by picking any number of elements from the array (each position can be used at most once) while keeping their original order. Return `Yes` if such a subsequence exists, otherwise return `No`.

## Example 1

```text
Input: values = [1, 2, 3, 4, 5], target = 8
Output: Yes
```

**Explanation:**

Subsequences such as `[1, 2, 5]`, `[1, 3, 4]` and `[3, 5]` all add up to `8`.

## Example 2

```text
Input: values = [4, 3, 9, 2], target = 10
Output: No
```

**Explanation:**

No subsequence of the array adds up to `10`.

## Example 3

```text
Input: values = [1, 10, 4, 5], target = 16
Output: Yes
```

**Explanation:**

The subsequence `[1, 10, 5]` adds up to `1 + 10 + 5 = 16`.

## Input Format

The first line contains an integer `n`, the number of elements in `values`.

The second line contains `n` space-separated integers, the elements of `values`.

The third line contains the integer `target`.

## Constraints

* `1 <= values.length <= 20`
* `1 <= values[i] <= 100`
* `1 <= target <= 2000`

## Output Format

Print `Yes` if a subsequence with sum equal to `target` exists, otherwise print `No`.

## Approach

Let `reachable[s]` be `true` when some subsequence of the elements processed so far sums to `s`.

* Start with `reachable[0] = true` (the empty subsequence).
* For every element `value`, update the sums from `target` down to `value`: if `reachable[sum - value]` is `true`, set `reachable[sum] = true`. Going downward makes sure the same element is not used twice.
* The answer is `reachable[target]`.

This needs `O(n * target)` time and `O(target)` extra space. (A plain recursion that either takes or skips each element also works for `n <= 20`, in `O(2^n)` time.)
