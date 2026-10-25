# Editorial: Check if an array represents a min heap

## 1. Problem Overview

A **Binary Min-Heap** is a complete binary tree where every node satisfies the **min-heap property**:
$$\text{key}(\text{parent}) \le \text{key}(\text{child})$$

Because a binary heap is always a **complete binary tree** (all levels are completely filled except possibly the deepest, which is filled from left to right), it is compactly represented in memory as a simple 0-indexed contiguous array `nums` of length $N$.

In this problem, we are given an array `nums` of size $N$ and must determine whether it satisfies the min-heap property everywhere.

---

## 2. Array Representation & Indexing Rules

For any 0-indexed array `nums` of size $N$:
- **Root:** Located at index `0`.
- For any node at index $i$:
  - **Left Child:** Located at index $2i + 1$.
  - **Right Child:** Located at index $2i + 2$.
  - **Parent:** Located at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).

A node at index $i$ has:
- A left child if and only if $2i + 1 < N$.
- A right child if and only if $2i + 2 < N$.

---

## 3. Approaches

### 3.1 Brute Force / Full Scan
One approach is to iterate through every index $i$ from `0` to $N - 1$, compute its left child $2i + 1$ and right child $2i + 2$, and check if either child is strictly smaller than `nums[i]`.
- **Time Complexity:** $\mathcal{O}(N)$.
- While correct, more than half of the checks (`left < N` and `right < N`) are performed on leaf nodes that never have children.

### 3.2 Optimal Approach: Checking Only Internal (Non-Leaf) Nodes
In a complete binary tree of size $N$:
- Leaf nodes have no children; therefore, they can never violate the min-heap condition.
- The first leaf node appears at index $\lfloor N / 2 \rfloor$.
- All internal (parent) nodes reside within the range:
  $$0 \le i \le \left\lfloor \frac{N - 2}{2} \right\rfloor \quad \left(\text{or equivalently } i < \left\lfloor \frac{N}{2} \right\rfloor\right)$$

#### Algorithm:
1. If $N \le 1$, return `true` immediately (empty or single-element trees are trivially valid heaps).
2. Iterate $i$ from $0$ up to $\lfloor (N - 2) / 2 \rfloor$:
   - Let `leftChild = 2 * i + 1`.
   - If `leftChild < N` and `nums[i] > nums[leftChild]`, return `false`.
   - Let `rightChild = 2 * i + 2`.
   - If `rightChild < N` and `nums[i] > nums[rightChild]`, return `false`.
3. If the loop completes without detecting any violations, return `true`.

---

## 4. Dry-Run Walkthrough

### Example 1: `nums = [10, 20, 30, 21, 23]`, $N = 5$
- Last parent node index: $\lfloor (5 - 2) / 2 \rfloor = 1$.
- Loop runs for $i = 0, 1$:
  - **$i = 0$ (`nums[0] = 10`):**
    - `leftChild = 1`, `nums[1] = 20`. Is $10 > 20$? No.
    - `rightChild = 2`, `nums[2] = 30`. Is $10 > 30$? No.
  - **$i = 1$ (`nums[1] = 20`):**
    - `leftChild = 3`, `nums[3] = 21`. Is $20 > 21$? No.
    - `rightChild = 4`, `nums[4] = 23`. Is $20 > 23$? No.
- Loop terminates cleanly $\implies$ returns `true`.

### Example 2: `nums = [10, 20, 30, 25, 15]`, $N = 5$
- Last parent node index: $\lfloor (5 - 2) / 2 \rfloor = 1$.
- Loop runs for $i = 0, 1$:
  - **$i = 0$ (`nums[0] = 10`):**
    - `leftChild = 1`, `nums[1] = 20`. Valid ($10 \le 20$).
    - `rightChild = 2`, `nums[2] = 30`. Valid ($10 \le 30$).
  - **$i = 1$ (`nums[1] = 20`):**
    - `leftChild = 3`, `nums[3] = 25`. Valid ($20 \le 25$).
    - `rightChild = 4`, `nums[4] = 15`. Violation: $20 > 15$.
- Early termination $\implies$ returns `false`.

---

## 5. Edge-Case Analysis

1. **Single Element ($N = 1$):**
   - The tree consists solely of the root node. With no children to violate the heap invariant, it returns `true`.
2. **Two Elements ($N = 2$):**
   - Node at index 0 has only a left child at index 1.
   - If `nums[0] <= nums[1]`, valid; otherwise, invalid.
3. **Identical / Duplicate Values:**
   - For example, `[5, 5, 5, 5, 5]`.
   - The min-heap property specifies $\le$ (less than or equal to). Since $5 \le 5$, equal values do not constitute a violation, correctly returning `true`.
4. **Sorted Non-Decreasing Array:**
   - Any sorted array $nums[0] \le nums[1] \le \dots \le nums[N-1]$ is guaranteed to be a valid min-heap because $i < 2i + 1 < 2i + 2$, implying $nums[i] \le nums[2i + 1]$ and $nums[i] \le nums[2i + 2]$.
5. **Reverse-Sorted Array:**
   - An array sorted in descending order (with distinct elements) violates the condition at the root or early internal nodes, returning `false`.
6. **Violation at the Very Last Element:**
   - Array is perfectly ordered until the final leaf node at index $N - 1$. Testing this boundary condition ensures that internal node checks cover odd and even $N$ properly.
7. **Negative Numbers & Extreme 32-bit Values:**
   - Numbers range from $-10^9$ to $10^9$. Standard signed integer comparisons handle negatives correctly (e.g., $-10 \le -5$ is true).

---

## 6. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N)$.
  - We only examine the internal nodes, which account for at most $\lfloor N / 2 \rfloor$ nodes.
  - At each node, we perform at most two constant-time comparisons.
  - In worst-case scenarios (where the array is a valid min-heap or the violation occurs at the last child), the loop performs exactly $\approx N / 2$ iterations.
  - In cases with early violations, the function terminates in $\mathcal{O}(1)$ time.
  - For $N = 10^5$, this executes in $\approx 1$ ms in Java.

- **Space Complexity:** $\mathcal{O}(1)$ auxiliary space.
  - The verification requires only primitive integer variables for loop indices. No recursion, auxiliary arrays, or call stack overhead are utilized.
