# Editorial: Replace Elements by Their Rank

## 1. Problem Overview

We are given an array `arr` of $N$ integers. We need to replace each element with its $1$-based **rank** based on its value relative to all other elements:
1. Ranks start from $1$ for the smallest unique value.
2. The smaller the value, the smaller the assigned rank.
3. Identical elements must receive the exact same rank.
4. The output must preserve the original order of the elements.

---

## 2. Brute-Force Approach

### Intuition
For every element `arr[i]`, we could determine its rank by finding the number of distinct elements strictly smaller than `arr[i]`. Its rank would then be:
$$\text{rank}(arr[i]) = 1 + |\{x \in \text{distinct}(arr) : x < arr[i]\}|$$

To do this for each of the $N$ elements:
1. Maintain a set or iterate over all elements to count unique values strictly less than `arr[i]`.
2. Assign that count $+ 1$ as the rank.

### Complexity Analysis
- **Time Complexity:** $\mathcal{O}(N^2)$ due to nested scanning across all elements.
- **Space Complexity:** $\mathcal{O}(N)$ to store intermediate unique sets or the resulting array.
- **Verdict:** For $N = 10^5$, $\mathcal{O}(N^2) \approx 10^{10}$ operations, which exceeds standard 1-second time limits and leads to Time Limit Exceeded (TLE).

---

## 3. Approach 2: Sorting + Hash Map (Coordinate Compression)

### Intuition
If the array were sorted, assigning ranks would be trivial:
1. Copy `arr` into an auxiliary array `temp` and sort it in ascending order.
2. Iterate through the sorted `temp` array. Use a hash map `rankMap` where each unique number encountered is mapped to an incrementing rank counter:
   - If `temp[i]` is not in `rankMap`, insert `rankMap[temp[i]] = rank++`.
   - If `temp[i]` is already in `rankMap`, skip it (duplicate value).
3. Reconstruct the output array by replacing each original `arr[i]` with `rankMap[arr[i]]`.

### Complexity Analysis
- **Time Complexity:** $\mathcal{O}(N \log N)$ to sort `temp`, plus $\mathcal{O}(N)$ average time for map lookups and insertions.
- **Space Complexity:** $\mathcal{O}(N)$ auxiliary space for `temp` and `rankMap`.

---

## 4. Approach 3: Optimal Min-Heap / Priority Queue

### Intuition
Since this problem is centrally featured in heap-based data structure patterns, we can solve it elegantly using a **Min-Heap (Priority Queue)**:
- Store every element alongside its original array index as a pair: `(arr[i], i)`.
- A Min-Heap naturally extracts elements in strictly non-decreasing order of their values.
- As we extract each element:
  - If the extracted value is strictly greater than the previously extracted value, we increment our `rank` counter.
  - If the value equals the previous value, the rank remains unchanged.
  - We immediately write the current `rank` directly into the result array at index `originalIndex`:
    $$\text{result}[originalIndex] = rank$$

### Algorithm Steps

1. **Heap Initialization:**
   - Initialize two parallel arrays `heapVal` and `heapIdx` of size $N$.
   - Populate `heapVal[i] = arr[i]` and `heapIdx[i] = i` for all $0 \le i < N$.
   - Transform the array into a valid min-heap in $\mathcal{O}(N)$ time using bottom-up sift-down operations starting from index $\lfloor N / 2 \rfloor - 1$ down to $0$.

2. **Extraction and Rank Assignment:**
   - Maintain `rank = 0` and `prevVal = Long.MIN_VALUE`.
   - While the min-heap is not empty:
     - Extract the root element: `val = heapVal[0]`, `idx = heapIdx[0]`.
     - If `val != prevVal`:
       - Increment `rank++`.
       - Update `prevVal = val`.
     - Assign `result[idx] = rank`.
     - Replace the root with the last element of the heap, decrement heap size, and call `siftDown(0)`.

3. **Return Result:**
   - Output the populated `result` array.

---

## 5. Dry Run Walkthrough

Consider Example 1: `arr = [20, 15, 26, 2, 98, 6]`, $N = 6$.

### Initial Pairs:
- `(20, 0), (15, 1), (26, 2), (2, 3), (98, 4), (6, 5)`

### After Building Min-Heap ($\mathcal{O}(N)$):
Root holds the minimum element `(2, 3)`.

### Extraction Steps:
1. **Pop `(2, 3)`:**
   - `val = 2 != prevVal (null)`. `rank` becomes `1`.
   - `result[3] = 1`, `prevVal = 2`.
2. **Pop `(6, 5)`:**
   - `val = 6 != 2`. `rank` becomes `2`.
   - `result[5] = 2`, `prevVal = 6`.
3. **Pop `(15, 1)`:**
   - `val = 15 != 6`. `rank` becomes `3`.
   - `result[1] = 3`, `prevVal = 15`.
4. **Pop `(20, 0)`:**
   - `val = 20 != 15`. `rank` becomes `4`.
   - `result[0] = 4`, `prevVal = 20`.
5. **Pop `(26, 2)`:**
   - `val = 26 != 20`. `rank` becomes `5`.
   - `result[2] = 5`, `prevVal = 26`.
6. **Pop `(98, 4)`:**
   - `val = 98 != 26`. `rank` becomes `6`.
   - `result[4] = 6`, `prevVal = 98`.

### Final `result` Array:
`result = [4, 3, 5, 1, 6, 2]`

Matches Example 1 output exactly.

---

## 6. Edge Cases & Boundary Analysis

1. **$N = 1$ (Single Element):**
   - The single element is extracted, receives rank $1$, and is returned immediately.
2. **All Elements Identical:**
   - Example: `[100, 100, 100, 100]`.
   - The first element popped receives rank $1$. Every subsequent element matches `prevVal`, so `rank` remains $1$. All entries in `result` are $1$.
3. **Array Already Sorted (Ascending):**
   - Elements are extracted in order from $0$ to $N - 1$. Ranks assigned are $1, 2, \dots, N$.
4. **Array Sorted in Reverse (Descending):**
   - Min-heap extracts elements from right to left; ranks assigned are $N, N - 1, \dots, 1$.
5. **Negative Numbers & Zero:**
   - Standard 32-bit signed comparisons correctly handle negative values (e.g. $-10^9 < 0 < 10^9$).
6. **Maximum Constraint Scale ($N = 10^5$):**
   - $N \log_2 N \approx 10^5 \times 17 \approx 1.7 \times 10^6$ operations.
   - In Java, using primitive flat arrays for the heap executes in $\approx 15\text{ ms}$, comfortably within standard time limits.

---

## 7. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N \log N)$
  - Building the min-heap takes $\mathcal{O}(N)$ time.
  - Each of the $N$ extractions from the min-heap takes $\mathcal{O}(\log N)$ time.
  - Overall time complexity is strictly $\mathcal{O}(N \log N)$.
- **Space Complexity:** $\mathcal{O}(N)$
  - Storing the heap and the result array requires $\mathcal{O}(N)$ auxiliary memory.
