# Sort a K-Sorted Array

## Problem Statement

Given an array of integers `nums` of size $N$ and an integer $K$, where each element is at most **$K$ positions away** from its target position in the sorted array, sort the array in **non-decreasing order**.

An array is said to be **$K$-sorted** (or nearly sorted) if for every element at index $i$ in `nums`, its correct sorted position index $j$ satisfies:
$$|i - j| \le K$$

Design an efficient algorithm that takes advantage of this bounded displacement property to achieve an $\mathcal{O}(N \log K)$ runtime.

---

## Input Format

- The first line contains two space-separated integers $N$ and $K$, denoting the size of the array and the maximum displacement $K$.
- The second line contains $N$ space-separated integers representing the elements of `nums`.

---

## Output Format

- Print a single line containing $N$ space-separated integers representing the sorted array in non-decreasing order.

---

## Examples

### Example 1

**Input:**
```text
7 3
6 5 3 2 8 10 9
```

**Output:**
```text
2 3 5 6 8 9 10
```

**Explanation:**
- The target sorted array is `[2, 3, 5, 6, 8, 9, 10]`.
- Check displacements:
  - Element `6`: index 0 in input $\rightarrow$ index 3 in sorted ($|0 - 3| = 3 \le 3$)
  - Element `5`: index 1 in input $\rightarrow$ index 2 in sorted ($|1 - 2| = 1 \le 3$)
  - Element `3`: index 2 in input $\rightarrow$ index 1 in sorted ($|2 - 1| = 1 \le 3$)
  - Element `2`: index 3 in input $\rightarrow$ index 0 in sorted ($|3 - 0| = 3 \le 3$)
  - Element `8`: index 4 in input $\rightarrow$ index 4 in sorted ($|4 - 4| = 0 \le 3$)
  - Element `10`: index 5 in input $\rightarrow$ index 6 in sorted ($|5 - 6| = 1 \le 3$)
  - Element `9`: index 6 in input $\rightarrow$ index 5 in sorted ($|6 - 5| = 1 \le 3$)
Every element is within $K = 3$ positions of its sorted location.

---

### Example 2

**Input:**
```text
6 2
3 2 1 5 6 4
```

**Output:**
```text
1 2 3 4 5 6
```

**Explanation:**
- Each element is displaced by at most $K = 2$ positions.
- Sorting the array yields `[1, 2, 3, 4, 5, 6]`.

---

### Example 3

**Input:**
```text
5 0
1 2 3 4 5
```

**Output:**
```text
1 2 3 4 5
```

**Explanation:**
- $K = 0$ implies each element is at most $0$ positions away from its sorted index. The array is already sorted.

---

## Constraints

- $1 \le N \le 10^5$
- $0 \le K \le N - 1$
- $-10^9 \le \text{nums}[i] \le 10^9$
- Every element in `nums` is guaranteed to be at most $K$ positions away from its target sorted position.
