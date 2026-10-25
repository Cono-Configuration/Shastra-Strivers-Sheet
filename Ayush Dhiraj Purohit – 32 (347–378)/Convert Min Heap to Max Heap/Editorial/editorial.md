# Editorial: Convert Min Heap to Max Heap

## 1. Problem Overview

We are given an array `nums` of size $N$ that already satisfies the **min-heap property**:
$$\text{nums}[\text{parent}(i)] \le \text{nums}[i] \quad \forall i > 0$$
Our objective is to convert this array in-place into a valid **binary max-heap**, which satisfies:
$$\text{nums}[\text{parent}(i)] \ge \text{nums}[i] \quad \forall i > 0$$

Both a min-heap and a max-heap share the exact same structural property: a **Complete Binary Tree** stored sequentially in level-order inside an array. Because the physical structure is already complete and contiguous, no nodes need to be added or deleted, and no tree rearrangement or re-allocation is necessary.

---

## 2. Approach Analysis

### 2.1 Suboptimal Approach: Full Sorting ($\mathcal{O}(N \log N)$)
One naive approach is to sort the array in descending order using merge sort, quicksort, or `Arrays.sort()`.
While a descending sorted array trivially satisfies the max-heap property, sorting requires $\mathcal{O}(N \log N)$ time and $\mathcal{O}(N)$ auxiliary space (or $\mathcal{O}(\log N)$ stack space). Furthermore, a heap is **not** required to be fully sorted—only parent-child relationships must satisfy the inequality.

### 2.2 Optimal Approach: Bottom-Up Max-Heapify ($\mathcal{O}(N)$)
Instead of sorting, we can treat the array as a complete binary tree and apply **Floyd's Heap Construction Algorithm** adapted for max-heaps:
1. All leaf nodes (nodes that have no children) trivially satisfy the max-heap property because they have no descendants.
2. In a 0-indexed complete binary tree of size $N$, all nodes from index $\lfloor N / 2 \rfloor$ to $N - 1$ are leaf nodes.
3. The last internal (non-leaf) node is located at:
   $$\text{lastInternal} = \left\lfloor \frac{N - 2}{2} \right\rfloor = \left\lfloor \frac{N}{2} \right\rfloor - 1$$
4. We iterate backward from this last internal node down to the root ($i = \text{lastInternal}, \dots, 0$) and apply **Max-Heapify (Sift-Down)** at each node $i$.
5. When we call `maxHeapify(i)`, the subtrees rooted at the left and right children are already valid max-heaps (thanks to the bottom-up processing order). Sifting down node $i$ merges the two valid subtrees into a single larger valid max-heap rooted at $i$.
6. When the loop reaches and completes for the root ($i = 0$), the entire array is guaranteed to be a valid max-heap.

---

## 3. Mathematical Proof of $\mathcal{O}(N)$ Complexity

While each individual `maxHeapify` call can traverse up to $\mathcal{O}(h)$ levels (where $h$ is the height of node $i$), most nodes reside close to the bottom of the tree:
- There are at most $\lceil N / 2^{h+1} \rceil$ nodes at height $h$.
- The maximum height of the tree is $H = \lfloor \log_2 N \rfloor$.

The total work performed across all internal nodes is bounded by:
$$T(N) = \sum_{h=0}^{\lfloor \log_2 N \rfloor} \left\lceil \frac{N}{2^{h+1}} \right\rceil \mathcal{O}(h) \le cN \sum_{h=0}^{\infty} \frac{h}{2^h}$$

The infinite series $\sum_{h=0}^{\infty} \frac{h}{2^h}$ converges to:
$$\sum_{h=0}^{\infty} \frac{h}{2^h} = 2$$

Therefore:
$$T(N) \le 2cN = \mathcal{O}(N)$$

This confirms that bottom-up heap construction runs in **strictly linear time $\mathcal{O}(N)$**, which is asymptotically optimal.

---

## 4. Iterative Sift-Down Algorithm

To guarantee stack safety and prevent deep recursion call overhead for $N = 10^5$, `maxHeapify` is implemented iteratively:

```java
private static void siftDown(int[] nums, int i, int n) {
    while (2 * i + 1 < n) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (nums[left] > nums[largest]) {
            largest = left;
        }
        if (right < n && nums[right] > nums[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = nums[i];
            nums[i] = nums[largest];
            nums[largest] = temp;
            i = largest;
        } else {
            break;
        }
    }
}
```

---

## 5. Dry-Run Walkthrough

### Example 1: `nums = [10, 20, 30, 21, 23]`, $N = 5$
- Last non-leaf node: $\lfloor (5 - 2) / 2 \rfloor = 1$.
- **$i = 1$ (`nums[1] = 20`):**
  - Left child: `nums[3] = 21`. Right child: `nums[4] = 23`.
  - Largest is index 4 (`23`).
  - Swap `nums[1]` (20) with `nums[4]` (23) $\implies [10, 23, 30, 21, 20]$.
  - Node 4 is a leaf $\implies$ sift-down terminates.
- **$i = 0$ (`nums[0] = 10`):**
  - Left child: `nums[1] = 23`. Right child: `nums[2] = 30`.
  - Largest is index 2 (`30`).
  - Swap `nums[0]` (10) with `nums[2]` (30) $\implies [30, 23, 10, 21, 20]$.
  - Node 2 has children at $2(2) + 1 = 5 \ge 5$ (leaf) $\implies$ sift-down terminates.
- **Result:** `[30, 23, 10, 21, 20]`.
  - Root $30 \ge \{23, 10\}$; node $23 \ge \{21, 20\}$. Valid max-heap.

---

## 6. Edge-Case Analysis

1. **Single Element ($N = 1$):**
   - $\lfloor (1 - 2) / 2 \rfloor = 0 / 2 - 1 = -1$. The loop condition $i \ge 0$ is immediately false. Array `[x]` remains unchanged and is trivially a valid max-heap.
2. **Two Elements ($N = 2$):**
   - $\lfloor (2 - 2) / 2 \rfloor = 0$. Node at index 0 has one left child at index 1. If `nums[0] < nums[1]`, they are swapped; otherwise unchanged.
3. **Duplicate & Identical Elements:**
   - E.g., `[7, 7, 7, 7, 7]`.
   - Strict `>` comparisons prevent unnecessary swaps, completing in $\mathcal{O}(N)$ with 0 array writes.
4. **Strictly Increasing / Ordered Min-Heap:**
   - E.g., `[1, 2, 3, 4, 5, 6, 7]`.
   - The root elements will travel the maximum possible distance downward to become leaves, demonstrating worst-case sift-down depth while still respecting the $\mathcal{O}(N)$ bound.
5. **Negative Numbers & Extreme 32-bit Values:**
   - The logic relies on standard signed comparisons (`nums[child] > nums[largest]`), handling $[-10^9, 10^9]$ seamlessly without overflow.

---

## 7. Complexity Summary

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N)$ | Bottom-up heapify processes $\approx N/2$ non-leaf nodes with aggregate travel bounded by $\sum h/2^h = \mathcal{O}(N)$. |
| **Auxiliary Space** | $\mathcal{O}(1)$ | Array is modified strictly in-place; the iterative loop uses $\mathcal{O}(1)$ primitive registers. |
