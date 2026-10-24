# Problem 165: Count Number of Substrings

**Difficulty:** Medium | **Tags:** Strings, Bit Manipulation, Prefix Sum, Hash Table

## Problem Statement

You are handed a string `word` made up only of the lowercase letters `'a'` through `'j'` (ten letters in total).

A contiguous, non-empty piece of `word` is called a **good substring** if, among all the letters appearing in it, **no more than one letter** shows up an odd number of times.

Count and return how many good substrings `word` has. Substrings are counted by position, so two equal pieces taken from different places are counted separately.

## Example 1

```text
Input: word = "aba"
Output: 4
```

**Explanation:**

The good substrings are `"a"`, `"b"`, `"a"` and `"aba"`. In each of them at most one letter has an odd count.

## Example 2

```text
Input: word = "aabb"
Output: 9
```

**Explanation:**

`"aabb"` has 10 substrings in total. The only bad one is `"ab"`, because both `'a'` and `'b'` appear an odd number of times there. All the other substrings are good, so the answer is 10 - 1 = 9.

## Now It's Your Turn!

Choose the correct output for the input below.

```text
Input: word = "abca"
```

Options: `3`   `5`   `4`   `2`

**Answer:** `4`

## Still Not Clear? Let's Walk Through More Examples

**Example A: `word = "abca"` -> `4`**

* Substrings of length 1: `"a"`, `"b"`, `"c"`, `"a"` -> each has exactly one odd letter -> **4 good**.
* `"ab"` -> `a`, `b` both odd -> bad.
* `"bc"` -> `b`, `c` both odd -> bad.
* `"ca"` -> `c`, `a` both odd -> bad.
* `"abc"` -> `a`, `b`, `c` all odd -> bad.
* `"bca"` -> `b`, `c`, `a` all odd -> bad.
* `"abca"` -> `a` appears 2 times (even), but `b` and `c` are odd -> bad.

Total good substrings = **4**.

**Example B: `word = "aaa"` -> `6`**

Every substring contains only `'a'`, so at most one letter can ever be odd. All 3 + 2 + 1 = 6 substrings are good.

**Example C: `word = "abab"` -> `7`**

* Length 1: `"a"`, `"b"`, `"a"`, `"b"` -> 4 good.
* Length 2: `"ab"`, `"ba"`, `"ab"` -> all bad (two odd letters).
* Length 3: `"aba"` (only `b` is odd) and `"bab"` (only `a` is odd) -> 2 good.
* Length 4: `"abab"` -> `a` twice, `b` twice, nothing is odd -> 1 good.

Total good substrings = 4 + 0 + 2 + 1 = **7**.

## Constraints

* `1 <= word.length <= 10^5`
* `word` contains only lowercase English letters from `'a'` to `'j'`.

## Output Format

Return a single integer: the number of good substrings of `word`. (The answer can be larger than the range of a 32-bit integer, so use a 64-bit type.)

## Approach

Keep a 10-bit mask where bit `k` tells whether the letter number `k` (`'a'` = 0 ... `'j'` = 9) has appeared an odd number of times so far.

A substring `word[i..j]` is good exactly when the masks of the two prefixes around it differ in **zero bits or in exactly one bit**.

While scanning the string:

* Update the mask for the current character.
* Add the number of earlier prefixes that have the **same** mask (nothing is odd).
* Add the number of earlier prefixes whose mask differs by **one** bit (exactly one letter is odd) - there are only 10 such masks to check.
* Record the current mask in a frequency array of size `1024`.

This gives an `O(10 * N)` solution.
