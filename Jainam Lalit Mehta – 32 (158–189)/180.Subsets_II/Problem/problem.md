# Problem 178: Subsets II

**Difficulty:** Medium | **Tags:** Array, Backtracking, Bit Manipulation

## Problem Statement

You are handed a list of integers called `items`. The list may contain repeated values. Work out every distinct sub-collection that can be formed by picking some of its entries, where picking none of them (the empty sub-collection) and picking all of them are both allowed. Collect all of these sub-collections into one answer.

The answer must not contain the same sub-collection twice, even when it can be formed by picking entries in more than one way.

The answer may be returned in any order. In the test files, the sub-collections are listed one per line in lexicographic order (comparing the sorted values element by element, where a shorter sequence that is a prefix of a longer one comes first).

## Example 1

```text
Input: items = [1,2,2]
Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
```

## Example 2

```text
Input: items = [0]
Output: [[],[0]]
```

## Constraints

* `1 <= items.length <= 10`
* `-10 <= items[i] <= 10`

## Input Format

The first line contains a single integer `n`, the number of entries in `items`.
The second line contains `n` integers separated by single spaces.

## Output Format

Print each distinct sub-collection on its own line, in bracket notation. Within a line, the values appear in non-decreasing order, separated by commas with no spaces. The empty sub-collection is printed as `[]`. Lines appear in lexicographic order as described above.

For Example 1 the input file is:

```text
3
1 2 2
```

and the output file is:

```text
[]
[1]
[1,2]
[1,2,2]
[2]
[2,2]
```

## Approach

Sort the values first, so that equal values sit next to each other. Then build sub-collections with backtracking: at each step, either stop and record the current sub-collection, or extend it with a value that comes at or after the current position.

The duplicate rule is the key point. When the loop is choosing the next value at a given depth, any value that equals the one just before it at that same depth has already been tried, so it is skipped. Without this rule, identical sub-collections would be produced more than once.

Because the values are sorted and each subset is recorded as soon as it is created, the sub-collections come out in lexicographic order automatically.
