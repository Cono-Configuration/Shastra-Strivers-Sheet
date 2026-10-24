# Problem 163: Roman to Integer

**Difficulty:** Easy | **Tags:** Strings, Hash Table, Math

## Problem Statement

Roman numerals are written using seven different symbols: `I`, `V`, `X`, `L`, `C`, `D` and `M`.

```text
Symbol    Value
I         1
V         5
X         10
L         50
C         100
D         500
M         1000
```

For example, the number `2` is written as `II`, which is just two ones added together. The number `12` is written as `XII`, which is `X + II`. The number `27` is written as `XXVII`, which is `XX + V + II`.

Roman numerals are normally written from the largest symbol to the smallest symbol, from left to right. However, the numeral for four is **not** `IIII`. Instead, four is written as `IV`: because the `I` stands before the `V`, it is subtracted, giving `5 - 1 = 4`. The same idea is used for nine, which is written as `IX`.

There are exactly six cases in which subtraction is used:

* `I` can be placed before `V` (5) and `X` (10) to make `4` and `9`.
* `X` can be placed before `L` (50) and `C` (100) to make `40` and `90`.
* `C` can be placed before `D` (500) and `M` (1000) to make `400` and `900`.

Given a Roman numeral `s`, convert it into an integer.

## Example 1

```text
Input: s = "XXIV"
Output: 24
```

**Explanation:**

```text
X + X + IV = 10 + 10 + 4 = 24
```

The `I` stands before the larger symbol `V`, so it is subtracted (`IV = 4`).

Therefore, the answer is `24`.

## Example 2

```text
Input: s = "DCCLXXXIX"
Output: 789
```

**Explanation:**

```text
D + CC + L + XXX + IX = 500 + 200 + 50 + 30 + 9 = 789
```

Only the final `IX` uses subtraction. Every other symbol is simply added.

Therefore, the answer is `789`.

## Example 3

```text
Input: s = "MMCDXCIV"
Output: 2494
```

**Explanation:**

```text
MM + CD + XC + IV = 2000 + 400 + 90 + 4 = 2494
```

Here three different subtractive pairs appear: `CD = 400`, `XC = 90` and `IV = 4`.

Therefore, the answer is `2494`.

## Constraints

* `1 <= s.length <= 15`
* `s` contains only the characters `'I'`, `'V'`, `'X'`, `'L'`, `'C'`, `'D'` and `'M'`.
* It is guaranteed that `s` is a valid Roman numeral representing a value in the range `[1, 3999]`.

## Output Format

Return a single integer: the value represented by the Roman numeral `s`.

## Approach

Process the string from **right to left**, keeping the value of the symbol that was just processed in a variable `prev`, and a running `total`.

For each symbol with value `cur`:

* If `cur < prev`, the symbol stands before a larger one, so it is part of a subtractive pair. **Subtract** `cur` from `total`.
* Otherwise, **add** `cur` to `total`.
* Set `prev = cur` and continue.

After the leftmost symbol has been processed, `total` is the answer.

The string is scanned once and only a few integer variables are used.
