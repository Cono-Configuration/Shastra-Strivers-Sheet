# Problem 174: Binary Strings With No Adjacent Ones

**Difficulty:** Medium | **Tags:** Backtracking, Recursion, String

## Problem Statement

You are given an integer `length`. Produce every binary string of exactly `length` characters in which no two `'1'` characters are next to each other. The strings must be returned in lexicographically increasing order.

A binary string is a string made only of the characters `'0'` and `'1'`.

## Example 1

```text
Input: length = 3
Output: ["000", "001", "010", "100", "101"]
```

**Explanation:**

All of these strings have length 3 and none of them contains two consecutive `1`s.

## Example 2

```text
Input: length = 2
Output: ["00", "01", "10"]
```

## Input Format

A single line containing the integer `length`.

## Constraints

* `1 <= length <= 18`

## Output Format

Print all valid strings on a single line in lexicographically increasing order, in the form `["000", "001", "010"]`: every string in double quotes, separated by a comma and one space, with the whole list in square brackets.

## Approach

Build the strings one character at a time with backtracking:

* If the current string already has `length` characters, store it.
* Otherwise first try to append `'0'` (always allowed), then try to append `'1'` (allowed only when the previous character is not `'1'`).
* Trying `'0'` before `'1'` at every position produces the strings in increasing lexicographic order automatically.

The number of valid strings follows the Fibonacci sequence (`2, 3, 5, 8, ...` for `length = 1, 2, 3, 4, ...`), so for `length = 18` there are `6765` strings.
