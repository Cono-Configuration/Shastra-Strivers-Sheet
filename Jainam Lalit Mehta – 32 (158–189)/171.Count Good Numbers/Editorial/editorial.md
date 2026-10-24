# Editorial: Problem 171 - Count Valid Digit Strings

## Difficulty

Medium

## Prerequisites

* Counting Principle (Multiplication Rule)
* Modular Arithmetic
* Binary Exponentiation
* Integer Overflow Awareness

## Problem Statement

A digit string is valid if the digits at even positions (0-indexed) are even and the digits at odd positions are prime (`2`, `3`, `5`, `7`). Given `length`, count the valid digit strings of that length, modulo `10^9 + 7`. Leading zeros are allowed.

## Input Format

A single line containing the integer `length`.

## Constraints

* `1 <= length <= 10^15`

## Output Format

Return the count of valid digit strings modulo `10^9 + 7`.

## Explanation & Approach

### Step 1: Count the Choices per Position

Each position can be filled independently:

```text
even position -> 0, 2, 4, 6, 8   -> 5 choices
odd position  -> 2, 3, 5, 7      -> 4 choices
```

### Step 2: Count the Positions of Each Kind

For a string of length `length` (positions `0` to `length - 1`):

```text
even positions = ceil(length / 2)  = (length + 1) / 2
odd positions  = floor(length / 2) = length / 2
```

### Step 3: Multiply the Choices

By the multiplication rule:

```text
answer = 5^((length + 1) / 2) * 4^(length / 2)   (mod 10^9 + 7)
```

Check with `length = 4`: there are `2` even and `2` odd positions, so `5^2 * 4^2 = 25 * 16 = 400`.

### Step 4: Fast Modular Exponentiation

A loop that multiplies `length / 2` times would need about `5 * 10^14` steps, which is impossible. Instead, use binary exponentiation:

* Keep `result = 1`.
* While `exp > 0`: if the lowest bit of `exp` is `1`, set `result = result * base % MOD`. Then set `base = base * base % MOD` and shift `exp` right by one.

This takes about `log2(10^15) ~ 50` iterations per power.

### Step 5: Avoid Overflow

* `length` can be up to `10^15`, so it must be stored in a `long` (an `int` overflows beyond about `2.1 * 10^9`).
* `base` and `result` are always below `10^9 + 7 < 2^30`, so a product is below `2^60` and fits in a `long`. Take the modulus after every multiplication.

## Example

### Input

```text
50
```

### Analysis

* even positions = `25`, odd positions = `25`.
* answer = `5^25 * 4^25 = 20^25`, taken modulo `10^9 + 7`.

### Output

```text
564908303
```

## Java Solution

```java
class Solution {

    private static final long MOD = 1_000_000_007L;

    public int countValidStrings(long length) {

        long evenSlots = (length + 1) / 2;
        long oddSlots = length / 2;

        long result = power(5, evenSlots) * power(4, oddSlots) % MOD;

        return (int) result;
    }

    private long power(long base, long exp) {

        long result = 1;
        base %= MOD;

        while (exp > 0) {

            if ((exp & 1) == 1) {
                result = result * base % MOD;
            }

            base = base * base % MOD;
            exp >>= 1;
        }

        return result;
    }
}
```

## Time Complexity

Two binary exponentiations, each running about `log2(length)` iterations.

**Time Complexity: `O(log length)`**

## Space Complexity

Only a few `long` variables are used.

**Space Complexity: `O(1)`**

## Key Takeaway

> Independent positions multiply: `5` choices for every even position and `4` for every odd one, giving `5^ceil(length/2) * 4^floor(length/2)`. Compute the powers with binary modular exponentiation, using `long` and a modulus after every multiplication.
