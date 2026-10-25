# Editorial: Check if a tree is a BST or not

## Problem Overview
We are given the root of a binary tree. Our task is to determine whether the tree satisfies all the properties of a **Binary Search Tree (BST)**.

The standard definition of a BST states that for every node $u$:
1. Every node in the **left subtree** of $u$ has a value strictly less than $\text{val}(u)$.
2. Every node in the **right subtree** of $u$ has a value strictly greater than $\text{val}(u)$.
3. Both the left and right subtrees must themselves be valid binary search trees.

An empty tree ($N = 0$) and a single-node tree ($N = 1$) are valid BSTs by definition. Duplicate values are strictly disallowed.

---

## The Common Local-Check Pitfall
A common mistake is verifying only the **immediate children** of each node:
$$\text{node.left.val} < \text{node.val} \quad \text{and} \quad \text{node.right.val} > \text{node.val}$$

### Counterexample:
```text
        5
       / \
      1   4
         / \
        3   6
```
- For node `4`: its left child is `3` ($3 < 4$) and its right child is `6` ($6 > 4$). Locally, node `4` appears valid.
- For root `5`: its left child is `1` ($1 < 5$) and right child is `4` ($4 < 5$, wait, right must be $> 5$!).
- Even if the right child of `5` were `6` with a left child `4` and right child `7`:
```text
        5
       / \
      1   6
         / \
        4   7
```
If node `4` had a right child `3`:
Node `4`'s right child `3` is less than `4`, violating the local check. But what if `6` had a left child `2`? Node `2 < 5` is in the right subtree of `5`!
Thus, **every node must satisfy the global constraints imposed by all its ancestors**, not just its direct parent.

---

## Method 1: Range-Based Validation ($minVal, maxVal$)
Every node in a BST is bounded by an open interval $(minVal, maxVal)$:
- The root begins with $(-\infty, +\infty)$.
- When moving to the **left child**, all values in the left subtree must be smaller than the current node:
  $$\text{new range} = (minVal, \text{curr.val})$$
- When moving to the **right child**, all values in the right subtree must be greater than the current node:
  $$\text{new range} = (\text{curr.val}, maxVal)$$

If any node fails $minVal < \text{node.val} < maxVal$, the tree is immediately invalid.

---

## Method 2: In-Order Traversal (Monotonic Sequence)

### The Mathematical Theorem:
An in-order traversal ($\text{Left} \to \text{Root} \to \text{Right}$) visits all nodes in the left subtree before the root, and the root before any node in the right subtree.

By induction:
> **A binary tree is a valid Binary Search Tree if and only if its in-order traversal visits all nodes in strictly monotonically increasing order.**

### Iterative Algorithm with Early Stopping:
We perform an iterative in-order traversal using an explicit stack (`Deque<TreeNode>`):
1. Keep track of the previously visited node `prev`.
2. As each node `curr` is popped from the stack:
   - If `prev != null` and $\text{curr.val} \le \text{prev.val}$, return `false` immediately!
   - Update `prev = curr`.
3. Advance to `curr.right`.
4. If the traversal completes without any violation, return `true`.

---

## Detailed Algorithm (Iterative In-Order)

```text
Algorithm IsValidBST(root):
1. If root is NULL:
     Return true
2. stack <- empty Deque
3. curr <- root, prev <- NULL
4. While curr is not NULL or stack is not empty:
     While curr is not NULL:
         stack.push(curr)
         curr <- curr.left
     curr <- stack.pop()
     
     If prev is not NULL and curr.val <= prev.val:
         Return false   // Violation detected!
     
     prev <- curr
     curr <- curr.right
5. Return true
```

---

## Step-by-Step Dry Runs

### Walkthrough 1: Example 1 (Valid BST)
- **Input:** `[5, 3, 6, 2, 4, -1, 7]`

```text
Tree:
        5
       / \
      3   6
     / \   \
    2   4   7
```

1. Push `5`, `3`, `2`.
2. Pop `2` $\implies prev = 2$.
3. Pop `3` $\implies 3 > 2$ (valid). $prev = 3$. Move to `3.right = 4`.
4. Pop `4` $\implies 4 > 3$ (valid). $prev = 4$.
5. Pop `5` $\implies 5 > 4$ (valid). $prev = 5$. Move to `5.right = 6`.
6. Push `6`. Move to `6.left` (null).
7. Pop `6` $\implies 6 > 5$ (valid). $prev = 6$. Move to `6.right = 7`.
8. Pop `7` $\implies 7 > 6$ (valid). $prev = 7$.
9. Traversal completes $\implies$ Output: `true`.

---

### Walkthrough 2: Example 3 (Ancestor Violation)
- **Input:** `[5, 1, 4, -1, -1, 3, 6]`

```text
Tree:
        5
       / \
      1   4
         / \
        3   6
```

1. Push `5`, `1`.
2. Pop `1` $\implies prev = 1$.
3. Pop `5` $\implies 5 > 1$ (valid). $prev = 5$. Move to `5.right = 4`.
4. Push `4`, `3`.
5. Pop `3` $\implies \text{curr.val} = 3, \text{prev.val} = 5$.
   - Since $3 \le 5$, the strictly increasing property is violated!
   - Immediately return `false`!

---

## Complexity Analysis

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N)$ | Each node is pushed onto the stack once and popped once. The traversal terminates early upon encountering the first violation. |
| **Auxiliary Space Complexity** | $\mathcal{O}(H)$ | The explicit stack stores at most $H$ nodes, where $H$ is the height of the tree ($\mathcal{O}(\log N)$ for balanced trees, $\mathcal{O}(N)$ for skewed trees). |
| **Total Space Complexity** | $\mathcal{O}(N)$ | Storing the input tree representation in memory requires $\mathcal{O}(N)$ space. |

---

## Edge Cases & Defensive Design

1. **Empty Tree ($N = 0$):**
   - Trivially satisfies the BST property $\implies$ returns `true`.

2. **Single-Node Tree ($N = 1$):**
   - Trivially valid $\implies$ returns `true`.

3. **Duplicate Values:**
   - BSTs require strict inequalities ($<$, $>$). If any node has value equal to an ancestor or sibling (e.g. `[2, 2, 2]`), `curr.val <= prev.val` evaluates to true and correctly returns `false`.

4. **Extreme Integer Values:**
   - Node values can be $0$ or up to $10^9$. Comparing node references or `prev.val` directly handles all boundary values without integer overflow bugs.

5. **Deeply Skewed Trees ($H = 10^4$):**
   - The iterative approach stores stack frames on the JVM heap, ensuring complete stack safety and preventing `StackOverflowError`.
