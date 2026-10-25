### Problem: Inorder Successor and Predecessor in BST
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Binary Search

**Problem Statement**
You are given the root node of a **Binary Search Tree (BST)** and an integer $key$. It is guaranteed that $key$ exists in the BST.

Your task is to find the **Inorder Predecessor** and **Inorder Successor** of $key$ in the given BST:
* **Inorder Predecessor:** The node with the **largest value** in the BST that is **strictly less** than $key$. If no such node exists, its value is defined as `-1`.
* **Inorder Successor:** The node with the **smallest value** in the BST that is **strictly greater** than $key$. If no such node exists, its value is defined as `-1`.

A **Binary Search Tree** is a binary tree where for every node:
1. All nodes in its **left subtree** have values strictly less than the node's value.
2. All nodes in its **right subtree** have values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

An **inorder traversal** visits nodes in ascending sorted order:
$$\text{Left Subtree} \longrightarrow \text{Root} \longrightarrow \text{Right Subtree}$$
Thus, the predecessor of $key$ is the element immediately preceding $key$ in the sorted inorder sequence, and the successor is the element immediately following $key$.

---

**Input Format**
* The first line contains two space-separated integers $N$ and $key$:
  * $N$ represents the number of elements in the level-order serialization of the BST.
  * $key$ represents the target value whose inorder predecessor and successor are to be determined.
* The second line contains $N$ space-separated integers representing the level-order traversal of the BST, where `-1` denotes a `null` node. Trailing `null` pointers are omitted.

**Output Format**
* Print two space-separated integers representing the **Inorder Predecessor** and **Inorder Successor** of $key$, respectively.
* If either the predecessor or successor does not exist in the BST, print `-1` in its place.

---

**Constraints**
* $1 \le \text{number of nodes in the BST} \le 10^4$
* $1 \le N \le 2 \times 10^4$
* $0 \le \text{Node.val}, key \le 10^9$
* All node values in the BST are unique.
* $key$ is guaranteed to exist in the BST.
* `-1` denotes a `null` node in the level-order traversal and indicates the absence of a predecessor or successor.

---

**Example 1**
**Input:**
```text
7 10
5 2 10 1 4 7 12
```

**Output:**
```text
7 12
```

**Explanation:**
The given BST is:
```text
        5
       / \
      2   10
     / \  / \
    1  4 7  12
```
- Inorder traversal of the BST (strictly ascending): `[1, 2, 4, 5, 7, 10, 12]`.
- For $key = 10$:
  - The value immediately preceding $10$ in the sorted order is $7$ (Predecessor).
  - The value immediately following $10$ in the sorted order is $12$ (Successor).
- Output: `7 12`.

---

**Example 2**
**Input:**
```text
7 12
5 2 10 1 4 7 12
```

**Output:**
```text
10 -1
```

**Explanation:**
Using the same BST from Example 1:
- Inorder traversal: `[1, 2, 4, 5, 7, 10, 12]`.
- For $key = 12$:
  - The largest value strictly smaller than $12$ is $10$ (Predecessor).
  - Since $12$ is the maximum element in the BST, there is no element strictly greater than $12$. Hence, the Successor is `-1`.
- Output: `10 -1`.

---

**Example 3**
**Input:**
```text
7 1
5 2 10 1 4 7 12
```

**Output:**
```text
-1 2
```

**Explanation:**
Using the same BST from Example 1:
- Inorder traversal: `[1, 2, 4, 5, 7, 10, 12]`.
- For $key = 1$:
  - Since $1$ is the minimum element in the BST, there is no element strictly smaller than $1$. Hence, the Predecessor is `-1`.
  - The smallest value strictly greater than $1$ is $2$ (Successor).
- Output: `-1 2`.

---

**Example 4**
**Input:**
```text
1 1
1
```

**Output:**
```text
-1 -1
```

**Explanation:**
The tree contains only a single node:
```text
1
```
- There is neither any node smaller than $1$ nor any node larger than $1$.
- Both Predecessor and Successor are `-1`.
- Output: `-1 -1`.
