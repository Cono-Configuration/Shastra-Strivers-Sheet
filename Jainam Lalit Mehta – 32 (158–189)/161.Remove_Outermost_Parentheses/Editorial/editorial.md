# Editorial: Remove Outermost Parentheses

## Difficulty

Easy

## Prerequisites

* Strings
* Iteration
* StringBuilder
* Basic Stack/Nesting Concepts

## Problem Statement

Given a valid parentheses string `S`, divide it into its primitive valid parentheses strings. For every primitive string, remove its outermost opening and closing parentheses.

Return the resulting string after processing all primitive components.

## Input Format

The input contains a single valid parentheses string `S`.

## Constraints

* `1 <= S.length <= 100000`
* `S[i]` is either `'('` or `')'`.
* `S` is a valid parentheses string.

## Output Format

Return the string obtained after removing the outermost parentheses from every primitive component of `S`.

## Explanation & Approach

### Step 1: Track the Nesting Level

Maintain an integer variable `depth` to keep track of how deeply nested we currently are.

Initially:

```text
depth = 0
```

When we encounter `'('`, the nesting level increases.

When we encounter `')'`, the nesting level decreases.

### Step 2: Identify the Outer Opening Parenthesis

For an opening parenthesis `'('`:

* If `depth == 0`, it is the outermost opening parenthesis of a primitive string, so we skip it.
* If `depth > 0`, it is an inner parenthesis, so we add it to the result.
* After processing it, increase `depth`.

For example:

```text
S = "(()())"
```

The first `'('` occurs when `depth = 0`, so it is skipped.

### Step 3: Identify the Outer Closing Parenthesis

For a closing parenthesis `')'`:

* First decrease `depth`.
* If `depth == 0`, this is the outermost closing parenthesis, so we skip it.
* If `depth > 0`, it is an inner parenthesis, so we add it to the result.

This allows us to remove the outermost pair without explicitly separating the string into primitive components.

### Step 4: Construct the Result

Consider:

```text
S = "(()())(())"
```

The primitive decomposition is:

```text
"(()())" + "(())"
```

After removing the outermost parentheses:

```text
"(()())" → "()()"
"(())"   → "()"
```

Therefore:

```text
"()()" + "()" = "()()()"
```

The final result is:

```text
()()()
```

## Example

### Input

```text
(()())(())
```

### Analysis

* The first primitive string is `(()())`.

  * Remove its outermost `(` and `)`.
  * Result: `()()`

* The second primitive string is `(())`.

  * Remove its outermost `(` and `)`.
  * Result: `()`

Combining both results:

```text
()() + () = ()()()
```

### Output

```text
()()()
```

## Java Solution

```java
class Solution {
    public String removeOuterParentheses(String S) {

        StringBuilder result = new StringBuilder();
        int depth = 0;

        for (char ch : S.toCharArray()) {

            if (ch == '(') {

                if (depth > 0) {
                    result.append(ch);
                }

                depth++;

            } else {

                depth--;

                if (depth > 0) {
                    result.append(ch);
                }
            }
        }

        return result.toString();
    }
}
```

## Time Complexity

We traverse the string exactly once.

**Time Complexity: `O(N)`**

where `N` is the length of the input string.

## Space Complexity

The `StringBuilder` stores the resulting string.

**Space Complexity: `O(N)`**

The auxiliary variable `depth` requires only `O(1)` additional space.

## Key Takeaway

> Keep track of the current nesting depth. Skip an opening parenthesis when the depth is `0`, and skip a closing parenthesis when the depth becomes `0`. This removes exactly the outermost pair from every primitive parentheses string in a single pass.
