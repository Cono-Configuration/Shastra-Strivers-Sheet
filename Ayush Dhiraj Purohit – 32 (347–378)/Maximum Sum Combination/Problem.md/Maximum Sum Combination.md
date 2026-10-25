# Maximum Sum Combination

## Problem Statement

Given two integer arrays `nums1` and `nums2` of size $N$ each, and an integer $K$, return the **$K$ maximum valid sum combinations** that can be formed by adding one element from `nums1` and one element from `nums2`.

A valid sum combination is formed by selecting an element from `nums1` and an element from `nums2`:
$$\text{sum} = \text{nums1}[i] + \text{nums2}[j] \quad (0 \le i, j < N)$$

The resulting $K$ sum combinations must be returned in **non-increasing order** (from largest to smallest).

---

## Input Format

- The first line contains two space-separated integers $N$ and $K$, denoting the size of the arrays and the number of maximum sum combinations to find, respectively.
- The second line contains $N$ space-separated integers representing the elements of `nums1`.
- The third line contains $N$ space-separated integers representing the elements of `nums2`.

---

## Output Format

- Print a single line containing $K$ space-separated integers representing the $K$ maximum sum combinations in non-increasing order.

---

## Examples

### Example 1

**Input:**
```text
2 2
7 3
1 6
```

**Output:**
```text
13 9
```

**Explanation:**
All possible sum combinations:
- `7 + 6 = 13`
- `3 + 6 = 9`
- `7 + 1 = 8`
- `3 + 1 = 4`
The top $K = 2$ combinations in non-increasing order are `13` and `9`.

---

### Example 2

**Input:**
```text
3 2
3 4 5
2 6 3
```

**Output:**
```text
11 10
```

**Explanation:**
The $2$ maximum combinations are:
- `nums1[2] + nums2[1] = 5 + 6 = 11`
- `nums1[1] + nums2[1] = 4 + 6 = 10`

---

### Example 3

**Input:**
```text
4 4
1 4 2 3
2 5 1 6
```

**Output:**
```text
10 9 9 8
```

**Explanation:**
Sorted `nums1`: `[1, 2, 3, 4]`
Sorted `nums2`: `[1, 2, 5, 6]`
The top $4$ maximum sum combinations are:
- `4 + 6 = 10`
- `4 + 5 = 9`
- `3 + 6 = 9`
- `3 + 5 = 8` (or `2 + 6 = 8`)

---

### Example 4

**Input:**
```text
1 1
10
20
```

**Output:**
```text
30
```

**Explanation:**
There is only one pair: `10 + 20 = 30`.

---

## Constraints

- $1 \le N \le 10^5$
- $1 \le K \le \min(10^5, N \times N)$
- $-10^9 \le \text{nums1}[i], \text{nums2}[i] \le 10^9$
