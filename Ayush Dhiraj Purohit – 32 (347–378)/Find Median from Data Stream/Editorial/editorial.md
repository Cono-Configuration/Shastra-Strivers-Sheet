# Editorial: Find Median from Data Stream

## 1. Problem Summary

We need to design a dynamic data structure, `MedianFinder`, that supports two continuous operations over a stream of incoming integers:
1. `addNum(int num)`: Inserts an integer `num` into the data structure.
2. `findMedian()`: Computes and returns the median of all elements inserted so far.

The **median** is defined as:
- If the total count of elements $N$ is **odd**, the median is the single middle element when the sequence is ordered.
- If the total count of elements $N$ is **even**, the median is the arithmetic mean of the two middle elements:
$$\text{median} = \frac{\text{mid}_1 + \text{mid}_2}{2.0}$$

---

## 2. Approach Analysis

### Approach 1: Brute Force (Append and Sort)

- **Mechanism:**
  - `addNum(num)`: Append `num` to a dynamic array or list in $\mathcal{O}(1)$ time.
  - `findMedian()`: Sort the entire list in $\mathcal{O}(N \log N)$ time, then retrieve the element at index $N / 2$ (or the average of indices $N / 2 - 1$ and $N / 2$).
- **Complexity:**
  - Insertion: $\mathcal{O}(1)$
  - Query: $\mathcal{O}(N \log N)$
  - Over $Q$ operations with up to $N$ queries: Total time is $\mathcal{O}(Q \cdot N \log N)$.
- **Why it Fails:** With $Q = 10^5$, sorting repeatedly incurs $\approx 10^5 \times 10^5 \log_2(10^5) > 10^{10}$ operations, resulting in a severe Time Limit Exceeded (TLE).

---

### Approach 2: Better (Maintained Sorted Order via Insertion Sort / Binary Search)

- **Mechanism:**
  - Maintain the list in sorted order at all times.
  - `addNum(num)`: Use binary search (`lower_bound` / `Collections.binarySearch`) to find the insertion index in $\mathcal{O}(\log N)$, then shift subsequent elements rightward to insert `num` in $\mathcal{O}(N)$ time.
  - `findMedian()`: Direct index lookup in $\mathcal{O}(1)$ time.
- **Complexity:**
  - Insertion: $\mathcal{O}(N)$ (due to array shift operations)
  - Query: $\mathcal{O}(1)$
  - Over $Q$ operations: Total time is $\mathcal{O}(Q \cdot N) \approx 10^{10}$ operations.
- **Why it Fails:** Array copying and shifting across $10^5$ insertions is still too slow for strict execution time limits.

---

### Approach 3: Optimal (Two Heaps — Max-Heap & Min-Heap)

#### Core Intuition

If we observe the definition of the median, it splits any sorted dataset into two halves:
1. **Lower Half (Smaller Values):** Contains all elements $\le \text{median}$.
2. **Upper Half (Larger Values):** Contains all elements $\ge \text{median}$.

Notice that we **do not need the full ordering** of all elements. We only need:
- The **largest** element of the lower half.
- The **smallest** element of the upper half.

This insight points directly to priority queues:
- A **Max-Heap (`maxHeap`)** to store the lower half. The root gives the maximum of the lower half.
- A **Min-Heap (`minHeap`)** to store the upper half. The root gives the minimum of the upper half.

```
       Lower Half (maxHeap)                  Upper Half (minHeap)
     [ ... , maxHeap.peek() ]  <=  [ minHeap.peek() , ... ]
                ^                               ^
         Max-Heap Root                   Min-Heap Root
                \                               /
                 \-----> [ MEDIAN ] <----------/
```

#### Structural Invariants

To guarantee that the heap roots always correspond to the middle elements, the two heaps must maintain two strict invariants at all times:

1. **Order Invariant:**
   Every element in `maxHeap` must be less than or equal to every element in `minHeap`:
   $$\max(\text{maxHeap}) \le \min(\text{minHeap})$$

2. **Size Invariant:**
   The lower half is chosen to hold the extra element when the total count $N$ is odd:
   $$0 \le |\text{maxHeap}| - |\text{minHeap}| \le 1$$

---

## 3. Algorithm Step-by-Step

### 1. Adding a Number: `addNum(int num)`

To maintain both invariants effortlessly without deep nested conditions:
1. Insert `num` into `maxHeap`.
2. Extract the maximum element from `maxHeap` and push it into `minHeap`. This guarantees that the order invariant holds: any element belonging to the upper half lands in `minHeap`.
3. If `minHeap.size() > maxHeap.size()`, extract the minimum element from `minHeap` and push it into `maxHeap`. This restores the size invariant.

```java
public void addNum(int num) {
    maxHeap.offer(num);
    minHeap.offer(maxHeap.poll());

    if (minHeap.size() > maxHeap.size()) {
        maxHeap.offer(minHeap.poll());
    }
}
```

Each step takes logarithmic time $\mathcal{O}(\log N)$.

### 2. Querying the Median: `findMedian()`

With both invariants maintained:
- **Odd total count** ($|\text{maxHeap}| > |\text{minHeap}|$):
  The median is simply the root of `maxHeap`:
  $$\text{median} = \text{maxHeap.peek()}$$
- **Even total count** ($|\text{maxHeap}| == |\text{minHeap}|$):
  The median is the average of the two roots:
  $$\text{median} = \frac{(\text{long})\text{maxHeap.peek()} + \text{minHeap.peek()}}{2.0}$$

```java
public double findMedian() {
    if (maxHeap.size() > minHeap.size()) {
        return (double) maxHeap.peek();
    }
    return ((long) maxHeap.peek() + minHeap.peek()) / 2.0;
}
```

This runs in constant time $\mathcal{O}(1)$.

---

## 4. Dry Run Example

Consider the stream of operations:
`addNum(1)`, `addNum(2)`, `findMedian()`, `addNum(3)`, `findMedian()`

| Step | Operation | Action on Heaps | `maxHeap` (Lower Half) | `minHeap` (Upper Half) | Median Returned |
|:---:|:---|:---|:---:|:---:|:---:|
| 1 | `addNum(1)` | Push 1 to max; move 1 to min; min > max $\implies$ move 1 to max | `[1]` (size 1) | `[]` (size 0) | — |
| 2 | `addNum(2)` | Push 2 to max $\implies$ poll 2 to min; sizes balanced | `[1]` (size 1) | `[2]` (size 1) | — |
| 3 | `findMedian()` | Sizes equal ($1 == 1$) $\implies (1 + 2) / 2.0$ | `[1]` | `[2]` | **`1.5`** |
| 4 | `addNum(3)` | Push 3 to max $\implies$ poll 3 to min; min size (2) > max size (1) $\implies$ poll 2 to max | `[2, 1]` (size 2) | `[3]` (size 1) | — |
| 5 | `findMedian()` | max size (2) > min size (1) $\implies$ root of max | `[2, 1]` | `[3]` | **`2.0`** |

---

## 5. Edge-Case Analysis

1. **Single Element ($N = 1$):**
   - The element resides in `maxHeap`. `findMedian()` returns `maxHeap.peek().0`.
2. **Even vs. Odd Transitions:**
   - The size invariant toggles between $|\text{maxHeap}| = |\text{minHeap}| + 1$ (odd) and $|\text{maxHeap}| = |\text{minHeap}|$ (even).
3. **Identical / Duplicate Elements:**
   - Heaps naturally accommodate duplicates without degradation; elements of equal value are divided evenly across both heaps.
4. **All Negative Elements:**
   - E.g. `[-3, -2, -1]`. `maxHeap` contains $[-2, -3]$, `minHeap` contains $[-1]$. Median is correctly `-2.0`.
5. **Integer Overflow During Addition:**
   - For values near $10^9$, computing `maxHeap.peek() + minHeap.peek()` can reach $2 \times 10^9$ or $-2 \times 10^9$. Explicitly casting to `(long)` prevents 32-bit signed integer overflow.
6. **Zero Crossing & Floating-Point `-0.0`:**
   - When roots are $-1$ and $1$, the sum is $0$. Ensure `-0.0` is normalized to `0.0` to preserve clean output.

---

## 6. Complexity Analysis

| Operation | Time Complexity | Space Complexity |
|---|:---:|:---:|
| `MedianFinder()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ |
| `addNum(int num)` | $\mathcal{O}(\log N)$ | $\mathcal{O}(1)$ auxiliary |
| `findMedian()` | $\mathcal{O}(1)$ | $\mathcal{O}(1)$ auxiliary |
| **Total ($Q$ Operations)** | $\mathcal{O}(Q \log N)$ | $\mathcal{O}(N)$ total space |

- **Time Complexity:** Each `addNum` performs at most three logarithmic heap operations, resulting in $\mathcal{O}(\log N)$ per addition. Each `findMedian` accesses only the heap roots, taking $\mathcal{O}(1)$. For $Q = 10^5$, total operations $\approx 3 \times 10^5 \log_2(10^5) \approx 5 \times 10^6$, executing in $< 0.15$ seconds in Java.
- **Space Complexity:** The two heaps collectively store exactly $N$ integers, where $N \le Q$ is the total count of numbers added. Thus, space complexity is strictly $\mathcal{O}(N)$.
