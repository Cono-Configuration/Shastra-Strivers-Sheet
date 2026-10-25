# Minimum Cost to Connect Sticks

## Problem Statement

You have some number of sticks with positive integer lengths. These lengths are given in the array `sticks`, where `sticks[i]` represents the length of the $i$-th stick.

You can connect any two sticks of lengths $x$ and $y$ into a single stick by paying a cost of $x + y$. You must continue connecting sticks until there is only **one stick remaining**.

Return the **minimum cost** of connecting all the given sticks into one stick.

---

## Input Format

- The first line contains an integer $N$, denoting the number of sticks in the array `sticks`.
- The second line contains $N$ space-separated integers representing the lengths of the `sticks`.

---

## Output Format

- Print a single integer representing the minimum cost needed to connect all sticks into one stick. If $N \le 1$, output `0`.

---

## Examples

### Example 1

**Input:**
```text
3
2 4 3
```

**Output:**
```text
14
```

**Explanation:**
1. Connect sticks `2` and `3` for a cost of $2 + 3 = 5$. Remaining sticks: `[4, 5]`.
2. Connect sticks `4` and `5` for a cost of $4 + 5 = 9$. Remaining sticks: `[9]`.
Total cost = $5 + 9 = 14$.

---

### Example 2

**Input:**
```text
4
1 8 3 5
```

**Output:**
```text
30
```

**Explanation:**
1. Connect sticks `1` and `3` for a cost of $1 + 3 = 4$. Remaining sticks: `[4, 5, 8]`.
2. Connect sticks `4` and `5` for a cost of $4 + 5 = 9$. Remaining sticks: `[9, 8]`.
3. Connect sticks `8` and `9` for a cost of $8 + 9 = 17$. Remaining sticks: `[17]`.
Total cost = $4 + 9 + 17 = 30$.

---

### Example 3

**Input:**
```text
1
5
```

**Output:**
```text
0
```

**Explanation:**
There is only one stick. No connections are needed, so the total cost is `0`.

---

### Example 4

**Input:**
```text
6
4 3 2 6 1 5
```

**Output:**
```text
51
```

**Explanation:**
Connecting the two smallest sticks greedily:
1. Connect $1$ and $2 \rightarrow$ cost $3$, sticks: `[3, 3, 4, 5, 6]`
2. Connect $3$ and $3 \rightarrow$ cost $6$, sticks: `[4, 5, 6, 6]`
3. Connect $4$ and $5 \rightarrow$ cost $9$, sticks: `[6, 6, 9]`
4. Connect $6$ and $6 \rightarrow$ cost $12$, sticks: `[9, 12]`
5. Connect $9$ and $12 \rightarrow$ cost $21$, sticks: `[21]`
Total cost = $3 + 6 + 9 + 12 + 21 = 51$.

---

## Constraints

- $1 \le N \le 10^5$, where $N$ is the number of sticks.
- $1 \le \text{sticks}[i] \le 10^9$
