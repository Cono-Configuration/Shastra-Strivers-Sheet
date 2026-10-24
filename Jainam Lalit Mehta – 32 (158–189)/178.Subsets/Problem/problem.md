# Problem 177: Subsets

**Difficulty:** Medium | **Tags:** Bit Manipulation, Backtracking, Array

## Problem Statement

You are given an array `elements` made of distinct integers. List every subset that can be built from these elements, that is, the complete power set, including the empty subset and the subset that uses all the elements.

No subset may be listed more than once. The subsets may be returned in any order.

## Example 1

```text
Input: elements = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

## Example 2

```text
Input: elements = [0]
Output: [[],[0]]
```

## Input Format

The first line contains an integer `n`, the number of elements in `elements`.

The second line contains `n` space-separated integers, the elements of `elements`.

## Constraints

* `1 <= elements.length <= 10`
* `-10 <= elements[i] <= 10`
* All the values in `elements` are distinct.

## Output Format

Return all subsets of `elements`.

For printing (so that the output is unique), the subsets must be listed in the following order: for every integer `mask` from `0` to `2^n - 1`, the subset contains `elements[i]` whenever bit `i` of `mask` is set, and the elements inside a subset keep their order from `elements`. This is the same order used in the examples. Print everything on a single line in the form `[[],[1],[2],...]`, with no spaces.

## Approach

An array of `n` elements has exactly `2^n` subsets, because each element is either taken or left out. A bitmask of `n` bits encodes one such choice:

* Loop `mask` from `0` to `2^n - 1`.
* For each `mask`, scan the positions `0` to `n - 1`. If bit `i` of `mask` is `1`, add `elements[i]` to the current subset.
* Add the finished subset to the answer.

Every mask is different, so no subset repeats, and every possible subset appears exactly once.
