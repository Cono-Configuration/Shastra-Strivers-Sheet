# Heapify Algorithm

## Problem Statement

Given an array `nums` representing a **min-heap**, an index `ind` (0-based), and an integer value `val`:
1. Set the element at index `ind` to `val` (`nums[ind] = val`).
2. Perform the **heapify** algorithm in-place such that the resulting array satisfies the min-heap property.

A **min-heap** is a complete binary tree where every node has a value less than or equal to the values of its children:
- For any 0-based index $i$:
  - Its parent is located at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
  - Its left child is located at index $2i + 1$ (if $2i + 1 < N$).
  - Its right child is located at index $2i + 2$ (if $2i + 2 < N$).
- The min-heap property requires:
  $$\text{nums}[i] \le \text{nums}[2i + 1] \quad \text{and} \quad \text{nums}[i] \le \text{nums}[2i + 2]$$

Modify the array **in-place**.

---

## Input Format

- The first line contains three space-separated integers:
  - $N$: the number of elements in the array `nums`.
  - $ind$: the 0-based index to update ($0 \le ind < N$).
  - $val$: the new integer value to set at index $ind$.
- The second line contains $N$ space-separated integers representing the initial elements of the min-heap `nums`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the array `nums` after updating index $ind$ and restoring the min-heap property.

---

## Examples

### Example 1

**Input:**
```text
6 5 2
1 4 5 5 7 6
```

**Output:**
```text
1 4 2 5 7 5
```

**Explanation:**
1. After setting index $5$ to $2$, the array becomes `[1, 4, 5, 5, 7, 2]`.
2. The parent of index $5$ is index $\lfloor (5 - 1) / 2 \rfloor = 2$, where `nums[2] = 5`.
3. Since $\text{nums}[5] = 2 < \text{nums}[2] = 5$, we swap `nums[5]` and `nums[2]`. The array becomes `[1, 4, 2, 5, 7, 5]`.
4. Now at index $2$, the parent is index $\lfloor (2 - 1) / 2 \rfloor = 0$, where `nums[0] = 1`.
5. Since $\text{nums}[2] = 2 \ge \text{nums}[0] = 1$, the min-heap property is satisfied and no further swaps are required.

---

### Example 2

**Input:**
```text
8 0 7
2 4 3 6 5 7 8 7
```

**Output:**
```text
3 4 7 6 5 7 8 7
```

**Explanation:**
1. After setting index $0$ to $7$, the array becomes `[7, 4, 3, 6, 5, 7, 8, 7]`.
2. The root at index $0$ has two children:
   - Left child at index $1$: $\text{nums}[1] = 4$.
   - Right child at index $2$: $\text{nums}[2] = 3$.
3. The smallest child is at index $2$ ($\text{val} = 3$). Since $3 < 7$, we swap `nums[0]` with `nums[2]`. The array becomes `[3, 4, 7, 6, 5, 7, 8, 7]`.
4. Now at index $2$, its children are at index $5$ ($\text{nums}[5] = 7$) and index $6$ ($\text{nums}[6] = 8$).
5. Since $\text{nums}[2] = 7 \le \text{nums}[5] = 7$ and $7 \le \text{nums}[6] = 8$, the min-heap property is fully satisfied.

---

### Example 3

**Input:**
```text
6 3 1
2 6 3 8 9 4
```

**Output:**
```text
1 2 3 6 9 4
```

**Explanation:**
1. After setting index $3$ to $1$, the array becomes `[2, 6, 3, 1, 9, 4]`.
2. Parent of index $3$ is index $1$ (`nums[1] = 6`). Since $1 < 6$, swap $\implies$ `[2, 1, 3, 6, 9, 4]`.
3. Parent of index $1$ is index $0$ (`nums[0] = 2`). Since $1 < 2$, swap $\implies$ `[1, 2, 3, 6, 9, 4]`.
4. Index $0$ is the root, so sift-up terminates.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^4 \le \text{nums}[i] \le 10^4$
- $0 \le ind < N$
- $-10^4 \le val \le 10^4$
- The input array `nums` initially represents a valid min-heap.
