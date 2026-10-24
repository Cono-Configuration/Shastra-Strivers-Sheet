# Editorial: 169. Generate Parentheses

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Strings / StringBuilder
* Catalan Numbers (for complexity)

## Problem Statement

Given an integer `pairs`, generate all combinations of well-formed round brackets that use exactly `pairs` pairs.

## Input Format

The input contains a single integer `pairs`.

## Constraints

* `1 <= pairs <= 8`

## Output Format

Return all well-formed bracket strings of length `2 * pairs`. In the test files, each string is printed on its own line in lexicographic order.

## Explanation & Approach

### Step 1: Think of Building the String Character by Character

At every position we choose either `(` or `)`. Blindly trying both gives `2^(2 * pairs)` strings, most of which are invalid. Instead, we only make choices that keep the current prefix valid.

### Step 2: Track Two Counters

Keep:

```text
opened = number of '(' placed so far
closed = number of ')' placed so far
```

### Step 3: Rules for Choosing the Next Character

* Place `(` only if `opened < pairs`.
* Place `)` only if `closed < opened` (there is an unmatched `(` to close).

These two rules guarantee that every prefix is valid and that every completed string is balanced.

### Step 4: Base Case

When the current string has length `2 * pairs`, it is complete. Add it to the answer.

### Step 5: Backtrack

After exploring a choice, remove the last character and try the next option. Always try `(` first, which produces the answers in lexicographic order.

### Walkthrough

```text
pairs = 2
```

```text
""
├── "("
│   ├── "(("
│   │   └── "(()" → "(())"   ✔
│   └── "()"
│       └── "()(" → "()()"   ✔
```

The result is `["(())", "()()"]`.

## Example

### Input

```text
3
```

### Output

```text
((()))
(()())
(())()
()(())
()()()
```

## Java Solution

```java
class Solution {
    public List<String> generateParenthesis(int pairs) {

        List<String> result = new ArrayList<>();
        build(result, new StringBuilder(), 0, 0, pairs);

        return result;
    }

    private void build(List<String> result, StringBuilder current, int opened, int closed, int pairs) {

        if (current.length() == 2 * pairs) {
            result.add(current.toString());
            return;
        }

        if (opened < pairs) {
            current.append('(');
            build(result, current, opened + 1, closed, pairs);
            current.deleteCharAt(current.length() - 1);
        }

        if (closed < opened) {
            current.append(')');
            build(result, current, opened, closed + 1, pairs);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
```

## Time Complexity

The number of valid strings is the `pairs`-th Catalan number `C(pairs) = (1 / (pairs + 1)) * C(2 * pairs, pairs)`, and each string has length `2 * pairs`.

**Time Complexity: `O(4^N / sqrt(N))`** where `N = pairs`, since the Catalan number grows like `4^N / (N * sqrt(N))` and each string costs `O(N)` to build.

## Space Complexity

The recursion depth is `2 * N`, and the current string uses `O(N)` space (the output itself is not counted).

**Space Complexity: `O(N)`**

## Key Takeaway

> Build the string one bracket at a time, but only add `(` while some are left to use and only add `)` while there is an unmatched `(`. This prunes every invalid prefix and generates exactly the Catalan-number many well-formed strings.
