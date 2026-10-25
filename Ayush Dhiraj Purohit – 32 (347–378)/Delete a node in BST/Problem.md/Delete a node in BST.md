### Problem: Delete a node in BST
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree

**Problem Statement**
Given the root node of a **Binary Search Tree (BST)** and an integer $key$, delete the node with the given $key$ in the BST such that the properties of the binary search tree are preserved. Return the root node of the modified BST.

A **Binary Search Tree** is defined as follows:
1. The **left subtree** of a node contains only nodes with values strictly less than the node's value.
2. The **right subtree** of a node contains only nodes with values strictly greater than the node's value.
3. Both the left and right subtrees must also be binary search trees.

The deletion can be broken down into two main phases:
1. **Search Phase:** Locate the node with value equal to $key$. If the key does not exist in the BST, the tree remains unchanged.
2. **Deletion Phase:** Remove the node while maintaining BST ordering. When removing a node with two children, replace its value with its **in-order successor** (the minimum value in its right subtree) and delete the successor from that subtree.

---

**Input Format**
* The first line contains two space-separated integers $N$ (the number of elements in the level-order serialization of the initial BST) and $key$ (the integer value of the node to be deleted).
* If $N > 0$, the second line contains $N$ space-separated integers representing the level-order traversal of the initial BST, where `-1` denotes a `null` node.
* If $N = 0$, the tree is initially empty and the second line is omitted.

**Output Format**
* Print space-separated integers representing the level-order traversal of the BST after deleting the node with value $key$, with `-1` denoting `null` nodes. Any trailing `-1` values should be omitted.
* If the resulting BST is empty, print an empty line.

---

**Constraints**
* $0 \le N \le 10^4$
* $0 \le \text{Node.val}, key \le 10^9$
* All values present in the initial BST are unique.
* The $key$ may or may not exist in the initial BST.
* `-1` represents a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
7 3
5 3 6 2 4 -1 7
```

**Output:**
```text
5 4 6 2 -1 -1 7
```

**Explanation:**
Initial BST:
```text
        5
       / \
      3   6
     / \   \
    2   4   7
```
We want to delete $key = 3$:
1. Locate node $3$ in the left subtree of root $5$.
2. Node $3$ has two children ($2$ and $4$).
3. Its in-order successor (the smallest node in the right subtree of $3$) is node $4$.
4. Replace the value of node $3$ with $4$, and remove the original node $4$.

Resulting BST:
```text
        5
       / \
      4   6
     /     \
    2       7
```
Level-order traversal: `5 4 6 2 -1 -1 7`.

---

**Example 2**
**Input:**
```text
7 0
5 3 6 2 4 -1 7
```

**Output:**
```text
5 3 6 2 4 -1 7
```

**Explanation:**
The value $0$ does not exist in the BST. The tree remains completely unchanged.

---

**Example 3**
**Input:**
```text
0 0
```

**Output:**
```text

```

**Explanation:**
The tree is initially empty ($N = 0$), so the result after deletion is an empty tree.

---

**Example 4**
**Input:**
```text
1 5
5
```

**Output:**
```text

```

**Explanation:**
The initial tree contains only a single node ($5$). Deleting $5$ leaves the tree completely empty.

---

**Example 5**
**Input:**
```text
3 2
2 1 3
```

**Output:**
```text
3 1
```

**Explanation:**
The root node $2$ is deleted. It has two children: $1$ and $3$.
The in-order successor is $3$.
Node $2$ takes the value $3$, and node $3$ is removed from the right subtree.
Resulting tree has root $3$ and left child $1$. Level-order: `3 1`.
