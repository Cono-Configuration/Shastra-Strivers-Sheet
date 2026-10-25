# Check if an array represents a min heap

## Problem Statement

Given an array of integers `nums` of size $N$, determine whether the array represents a valid **Binary Min-Heap**.

A binary min-heap is a complete binary tree that satisfies the **min-heap property**:
- The tree is complete: every level is completely filled, except possibly the last level which is filled from left to right. This structural property is inherently satisfied by any contiguous array representation.
- The value of each parent node is **less than or equal to** the values of its children:
  $$\text{nums}[\text{parent}] \le \text{nums}[\text{child}]$$

For an array `nums` indexed from $0$ to $N - 1$, any node at index $i$ has:
- **Left Child:** located at index $2i + 1$ (if $2i + 1 < N$).
- **Right Child:** located at index $2i + 2$ (if $2i + 2 < N$).

Return `true` if the given array represents a valid binary min-heap, otherwise return `false`.

---

## Input Format

- The first line contains an integer $N$, denoting the number of elements in the array.
- The second line contains $N$ space-separated integers, representing the elements of `nums`.
- If $N = 0$, the second line is omitted (an empty array trivially satisfies the property).

---

## Output Format

- Print a single line containing `true` if the array represents a valid binary min-heap, or `false` otherwise.

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
true
```

**Explanation:**
- Node at index 0 (`value = 10`):
  - Left child at index 1 is `20` ($10 \le 20$, valid).
  - Right child at index 2 is `30` ($10 \le 30$, valid).
- Node at index 1 (`value = 20`):
  - Left child at index 3 is `21` ($20 \le 21$, valid).
  - Right child at index 4 is `23` ($20 \le 23$, valid).
- Nodes at index 2, 3, and 4 are leaves (no children).
Every parent node satisfies `nums[parent] <= nums[child]`. Hence, the output is `true`.

---

### Example 2

**Input:**
```text
5
10 20 30 25 15
```

**Output:**
```text
false
```

**Explanation:**
- Node at index 0 (`value = 10`):
  - Left child is `20` ($10 \le 20$, valid).
  - Right child is `30` ($10 \le 30$, valid).
- Node at index 1 (`value = 20`):
  - Left child at index 3 is `25` ($20 \le 25$, valid).
  - Right child at index 4 is `15` ($20 > 15$, **violation**).
The parent node $20$ is strictly greater than its right child $15$, violating the min-heap property. Hence, the output is `false`.

---

### Example 3

**Input:**
```text
6
1 3 2 4 5 6
```

**Output:**
```text
true
```

**Explanation:**
- Index 0 (`1`): Left child is `3`, right child is `2`. Both $\ge 1$.
- Index 1 (`3`): Left child is `4`, right child is `5`. Both $\ge 3$.
- Index 2 (`2`): Left child is `6` ($\ge 2$).
All parent-child relationships satisfy the min-heap property. Hence, the output is `true`.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^9 \le \text{nums}[i] \le 10^9$
