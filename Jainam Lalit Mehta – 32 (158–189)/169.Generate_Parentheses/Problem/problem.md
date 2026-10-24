# Problem 169: Generate Parentheses

**Difficulty:** Medium | **Tags:** String, Backtracking, Dynamic Programming

## Problem Statement

You are given an integer `pairs`, the number of pairs of round brackets. Produce every distinct string of length `2 * pairs` that forms a well-formed (balanced) arrangement of these brackets, and return all of them.

A string is well-formed if every opening bracket `(` is matched by a later closing bracket `)` and no closing bracket appears before its matching opening bracket.

The answer may be returned in any order. In the test files, the strings are listed one per line in lexicographic order (with `(` considered smaller than `)`).

## Example 1

```text
Input: pairs = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]
```

## Example 2

```text
Input: pairs = 1
Output: ["()"]
```

## Constraints

* `1 <= pairs <= 8`

## Output Format

Return a list containing every well-formed bracket string that can be built using exactly `pairs` pairs of brackets.

## Approach

Build each string one character at a time using backtracking, while tracking how many `(` and `)` have been placed so far.

* A `(` may be placed as long as fewer than `pairs` opening brackets have been used.
* A `)` may be placed only if the number of closing brackets used so far is smaller than the number of opening brackets used.
* When the string reaches length `2 * pairs`, it is a valid answer and is added to the result.

Trying `(` before `)` at every step naturally produces the strings in lexicographic order, and because invalid prefixes are never extended, no work is wasted on malformed strings.
