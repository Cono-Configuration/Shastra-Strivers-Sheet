# Editorial: Top K Frequent Elements

## 1. Problem Summary

Given an integer array `nums` of size $N$ and an integer $K$, we are required to find and return the **$K$ most frequent elements**. 

The elements must be returned in **descending order of their frequencies**. If two elements share the same frequency, ties must be broken in **ascending numerical order** (smaller value first).

---

## 2. Approach Analysis

### Approach 1: Brute Force (Frequency Map + Full Sorting)

- **Mechanism:**
  1. Traverse the array and count the frequency of each unique element using a hash map (`HashMap<Integer, Integer>`).
  2. Collect all unique elements into a list.
  3. Sort the list using a custom comparator:
     - Compare frequencies in descending order: `freq2 - freq1`.
     - If frequencies are equal, compare values in ascending order: `val1 - val2`.
  4. Extract the first $K$ elements.
- **Complexity:**
  - Counting frequencies: $\mathcal{O}(N)$.
  - Sorting unique elements: $\mathcal{O}(U \log U)$, where $U$ is the number of distinct elements ($1 \le U \le N$). In the worst case ($U = N$), this is $\mathcal{O}(N \log N)$.
  - Total Time Complexity: $\mathcal{O}(N \log N)$.
  - Total Space Complexity: $\mathcal{O}(N)$ to store the frequency map and list.

---

### Approach 2: Better (Min-Heap / Priority Queue)

- **Mechanism:**
  1. Build a frequency hash map of all elements in $\mathcal{O}(N)$ time.
  2. Maintain a **Min-Heap** of size bounded by $K$.
     - Heap ordering: The element with the smallest frequency is at the root. For equal frequencies, the larger value is at the root (so it is evicted first).
  3. Iterate through each `(element, frequency)` entry in the map:
     - Push the entry into the min-heap.
     - If the heap size exceeds $K$, remove the minimum root element (`poll()`).
  4. After considering all $U$ unique elements, the heap contains exactly the $K$ most frequent elements.
  5. Extract the $K$ elements and sort them according to the required output format (`freq DESC, val ASC`).
- **Complexity:**
  - Building frequency map: $\mathcal{O}(N)$.
  - Heap operations: Each of the $U$ elements undergoes at most one insert and one delete in a heap of size at most $K + 1$, taking $\mathcal{O}(U \log K)$.
  - Final sorting of $K$ elements: $\mathcal{O}(K \log K)$.
  - Total Time Complexity: $\mathcal{O}(N + U \log K)$.
  - Total Space Complexity: $\mathcal{O}(N + K)$.

---

### Approach 3: Optimal (Bucket Sort / Counting Sort by Frequency)

#### Core Intuition

Notice a crucial structural property of the problem:
- Any element's frequency must be an integer between $1$ and $N$.
- The range of possible frequencies $[1, N]$ is bounded directly by the array length!

Because the frequencies are bounded within a finite range $[1, N]$, we do not need comparison-based sorting ($\mathcal{O}(N \log N)$ or $\mathcal{O}(N \log K)$). Instead, we can use **Bucket Sort**:
1. An array of buckets `buckets[0...N]` is initialized, where each bucket index $f$ represents a frequency, and `buckets[f]` contains the list of all unique elements that appear exactly $f$ times.
2. After populating the buckets, we iterate backward from index $N$ down to $1$ (from highest possible frequency to lowest) and collect elements until we have retrieved $K$ elements.

```
Array: nums = [1, 1, 1, 2, 2, 3], N = 6, K = 2

Frequency Map:
  1 -> 3
  2 -> 2
  3 -> 1

Buckets (index = frequency):
  Bucket[6]: []
  Bucket[5]: []
  Bucket[4]: []
  Bucket[3]: [1]        <-- 1st collected (freq 3)
  Bucket[2]: [2]        <-- 2nd collected (freq 2)
  Bucket[1]: [3]
  Bucket[0]: []

Top K = 2 elements collected: [1, 2]
```

#### Step-by-Step Algorithm

1. **Count Frequencies:**
   Traverse `nums` and build `HashMap<Integer, Integer> freqMap`.
2. **Populate Buckets:**
   Create an array of lists `List<Integer>[] buckets = new ArrayList[N + 1]`. For each entry `(val, freq)` in `freqMap`, append `val` to `buckets[freq]`.
3. **Collect and Order:**
   Traverse `buckets` backward from $f = N$ down to $1$:
   - If `buckets[f]` is not empty, sort `buckets[f]` in ascending order to honor the tie-breaking rule.
   - Add elements from `buckets[f]` to the output array until $K$ elements are collected.

---

## 3. Dry Run Example

Consider `nums = [4, 1, -1, 2, -1, 2, 3, -1]`, $N = 8, K = 3$.

| Step | Operation | State / Action |
|:---:|:---|:---|
| 1 | Count Frequencies | `{-1: 3, 2: 2, 1: 1, 3: 1, 4: 1}` |
| 2 | Bucket Assignment | `bucket[3] = [-1]`<br>`bucket[2] = [2]`<br>`bucket[1] = [4, 1, 3]` |
| 3 | Scan $f = 8$ down to $4$ | Buckets are empty |
| 4 | Scan $f = 3$ | `bucket[3]` has `[-1]`. Collect `-1`. Remaining $K = 2$. |
| 5 | Scan $f = 2$ | `bucket[2]` has `[2]`. Collect `2`. Remaining $K = 1$. |
| 6 | Scan $f = 1$ | `bucket[1]` has `[4, 1, 3]`. Sorted: `[1, 3, 4]`. Collect `1`. Remaining $K = 0$. |
| 7 | Done | Result: `[-1, 2, 1]` |

---

## 4. Edge-Case Analysis

1. **Single Element ($N = 1, K = 1$):**
   - The frequency is $1$. Bucket $1$ contains the single element. Handled in $\mathcal{O}(1)$.
2. **All Identical Elements ($U = 1$):**
   - Single element has frequency $N$. Bucket $N$ contains it.
3. **All Unique Elements ($K = N$):**
   - Every element has frequency $1$. All elements reside in `bucket[1]`. Sorting `bucket[1]` returns all elements in ascending order.
4. **Negative Numbers & Extreme Values ($\pm 10^9$):**
   - The values can span from $-10^9$ to $10^9$. Hash map handles arbitrary 32-bit signed integers seamlessly.
5. **Multiple Ties Within Buckets:**
   - Sorting each bucket independently satisfies the secondary ordering criterion without affecting the $\mathcal{O}(N)$ total bucket traversal time.

---

## 5. Complexity Comparison

| Approach | Time Complexity | Auxiliary Space Complexity | Remarks |
|---|:---:|:---:|---|
| **Brute Force (Full Sort)** | $\mathcal{O}(N \log N)$ | $\mathcal{O}(N)$ | Too slow for large $N$ |
| **Better (Min-Heap)** | $\mathcal{O}(N + U \log K)$ | $\mathcal{O}(N + K)$ | Excellent when $K \ll N$ |
| **Optimal (Bucket Sort)** | $\mathcal{O}(N)$ | $\mathcal{O}(N)$ | Linear time, optimal across all inputs |

- **Time Complexity:** $\mathcal{O}(N)$ where $N$ is the number of elements in `nums`. Frequency counting takes $\mathcal{O}(N)$, bucket population takes $\mathcal{O}(U) \le \mathcal{O}(N)$, and traversing buckets takes $\mathcal{O}(N)$ total steps.
- **Space Complexity:** $\mathcal{O}(N)$ auxiliary space for storing the frequency map and bucket lists.
