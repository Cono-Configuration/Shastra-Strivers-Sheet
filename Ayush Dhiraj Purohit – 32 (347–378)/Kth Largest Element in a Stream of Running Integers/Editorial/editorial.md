# Editorial: Kth Largest Element in a Stream of Running Integers

## 1. Problem Overview

In a data stream environment, integers arrive sequentially one by one. After each incoming integer, we must determine the $K^{\text{th}}$ largest element among all integers observed so far.

The key challenge is that the stream is dynamic and potentially unbounded:
- An offline algorithm that re-sorts all observed elements upon each insertion requires $\mathcal{O}(M \log M)$ time per query (where $M$ is the number of elements processed so far), resulting in an unacceptable $\mathcal{O}(Q^2 \log Q)$ cumulative runtime.
- We need an online data structure that processes each insertion and answers the query in sub-linear time.

---

## 2. The Min-Heap Invariant

The fundamental insight is that **we only need to retain the $K$ largest elements seen so far**.

Consider an element $x$:
- If there are already at least $K$ elements strictly greater than or equal to $x$, $x$ cannot be among the top $K$ largest elements now.
- Furthermore, as future elements arrive, the boundary for the top $K$ can only increase (or remain equal), never decrease. Therefore, any element outside the top $K$ can **never** re-enter the top $K$. It can be safely discarded forever.

By storing the top $K$ largest elements in a **Binary Min-Heap** of capacity $K$:
1. The heap contains exactly $K$ elements (once at least $K$ elements have been observed).
2. The root of the min-heap holds the smallest value among the top $K$ elements.
3. The smallest value among the top $K$ largest elements is, by definition, the **$K^{\text{th}}$ largest element** overall.

---

## 3. Algorithm Step-by-Step

### 3.1 Data Structure Representation
We maintain an array-based min-heap of fixed capacity $K$:
- `int[] heap` of size $K$.
- `int size`: number of elements currently stored ($0 \le \text{size} \le K$).

### 3.2 Constructor `KthLargest(int k, int[] nums)`
1. Initialize `heap = new int[k]` and `size = 0`.
2. For each element $x \in \text{nums}$:
   - Insert $x$ using the internal insertion routine.
   - Heap size is kept capped at $K$.

### 3.3 Query `int add(int val)`
1. **Case 1: Heap not yet full (`size < K`)**
   - Place `val` at index `size`.
   - Perform **Sift-Up** to restore min-heap order.
   - Increment `size`.
2. **Case 2: Heap is full (`size == K`)**
   - Compare `val` with the root `heap[0]`.
   - If `val > heap[0]`:
     - The incoming `val` qualifies for the top $K$ largest elements, knocking out the previous $K^{\text{th}}$ largest.
     - Overwrite the root: `heap[0] = val`.
     - Perform **Sift-Down** from the root ($0$) to restore the min-heap order.
   - If `val <= heap[0]`:
     - `val` does not qualify for the top $K$. Discard it immediately in $\mathcal{O}(1)$ time.
3. Return `heap[0]`.

---

## 4. Complexity Analysis

| Operation | Brute Force (Sorting) | Min-Heap of Size $K$ |
| :--- | :--- | :--- |
| **Constructor** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N \log K)$ |
| **`add(val)`** | $\mathcal{O}(M \log M)$ | $\mathcal{O}(\log K)$ worst-case, $\mathcal{O}(1)$ if $val \le \text{root}$ |
| **Total Time ($Q$ queries)** | $\mathcal{O}(Q^2 \log Q)$ (TLE) | $\mathcal{O}((N + Q) \log K)$ |
| **Auxiliary Space** | $\mathcal{O}(N + Q)$ | $\mathcal{O}(K)$ |

- **Time Complexity:** $\mathcal{O}(\log K)$ per query. For $Q = 10^5$ and $K \le 10^5$, all operations complete in under ~35 ms in Java.
- **Auxiliary Space:** $\mathcal{O}(K)$ to store the min-heap of capacity $K$. Primitive array representation avoids boxed integer object overhead.

---

## 5. Dry-Run Walkthrough: Example 1

**Input:** $K = 3$, `nums = [4, 5, 8, 2]`. Queries: `[3, 5, 10, 9, 4]`.

1. **Initialization:**
   - Insert `4` $\implies [4]$ (size 1)
   - Insert `5` $\implies [4, 5]$ (size 2)
   - Insert `8` $\implies [4, 5, 8]$ (size 3, full)
   - Insert `2`: $2 \le \text{root}(4) \implies$ discarded $\implies [4, 5, 8]$
2. **`add(3)`:**
   - $3 \le \text{root}(4) \implies$ discarded.
   - Top of heap is `4`. Output: `4`.
3. **`add(5)`:**
   - $5 > \text{root}(4) \implies \text{heap}[0] = 5 \implies [5, 5, 8]$. Sift down: $[5, 5, 8]$.
   - Top of heap is `5`. Output: `5`.
4. **`add(10)`:**
   - $10 > \text{root}(5) \implies \text{heap}[0] = 10 \implies [10, 5, 8]$. Sift down: $[5, 10, 8]$.
   - Top of heap is `5`. Output: `5`.
5. **`add(9)`:**
   - $9 > \text{root}(5) \implies \text{heap}[0] = 9 \implies [9, 10, 8]$. Sift down: $[8, 10, 9]$.
   - Top of heap is `8`. Output: `8`.
6. **`add(4)`:**
   - $4 \le \text{root}(8) \implies$ discarded.
   - Top of heap is `8`. Output: `8`.
- **Final Result:** `4 5 5 8 8`.

---

## 6. Edge-Case Analysis

1. **$K = 1$ (Running Maximum):**
   - Heap has size 1. The operation simplifies to tracking the running maximum in $\mathcal{O}(1)$ time.
2. **$N = 0$ (Empty Initial Stream):**
   - Initial heap is empty (`size = 0`). The first $K$ calls to `add` populate the heap until `size == K`. Handled seamlessly.
3. **Identical Stream Elements:**
   - E.g., stream of all zeros `[0, 0, 0, ...]`. Since $0 \le 0$, incoming duplicates do not trigger sift-down swaps, executing in $\mathcal{O}(1)$ time per query.
4. **Strictly Increasing Sequence:**
   - Every incoming number is larger than the root, triggering a replacement and sift-down on each query, exercising the $\mathcal{O}(\log K)$ upper bound.
5. **Strictly Decreasing Sequence:**
   - After initial $K$ elements, all subsequent numbers are smaller than the root, discarded in $\mathcal{O}(1)$ time without any heap modifications.
6. **Negative Numbers & Extreme 32-bit Values:**
   - Fully compatible with integers in $[-10^9, 10^9]$.
