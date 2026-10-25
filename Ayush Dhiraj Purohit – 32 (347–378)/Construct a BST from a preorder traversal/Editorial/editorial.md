# Editorial: Construct a BST from a preorder traversal

## Problem Overview
We are given an array `preorder` representing the preorder traversal of a **Binary Search Tree (BST)**. Our objective is to reconstruct the original, unique BST and return its root.

A **preorder traversal** visits nodes in the order:
$$\text{Root} \longrightarrow \text{Left Subtree} \longrightarrow \text{Right Subtree}$$

A **Binary Search Tree** satisfies the invariant:
$$\forall u \in \text{LeftSubtree}(\text{root}), \quad \text{val}(u) < \text{val}(\text{root})$$
$$\forall v \in \text{RightSubtree}(\text{root}), \quad \text{val}(v) > \text{val}(\text{root})$$

---

## Evolution of Approaches

### Method 1: Brute Force (Repeated BST Insertion)
- **Idea:** Start with an empty tree `root = null`. Iterate through each element in `preorder` and perform standard BST insertion:
  - Compare the element with the current node; go left if smaller, right if larger, until an empty spot is found.
- **Complexity:**
  - In a balanced tree, each insertion takes $\mathcal{O}(\log N)$, yielding an average time of $\mathcal{O}(N \log N)$.
  - In a skewed tree (e.g., strictly ascending or descending array), each insertion takes $\mathcal{O}(i)$ time, yielding a worst-case time complexity of $\mathcal{O}(N^2)$.
- **Verdict:** Suboptimal for $N = 10^4$.

---

### Method 2: Sorting to Obtain Inorder + Preorder Construction
- **Idea:** 
  1. The **inorder traversal** of any valid BST is strictly sorted in ascending order.
  2. By sorting `preorder`, we obtain `inorder = sort(preorder)`.
  3. We then build the unique binary tree using the standard Preorder + Inorder construction algorithm with a hash map.
- **Complexity:**
  - Time Complexity: $\mathcal{O}(N \log N)$ to sort + $\mathcal{O}(N)$ to build tree = $\mathcal{O}(N \log N)$.
  - Space Complexity: $\mathcal{O}(N)$ to store `inorder` and the hash map.
- **Verdict:** Much better, but unnecessary sorting overhead.

---

### Method 3: Optimal Recursive Approach (Upper Bound / Range Constraint)
- **Key Insight:** 
  - The first element of `preorder` is always the root.
  - Any subsequent elements belong to the left subtree as long as they are smaller than the current root's value.
  - Once an element exceeds the current root's value, it must belong to the right subtree of an ancestor.
  - Hence, every subtree has an implicit **upper bound**:
    - Left subtree upper bound = `root.val`.
    - Right subtree upper bound = parent's upper bound.
- **Algorithm:**
  ```text
  Function build(preorder, index, bound):
      If index == preorder.length OR preorder[index] > bound:
          Return null
      root = new TreeNode(preorder[index++])
      root.left = build(preorder, index, root.val)
      root.right = build(preorder, index, bound)
      Return root
  ```
- **Complexity:**
  - Time Complexity: $\mathcal{O}(N)$ because each node is visited and created exactly once.
  - Space Complexity: $\mathcal{O}(H)$ for the recursive call stack.
- **Call-Stack Caveat:** In a deeply skewed tree where $H = 10^4$, recursive call-stack frames can trigger a `StackOverflowError` in constrained execution environments.

---

### Method 4: Optimal Iterative Approach (Monotonic Stack) — Production-Grade
To achieve true **stack-safety** with $\mathcal{O}(N)$ time and $\mathcal{O}(H)$ memory on the heap, we can simulate the construction iteratively using a stack.

- **Stack Invariant:**
  - The stack stores ancestors that have not yet had their right children assigned.
  - The values in the stack strictly decrease from bottom to top (monotonic decreasing order).

- **Algorithm:**
  1. Initialize `root = new TreeNode(preorder[0])`.
  2. Push `root` to an explicit stack.
  3. For each subsequent value `val` in `preorder[1..N-1]`:
     - Create `node = new TreeNode(val)`.
     - **Case A: `val < stack.peek().val`**
       - Because `val` is smaller than the top of the stack, it must be the immediate **left child** of `stack.peek()`.
       - Attach `stack.peek().left = node`.
       - Push `node` to the stack.
     - **Case B: `val > stack.peek().val`**
       - The current value belongs to the **right subtree** of some ancestor.
       - Pop nodes from the stack while `!stack.isEmpty() && stack.peek().val < val`.
       - The last popped node is the deepest ancestor whose value is less than `val`.
       - Attach `lastPopped.right = node`.
       - Push `node` to the stack.
  4. Return `root`.

---

## Detailed Step-by-Step Dry Run

Consider `preorder = [8, 5, 1, 7, 10, 12]`:

1. **Process `8`:**
   - Create root `8`.
   - `stack = [8]`.

2. **Process `5`:**
   - $5 < 8 \implies 8.\text{left} = 5$.
   - Push `5`. `stack = [8, 5]`.

3. **Process `1`:**
   - $1 < 5 \implies 5.\text{left} = 1$.
   - Push `1`. `stack = [8, 5, 1]`.

4. **Process `7`:**
   - $7 > 1$: Pop `1` (`lastPopped = 1`).
   - $7 > 5$: Pop `5` (`lastPopped = 5`).
   - Top of stack is now `8` ($8 > 7$, stop popping).
   - Attach $5.\text{right} = 7$.
   - Push `7`. `stack = [8, 7]`.

5. **Process `10`:**
   - $10 > 7$: Pop `7` (`lastPopped = 7`).
   - $10 > 8$: Pop `8` (`lastPopped = 8`).
   - Stack is now empty.
   - Attach $8.\text{right} = 10$.
   - Push `10`. `stack = [10]`.

6. **Process `12`:**
   - $12 > 10$: Pop `10` (`lastPopped = 10`).
   - Stack is empty.
   - Attach $10.\text{right} = 12$.
   - Push `12`. `stack = [12]`.

**Final Tree:**
```text
        8
      /   \
     5     10
    / \      \
   1   7      12
```

---

## Edge Case Analysis

1. **Single Node ($N = 1$):**
   - The loop runs 0 times; `root` is returned immediately.
2. **Strictly Ascending Array ($[1, 2, 3, 4, 5]$):**
   - Generates a degenerate right-skewed tree (linked-list structure).
   - In each step, the stack pops the previous node and attaches the new node to its right.
3. **Strictly Descending Array ($[5, 4, 3, 2, 1]$):**
   - Generates a degenerate left-skewed tree.
   - In each step, the new node is attached to the left of `stack.peek()`.
   - Stack size grows up to $N$ on the heap without overflowing the thread call stack.
4. **Large Values ($10^9$):**
   - All standard comparisons and pointer assignments function properly within standard signed 32-bit integers.

---

## Complexity Summary

| Metric | Brute Force Insertion | Sort + Inorder/Preorder | Recursive Upper Bound | Iterative Monotonic Stack |
| :--- | :--- | :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N^2)$ (worst) | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ |
| **Auxiliary Space** | $\mathcal{O}(H)$ | $\mathcal{O}(N)$ | $\mathcal{O}(H)$ call stack | $\mathcal{O}(H)$ heap space |
| **Stack Safety** | Risk on skewed trees | Risk on skewed trees | Risk of `StackOverflowError` | **100% Stack-Safe** |
| **Implementation** | Trivial | Moderate | Compact | Production-Grade |
