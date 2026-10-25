# Replace Elements by Their Rank

## Problem Statement

Given an array of $N$ integers `arr`, replace each element with its **rank** in the array.

The **rank** of an element represents its $1$-based position when all unique elements of the array are sorted in ascending order:
- The smallest element in the array receives rank $1$.
- The next smallest unique element receives rank $2$, the next receives rank $3$, and so on.
- If two or more elements are identical, they must be assigned the **same rank**.
- The resulting ranks must be returned in the **original order** of their corresponding elements in `arr`.

---

## Input Format

- The first line contains an integer $N$, denoting the number of elements in the array `arr`.
- The second line contains $N$ space-separated integers representing the elements of `arr`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the array where each original element has been replaced by its rank.

---

## Examples

### Example 1

**Input:**
```text
6
20 15 26 2 98 6
```

**Output:**
```text
4 3 5 1 6 2
```

**Explanation:**
- The sorted unique elements are: `[2, 6, 15, 20, 26, 98]`.
- Their corresponding ranks are:
  - $2 \rightarrow 1$
  - $6 \rightarrow 2$
  - $15 \rightarrow 3$
  - $20 \rightarrow 4$
  - $26 \rightarrow 5$
  - $98 \rightarrow 6$
- Replacing each element in the original array `[20, 15, 26, 2, 98, 6]` with its rank gives:
  `[4, 3, 5, 1, 6, 2]`.

---

### Example 2

**Input:**
```text
7
1 5 8 15 8 25 9
```

**Output:**
```text
1 2 3 5 3 6 4
```

**Explanation:**
- The sorted unique elements are: `[1, 5, 8, 9, 15, 25]`.
- Their corresponding ranks are:
  - $1 \rightarrow 1$
  - $5 \rightarrow 2$
  - $8 \rightarrow 3$
  - $9 \rightarrow 4$
  - $15 \rightarrow 5$
  - $25 \rightarrow 6$
- Both occurrences of $8$ at index 2 and index 4 share the same rank $3$.
- Replacing original elements yields: `[1, 2, 3, 5, 3, 6, 4]`.

---

### Example 3

**Input:**
```text
4
100 100 100 100
```

**Output:**
```text
1 1 1 1
```

**Explanation:**
- All elements are identical. The only unique value is $100$, which receives rank $1$.
- Every element in the output array is $1$.

---

### Example 4

**Input:**
```text
5
37 -9 12 -9 -9
```

**Output:**
```text
3 1 2 1 1
```

**Explanation:**
- The sorted unique elements are: `[-9, 12, 37]`.
- Ranks: $-9 \rightarrow 1$, $12 \rightarrow 2$, $37 \rightarrow 3$.
- Replacing original elements yields: `[3, 1, 2, 1, 1]`.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^9 \le \text{arr}[i] \le 10^9$
