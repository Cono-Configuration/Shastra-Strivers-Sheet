# Editorial: LCA in BST

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** and two distinct node values, $p$ and $q$, both of which are guaranteed to exist within the tree. Our objective is to determine the **Lowest Common Ancestor (LCA)** of nodes $p$ and $q$.

According to the formal definition:
> The **Lowest Common Ancestor** of two nodes $p$ and $q$ in a tree $T$ is the deepest (lowest) node that has both $p$ and $q$ as descendants, where a node is allowed to be a descendant of itself.

---

## Key Observation: BST Ordering Invariant

In an arbitrary binary tree, finding the LCA typically requires checking both subtrees of every node or storing path traces from the root to each node ($\mathcal{O}(N)$ time and space).

However, in a **Binary Search Tree**, keys obey a strict ordering invariant:
1. For any node $u$, all nodes in its left subtree have keys strictly less than $\text{val}(u)$:
   $$\forall v \in \text{LeftSubtree}(u), \quad \text{val}(v) < \text{val}(u)$$
2. All nodes in its right subtree have keys strictly greater than $\text{val}(u)$:
   $$\forall w \in \text{RightSubtree}(u), \quad \text{val}(w) > \text{val}(u)$$

This ordering property enables us to make a **deterministic directional choice** at each node:
- If both target values $p$ and $q$ are strictly smaller than the current node's value ($\min(p, q) < \text{curr.val}$ and $\max(p, q) < \text{curr.val}$), then by definition both $p$ and $q$ reside entirely inside the **left subtree**. Therefore, their lowest common ancestor must also be in the left subtree.
- If both target values $p$ and $q$ are strictly greater than the current node's value ($\min(p, q) > \text{curr.val}$ and $\max(p, q) > \text{curr.val}$), then both $p$ and $q$ reside entirely inside the **right subtree**. Thus, their lowest common ancestor must be in the right subtree.
- If neither of the above holds, the search paths to $p$ and $q$ **diverge** at the current node:
  - Either $p < \text{curr.val} < q$ (or $q < \text{curr.val} < p$), meaning one node is in the left subtree and the other is in the right subtree.
  - Or $\text{curr.val} = p$ (meaning $p$ is an ancestor of $q$), or $\text{curr.val} = q$ (meaning $q$ is an ancestor of $p$).

In all split or match scenarios, the current node is the **deepest node common to both paths**. Hence, **$\text{curr}$ is the Lowest Common Ancestor**.

---

## Comparison: Recursive vs. Iterative Approach

| Metric | Recursive Approach | Iterative Approach (Optimal) |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(H)$ | $\mathcal{O}(H)$ |
| **Auxiliary Space** | $\mathcal{O}(H)$ call stack | $\mathcal{O}(1)$ |
| **Call Stack Risk** | Risk of `StackOverflowError` on skewed trees ($H = 10^4$) | 100% Stack-Safe |
| **Overhead** | Function frame allocation per level | No function call overhead |

Because the search never needs to backtrack or visit both branches simultaneously, recursion is completely unnecessary. An **iterative traversal** traverses downward like a simple while-loop, using strictly $\mathcal{O}(1)$ memory.

---

## Detailed Algorithm (Iterative)

```text
Algorithm LowestCommonAncestor(root, p, q):
1. curr <- root
2. While curr is not NULL:
     a. If p < curr.val AND q < curr.val:
          curr <- curr.left
     b. Else If p > curr.val AND q > curr.val:
          curr <- curr.right
     c. Else:
          Return curr   // Found the split point or exact match
3. Return NULL
```

---

## Step-by-Step Dry Run

### Case 1: Split Point Between Two Subtrees
Consider the BST:
```text
        5
       / \
      3   6
     / \   \
    2   4   7
```
Target nodes: $p = 2, q = 4$.

1. Start at `curr = 5`:
   - $p = 2 < 5$ and $q = 4 < 5$.
   - Both are strictly less than $5$, so we advance to `curr = curr.left` (node $3$).
2. At `curr = 3`:
   - $p = 2 < 3$, but $q = 4 > 3$.
   - Condition ($p < 3 \land q < 3$) is false.
   - Condition ($p > 3 \land q > 3$) is false.
   - We hit the `else` branch: node $3$ is the split point!
3. Output: `3`.

---

### Case 2: One Node is Ancestor of the Other
Target nodes: $p = 3, q = 2$ on the same tree.

1. Start at `curr = 5`:
   - Both $3 < 5$ and $2 < 5 \implies$ advance to `curr = curr.left` (node $3$).
2. At `curr = 3`:
   - $p = 3 = \text{curr.val}$.
   - Neither both-less nor both-greater holds.
   - We hit the `else` branch: node $3$ is the split point (and descendant of itself).
3. Output: `3`.

---

## Edge Case Analysis

1. **Root is the LCA:**
   - When $p$ lies in the left subtree of the root and $q$ lies in the right subtree (or one of them is the root itself), the loop terminates on the very first iteration at `root`.
2. **Direct Ancestor / Descendant Relationship:**
   - If $p$ is an ancestor of $q$, the traversal stops at $p$. The definition allows a node to be a descendant of itself, so $p$ is correctly returned.
3. **Deep Skewed BST (Left-skewed or Right-skewed):**
   - In a degenerate BST shaped like a linked list ($H = N$), our iterative loop traverses at most $N$ steps using $\mathcal{O}(1)$ space without overflowing the call stack.
4. **Minimal BST ($N = 2$):**
   - With only a root and one child, the root is always the LCA. The algorithm handles this in $\mathcal{O}(1)$ time.
5. **Very Large Key Values:**
   - Keys up to $10^9$ fit comfortably within standard signed 32-bit integers (`int` in Java/C++).

---

## Complexity Analysis

- **Time Complexity:** $\mathcal{O}(H)$
  - In a balanced BST, the height $H = \mathcal{O}(\log N)$, leading to logarithmic time.
  - In a skewed BST, $H = \mathcal{O}(N)$ in the worst case.
  - At each step, we perform constant-time comparisons and move down exactly one level.
- **Space Complexity:** $\mathcal{O}(1)$ auxiliary space
  - The iterative solution uses only a single pointer variable `curr` and no recursion stack.
