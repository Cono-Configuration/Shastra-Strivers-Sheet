# Build heap from a given Array

## Problem Statement

Given an array of integers `nums`, convert it **in-place** into a **binary min-heap**.

A **binary min-heap** is a complete binary tree stored in an array such that:
- The key at the root is the **minimum** among all keys present in the heap.
- The same property holds recursively for every subtree: each node's value is less than or equal to the values of its children.

For any element at index $i$ (0-indexed):
- Its parent is located at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
- Its left child is located at index $2i + 1$ (if $2i + 1 < N$).
- Its right child is located at index $2i + 2$ (if $2i + 2 < N$).
- The min-heap property requires:
  $$\text{nums}[i] \le \text{nums}[2i + 1] \quad \text{and} \quad \text{nums}[i] \le \text{nums}[2i + 2]$$

To achieve optimal $\mathcal{O}(N)$ construction, convert the array into a min-heap using the bottom-up **heapify-down (sift-down)** algorithm starting from the last non-leaf node down to the root:
$$\text{startIndex} = \left\lfloor \frac{N}{2} \right\rfloor - 1$$

---

## Input Format

- The first line contains an integer $N$, denoting the number of elements in the array `nums`.
- The second line contains $N$ space-separated integers representing the elements of `nums`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the array `nums` after converting it in-place into a min-heap.

---

## Examples

### Example 1

**Input:**
```text
6
6 5 2 7 1 7
```

**Output:**
```text
1 5 2 7 6 7
```

**Explanation:**
- Non-leaf nodes are at indices $\lfloor 6/2 \rfloor - 1 = 2$ down to $0$.
- At index $2$ (`nums[2] = 2`): Left child is `nums[5] = 7`. Since $2 \le 7$, no swap is needed.
- At index $1$ (`nums[1] = 5`): Left child is `nums[3] = 7`, right child is `nums[4] = 1`. Smallest is `nums[4] = 1`. Swap `nums[1]` and `nums[4]` $\implies [6, 1, 2, 7, 5, 7]$.
- At index $0$ (`nums[0] = 6`): Children are `nums[1] = 1` and `nums[2] = 2`. Smallest is `nums[1] = 1`. Swap `nums[0]` and `nums[1]` $\implies [1, 6, 2, 7, 5, 7]$.
  - Now at index $1$ (`nums[1] = 6`): Children are `nums[3] = 7` and `nums[4] = 5`. Smallest is `nums[4] = 5`. Swap `nums[1]` and `nums[4]` $\implies [1, 5, 2, 7, 6, 7]$.
- Final array: `[1, 5, 2, 7, 6, 7]`.
  - $\text{nums}[0] = 1 \le \text{nums}[1] = 5, \text{nums}[2] = 2$
  - $\text{nums}[1] = 5 \le \text{nums}[3] = 7, \text{nums}[4] = 6$
  - $\text{nums}[2] = 2 \le \text{nums}[5] = 7$

---

### Example 2

**Input:**
```text
9
2 3 4 1 7 3 9 4 6
```

**Output:**
```text
1 2 3 3 7 4 9 4 6
```

**Explanation:**
- Non-leaf nodes are at indices $\lfloor 9/2 \rfloor - 1 = 3$ down to $0$.
- Heapifying down from index $3$ down to $0$ yields `[1, 2, 3, 3, 7, 4, 9, 4, 6]`.
- All parent-child min-heap conditions are satisfied:
  - $\text{nums}[0] = 1 \le \text{nums}[1] = 2, \text{nums}[2] = 3$
  - $\text{nums}[1] = 2 \le \text{nums}[3] = 3, \text{nums}[4] = 7$
  - $\text{nums}[2] = 3 \le \text{nums}[5] = 4, \text{nums}[6] = 9$
  - $\text{nums}[3] = 3 \le \text{nums}[7] = 4, \text{nums}[8] = 6$

---

### Example 3

**Input:**
```text
5
5 4 3 2 1
```

**Output:**
```text
1 2 3 5 4
```

**Explanation:**
- Non-leaf nodes start at index $\lfloor 5/2 \rfloor - 1 = 1$.
- At index $1$ (`nums[1] = 4`): Children are `nums[3] = 2` and `nums[4] = 1`. Swap with `nums[4]` $\implies [5, 1, 3, 2, 4]$.
- At index $0$ (`nums[0] = 5`): Children are `nums[1] = 1` and `nums[2] = 3`. Swap with `nums[1]` $\implies [1, 5, 3, 2, 4]$. Sift down continues at index $1$: swap with `nums[3] = 2` $\implies [1, 2, 3, 5, 4]$.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^4 \le \text{nums}[i] \le 10^4$
