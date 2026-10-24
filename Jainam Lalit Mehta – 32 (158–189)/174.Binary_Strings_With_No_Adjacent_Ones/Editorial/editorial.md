# Editorial: Problem 174 - Binary Strings With No Adjacent Ones

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Strings / StringBuilder

## Problem Statement

Given an integer `length`, return all binary strings of that length that do not contain two consecutive `1`s, in lexicographically increasing order.

## Input Format

A single line containing the integer `length`.

## Constraints

* `1 <= length <= 18`

## Output Format

Print all valid strings on one line in increasing lexicographic order, written like `["000", "001", "010", "100", "101"]`.

## Explanation & Approach

### Step 1: Build the String Position by Position

At every position we can write `'0'` or `'1'`. Trying every combination would give `2^length` strings, but most of them contain `11` and must be discarded. It is better to avoid creating them in the first place.

### Step 2: Prune with the Rule

When we are about to write a `'1'`, we look at the previous character:

```text
previous is '1'  ->  writing '1' would create "11", so it is not allowed
otherwise        ->  both '0' and '1' are allowed
```

Writing `'0'` is always allowed.

### Step 3: Keep the Order Sorted

At every position we try `'0'` first and `'1'` second. Because `'0' < '1'`, the strings are discovered in increasing lexicographic order, so no sorting is needed.

### Step 4: Backtracking

```text
build(current):
    if current has length characters: save it, return
    append '0', build(current), remove the last character
    if the last character is not '1':
        append '1', build(current), remove the last character
```

Removing the last character after each recursive call (backtracking) lets us reuse the same `StringBuilder`.

### Step 5: How Many Strings Are There?

Let `f(n)` be the number of valid strings of length `n`. A valid string ends either with `0` (any valid string of length `n - 1` can precede it) or with `01` (any valid string of length `n - 2` can precede it), so:

```text
f(n) = f(n - 1) + f(n - 2),   f(1) = 2,  f(2) = 3
```

That is the Fibonacci sequence, so `f(18) = 6765`, small enough to list.

## Example

### Input

```text
3
```

### Analysis

| Prefix chosen so far | Next choices          |
| -------------------- | --------------------- |
| (empty)              | `0`, `1`              |
| `0`                  | `00`, `01`            |
| `1`                  | `10` only (`11` is not allowed) |
| `00`                 | `000`, `001`          |
| `01`                 | `010` only            |
| `10`                 | `100`, `101`          |

The strings found in order are `000`, `001`, `010`, `100`, `101`.

### Output

```text
["000", "001", "010", "100", "101"]
```

## Java Solution

```java
import java.util.*;

class Solution {

    public List<String> generateStrings(int length) {

        List<String> result = new ArrayList<>();

        build(new StringBuilder(), length, result);

        return result;
    }

    private void build(StringBuilder current, int length, List<String> result) {

        if (current.length() == length) {
            result.add(current.toString());
            return;
        }

        current.append('0');
        build(current, length, result);
        current.deleteCharAt(current.length() - 1);

        boolean previousIsOne = current.length() > 0 && current.charAt(current.length() - 1) == '1';

        if (!previousIsOne) {
            current.append('1');
            build(current, length, result);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
```

## Time Complexity

There are `f(length + 2)` (Fibonacci) valid strings, each of length `length`, so the work is proportional to the output size.

**Time Complexity: `O(length * Fib(length))`**, i.e. about `O(length * 1.618^length)`

## Space Complexity

Apart from the output, the recursion depth and the `StringBuilder` use `O(length)` space.

**Space Complexity: `O(length)`** auxiliary

## Key Takeaway

> When a rule forbids a pattern, check it while building the answer instead of filtering afterwards. Trying `'0'` before `'1'` gives lexicographic order for free, and the count of valid strings follows the Fibonacci sequence.
