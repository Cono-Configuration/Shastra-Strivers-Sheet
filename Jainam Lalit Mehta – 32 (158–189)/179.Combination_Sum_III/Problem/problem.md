# Problem 179: Combination Sum III

**Difficulty:** Medium | **Tags:** Array, Backtracking, Combinations

## Problem Statement

You are given two integers, `picks` and `goal`. Enumerate every group of exactly `picks` different numbers such that:

* every number in the group is between `1` and `9` (inclusive),
* no number appears more than once in the same group, and
* the numbers in the group add up to `goal`.

Return all such groups. No group may appear twice in the answer, and the groups may be returned in any order. If no group exists, return an empty list.

In the test files, each group is written in ascending order and the groups are listed one per line in lexicographic order (comparing element by element, where a shorter sequence that is a prefix of a longer one comes first).

## Example 1

```text
Input: picks = 3, goal = 7
Output: [[1,2,4]]
Explanation:
1 + 2 + 4 = 7
There are no other valid groups.
```

## Example 2

```text
Input: picks = 3, goal = 9
Output: [[1,2,6],[1,3,5],[2,3,4]]
Explanation:
1 + 2 + 6 = 9
1 + 3 + 5 = 9
2 + 3 + 4 = 9
There are no other valid groups.
```

## Example 3

```text
Input: picks = 4, goal = 1
Output: []
Explanation: There are no valid groups.
Using 4 different numbers in the range [1,9], the smallest sum we can get is 1+2+3+4 = 10, and since 10 > 1, there are no valid groups.
```

## Constraints

* `2 <= picks <= 9`
* `1 <= goal <= 60`

## Input Format

A single line containing two integers separated by a space: `picks` and `goal`.

## Output Format

Print each valid group on its own line in bracket notation. Values within a line are in ascending order, separated by commas with no spaces, for example `[1,2,4]`. Lines appear in lexicographic order as described above.

If there are no valid groups, the output file is empty (zero lines). This is the only case with no lines, because `picks >= 2` means a group can never be empty.

For Example 2 the input file is:

```text
3 9
```

and the output file is:

```text
[1,2,6]
[1,3,5]
[2,3,4]
```

## Approach

Use backtracking over the values `1` through `9`. Keep a starting value so that each group is built in ascending order, which prevents duplicates and permutations of the same group. Track how many values are still needed and how much of the total is still left.

Two rules prune the search:

* When the group is complete, it counts only if the remaining total is exactly `0`.
* Values are tried in increasing order. Once a value is larger than the remaining total, every larger value is too, so the loop can stop.
