# Problem 170: Enumerate Power Set

**Difficulty:** Medium | **Tags:** Backtracking, Bit Manipulation, Array

## Problem Statement

You are given an array `items` that contains distinct integers. Generate every subset that can be formed from its elements (the power set), including the empty subset and the subset containing all the elements.

The answer must not contain the same subset twice. The subsets may be returned in any order.

## Example 1

```text
Input: items = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

## Example 2

```text
Input: items = [0]
Output: [[],[0]]
```

## Input Format

The first line contains an integer `n`, the number of elements in `items`.

The second line contains `n` space-separated integers, the elements of `items`.

## Constraints

* `1 <= items.length <= 10`
* `-10 <= items[i] <= 10`
* All the values in `items` are distinct.

## Output Format

Return all subsets of `items`.

For printing (so that the output is unique), the subsets must be listed in the following order: for every integer `mask` from `0` to `2^n - 1`, the subset contains `items[i]` whenever bit `i` of `mask` is set, and the elements inside a subset keep their order from `items`. This is the same order used in the examples. Print everything on a single line in the form `[[],[1],[2],...]`, with no spaces.

## Approach

An array of `n` elements has exactly `2^n` subsets, because each element is either taken or left out. A bitmask of `n` bits encodes one such choice:

* Loop `mask` from `0` to `2^n - 1`.
* For each `mask`, scan the positions `0` to `n - 1`. If bit `i` of `mask` is `1`, add `items[i]` to the current subset.
* Add the finished subset to the answer.

Every mask is different, so no subset repeats, and every possible subset appears exactly once.
