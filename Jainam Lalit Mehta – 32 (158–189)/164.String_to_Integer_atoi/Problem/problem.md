# Problem 164: String to Integer (atoi)

**Difficulty:** Medium | **Tags:** Strings, Math, Simulation

## Problem Statement

Implement the function `myAtoi(String s)`, which converts a string into a **32-bit signed integer**.

The function must follow these steps in order:

1. **Whitespace:** Skip all leading whitespace characters (`' '`).
2. **Sign:** Look at the next character. If it is `'-'`, the result will be negative; if it is `'+'`, the result will be positive. If it is neither, assume the result is positive. A sign character, when present, is consumed.
3. **Digits:** Read digit characters one after another and stop at the first non-digit character or at the end of the string. Leading zeros do not affect the value. If no digits were read at all, the result is `0`.
4. **Rounding:** If the value is outside the 32-bit signed integer range `[-2^31, 2^31 - 1]`, round it to the nearest end of the range. That is, values below `-2^31` become `-2^31`, and values above `2^31 - 1` become `2^31 - 1`.
5. Return the resulting integer.

**Note:** You must **not** use any built-in library function that converts a string to a number (such as `atoi`, `stoi`, `Integer.parseInt`, `int()`, `parseInt` or `Number()`). The conversion must be done manually.

## Example 1

```text
Input: s = "315"
Output: 315
```

**Explanation:**

```text
Step 1: no leading whitespace to skip.
Step 2: no '-' or '+', so the number is positive.
Step 3: "315" is read in.
```

Therefore, the answer is `315`.

## Example 2

```text
Input: s = "   +0072"
Output: 72
```

**Explanation:**

```text
Step 1: the three leading spaces are skipped.
Step 2: '+' is read, so the number is positive.
Step 3: "0072" is read in; the leading zeros do not change the value.
```

Therefore, the answer is `72`.

## Example 3

```text
Input: s = "908xyz12"
Output: 908
```

**Explanation:**

```text
Step 1: no leading whitespace to skip.
Step 2: no '-' or '+', so the number is positive.
Step 3: "908" is read in; reading stops at 'x' because it is not a digit.
```

Therefore, the answer is `908`.

## Example 4

```text
Input: s = "-91283472332"
Output: -2147483648
```

**Explanation:**

```text
Step 1: no leading whitespace to skip.
Step 2: '-' is read, so the number is negative.
Step 3: "91283472332" is read in, giving -91283472332.
Step 4: this is smaller than -2^31, so it is rounded to -2^31 = -2147483648.
```

Therefore, the answer is `-2147483648`.

## Example 5

```text
Input: s = "ice 77"
Output: 0
```

**Explanation:**

The first character `'i'` is not whitespace, not a sign and not a digit, so reading stops immediately and no digits are read. The `77` later in the string is never reached.

Therefore, the answer is `0`.

## Constraints

* `0 <= s.length <= 200`
* `s` consists of English letters (lower-case and upper-case), digits (`0-9`), `' '`, `'+'`, `'-'` and `'.'`.

## Output Format

Return a single 32-bit signed integer: the converted value after rounding.

## Approach

Walk through the string once with an index `i`:

1. Move `i` forward while `s[i]` is a space.
2. If `s[i]` is `'+'` or `'-'`, remember the sign and move `i` forward.
3. While `s[i]` is a digit, append it to `result` using `result = result * 10 + digit`.
4. Before every append, check whether `result * 10 + digit` would exceed the 32-bit range. If it would, immediately return `2^31 - 1` (for a positive number) or `-2^31` (for a negative number).
5. Stop at the first non-digit character or at the end of the string, and return `sign * result`.

The overflow test is done **before** multiplying, using only `int` arithmetic: it is enough to compare `result` with `Integer.MAX_VALUE / 10` and, when they are equal, the next digit with `7`.

The string is scanned once and only a few integer variables are used.
