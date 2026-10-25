# Editorial: Insert a given node in BST

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** and an integer value $val$. Our goal is to insert $val$ into the BST in such a way that the binary search tree invariants remain valid, and return the root of the modified tree.

It is guaranteed that $val$ does not already exist in the BST.

---

## Understanding Binary Search Tree Properties
A Binary Search Tree (BST) is characterized by the following invariant for every node $u$:
1. For every node $v$ in the **left subtree** of $u$: $\text{val}(v) < \text{val}(u)$.
2. For every node $w$ in the **right subtree** of $u$: $\text{val}(w) > \text{val}(u)$.
3. Both the left and right subtrees are themselves valid BSTs.

Because of this ordering:
- Whenever we search for or insert a value $val$, we can deterministically decide at each step whether to move **left** (if $val < \text{curr.val}$) or **right** (if $val > \text{curr.val}$).
- The standard BST insertion algorithm does **not** rebalance or rotate the tree; it finds the unique vacant child pointer (a `null` pointer at an existing node) where $val$ belongs and attaches a new leaf node there.

---

## Comparison: Recursive vs. Iterative Approach

### 1. Recursive Approach
- **Mechanism:**
  - Base case: If `root == null`, return `new TreeNode(val)`.
  - If `val < root.val`: `root.left = insertIntoBST(root.left, val)`.
  - If `val > root.val`: `root.right = insertIntoBST(root.right, val)`.
  - Return `root`.
- **Drawback:** In the worst-case scenario (a skewed tree where $H = N$), recursion requires $\mathcal{O}(N)$ frames on the call stack. For constraints up to $N = 10^4$, this risks a `StackOverflowError` in memory-constrained environments.

### 2. Iterative Approach (Optimal & Stack-Safe)
- **Mechanism:**
  - If the tree is initially empty (`root == null`), immediately return `new TreeNode(val)`.
  - Maintain a pointer `curr = root`.
  - In a loop:
    - If $val < \text{curr.val}$:
      - If `curr.left != null`, advance `curr = curr.left`.
      - Else, attach `curr.left = new TreeNode(val)` and break.
    - If $val > \text{curr.val}$:
      - If `curr.right != null`, advance `curr = curr.right`.
      - Else, attach `curr.right = new TreeNode(val)` and break.
  - Return the original `root`.
- **Advantage:** $\mathcal{O}(1)$ auxiliary space, zero function call overhead, completely stack-safe even for $N = 10^4$ or deeper skewed trees.

---

## Detailed Algorithm (Iterative)

```text
Algorithm InsertIntoBST(root, val):
1. If root is NULL:
     Return new TreeNode(val)
2. curr <- root
3. While TRUE:
     a. If val < curr.val:
          If curr.left is not NULL:
               curr <- curr.left
          Else:
               curr.left <- new TreeNode(val)
               Break
     b. Else (val > curr.val):
          If curr.right is not NULL:
               curr <- curr.right
          Else:
               curr.right <- new TreeNode(val)
               Break
4. Return root
```

---

## Step-by-Step Dry Run

### Walkthrough 1: Example 1
- **Initial Tree:** `[4, 2, 7, 1, 3]`
- **Insert Value:** $val = 5$

```text
Initial State:
        4
       / \
      2   7
     / \
    1   3
```

1. **Step 1:** Start at `curr = 4`.
   - Compare $val = 5$ with `curr.val = 4`.
   - Since $5 > 4$, we move to the right child.
   - `curr.right` exists (node `7`), so `curr = 7`.

2. **Step 2:** At `curr = 7`.
   - Compare $val = 5$ with `curr.val = 7`.
   - Since $5 < 7$, we look at the left child.
   - `curr.left` is `null`.
   - Attach new node `5` as `curr.left = new TreeNode(5)`.
   - Break out of the loop.

3. **Final Tree:**
```text
        4
       / \
      2   7
     / \ /
    1  3 5
```
- **Level-Order Output:** `4 2 7 1 3 5`.

---

### Walkthrough 2: Example 2
- **Initial Tree:** `[40, 20, 60, 10, 30, 50, 70]`
- **Insert Value:** $val = 25$

```text
Initial State:
          40
        /    \
      20      60
     /  \    /  \
   10   30  50  70
```

1. **Step 1:** At `curr = 40`. $25 < 40 \implies$ `curr = curr.left` (node `20`).
2. **Step 2:** At `curr = 20`. $25 > 20 \implies$ `curr = curr.right` (node `30`).
3. **Step 3:** At `curr = 30`. $25 < 30 \implies$ `curr.left` is `null`.
   - Attach `curr.left = new TreeNode(25)`.
   - Break.

```text
Final Tree:
          40
        /    \
      20      60
     /  \    /  \
   10   30  50  70
       /
      25
```
- **Level-Order Traversal:** `40 20 60 10 30 50 70 -1 -1 25`.

---

## Complexity Analysis

| Metric | Complexity | Explanation |
| :--- | :--- | :--- |
| **Time Complexity (Average / Balanced BST)** | $\mathcal{O}(\log N)$ | For a balanced BST, the height $H \approx \log_2 N$. At each step, we eliminate half the remaining nodes. |
| **Time Complexity (Worst Case / Skewed BST)** | $\mathcal{O}(H) = \mathcal{O}(N)$ | If the BST is degenerate (e.g., elements inserted in ascending or descending order), height $H = N$. The search traverses the entire chain. |
| **Auxiliary Space Complexity** | $\mathcal{O}(1)$ | The iterative pointer traversal uses a constant number of references (`curr`), requiring zero auxiliary heap or stack memory. |
| **Total Space Complexity (I/O & Representation)** | $\mathcal{O}(N)$ | Storing the tree nodes and serializing the level-order output requires $\mathcal{O}(N)$ memory. |

---

## Edge Cases & Defensive Design

1. **Empty BST ($N = 0$):**
   - When the initial tree contains no nodes (`root == null`), the inserted value becomes the root of a new 1-node BST.
   - Result: `[val]`.

2. **Single-Node Tree ($N = 1$):**
   - If $val < \text{root.val}$, it attaches as `root.left`.
   - If $val > \text{root.val}$, it attaches as `root.right`.

3. **Global Minimum / Maximum Insertions:**
   - If $val$ is strictly smaller than all elements in the BST, the traversal moves left at every decision point and attaches as the leftmost leaf.
   - If $val$ is strictly larger than all elements in the BST, the traversal moves right at every decision point and attaches as the rightmost leaf.

4. **Degenerate / Skewed Trees:**
   - For a strictly left-skewed or right-skewed tree of size up to $10^4$, the iterative traversal never causes stack overflow, unlike recursive approaches.

5. **Serialization Representation:**
   - Level-order traversal utilizes `-1` for `null` nodes.
   - All trailing `-1` values at the end of the BFS serialization are trimmed to produce clean, compact representation matching standard competitive programming standards.
