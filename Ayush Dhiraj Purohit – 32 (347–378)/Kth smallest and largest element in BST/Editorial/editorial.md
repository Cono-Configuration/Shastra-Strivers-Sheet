# Editorial: Kth smallest and largest element in BST

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** and an integer $K$. We need to return both the **$K^{\text{th}}$ smallest** and the **$K^{\text{th}}$ largest** elements in the BST (1-indexed).

All elements in the BST are strictly unique, and $K$ is guaranteed to satisfy $1 \le K \le \text{total nodes in the BST}$.

---

## Fundamental BST Property: In-order Traversal is Sorted

A Binary Search Tree has a defining invariant:
$$\text{values}(\text{left subtree}) < \text{value}(\text{root}) < \text{values}(\text{right subtree})$$

From this property:
1. **Standard In-order Traversal ($\text{Left} \to \text{Root} \to \text{Right}$)** visits nodes in **strictly ascending order**.
   - The $1^{\text{st}}$ visited node is the minimum element.
   - The $K^{\text{th}}$ visited node is the **$K^{\text{th}}$ smallest element**.
2. **Reverse In-order Traversal ($\text{Right} \to \text{Root} \to \text{Left}$)** visits nodes in **strictly descending order**.
   - The $1^{\text{st}}$ visited node is the maximum element.
   - The $K^{\text{th}}$ visited node is the **$K^{\text{th}}$ largest element**.

---

## Approaches

### Approach 1: Full In-order Traversal into an Array (Brute Force)
- Perform a complete in-order traversal and store all $n$ node values in a dynamic array `list`.
- Since `list` is sorted:
  - $K^{\text{th}}$ smallest = `list.get(K - 1)`
  - $K^{\text{th}}$ largest = `list.get(n - K)`
- **Drawback:** Requires $\mathcal{O}(n)$ auxiliary memory to store all node values and traverses the entire tree even if $K = 1$.

---

### Approach 2: Iterative Traversal with Early Stopping (Optimal & Stack-Safe)
Instead of visiting all $n$ nodes and storing them:
1. **$K^{\text{th}}$ Smallest:**
   - Run an iterative in-order traversal using an explicit stack (`Deque<TreeNode>`).
   - Maintain a counter `count = 0`.
   - Every time a node is popped from the stack, increment `count++`.
   - The moment `count == K`, return `curr.val` immediately!
2. **$K^{\text{th}}$ Largest:**
   - Run an iterative reverse in-order traversal ($\text{Right} \to \text{Root} \to \text{Left}$).
   - Every time a node is popped from the stack, increment `count++`.
   - The moment `count == K`, return `curr.val` immediately!

**Key Advantages:**
- **Early Exit:** For small $K$ (e.g. $K = 1$), only $\mathcal{O}(H)$ nodes are visited rather than the entire tree of $10^4$ nodes.
- **Stack-Safety:** Using a heap-allocated `ArrayDeque` avoids JVM call-stack limitations, safely handling deep or skewed trees.

---

### Approach 3: Morris Traversal ($\mathcal{O}(1)$ Auxiliary Space)
- By using threaded binary tree pointers (connecting a node's predecessor's right child to itself), Morris Traversal achieves in-order traversal in $\mathcal{O}(n)$ time and $\mathcal{O}(1)$ auxiliary memory without a stack or recursion.
- In competitive programming and online platforms, the iterative stack approach (Approach 2) is universally preferred due to cleaner non-mutating semantics and immediate early termination.

---

## Detailed Algorithm (Iterative Early Stop)

```text
Algorithm FindKthSmallest(root, K):
1. stack <- empty Deque
2. curr <- root, count <- 0
3. While curr is not NULL or stack is not empty:
     While curr is not NULL:
         stack.push(curr)
         curr <- curr.left
     curr <- stack.pop()
     count <- count + 1
     If count == K:
         Return curr.val
     curr <- curr.right
4. Return -1

Algorithm FindKthLargest(root, K):
1. stack <- empty Deque
2. curr <- root, count <- 0
3. While curr is not NULL or stack is not empty:
     While curr is not NULL:
         stack.push(curr)
         curr <- curr.right
     curr <- stack.pop()
     count <- count + 1
     If count == K:
         Return curr.val
     curr <- curr.left
4. Return -1
```

---

## Step-by-Step Dry Runs

### Walkthrough 1: Example 1
- **BST:** `[3, 1, 4, null, 2]`
- **Target Rank:** $K = 1$

```text
Tree:
        3
       / \
      1   4
       \
        2
```

1. **Finding $1^{\text{st}}$ Smallest:**
   - Push `3`, push `1`. `1.left` is `null`.
   - Pop `1`. `count = 1 == K`.
   - **$1^{\text{st}}$ Smallest = 1**. (Early exit after visiting just 2 nodes!)

2. **Finding $1^{\text{st}}$ Largest:**
   - Push `3`, push `4`. `4.right` is `null`.
   - Pop `4`. `count = 1 == K`.
   - **$1^{\text{st}}$ Largest = 4**. (Early exit after visiting just 2 nodes!)

- **Output:** `1 4`.

---

### Walkthrough 2: Example 2
- **BST:** `[5, 3, 6, 2, null, null, null, 1]`
- **Target Rank:** $K = 3$

```text
Tree:
          5
         / \
        3   6
       /
      2
     /
    1
```

1. **Finding $3^{\text{rd}}$ Smallest:**
   - Push `5`, `3`, `2`, `1`.
   - Pop `1` $\implies count = 1$. `1.right` is null.
   - Pop `2` $\implies count = 2$. `2.right` is null.
   - Pop `3` $\implies count = 3 == K$.
   - **$3^{\text{rd}}$ Smallest = 3**.

2. **Finding $3^{\text{rd}}$ Largest:**
   - Push `5`, `6`. `6.right` is null.
   - Pop `6` $\implies count = 1$. `6.left` is null.
   - Pop `5` $\implies count = 2$. Move to `5.left = 3`.
   - Push `3`. `3.right` is null.
   - Pop `3` $\implies count = 3 == K$.
   - **$3^{\text{rd}}$ Largest = 3**.

- **Output:** `3 3`.

---

## Complexity Analysis

| Approach | Time Complexity | Auxiliary Space Complexity | Explanation |
| :--- | :--- | :--- | :--- |
| **Approach 1 (Full Array)** | $\mathcal{O}(n)$ | $\mathcal{O}(n)$ | Stores all $n$ nodes in an array list. |
| **Approach 2 (Iterative Early Stop)** | $\mathcal{O}(H + K)$ | $\mathcal{O}(H)$ | Visits only until the $K^{\text{th}}$ element. Stack depth is bounded by tree height $H$. For balanced trees, $\mathcal{O}(\log n + K)$. |
| **Approach 3 (Morris Traversal)** | $\mathcal{O}(n)$ | $\mathcal{O}(1)$ | Modifies tree pointers temporarily using threaded links. |

---

## Edge Cases & Defensive Design

1. **Single Node BST ($n = 1, K = 1$):**
   - Both the $1^{\text{st}}$ smallest and $1^{\text{st}}$ largest element are identical to the root value.
   - Output: `root.val root.val`.

2. **Extremal Boundary $K = 1$:**
   - Minimum is the leftmost leaf; maximum is the rightmost leaf. Found in $\mathcal{O}(H)$ time with immediate early exit.

3. **Extremal Boundary $K = n$:**
   - The $n^{\text{th}}$ smallest is the maximum element; the $n^{\text{th}}$ largest is the minimum element.

4. **Degenerate / Skewed BST ($H = n$):**
   - For a straight left-skewed or right-skewed tree of $10^4$ nodes, the iterative approach maintains stack elements on the heap, ensuring 0 risk of stack overflow.
