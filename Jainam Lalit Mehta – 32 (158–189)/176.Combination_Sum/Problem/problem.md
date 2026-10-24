# Problem 175: Combination Sum

**Difficulty:** Medium | **Tags:** Array, Backtracking, Recursion

## Problem Statement

You are given an array `vals` of distinct integers and an integer `goal`. Return every unique collection of numbers from `vals` whose total equals `goal`. The collections may be returned in any order.

Each number in `vals` may be used any number of times. Two collections are considered different when at least one number is used a different number of times in them.

The input is guaranteed to produce fewer than `150` such collections. If no collection adds up to `goal`, return an empty list.

## Example 1

```text
Input: vals = [2, 3, 5, 4], goal = 7
Output: [[2, 2, 3], [2, 5], [3, 4]]
```

**Explanation:**

* `2 + 2 + 3 = 7`. Note that `2` can be used more than once.
* `2 + 5 = 7`.
* `3 + 4 = 7`.

There are three collections in total.

## Example 2

```text
Input: vals = [2], goal = 1
Output: []
```

**Explanation:** There is no way to pick numbers from `vals` that add up to `goal`.

## Example 3

```text
Input: vals = [3, 4, 5, 6], goal = 10
Output: [[3, 3, 4], [4, 6], [5, 5]]
```

**Explanation:**

* `3 + 3 + 4 = 10`.
* `4 + 6 = 10`.
* `5 + 5 = 10`.

Collections such as `[4, 3, 3]` or `[6, 4]` are not listed separately, because they use the same numbers the same number of times as `[3, 3, 4]` and `[4, 6]`.

## Constraints

* `1 <= vals.length <= 30`
* `2 <= vals[i] <= 40`
* All the elements of `vals` are distinct.
* `1 <= goal <= 40`
* The number of valid collections is less than `150`.

## Input Format

* The first line contains an integer `n`, the size of `vals`.
* The second line contains `n` space-separated integers, the elements of `vals`.
* The third line contains the integer `goal`.

## Output Format

* The first line contains an integer `m`, the number of valid collections.
* The next `m` lines each contain one collection, written as space-separated numbers in non-decreasing order.
* The collections are listed in lexicographic order. If `m` is `0`, only the line `0` is printed.

(Since any order is accepted by the original problem, this fixed order is used only so that the expected output file is unique.)

## Approach

Sort the array, then build collections with backtracking.

* Keep the list of numbers chosen so far and the amount still needed.
* When the remaining amount becomes `0`, store a copy of the current list.
* At each step, try the numbers from the current index onward. Staying on the same index again allows a number to be reused, while never moving backward prevents the same collection from appearing in a different order.
* Because the array is sorted, stop trying numbers as soon as one is larger than the remaining amount.
