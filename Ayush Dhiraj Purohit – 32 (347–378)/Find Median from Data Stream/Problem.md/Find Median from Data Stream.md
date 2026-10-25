# Find Median from Data Stream

## Problem Statement

The **median** is the middle value in an ordered integer list. If the size of the list is even, there is no single middle value, and the median is defined as the mean (average) of the two middle values.

- For example, for `arr = [2, 3, 4]`, the median is `3.0`.
- For example, for `arr = [2, 3]`, the median is `(2 + 3) / 2.0 = 2.5`.

Implement the `MedianFinder` class:

- `MedianFinder()`: Initializes the `MedianFinder` object with an empty stream of data.
- `void addNum(int num)`: Adds the integer `num` from the data stream to the data structure.
- `double findMedian()`: Returns the median of all elements added to the data structure so far. Answers within $10^{-5}$ of the actual answer will be accepted.

---

## Input Format

- The first line contains an integer $Q$, denoting the total number of operations to execute.
- The next $Q$ lines each describe an operation in one of the following formats:
  - `addNum <num>`: Adds the integer `<num>` into the stream.
  - `findMedian`: Queries the current median of the stream.

---

## Output Format

- Print a single line containing $Q$ space-separated tokens corresponding to the results of each operation:
  - For `addNum`, output `null`.
  - For `findMedian`, output the calculated median formatted as a floating-point number with one decimal place (e.g., `1.5`, `2.0`, `-0.5`).

---

## Examples

### Example 1

**Input:**
```text
5
addNum 1
addNum 2
findMedian
addNum 3
findMedian
```

**Output:**
```text
null null 1.5 null 2.0
```

**Explanation:**
1. `addNum 1`: Stream is `[1]`. Outputs `null`.
2. `addNum 2`: Stream is `[1, 2]`. Outputs `null`.
3. `findMedian`: The current stream is `[1, 2]`. The count of elements is even ($2$). The two middle values are $1$ and $2$. The median is $(1 + 2) / 2.0 = 1.5$.
4. `addNum 3`: Stream is `[1, 2, 3]`. Outputs `null`.
5. `findMedian`: The current stream is `[1, 2, 3]`. The count of elements is odd ($3$). The middle value is $2$. The median is $2.0$.

---

### Example 2

**Input:**
```text
6
addNum -1
findMedian
addNum -2
findMedian
addNum -3
findMedian
```

**Output:**
```text
null -1.0 null -1.5 null -2.0
```

**Explanation:**
1. `addNum -1`: Stream is `[-1]`. Outputs `null`.
2. `findMedian`: Single element `[-1]`. Median is `-1.0`.
3. `addNum -2`: Stream is `[-2, -1]`. Outputs `null`.
4. `findMedian`: Middle values are $-2$ and $-1$. Median is $(-2 + -1) / 2.0 = -1.5$.
5. `addNum -3`: Stream is `[-3, -2, -1]`. Outputs `null`.
6. `findMedian`: Sorted stream is `[-3, -2, -1]`. Middle element is $-2.0$.

---

### Example 3

**Input:**
```text
7
addNum 10
addNum 20
addNum 30
findMedian
addNum 5
addNum 15
findMedian
```

**Output:**
```text
null null null 20.0 null null 15.0
```

**Explanation:**
1. After adding `10, 20, 30`, sorted stream is `[10, 20, 30]`. Median is `20.0`.
2. After adding `5, 15`, sorted stream is `[5, 10, 15, 20, 30]`. Median is `15.0`.

---

### Example 4

**Input:**
```text
4
addNum 1000000000
addNum -1000000000
findMedian
addNum 0
```

**Output:**
```text
null null 0.0 null
```

**Explanation:**
1. `addNum 1000000000`: Stream is `[1000000000]`.
2. `addNum -1000000000`: Stream is `[-1000000000, 1000000000]`.
3. `findMedian`: Median is $(1000000000 + (-1000000000)) / 2.0 = 0.0$.
4. `addNum 0`: Stream is `[-1000000000, 0, 1000000000]`. Outputs `null`.

---

## Constraints

- $1 \le Q \le 10^5$
- $-10^9 \le \text{num} \le 10^9$
- There will be at least one element in the data structure before each call to `findMedian()`.
- At most $10^5$ calls will be made to `addNum` and `findMedian` in total.
