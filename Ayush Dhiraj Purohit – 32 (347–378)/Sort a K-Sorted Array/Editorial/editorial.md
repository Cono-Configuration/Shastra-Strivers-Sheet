# Editorial: Sort a K-Sorted Array

## 1. Problem Overview

We are given an array `nums` of size $N$ that is **$K$-sorted** (also known as nearly sorted): every element is at most $K$ positions away from its target position in the sorted array.

### Why Does Bounded Displacement Help?
In a completely arbitrary unsorted array, the smallest element could be located anywhere from index `0` to $N - 1$, requiring $\Omega(N \log N)$ comparison-based sorting work.
However, in a $K$-sorted array:
- The smallest element in the entire array **must** reside within the first $K + 1$ elements (indices `0` through $K$).
- More generally, for any output position $i$, the element that belongs at index $i$ must reside in the window $[i, \min(N - 1, i + K)]$.

This bounded localization allows us to use a **sliding window of size $K + 1$**, reducing the sorting complexity from $\mathcal{O}(N \log N)$ down to $\mathcal{O}(N \log K)$.

---

## 2. Approach Analysis

### Approach 1: Generic Full Sorting ($\mathcal{O}(N \log N)$)
- Run QuickSort, MergeSort, or standard HeapSort on the entire array.
- **Time Complexity:** $\mathcal{O}(N \log N)$.
- **Limitation:** Fails to exploit the bounded displacement property; does redundant work when $K \ll N$.

### Approach 2: Modified Insertion Sort ($\mathcal{O}(N \cdot K)$)
- Since each element is at most $K$ places away, an element shifts backward by at most $K$ positions during Insertion Sort.
- **Time Complexity:** $\mathcal{O}(N \cdot K)$.
- **Limitation:** Highly efficient when $K$ is tiny ($K \le 5$), but degrades to $\mathcal{O}(N^2)$ when $K$ grows proportionally to $N$.

### Approach 3: Sliding Window Min-Heap ($\mathcal{O}(N \log K)$ — Optimal)
- We maintain a **Binary Min-Heap** containing the next candidate elements for the current output position.
- **Window Size:** The heap contains at most $K + 1$ elements.
- **Invariant:** At any point when writing to index `targetIndex`, the min-heap contains all elements that could legitimately belong at `targetIndex`. The minimum element in the heap is strictly the smallest among all available valid candidates, so extracting the root yields the correct sorted element.

---

## 3. Algorithm Step-by-Step

1. **Edge Cases:** If $N \le 1$ or $K = 0$, the array is already sorted; return immediately.
2. **Initialize Heap:**
   - Create a min-heap with capacity $\min(K + 1, N)$.
   - Insert the first $\min(K + 1, N)$ elements of `nums` into the min-heap.
3. **Sliding Window Processing:**
   - Set `targetIndex = 0`.
   - For each element from index $i = K + 1$ to $N - 1$:
     - The root of the min-heap is the minimum among the current window.
     - Place `heap[0]` at `nums[targetIndex++]`.
     - Replace the root with the incoming element `nums[i]`.
     - Sift-down the root to restore the min-heap property.
4. **Draining the Heap:**
   - While the heap is non-empty:
     - Place the root at `nums[targetIndex++]`.
     - Move the last heap element to the root and sift-down.
5. The array `nums` is now fully sorted in non-decreasing order.

---

## 4. Complexity Analysis

| Metric | Insertion Sort | Full Sorting | Min-Heap Sliding Window |
| :--- | :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N \cdot K)$ | $\mathcal{O}(N \log N)$ | $\mathbf{\mathcal{O}(N \log K)}$ |
| **Auxiliary Space** | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ or $\mathcal{O}(N)$ | $\mathbf{\mathcal{O}(K)}$ |
| **When $K \ll N$** | $\mathcal{O}(N)$ for small $K$ | $\mathcal{O}(N \log N)$ | $\mathbf{\mathcal{O}(N \log K)}$ (near linear) |

- **Time Complexity:**
  - Building initial heap of size $K + 1$: $\mathcal{O}(K)$.
  - For each of the remaining $N - (K + 1)$ elements: extract-min and insert in $\mathcal{O}(\log K)$.
  - Draining remaining $K + 1$ elements: $(K + 1) \times \mathcal{O}(\log K) = \mathcal{O}(K \log K)$.
  - Total Time: $\mathcal{O}(N \log K)$. For $N = 10^5$, this executes in ~20 ms in Java.
- **Auxiliary Space:**
  - $\mathcal{O}(\min(K + 1, N))$ to store the sliding min-heap. When implemented with a primitive `int[]` array, memory overhead is negligible ($< 1$ MB).

---

## 5. Dry-Run Walkthrough: Example 1

**Input:** `nums = [6, 5, 3, 2, 8, 10, 9]`, $N = 7, K = 3$.
Window size: $K + 1 = 4$.

1. **Initialize Min-Heap with first 4 elements:**
   - Insert `6, 5, 3, 2` $\implies$ Heap: `[2, 3, 5, 6]` (Root = `2`).
   - `targetIndex = 0`.
2. **Process remaining elements ($i = 4, 5, 6$):**
   - **$i = 4$ (`nums[4] = 8`):**
     - Extract root `2` $\implies \text{nums}[0] = 2$.
     - Push `8` $\implies$ Heap: `[3, 6, 5, 8]` (Root = `3`).
   - **$i = 5$ (`nums[5] = 10`):**
     - Extract root `3` $\implies \text{nums}[1] = 3$.
     - Push `10` $\implies$ Heap: `[5, 6, 10, 8]` (Root = `5`).
   - **$i = 6$ (`nums[6] = 9`):**
     - Extract root `5` $\implies \text{nums}[2] = 5$.
     - Push `9` $\implies$ Heap: `[6, 8, 10, 9]` (Root = `6`).
3. **Drain remaining heap:**
   - Extract `6` $\implies \text{nums}[3] = 6$.
   - Extract `8` $\implies \text{nums}[4] = 8$.
   - Extract `9` $\implies \text{nums}[5] = 9$.
   - Extract `10` $\implies \text{nums}[6] = 10$.
4. **Final Array:** `[2, 3, 5, 6, 8, 9, 10]`.

---

## 6. Edge-Case Analysis

1. **$K = 0$ (Already Sorted):**
   - Every element is within $0$ positions of its sorted spot. The algorithm detects $K = 0$ and returns immediately in $\mathcal{O}(1)$ time.
2. **$K \ge N - 1$:**
   - Displacements can span the entire array. The heap capacity caps at $N$, gracefully executing in standard HeapSort time $\mathcal{O}(N \log N)$.
3. **$N = 1$:**
   - Single-element array; returns immediately without any operations.
4. **All Elements Identical:**
   - E.g., `[4, 4, 4, 4]`. Sift-down comparisons perform 0 swaps; elements are written back in order in $\mathcal{O}(N)$ time.
5. **Negative Numbers & Extreme 32-bit Values:**
   - Standard signed comparisons handle values in $[-10^9, 10^9]$ correctly.
