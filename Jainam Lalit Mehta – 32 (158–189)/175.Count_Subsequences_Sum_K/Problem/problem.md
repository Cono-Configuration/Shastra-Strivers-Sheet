# Problem 174: Count All Subsequences with Sum K

**Difficulty:** Medium | **Tags:** Array, Dynamic Programming, Subsequence

## Problem Statement

You are given an array `values` of positive integers and a positive integer `target`.

Count the non-empty subsequences of `values` whose elements add up to `target`. A subsequence is chosen by picking some positions of the array, in their original order. Two subsequences are different if they use different positions, even when their values are the same.

## Example 1

```text
Input: values = [4, 9, 2, 5, 1], target = 10
Output: 2
Explanation: The subsequences with sum 10 are [9, 1] and [4, 5, 1].
```

## Example 2

```text
Input: values = [4, 2, 10, 5, 1, 3], target = 5
Output: 3
Explanation: The subsequences with sum 5 are [4, 1], [2, 3], and [5].
```

## Constraints

* `1 <= values.length <= 20`
* `1 <= values[i] <= 100`
* `1 <= target <= 2000`

## Input Format

Three lines:

1. The integer `n`, the length of `values`.
2. The `n` integers, separated by single spaces.
3. The integer `target`.

## Output Format

Print a single integer: the number of non-empty subsequences whose sum is `target`.

For Example 1 the input file is:

```text
5
4 9 2 5 1
10
```

and the output file is:

```text
2
```

## Approach

Keep an array `ways`, where `ways[s]` is the number of subsequences that sum to `s`. Start with `ways[0] = 1`, since the empty subsequence has sum `0`.

Process the values one at a time. For each value, add `ways[s - value]` to `ways[s]` for every sum `s` from `target` down to `value`. Going downward makes sure each value is used at most once. The answer is `ways[target]`.

Because `target >= 1`, the empty subsequence is never counted in the answer.
