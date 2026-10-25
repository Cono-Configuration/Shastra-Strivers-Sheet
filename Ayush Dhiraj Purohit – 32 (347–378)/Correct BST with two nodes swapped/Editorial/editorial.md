# Editorial: Correct BST with two nodes swapped

## Problem Overview
We are given the root of a **Binary Search Tree (BST)** where the values of **exactly two nodes** were accidentally swapped. Our objective is to restore the original BST without modifying the tree's pointer structure — we must only swap back the two misplaced values.

---

## Fundamental Mathematical Invariant
In any valid Binary Search Tree, an **in-order traversal** ($\text{Left} \rightarrow \text{Root} \rightarrow \text{Right}$) visits all node values in **strictly ascending sorted order**:
$$A_1 < A_2 < A_3 < \dots < A_N$$

When exactly two nodes with indices $i$ and $j$ ($i < j$) are swapped such that $A_i > A_j$, the sorted sequence is corrupted. By inspecting where the monotonicity condition $A_k < A_{k+1}$ fails, we can deterministically locate the swapped pair.

---

## Anomaly Detection: Two Distinct Scenarios

During an in-order traversal, we track the previously visited node `prev`. A violation occurs whenever:
$$\text{prev.val} > \text{curr.val}$$

There are only two structural possibilities:

### Scenario 1: The Swapped Nodes Are Non-Adjacent in In-Order Sequence
- **Example:** Correct order: `[1, 2, 3, 4, 5]`. Nodes `2` and `5` are swapped $\implies `[1, 5, 3, 4, 2]`.
- **Violations:**
  1. First violation: $5 > 3$. Here, the larger element (`5`) has migrated forward into an earlier position.
     - We record `first = prev` (`5`) and `middle = curr` (`3`).
  2. Second violation: $4 > 2$. Here, the smaller element (`2`) has migrated backward into a later position.
     - We record `last = curr` (`2`).
- **Correction:** Swap the values of `first` and `last`.

### Scenario 2: The Swapped Nodes Are Adjacent in In-Order Sequence
- **Example:** Correct order: `[1, 2, 3, 4, 5]`. Nodes `2` and `3` are swapped $\implies `[1, 3, 2, 4, 5]`.
- **Violations:**
  1. Only a single violation occurs: $3 > 2$.
     - We record `first = prev` (`3`) and `middle = curr` (`2`).
  2. No second violation ever occurs, so `last` remains `null`.
- **Correction:** Swap the values of `first` and `middle`.

---

## Evolution of Approaches

### Method 1: In-Order Traversal + Sorting (Brute Force)
- **Idea:**
  1. Perform an in-order traversal and copy all node values into an array of size $N$.
  2. Sort the array in $\mathcal{O}(N \log N)$ time.
  3. Perform a second in-order traversal, overwriting each node's value with the corresponding sorted value.
- **Complexity:** $\mathcal{O}(N \log N)$ time, $\mathcal{O}(N)$ auxiliary space.
- **Limitation:** Inefficient sorting and requires linear memory allocations.

---

### Method 2: Recursive In-Order Pointer Tracking
- **Idea:** Perform in-order traversal recursively, updating global pointers `prev`, `first`, `middle`, and `last`.
- **Complexity:** $\mathcal{O}(N)$ time, $\mathcal{O}(H)$ call stack space.
- **Drawback:** In degenerate skewed trees where $H = 10^4$, recursive function frames can trigger a `StackOverflowError`.

---

### Method 3: Iterative In-Order Pointer Tracking (Optimal & Stack-Safe)
- **Mechanism:**
  Use an explicit stack on the heap to simulate the in-order traversal iteratively:
  1. Initialize `stack = new ArrayDeque<>()`, `curr = root`, and pointers `first = middle = last = prev = null`.
  2. While `curr != null` or `!stack.isEmpty()`:
     - Push all left descendants onto the stack.
     - Pop the top node into `curr`.
     - Check violation: if `prev != null && prev.val > curr.val`:
       - If `first == null`: `first = prev; middle = curr;`
       - Else: `last = curr;`
     - Update `prev = curr`.
     - Advance `curr = curr.right`.
  3. Perform the swap:
     - If `last != null`: swap `first.val` and `last.val`.
     - Else: swap `first.val` and `middle.val`.
- **Complexity:** $\mathcal{O}(N)$ time, $\mathcal{O}(H)$ heap space. 100% stack-safe.

---

## Step-by-Step Dry Run

### Walkthrough on Example 1: `[1, 3, null, null, 2]`
Corrupted tree:
```text
    1
   /
  3
   \
    2
```
1. In-order traversal sequence: `3 -> 2 -> 1`.
2. Step 1 (`curr = 3`): `prev = null`. Set `prev = 3`.
3. Step 2 (`curr = 2`): `prev.val (3) > curr.val (2)`.
   - First violation! Set `first = 3`, `middle = 2`.
   - Set `prev = 2`.
4. Step 3 (`curr = 1`): `prev.val (2) > curr.val (1)`.
   - Second violation! Set `last = 1`.
   - Set `prev = 1`.
5. Post-traversal: both `first` (`3`) and `last` (`1`) are non-null.
   - Swap `first.val` and `last.val` $\implies$ `3` becomes `1`, and `1` becomes `3`.
6. Recovered in-order traversal: `1 -> 2 -> 3`.

---

## Edge Case Analysis

1. **Adjacent vs. Non-Adjacent Swaps:**
   - Both cases are seamlessly handled by checking if `last` was populated or remained `null`.
2. **Root Node Is Involved:**
   - Works uniformly whether the root was swapped with a leaf, an internal node, or its immediate child.
3. **Minimal BST ($N = 2$):**
   - Swapped root and child (`1` as root, `2` as left child).
   - Exactly one violation detected; swapped back in $\mathcal{O}(1)$ operations.
4. **Deep Skewed BST ($H = N$):**
   - The iterative Monotonic Stack implementation avoids recursive call stack allocations, guaranteeing complete stability on chains of depth $10^4$.
5. **Large Values ($10^9$):**
   - Handled directly within standard signed 32-bit integers without overflow.

---

## Complexity Summary

| Metric | Brute Force (Sort In-Order) | Recursive In-Order | Iterative Stack (Optimal) |
| :--- | :--- | :--- | :--- |
| **Time Complexity** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N)$ | **$\mathcal{O}(N)$** |
| **Auxiliary Space** | $\mathcal{O}(N)$ | $\mathcal{O}(H)$ call stack | **$\mathcal{O}(H)$ heap** |
| **Stack Safety** | Recursion risk | `StackOverflowError` risk | **100% Stack-Safe** |
| **Passes Required** | 2 passes | 1 pass | **1 pass** |
