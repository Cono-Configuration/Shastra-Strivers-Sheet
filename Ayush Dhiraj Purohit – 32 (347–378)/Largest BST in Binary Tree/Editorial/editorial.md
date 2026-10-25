# Editorial: Largest BST in Binary Tree

## 1. Problem Overview

Given a binary tree where each node holds an integer value, our task is to find the size (total number of nodes) of the largest subtree that forms a valid **Binary Search Tree (BST)**.

A **subtree** is defined as any node $u$ together with all of its descendants.

A binary tree is a valid BST if and only if:
1. For every node, all values in its left subtree are **strictly smaller** than the node's value.
2. For every node, all values in its right subtree are **strictly greater** than the node's value.
3. Both left and right subtrees are themselves valid BSTs.

---

## 2. Brute-Force Approach

### Concept
A natural top-down approach is to test every node $u$ in the tree to determine whether the subtree rooted at $u$ is a valid BST:
1. Traverse every node $u$ in the tree.
2. For each node $u$, invoke a validation helper `isValidBST(u, minLimit, maxLimit)`.
3. If `isValidBST(u)` is true, count the total number of nodes in $u$'s subtree and update the maximum size found so far.
4. If not true, recursively check $u$'s left and right children.

### Complexity Analysis of Brute Force
- **Time Complexity:** 
  - For a balanced tree: $T(N) = 2T(N/2) + \mathcal{O}(N) \implies \mathcal{O}(N \log N)$.
  - For a skewed tree (e.g. a degenerate chain): validating subtrees of sizes $N, N-1, N-2, \dots, 1$ takes $\sum_{k=1}^N \mathcal{O}(k) = \mathcal{O}(N^2)$ time.
- **Space Complexity:** $\mathcal{O}(H)$ recursion stack frames, which degrades to $\mathcal{O}(N)$ for skewed trees.

With $N = 10^4$, an $\mathcal{O}(N^2)$ approach performs up to $10^8$ operations, risking a Time Limit Exceeded (TLE) verdict. Furthermore, top-down validation redundantly re-checks descendants multiple times.

---

## 3. Optimal Approach: Bottom-Up Post-Order Traversal ($\mathcal{O}(N)$)

### Core Intuition
Instead of validating from top to bottom, we process the tree **bottom-up** using a **Post-Order Traversal** (`Left` $\to$ `Right` $\to$ `Root`).

By processing children before their parent:
- When standing at node $u$, we already know everything about $u$'s left subtree and $u$'s right subtree.
- A node $u$ can form a valid BST if and only if:
  1. $u$'s left subtree is a valid BST.
  2. $u$'s right subtree is a valid BST.
  3. The maximum value in the left subtree is strictly less than $u.\text{val}$ (`left.maxVal < u.val`).
  4. The minimum value in the right subtree is strictly greater than $u.\text{val}$ (`u.val < right.minVal`).

If these conditions are met, the subtree rooted at $u$ is a valid BST! Its size is simply:
$$\text{size}(u) = \text{size}(u.\text{left}) + \text{size}(u.\text{right}) + 1$$

If any condition fails, $u$ cannot form a BST. In this case, the largest BST subtree inside $u$'s subtree is simply:
$$\text{maxSize}(u) = \max(\text{maxSize}(u.\text{left}), \text{maxSize}(u.\text{right}))$$

### Information Tuple
To implement this $\mathcal{O}(1)$ state transition per node, each subtree returns a `NodeInfo` record:
- `isBST` (boolean): whether the subtree rooted at this node is a valid BST.
- `size` (int): the number of nodes in this subtree if `isBST == true`.
- `maxSize` (int): the size of the largest BST subtree located within this subtree.
- `minVal` (long): the minimum value in this subtree.
- `maxVal` (long): the maximum value in this subtree.

### Base Case (Null Subtree)
When encountering a `null` node:
- An empty subtree is vacuously a valid BST.
- `isBST = true`
- `size = 0`
- `maxSize = 0`
- `minVal = +∞` (`Long.MAX_VALUE`)
- `maxVal = -∞` (`Long.MIN_VALUE`)

> **Why set $\text{minVal} = +\infty$ and $\text{maxVal} = -\infty$ for null?**
> - For a leaf node $u$ with `left = null`, we check `left.maxVal < u.val` $\implies -\infty < u.\text{val}$, which always evaluates to `true`.
> - For `right = null`, we check `u.val < right.minVal` $\implies u.\text{val} < +\infty$, which also always evaluates to `true`.

### Induction / State Transitions for Node $u$
Given `left = info(u.left)` and `right = info(u.right)`:

1. **Valid BST Check**:
   $$\text{isValid} = \text{left.isBST} \land \text{right.isBST} \land (\text{left.maxVal} < u.\text{val}) \land (u.\text{val} < \text{right.minVal})$$

2. **If `isValid` is true**:
   - $\text{curSize} = \text{left.size} + \text{right.size} + 1$
   - $\text{curMaxSize} = \text{curSize}$
   - $\text{curMin} = \min(u.\text{val}, \text{left.minVal})$
   - $\text{curMax} = \max(u.\text{val}, \text{right.maxVal})$
   - Return `NodeInfo(true, curSize, curMaxSize, curMin, curMax)`

3. **If `isValid` is false**:
   - $\text{curMaxSize} = \max(\text{left.maxSize}, \text{right.maxSize})$
   - $\text{curMin} = -\infty$ (poisons any ancestor check)
   - $\text{curMax} = +\infty$ (poisons any ancestor check)
   - Return `NodeInfo(false, 0, curMaxSize, -∞, +∞)`

---

## 4. Stack-Safe Iterative Implementation

Recursive implementations risk triggering a `StackOverflowError` in Java when processing degenerate trees (e.g. skewed chains of depth $10^4$).

To ensure **100% stack safety**:
1. Perform an iterative post-order traversal using an explicit heap stack:
   - Push `root` to an explicit stack.
   - Pop `curr`, add it to a `postOrder` list, and push its left and right children.
   - Reversing `postOrder` produces the exact post-order sequence (`Left` $\to$ `Right` $\to$ `Root`).
2. Traverse the list in post-order: every node's children are processed strictly before the node itself.
3. Retrieve child information from an array/map and compute the node's `NodeInfo`.

---

## 5. Dry Run Walkthrough

Consider Example 3:
```text
        3
       / \
      1   4
         /
        2
```

1. **Leaf Node 1**:
   - Left: `(true, 0, 0, +∞, -∞)`
   - Right: `(true, 0, 0, +∞, -∞)`
   - Conditions: $-\infty < 1 < +\infty \implies$ valid.
   - Returns: `(true, size=1, maxSize=1, min=1, max=1)`

2. **Leaf Node 2**:
   - Left: `(true, 0, 0, +∞, -∞)`
   - Right: `(true, 0, 0, +∞, -∞)`
   - Returns: `(true, size=1, maxSize=1, min=2, max=2)`

3. **Node 4**:
   - Left (Node 2): `(true, 1, 1, min=2, max=2)`
   - Right (null): `(true, 0, 0, +∞, -∞)`
   - Condition: $2 < 4 < +\infty \implies$ valid!
   - $\text{size} = 1 + 0 + 1 = 2$, $\text{maxSize} = 2$.
   - $\text{min} = \min(4, 2) = 2$, $\text{max} = \max(4, -\infty) = 4$.
   - Returns: `(true, size=2, maxSize=2, min=2, max=4)`

4. **Root Node 3**:
   - Left (Node 1): `(true, 1, 1, min=1, max=1)`
   - Right (Node 4): `(true, 2, 2, min=2, max=4)`
   - Check left: $\text{left.maxVal} < 3 \implies 1 < 3$ (True).
   - Check right: $3 < \text{right.minVal} \implies 3 < 2$ (**False**!).
   - Result: Not a BST!
   - $\text{maxSize} = \max(\text{left.maxSize}, \text{right.maxSize}) = \max(1, 2) = 2$.
   - Returns: `(false, 0, maxSize=2, -∞, +∞)`

Final Answer at root: $\mathbf{2}$.

---

## 6. Edge Cases & Boundary Conditions

| Edge Case | Description | Handled By |
| :--- | :--- | :--- |
| **Single Node ($N = 1$)** | Any single node is trivially a valid BST of size 1. | Base condition yields size 1. |
| **Completely Valid BST** | All nodes satisfy BST property; answer is $N$. | State transition sums up sizes to $N$. |
| **Completely Inverted Tree** | Skewed tree with strictly descending values. No 2 nodes form a BST. | Any single node forms a BST; returns 1. |
| **Identical Duplicate Values** | Nodes with equal values (e.g. `2 2 2`). | Strict inequalities (`<` and `>`) correctly invalidate BST. |
| **Deep Skewed Chains ($H = 10^4$)** | Trees that would cause recursion stack overflow. | Explicit heap-stack iterative post-order traversal. |
| **Boundary Values ($10^9$)** | Node values near integer boundaries. | `long` arithmetic avoids overflow in boundary checks. |

---

## 7. Complexity Analysis

| Approach | Time Complexity | Auxiliary Space Complexity | Stack Safe? |
| :--- | :--- | :--- | :--- |
| **Brute Force (Top-Down)** | $\mathcal{O}(N^2)$ | $\mathcal{O}(N)$ recursion | No |
| **Recursive Post-Order** | $\mathcal{O}(N)$ | $\mathcal{O}(H)$ call stack | Risk on $H \approx 10^4$ |
| **Iterative Post-Order (Optimal)** | $\mathbf{\mathcal{O}(N)}$ | $\mathbf{\mathcal{O}(N)}$ heap | **Yes (100% Stack Safe)** |
