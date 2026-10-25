# Editorial: Hand of Straights

## 1. Problem Overview

We are given an integer array `hand` of size $N$ and an integer `groupSize`. We must determine whether the cards can be partitioned into sets of size `groupSize`, where each set consists of strictly consecutive integers:
$$[x, x + 1, x + 2, \dots, x + groupSize - 1]$$

---

## 2. Fundamental Invariants & Pruning

1. **Divisibility Check:**
   - Every group must contain exactly `groupSize` cards.
   - If $N \pmod{groupSize} \ne 0$, it is impossible to partition all cards evenly into groups of size `groupSize`. We can immediately return `false`.
2. **Trivial Case ($groupSize = 1$):**
   - If $groupSize = 1$, every card forms its own valid group of size $1$. Always return `true`.

---

## 3. The Greedy Choice Property

### Why Greedy Works
Consider the smallest card value currently available in the hand, say $m$.
- Can $m$ be the second card in some group? No, because that would require a card with value $m - 1$, which does not exist (as $m$ is the minimum available card).
- Can $m$ be the third or any subsequent card in some group? No, for the same reason.
- Therefore, **$m$ must be the first element (the start) of a consecutive group**.

This gives us an unequivocal greedy strategy:
1. Always find the smallest available card $m$.
2. It must form a group: $[m, m + 1, m + 2, \dots, m + groupSize - 1]$.
3. If any of the required cards $m + i$ (for $0 \le i < groupSize$) is missing or has exhausted its count, then forming valid groups is impossible $\rightarrow$ return `false`.
4. Otherwise, decrement the counts of all cards in this group and repeat until all cards are grouped.

---

## 4. Implementation Approaches

### Approach 1: Min-Heap + Frequency Map
1. Count the frequency of each card using a hash map `countMap`.
2. Insert all unique card values into a **Min-Heap**.
3. While the Min-Heap is not empty:
   - Peek at the minimum card `startCard`.
   - If `countMap.get(startCard) == 0`, pop it from the heap and continue.
   - Let `freq = countMap.get(startCard)`.
   - For each offset $i$ from $0$ to $groupSize - 1$:
     - Check if `countMap.getOrDefault(startCard + i, 0) < freq`. If so, return `false`.
     - Decrement: `countMap.put(startCard + i, countMap.get(startCard + i) - freq)`.
   - Pop `startCard` from the heap.
4. Return `true`.

### Approach 2: Sorting + Frequency Map (Optimal & Clean)
Instead of a dynamic heap, we can sort the cards initially:
1. Count the frequency of each card in a Hash Map `count`.
2. Sort the original array `hand` in non-decreasing order.
3. Iterate through each card $x \in hand$:
   - If `count.get(x) == 0`, this card was already used as part of an earlier group; skip it.
   - Otherwise, $x$ must start `c = count.get(x)` groups of size `groupSize`.
   - For each offset $i$ from $0$ to $groupSize - 1$:
     - If `count.getOrDefault(x + i, 0) < c`, return `false`.
     - Decrement `count.put(x + i, count.get(x + i) - c)`.
4. If the loop finishes without conflict, return `true`.

---

## 5. Step-by-Step Dry Run

Consider Example 1: `hand = [1, 2, 3, 6, 2, 3, 4, 7, 8]`, `groupSize = 3`, $N = 9$.

### Divisibility:
$9 \pmod 3 = 0$ (Valid, proceed).

### Initial Frequency Map:
- `1`: 1
- `2`: 2
- `3`: 2
- `4`: 1
- `6`: 1
- `7`: 1
- `8`: 1

### Sorted Unique Card Iteration:
1. Smallest card is `1` (count = 1):
   - Form group: `[1, 2, 3]` with count 1.
   - Decrement counts:
     - `1`: $1 - 1 = 0$
     - `2`: $2 - 1 = 1$
     - `3`: $2 - 1 = 1$
2. Next available smallest card is `2` (count = 1):
   - Form group: `[2, 3, 4]` with count 1.
   - Decrement counts:
     - `2`: $1 - 1 = 0$
     - `3`: $1 - 1 = 0$
     - `4`: $1 - 1 = 0$
3. Next available smallest card is `6` (count = 1):
   - Form group: `[6, 7, 8]` with count 1.
   - Decrement counts:
     - `6`: $1 - 1 = 0$
     - `7`: $1 - 1 = 0$
     - `8`: $1 - 1 = 0$

All counts are $0$. Return `true`.

---

## 6. Edge Cases & Boundary Analysis

1. **$N \pmod{groupSize} \ne 0$:**
   - Caught immediately in $\mathcal{O}(1)$ time.
2. **$groupSize = 1$:**
   - Handled immediately; any hand can be trivially partitioned into 1-card groups.
3. **Large Card Values ($10^9$):**
   - Values fit comfortably in standard 32-bit signed integers.
   - $x + i$ with $x \le 10^9$ and $i \le 10^5$ will not overflow 32-bit signed integers ($10^9 + 10^5 \ll 2 \times 10^9$).
4. **Duplicate Cards:**
   - Batch decrementing by `count.get(x)` processes duplicate starts in $\mathcal{O}(1)$ amortized operations.
5. **Gaps in Sequences:**
   - Example: `[1, 2, 4]` with `groupSize = 3`. Fails when checking for card `3`.

---

## 7. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N \log N)$
  - Frequency counting takes $\mathcal{O}(N)$ time.
  - Sorting the array `hand` takes $\mathcal{O}(N \log N)$ time.
  - In the outer loop, each distinct starting card consumes all its occurrences in a single batch. Across the entire execution, each card is examined at most once as part of a group, resulting in $\mathcal{O}(N)$ total hash map operations.
  - Overall time complexity is dominated by sorting: $\mathcal{O}(N \log N)$.
- **Space Complexity:** $\mathcal{O}(N)$
  - The frequency map stores at most $N$ unique card values.
