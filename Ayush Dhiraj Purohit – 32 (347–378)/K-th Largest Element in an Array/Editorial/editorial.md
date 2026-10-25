# Editorial: K-th Largest Element in an Array

## 1. Problem Overview

Given an array of integers `nums` of size $N$ and an integer $K$ ($1 \le K \le N$), our objective is to find the $K^{\text{th}}$ largest element in the array.

### Important Distinction
The problem asks for the **$K^{\text{th}}$ largest element in sorted order**, not the $K^{\text{th}}$ distinct element. For instance, in `[2, 2, 1]`:
- The $1^{\text{st}}$ largest element is `2`.
- The $2^{\text{nd}}$ largest element is `2`.
- The $3^{\text{rd}}$ largest element is `1`.

---

## 2. Approach Analysis

### Approach 1: Full Sorting (Brute Force)
- Sort the entire array `nums` in descending order (or ascending order and select index $N - K$).
- **Time Complexity:** $\mathcal{O}(N \log N)$.
- **Space Complexity:** $\mathcal{O}(1)$ (in-place) or $\mathcal{O}(N)$ (if a copy is created).
- **Limitation:** Sorting does extra work by finding the complete relative order of all $N$ elements, whereas we only need the element at rank $K$.

### Approach 2: Min-Heap of Size $K$ (Optimized & Robust)
- We maintain a **Binary Min-Heap** of capacity $K$ representing the $K$ largest elements discovered so far.
- **Invariant:** The heap contains at most $K$ elements. The root of the min-heap always holds the minimum among these $K$ elements, which is precisely the $K^{\text{th}}$ largest element seen so far.
- **Algorithm:**
  1. Insert the first $K$ elements of `nums` into the min-heap and heapify.
  2. For each remaining element $x \in \text{nums}[K \dots N - 1]$:
     - If $x > \text{heapRoot}$:
       - Replace the root with $x$.
       - Perform a Sift-Down (heapify-down) on the root to restore the min-heap invariant.
     - If $x \le \text{heapRoot}$:
       - $x$ cannot be part of the top $K$ largest elements, so we discard it in $\mathcal{O}(1)$ time.
  3. After iterating through all $N$ elements, the root of the min-heap holds the $K^{\text{th}}$ largest element.
- **Time Complexity:** $\mathcal{O}(K + (N - K) \log K) = \mathcal{O}(N \log K)$.
- **Space Complexity:** $\mathcal{O}(K)$ auxiliary space.

### Approach 3: Quickselect / Hoare's Selection Algorithm (Average Linear Time)
- Based on QuickSort's partitioning mechanism:
  1. Pick a pivot element (randomly or using median-of-three to avoid adversarial patterns).
  2. Partition the array around the pivot such that elements greater than the pivot are to the left, and elements smaller are to the right.
  3. If the pivot ends up at index $K - 1$, the pivot is the $K^{\text{th}}$ largest element.
  4. If the pivot index is greater than $K - 1$, search only the left partition; otherwise, search the right partition.
- **Time Complexity:** Average $\mathcal{O}(N)$, worst-case $\mathcal{O}(N^2)$ (mitigated via 3-way partitioning and randomized pivot selection).
- **Space Complexity:** $\mathcal{O}(1)$ auxiliary space when implemented iteratively.

---

## 3. Comparison of Approaches

| Metric | Full Sorting | Min-Heap of Size $K$ | Quickselect (Average) |
| :--- | :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log K)$ | $\mathcal{O}(N)$ average, $\mathcal{O}(N^2)$ worst |
| **Auxiliary Space** | $\mathcal{O}(1)$ | $\mathcal{O}(K)$ | $\mathcal{O}(1)$ |
| **Streaming / Online** | No (requires full array) | **Yes** (processes elements one-by-one) | No |
| **Worst-Case Guarantee** | Yes | **Yes** | No (probabilistic) |

For competitive programming and production pipelines, the **Min-Heap approach** is exceptionally fast, cache-friendly, guarantees strictly bounded runtime without worst-case degradation, and operates seamlessly on streaming data.

---

## 4. Dry-Run Walkthrough: Example 1

**Input:** `nums = [1, 2, 3, 4, 5]`, $K = 2$.
1. Initialize Min-Heap of size $K = 2$ with the first 2 elements:
   - Insert `1`: `[1]`
   - Insert `2`: `[1, 2]`
   - Current $K^{\text{th}}$ largest: root = `1`.
2. Process `x = 3`:
   - $3 > \text{root} (1) \implies$ replace root with `3`: `[3, 2]`.
   - Sift-down: swap $3$ and $2 \implies [2, 3]$.
3. Process `x = 4`:
   - $4 > \text{root} (2) \implies$ replace root with `4`: `[4, 3]`.
   - Sift-down: swap $4$ and $3 \implies [3, 4]$.
4. Process `x = 5`:
   - $5 > \text{root} (3) \implies$ replace root with `5`: `[5, 4]`.
   - Sift-down: swap $5$ and $4 \implies [4, 5]$.
5. Final root value: `4`. Output = `4`.

---

## 5. Edge-Case Analysis

1. **$K = 1$ (Maximum Element):**
   - The min-heap has size 1. Every incoming element larger than the current root replaces it. The result is the overall maximum in $\mathcal{O}(N)$ comparisons.
2. **$K = N$ (Minimum Element):**
   - The min-heap has size $N$. Building the heap takes $\mathcal{O}(N)$ time. The root is the minimum element of the entire array.
3. **$N = 1, K = 1$:**
   - Single-element array; returns `nums[0]` in $\mathcal{O}(1)$ time.
4. **All Elements Identical:**
   - E.g., `nums = [7, 7, 7, 7], K = 3`.
   - The root will always be `7`, correctly outputting `7`.
5. **Negative Numbers & Extreme 32-bit Values:**
   - Works seamlessly with standard signed integers $[-10^9, 10^9]$.
