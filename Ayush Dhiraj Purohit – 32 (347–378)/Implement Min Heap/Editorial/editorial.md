# Editorial: Implement Min Heap

## 1. Problem Overview

A **Binary Min-Heap** is a specialized tree-based data structure that satisfies the **min-heap property**: the value of each node is greater than or equal to the value of its parent, with the minimum value stored at the root.

Min-heaps serve as the foundational engine for **Priority Queues**, used extensively in:
- Graph algorithms (e.g., Dijkstra's shortest path, Prim's Minimum Spanning Tree).
- Job and process scheduling in operating systems.
- Streaming data analysis (e.g., finding the $K$-th largest/smallest elements).
- Greedy algorithms such as Huffman coding.

In this problem, we design and implement a complete, production-grade `MinHeap` class supporting:
1. `initializeHeap()`: Resets/clears the heap.
2. `insert(key)`: Inserts a new element.
3. `changeKey(index, newVal)`: Modifies an element at a given index and restores heap invariant.
4. `extractMin()`: Removes the root element.
5. `isEmpty()`: Checks if the heap is empty.
6. `getMin()`: Inspects the root element.
7. `heapSize()`: Returns the number of elements.

---

## 2. Array Representation of a Binary Heap

Because a binary heap is always a **complete binary tree** (all levels are completely filled except possibly the last, which is filled from left to right), it can be stored compactly in a contiguous array without pointers:

- **Root:** Positioned at index `0`.
- **Parent of node $i$:**
  $$\text{parent}(i) = \left\lfloor \frac{i - 1}{2} \right\rfloor \quad (\text{for } i > 0)$$
- **Left child of node $i$:**
  $$\text{leftChild}(i) = 2i + 1$$
- **Right child of node $i$:**
  $$\text{rightChild}(i) = 2i + 2$$
- **Min-Heap Property:**
  $$\text{heap}[i] \le \text{heap}[2i + 1] \quad \text{and} \quad \text{heap}[i] \le \text{heap}[2i + 2]$$

---

## 3. Detailed Method Logic

### 3.1 `initializeHeap()`
- Resets the heap by setting `size = 0`.
- If using a dynamic array, can re-instantiate or reuse the existing buffer.
- **Time Complexity:** $\mathcal{O}(1)$.
- **Space Complexity:** $\mathcal{O}(1)$.

### 3.2 `isEmpty()`
- Evaluates whether the number of active elements is `0`:
  $$\text{return } (\text{size} == 0 \,?\, 1 : 0)$$
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.3 `heapSize()`
- Returns the current integer `size`.
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.4 `getMin()`
- In a valid min-heap, the minimum element always resides at index `0`.
- If `size == 0`, return an error/sentinel (e.g., `-1`), though queries are guaranteed on non-empty heaps.
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.5 `insert(int key)`
1. Append `key` at the next available leaf position, which is index `size`.
2. Increment `size` by `1`.
3. The new element might violate the min-heap property with respect to its ancestors. To restore order, perform **Heapify-Up (Sift-Up)**:
   - Compare `heap[i]` with `heap[parent(i)]`.
   - If `heap[i] < heap[parent(i)]`, swap them and set $i \leftarrow \text{parent}(i)$.
   - Repeat until the root is reached ($i = 0$) or `heap[parent(i)] <= heap[i]`.
- **Time Complexity:** $\mathcal{O}(\log N)$ because the height of a complete binary tree with $N$ nodes is $\lfloor \log_2 N \rfloor$.

### 3.6 `extractMin()`
1. If `size == 0`, return immediately.
2. The root `heap[0]` holds the minimum element.
3. Overwrite `heap[0]` with the last element in the heap: `heap[size - 1]`.
4. Decrement `size` by `1`.
5. The new root element might violate the min-heap property with respect to its descendants. To restore order, perform **Heapify-Down (Sift-Down)**:
   - Let current index be $i = 0$.
   - Find the index of the smallest value among `heap[i]`, `heap[2i + 1]` (if valid), and `heap[2i + 2]` (if valid).
   - If the smallest is not $i$, swap `heap[i]` with `heap[smallest]` and continue sifting down from $smallest$.
   - Repeat until $i$ is a leaf or `heap[i]` is smaller than or equal to both children.
- **Time Complexity:** $\mathcal{O}(\log N)$.

### 3.7 `changeKey(int index, int newVal)`
1. Retrieve the current value `oldVal = heap[index]`.
2. Update `heap[index] = newVal`.
3. If `newVal < oldVal`:
   - The value decreased. It cannot violate the min-heap invariant with its children, but it may now be smaller than its parent.
   - Perform **Sift-Up** from `index`.
4. If `newVal > oldVal`:
   - The value increased. It cannot violate the min-heap invariant with its parent, but it may now be greater than its children.
   - Perform **Sift-Down** from `index`.
5. If `newVal == oldVal`:
   - No change needed.
- **Time Complexity:** $\mathcal{O}(\log N)$.

---

## 4. Algorithmic Implementation (Iterative & Stack-Safe)

### Sift-Up Implementation
```java
private void siftUp(int i) {
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap[i] < heap[parent]) {
            swap(i, parent);
            i = parent;
        } else {
            break;
        }
    }
}
```

### Sift-Down Implementation
```java
private void siftDown(int i) {
    while (2 * i + 1 < size) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int smallest = i;

        if (heap[left] < heap[smallest]) {
            smallest = left;
        }
        if (right < size && heap[right] < heap[smallest]) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            i = smallest;
        } else {
            break;
        }
    }
}
```

---

## 5. Dry Run of Example 1

**Operations:**
1. `initializeHeap`: `heap = []`, `size = 0` $\implies$ Output: `null`
2. `insert 4`: `heap = [4]`, `size = 1` $\implies$ Output: `null`
3. `insert 1`:
   - Append `1`: `[4, 1]`.
   - `parent((1-1)/2) = 0`. `heap[1] = 1 < heap[0] = 4`. Swap $\implies [1, 4]$.
   - Output: `null`
4. `insert 10`:
   - Append `10`: `[1, 4, 10]`.
   - `parent((2-1)/2) = 0`. `heap[2] = 10 >= heap[0] = 1`. No swap $\implies [1, 4, 10]$.
   - Output: `null`
5. `getMin`: Root is `heap[0] = 1` $\implies$ Output: `1`
6. `heapSize`: `size = 3` $\implies$ Output: `3`
7. `isEmpty`: `size == 0 ? 1 : 0` $\implies$ Output: `0`
8. `extractMin`:
   - Root is `1`. Replace with last element `10`: `heap[0] = 10`.
   - Decrement `size` to `2`.
   - Heap array before sift-down: `[10, 4]`.
   - At index `0`: Children are left = `4` (index 1). Smallest is `4` at index 1.
   - Swap index 0 and 1 $\implies [4, 10]$.
   - Output: `null`
9. `changeKey 0 16`:
   - `oldVal = 4`, `newVal = 16`. Since $16 > 4$, sift down.
   - `heap[0] = 16`. Array: `[16, 10]`.
   - Compare index 0 with left child index 1 (`10`). $10 < 16$, swap $\implies [10, 16]$.
   - Output: `null`
10. `getMin`: Root is `heap[0] = 10` $\implies$ Output: `10`

**Final Output Sequence:**
```text
null null null null 1 3 0 null null 10
```

---

## 6. Edge Cases & Safety Analysis

1. **Repeated Insertions and Extractions:**
   - Testing a dynamic capacity growth strategy: doubling the capacity whenever the array is full prevents `ArrayIndexOutOfBoundsException` while maintaining amortized $\mathcal{O}(1)$ insertion.
2. **Key Changes at Boundary Indices:**
   - Changing the root ($index = 0$): Can only sift down.
   - Changing a leaf node: Can only sift up.
   - Sift-up and sift-down safely terminate when bounds are hit.
3. **Duplicate Keys:**
   - Sifting checks use strict `<` conditions (`heap[i] < heap[parent]` and `heap[child] < heap[smallest]`), ensuring stable termination when duplicates exist.
4. **Stack Safety:**
   - Both `siftUp` and `siftDown` are implemented iteratively with `while` loops, preventing `StackOverflowError` regardless of tree depth ($Q \le 10^5$).

---

## 7. Complexity Summary

| Operation | Time Complexity | Auxiliary Space Complexity | Explanation |
| :--- | :--- | :--- | :--- |
| `initializeHeap()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ | Resets pointer/size |
| `insert(key)` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ | Sifts up at most tree height |
| `getMin()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ | Direct array access at index `0` |
| `extractMin()` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ | Moves last element to root and sifts down |
| `changeKey(index, val)` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ | Either sifts up or sifts down |
| `heapSize()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ | Returns size counter |
| `isEmpty()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ | Checks if size is zero |
| **Total Memory** | — | $\mathcal{O}(N)$ | Contiguous array storage for $N$ elements |
