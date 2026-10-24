# Editorial: 167. Sum of Beauty of All Substrings

## Difficulty

Medium

## Prerequisites

* Strings
* Hashing / Frequency Arrays
* Nested Iteration over Substrings

## Problem Statement

The beauty of a string is the difference between the highest and the lowest occurrence count among the characters that appear in it. Given a string `text`, return the sum of the beauty of all of its substrings.

## Input Format

The input contains a single string `text`.

## Constraints

* `1 <= text.length <= 500`
* `text` consists of only lowercase English letters.

## Output Format

Return a single integer: the sum of the beauty of all substrings of `text`.

## Explanation & Approach

### Step 1: Enumerate Every Substring Efficiently

There are `N * (N + 1) / 2` substrings. Instead of rebuilding the frequency of every substring from scratch, fix a starting index `start` and grow the substring one character at a time by moving `end` to the right.

Keep a frequency array of size 26:

```text
freq[c - 'a']++   // when text[end] is added
```

Because the array is reused while `end` grows, each extension costs `O(1)` to update.

### Step 2: Compute the Beauty of the Current Substring

After each update, scan the 26 counters:

* Ignore counters equal to `0` (those characters are not in the substring).
* Track the largest non-zero counter as `highest`.
* Track the smallest non-zero counter as `lowest`.

The beauty is:

```text
beauty = highest - lowest
```

If the substring contains only one distinct character, `highest == lowest`, so the beauty is `0`.

### Step 3: Accumulate

Add `beauty` to `total` for every `(start, end)` pair. Reset the frequency array when `start` moves forward.

### Walkthrough

```text
text = "aabcb"
```

| start | substring | beauty |
|-------|-----------|--------|
| 0 | "a" | 0 |
| 0 | "aa" | 0 |
| 0 | "aab" | 2 - 1 = 1 |
| 0 | "aabc" | 2 - 1 = 1 |
| 0 | "aabcb" | 2 - 1 = 1 |
| 1 | "a", "ab", "abc" | 0, 0, 0 |
| 1 | "abcb" | 2 - 1 = 1 |
| 2 | "b", "bc" | 0, 0 |
| 2 | "bcb" | 2 - 1 = 1 |
| 3 | "c", "cb" | 0, 0 |
| 4 | "b" | 0 |

Total = `1 + 1 + 1 + 1 + 1 = 5`.

## Example

### Input

```text
aabcbaa
```

### Output

```text
17
```

## Java Solution

```java
class Solution {
    public int beautySum(String text) {

        int n = text.length();
        int total = 0;

        for (int start = 0; start < n; start++) {

            int[] freq = new int[26];

            for (int end = start; end < n; end++) {

                freq[text.charAt(end) - 'a']++;

                int highest = 0;
                int lowest = Integer.MAX_VALUE;

                for (int count : freq) {
                    if (count > 0) {
                        highest = Math.max(highest, count);
                        lowest = Math.min(lowest, count);
                    }
                }

                total += highest - lowest;
            }
        }

        return total;
    }
}
```

## Time Complexity

There are `O(N^2)` substrings and each one needs a scan of 26 counters.

**Time Complexity: `O(26 * N^2)`**, which is effectively `O(N^2)` since the alphabet size is a constant.

## Space Complexity

Only a frequency array of fixed size 26 is used.

**Space Complexity: `O(1)`**

## Key Takeaway

> Fix the start, extend the end, and keep a running 26-slot frequency array. The beauty of each substring is the max minus the min of the non-zero counters, and summing it over all `(start, end)` pairs gives the answer without recounting any substring from scratch.
