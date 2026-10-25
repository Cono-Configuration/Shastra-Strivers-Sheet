# Hand of Straights

## Problem Statement

Alice has some number of cards, and she wants to rearrange the cards into groups such that:
1. Every group contains exactly `groupSize` cards.
2. The card values in each group form a sequence of **consecutive integers** (e.g., $[3, 4, 5]$).

Given an integer array `hand` where `hand[i]` represents the value written on the $i$-th card, and an integer `groupSize`, return `true` if Alice can rearrange all cards into such groups, or `false` otherwise.

---

## Input Format

- The first line contains two space-separated integers $N$ and $groupSize$, denoting the total number of cards in `hand` and the required size of each group.
- The second line contains $N$ space-separated integers representing the card values in `hand`.

---

## Output Format

- Print a single line containing `true` if it is possible to rearrange all cards into valid consecutive groups of size `groupSize`, or `false` otherwise.

---

## Examples

### Example 1

**Input:**
```text
9 3
1 2 3 6 2 3 4 7 8
```

**Output:**
```text
true
```

**Explanation:**
Alice's hand can be rearranged into three valid groups of size $3$:
- Group 1: `[1, 2, 3]`
- Group 2: `[2, 3, 4]`
- Group 3: `[6, 7, 8]`
All cards are used, and each group consists of $3$ consecutive cards.

---

### Example 2

**Input:**
```text
5 4
1 2 3 4 5
```

**Output:**
```text
false
```

**Explanation:**
The hand has $5$ cards, which cannot be divided evenly into groups of size $4$ ($5 \pmod 4 \ne 0$). Hence, it is impossible.

---

### Example 3

**Input:**
```text
8 4
1 2 3 4 2 3 4 5
```

**Output:**
```text
true
```

**Explanation:**
Alice's hand can be divided into two valid groups of size $4$:
- Group 1: `[1, 2, 3, 4]`
- Group 2: `[2, 3, 4, 5]`

---

### Example 4

**Input:**
```text
6 3
1 1 2 2 3 3
```

**Output:**
```text
true
```

**Explanation:**
Alice's hand can be partitioned into two identical consecutive groups of size $3$:
- Group 1: `[1, 2, 3]`
- Group 2: `[1, 2, 3]`

---

## Constraints

- $1 \le N \le 10^5$, where $N$ is the number of cards in `hand`.
- $0 \le \text{hand}[i] \le 10^9$
- $1 \le groupSize \le N$
