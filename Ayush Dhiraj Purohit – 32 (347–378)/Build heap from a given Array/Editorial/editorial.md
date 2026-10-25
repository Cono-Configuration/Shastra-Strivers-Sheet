# Editorial: Build heap from a given Array

## 1. Problem Overview

We are given an arbitrary integer array `nums` of size $N$. Our goal is to rearrange the elements **in-place** to transform the array into a valid **binary min-heap** in optimal **$\mathcal{O}(N)$** time.

A **binary min-heap** is a complete binary tree satisfying the min-heap property:
- For every node at index $i$:
  - Left child is at index $2i + 1$ (if $2i + 1 < N$).
  - Right child is at index $2i + 2$ (if $2i + 2 < N$).
  - $\text{nums}[i] \le \text{nums}[2i + 1]$ and $\text{nums}[i] \le \text{nums}[2i + 2]$.

---

## 2. Brute Force: Sequential Insertion ($\mathcal{O}(N \log N)$)

### Approach
One intuitive idea is to treat the array as an initially empty heap and insert elements one by one from index $0$ to $N - 1$:
1. For each element at index $k$, place it at the end of the current heap.
2. Sift it up (heapify-up) towards the root until the min-heap property is satisfied.

### Complexity Analysis
- In a tree of size $k$, sifting up takes $\mathcal{O}(\log k)$ time.
- Total time:
  $$\sum_{k=1}^N \mathcal{O}(\log k) = \mathcal{O}(\log(N!)) = \mathcal{O}(N \log N)$$
- This approach is sub-optimal because the majority of nodes (the leaves, which make up roughly $N/2$ of all nodes) are at the deepest level and could potentially travel all the way up to the root ($\approx \log N$ steps each).

---

## 3. Optimal Approach: Bottom-Up Heap Construction (Floyd's Algorithm, $\mathcal{O}(N)$)

### Core Intuition
Instead of pushing elements upwards, we construct the heap **bottom-up** using **Heapify-Down (Sift-Down)**:

1. **Leaf Nodes Already Satisfy the Heap Property:**
   - In a complete binary tree of size $N$, any node with index $i \ge \lfloor N/2 \rfloor$ has no children ($2i + 1 \ge N$).
   - A node with no children is vacuously a valid min-heap of size 1!
   - Therefore, we can completely skip all leaf nodes from index $\lfloor N/2 \rfloor$ to $N - 1$.

2. **Bottom-Up Induction:**
   - The last non-leaf node is located at:
     $$\text{startIndex} = \left\lfloor \frac{N}{2} \right\rfloor - 1$$
   - We iterate from $i = \text{startIndex}$ down to $0$.
   - When we process node $i$, both its left subtree and its right subtree have **already** been transformed into valid min-heaps.
   - Thus, calling `siftDown(i)` on node $i$ correctly integrates the root with its two valid min-heap subtrees, forming a single larger valid min-heap.

---

## 4. Mathematical Proof of $\mathcal{O}(N)$ Linear Time

Why is bottom-up construction $\mathcal{O}(N)$ while top-down insertion is $\mathcal{O}(N \log N)$?

In a complete binary tree of height $H = \lfloor \log_2 N \rfloor$:
- Level $h$ (measured as height above the leaves, where leaves have $h = 0$):
  - Height of node: $h$
  - Maximum sifting distance down to a leaf: $h$
  - Number of nodes at height $h$: at most $\lceil N / 2^{h+1} \rceil$

Summing the maximum operations across all levels:
$$T(N) = \sum_{h=0}^{\log_2 N} \left\lceil \frac{N}{2^{h+1}} \right\rceil \cdot h \le \frac{N}{2} \sum_{h=0}^\infty \frac{h}{2^h}$$

Recall the well-known mathematical identity for an arithmetic-geometric series:
$$\sum_{h=0}^\infty \frac{h}{2^h} = \frac{1/2}{(1 - 1/2)^2} = \frac{1/2}{1/4} = 2$$

Substituting this into the inequality:
$$T(N) \le \frac{N}{2} \times 2 = N \implies \mathbf{\mathcal{O}(N)}$$

**Key Insight:** In bottom-up construction, the vast majority of nodes (the $N/2$ leaves) do $0$ work. Nodes at height 1 ($N/4$ nodes) do at most 1 swap. Only the single root node does $\log_2 N$ work. The total work converges to a linear constant times $N$.

---

## 5. Detailed Sift-Down Algorithm

For a given node $i$:
1. Compute left child: $\text{left} = 2i + 1$.
2. Compute right child: $\text{right} = 2i + 2$.
3. Find the index of the smallest element among $\{\text{nums}[i], \text{nums}[\text{left}], \text{nums}[\text{right}]\}$.
4. If $\text{smallest} \neq i$:
   - Swap $\text{nums}[i]$ and $\text{nums}[\text{smallest}]$.
   - Update $i = \text{smallest}$ and repeat.
5. If $\text{smallest} == i$, the subtree rooted at $i$ satisfies the min-heap invariant; terminate the loop.

---

## 6. Dry Run Walkthrough

Consider Example 1: `nums = [6, 5, 2, 7, 1, 7]`, $N = 6$.
- Last non-leaf index: $\lfloor 6/2 \rfloor - 1 = 2$.
- Iterate $i = 2, 1, 0$:

1. **$i = 2$ (`nums[2] = 2`):**
   - Left child at index $2(2) + 1 = 5$ (`nums[5] = 7`).
   - Right child out of bounds ($6 \ge 6$).
   - Smallest among $\{2, 7\}$ is $2$ at index $2$.
   - No swap needed $\implies [6, 5, 2, 7, 1, 7]$.

2. **$i = 1$ (`nums[1] = 5`):**
   - Left child at index $3$ (`nums[3] = 7`).
   - Right child at index $4$ (`nums[4] = 1`).
   - Smallest among $\{5, 7, 1\}$ is $1$ at index $4$.
   - Swap $\text{nums}[1]$ and $\text{nums}[4]$:
     $$\text{nums} = [6, \mathbf{1}, 2, 7, \mathbf{5}, 7]$$
   - Index $4$ has no children $\implies$ stop.

3. **$i = 0$ (`nums[0] = 6`):**
   - Left child at index $1$ (`nums[1] = 1`).
   - Right child at index $2$ (`nums[2] = 2`).
   - Smallest among $\{6, 1, 2\}$ is $1$ at index $1$.
   - Swap $\text{nums}[0]$ and $\text{nums}[1]$:
     $$\text{nums} = [\mathbf{1}, \mathbf{6}, 2, 7, 5, 7]$$
   - Now at index $1$ (`nums[1] = 6`):
     - Left child at index $3$ (`nums[3] = 7`).
     - Right child at index $4$ (`nums[4] = 5`).
     - Smallest among $\{6, 7, 5\}$ is $5$ at index $4$.
     - Swap $\text{nums}[1]$ and $\text{nums}[4]$:
       $$\text{nums} = [1, \mathbf{5}, 2, 7, \mathbf{6}, 7]$$
     - Index $4$ has no children $\implies$ stop.

Final array: `[1, 5, 2, 7, 6, 7]`.

---

## 7. Edge Cases & Boundary Conditions

| Scenario | Behavior / Handling |
| :--- | :--- |
| **$N = 1$ (Single Element)** | $\lfloor 1/2 \rfloor - 1 = -1$, loop does not execute; single element is already a valid min-heap. |
| **$N = 2$ (Two Elements)** | Only one non-leaf node at index $0$. Sift-down compares index 0 with left child at index 1 and swaps if necessary. |
| **Already Sorted Array** | For an ascending array, every parent is already $\le$ its children. $0$ swaps occur, verified in $\mathcal{O}(N)$ time. |
| **Reverse Sorted Array** | Descending array requires the maximum number of downward sifts. Still executes in strictly $\mathcal{O}(N)$ time. |
| **All Identical Elements** | Equal elements satisfy $\text{nums}[i] \le \text{nums}[\text{child}]$; $0$ swaps are performed. |
| **Negative Values** | Signed comparisons handle negative values (e.g. $-10^4$) seamlessly. |

---

## 8. Complexity Summary

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathbf{\mathcal{O}(N)}$ | Proven via summation of heights: $\sum h / 2^h \le 2$. Performs at most $2N$ comparisons and swaps. |
| **Space Complexity** | $\mathbf{\mathcal{O}(1)}$ | In-place modification of the array using constant auxiliary variables. |
| **Stack Safety** | $100\%$ Safe | The iterative sift-down loop uses $0$ recursion stack frames, completely safe for $N = 10^5$. |
