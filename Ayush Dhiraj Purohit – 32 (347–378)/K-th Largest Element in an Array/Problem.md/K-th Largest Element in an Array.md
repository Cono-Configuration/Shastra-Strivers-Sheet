# K-th Largest Element in an Array

## Problem Statement

Given an integer array `nums` of size $N$ and an integer $K$, return the **$K^{\text{th}}$ largest element** present in the array.

Note that it is the $K^{\text{th}}$ largest element in sorted order, **not** the $K^{\text{th}}$ distinct element. Duplicate values occupy distinct positions in the ordering.

For example, in the array `[3, 2, 3, 1, 2, 4, 5, 5, 6]`, the elements sorted in non-increasing (descending) order are:
$$[6, 5, 5, 4, 3, 3, 2, 2, 1]$$
The $4^{\text{th}}$ largest element is `4`.

---

## Input Format

- The first line contains two space-separated integers $N$ and $K$, denoting the size of the array and the target rank $K$.
- The second line contains $N$ space-separated integers representing the elements of `nums`.

---

## Output Format

- Print a single integer representing the $K^{\text{th}}$ largest element in `nums`.

---

## Examples

### Example 1

**Input:**
```text
5 2
1 2 3 4 5
```

**Output:**
```text
4
```

**Explanation:**
- Array sorted in descending order: `[5, 4, 3, 2, 1]`.
- The 1st largest element is `5`.
- The 2nd largest element is `4`.

---

### Example 2

**Input:**
```text
5 5
-5 4 1 2 -3
```

**Output:**
```text
-5
```

**Explanation:**
- Array sorted in descending order: `[4, 2, 1, -3, -5]`.
- The 5th largest element (which is the minimum element in the array) is `-5`.

---

### Example 3

**Input:**
```text
9 4
3 2 3 1 2 4 5 5 6
```

**Output:**
```text
4
```

**Explanation:**
- Array sorted in descending order: `[6, 5, 5, 4, 3, 3, 2, 2, 1]`.
- The 4th element in this sequence is `4`.

---

## Constraints

- $1 \le K \le N \le 10^5$
- $-10^9 \le \text{nums}[i] \le 10^9$
