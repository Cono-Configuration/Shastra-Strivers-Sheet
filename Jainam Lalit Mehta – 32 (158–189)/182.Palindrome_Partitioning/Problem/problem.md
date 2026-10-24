# Palindrome Partitioning

**Difficulty:** Medium | **Tags:** Strings, Backtracking, Dynamic Programming

## Problem Statement

A **palindrome** is a string that reads exactly the same from left to right and from right to left.

You are given a string `inputText`. Cut `inputText` into one or more consecutive pieces so that **every piece is a palindrome**. Such a division is called a palindrome partitioning.

Return **all possible** palindrome partitionings of `inputText`.

The partitionings must be listed in the following order: scan the pieces from left to right and prefer the partitioning whose piece is **shorter** at the first position where two partitionings differ. (This is the order produced by a left-to-right backtracking that tries shorter prefixes first.)

## Example 1

```text
Input: inputText = "aab"
Output: [["a","a","b"],["aa","b"]]
```

**Explanation:**

The string `"aab"` can be cut in the following ways, where every piece is a palindrome:

```text
"a" | "a" | "b"
"aa" | "b"
```

The cut `"a" | "ab"` is invalid because `"ab"` is not a palindrome.

## Example 2

```text
Input: inputText = "a"
Output: [["a"]]
```

**Explanation:**

The string has only one character, so the only partitioning is `["a"]`.

## Constraints

* `1 <= inputText.length <= 16`
* `inputText` contains only lowercase English letters.

## Input Format

A single line containing the string `inputText`.

## Output Format

Print all palindrome partitionings in the order described above, in the format `[["a","a","b"],["aa","b"]]`.

## Approach

Use backtracking together with a precomputed palindrome table.

* Build a table `isPal[l][r]` that tells whether `inputText[l..r]` is a palindrome. A substring is a palindrome if its end characters match and the inside part is also a palindrome (or has length at most 2).
* Start from index `start = 0` with an empty list `current`.
* For every `end` from `start` to the last index, if `inputText[start..end]` is a palindrome, add it to `current` and recursively process `end + 1`.
* When `start` reaches the length of the string, a valid partitioning has been formed, so save a copy of `current`.
* After the recursive call returns, remove the last piece (backtrack) and try the next `end`.

Trying `end` in increasing order gives shorter first pieces before longer ones, which matches the required output order.
