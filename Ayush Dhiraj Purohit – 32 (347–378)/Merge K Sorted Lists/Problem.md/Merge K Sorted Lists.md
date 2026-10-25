# Merge K Sorted Lists

## Problem Statement

You are given an array of $K$ singly linked lists `lists`, where each individual linked list is sorted in **non-decreasing order**.

Merge all the linked lists into a single sorted linked list and return its head.

---

## Input Format

- The first line contains an integer $K$, denoting the number of sorted linked lists ($K \ge 0$).
- The following $K$ lines describe each linked list:
  - Each line begins with an integer $M$ ($M \ge 0$), denoting the number of elements in the linked list.
  - If $M > 0$, it is followed by $M$ space-separated integers in non-decreasing order representing the elements of that linked list.
  - If $M = 0$, it represents an empty linked list.

---

## Output Format

- Print a single line containing space-separated integers representing the values of the merged sorted linked list in non-decreasing order.
- If the merged linked list is empty (contains $0$ nodes), print an empty line.

---

## Examples

### Example 1

**Input:**
```text
3
3 1 4 5
3 1 3 4
2 2 6
```

**Output:**
```text
1 1 2 3 4 4 5 6
```

**Explanation:**
The linked lists are:
- List 1: `1 -> 4 -> 5`
- List 2: `1 -> 3 -> 4`
- List 3: `2 -> 6`

Merging all nodes into one sorted list yields:
`1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6`

---

### Example 2

**Input:**
```text
0
```

**Output:**
```text

```

**Explanation:**
$K = 0$, meaning there are no lists to merge. The resulting merged linked list is empty (`null`).

---

### Example 3

**Input:**
```text
1
0
```

**Output:**
```text

```

**Explanation:**
$K = 1$, but the list itself contains $0$ elements (`[]`). The resulting merged linked list is empty (`null`).

---

### Example 4

**Input:**
```text
3
0
3 -5 0 7
0
```

**Output:**
```text
-5 0 7
```

**Explanation:**
Lists 1 and 3 are empty, and List 2 contains `-5 -> 0 -> 7`. Merging them yields `-5 0 7`.

---

## Constraints

- $0 \le K \le 10^4$
- $0 \le N \le 10^5$, where $N$ is the total number of nodes across all $K$ linked lists.
- $0 \le M_i \le 10^5$, where $M_i$ is the length of the $i$-th linked list.
- $\sum_{i=0}^{K-1} M_i = N \le 10^5$
- $-10^9 \le \text{node.val} \le 10^9$
- Each linked list is guaranteed to be sorted in **non-decreasing order**.
