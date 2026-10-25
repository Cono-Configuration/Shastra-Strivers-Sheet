# Editorial: BST Iterator

## Problem Overview
We need to design and implement an iterator class, `BSTIterator`, that visits the elements of a **Binary Search Tree (BST)** in **in-order traversal** order (strictly ascending sorted order):
- `BSTIterator(TreeNode root)`: Initializes the iterator over the given BST.
- `int next()`: Returns the next smallest element in the BST.
- `boolean hasNext()`: Returns `true` if there are still elements remaining to be visited, and `false` otherwise.

**Key Design Requirement:**
- `next()` must run in **amortized $\mathcal{O}(1)$ time**.
- `hasNext()` must run in **$\mathcal{O}(1)$ time**.
- Auxiliary space must be bounded by **$\mathcal{O}(H)$**, where $H$ is the height of the BST ($\mathcal{O}(\log N)$ for balanced trees).

---

## Brute Force Approach: Array Flattening
- **Idea:**
  During constructor initialization, perform a full in-order traversal of the BST and store all $N$ node values in a `List<Integer>` or array. Maintain an index pointer `idx = 0`.
  - `next()`: returns `list.get(idx++)`.
  - `hasNext()`: returns `idx < list.size()`.
- **Drawbacks:**
  - **Memory:** Requires $\mathcal{O}(N)$ memory to store the entire tree upfront. For large trees (e.g., $10^7$ nodes), this consumes substantial heap memory even if the client only queries the first 5 elements.
  - **Latency:** Initialization takes $\mathcal{O}(N)$ time before the first `next()` call can even be served.

---

## Optimal Approach: Controlled Lazy In-Order Traversal (Monotonic Stack)

Instead of flattening the entire tree into memory upfront, we simulate the standard iterative in-order traversal **on demand** (lazily) using an explicit stack.

### 1. Invariant of In-Order Traversal
In an in-order traversal ($\text{Left} \rightarrow \text{Root} \rightarrow \text{Right}$), the next smallest element is always the **leftmost descendant** among the currently reachable nodes.

### 2. Constructor `BSTIterator(root)`
- Initialize an empty stack: `stack = new ArrayDeque<>()`.
- Call a helper function `pushAll(root)`:
  - Starting from `root`, traverse continuously to the left child, pushing every node encountered onto the stack until reaching `null`.
  - After this operation, the top of the stack is the smallest element in the entire BST.
  - The stack holds at most $H$ nodes (the leftmost path).

### 3. Method `next()`
1. Pop the top node from the stack: `TreeNode curr = stack.pop()`.
   - `curr.val` is the next smallest element to be returned.
2. Since `curr` has now been visited, its **right subtree** must be traversed next (according to $\text{Left} \rightarrow \text{Node} \rightarrow \text{Right}$).
   - Call `pushAll(curr.right)`: if `curr.right` exists, push it and all its leftmost descendants onto the stack.
3. Return `curr.val`.

### 4. Method `hasNext()`
- Simply check whether the stack contains any nodes:
  $$\text{return } !\text{stack.isEmpty()}$$
- If the stack is empty, every node in the BST has been visited.

---

## Amortized Complexity Analysis: Why `next()` is $\mathcal{O}(1)$

A common concern is that `pushAll(curr.right)` can push up to $H$ nodes in a single call, appearing to take $\mathcal{O}(H)$ worst-case time.

However, consider the **aggregate cost across all $N$ elements**:
1. Every node in the BST is pushed onto the stack **exactly once**:
   - Nodes along the initial leftmost branch are pushed in the constructor.
   - All other nodes are pushed when their parent's right child is processed via `pushAll(curr.right)`.
2. Every node is popped from the stack **exactly once** across all $N$ calls to `next()`.
3. Total stack push operations across all calls = $N$.
4. Total stack pop operations across all calls = $N$.

Therefore, the total time spent across all $N$ calls to `next()` is:
$$\text{Total Time} = \mathcal{O}(N)$$
$$\text{Amortized Time per } \text{next}() = \frac{\mathcal{O}(N)}{N} = \mathcal{O}(1)$$

---

## Step-by-Step Dry Run

Consider the BST from Example 1:
```text
        7
       / \
      3   15
         /  \
        9   20
```

1. **Initialization `BSTIterator(root = 7)`:**
   - `pushAll(7)`: pushes `7`, then `3`.
   - `stack = [7, 3]` (top is `3`).

2. **Operation 1: `next()`:**
   - Pop `3`.
   - `3.right` is `null`, nothing pushed.
   - `stack = [7]`.
   - Returns **`3`**.

3. **Operation 2: `next()`:**
   - Pop `7`.
   - `7.right` is `15`. Call `pushAll(15)`: pushes `15`, then `9`.
   - `stack = [15, 9]` (top is `9`).
   - Returns **`7`**.

4. **Operation 3: `hasNext()`:**
   - `!stack.isEmpty()` is true.
   - Returns **`true`**.

5. **Operation 4: `next()`:**
   - Pop `9`.
   - `9.right` is `null`, nothing pushed.
   - `stack = [15]`.
   - Returns **`9`**.

6. **Operation 5: `hasNext()`:**
   - Returns **`true`**.

7. **Operation 6: `next()`:**
   - Pop `15`.
   - `15.right` is `20`. Call `pushAll(20)`: pushes `20`.
   - `stack = [20]`.
   - Returns **`15`**.

8. **Operation 7: `hasNext()`:**
   - Returns **`true`**.

9. **Operation 8: `next()`:**
   - Pop `20`.
   - `20.right` is `null`.
   - `stack = []`.
   - Returns **`20`**.

10. **Operation 9: `hasNext()`:**
    - `stack.isEmpty()` is true.
    - Returns **`false`**.

---

## Edge Case Analysis

1. **Single Node Tree ($N = 1$):**
   - Constructor pushes root.
   - First `hasNext()` returns `true`.
   - First `next()` pops root and stack becomes empty.
   - Subsequent `hasNext()` returns `false`.
2. **Strictly Left-Skewed Tree ($3 \leftarrow 2 \leftarrow 1$):**
   - Constructor pushes all nodes into the stack.
   - Each `next()` simply pops the top element without pushing anything (since right children are all `null`).
   - Maximum stack memory used is $N = H$.
3. **Strictly Right-Skewed Tree ($1 \rightarrow 2 \rightarrow 3$):**
   - Constructor pushes only the root (`1`).
   - Each `next()` pops the current node and pushes its right child.
   - Maximum stack size at any moment is strictly $1$!
4. **Multiple `hasNext()` Invocations:**
   - Calling `hasNext()` multiple times without calling `next()` is completely safe and takes $\mathcal{O}(1)$ time without modifying the iterator state.

---

## Complexity Comparison

| Metric | Full Array In-Order Flattening | Monotonic Stack (Optimal) |
| :--- | :--- | :--- |
| **Constructor Time** | $\mathcal{O}(N)$ | $\mathcal{O}(H)$ |
| **`next()` Time** | $\mathcal{O}(1)$ | **Amortized $\mathcal{O}(1)$** |
| **`hasNext()` Time** | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |
| **Auxiliary Space** | $\mathcal{O}(N)$ upfront | **$\mathcal{O}(H)$** ($\mathcal{O}(\log N)$ on balanced trees) |
| **Call Stack Safety** | Recursion risk on skewed trees | **100% Stack-Safe (Iterative)** |
