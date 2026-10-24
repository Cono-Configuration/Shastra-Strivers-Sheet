# Editorial: Longest Palindromic Substring

## Difficulty

Medium

## Prerequisites

* Strings
* Two Pointers
* Substring Operations
* Basic Palindrome Concepts

## Problem Statement

Given a string `text`, find and return its longest palindromic substring. If several palindromic substrings share the maximum length, return the one that starts at the smallest index.

## Input Format

The input contains a single string `text`.

## Constraints

* `1 <= text.length <= 1000`
* `text` consists of only digits and English letters.
* Comparison is case-sensitive.

## Output Format

Return the longest palindromic substring of `text`, choosing the leftmost one when there is a tie.

## Explanation & Approach

### Step 1: Brute Force Idea

Check every substring and test whether it is a palindrome.

There are `O(N^2)` substrings and each check takes `O(N)`, so this takes `O(N^3)` time, which is unnecessarily slow.

### Step 2: Think in Terms of Centers

A palindrome is symmetric around its center. Instead of checking every substring, pick a center and grow outward as long as the characters on both sides match.

There are two kinds of centers:

* **Odd length:** the center is one character, e.g. `"aba"` is centered at `'b'`.
* **Even length:** the center is between two characters, e.g. `"abba"` is centered between the two `'b'` characters.

For a string of length `N` there are `N` odd centers and `N - 1` even centers.

### Step 3: Expand Around Each Center

Write a helper `expand(left, right)`:

```text
while left >= 0 and right < N and text[left] == text[right]:
    left--
    right++
return right - left - 1
```

The returned value is the length of the palindrome that was found.

### Step 4: Track the Best Answer

For each index `center`:

* `oddLength  = expand(center, center)`
* `evenLength = expand(center, center + 1)`

If a length is **strictly greater** than `bestLength`, update:

* Odd:  `bestStart = center - (oddLength - 1) / 2`
* Even: `bestStart = center - (evenLength - 2) / 2`

Using a strict `>` comparison keeps the first (leftmost) palindrome among those of equal length. All maximum-length palindromes share the same length, so their start index increases with their center, and centers are processed from left to right.

## Example

### Input

```text
babad
```

### Analysis

* Center at index `0` (`'b'`): palindrome `"b"`, length `1`.
* Center at index `1` (`'a'`): expands to `"bab"`, length `3`. Best so far: start `0`, length `3`.
* Center at index `2` (`'b'`): expands to `"aba"`, length `3`. Not strictly longer, so the answer stays `"bab"`.
* Remaining centers give palindromes of length at most `1`.

### Output

```text
bab
```

## Java Solution

```java
class Solution {
    public String findLongestPalindrome(String text) {

        int n = text.length();
        int bestStart = 0;
        int bestLength = 1;

        for (int center = 0; center < n; center++) {

            int oddLength = expand(text, center, center);
            if (oddLength > bestLength) {
                bestLength = oddLength;
                bestStart = center - (oddLength - 1) / 2;
            }

            int evenLength = expand(text, center, center + 1);
            if (evenLength > bestLength) {
                bestLength = evenLength;
                bestStart = center - (evenLength - 2) / 2;
            }
        }

        return text.substring(bestStart, bestStart + bestLength);
    }

    private int expand(String text, int left, int right) {

        while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
            left--;
            right++;
        }

        return right - left - 1;
    }
}
```

## Time Complexity

There are `2N - 1` centers, and each expansion can take up to `O(N)` steps.

**Time Complexity: `O(N^2)`**

where `N` is the length of the input string.

## Space Complexity

Only a few integer variables are used. The returned substring is the output and is not counted as auxiliary space.

**Space Complexity: `O(1)`**

## Key Takeaway

> Every palindrome is mirrored around a center. Try all `2N - 1` centers, expand outward while the characters match, and update the answer only when a strictly longer palindrome is found. This gives an `O(N^2)` solution with `O(1)` extra space and naturally returns the leftmost answer on ties.
