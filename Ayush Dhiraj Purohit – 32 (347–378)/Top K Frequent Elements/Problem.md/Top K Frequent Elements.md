# Top K Frequent Elements

## Problem Statement

Given an integer array `nums` of size $N$ and an integer $K$, return the **$K$ most frequent elements** in the array.

The resulting $K$ elements should be returned in **descending order of their frequencies**. If two elements have the same frequency, break the tie by ordering them in **ascending numerical order** (smaller value first).

It is guaranteed that the set of top $K$ frequent elements is unique.

---

## Input Format

- The first line contains two space-separated integers $N$ and $K$, denoting the size of the array `nums` and the number of top frequent elements to retrieve, respectively.
- The second line contains $N$ space-separated integers representing the elements of `nums`.

---

## Output Format

- Print a single line containing $K$ space-separated integers, representing the $K$ most frequent elements in descending order of frequency (with ties broken by smaller value first).

---

## Examples

### Example 1

**Input:**
```text
6 2
1 1 1 2 2 3
```

**Output:**
```text
1 2
```

**Explanation:**
- Element `1` appears with frequency $3$.
- Element `2` appears with frequency $2$.
- Element `3` appears with frequency $1$.
The $K = 2$ most frequent elements are `1` and `2`.

---

### Example 2

**Input:**
```text
1 1
4
```

**Output:**
```text
4
```

**Explanation:**
The array contains only one distinct element `4` with frequency $1$. The top $1$ most frequent element is `4`.

---

### Example 3

**Input:**
```text
8 3
4 1 -1 2 -1 2 3 -1
```

**Output:**
```text
-1 2 1
```

**Explanation:**
Frequencies of unique elements:
- `-1`: appears $3$ times
- `2`: appears $2$ times
- `1`: appears $1$ time
- `3`: appears $1$ time
- `4`: appears $1$ time
The top 2 most frequent are `-1` (freq 3) and `2` (freq 2). For the 3rd element, `1`, `3`, and `4` have equal frequency $1$. The tie is broken by smaller numerical value, so `1` is chosen.

---

### Example 4

**Input:**
```text
5 2
5 5 10 10 20
```

**Output:**
```text
5 10
```

**Explanation:**
- Element `5`: frequency $2$
- Element `10`: frequency $2$
- Element `20`: frequency $1$
Both `5` and `10` have frequency $2$. Because they share the highest frequency, they are ordered by value (`5 < 10`).

---

## Constraints

- $1 \le N \le 10^5$
- $1 \le K \le \text{number of unique elements in } \text{nums}$
- $-10^9 \le \text{nums}[i] \le 10^9$
- It is guaranteed that the set of top $K$ frequent elements is unique.
