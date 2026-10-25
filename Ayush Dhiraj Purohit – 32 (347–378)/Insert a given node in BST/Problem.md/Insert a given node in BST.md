### Problem: Insert a given node in BST
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree

**Problem Statement**
You are given the root node of a **Binary Search Tree (BST)** and an integer value $val$.

A **Binary Search Tree** is a binary tree where for every node:
1. The **left subtree** contains only nodes with values strictly less than the node's value.
2. The **right subtree** contains only nodes with values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

Your task is to insert $val$ into the BST such that the binary search tree properties are preserved.

Using the standard BST insertion algorithm, the new node is inserted as a leaf at the unique position determined by the BST ordering, without rearranging or restructuring existing nodes.

Return the root node of the modified BST after insertion.

---

**Input Format**
* The first line contains two space-separated integers $N$ (the number of elements in the level-order serialization of the initial BST) and $val$ (the integer value to be inserted into the BST).
* If $N > 0$, the second line contains $N$ space-separated integers representing the level-order traversal of the initial BST, where `-1` denotes a `null` node.
* If $N = 0$, the tree is initially empty and the second line is omitted.

**Output Format**
* Print space-separated integers representing the level-order traversal of the BST after inserting $val$, with `-1` denoting `null` nodes. Any trailing `-1` values should be omitted.

---

**Constraints**
* $0 \le N \le 10^4$
* $1 \le \text{Node.val}, val \le 10^9$
* All values present in the initial BST are unique.
* It is guaranteed that $val$ does not already exist in the initial BST.
* `-1` represents a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
5 5
4 2 7 1 3
```

**Output:**
```text
4 2 7 1 3 5
```

**Explanation:**
Initial BST:
```text
        4
       / \
      2   7
     / \
    1   3
```
We want to insert $val = 5$:
1. Compare $5$ with root $4$: since $5 > 4$, move to the right child ($7$).
2. Compare $5$ with $7$: since $5 < 7$, check the left child of $7$.
3. The left child of $7$ is `null`. Attach a new node with value $5$ as the left child of $7$.

Resulting BST:
```text
        4
       / \
      2   7
     / \ /
    1  3 5
```
Level-order traversal: `4 2 7 1 3 5`.

---

**Example 2**
**Input:**
```text
7 25
40 20 60 10 30 50 70
```

**Output:**
```text
40 20 60 10 30 50 70 -1 -1 25
```

**Explanation:**
Initial BST:
```text
          40
        /    \
      20      60
     /  \    /  \
   10   30  50  70
```
We want to insert $val = 25$:
1. At root $40$: $25 < 40 \implies$ move left to $20$.
2. At node $20$: $25 > 20 \implies$ move right to $30$.
3. At node $30$: $25 < 30 \implies$ left child of $30$ is `null`. Attach $25$ as the left child of $30$.

Resulting BST:
```text
          40
        /    \
      20      60
     /  \    /  \
   10   30  50  70
       /
      25
```
Level-order traversal: `40 20 60 10 30 50 70 -1 -1 25` (trailing `null`s omitted).

---

**Example 3**
**Input:**
```text
0 5
```

**Output:**
```text
5
```

**Explanation:**
The initial BST is empty ($N = 0$). Inserting $val = 5$ creates a single-node BST with root value $5$.

---

**Example 4**
**Input:**
```text
5 8
4 2 7 1 3
```

**Output:**
```text
4 2 7 1 3 -1 8
```

**Explanation:**
Starting at root $4$:
* $8 > 4 \implies$ move right to $7$.
* $8 > 7 \implies$ right child of $7$ is `null`. Attach $8$ as the right child of $7$.
Level-order traversal: `4 2 7 1 3 -1 8`.
