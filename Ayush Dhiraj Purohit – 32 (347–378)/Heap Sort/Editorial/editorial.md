# Editorial: Heap Sort

## 1. Problem Overview

**Heap Sort** is a comparison-based in-place sorting algorithm with a guaranteed $\mathcal{O}(N \log N)$ time complexity in all cases (worst, average, and best). It leverages the properties of a **Binary Max-Heap** to systematically extract elements in order.

### Comparison with Other Sorting Algorithms

| Algorithm | Best Time | Average Time | Worst Time | Auxiliary Space | Stability |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Heap Sort** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log N)$ | $\mathcal{O}(1)$ | No |
| **Quick Sort** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N^2)$ | $\mathcal{O}(\log N)$ | No |
| **Merge Sort** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N)$ | Yes |

Unlike Quick Sort, Heap Sort has no catastrophic $\mathcal{O}(N^2)$ worst case. Unlike Merge Sort, it operates **in-place** with $\mathcal{O}(1)$ auxiliary space, making it a critical choice in memory-constrained and mission-critical embedded systems (such as Linux kernel schedulers and aerospace software).

---

## 2. Array Representation of a Binary Heap

A binary heap is a complete binary tree laid out sequentially in level-order in an array:
- **Root:** index `0`.
- For any node at index $i$:
  - **Left Child:** $2i + 1$ (valid if $2i + 1 < \text{heapSize}$).
  - **Right Child:** $2i + 2$ (valid if $2i + 2 < \text{heapSize}$).
  - **Parent:** $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).

---

## 3. The Two-Phase Heap Sort Algorithm

Heap sort operates in two distinct phases:

### Phase 1: Build Max-Heap ($\mathcal{O}(N)$)
1. Treat the arbitrary unsorted array as a complete binary tree.
2. In a 0-indexed complete binary tree of size $N$, all nodes from index $\lfloor N / 2 \rfloor$ to $N - 1$ are leaf nodes. A leaf node trivially satisfies the heap property because it has no children.
3. Iterate backward from the last non-leaf node down to the root:
   $$i = \left\lfloor \frac{N}{2} \right\rfloor - 1 \quad \text{down to} \quad 0$$
4. At each index $i$, call `siftDown(nums, i, N)`:
   - Find the largest element among node $i$, its left child, and its right child.
   - If the largest is not $i$, swap `nums[i]` with the largest child and recursively/iteratively continue sifting down.
5. After completing Phase 1, the maximum element in the entire array is at `nums[0]`.

### Phase 2: In-Place Extraction & Sorting ($\mathcal{O}(N \log N)$)
1. For $i = N - 1$ down to $1$:
   - The root `nums[0]` is the largest element among the remaining unsorted prefix $[0, i]$.
   - Swap `nums[0]` with `nums[i]`. Now `nums[i]` is in its final sorted position.
   - The active heap size is reduced from $i + 1$ to $i$.
   - The new root `nums[0]` may violate the max-heap property. Restore the heap by calling `siftDown(nums, 0, i)`.
2. Once $i = 1$ is processed, all elements are placed in non-decreasing order.

---

## 4. Complexity Analysis

### 4.1 Time Complexity
- **Phase 1 (Build Heap):** $\mathcal{O}(N)$.
  - Although each sift-down can take up to $\mathcal{O}(h)$ time, the majority of nodes are located near the bottom of the tree with very small heights.
  - The aggregate work is given by:
    $$\sum_{h=0}^{\lfloor \log_2 N \rfloor} \left\lceil \frac{N}{2^{h+1}} \right\rceil \mathcal{O}(h) = \mathcal{O}\left(N \sum_{h=0}^{\infty} \frac{h}{2^h}\right) = \mathcal{O}(N)$$
- **Phase 2 (Sorting):** $\mathcal{O}(N \log N)$.
  - We perform $N - 1$ extractions.
  - In each extraction, the root travels from the top to at most height $\log_2 i$.
  - Total work:
    $$\sum_{i=1}^{N-1} \mathcal{O}(\log_2 i) = \mathcal{O}(\log_2 (N!)) = \mathcal{O}(N \log N)$$
- **Overall Time Complexity:**
  $$\mathcal{O}(N) + \mathcal{O}(N \log N) = \mathcal{O}(N \log N) \quad \text{(Best, Average, and Worst Case)}$$

### 4.2 Space Complexity
- **Auxiliary Space:** $\mathcal{O}(1)$.
  - All operations (swaps and sift-downs) are executed strictly in-place on the input array.
  - Using an **iterative** implementation of `siftDown` eliminates function call recursion stack overhead entirely, ensuring $\mathcal{O}(1)$ space safety even for $N = 10^5$.

---

## 5. Stability Analysis

Heap sort is **unstable**.
During the heapification and root-swap steps, elements with identical values can be moved across large distances in the array. For example, in `[3a, 3b, 1]`, building the heap and swapping roots places `3a` behind `3b`, inverting their relative input order.

---

## 6. Detailed Walkthrough: `nums = [7, 4, 1, 5, 3]`

1. **Input:** `[7, 4, 1, 5, 3]`, $N = 5$.
2. **Phase 1 (Build Max-Heap):**
   - $\text{start} = \lfloor 5 / 2 \rfloor - 1 = 1$.
   - $i = 1$ (`4`): Left child is `5`, right child is `3`. Largest is `5`. Swap `nums[1]` and `nums[3]` $\implies [7, 5, 1, 4, 3]$.
   - $i = 0$ (`7`): Left child is `5`, right child is `1`. Largest is `7`. No swap needed.
   - Max-heap built: `[7, 5, 1, 4, 3]`.
3. **Phase 2 (Sorting):**
   - **$i = 4$:** Swap `nums[0]` (7) and `nums[4]` (3) $\implies [3, 5, 1, 4, \mathbf{7}]$.
     - `siftDown(0, 4)`: At index 0, largest child is `nums[1] = 5`. Swap $\implies [5, 3, 1, 4, \mathbf{7}]$.
     - At index 1, largest child is `nums[3] = 4`. Swap $\implies [5, 4, 1, 3, \mathbf{7}]$.
   - **$i = 3$:** Swap `nums[0]` (5) and `nums[3]` (3) $\implies [3, 4, 1, \mathbf{5}, \mathbf{7}]$.
     - `siftDown(0, 3)`: At index 0, largest child is `nums[1] = 4`. Swap $\implies [4, 3, 1, \mathbf{5}, \mathbf{7}]$.
   - **$i = 2$:** Swap `nums[0]` (4) and `nums[2]` (1) $\implies [1, 3, \mathbf{4}, \mathbf{5}, \mathbf{7}]$.
     - `siftDown(0, 2)`: At index 0, largest child is `nums[1] = 3`. Swap $\implies [3, 1, \mathbf{4}, \mathbf{5}, \mathbf{7}]$.
   - **$i = 1$:** Swap `nums[0]` (3) and `nums[1]` (1) $\implies [\mathbf{1}, \mathbf{3}, \mathbf{4}, \mathbf{5}, \mathbf{7}]$.
4. **Final Array:** `[1, 3, 4, 5, 7]`.

---

## 7. Edge-Case Analysis

1. **Single Element ($N = 1$):**
   - Loops for Phase 1 and Phase 2 do not execute. The array is trivially sorted in $\mathcal{O}(1)$ time.
2. **Two Elements ($N = 2$):**
   - If `nums[0] > nums[1]`, swap occurs during Phase 2. Handled correctly with 1 comparison.
3. **Identical Elements ($[c, c, \dots, c]$):**
   - Strict `>` comparisons ensure zero unnecessary swaps occur during Phase 1 sift-down.
4. **Already Sorted Array ($[1, 2, \dots, N]$):**
   - Phase 1 converts the sorted array into a max-heap; Phase 2 sorts it back. Time remains $\mathcal{O}(N \log N)$.
5. **Reverse Sorted Array ($[N, N-1, \dots, 1]$):**
   - Phase 1 takes $\mathcal{O}(1)$ swaps because a reverse sorted array is already almost a max-heap.
6. **Negative Numbers & Large 32-bit Values:**
   - Works seamlessly with standard signed integers in the range $[-10^9, 10^9]$.
