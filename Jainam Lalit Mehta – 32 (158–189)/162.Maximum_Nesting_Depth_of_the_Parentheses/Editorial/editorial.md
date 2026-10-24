# Editorial: Maximum Nesting Depth of the Parentheses

## Difficulty

Easy

## Prerequisites

* Strings
* Iteration
* Counters
* Basic Stack/Nesting Concepts

## Problem Statement

Given a valid parentheses string `s` that may also contain digits and the operators `+`, `-`, `*`, `/`, find its nesting depth, i.e. the largest number of parentheses that are open at the same time.

Return this maximum as an integer.

## Input Format

The input contains a single line with the string `s`.

## Constraints

* `1 <= s.length <= 100`
* `s` consists of digits `0-9` and the characters `'+'`, `'-'`, `'*'`, `'/'`, `'('` and `')'`.
* The parentheses in `s` form a valid parentheses string.

## Output Format

Return a single integer: the nesting depth of `s`.

## Explanation & Approach

### Step 1: Track How Many Parentheses Are Open

Maintain an integer variable `current` that stores the number of parentheses that are open right now, and another integer `maxDepth` that stores the largest value `current` has taken.

Initially:

```text
current  = 0
maxDepth = 0
```

### Step 2: Handle an Opening Parenthesis

When we see `'('`, one more parenthesis becomes open:

* Increase `current` by `1`.
* If `current` is now greater than `maxDepth`, update `maxDepth`.

### Step 3: Handle a Closing Parenthesis

When we see `')'`, the most recently opened parenthesis is closed:

* Decrease `current` by `1`.

Nothing needs to be recorded here, because closing can only lower the depth.

### Step 4: Ignore Everything Else

Digits and the operators `+`, `-`, `*`, `/` never change how many parentheses are open, so they are skipped.

### Step 5: Return the Answer

Because `s` is guaranteed to be a valid parentheses string, `current` never drops below `0` and finishes at `0`. After the last character, `maxDepth` holds the nesting depth.

### Why No Stack Is Needed

A stack would only be useful if we had to remember *which* parentheses are open. Here we only need *how many* are open, so a single integer is enough. This keeps the extra space constant.

## Example

### Input

```text
(7+(2*(4-1)))/3
```

### Analysis

| Character | Open count after it | `maxDepth` |
| --------- | ------------------- | ---------- |
| `(`       | 1                   | 1          |
| `7`       | 1                   | 1          |
| `+`       | 1                   | 1          |
| `(`       | 2                   | 2          |
| `2`       | 2                   | 2          |
| `*`       | 2                   | 2          |
| `(`       | 3                   | 3          |
| `4`       | 3                   | 3          |
| `-`       | 3                   | 3          |
| `1`       | 3                   | 3          |
| `)`       | 2                   | 3          |
| `)`       | 1                   | 3          |
| `)`       | 0                   | 3          |
| `/`       | 0                   | 3          |
| `3`       | 0                   | 3          |

The open count reaches its peak of `3` while reading `(4-1)`.

### Output

```text
3
```

## Java Solution

```java
class Solution {
    public int maxDepth(String s) {

        int current = 0;
        int maxDepth = 0;

        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            if (ch == '(') {
                current++;
                if (current > maxDepth) {
                    maxDepth = current;
                }
            } else if (ch == ')') {
                current--;
            }
        }

        return maxDepth;
    }
}
```

## Time Complexity

We traverse the string exactly once.

**Time Complexity: `O(N)`**

where `N` is the length of the input string.

## Space Complexity

Only the two integer variables `current` and `maxDepth` are used. No stack and no extra string are built.

**Space Complexity: `O(1)`**

## Key Takeaway

> The nesting depth of a valid parentheses string is the largest number of parentheses that are open at the same time. Count `+1` for every `(` and `-1` for every `)`, and remember the highest value reached. This gives the answer in a single pass with constant extra space.
