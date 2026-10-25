# Editorial: Delete a node in BST

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** and an integer $key$. Our goal is to delete the node with the value $key$ from the tree while ensuring that the modified tree strictly preserves all binary search tree invariants:
1. Every node in the left subtree has a value strictly less than the node's value.
2. Every node in the right subtree has a value strictly greater than the node's value.
3. Both the left and right subtrees are valid binary search trees.

If the given $key$ does not exist in the BST, the tree remains unchanged.

---

## The Three Deletion Scenarios
Once the target node $curr$ is located, its removal falls into one of three structural cases based on its number of children:

```text
               Case 1 (0 Children)         Case 2 (1 Child)             Case 3 (2 Children)
                   [Parent]                    [Parent]                      [Parent]
                      |                           |                             |
                    [curr]                      [curr]                        [curr]
                   /      \                    /      \                      /      \
                null      null              [Child]   null               [Left]    [Right]
             (Simply delete)             (Bypass curr)              (Replace with in-order
                                                                     successor & delete it)
```

### Case 1: Node has 0 Children (Leaf Node)
- The node has no subtrees attached to it.
- **Action:** Simply remove the node by setting the corresponding pointer of its parent (`parent.left` or `parent.right`) to `null`. If the node is the root itself, the tree becomes empty (`null`).

### Case 2: Node has 1 Child
- The node has either only a left child or only a right child.
- **Action:** Splice the node out by linking its parent directly to its existing child. If the node is the root, its single child becomes the new root.

### Case 3: Node has 2 Children
- The node has both non-empty left and right subtrees. Directly splicing it out would orphan one of the subtrees.
- **Action:** Replace the node's value with its **in-order successor** (the smallest value in its right subtree).
  - The in-order successor is guaranteed to be strictly greater than all elements in the left subtree, and strictly less than all other elements in the right subtree.
  - The successor is the leftmost node of the right subtree, which means it has **at most one child** (a right child; its left child is guaranteed to be `null`).
  - Thus, removing the successor reduces to Case 1 or Case 2!

---

## Comparison: Recursive vs. Iterative Implementation

### 1. Recursive Approach
- **Mechanism:** Recursively search down the tree until `root.val == key`, apply the deletion logic, and reconnect subtrees on the return path:
  ```java
  if (key < root.val) root.left = deleteNode(root.left, key);
  else if (key > root.val) root.right = deleteNode(root.right, key);
  ```
- **Limitation:** In the worst-case scenario (a degenerate skewed tree where height $H = N$), recursion requires $\mathcal{O}(N)$ stack frames. For $N = 10^4$, this can lead to a `StackOverflowError` in default thread environments.

### 2. Iterative Approach (Optimal & Stack-Safe)
- **Mechanism:**
  1. Use two pointers (`curr` and `parent`) in a `while` loop to locate the target node.
  2. Handle 0 or 1 child by updating `parent.left` or `parent.right` directly.
  3. For 2 children, find the in-order successor (`succ`) and its parent (`succParent`) iteratively by walking left from `curr.right`.
  4. Copy `succ.val` into `curr.val`, then splice out `succ`.
- **Advantage:** $\mathcal{O}(1)$ auxiliary space, zero function call overhead, completely immune to stack overflow even on $N = 10^4$ degenerate trees.

---

## Detailed Iterative Algorithm

```text
Algorithm DeleteNode(root, key):
1. curr <- root, parent <- NULL
2. While curr != NULL and curr.val != key:
     parent <- curr
     If key < curr.val:
         curr <- curr.left
     Else:
         curr <- curr.right

3. If curr == NULL:
     Return root    // key not found

4. If curr.left == NULL or curr.right == NULL:
     child <- (curr.left != NULL) ? curr.left : curr.right
     If parent == NULL:
         Return child   // Root is being deleted
     If parent.left == curr:
         parent.left <- child
     Else:
         parent.right <- child
     Return root

5. Else (curr has 2 children):
     succParent <- curr
     succ <- curr.right
     While succ.left != NULL:
         succParent <- succ
         succ <- succ.left
     
     curr.val <- succ.val
     
     If succParent != curr:
         succParent.left <- succ.right
     Else:
         succParent.right <- succ.right
     
     Return root
```

---

## Step-by-Step Dry Runs

### Walkthrough 1: Example 1 (Deleting Node with 2 Children)
- **Initial Tree:** `[5, 3, 6, 2, 4, -1, 7]`
- **Target Key:** $key = 3$

```text
Initial BST:
        5
       / \
      3   6
     / \   \
    2   4   7
```

1. **Search Phase:**
   - At root `5`: $3 < 5 \implies$ `parent = 5`, `curr = 3`.
   - Node `3` found (`curr.val == 3`).

2. **Deletion Phase:**
   - Node `3` has two children (`curr.left = 2`, `curr.right = 4`).
   - Find successor: start at `curr.right` (node `4`).
   - `4.left` is `null`, so successor is `succ = 4`, `succParent = 3`.
   - Copy value: `curr.val = 4`.
   - Since `succParent == curr`, update `succParent.right = succ.right` (`3.right = null`).

3. **Final Tree:**
```text
        5
       / \
      4   6
     /     \
    2       7
```
- **Level-Order Traversal:** `5 4 6 2 -1 -1 7`.

---

### Walkthrough 2: Example 5 (Deleting Root Node with 2 Children)
- **Initial Tree:** `[2, 1, 3]`
- **Target Key:** $key = 2$

```text
Initial BST:
      2
     / \
    1   3
```

1. **Search Phase:**
   - At root: `curr = 2`, `parent = null`. Target matches immediately.

2. **Deletion Phase:**
   - Node `2` has two children (`1` and `3`).
   - Successor in right subtree is node `3` (`succParent = 2`, `succ = 3`).
   - Copy value: `curr.val = 3`.
   - Splice out `succ`: `succParent.right = succ.right` (`2.right = null`).

3. **Final Tree:**
```text
      3
     /
    1
```
- **Level-Order Traversal:** `3 1`.

---

## Complexity Analysis

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity (Balanced BST)** | $\mathcal{O}(\log N)$ | Searching for the node takes $\mathcal{O}(\log N)$ comparisons. Finding the in-order successor takes $\mathcal{O}(\log N)$ steps. |
| **Time Complexity (Worst-Case / Skewed BST)** | $\mathcal{O}(H) = \mathcal{O}(N)$ | If the tree is degenerate (height $H = N$), traversing to the target or successor visits at most $N$ nodes. |
| **Auxiliary Space Complexity** | $\mathcal{O}(1)$ | The iterative algorithm maintains only a few pointer variables (`curr`, `parent`, `succ`, `succParent`), using zero extra stack or heap memory. |
| **Total Space Complexity (I/O & Tree)** | $\mathcal{O}(N)$ | Storing the tree representation in memory and serializing the output requires $\mathcal{O}(N)$ space. |

---

## Edge Cases & Defensive Design

1. **Empty Tree ($N = 0$):**
   - When `root == null`, no search is performed, and an empty tree is returned immediately.

2. **Single-Node Tree ($N = 1$):**
   - If the single node matches $key$, `parent == null` and `child == null`, so `null` is returned (tree becomes empty).
   - If the single node does not match $key$, the original tree is returned unchanged.

3. **Key Not Present in BST:**
   - The traversal falls off the tree (`curr == null`). The original BST is returned unmodified.

4. **Deleting the Root Node:**
   - Handled cleanly in all cases:
     - 0 children: returns `null`.
     - 1 child: returns the non-null child as the new root.
     - 2 children: root keeps its reference, adopts the successor's value, and successor is removed from the right subtree.

5. **Successor Has a Right Child:**
   - If `succ.right != null`, it is cleanly attached to `succParent` (`succParent.left = succ.right`), preserving all sub-elements.

6. **Degenerate Line Trees ($N = 10^4$):**
   - The iterative approach ensures absolute stack safety, preventing crashes on deeply nested chains.
