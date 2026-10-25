# Heap Sort

## Problem Statement

Given an array of integers `nums` of size $N$, sort the array in **non-decreasing order** using the **Heap Sort** algorithm.

### Heap Sort Fundamentals

**Heap Sort** is an efficient, in-place, comparison-based sorting algorithm based on the **Binary Max-Heap** data structure:
1. **Build Max-Heap:** Transform the input array into a binary max-heap. In a max-heap, the maximum element is always positioned at the root (index `0`), and for every node at index $i$, $\text{nums}[\text{parent}(i)] \ge \text{nums}[i]$.
2. **Extract Elements & Sift-Down:**
   - Swap the root (maximum element) with the last element of the active heap range.
   - The maximum element is now in its correct, permanent position at the end of the array.
   - Decrease the effective size of the heap by `1`.
   - Restore the max-heap property on the remaining heap by performing a **Sift-Down (Heapify-Down)** on the new root.
   - Repeat this process until all elements are sorted.

### Array Representation & Indexing Rules

For any element at index $i$ (0-based indexing):
- Parent: $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$)
- Left Child: $2i + 1$ (if $2i + 1 < \text{heapSize}$)
- Right Child: $2i + 2$ (if $2i + 2 < \text{heapSize}$)

---

## Input Format

- The first line contains an integer $N$, denoting the number of elements in the array `nums`.
- The second line contains $N$ space-separated integers representing the elements of `nums`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the array `nums` sorted in non-decreasing order.

---

## Examples

### Example 1

**Input:**
```text
5
7 4 1 5 3
```

**Output:**
```text
1 3 4 5 7
```

**Explanation:**
1. **Build Max-Heap Phase:**
   - Non-leaf nodes are at index $1$ and $0$.
   - At index $1$ (`4`): Children are `5` and `3`. Largest is `5`. Swap `nums[1]` and `nums[3]` $\implies [7, 5, 1, 4, 3]$.
   - At index $0$ (`7`): Children are `5` and `1`. Largest is `7`. Max-heap is `[7, 5, 1, 4, 3]`.
2. **Sorting Phase:**
   - Swap root `7` with last element `3` $\implies [3, 5, 1, 4, 7]$. Sift down `3` in range $[0, 3] \implies [5, 4, 1, 3, 7]$.
   - Swap root `5` with `3` $\implies [3, 4, 1, 5, 7]$. Sift down `3` in range $[0, 2] \implies [4, 3, 1, 5, 7]$.
   - Swap root `4` with `1` $\implies [1, 3, 4, 5, 7]$. Sift down `1` in range $[0, 1] \implies [3, 1, 4, 5, 7]$.
   - Swap root `3` with `1` $\implies [1, 3, 4, 5, 7]$.
3. Resulting array: `[1, 3, 4, 5, 7]`.

---

### Example 2

**Input:**
```text
5
5 4 4 1 1
```

**Output:**
```text
1 1 4 4 5
```

**Explanation:**
- The array contains duplicate elements.
- Heap sort correctly handles duplicates and produces the non-decreasing sorted order `[1, 1, 4, 4, 5]`.

---

### Example 3

**Input:**
```text
6
12 11 13 5 6 7
```

**Output:**
```text
5 6 7 11 12 13
```

**Explanation:**
- Building max-heap gives `[13, 11, 12, 5, 6, 7]`.
- Repeated root extraction yields `[5, 6, 7, 11, 12, 13]`.

---

## Constraints

- $1 \le N \le 10^5$
- $-10^9 \le \text{nums}[i] \le 10^9$
