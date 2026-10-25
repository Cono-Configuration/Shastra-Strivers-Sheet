### Problem: Correct BST with two nodes swapped
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Depth-First Search, Inorder Traversal, Two Pointers

**Problem Statement**
You are given the root of a **Binary Search Tree (BST)**, where the values of **exactly two nodes** were swapped by mistake.

Your task is to recover the tree without changing its structure (i.e., do not modify pointers; only swap back the misplaced node values so that the binary search tree property is restored).

A **Binary Search Tree** is a binary tree where for every node:
1. All nodes in its **left subtree** have values strictly less than the node's value.
2. All nodes in its **right subtree** have values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

An **in-order traversal** ($\text{Left} \rightarrow \text{Root} \rightarrow \text{Right}$) of a valid BST always visits node values in strictly ascending sorted order. When two nodes are swapped, this monotonic property is violated.

---

**Input Format**
* The first line contains an integer $N$ representing the number of elements in the level-order serialization of the corrupted BST.
* The second line contains $N$ space-separated integers representing the level-order traversal of the corrupted BST, where `-1` denotes a `null` node. Trailing `null` pointers are omitted.

**Output Format**
* Print the space-separated integers representing the level-order traversal of the recovered (corrected) BST, with `-1` denoting `null` nodes and trailing `null` pointers omitted.

---

**Constraints**
* $2 \le \text{number of nodes in the tree} \le 10^4$
* $1 \le N \le 2 \times 10^4$
* $0 \le \text{Node.val} \le 10^9$
* All values in the original valid BST were unique.
* Exactly two nodes were swapped by mistake.
* `-1` denotes a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
5
1 3 -1 -1 2
```

**Output:**
```text
3 1 -1 -1 2
```

**Explanation:**
Corrupted Tree:
```text
    1
   /
  3
   \
    2
```
- In-order traversal: `[3, 2, 1]`.
- Sorted order should be `[1, 2, 3]`.
- The nodes with values `1` and `3` were swapped by mistake.
- Swapping `1` and `3` back restores the BST:
```text
    3
   /
  1
   \
    2
```
- Level-order traversal of recovered tree: `3 1 -1 -1 2`.

---

**Example 2**
**Input:**
```text
6
3 1 4 -1 -1 2
```

**Output:**
```text
2 1 4 -1 -1 3
```

**Explanation:**
Corrupted Tree:
```text
      3
     / \
    1   4
       /
      2
```
- In-order traversal: `[1, 3, 2, 4]`.
- Notice the inversion between `3` and `2` ($3 > 2$).
- Swapping `3` and `2` produces the sorted in-order traversal `[1, 2, 3, 4]`.
Recovered Tree:
```text
      2
     / \
    1   4
       /
      3
```
- Level-order traversal: `2 1 4 -1 -1 3`.

---

**Example 3**
**Input:**
```text
3
2 3 1
```

**Output:**
```text
2 1 3
```

**Explanation:**
Corrupted Tree:
```text
    2
   / \
  3   1
```
- In-order traversal: `[3, 2, 1]`.
- Swapping the left child `3` and right child `1` restores the valid BST:
```text
    2
   / \
  1   3
```
- Level-order traversal: `2 1 3`.

---

**Example 4**
**Input:**
```text
2
1 2
```

**Output:**
```text
2 1
```

**Explanation:**
Corrupted Tree:
```text
  1
 /
2
```
- Node `2` is the left child of `1`, which is invalid since $2 > 1$.
- Swapping `1` and `2` yields root `2` with left child `1`.
- Level-order traversal: `2 1`.
