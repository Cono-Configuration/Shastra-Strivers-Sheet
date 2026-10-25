# Kth Largest Element in a Stream of Running Integers

## Problem Statement

Design a class to find the **$K^{\text{th}}$ largest element in a stream of running integers**. Note that it is the $K^{\text{th}}$ largest element in sorted order, not the $K^{\text{th}}$ distinct element. Duplicate values occupy distinct positions in the ordering.

Implement the `KthLargest` class:
- `KthLargest(int k, int[] nums)`: Initializes the object with the integer $K$ and the stream of initial integers `nums`.
- `int add(int val)`: Appends the integer `val` to the stream and returns the element representing the $K^{\text{th}}$ largest element in the stream so far.

---

## Input Format

- The first line contains two space-separated integers $K$ and $N$, denoting the target rank $K$ and the size of the initial array `nums`.
- The second line contains $N$ space-separated integers representing the initial elements of `nums`. If $N = 0$, this line is omitted or empty.
- The third line contains an integer $Q$, denoting the number of subsequent `add` operations.
- The fourth line contains $Q$ space-separated integers, representing the values to be added sequentially into the stream.

---

## Output Format

- Print a single line containing $Q$ space-separated integers, where the $i^{\text{th}}$ integer is the result returned by the $i^{\text{th}}$ call to `add(val)`.

---

## Examples

### Example 1

**Input:**
```text
3 4
4 5 8 2
5
3 5 10 9 4
```

**Output:**
```text
4 5 5 8 8
```

**Explanation:**
1. Initialize with $K = 3$ and `nums = [4, 5, 8, 2]`. The top 3 largest elements are `[8, 5, 4]`. The 3rd largest is `4`.
2. `add(3)`: Stream is `[4, 5, 8, 2, 3]`. Sorted descending: `[8, 5, 4, 3, 2]`. 3rd largest is `4`.
3. `add(5)`: Stream is `[4, 5, 8, 2, 3, 5]`. Sorted descending: `[8, 5, 5, 4, 3, 2]`. 3rd largest is `5`.
4. `add(10)`: Stream is `[4, 5, 8, 2, 3, 5, 10]`. Sorted descending: `[10, 8, 5, 5, 4, 3, 2]`. 3rd largest is `5`.
5. `add(9)`: Stream is `[4, 5, 8, 2, 3, 5, 10, 9]`. Sorted descending: `[10, 9, 8, 5, 5, 4, 3, 2]`. 3rd largest is `8`.
6. `add(4)`: Stream is `[4, 5, 8, 2, 3, 5, 10, 9, 4]`. Sorted descending: `[10, 9, 8, 5, 5, 4, 4, 3, 2]`. 3rd largest is `8`.

---

### Example 2

**Input:**
```text
1 0

4
5 10 2 12
```

**Output:**
```text
5 10 10 12
```

**Explanation:**
1. Initialize with $K = 1$ and empty initial array `nums = []`.
2. `add(5)`: Stream is `[5]`. 1st largest is `5`.
3. `add(10)`: Stream is `[5, 10]`. 1st largest is `10`.
4. `add(2)`: Stream is `[5, 10, 2]`. 1st largest is `10`.
5. `add(12)`: Stream is `[5, 10, 2, 12]`. 1st largest is `12`.

---

### Example 3

**Input:**
```text
2 2
0 0
3
-1 0 1
```

**Output:**
```text
0 0 0
```

**Explanation:**
- Initial: $K = 2$, `[0, 0]`. 2nd largest is `0`.
- `add(-1)`: `[-1, 0, 0]`. 2nd largest is `0`.
- `add(0)`: `[-1, 0, 0, 0]`. 2nd largest is `0`.
- `add(1)`: `[-1, 0, 0, 0, 1]`. 2nd largest is `0`.

---

## Constraints

- $1 \le K \le 10^5$
- $0 \le N \le 10^5$
- $1 \le Q \le 10^5$
- $N + Q \ge K$
- It is guaranteed that there are at least $K$ elements in the stream when `add` is invoked.
- $-10^9 \le \text{nums}[i], \text{val} \le 10^9$
