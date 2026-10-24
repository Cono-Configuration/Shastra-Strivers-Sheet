# Editorial: Palindrome Partitioning

## Difficulty

Medium

## Prerequisites

* Strings
* Recursion
* Backtracking
* Dynamic Programming (palindrome table)

## Problem Statement

Given a string `inputText`, split it into consecutive pieces such that every piece is a palindrome, and return all possible ways to do so. Partitionings are listed so that, at the first position where two of them differ, the one with the shorter piece comes first.

## Input Format

The input contains a single string `inputText`.

## Constraints

* `1 <= inputText.length <= 16`
* `inputText` contains only lowercase English letters.

## Output Format

Print every palindrome partitioning in the required order, in the format `[["a","a","b"],["aa","b"]]`.

## Explanation & Approach

### Step 1: Decide Where to Cut

Between every two neighbouring characters we can either cut or not cut. A string of length `N` therefore has `N - 1` cut positions and `2^(N-1)` ways to be divided. We only keep the ways in which every piece is a palindrome.

### Step 2: Backtracking

Define `explore(start)`, which builds partitionings of the suffix beginning at `start`.

* If `start == N`, the whole string has been used, so save the current list of pieces.
* Otherwise, try every `end` from `start` to `N - 1`.
  * If `inputText[start..end]` is a palindrome, add it to the current list.
  * Call `explore(end + 1)`.
  * Remove the piece again (backtrack).

Because `end` increases, shorter pieces are tried first. This produces the partitionings in exactly the required order.

### Step 3: Fast Palindrome Checks

Checking each piece character by character repeats a lot of work. Instead, precompute `isPal[left][right]`:

```text
isPal[left][right] = true  if  text[left] == text[right]
                              and (right - left <= 2 or isPal[left + 1][right - 1])
```

Filling the table in order of increasing `right` guarantees that `isPal[left + 1][right - 1]` is already known. Each check in the backtracking is then `O(1)`.

## Example

### Input

```text
aab
```

### Analysis

* `start = 0`: `"a"` is a palindrome, so take it and continue from `1`.
  * `start = 1`: `"a"` is a palindrome, so take it and continue from `2`.
    * `start = 2`: `"b"` is a palindrome, so take it. `start = 3` equals the length, so save `["a","a","b"]`.
  * `"ab"` is not a palindrome, so it is skipped.
* `start = 0`: `"aa"` is a palindrome, so take it and continue from `2`.
  * `start = 2`: `"b"` is a palindrome, so save `["aa","b"]`.
* `"aab"` is not a palindrome, so it is skipped.

### Output

```text
[["a","a","b"],["aa","b"]]
```

## Java Solution

```java
class Solution {
    public List<List<String>> splitIntoPalindromes(String inputText) {

        int n = inputText.length();
        boolean[][] isPal = new boolean[n][n];

        for (int right = 0; right < n; right++) {
            for (int left = 0; left <= right; left++) {
                if (inputText.charAt(left) == inputText.charAt(right)
                        && (right - left <= 2 || isPal[left + 1][right - 1])) {
                    isPal[left][right] = true;
                }
            }
        }

        List<List<String>> answer = new ArrayList<>();
        explore(inputText, 0, isPal, new ArrayList<>(), answer);

        return answer;
    }

    private void explore(String inputText, int start, boolean[][] isPal,
                         List<String> current, List<List<String>> answer) {

        if (start == inputText.length()) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int end = start; end < inputText.length(); end++) {

            if (isPal[start][end]) {
                current.add(inputText.substring(start, end + 1));
                explore(inputText, end + 1, isPal, current, answer);
                current.remove(current.size() - 1);
            }
        }
    }
}
```

## Time Complexity

In the worst case (for example, a string of one repeated letter) every one of the `2^(N-1)` cut patterns is valid, and building each partitioning costs up to `O(N)`.

**Time Complexity: `O(N * 2^N)`**

where `N` is the length of the input string. The palindrome table adds `O(N^2)`, which is negligible in comparison.

## Space Complexity

The palindrome table uses `O(N^2)` space and the recursion depth is at most `N`. The list of answers is the output and is not counted as auxiliary space.

**Space Complexity: `O(N^2)`**

## Key Takeaway

> Palindrome partitioning is a "choose where to cut" problem. Backtrack over the end of the next piece, accept it only if it is a palindrome (checked in `O(1)` with a precomputed table), recurse on the remaining suffix, then undo the choice. Trying shorter pieces first gives a natural, deterministic output order.
