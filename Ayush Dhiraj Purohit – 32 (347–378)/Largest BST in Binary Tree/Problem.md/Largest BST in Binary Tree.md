# Largest BST in Binary Tree

## Problem Statement

Given the `root` of a Binary Tree, where each node contains an integer value, return the size (the total number of nodes) of the **largest subtree** of the binary tree which is also a valid **Binary Search Tree (BST)**.

A **subtree** of a tree $T$ is a tree consisting of a node in $T$ and all of its descendants in $T$.

A **Binary Search Tree (BST)** is defined as follows:
- The left subtree of a node contains only nodes with keys **strictly less** than the node's key.
- The right subtree of a node contains only nodes with keys **strictly greater** than the node's key.
- Both the left and right subtrees must also be binary search trees.

---

## Input Format

1. The first line contains an integer $N$ representing the number of elements in the serialized level-order traversal of the binary tree.
2. The second line contains $N$ space-separated integers representing the binary tree in level-order traversal:
   - Positive integers represent valid node values.
   - `-1` represents a `null` node (empty child). Trailing `null` values are omitted.

---

## Output Format

- Output a single integer: the size (number of nodes) of the largest subtree that forms a valid Binary Search Tree.

---

## Examples

### Example 1

```text
       2
      / \
     1   3
```

**Input:**
```text
3
2 1 3
```

**Output:**
```text
3
```

**Explanation:**
The entire binary tree is a valid BST with 3 nodes ($1 < 2 < 3$). Thus, the size of the largest BST is $3$.

---

### Example 2

```text
    10
      \
       20
         \
          30
            \
             40
               \
                50
```

**Input:**
```text
9
10 -1 20 -1 30 -1 40 -1 50
```

**Output:**
```text
5
```

**Explanation:**
The tree is a right-skewed tree where each parent is strictly smaller than its right child ($10 < 20 < 30 < 40 < 50$). The entire tree forms a valid BST of size $5$.

---

### Example 3

```text
        3
       / \
      1   4
         /
        2
```

**Input:**
```text
6
3 1 4 -1 -1 2
```

**Output:**
```text
2
```

**Explanation:**
- The subtree rooted at node $4$ has left child $2$ ($2 < 4$), which is a valid BST of size $2$.
- The subtree rooted at node $1$ is a valid BST of size $1$.
- The entire tree rooted at $3$ is **not** a valid BST because the node with value $2$ resides in its right subtree, violating the BST property ($2 < 3$).
- The maximum size among all valid BST subtrees is $\max(2, 1) = 2$.

---

### Example 4

```text
              50
            /    \
          30      60
         /  \    /  \
        5   20  45   70
                    /  \
                   65   80
```

**Input:**
```text
15
50 30 60 5 20 45 70 -1 -1 -1 -1 -1 -1 65 80
```

**Output:**
```text
5
```

**Explanation:**
- Subtree at node $30$: right child is $20 < 30$, which is invalid.
- Subtree at node $60$: left child is $45 < 60$, right child is $70 > 60$. At node $70$, left child is $65 < 70$, right child is $80 > 70$. All keys in left subtree of $60$ are $< 60$, and all keys in right subtree are $> 60$. Thus, subtree rooted at $60$ is a valid BST of size $5$.
- Subtree at node $50$: left subtree is not a BST, so the tree rooted at $50$ cannot be a valid BST.
- The largest BST subtree has size $5$.

---

## Constraints

- $1 \le \text{Number of Nodes} \le 10^4$
- $1 \le \text{Node.val} \le 10^5$
- Node values in the tree are positive integers ($-1$ is strictly reserved for `null` placeholders in the level-order serialization).
