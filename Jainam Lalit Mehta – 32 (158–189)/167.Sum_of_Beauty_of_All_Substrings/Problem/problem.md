# Problem 167: Sum of Beauty of All Substrings

**Difficulty:** Medium | **Tags:** Hash Table, String, Counting

## Problem Statement

The **beauty** of a string is defined as the gap between the occurrence count of its most common character and the occurrence count of its least common character (only characters that actually appear in the string are considered).

For example, the beauty of `"abaacc"` is `3 - 1 = 2`, because `'a'` appears 3 times and `'b'` appears 1 time.

You are given a string `text`. Compute the total of the beauty values of every substring of `text`, and return that total.

## Example 1

```text
Input: text = "aabcb"
Output: 5
```

**Explanation:**

The substrings of `"aabcb"` that have a non-zero beauty are:

```text
"aab", "aabc", "aabcb", "abcb", "bcb"
```

Each of these substrings has a beauty of `1`, and every other substring has a beauty of `0`.

Therefore, the total is `1 + 1 + 1 + 1 + 1 = 5`.

## Example 2

```text
Input: text = "aabcbaa"
Output: 17
```

## Constraints

* `1 <= text.length <= 500`
* `text` consists of only lowercase English letters.

## Output Format

Return a single integer: the sum of the beauty of all substrings of `text`.

## Approach

Fix the starting index of a substring and extend its ending index one character at a time, maintaining an array of 26 frequency counters.

* After each extension, scan the 26 counters and look only at the non-zero ones.
* The beauty of the current substring is `maxFrequency - minFrequency` over those non-zero counters.
* Add this value to the running total.

Repeating this for every starting index covers every substring exactly once.
