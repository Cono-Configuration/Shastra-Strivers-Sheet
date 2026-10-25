### Problem: BST Iterator
**Difficulty:** Medium | **Tags:** Tree, Binary Search Tree, Binary Tree, Stack, Design, Iterator

**Problem Statement**
Implement the `BSTIterator` class that represents an iterator over the **in-order traversal** of a **Binary Search Tree (BST)**:

* `BSTIterator(TreeNode root)`: Initializes an object of the `BSTIterator` class. The root of the BST is given as part of the constructor. The pointer should be initialized to a non-existent number smaller than any element in the BST.
* `boolean hasNext()`: Returns `true` if there exists a number in the traversal to the right of the pointer, otherwise returns `false`.
* `int next()`: Moves the pointer to the right, then returns the number at the pointer.

Notice that by initializing the pointer to a non-existent smallest number, the first call to `next()` will return the smallest element in the BST.

You may assume that `next()` calls will always be valid, that is, there will be at least a next number in the in-order traversal when `next()` is called.

**Follow up:**
Could you implement `next()` and `hasNext()` to run in average $\mathcal{O}(1)$ time and use $\mathcal{O}(H)$ memory, where $H$ is the height of the tree?

---

**Input Format**
* The first line contains an integer $N$ representing the number of elements in the level-order serialization of the BST.
* The second line contains $N$ space-separated integers representing the level-order traversal of the BST, where `-1` denotes a `null` node. Trailing `null` pointers are omitted.
* The third line contains an integer $Q$ representing the number of iterator operations to be executed.
* The fourth line contains $Q$ space-separated strings, each being either `"next"` or `"hasNext"`.

**Output Format**
* Print a single line containing $Q$ space-separated tokens corresponding to the results of each query in sequence:
  * For a `"next"` query, output the integer value returned.
  * For a `"hasNext"` query, output `"true"` or `"false"`.

---

**Constraints**
* $1 \le \text{number of nodes in the BST} \le 10^4$
* $1 \le N \le 2 \times 10^4$
* $0 \le \text{Node.val} \le 10^9$
* All values in the BST are unique.
* $1 \le Q \le 2 \times 10^4$
* At most $N$ calls will be made to `next()`.
* Calls to `next()` are guaranteed to be valid (`hasNext()` is `true`).
* `-1` denotes a `null` node in the level-order traversal.

---

**Example 1**
**Input:**
```text
7
7 3 15 -1 -1 9 20
9
next next hasNext next hasNext next hasNext next hasNext
```

**Output:**
```text
3 7 true 9 true 15 true 20 false
```

**Explanation:**
The given BST is:
```text
        7
       / \
      3   15
         /  \
        9   20
```
- In-order traversal: `[3, 7, 9, 15, 20]`.
- Operations executed:
  1. `next()` $\rightarrow 3$
  2. `next()` $\rightarrow 7$
  3. `hasNext()` $\rightarrow \text{true}$ (elements 9, 15, 20 remain)
  4. `next()` $\rightarrow 9$
  5. `hasNext()` $\rightarrow \text{true}$ (elements 15, 20 remain)
  6. `next()` $\rightarrow 15$
  7. `hasNext()` $\rightarrow \text{true}$ (element 20 remains)
  8. `next()` $\rightarrow 20$
  9. `hasNext()` $\rightarrow \text{false}$ (all elements have been traversed)
- Final output: `3 7 true 9 true 15 true 20 false`.

---

**Example 2**
**Input:**
```text
3
2 1 3
5
hasNext next next next hasNext
```

**Output:**
```text
true 1 2 3 false
```

**Explanation:**
The given BST is:
```text
    2
   / \
  1   3
```
- In-order traversal: `[1, 2, 3]`.
- Operations:
  1. `hasNext()` $\rightarrow \text{true}$
  2. `next()` $\rightarrow 1$
  3. `next()` $\rightarrow 2$
  4. `next()` $\rightarrow 3$
  5. `hasNext()` $\rightarrow \text{false}$
- Final output: `true 1 2 3 false`.

---

**Example 3**
**Input:**
```text
1
42
3
hasNext next hasNext
```

**Output:**
```text
true 42 false
```

**Explanation:**
Single node BST with value `42`.
1. `hasNext()` returns `true`.
2. `next()` returns `42`.
3. `hasNext()` returns `false`.
