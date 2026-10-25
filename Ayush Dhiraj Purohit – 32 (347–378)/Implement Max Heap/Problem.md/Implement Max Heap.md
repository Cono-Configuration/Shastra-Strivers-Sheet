# Implement Max Heap

## Problem Statement

You need to implement a **Binary Max-Heap** data structure supporting the following operations:

- `initializeHeap()`: Initializes or clears the heap to an empty state.
- `insert(key)`: Inserts an integer value `key` into the max-heap.
- `changeKey(index, newVal)`: Updates the value at the given 0-based `index` in the internal heap representation to `newVal` and restores the max-heap property.
- `extractMax()`: Removes the maximum element (the root) from the max-heap. If during extraction both left and right children are equal and strictly greater than the current node, the element must be swapped with the **left** child.
- `isEmpty()`: Checks whether the max-heap is empty. Returns `1` if empty, otherwise `0`.
- `getMax()`: Returns the maximum element currently present in the max-heap without removing it.
- `heapSize()`: Returns the current number of elements in the max-heap.

### Binary Max-Heap Fundamentals

A **binary max-heap** is a complete binary tree stored compactly in a contiguous 0-indexed dynamic array:
- The root of the tree resides at index `0`.
- For any node at index $i$:
  - Its parent is located at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
  - Its left child is located at index $2i + 1$.
  - Its right child is located at index $2i + 2$.
- **Max-Heap Property:** For every non-root node at index $i$, $\text{heap}[\text{parent}(i)] \ge \text{heap}[i]$. Consequently, the maximum element is always maintained at the root (index `0`).

---

## Input Format

- The first line contains an integer $Q$, denoting the total number of operations to be executed.
- The next $Q$ lines each describe an operation in one of the following formats:
  - `initializeHeap`
  - `insert <key>`
  - `changeKey <index> <newVal>`
  - `extractMax`
  - `isEmpty`
  - `getMax`
  - `heapSize`

---

## Output Format

- Print a single line containing $Q$ space-separated tokens corresponding to the results of each operation:
  - For operations that do not return a value (`initializeHeap`, `insert`, `changeKey`, `extractMax`), output `null`.
  - For `getMax` and `heapSize`, output the returned integer.
  - For `isEmpty`, output `1` if the heap is empty, otherwise `0`.

---

## Examples

### Example 1

**Input:**
```text
10
initializeHeap
insert 4
insert 1
insert 10
getMax
heapSize
isEmpty
extractMax
changeKey 0 16
getMax
```

**Output:**
```text
null null null null 10 3 0 null null 16
```

**Explanation:**
1. `initializeHeap`: Initializes an empty heap $\implies []$ (outputs `null`).
2. `insert 4`: Appends `4` $\implies [4]$ (outputs `null`).
3. `insert 1`: Appends `1` $\implies [4, 1]$ (parent $4 \ge 1$, outputs `null`).
4. `insert 10`: Appends `10` at index 2 $\implies [4, 1, 10]$. Sifts up $10$ against parent $4$ ($10 > 4$) $\implies [10, 1, 4]$ (outputs `null`).
5. `getMax`: The root element is `10` (outputs `10`).
6. `heapSize`: The heap currently contains 3 elements (outputs `3`).
7. `isEmpty`: The heap is not empty (outputs `0`).
8. `extractMax`: Replaces root `10` with last element `4`, removes `10` $\implies [4, 1]$. Sifts down `4` against left child `1` ($4 \ge 1$, no swap) $\implies [4, 1]$ (outputs `null`).
9. `changeKey 0 16`: Changes `heap[0]` to `16` $\implies [16, 1]$. Sifts up `16` (already root) $\implies [16, 1]$ (outputs `null`).
10. `getMax`: The root element is `16` (outputs `16`).

---

### Example 2

**Input:**
```text
11
initializeHeap
insert 4
insert 1
extractMax
getMax
insert 1
heapSize
isEmpty
extractMax
changeKey 0 2
getMax
```

**Output:**
```text
null null null null 1 null 2 0 null null 2
```

**Explanation:**
1. `initializeHeap`: Initializes empty heap $\implies []$ (outputs `null`).
2. `insert 4`: Heap is $[4]$ (outputs `null`).
3. `insert 1`: Heap is $[4, 1]$ (outputs `null`).
4. `extractMax`: Removes root `4`, last element `1` becomes root $\implies [1]$ (outputs `null`).
5. `getMax`: Root is `1` (outputs `1`).
6. `insert 1`: Appends `1` $\implies [1, 1]$ (outputs `null`).
7. `heapSize`: Number of elements is 2 (outputs `2`).
8. `isEmpty`: Heap is not empty (outputs `0`).
9. `extractMax`: Removes root `1`, last element `1` becomes root $\implies [1]$ (outputs `null`).
10. `changeKey 0 2`: Updates `heap[0]` from `1` to `2` $\implies [2]$ (outputs `null`).
11. `getMax`: Root is `2` (outputs `2`).

---

## Constraints

- $1 \le Q \le 10^5$
- $-10^9 \le \text{key}, \text{newVal} \le 10^9$
- For `changeKey`, $0 \le \text{index} < \text{heapSize}$.
- `getMax` and `extractMax` are guaranteed to be called only when the heap is non-empty ($\text{heapSize} > 0$).
