### Problem: Construct a BST from a preorder traversal
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Stack, Monotonic Stack

**Problem Statement**
Given an array of integers `preorder` representing the **preorder traversal** of a **Binary Search Tree (BST)**, reconstruct the unique Binary Search Tree and return its root node.

A **Binary Search Tree** is a binary tree where for every node:
1. All nodes in its **left subtree** have values strictly less than the node's value.
2. All nodes in its **right subtree** have values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

A **preorder traversal** of a binary tree visits nodes in the following order:
$$\text{Root} \longrightarrow \text{Left Subtree} \longrightarrow \text{Right Subtree}$$

It is guaranteed that there always exists a unique binary search tree corresponding to the given preorder sequence.

---

**Input Format**
* The first line contains an integer $N$ representing the number of elements in the `preorder` traversal array.
* The second line contains $N$ space-separated integers representing the elements of the `preorder` traversal.
* If $N = 0$, the tree is empty and the second line is omitted.

**Output Format**
* Print the level-order traversal of the constructed Binary Search Tree as space-separated integers, where `-1` represents a `null` node and trailing `null` pointers are omitted.

---

**Constraints**
* $1 \le N \le 10^4$
* $0 \le \text{preorder}[i] \le 10^9$
* All values in `preorder` are unique.
* `preorder` is guaranteed to be a valid preorder traversal of a Binary Search Tree.
* `-1` denotes a `null` node in the level-order output.

---

**Example 1**
**Input:**
```text
6
8 5 1 7 10 12
```

**Output:**
```text
8 5 10 1 7 -1 12
```

**Explanation:**
1. The first element of preorder is `8`, which is the root of the tree.
2. Elements following `8` that are smaller than `8` form its left subtree: `[5, 1, 7]`.
   - `5` is the root of this left subtree.
   - `1 < 5` forms the left child of `5`.
   - `7 > 5` (and `< 8`) forms the right child of `5`.
3. Elements greater than `8` form its right subtree: `[10, 12]`.
   - `10` is the root of this right subtree.
   - There are no elements smaller than `10`, so its left child is `null`.
   - `12 > 10` forms the right child of `10`.

Reconstructed BST:
```text
        8
      /   \
     5     10
    / \      \
   1   7      12
```
Level-order traversal: `8 5 10 1 7 -1 12`.

---

**Example 2**
**Input:**
```text
2
1 3
```

**Output:**
```text
1 -1 3
```

**Explanation:**
- `1` is the root.
- `3 > 1`, so `3` is the right child of `1`.
- The left child of `1` is empty (`null`).

Reconstructed BST:
```text
    1
     \
      3
```
Level-order traversal: `1 -1 3`.

---

**Example 3**
**Input:**
```text
3
2 1 3
```

**Output:**
```text
2 1 3
```

**Explanation:**
- `2` is the root.
- `1 < 2` is the left child of `2`.
- `3 > 2` is the right child of `2`.

Reconstructed BST:
```text
    2
   / \
  1   3
```
Level-order traversal: `2 1 3`.

---

**Example 4**
**Input:**
```text
5
5 4 3 2 1
```

**Output:**
```text
5 4 -1 3 -1 2 -1 1
```

**Explanation:**
Every element is strictly smaller than the previous element, resulting in a degenerate left-skewed Binary Search Tree:
```text
        5
       /
      4
     /
    3
   /
  2
 /
1
```
Level-order traversal: `5 4 -1 3 -1 2 -1 1`.
