# Editorial: 180. Letter Combinations of a Phone Number

## Difficulty

Medium

## Prerequisites

* Recursion
* Backtracking
* Strings and StringBuilder
* A lookup table for the keypad

## Problem Statement

Given a string of key presses (digits `2` to `9`), return every word that can be spelled by choosing one letter for each key, in order.

## Input Format

A single line containing the string `presses`.

## Constraints

* `1 <= presses.length <= 4`
* Each character is a digit from `'2'` to `'9'`.

## Output Format

Each word is printed on its own line in lexicographic order.

## Explanation & Approach

### Step 1: Map Each Key to Its Letters

Store the keypad as a lookup table indexed by digit. Key `2` maps to `"abc"`, key `7` maps to `"pqrs"`, and so on.

### Step 2: Choose One Letter per Key

Recurse on a position in `presses`. For the current key, try every letter it maps to. Append the letter, recurse on the next position, then remove the letter (backtrack).

### Step 3: Base Case

When the position reaches the end of `presses`, the word is complete. Add it to the answer.

### Step 4: Order

Keys are handled left to right, and each key's letters are tried in alphabetical order. This produces the words in lexicographic order automatically.

### Walkthrough

```text
presses = "23"

""
├── "a"  ->  "ad", "ae", "af"
├── "b"  ->  "bd", "be", "bf"
└── "c"  ->  "cd", "ce", "cf"
```

## Example

### Input

```text
23
```

### Output

```text
ad
ae
af
bd
be
bf
cd
ce
cf
```

## Java Solution

```java
import java.util.*;

class Solution {
    private static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> expandPresses(String presses) {
        List<String> result = new ArrayList<>();
        build(result, new StringBuilder(), presses, 0);
        return result;
    }

    private void build(List<String> result, StringBuilder current, String presses, int position) {
        if (position == presses.length()) {
            result.add(current.toString());
            return;
        }

        for (char letter : KEYS[presses.charAt(position) - '0'].toCharArray()) {
            current.append(letter);
            build(result, current, presses, position + 1);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
```

## Time Complexity

Each key has at most 4 letters, and there are at most 4 keys, so the answer has at most `4^4 = 256` words, each of length at most 4.

**Time Complexity: `O(4^N * N)`** where `N = presses.length`, since each word costs `O(N)` to build and copy.

## Space Complexity

The recursion depth equals `N`, and the current word uses `O(N)` space. The output is not counted.

**Space Complexity: `O(N)`**

## Key Takeaway

> Map each key to its letters with a lookup table, then choose one letter per key with backtracking. Processing keys left to right with sorted letters yields lexicographic order for free.
