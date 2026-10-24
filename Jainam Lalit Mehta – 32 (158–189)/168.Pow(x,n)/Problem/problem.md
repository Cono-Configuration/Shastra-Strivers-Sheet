# Problem 168: Raise Base to Exponent

**Difficulty:** Medium | **Tags:** Binary Exponentiation, Recursion, Math

## Problem Statement

You are given a real number `base` and an integer `exp`. Write a function that computes the value of `base` raised to the power `exp` (that is, `base^exp`) without relying on a built-in power function.

A negative exponent means the reciprocal of the positive power, i.e. `base^(-k) = 1 / (base^k)`, and any non-zero `base` raised to the power `0` equals `1`.

## Example 1

```text
Input: base = 2.00000, exp = 10
Output: 1024.00000
```

## Example 2

```text
Input: base = 2.10000, exp = 3
Output: 9.26100
```

## Example 3

```text
Input: base = 2.00000, exp = -2
Output: 0.25000
```

**Explanation:**

A negative exponent means we take the reciprocal of the positive power:

```text
2^(-2) = 1 / 2^2 = 1 / 4 = 0.25
```

## Input Format

A single line containing two space-separated values: the real number `base` (with at most 5 digits after the decimal point) followed by the integer `exp`.

## Constraints

* `-100.0 < base < 100.0`
* `-2^31 <= exp <= 2^31 - 1`
* `exp` is an integer.
* Either `base` is not zero or `exp > 0`.
* `-10^4 <= base^exp <= 10^4`

## Output Format

Return the value of `base^exp`.

When printing, show the answer rounded to exactly `5` digits after the decimal point. If the rounded answer is zero, print `0.00000` (never `-0.00000`).

## Approach

Multiplying `base` by itself `exp` times would need up to about `2 * 10^9` multiplications, which is far too slow. Instead, use the binary representation of the exponent (square-and-multiply):

* Convert `exp` to a `long` first, so that `-2^31` can be negated safely.
* If `exp` is negative, replace `base` with `1 / base` and use `|exp|` as the exponent.
* Keep a `result` initialised to `1`. While the exponent is greater than `0`:
  * If the lowest bit of the exponent is `1`, multiply `result` by the current `base`.
  * Square `base` and shift the exponent right by one bit.

Each iteration halves the exponent, so the loop runs at most about `32` times.
