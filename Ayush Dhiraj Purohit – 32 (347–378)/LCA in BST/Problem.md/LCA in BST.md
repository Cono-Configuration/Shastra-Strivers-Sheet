### Problem: LCA in BST
**Difficulty:** Easy / Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Divide and Conquer

**Problem Statement**
You are given the root node of a **Binary Search Tree (BST)** and two distinct node values $p$ and $q$ that are guaranteed to exist in the BST.

A **Binary Search Tree** is a binary tree where for every node:
1. All nodes in its **left subtree** have values strictly less than the node's value.
2. All nodes in its **right subtree** have values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

According to the definition of **Lowest Common Ancestor (LCA)** on Wikipedia:
> "The lowest common ancestor is defined between two nodes $p$ and $q$ as the lowest node in $T$ that has both $p$ and $q$ as descendants (where we allow a node to be a descendant of itself)."

Your task is to find the **Lowest Common Ancestor** of nodes $p$ and $q$ in the given BST and return its value.

---

**Input Format**
* The first line contains three space-separated integers $N$, $p$, and $q$:
  * $N$ represents the number of elements in the level-order serialization of the BST.
  * $p$ and $q$ represent the values of the two target nodes whose lowest common ancestor is to be determined.
* The second line contains $N$ space-separated integers representing the level-order traversal of the BST, where `-1` denotes a `null` node. Trailing `null` pointers are omitted.

**Output Format**
* Print a single integer representing the value of the Lowest Common Ancestor node of $p$ and $q$.

---

**Constraints**
* $2 \le \text{number of nodes in the BST} \le 10^4$
* $1 \le N \le 2 \times 10^4$
* $0 \le \text{Node.val}, p, q \le 10^9$
* All values in the BST are unique.
* $p \ne q$
* Both $p$ and $q$ are guaranteed to exist in the BST.
* `-1` denotes a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
7 2 4
5 3 6 2 4 -1 7
```

**Output:**
```text
3
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
- We are searching for the LCA of $p = 2$ and $q = 4$.
- Starting at the root node ($5$): both $2$ and $4$ are strictly less than $5$, so the LCA must lie in the left subtree. We move to node $3$.
- At node $3$: $p = 2 < 3$ and $q = 4 > 3$. The paths to $2$ and $4$ diverge here (one is in the left subtree, one in the right subtree).
- Therefore, node $3$ is the split point and the Lowest Common Ancestor.

---

**Example 2**
**Input:**
```text
7 2 7
5 3 6 2 4 -1 7
```

**Output:**
```text
5
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
- We are searching for the LCA of $p = 2$ and $q = 7$.
- Starting at the root node ($5$): $p = 2 < 5$ (in the left subtree) and $q = 7 > 5$ (in the right subtree).
- Since one node lies in the left subtree and the other in the right subtree, root node $5$ is the split point.
- Hence, the LCA is $5$.

---

**Example 3**
**Input:**
```text
2 2 1
2 1
```

**Output:**
```text
2
```

**Explanation:**
The given BST is:
```text
    2
   /
  1
```
- We are searching for the LCA of $p = 2$ and $q = 1$.
- By definition, a node can be a descendant of itself.
- Since node $2$ is the root and node $1$ is a descendant of node $2$, the LCA of $2$ and $1$ is $2$.

---

**Example 4**
**Input:**
```text
11 7 9
6 2 8 0 4 7 9 -1 -1 3 5
```

**Output:**
```text
8
```

**Explanation:**
The given BST is:
```text
          6
        /   \
       2     8
      / \   / \
     0   4 7   9
        / \
       3   5
```
- Starting at root $6$: both $p = 7$ and $q = 9$ are strictly greater than $6$. Thus, we move to the right child, node $8$.
- At node $8$: $p = 7 < 8$ while $q = 9 > 8$. The paths split here.
- Therefore, the LCA of $7$ and $9$ is $8$.
