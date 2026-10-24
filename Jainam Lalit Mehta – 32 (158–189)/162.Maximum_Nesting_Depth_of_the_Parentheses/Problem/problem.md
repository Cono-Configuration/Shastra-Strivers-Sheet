# Problem 162: Maximum Nesting Depth of the Parentheses

**Difficulty:** Easy | **Tags:** Strings, Stack, Counting

## Problem Statement

A string is called a **valid parentheses string (VPS)** if it satisfies any one of the following rules:

* It is an empty string `""`, or a single character that is neither `'('` nor `')'`.
* It can be written as `A + B`, where `A` and `B` are both VPS, and `+` represents string concatenation.
* It can be written as `"(" + A + ")"`, where `A` is a VPS.

The **nesting depth** `depth(S)` of a VPS `S` is defined as follows:

* `depth("") = 0`
* `depth(C) = 0`, where `C` is a single character that is not a parenthesis.
* `depth(A + B) = max(depth(A), depth(B))`, where `A` and `B` are VPS.
* `depth("(" + A + ")") = 1 + depth(A)`, where `A` is a VPS.

For example, `""`, `"2*3"`, `"(1)(2)"` and `"(4+(5-(6)))"` are VPS, while `")("` and `"(7+"` are **not**.

Given a string `s` that is a VPS, return the **nesting depth** of `s`, i.e. the largest number of parentheses that are open at the same time at any position in `s`.

## Example 1

```text
Input: s = "(7+(2*(4-1)))/3"
Output: 3
```

**Explanation:**

Reading the string from left to right, the number of currently open parentheses (after each character) changes as follows:

```text
Character  :  (  7  +  (  2  *  (  4  -  1  )  )  )  /  3
Open count :  1  1  1  2  2  2  3  3  3  3  2  1  0  0  0
```

The characters `4`, `-` and `1` sit inside three open parentheses, and no position is inside more than three.

Therefore, the nesting depth is `3`.

## Example 2

```text
Input: s = "()(()())(())"
Output: 2
```

**Explanation:**

The string splits into three groups:

```text
"()" + "(()())" + "(())"
```

Their depths are `1`, `2` and `2` respectively, so the nesting depth of the whole string is the largest of them.

Therefore, the nesting depth is `2`.

## Example 3

```text
Input: s = "8*5-2+9"
Output: 0
```

**Explanation:**

The string contains no parentheses at all, so no position is ever inside an open parenthesis.

Therefore, the nesting depth is `0`.

## Constraints

* `1 <= s.length <= 100`
* `s` consists of digits `0-9` and the characters `'+'`, `'-'`, `'*'`, `'/'`, `'('` and `')'`.
* It is guaranteed that the parentheses in `s` form a valid parentheses string (VPS).

## Output Format

Return a single integer: the nesting depth of `s`.

## Approach

Maintain a counter `current` that stores how many parentheses are open at the moment, and a variable `maxDepth` that stores the largest value `current` has ever reached.

While traversing `s`:

* When `'('` is encountered, increase `current` and update `maxDepth` if `current` is now larger.
* When `')'` is encountered, decrease `current`.
* Digits and operators are ignored.

Since `s` is guaranteed to be a VPS, `current` never becomes negative and ends at `0`. The value of `maxDepth` after the last character is the answer.

This computes the result in a single pass using only two integer variables.
