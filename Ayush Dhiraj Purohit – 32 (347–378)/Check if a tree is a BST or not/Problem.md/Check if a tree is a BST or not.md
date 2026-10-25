### Problem: Check if a tree is a BST or not
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Depth-First Search, Breadth-First Search

**Problem Statement**
Given the root of a binary tree, determine whether it is a valid **Binary Search Tree (BST)**.

A binary tree is considered a valid **Binary Search Tree** if and only if:
1. The **left subtree** of every node contains only nodes with values **strictly less than** the node's value.
2. The **right subtree** of every node contains only nodes with values **strictly greater than** the node's value.
3. Both the left and right subtrees must themselves be valid binary search trees.

An empty tree ($N = 0$) and a single-node tree ($N = 1$) are valid BSTs by definition.
Duplicate values anywhere in the tree violate the strict BST inequality and make the tree invalid.

---

**Input Format**
* The first line contains an integer $N$ representing the number of elements in the level-order serialization of the binary tree.
* If $N > 0$, the second line contains $N$ space-separated integers representing the level-order traversal of the binary tree, where `-1` denotes a `null` node.
* If $N = 0$, the tree is empty and the second line is omitted.

**Output Format**
* Print `true` if the given binary tree is a valid Binary Search Tree.
* Print `false` otherwise.

---

**Constraints**
* $0 \le N \le 10^4$
* $0 \le \text{Node.val} \le 10^9$
* `-1` represents a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
7
5 3 6 2 4 -1 7
```

**Output:**
```text
true
```

**Explanation:**
The binary tree:
```text
        5
       / \
      3   6
     / \   \
    2   4   7
```
* At root $5$: left subtree has values $\{2, 3, 4\}$ (all $< 5$), right subtree has $\{6, 7\}$ (all $> 5$).
* At node $3$: left child $2 < 3$, right child $4 > 3$.
* At node $6$: right child $7 > 6$.
Every node satisfies the strict BST property. Output is `true`.

---

**Example 2**
**Input:**
```text
7
5 3 6 4 2 -1 7
```

**Output:**
```text
false
```

**Explanation:**
The binary tree:
```text
        5
       / \
      3   6
     / \   \
    4   2   7
```
At node $3$, its left child is $4$ ($4 > 3$) and its right child is $2$ ($2 < 3$). Both violate the BST condition. Output is `false`.

---

**Example 3**
**Input:**
```text
5
5 1 4 -1 -1 3 6
```

**Output:**
```text
false
```

**Explanation:**
The binary tree:
```text
        5
       / \
      1   4
         / \
        3   6
```
Although node $4$ has left child $3$ ($3 < 4$) and right child $6$ ($6 > 4$), node $4$ is in the **right subtree** of the root $5$.
All nodes in the right subtree of $5$ must be strictly greater than $5$. However, $4 < 5$ and $3 < 5$. Therefore, the tree is not a valid BST. Output is `false`.

---

**Example 4**
**Input:**
```text
1
10
```

**Output:**
```text
true
```

**Explanation:**
A single-node tree satisfies all BST conditions trivially. Output is `true`.

---

**Example 5**
**Input:**
```text
0
```

**Output:**
```text
true
```

**Explanation:**
An empty tree has no nodes violating the BST invariant. Output is `true`.
