# Editorial: Inorder Successor and Predecessor in BST

## Problem Overview
Given the root of a **Binary Search Tree (BST)** and an integer $key$ that is guaranteed to exist in the tree, we need to find its **Inorder Predecessor** and **Inorder Successor**:
- **Inorder Predecessor:** The largest value in the BST that is strictly less than $key$. If no such value exists, return `-1`.
- **Inorder Successor:** The smallest value in the BST that is strictly greater than $key$. If no such value exists, return `-1`.

In an inorder traversal ($\text{Left} \rightarrow \text{Root} \rightarrow \text{Right}$), nodes of a BST are visited in strictly ascending sorted order. Therefore:
- The **predecessor** is the node that immediately precedes $key$ in the sorted sequence.
- The **successor** is the node that immediately follows $key$ in the sorted sequence.

---

## Approach 1: Brute Force (Inorder Traversal Array Scan)
- **Idea:**
  1. Perform an inorder traversal of the BST and collect all node values into an array/list.
  2. Because the tree is a BST, the resulting list is sorted in strictly increasing order.
  3. Find the index $idx$ of $key$ in the sorted list.
  4. The predecessor is `list[idx - 1]` (or `-1` if $idx == 0$).
  5. The successor is `list[idx + 1]` (or `-1` if $idx == \text{list.size()} - 1$).
- **Complexity:**
  - **Time Complexity:** $\mathcal{O}(N)$ to traverse all nodes and scan the list.
  - **Space Complexity:** $\mathcal{O}(N)$ to store the inorder traversal array + $\mathcal{O}(H)$ recursion stack.
- **Limitation:** Incurs unnecessary linear overhead by scanning the entire tree instead of following targeted logarithmic search paths.

---

## Approach 2: Optimal Iterative Binary Search — $\mathcal{O}(H)$ Time, $\mathcal{O}(1)$ Space

Instead of traversing the entire tree, we can leverage the BST ordering property to search for the predecessor and successor in two independent, iterative top-down walks.

### 1. Finding the Inorder Predecessor
We seek the **maximum element** that is strictly smaller than $key$:
- Start at `curr = root` and initialize `predecessor = -1`.
- While `curr != null`:
  - If `curr.val < key`:
    - `curr.val` is smaller than $key$, so `curr` is a viable predecessor candidate!
    - Record `predecessor = curr.val`.
    - To find an even larger value that might still be strictly smaller than $key$, move to the right child: `curr = curr.right`.
  - Else (`curr.val >= key`):
    - `curr.val` is too large or equal to $key$. It cannot be the predecessor, and neither can any node in its right subtree.
    - Move to the left child: `curr = curr.left`.
- Return `predecessor`.

### 2. Finding the Inorder Successor
We seek the **minimum element** that is strictly greater than $key$:
- Start at `curr = root` and initialize `successor = -1`.
- While `curr != null`:
  - If `curr.val > key`:
    - `curr.val` is strictly greater than $key$, so `curr` is a viable successor candidate!
    - Record `successor = curr.val`.
    - To find an even smaller value that might still be strictly greater than $key$, move to the left child: `curr = curr.left`.
  - Else (`curr.val <= key`):
    - `curr.val` is too small or equal to $key$. It cannot be the successor, and neither can any node in its left subtree.
    - Move to the right child: `curr = curr.right`.
- Return `successor`.

---

## Step-by-Step Dry Run

Consider the BST from Example 1:
```text
        5
       / \
      2   10
     / \  / \
    1  4 7  12
```
Target: $key = 10$.

### Trace: Predecessor Search ($key = 10$)
1. `curr = 5`:
   - $5 < 10 \implies \text{predecessor} = 5$.
   - Move to `curr = curr.right` (node $10$).
2. `curr = 10`:
   - $10 \not< 10 \implies$ cannot be predecessor.
   - Move to `curr = curr.left` (node $7$).
3. `curr = 7`:
   - $7 < 10 \implies \text{predecessor} = 7$.
   - Move to `curr = curr.right` (`null`).
4. Loop terminates. Final Predecessor = `7`.

### Trace: Successor Search ($key = 10$)
1. `curr = 5`:
   - $5 \not> 10 \implies$ cannot be successor.
   - Move to `curr = curr.right` (node $10$).
2. `curr = 10`:
   - $10 \not> 10 \implies$ cannot be successor.
   - Move to `curr = curr.right` (node $12$).
3. `curr = 12`:
   - $12 > 10 \implies \text{successor} = 12$.
   - Move to `curr = curr.left` (`null`).
4. Loop terminates. Final Successor = `12`.

Result: `7 12`.

---

## Edge Case Analysis

1. **Target is the Minimum Element:**
   - E.g., $key = 1$ in the tree above.
   - No node in the BST is strictly less than $1 \implies \text{predecessor} = -1$.
   - Successor is correctly identified as $2$.
2. **Target is the Maximum Element:**
   - E.g., $key = 12$ in the tree above.
   - No node in the BST is strictly greater than $12 \implies \text{successor} = -1$.
   - Predecessor is correctly identified as $10$.
3. **Single Node Tree ($N = 1$):**
   - E.g., `root = 1`, `key = 1`.
   - The tree has no other elements. Both loops terminate with `-1 -1`.
4. **Deep Skewed BST ($H = N$):**
   - In a degenerate linked-list BST, the iterative while-loops run in $\mathcal{O}(N)$ time with strictly $\mathcal{O}(1)$ auxiliary space.
   - Zero function call overhead, 100% stack-safe.
5. **Large Values ($10^9$):**
   - Key and node values fit within signed 32-bit integers (`int`).

---

## Complexity Summary

| Metric | Brute Force (Inorder List) | Optimal Iterative BST Search |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N)$ | $\mathcal{O}(H)$ ($\mathcal{O}(\log N)$ balanced, $\mathcal{O}(N)$ skewed) |
| **Auxiliary Space** | $\mathcal{O}(N)$ heap + $\mathcal{O}(H)$ call stack | $\mathcal{O}(1)$ |
| **Stack Safety** | Potential `StackOverflowError` on skewed trees | **100% Stack-Safe** |
