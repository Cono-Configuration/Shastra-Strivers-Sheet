# Problem 171: Count Valid Digit Strings

**Difficulty:** Medium | **Tags:** Recursion, Math, Modular Exponentiation

## Problem Statement

A digit string is called **valid** if the digits at even positions (0-indexed) are even digits and the digits at odd positions (0-indexed) are prime digits (`2`, `3`, `5` or `7`).

For example, `"2582"` is valid because the digits at the even positions (`2` and `8`) are even and the digits at the odd positions (`5` and `2`) are prime. On the other hand, `"3245"` is not valid because `3` sits at an even position but is not an even digit.

Given an integer `length`, return how many valid digit strings of exactly `length` characters exist. Since the answer can be very large, return it modulo `10^9 + 7`.

A digit string is a string made only of the digits `0` through `9`, and it may contain leading zeros.

## Example 1

```text
Input: length = 1
Output: 5
```

**Explanation:**

The valid strings of length 1 are `"0"`, `"2"`, `"4"`, `"6"` and `"8"`.

## Example 2

```text
Input: length = 4
Output: 400
```

## Example 3

```text
Input: length = 50
Output: 564908303
```

## Input Format

A single line containing the integer `length`.

## Constraints

* `1 <= length <= 10^15`

## Output Format

Return the number of valid digit strings of length `length`, modulo `10^9 + 7`.

## Approach

Positions are independent of each other:

* Every even position can hold any of the `5` even digits (`0, 2, 4, 6, 8`).
* Every odd position can hold any of the `4` prime digits (`2, 3, 5, 7`).

A string of length `length` has `ceil(length / 2)` even positions and `floor(length / 2)` odd positions, so the answer is:

```text
5^ceil(length / 2) * 4^floor(length / 2)   (mod 10^9 + 7)
```

Since `length` can be as large as `10^15`, the powers must be computed with fast (binary) modular exponentiation, which needs only about `50` steps. Use `long` for every intermediate product, and reduce modulo `10^9 + 7` after each multiplication.
