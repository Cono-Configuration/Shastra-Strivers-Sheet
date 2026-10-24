# Problem 176: Combination Sum II

**Difficulty:** Medium | **Tags:** Array, Backtracking, Recursion, Sorting

## Problem Statement

You are given a collection of integers `pool` (which may contain repeated values) and an integer `goal`. Find every distinct group of numbers taken from `pool` whose total equals `goal`, and return all such groups in sorted order.

Every element of `pool` can be used **at most once** in a group. Two groups that contain the same numbers in a different arrangement count as the same group. For instance, `[1, 1, 2]` and `[1, 2, 1]` are not unique.

## Example 1

```text
Input: pool = [2, 1, 2, 7, 6, 1, 5], goal = 8
Output: [[1, 1, 6], [1, 2, 5], [1, 7], [2, 6]]
```

**Explanation:** The groups that add up to the goal are:

```text
1 + 1 + 6 => 8
1 + 2 + 5 => 8
1 + 7     => 8
2 + 6     => 8
```

## Example 2

```text
Input: pool = [2, 5, 2, 1, 2], goal = 5
Output: [[1, 2, 2], [5]]
```

**Explanation:** The groups that add up to the goal are:

```text
1 + 2 + 2 => 5
5         => 5
```

## Example 3

```text
Input: pool = [2, 1, 2], goal = 5
Output: [[1, 2, 2]]
```

**Explanation:** `1 + 2 + 2 = 5`, and each element is used once. Using a `1` several times, such as `[1, 1, 1, 1, 1]`, is not allowed because `pool` contains only a single `1`.

## Constraints

* `1 <= pool.length <= 100`
* `1 <= pool[i] <= 50`
* `1 <= goal <= 30`

## Input Format

* The first line contains an integer `n`, the size of `pool`.
* The second line contains `n` space-separated integers, the elements of `pool`.
* The third line contains the integer `goal`.

## Output Format

* The first line contains an integer `m`, the number of distinct groups.
* The next `m` lines each contain one group, written as space-separated numbers in non-decreasing order.
* The groups are listed in lexicographic order. If `m` is `0`, only the line `0` is printed.

## Approach

Sort the array first so that equal numbers sit next to each other, then build groups with backtracking.

* Keep the numbers chosen so far and the amount still needed. When the amount reaches `0`, save a copy of the chosen numbers.
* At each step, try the numbers from the current position onward and move to the **next** index after picking one, since an element cannot be reused.
* To avoid duplicate groups, at one recursion level skip a number if it equals the previous number tried at that same level.
* Because the array is sorted, stop trying numbers once one is larger than the remaining amount.

Sorting also makes every group non-decreasing and the groups come out in lexicographic order automatically.
