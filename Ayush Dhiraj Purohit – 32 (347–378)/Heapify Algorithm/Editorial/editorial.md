# Editorial: Heapify Algorithm

## 1. Problem Overview

We are given a 0-indexed array `nums` of length $N$ representing a valid **min-heap**, an index `ind` ($0 \le ind < N$), and a new integer value `val`. Our goal is to update the value at index `ind` to `val` (`nums[ind] = val`) and restore the min-heap invariant in-place.

---

## 2. Min-Heap Properties & Array Representation

A binary heap is a **complete binary tree** compactly stored in a contiguous array without pointers:
- The root of the tree is stored at index `0`.
- For any node at index $i$:
  - Its **parent** is at index $\lfloor (i - 1) / 2 \rfloor$ (for $i > 0$).
  - Its **left child** is at index $2i + 1$ (valid if $2i + 1 < N$).
  - Its **right child** is at index $2i + 2$ (valid if $2i + 2 < N$).

### Min-Heap Invariant
In a min-heap, every node must be smaller than or equal to both of its children:
$$\text{nums}[i] \le \text{nums}[2i + 1] \quad \text{and} \quad \text{nums}[i] \le \text{nums}[2i + 2]$$
Equivalently, every node (except the root) must be greater than or equal to its parent:
$$\text{nums}[i] \ge \text{nums}[\lfloor (i - 1) / 2 \rfloor]$$

---

## 3. Analysis of the Update Operation

When `nums[ind]` is updated to `val`, the relationship between the modified node, its parent, and its children may be disturbed.

Let $\text{oldVal}$ be the previous value at index `ind`:

### Case 1: Value Decreased ($\text{val} < \text{oldVal}$)
- The new value is smaller than before.
- Since $\text{val} < \text{oldVal} \le \text{children}$, the node is **guaranteed** to still be strictly smaller than its children. Thus, the heap property cannot be violated downwards.
- However, the new value may now be strictly smaller than its parent ($\text{val} < \text{nums}[\text{parent}]$).
- **Remedy:** Perform **Heapify-Up (Sift-Up)** towards the root.

### Case 2: Value Increased ($\text{val} > \text{oldVal}$)
- The new value is larger than before.
- Since $\text{val} > \text{oldVal} \ge \text{parent}$, the node is **guaranteed** to still be strictly greater than or equal to its parent. Thus, the heap property cannot be violated upwards.
- However, the new value may now be strictly larger than one or both of its children.
- **Remedy:** Perform **Heapify-Down (Sift-Down)** towards the leaves.

### Case 3: Value Unchanged ($\text{val} == \text{oldVal}$)
- The min-heap property remains intact; no operations or swaps are required.

---

## 4. Algorithmic Steps

### Step 1: Update
Set `nums[ind] = val`.

### Step 2: Directional Sifting

```text
if ind > 0 and nums[ind] < nums[(ind - 1) / 2]:
    siftUp(ind)
else:
    siftDown(ind)
```

#### Sift-Up (Heapify-Up) Procedure
1. While $i > 0$ and $\text{nums}[i] < \text{nums}[\lfloor (i - 1) / 2 \rfloor]$:
   - Swap $\text{nums}[i]$ with $\text{nums}[\lfloor (i - 1) / 2 \rfloor]$.
   - Move $i$ to $\lfloor (i - 1) / 2 \rfloor$.
2. Stop when $i = 0$ (reached root) or the parent is $\le \text{nums}[i]$.

#### Sift-Down (Heapify-Down) Procedure
1. While $i$ has at least one child ($2i + 1 < N$):
   - Identify the smallest of the three candidates: $\{\text{nums}[i], \text{nums}[2i + 1], \text{nums}[2i + 2]\}$.
   - If $\text{nums}[i]$ is already the smallest, the heap property is satisfied $\implies$ terminate.
   - Otherwise, swap $\text{nums}[i]$ with the smaller child, and update $i$ to the child's index.
2. Stop when $i$ has no children or no swap is needed.

---

## 5. Dry Run Walkthroughs

### Walkthrough 1: Sift-Up (Example 1)
- `nums = [1, 4, 5, 5, 7, 6]`, `ind = 5`, `val = 2`.
1. Update `nums[5] = 2` $\implies [1, 4, 5, 5, 7, \mathbf{2}]$.
2. Parent of index $5$ is index $\lfloor (5 - 1) / 2 \rfloor = 2$ (`nums[2] = 5`).
3. Since $2 < 5$, swap index $5$ and index $2$:
   $$\text{nums} = [1, 4, \mathbf{2}, 5, 7, \mathbf{5}]$$
4. Now at index $2$, parent is index $\lfloor (2 - 1) / 2 \rfloor = 0$ (`nums[0] = 1`).
5. Since $2 \ge 1$, terminate.
- Final output: `[1, 4, 2, 5, 7, 5]`.

---

### Walkthrough 2: Sift-Down (Example 2)
- `nums = [2, 4, 3, 6, 5, 7, 8, 7]`, `ind = 0`, `val = 7`.
1. Update `nums[0] = 7` $\implies [\mathbf{7}, 4, 3, 6, 5, 7, 8, 7]$.
2. Root $i = 0$ has:
   - Left child at index $1$: $\text{nums}[1] = 4$.
   - Right child at index $2$: $\text{nums}[2] = 3$.
3. The smallest element among $\{7, 4, 3\}$ is $3$ at index $2$.
4. Swap `nums[0]` with `nums[2]`:
   $$\text{nums} = [\mathbf{3}, 4, \mathbf{7}, 6, 5, 7, 8, 7]$$
5. Now at index $2$, its children are:
   - Left child at index $2(2) + 1 = 5$: $\text{nums}[5] = 7$.
   - Right child at index $2(2) + 2 = 6$: $\text{nums}[6] = 8$.
6. Smallest among $\{7, 7, 8\}$ is $7$ at index $2$. No swap needed $\implies$ terminate.
- Final output: `[3, 4, 7, 6, 5, 7, 8, 7]`.

---

## 6. Edge Cases & Boundary Conditions

| Scenario | Behavior / Handling |
| :--- | :--- |
| **$N = 1$ (Single Element)** | Updating the single element never triggers sift-up ($ind = 0$) or sift-down (no children). Correctly leaves array with 1 updated element. |
| **Update at Root ($ind = 0$)** | Root has no parent, so it can never sift up; it only sifts down if its new value exceeds either child. |
| **Update at Leaf** | Leaves have no children, so they can never sift down; they only sift up if their new value is smaller than their parent. |
| **Duplicate Values** | Strict and non-strict comparisons ($\le$) ensure stability and prevent unnecessary swaps when values match. |
| **Negative Values** | The algorithm functions identically with negative integers (e.g. $-10^4$). |
| **No Invariant Violation** | If the new value satisfies parent $\le val \le$ children, 0 swaps occur. |

---

## 7. Complexity Analysis

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(\log N)$ | In the worst case, the element travels from root to leaf or leaf to root along a single branch of the binary heap tree of height $H = \lfloor \log_2 N \rfloor$. Each level requires $\mathcal{O}(1)$ comparisons and swaps. |
| **Space Complexity** | $\mathcal{O}(1)$ | The algorithm modifies the input array in-place using simple iterative pointer manipulations with zero auxiliary heap allocations. |
| **Stack Safety** | $100\%$ Safe | The iterative implementation uses simple `while` loops with $0$ recursive call stack frames, entirely safe for $N = 10^5$. |
