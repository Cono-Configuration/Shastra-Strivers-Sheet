# Implement Min Heap

## Problem Statement

You need to implement a **Binary Min-Heap** data structure supporting the following operations:

- `initializeHeap()`: Initializes or clears the heap to an empty state.
- `insert(key)`: Inserts an integer value `key` into the min-heap.
- `changeKey(index, newVal)`: Updates the value at the given 0-based `index` in the internal heap representation to `newVal` and restores the min-heap property.
- `extractMin()`: Removes the minimum element (the root) from the min-heap.
- `isEmpty()`: Checks whether the min-heap is empty. Returns `1` if empty, otherwise `0`.
- `getMin()`: Returns the minimum element currently present in the min-heap without removing it.
- `heapSize()`: Returns the current number of elements in the min-heap.

### Binary Min-Heap Fundamentals

A **binary min-heap** is a complete binary tree stored compactly in a contiguous 0-indexed dynamic array:
- The root of the tree resides at index `0`.
- For any node at index $i$:
  - Its parent is located at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
  - Its left child is located at index $2i + 1$.
  - Its right child is located at index $2i + 2$.
- **Min-Heap Property:** For every non-root node at index $i$, $\text{heap}[\text{parent}(i)] \le \text{heap}[i]$. Consequently, the smallest element is always maintained at the root (index `0`).

---

## Input Format

- The first line contains an integer $Q$, denoting the total number of operations to be executed.
- The next $Q$ lines each describe an operation in one of the following formats:
  - `initializeHeap`
  - `insert <key>`
  - `changeKey <index> <newVal>`
  - `extractMin`
  - `isEmpty`
  - `getMin`
  - `heapSize`

---

## Output Format

- Print a single line containing $Q$ space-separated tokens corresponding to the results of each operation:
  - For operations that do not return a value (`initializeHeap`, `insert`, `changeKey`, `extractMin`), output `null`.
  - For `getMin` and `heapSize`, output the returned integer.
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
getMin
heapSize
isEmpty
extractMin
changeKey 0 16
getMin
```

**Output:**
```text
null null null null 1 3 0 null null 10
```

**Explanation:**
1. `initializeHeap`: Initializes an empty heap $\implies []$ (outputs `null`).
2. `insert 4`: Appends `4` $\implies [4]$ (outputs `null`).
3. `insert 1`: Appends `1` and sifts up against parent `4` $\implies [1, 4]$ (outputs `null`).
4. `insert 10`: Appends `10` at index 2. Parent is $1 \le 10$, no swap $\implies [1, 4, 10]$ (outputs `null`).
5. `getMin`: The root element is `1` (outputs `1`).
6. `heapSize`: The heap currently contains 3 elements (outputs `3`).
7. `isEmpty`: The heap is not empty (outputs `0`).
8. `extractMin`: Swaps root `1` with last element `10`, removes `1`, and sifts down `10` against children (`4` and none) $\implies [4, 10]$ (outputs `null`).
9. `changeKey 0 16`: Changes `heap[0]` to `16` $\implies [16, 10]$. Sifts down `16` against child `10` $\implies [10, 16]$ (outputs `null`).
10. `getMin`: The root element is `10` (outputs `10`).

---

### Example 2

**Input:**
```text
11
initializeHeap
insert 4
insert 1
extractMin
getMin
insert 1
heapSize
isEmpty
extractMin
changeKey 0 2
getMin
```

**Output:**
```text
null null null null 4 null 2 0 null null 2
```

**Explanation:**
1. `initializeHeap`: Initializes empty heap $\implies []$ (outputs `null`).
2. `insert 4`: Heap is $[4]$ (outputs `null`).
3. `insert 1`: Heap is $[1, 4]$ (outputs `null`).
4. `extractMin`: Removes root `1`, last element `4` becomes root $\implies [4]$ (outputs `null`).
5. `getMin`: Root is `4` (outputs `4`).
6. `insert 1`: Appends `1` and sifts up $\implies [1, 4]$ (outputs `null`).
7. `heapSize`: Number of elements is 2 (outputs `2`).
8. `isEmpty`: Heap is not empty (outputs `0`).
9. `extractMin`: Removes root `1`, `4` becomes root $\implies [4]$ (outputs `null`).
10. `changeKey 0 2`: Updates `heap[0]` from `4` to `2` $\implies [2]$ (outputs `null`).
11. `getMin`: Root is `2` (outputs `2`).

---

## Constraints

- $1 \le Q \le 10^5$
- $-10^5 \le \text{key}, \text{newVal} \le 10^5$
- For `changeKey`, $0 \le \text{index} < \text{heapSize}$.
- `getMin` and `extractMin` are guaranteed to be called only when the heap is non-empty ($\text{heapSize} > 0$).
