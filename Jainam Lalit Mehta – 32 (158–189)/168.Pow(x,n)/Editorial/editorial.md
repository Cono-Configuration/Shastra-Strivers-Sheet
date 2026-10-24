# Editorial: Problem 168 - Raise Base to Exponent

## Difficulty

Medium

## Prerequisites

* Math Basics (Exponents and Reciprocals)
* Bit Manipulation
* Iteration / Recursion
* Floating-Point Numbers

## Problem Statement

Given a real number `base` and an integer `exp`, compute `base` raised to the power `exp` (`base^exp`) without using a built-in power function.

A negative exponent means the reciprocal of the positive power, and any non-zero base raised to the power `0` is `1`.

## Input Format

The input contains a single line with two space-separated values: the real number `base` (at most 5 digits after the decimal point) and the integer `exp`.

## Constraints

* `-100.0 < base < 100.0`
* `-2^31 <= exp <= 2^31 - 1`
* `exp` is an integer.
* Either `base` is not zero or `exp > 0`.
* `-10^4 <= base^exp <= 10^4`

## Output Format

Return the value of `base^exp`. When printing, round it to exactly `5` digits after the decimal point. If the rounded value is zero, print `0.00000` (never `-0.00000`).

## Explanation & Approach

### Step 1: Why the Naive Loop Fails

The obvious approach multiplies `base` by itself `|exp|` times.

```text
base^exp = base * base * ... * base   (|exp| times)
```

Since `|exp|` can be as large as `2^31`, this means about `2 * 10^9` multiplications, which is too slow.

### Step 2: Use the Binary Form of the Exponent

Every exponent can be written as a sum of powers of two. For example:

```text
10 = 8 + 2   (binary 1010)
base^10 = base^8 * base^2
```

We can obtain `base^1, base^2, base^4, base^8, ...` by repeatedly squaring `base`, and we only multiply the ones that correspond to a `1` bit in the exponent. This technique is called **binary exponentiation** (square-and-multiply).

The loop works like this:

* Keep `result = 1`.
* While the exponent is greater than `0`:
  * If the lowest bit of the exponent is `1`, multiply `result` by the current `base`.
  * Square `base`.
  * Shift the exponent right by one bit.

### Step 3: Handle Negative Exponents

For a negative exponent:

```text
base^(-k) = (1 / base)^k
```

So when `exp < 0`, replace `base` by `1 / base` and continue with `k = -exp`. The constraints guarantee that `base` is non-zero whenever `exp <= 0`, so the division is always safe.

### Step 4: Beware of Overflow on `-exp`

The smallest allowed exponent is `-2^31 = -2147483648`. Its negation, `2147483648`, does **not** fit in a 32-bit `int`, so `-exp` silently stays `-2147483648`. This breaks the loop and returns a wrong answer.

The fix is to copy the exponent into a `long` **before** negating it:

```text
long power = exp;
if (power < 0) { base = 1 / base; power = -power; }
```

### Step 5: Output Formatting

* Print with exactly `5` digits after the decimal point.
* A very small negative result such as `-0.0000001` (or `-0.0`) must be printed as `0.00000`.
* Results can underflow to `0` (for example `2^(-2147483648)`), which is a valid answer.

## Example

### Input

```text
2.00000 10
```

### Analysis

`exp = 10` is `1010` in binary.

| Step | Lowest bit of exp | base (before square) | result |
| ---- | ----------------- | -------------------- | ------ |
| 1    | 0 (exp = 10)      | 2                    | 1      |
| 2    | 1 (exp = 5)       | 4                    | 4      |
| 3    | 0 (exp = 2)       | 16                   | 4      |
| 4    | 1 (exp = 1)       | 256                  | 1024   |

After the last step the exponent becomes `0` and the loop stops.

### Output

```text
1024.00000
```

## Java Solution

```java
class Solution {
    public double raisePower(double base, int exp) {

        long power = exp;

        if (power < 0) {
            base = 1.0 / base;
            power = -power;
        }

        double result = 1.0;

        while (power > 0) {

            if ((power & 1) == 1) {
                result *= base;
            }

            base *= base;
            power >>= 1;
        }

        return result;
    }
}
```

## Alternative: Recursive Approach

```text
pow(base, 0) = 1
pow(base, k) = half * half            if k is even, where half = pow(base, k / 2)
pow(base, k) = half * half * base     if k is odd
```

This performs the same work with a recursion depth of at most about `32`.

## Time Complexity

Each iteration halves the exponent, so the loop runs about `log2(|exp|)` times.

**Time Complexity: `O(log |exp|)`**

## Space Complexity

Only a few variables (`power`, `base`, `result`) are used.

**Space Complexity: `O(1)`**

(The recursive version uses `O(log |exp|)` stack space.)

## Key Takeaway

> Do not multiply `exp` times. Square the base and look at the exponent bit by bit, multiplying into the answer only when the bit is `1`. Handle negative exponents by inverting the base, and always widen `exp` to a `long` before negating it to avoid the `-2^31` overflow.
