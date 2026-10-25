# Editorial: Two Sum in BST

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** and an integer target $k$. Our goal is to determine whether there exist two **distinct** nodes in the BST whose values sum to $k$. We must return `true` if such a pair exists, and `false` otherwise.

---

## Evolution of Approaches

### Approach 1: Hash Set Traversal (Generic Binary Tree Approach)
- **Idea:** Traverse the tree using DFS or BFS. For each node with value $val$, check if $k - val$ is already present in a hash set. If so, return `true`. Otherwise, insert $val$ into the set and continue.
- **Complexity:**
  - **Time Complexity:** $\mathcal{O}(N)$ on average.
  - **Space Complexity:** $\mathcal{O}(N)$ to store up to $N$ elements in the hash set + $\mathcal{O}(H)$ recursion stack.
- **Drawback:** It does not exploit the sorted structure of a Binary Search Tree and requires linear auxiliary memory $\mathcal{O}(N)$.

---

### Approach 2: In-Order Traversal Flattening + Two Pointers
- **Idea:**
  1. An in-order traversal of a BST visits nodes in strictly ascending sorted order.
  2. Perform an in-order traversal to extract all elements into an array `arr`.
  3. Use the classic two-pointer technique with `left = 0` and `right = N - 1`:
     - If `arr[left] + arr[right] == k`, return `true`.
     - If `arr[left] + arr[right] < k`, advance `left++`.
     - If `arr[left] + arr[right] > k`, advance `right--`.
- **Complexity:**
  - **Time Complexity:** $\mathcal{O}(N)$ to traverse + $\mathcal{O}(N)$ for two-pointer search = $\mathcal{O}(N)$.
  - **Space Complexity:** $\mathcal{O}(N)$ to store all nodes in the array.
- **Limitation:** Upfront storage of all $N$ elements consumes $\mathcal{O}(N)$ space even if a matching pair is found in the very first step.

---

### Approach 3: Optimal Dual BST Iterator (Two Pointers directly on the BST) — $\mathcal{O}(H)$ Space

Instead of materializing the entire sorted array in memory, we can simulate the two pointers lazily on the BST using two independent **BST Iterators**:
1. **Forward Iterator (`leftIter`):** Traverses the tree in ascending in-order ($\text{Left} \rightarrow \text{Root} \rightarrow \text{Right}$), simulating the `left` pointer.
2. **Reverse Iterator (`rightIter`):** Traverses the tree in descending reverse in-order ($\text{Right} \rightarrow \text{Root} \rightarrow \text{Left}$), simulating the `right` pointer.

#### Iterator Invariant:
- In `leftIter`: `pushAll(node)` pushes `node` and moves to `node.left`. When popped, we explore `node.right`.
- In `rightIter`: `pushAll(node)` pushes `node` and moves to `node.right`. When popped, we explore `node.left`.

#### Two-Pointer Search:
1. Initialize `leftVal = leftIter.next()` and `rightVal = rightIter.next()`.
2. While `leftVal < rightVal`:
   - Compute `sum = (long) leftVal + rightVal` (using 64-bit integer to prevent overflow).
   - If `sum == k`: A valid pair is found! Return `true`.
   - If `sum < k`: The sum is too small; advance `leftVal = leftIter.next()`.
   - If `sum > k`: The sum is too large; advance `rightVal = rightIter.next()`.
3. If `leftVal >= rightVal` without finding a match, no valid pair exists; return `false`.

---

## Step-by-Step Dry Run

Consider the BST from Example 1:
```text
        5
       / \
      3   6
     / \   \
    2   4   7
```
Target: $k = 9$.

1. **Initialization:**
   - `leftIter` initialized: leftmost path from `5` $\implies$ pushes `5, 3, 2`. Top is `2`.
   - `rightIter` initialized: rightmost path from `5` $\implies$ pushes `5, 6, 7`. Top is `7`.
   - `leftVal = leftIter.next() = 2`.
   - `rightVal = rightIter.next() = 7`.

2. **Iteration 1:**
   - `leftVal = 2`, `rightVal = 7`.
   - `sum = 2 + 7 = 9 == k`.
   - Match found on the first check! Return `true`.

---

## Edge Case Analysis

1. **Single Node Tree ($N = 1$):**
   - Both iterators pop the single node. `leftVal == rightVal`.
   - The loop condition `leftVal < rightVal` is false.
   - Correctly returns `false` (cannot pick the same node twice).
2. **No Valid Pair ($k = 14$ on same tree):**
   - The two pointers traverse toward each other and cross without finding any sum equal to $14$.
   - Returns `false`.
3. **Values Near Boundaries ($10^9$):**
   - Using `(long) leftVal + rightVal` prevents standard 32-bit integer overflow when node values reach $10^9$.
4. **Deep Skewed BST ($H = N$):**
   - The iterative `pushAll` implementation runs with zero function recursion frames, eliminating any risk of `StackOverflowError`.

---

## Complexity Summary

| Metric | Hash Set Traversal | In-Order Array Flattening | Dual BST Iterator (Optimal) |
| :--- | :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ | **$\mathcal{O}(N)$** |
| **Auxiliary Space** | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ | **$\mathcal{O}(H)$** ($\mathcal{O}(\log N)$ on balanced trees) |
| **Call Stack Risk** | Recursion risk | Recursion risk | **100% Stack-Safe (Iterative)** |
| **Early Exit** | Yes | No (requires full array) | **Yes (stops as soon as pair is found)** |
