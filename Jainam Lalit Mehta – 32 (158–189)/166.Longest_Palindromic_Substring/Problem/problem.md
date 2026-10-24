# Problem:-166.Longest Palindromic Substring

**Difficulty:** Medium | **Tags:** Strings, Two Pointers, Expand Around Center

## Problem Statement

A **palindrome** is a sequence of characters that reads exactly the same from left to right and from right to left.

You are given a string `text`. Find the longest **contiguous segment** (substring) of `text` that is a palindrome and return it.

If more than one palindromic substring has the maximum length, return the one that **starts at the smallest index** (the leftmost one).

The comparison is **case-sensitive**, so `'a'` and `'A'` are different characters.

## Example 1

```text
Input: text = "babad"
Output: "bab"
```

**Explanation:**

Both `"bab"` and `"aba"` are palindromic substrings of length `3`, which is the maximum possible.

Since `"bab"` starts at index `0` and `"aba"` starts at index `1`, the leftmost one, `"bab"`, is returned.

## Example 2

```text
Input: text = "cbbd"
Output: "bb"
```

**Explanation:**

The longest palindromic substring of `"cbbd"` is `"bb"`, which has length `2`.

## Constraints

* `1 <= text.length <= 1000`
* `text` consists of only digits and English letters (uppercase and lowercase).

## Input Format

A single line containing the string `text`.

## Output Format

Return the longest palindromic substring of `text` (the leftmost one in case of a tie).

## Approach

Every palindrome is mirrored around a **center**. The center is either a single character (odd length) or the gap between two equal neighbouring characters (even length).

For every index `center` in `text`:

* Expand outward from `(center, center)` to find the longest odd-length palindrome.
* Expand outward from `(center, center + 1)` to find the longest even-length palindrome.
* Expansion continues while both ends are inside the string and the two characters are equal.

Keep track of the start and length of the best palindrome found so far, and update it only when a **strictly longer** palindrome is found. Because centers are visited from left to right, this automatically keeps the leftmost palindrome among equally long ones.

Finally, return the substring defined by the best start and length.
