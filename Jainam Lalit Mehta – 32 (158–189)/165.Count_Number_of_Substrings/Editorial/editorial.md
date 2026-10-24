# Editorial: Count Number of Substrings

## Difficulty

Medium

## Prerequisites

* Strings
* Bit Manipulation (XOR)
* Prefix Sums
* Frequency Arrays / Hash Maps

## Problem Statement

Given a string `word` that contains only the letters `'a'` to `'j'`, count the substrings in which at most one letter occurs an odd number of times.

## Input Format

The input contains a single string `word`.

## Constraints

* `1 <= word.length <= 100000`
* `word` consists only of lowercase English letters from `'a'` to `'j'`.

## Output Format

Print the number of good substrings.

## Explanation & Approach

### Step 1: Why Brute Force Fails

There are about `N * (N + 1) / 2` substrings. Checking each one costs even more, so with `N = 100000` an `O(N^2)` approach is far too slow.

### Step 2: Describe Parity with a Bitmask

Only the **parity** (odd or even) of each letter's count matters. Since there are just 10 letters, we store the parities in a 10-bit integer `mask`:

```text
bit 0 -> 'a'    bit 1 -> 'b'   ...   bit 9 -> 'j'
bit = 1 -> that letter has appeared an odd number of times
bit = 0 -> that letter has appeared an even number of times
```

Reading a character `ch` flips one bit:

```text
mask = mask ^ (1 << (ch - 'a'))
```

### Step 3: Substring = Difference of Two Prefixes

Let `mask[i]` be the parity mask of the first `i` characters (`mask[0] = 0`).

The parity mask of the substring `word[i..j-1]` is `mask[j] ^ mask[i]`.

The substring is good when that XOR has **at most one bit set**, which means:

* `mask[j] == mask[i]` (no letter is odd), or
* `mask[j]` and `mask[i]` differ in exactly one bit (one letter is odd).

### Step 4: Count with a Frequency Array

Keep `seen[m]` = how many earlier prefixes had mask `m`. Initially `seen[0] = 1` (the empty prefix).

For every new prefix mask `mask`:

```text
total += seen[mask]                     // nothing is odd
for bit in 0..9: total += seen[mask ^ (1 << bit)]   // exactly one letter is odd
seen[mask]++
```

### Step 5: Walk-through on `"aba"`

```text
start: seen[0] = 1

'a' -> mask = {a}      equal: 0 | one-bit away: {} (=seen[0]=1) -> +1   total = 1
'b' -> mask = {a,b}    equal: 0 | one-bit away: {a} (=1)        -> +1   total = 2
'a' -> mask = {b}      equal: 0 | one-bit away: {} (=1), {a,b} (=1) -> +2   total = 4
```

The answer is `4`, matching the expected output.

## Example

### Input

```text
aabb
```

### Analysis

* There are 10 substrings in total.
* Only `"ab"` (the middle one) has two letters with an odd count.
* Every other substring is good.

### Output

```text
9
```

## Java Solution

```java
class Solution {
    public long countValidSubstrings(String word) {

        long[] seen = new long[1 << 10];
        seen[0] = 1;

        int mask = 0;
        long total = 0;

        for (int i = 0; i < word.length(); i++) {

            mask ^= 1 << (word.charAt(i) - 'a');

            total += seen[mask];

            for (int bit = 0; bit < 10; bit++) {
                total += seen[mask ^ (1 << bit)];
            }

            seen[mask]++;
        }

        return total;
    }
}
```

## Time Complexity

For each of the `N` characters we do `11` array look-ups.

**Time Complexity: `O(10 * N)` = `O(N)`**

## Space Complexity

The frequency array has `2^10 = 1024` entries regardless of `N`.

**Space Complexity: `O(1)`**

## Key Takeaway

> Parity of letter counts fits in a bitmask. A substring is good when its two surrounding prefix masks are equal or differ in exactly one bit, so counting earlier prefixes with the same mask or any of the 10 one-bit-different masks solves the problem in a single pass. Use `long` - the answer can reach about 5 * 10^9.
