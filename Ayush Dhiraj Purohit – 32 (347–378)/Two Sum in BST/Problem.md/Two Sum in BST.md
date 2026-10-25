### Problem: Two Sum in BST
**Difficulty:** Easy / Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Two Pointers, Stack, Hash Table

**Problem Statement**
Given the root node of a **Binary Search Tree (BST)** and an integer target $k$, return `true` if there exist two **distinct** nodes in the BST such that the sum of their values equals $k$, otherwise return `false`.

A **Binary Search Tree** is a binary tree where for every node:
1. All nodes in its **left subtree** have values strictly less than the node's value.
2. All nodes in its **right subtree** have values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

Note: A single node cannot be added to itself to form the target sum $k$; the two elements must come from two distinct nodes.

---

**Input Format**
* The first line contains two space-separated integers $N$ and $k$:
  * $N$ represents the number of elements in the level-order serialization of the BST.
  * $k$ represents the target sum.
* The second line contains $N$ space-separated integers representing the level-order traversal of the BST, where `-1` denotes a `null` node. Trailing `null` pointers are omitted.

**Output Format**
* Print `true` if there exist two distinct nodes in the BST whose values sum to $k$, otherwise print `false`.

---

**Constraints**
* $1 \le \text{number of nodes in the BST} \le 10^4$
* $1 \le N \le 2 \times 10^4$
* $0 \le \text{Node.val} \le 10^9$
* All values in the BST are unique.
* $-10^9 \le k \le 2 \times 10^9$
* `-1` denotes a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
7 9
5 3 6 2 4 -1 7
```

**Output:**
```text
true
```

**Explanation:**
The given BST is:
```text
        5
       / \
      3   6
     / \   \
    2   4   7
```
- In-order traversal: `[2, 3, 4, 5, 6, 7]`.
- Multiple valid pairs sum up to $k = 9$:
  - $2 + 7 = 9$
  - $3 + 6 = 9$
  - $4 + 5 = 9$
- Since at least one pair exists, the output is `true`.

---

**Example 2**
**Input:**
```text
7 14
5 3 6 2 4 -1 7
```

**Output:**
```text
false
```

**Explanation:**
Using the same BST from Example 1:
- The maximum possible sum of two distinct nodes is $6 + 7 = 13$, which is strictly less than $14$.
- No pair of distinct nodes can sum to $14$.
- Output: `false`.

---

**Example 3**
**Input:**
```text
3 4
2 1 3
```

**Output:**
```text
true
```

**Explanation:**
The given BST is:
```text
    2
   / \
  1   3
```
- The pair of nodes $(1, 3)$ has sum $1 + 3 = 4$.
- Output: `true`.

---

**Example 4**
**Input:**
```text
1 2
1
```

**Output:**
```text
false
```

**Explanation:**
- The tree contains only a single node with value `1`.
- A node cannot be paired with itself to form $1 + 1 = 2$.
- Because two distinct nodes are required, no valid pair exists.
- Output: `false`.
