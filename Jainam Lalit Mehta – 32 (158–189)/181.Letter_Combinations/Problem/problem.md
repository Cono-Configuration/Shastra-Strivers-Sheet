# Problem 180: Letter Combinations of a Phone Number

**Difficulty:** Medium | **Tags:** Hash Table, String, Backtracking

## Problem Statement

You are given a string `presses` of digits. Each digit is a key pressed on a classic phone keypad, and each key from `2` to `9` stands for a fixed set of letters:

| Key | Letters |
|-----|---------|
| 2   | a b c   |
| 3   | d e f   |
| 4   | g h i   |
| 5   | j k l   |
| 6   | m n o   |
| 7   | p q r s |
| 8   | t u v   |
| 9   | w x y z |

Key `1` has no letters, and it never appears in `presses`.

Return every word that the presses could spell. A word is formed by choosing one letter from the set of each key, in the order the keys were pressed. The answer may be returned in any order. In the test files, the words are listed one per line in lexicographic order.

## Example 1

```text
Input: presses = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
```

## Example 2

```text
Input: presses = "2"
Output: ["a","b","c"]
```

## Constraints

* `1 <= presses.length <= 4`
* Each character of `presses` is a digit from `'2'` to `'9'`.

## Input Format

A single line containing the string `presses`.

## Output Format

Print each word on its own line, with no quotes and no separators. Words appear in lexicographic order.

For Example 1 the input file is:

```text
23
```

and the output file is:

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

## Approach

Process the presses from left to right. For each key, try each letter it stands for, add that letter to the word being built, and recurse on the next key. When every key has been used, the word is complete and is recorded.

Because each key's letters are tried in alphabetical order, and keys are processed in order, the words come out in lexicographic order without a final sort.
