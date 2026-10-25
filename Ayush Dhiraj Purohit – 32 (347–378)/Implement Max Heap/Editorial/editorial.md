# Editorial: Implement Max Heap

## 1. Problem Overview

A **Binary Max-Heap** is a specialized tree-based data structure that satisfies the **max-heap property**: the value of each node is less than or equal to the value of its parent, with the maximum value maintained at the root.

Max-heaps are an essential building block in computer science, powering:
- Priority queues that order tasks by highest priority first.
- Operating system schedulers for prioritizing high-urgency interrupts or processes.
- Heap Sort ($\mathcal{O}(N \log N)$ in-place sorting).
- Streaming algorithms finding the $K$-th smallest/largest elements.
- Graph algorithms (e.g., finding maximum bandwidth paths).

In this problem, we design and implement a production-grade `MaxHeap` supporting:
1. `initializeHeap()`: Clears and resets the heap to an empty state.
2. `insert(key)`: Inserts a new element into the heap.
3. `changeKey(index, newVal)`: Modifies an element at a given 0-based index and restores the max-heap invariant.
4. `extractMax()`: Removes the root element (maximum value) and re-heapifies.
5. `isEmpty()`: Checks whether the heap currently holds zero elements.
6. `getMax()`: Returns the maximum element without removal.
7. `heapSize()`: Returns the number of active elements in the heap.

---

## 2. Array Representation of a Binary Heap

A binary heap is always maintained as a **complete binary tree** (every level is fully filled, except possibly the deepest level, which is populated strictly from left to right). Because of this structural invariant, a complete binary tree can be mapped directly to a contiguous, 0-indexed dynamic array without requiring pointers or node objects:

- **Root:** Always stored at index `0`.
- **Parent of node at index $i$:**
  $$\text{parent}(i) = \left\lfloor \frac{i - 1}{2} \right\rfloor \quad (\text{for } i > 0)$$
- **Left child of node at index $i$:**
  $$\text{leftChild}(i) = 2i + 1$$
- **Right child of node at index $i$:**
  $$\text{rightChild}(i) = 2i + 2$$
- **Max-Heap Invariant:**
  $$\text{heap}[\text{parent}(i)] \ge \text{heap}[i] \quad \forall i > 0$$
  Equivalently, for every node $i$:
  $$\text{heap}[i] \ge \text{heap}[2i + 1] \quad \text{and} \quad \text{heap}[i] \ge \text{heap}[2i + 2]$$
  (whenever the corresponding child index is within the heap size).

---

## 3. Detailed Operation Logic

### 3.1 `initializeHeap()`
- Resets `size = 0`.
- Retains or reallocates the underlying array buffer.
- **Time Complexity:** $\mathcal{O}(1)$.
- **Space Complexity:** $\mathcal{O}(1)$.

### 3.2 `isEmpty()`
- Returns `1` if `size == 0`, and `0` otherwise.
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.3 `heapSize()`
- Returns the current integer `size`.
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.4 `getMax()`
- Due to the max-heap property, the maximum element is always at the root (`heap[0]`).
- If `size == 0`, return a sentinel (such as `-1`).
- **Time Complexity:** $\mathcal{O}(1)$.

### 3.5 `insert(int key)`
1. Append the new `key` at the next available leaf position: index `size`.
2. Increment `size` by `1`.
3. The newly inserted element might violate the max-heap property with its ancestors if it is strictly greater than its parent. To restore the invariant, we perform **Sift-Up (Heapify-Up)**:
   - While $i > 0$ and $\text{heap}[i] > \text{heap}[\text{parent}(i)]$:
     - Swap $\text{heap}[i]$ with $\text{heap}[\text{parent}(i)]$.
     - Move index $i \leftarrow \text{parent}(i)$.
   - Terminate when the root is reached ($i = 0$) or the parent's value is greater than or equal to $\text{heap}[i]$.
- **Time Complexity:** $\mathcal{O}(\log N)$, since a complete binary tree of $N$ nodes has height $\lfloor \log_2 N \rfloor$.

### 3.6 `extractMax()`
1. If `size == 0`, return immediately.
2. The root (`heap[0]`) holds the maximum element.
3. Replace the root `heap[0]` with the last element in the heap: `heap[size - 1]`.
4. Decrement `size` by `1`.
5. If `size > 0`, the new root might violate the max-heap invariant with its descendants. To restore order, perform **Sift-Down (Heapify-Down)** starting at $i = 0$:
   - Let `largest = i`.
   - Check left child ($2i + 1 < \text{size}$): if $\text{heap}[2i + 1] > \text{heap}[\text{largest}]$, update `largest = 2i + 1`.
   - Check right child ($2i + 2 < \text{size}$): if $\text{heap}[2i + 2] > \text{heap}[\text{largest}]$, update `largest = 2i + 2`.
   - **Tie-Breaking Rule:** If both left and right children are equal and strictly greater than the current node, checking the right child with a strict `>` condition preserves the left child as `largest`, thus prioritizing swapping with the **left child**.
   - If `largest != i`, swap $\text{heap}[i]$ with $\text{heap}[\text{largest}]$ and continue sifting down at `largest`.
   - If `largest == i`, the node satisfies the max-heap condition and the sift-down terminates.
- **Time Complexity:** $\mathcal{O}(\log N)$.

### 3.7 `changeKey(int index, int newVal)`
1. Read the previous value: `oldVal = heap[index]`.
2. Update the array at `index`: `heap[index] = newVal`.
3. If `newVal > oldVal`:
   - The key increased. It cannot violate the max-heap invariant with its children, but it may now exceed its parent.
   - Perform **Sift-Up** from `index`.
4. If `newVal < oldVal`:
   - The key decreased. It cannot violate the max-heap invariant with its parent, but it may now be smaller than one or both children.
   - Perform **Sift-Down** from `index`.
5. If `newVal == oldVal`:
   - Invariant remains intact; no swaps needed.
- **Time Complexity:** $\mathcal{O}(\log N)$.

---

## 4. Edge-Case Analysis

1. **Single-Element Operations:**
   - Inserting into an empty heap places the item at index `0`. Sift-up terminates immediately because $i = 0$.
   - Extracting the only element decrements size to `0`; no sift-down is triggered.
   - `changeKey(0, newVal)` updates the root; since it has neither parent nor children, no swaps take place.
2. **Duplicate Elements:**
   - When keys with identical values are inserted, the strict inequalities (`>` rather than `>=`) prevent redundant swaps during sift-up and sift-down, ensuring structural stability and $\mathcal{O}(1)$ insertion for identical streams.
3. **Tie-Breaking Equality During Sift-Down:**
   - If both left and right children are equal and strictly larger than the node, the algorithm compares `heap[left] > heap[largest]` (which sets `largest = left`), and then `heap[right] > heap[largest]` evaluates to false. Hence, `largest` remains `left`, strictly conforming to the standard problem specification.
4. **Extreme / Negative Values:**
   - Keys span standard 32-bit signed integer ranges $[-10^9, 10^9]$. Using signed integers ensures negative values and zero are correctly ordered (e.g., $-5 > -10$).
5. **Large Consecutive Operations:**
   - With $Q = 10^5$, amortized resizing of the internal primitive array `int[]` guarantees zero memory thrashing and prevents garbage collector overhead.

---

## 5. Complexity Summary

| Operation | Time Complexity | Auxiliary Space Complexity |
| :--- | :--- | :--- |
| `initializeHeap()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |
| `insert(key)` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ |
| `changeKey(index, newVal)` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ |
| `extractMax()` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ |
| `isEmpty()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |
| `getMax()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |
| `heapSize()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |

- **Total Time Complexity:** For $Q$ operations, the overall execution time is bounded by $\mathcal{O}(Q \log Q)$, executing within ~50ms in Java for $Q = 10^5$.
- **Total Space Complexity:** $\mathcal{O}(N)$ where $N \le Q$ is the maximum simultaneous heap size, occupying less than 2 MB of memory.
