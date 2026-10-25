### Problem: Kth smallest and largest element in BST
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Depth-First Search

**Problem Statement**
Given the root of a **Binary Search Tree (BST)** and an integer $K$, find both the **$K^{\text{th}}$ smallest** and the **$K^{\text{th}}$ largest** elements present in the BST.

A **Binary Search Tree** is defined by the following invariants:
1. The **left subtree** of a node contains only nodes with values strictly less than the node's value.
2. The **right subtree** of a node contains only nodes with values strictly greater than the node's value.
3. Both the left and right subtrees are also binary search trees.

Indices are 1-based:
* The $1^{\text{st}}$ smallest element is the minimum element in the BST.
* The $1^{\text{st}}$ largest element is the maximum element in the BST.

It is guaranteed that $K$ is valid ($1 \le K \le \text{total number of nodes in the BST}$). All values in the BST are unique.

---

**Input Format**
* The first line contains two space-separated integers $N$ and $K$, where $N$ is the number of elements in the level-order serialization of the BST, and $K$ is the target rank.
* If $N > 0$, the second line contains $N$ space-separated integers representing the level-order traversal of the BST, where `-1` denotes a `null` node.
* If $N = 0$, the tree is empty and the second line is omitted.

**Output Format**
* Print two space-separated integers:
  $$\text{kth\_smallest} \quad \text{kth\_largest}$$
* If the tree has fewer than $K$ elements, output `-1 -1`.

---

**Constraints**
* $1 \le N \le 10^4$
* $1 \le K \le \text{number of nodes in the BST} \le 10^4$
* $0 \le \text{Node.val} \le 10^9$
* All values in the BST are unique.
* `-1` represents a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
5 1
3 1 4 -1 2
```

**Output:**
```text
1 4
```

**Explanation:**
The BST structure:
```text
        3
       / \
      1   4
       \
        2
```
In-order traversal (ascending): `[1, 2, 3, 4]`.
* Total nodes = $4$.
* For $K = 1$:
  * $1^{\text{st}}$ smallest element is $1$.
  * $1^{\text{st}}$ largest element is $4$.

Output: `1 4`.

---

**Example 2**
**Input:**
```text
8 3
5 3 6 2 -1 -1 -1 1
```

**Output:**
```text
3 3
```

**Explanation:**
The BST structure:
```text
          5
         / \
        3   6
       /
      2
     /
    1
```
In-order traversal (ascending): `[1, 2, 3, 5, 6]`.
* Total nodes = $5$.
* For $K = 3$:
  * $3^{\text{rd}}$ smallest element (from start): $3$.
  * $3^{\text{rd}}$ largest element (from end): $3$.

Output: `3 3`.

---

**Example 3**
**Input:**
```text
1 1
10
```

**Output:**
```text
10 10
```

**Explanation:**
The tree contains only one node with value $10$.
* $1^{\text{st}}$ smallest is $10$.
* $1^{\text{st}}$ largest is $10$.

---

**Example 4**
**Input:**
```text
7 2
40 20 60 10 30 50 70
```

**Output:**
```text
20 60
```

**Explanation:**
Sorted values: `[10, 20, 30, 40, 50, 60, 70]`.
* $2^{\text{nd}}$ smallest value is $20$.
* $2^{\text{nd}}$ largest value is $60$.
