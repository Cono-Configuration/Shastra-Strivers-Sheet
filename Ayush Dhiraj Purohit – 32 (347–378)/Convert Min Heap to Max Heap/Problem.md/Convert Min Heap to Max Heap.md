# Convert Min Heap to Max Heap

## Problem Statement

Given an array of integers `nums` of size $N$ that represents a valid **Binary Min-Heap**, convert it **in-place** into a valid **Binary Max-Heap**.

### Heap Properties

- A **Binary Min-Heap** is a complete binary tree stored in an array such that the value of each node is less than or equal to the values of its children:
  $$\text{nums}[\text{parent}] \le \text{nums}[\text{child}]$$
  The minimum element is located at the root (index `0`).
- A **Binary Max-Heap** is a complete binary tree stored in an array such that the value of each node is greater than or equal to the values of its children:
  $$\text{nums}[\text{parent}] \ge \text{nums}[\text{child}]$$
  The maximum element is located at the root (index `0`).

### Indexing Rules for 0-Indexed Array Representation

For any element at index $i$:
- Its parent is at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
- Its left child is at index $2i + 1$ (if $2i + 1 < N$).
- Its right child is at index $2i + 2$ (if $2i + 2 < N$).

To achieve optimal linear $\mathcal{O}(N)$ time complexity, perform bottom-up **Max-Heapify (Sift-Down)** starting from the last internal (non-leaf) node down to the root:
$$\text{startIndex} = \left\lfloor \frac{N - 2}{2} \right\rfloor$$

When both left and right children are equal and strictly greater than the current node, the tie-breaking rule selects the **left child**.

---

## Input Format

- The first line contains an integer $N$, denoting the number of elements in the array `nums`.
- The second line contains $N$ space-separated integers representing the elements of the min-heap array `nums`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the array `nums` after converting it in-place into a valid binary max-heap.

---

## Examples

### Example 1

**Input:**
```text
5
10 20 30 21 23
```

**Output:**
```text
30 23 10 21 20
```

**Explanation:**
- Non-leaf nodes are at indices $\lfloor (5 - 2) / 2 \rfloor = 1$ down to $0$.
- At index $1$ (`nums[1] = 20`): Left child is `nums[3] = 21`, right child is `nums[4] = 23`. The largest child is $23$. Swap `nums[1]` and `nums[4]` $\implies [10, 23, 30, 21, 20]$.
- At index $0$ (`nums[0] = 10`): Children are `nums[1] = 23` and `nums[2] = 30`. The largest child is $30$. Swap `nums[0]` and `nums[2]` $\implies [30, 23, 10, 21, 20]$.
- Index $2$ has no children ($2 \times 2 + 1 \ge 5$). Sift-down terminates.
- Final max-heap:
  - $\text{nums}[0] = 30 \ge \text{nums}[1] = 23, \text{nums}[2] = 10$
  - $\text{nums}[1] = 23 \ge \text{nums}[3] = 21, \text{nums}[4] = 20$
Every parent node satisfies the max-heap property.

---

### Example 2

**Input:**
```text
5
-5 -4 -3 -2 -1
```

**Output:**
```text
-1 -2 -3 -5 -4
```

**Explanation:**
- Non-leaf nodes are at index $1$ and $0$.
- At index $1$ (`-4`): Children are `-2` and `-1`. Largest is `-1`. Swap `nums[1]` and `nums[4]` $\implies [-5, -1, -3, -2, -4]$.
- At index $0$ (`-5`): Children are `-1` and `-3`. Largest is `-1`. Swap `nums[0]` and `nums[1]` $\implies [-1, -5, -3, -2, -4]$.
- Sift-down continues at index $1$ (`-5`): Children are `-2` and `-4`. Largest is `-2`. Swap `nums[1]` and `nums[3]` $\implies [-1, -2, -3, -5, -4]$.
- Final array forms a valid max-heap: $-1 \ge \{-2, -3\}$ and $-2 \ge \{-5, -4\}$.

---

### Example 3

**Input:**
```text
4
1 2 3 4
```

**Output:**
```text
4 2 3 1
```

**Explanation:**
- Non-leaf nodes are index $1$ and index $0$.
- At index $1$ (`2`): Left child is `4`. Swap `nums[1]` and `nums[3]` $\implies [1, 4, 3, 2]$.
- At index $0$ (`1`): Children are `4` and `3`. Largest is `4`. Swap `nums[0]` and `nums[1]` $\implies [4, 1, 3, 2]$.
- Sifting down at index $1$ (`1`): Child is `2`. Swap `nums[1]` and `nums[3]` $\implies [4, 2, 3, 1]$.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^9 \le \text{nums}[i] \le 10^9$
- The input array `nums` is guaranteed to represent a valid binary min-heap.
