# Editorial: String to Integer (atoi)

## Difficulty

Medium

## Prerequisites

* Strings
* Iteration
* Digit-by-digit number building
* Integer overflow

## Problem Statement

Convert a string into a 32-bit signed integer by following these rules in order: skip leading spaces, read an optional `'+'` or `'-'` sign, read consecutive digits, and round the value to `[-2^31, 2^31 - 1]` if it is out of range.

If no digits are read, the answer is `0`. Built-in string-to-number functions must not be used.

## Input Format

The input contains a single line with the string `s`. The line may be empty, and it may start or end with spaces.

## Constraints

* `0 <= s.length <= 200`
* `s` consists of English letters, digits, `' '`, `'+'`, `'-'` and `'.'`.

## Output Format

Return a single 32-bit signed integer.

## Explanation & Approach

### Step 1: Skip Leading Spaces

Keep an index `i` starting at `0`. While `i` is inside the string and `s[i]` is a space, move `i` forward.

Only **leading** spaces are skipped. A space that appears later (for example after the sign, or between digits) is an ordinary non-digit character.

### Step 2: Read the Sign

If `s[i]` is `'-'`, set `sign = -1` and move `i` forward. If it is `'+'`, keep `sign = 1` and move `i` forward. Otherwise leave `sign = 1`.

Only **one** sign character is consumed. In `"-+4"` the `'+'` is not a digit, so nothing is read and the answer is `0`.

### Step 3: Read Digits

While `i` is inside the string and `s[i]` is a digit, convert it with `digit = s[i] - '0'` and update:

```text
result = result * 10 + digit
```

Stop at the first non-digit character. Leading zeros need no special handling because they simply keep `result` at `0`.

### Step 4: Detect Overflow Before It Happens

`result * 10 + digit` may not fit in a 32-bit `int`, so the check is made **before** the update. Let `LIMIT = Integer.MAX_VALUE / 10 = 214748364`. The update is unsafe when:

* `result > LIMIT`, or
* `result == LIMIT` and `digit > 7` (because `Integer.MAX_VALUE = 2147483647` ends in `7`).

In that case return `Integer.MAX_VALUE` for a positive number, or `Integer.MIN_VALUE` for a negative one. For the negative case, a value of exactly `-2147483648` also lands on `Integer.MIN_VALUE`, which is the correct answer.

### Step 5: Return the Answer

If the loop finishes without overflow, return `sign * result`. If no digit was read, `result` is still `0`, so `0` is returned.

## Example

### Input

```text
   +0072
```

### Analysis

| Position `i` | Character | Action               | `sign` | `result` |
| ------------ | --------- | -------------------- | ------ | -------- |
| 0            | `' '`     | skip space           | 1      | 0        |
| 1            | `' '`     | skip space           | 1      | 0        |
| 2            | `' '`     | skip space           | 1      | 0        |
| 3            | `'+'`     | read sign, positive  | 1      | 0        |
| 4            | `'0'`     | digit 0              | 1      | 0        |
| 5            | `'0'`     | digit 0              | 1      | 0        |
| 6            | `'7'`     | digit 7              | 1      | 7        |
| 7            | `'2'`     | digit 2              | 1      | 72       |

The end of the string is reached, so the answer is `sign * result = 72`.

### Overflow Example

For `"-91283472332"` the sign is negative and `result` grows as `9, 91, 912, ..., 91283472, 912834723`. Before the next digit is added, `912834723 > 214748364`, so the function returns `Integer.MIN_VALUE = -2147483648` without ever computing the overflowing value.

### Output

```text
72
```

## Java Solution

```java
class Solution {
    public int myAtoi(String s) {

        int n = s.length();
        int i = 0;

        // Step 1: skip leading spaces
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        // Step 2: read the sign
        int sign = 1;

        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            if (s.charAt(i) == '-') {
                sign = -1;
            }
            i++;
        }

        // Step 3 and 4: read digits, rounding on overflow
        int result = 0;

        while (i < n) {

            char ch = s.charAt(i);

            if (ch < '0' || ch > '9') {
                break;
            }

            int digit = ch - '0';

            if (result > Integer.MAX_VALUE / 10
                    || (result == Integer.MAX_VALUE / 10 && digit > 7)) {
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            result = result * 10 + digit;
            i++;
        }

        return sign * result;
    }
}
```

## Time Complexity

Every character is looked at at most once.

**Time Complexity: `O(N)`**

where `N` is the length of the string (at most 200).

## Space Complexity

Only a few integer variables are used. No `long`, no `BigInteger` and no extra string are needed.

**Space Complexity: `O(1)`**

## Key Takeaway

> Parsing a number by hand is a short state machine: spaces, then an optional sign, then digits. The only tricky part is overflow, which is handled by comparing `result` with `Integer.MAX_VALUE / 10` before multiplying, so the answer is rounded correctly without ever leaving the `int` range.
