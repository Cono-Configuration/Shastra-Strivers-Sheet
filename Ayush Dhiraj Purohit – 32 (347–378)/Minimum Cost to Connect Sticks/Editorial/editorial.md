# Editorial: Minimum Cost to Connect Sticks

## 1. Problem Overview

We are given an array `sticks` consisting of $N$ positive integers, representing the lengths of $N$ sticks. In each step, we can take any two sticks of lengths $x$ and $y$ and connect them into a single stick of length $x + y$, incurring a cost of $x + y$. We continue this process until only one stick remains. Our goal is to determine the minimum possible total cost to connect all sticks.

---

## 2. Mathematical Intuition & Connection to Huffman Coding

### The Structure of the Total Cost
Suppose we connect $N$ sticks into one stick using a binary merge tree. Every stick begins at a certain leaf in the tree:
- If a stick $i$ is at depth $d_i$ from the root (where depth represents the number of merge operations it participates in), its initial length contributes to the total cost exactly $d_i$ times.
- Therefore, the total cost can be written as:
  $$\text{Total Cost} = \sum_{i=1}^{N} \text{sticks}[i] \times d_i$$

### The Greedy Choice Property
To minimize the sum of products $\sum \text{sticks}[i] \times d_i$:
- The sticks with the **largest initial lengths** must have the **smallest depths $d_i$** (merged as few times as possible).
- The sticks with the **smallest initial lengths** can tolerate the **largest depths $d_i$** (merged earliest).

This is precisely the foundational concept behind **Huffman's Algorithm** and **Optimal Merge Patterns**:
At every single step, we must greedily select and merge the **two shortest available sticks**. The newly formed stick is then placed back into the pool of available sticks.

---

## 3. Algorithmic Approaches

### 3.1 Naive Approach
A naive approach would repeatedly sort the array or scan it linearly to find the two minimum sticks, combine them, and insert the result back:
- Finding the two minimums takes $\mathcal{O}(N)$ time.
- Repeating this for $N - 1$ merges leads to $\mathcal{O}(N^2)$ time.
- For $N = 10^5$, $\mathcal{O}(N^2) \approx 10^{10}$ operations, leading to Time Limit Exceeded (TLE).

### 3.2 Optimal Approach: Min-Heap (Priority Queue)
A **Min-Heap** provides:
- $\mathcal{O}(1)$ query for the minimum element.
- $\mathcal{O}(\log K)$ removal and insertion of elements.

#### Algorithm Steps:
1. **Base Case:** If $N \le 1$, no connection operations are possible or needed. Return $0$.
2. **Heap Construction:** Insert all $N$ stick lengths into a Min-Heap. Using bottom-up heapification, this takes $\mathcal{O}(N)$ time.
3. **Merge Loop:**
   - Initialize `totalCost = 0` (using a 64-bit `long` to avoid integer overflow).
   - While the heap contains more than $1$ element:
     - Extract the smallest element: `first = minHeap.poll()`.
     - Extract the second smallest element: `second = minHeap.poll()`.
     - Compute the merge cost: `cost = first + second`.
     - Add `cost` to `totalCost`.
     - Insert `cost` back into the heap: `minHeap.add(cost)`.
4. **Result:** Return `totalCost`.

---

## 4. Step-by-Step Dry Run

Consider Example 1: `sticks = [2, 4, 3]`, $N = 3$.

- **Initial Heap:** `[2, 3, 4]`, `totalCost = 0`.

### Iteration 1:
- Extract two minimums: `first = 2`, `second = 3`.
- `cost = 2 + 3 = 5`.
- `totalCost = 0 + 5 = 5`.
- Push $5$ back into heap: `[4, 5]`.

### Iteration 2:
- Extract two minimums: `first = 4`, `second = 5`.
- `cost = 4 + 5 = 9`.
- `totalCost = 5 + 9 = 14`.
- Push $9$ back into heap: `[9]`.

### Termination:
- Heap size is now $1$.
- Total minimum cost = $14$.

---

## 5. Edge Cases & Boundary Analysis

1. **$N = 1$ (Single Stick):**
   - No connections are needed; total cost is $0$.
2. **$N = 2$ (Two Sticks):**
   - Exactly one merge is performed; cost is `sticks[0] + sticks[1]`.
3. **All Sticks Identical:**
   - Example: `[5, 5, 5, 5]`.
   - First merge: $5 + 5 = 10$, sticks: `[5, 5, 10]`.
   - Second merge: $5 + 5 = 10$, sticks: `[10, 10]`.
   - Third merge: $10 + 10 = 20$, sticks: `[20]`.
   - Total cost = $10 + 10 + 20 = 40$.
4. **Integer Overflow Prevention:**
   - In the worst case, $N = 10^5$ and each stick length is $10^9$.
   - The total accumulated cost can easily exceed $2 \times 10^9$ (maximum value of a 32-bit signed integer).
   - Using 64-bit signed integers (`long` in Java) prevents arithmetic overflow.
5. **Maximum Scale Constraints ($N = 10^5$):**
   - Building the heap takes $\mathcal{O}(N)$ time.
   - Performing $N - 1$ merges with $\mathcal{O}(\log N)$ heap updates takes $\approx 10^5 \times 17 \approx 1.7 \times 10^6$ operations.
   - Executes in $< 20\text{ ms}$ in Java.

---

## 6. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N \log N)$
  - Building the min-heap takes $\mathcal{O}(N)$ time.
  - There are $N - 1$ iterations in the merge loop. In each iteration, two extract-min operations and one insert operation are performed, taking $\mathcal{O}(\log N)$ time.
  - Total time complexity is strictly $\mathcal{O}(N \log N)$.
- **Space Complexity:** $\mathcal{O}(N)$
  - The Min-Heap stores at most $N$ elements simultaneously.
