# Problem 161: Remove Outermost Parentheses

**Difficulty:** Easy | **Tags:** Strings, Stack, String Manipulation

## Problem Statement

A valid parentheses string is either an empty string `""`, `"(" + X + ")"`, or `X + Y`, where `X` and `Y` are valid parentheses strings, and `+` represents string concatenation.

For example, `""`, `"()"`, `"(())()"`, and `"(()(()))"` are all valid parentheses strings.

A valid parentheses string `S` is called **primitive** if it is non-empty and there is no way to split it into `S = X + Y`, where `X` and `Y` are both non-empty valid parentheses strings.

Given a valid parentheses string `S`, consider its primitive decomposition:

`S = P1 + P2 + ... + Pk`

where every `Pi` is a primitive valid parentheses string.

Return `S` after removing the **outermost parentheses of every primitive string** in its primitive decomposition.

## Example 1

```text
Input: S = "(()())(())"
Output: "()()()"
```

**Explanation:**

The input string is `"(()())(())"`.

Its primitive decomposition is:

```text
"(()())" + "(())"
```

After removing the outermost parentheses from each primitive part:

```text
"()()" + "()"
```

Therefore, the resulting string is:

```text
"()()()"
```

## Example 2

```text
Input: S = "(()())(())(()(()))"
Output: "()()()()(())"
```

**Explanation:**

The input string is `"(()())(())(()(()))"`.

Its primitive decomposition is:

```text
"(()())" + "(())" + "(()(()))"
```

After removing the outermost parentheses from every primitive part:

```text
"()()" + "()" + "()(())"
```

Therefore, the resulting string is:

```text
"()()()()(())"
```

## Example 3

```text
Input: S = "()()"
Output: ""
```

**Explanation:**

The input string is `"()()"`.

Its primitive decomposition is:

```text
"()" + "()"
```

After removing the outermost parentheses from each primitive part:

```text
"" + ""
```

Therefore, the resulting string is an empty string.

## Constraints

* `1 <= S.length <= 10^5`
* `S[i]` is either `'('` or `')'`.
* `S` is a valid parentheses string.

## Output Format

Return the string obtained after removing the outermost parentheses from every primitive string in the primitive decomposition of `S`.

## Approach

Maintain a counter `depth` to keep track of the current level of nested parentheses.

While traversing `S`:

* When `'('` is encountered, add it to the result only if `depth > 0`, then increase `depth`.
* When `')'` is encountered, decrease `depth` first, and add it to the result only if `depth > 0`.

The parentheses skipped when `depth` changes between `0` and `1` are exactly the outermost parentheses of each primitive string.

This allows us to construct the required result while traversing the string only once.
