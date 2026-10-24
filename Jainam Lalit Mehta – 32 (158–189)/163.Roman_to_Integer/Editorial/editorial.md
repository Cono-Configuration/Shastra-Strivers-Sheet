# Editorial: Roman to Integer

## Difficulty

Easy

## Prerequisites

* Strings
* Iteration
* Character-to-value mapping
* Basic Math

## Problem Statement

Given a valid Roman numeral `s` built from the symbols `I`, `V`, `X`, `L`, `C`, `D` and `M`, convert it into the integer it represents.

Symbols are normally added from the largest to the smallest, except in six subtractive pairs: `IV`, `IX`, `XL`, `XC`, `CD` and `CM`, where a smaller symbol written before a larger one is subtracted.

## Input Format

The input contains a single line with the string `s`.

## Constraints

* `1 <= s.length <= 15`
* `s` contains only the characters `'I'`, `'V'`, `'X'`, `'L'`, `'C'`, `'D'` and `'M'`.
* `s` is a valid Roman numeral in the range `[1, 3999]`.

## Output Format

Return a single integer: the value of the Roman numeral.

## Explanation & Approach

### Step 1: Know the Value of Each Symbol

```text
I = 1    V = 5    X = 10    L = 50
C = 100  D = 500  M = 1000
```

### Step 2: Spot the Only Rule That Matters

A symbol is **subtracted** exactly when the symbol on its **right** has a larger value. In every other case it is **added**.

This single rule covers all six subtractive pairs, so there is no need to treat `IV`, `IX`, `XL`, `XC`, `CD` and `CM` as special cases.

### Step 3: Scan from Right to Left

Scanning from the right means that, when we look at a symbol, we already know the symbol that follows it. Keep:

```text
total = 0   (the running answer)
prev  = 0   (value of the symbol processed just before)
```

### Step 4: Add or Subtract

For the symbol with value `cur`:

* If `cur < prev`, do `total -= cur`.
* Otherwise, do `total += cur`.

Then set `prev = cur`.

Equal neighbours (such as the two `I`s in `II`) satisfy `cur == prev`, so they are correctly added.

### Step 5: Return the Answer

After the leftmost symbol is processed, `total` holds the value of the whole numeral.

## Example

### Input

```text
MMCDXCIV
```

### Analysis (right to left)

| Symbol read | `cur` | `prev` | Action          | `total` |
| ----------- | ----- | ------ | --------------- | ------- |
| `V`         | 5     | 0      | add 5           | 5       |
| `I`         | 1     | 5      | subtract 1      | 4       |
| `C`         | 100   | 1      | add 100         | 104     |
| `X`         | 10    | 100    | subtract 10     | 94      |
| `D`         | 500   | 10     | add 500         | 594     |
| `C`         | 100   | 500    | subtract 100    | 494     |
| `M`         | 1000  | 100    | add 1000        | 1494    |
| `M`         | 1000  | 1000   | add 1000        | 2494    |

### Output

```text
2494
```

## Java Solution

```java
class Solution {
    public int romanToInt(String s) {

        int total = 0;
        int prev = 0;

        for (int i = s.length() - 1; i >= 0; i--) {

            int cur = value(s.charAt(i));

            if (cur < prev) {
                total -= cur;
            } else {
                total += cur;
            }

            prev = cur;
        }

        return total;
    }

    private int value(char ch) {

        switch (ch) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default:  return 0;
        }
    }
}
```

## Time Complexity

We look at every character exactly once.

**Time Complexity: `O(N)`**

where `N` is the length of the string (at most 15).

## Space Complexity

Only a few integer variables are used. The symbol values come from a `switch`, so no map or extra array is built.

**Space Complexity: `O(1)`**

## Key Takeaway

> In a Roman numeral, a symbol is subtracted when a larger symbol comes right after it, and added otherwise. Scanning from right to left and comparing each symbol with the one just processed turns this rule into a simple single pass with constant extra space.
