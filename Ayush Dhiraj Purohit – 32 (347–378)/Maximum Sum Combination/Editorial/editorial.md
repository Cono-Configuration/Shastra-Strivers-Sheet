# Editorial: Maximum Sum Combination

## 1. Problem Overview

We are given two integer arrays `nums1` and `nums2` of size $N$ each, and an integer $K$. We must return the top $K$ maximum sum combinations $\text{nums1}[i] + \text{nums2}[j]$ (for $0 \le i, j < N$), sorted in non-increasing order (from largest to smallest).

---

## 2. Brute-Force Approach

### Intuition
Generate all possible pair sums by pairing every element in `nums1` with every element in `nums2`. Store these $N^2$ sums in an array, sort the array in descending order, and pick the first $K$ elements.

### Complexity Analysis
- **Time Complexity:** $\mathcal{O}(N^2 \log(N^2))$ due to sorting $N^2$ elements.
- **Space Complexity:** $\mathcal{O}(N^2)$ to store all $N^2$ sum combinations.
- **Verdict:** For $N = 10^5$, $N^2 = 10^{10}$, which will exceed both memory limits (MLE) and time limits (TLE) by several orders of magnitude.

---

## 3. Approach 2: Max-Heap with Visited Set

### Intuition
If both arrays are sorted in ascending order, the globally largest sum is guaranteed to be formed by their respective last elements:
$$\text{Max Sum} = \text{nums1}[N - 1] + \text{nums2}[N - 1]$$

After extracting the maximum pair $(i, j)$, the only candidates that could possibly be the next largest are:
1. $(i - 1, j)$ with sum $\text{nums1}[i - 1] + \text{nums2}[j]$
2. $(i, j - 1)$ with sum $\text{nums1}[i] + \text{nums2}[j - 1]$

To avoid inserting the same coordinate pair $(i, j)$ multiple times (since $(i - 1, j - 1)$ can be reached from both $(i - 1, j)$ and $(i, j - 1)$), we track visited coordinate pairs using a hash set.

### Complexity:
- **Time Complexity:** $\mathcal{O}(N \log N + K \log K)$
- **Space Complexity:** $\mathcal{O}(K)$ for the heap and visited set.

---

## 4. Approach 3: Row-Based K-Way Heap Merge (Optimal)

### Intuition
We can eliminate the need for a visited `Set` entirely by interpreting the problem as a **K-Way Merge of sorted rows**:
- Consider a virtual matrix $M$ where $M[i][j] = \text{nums1}[i] + \text{nums2}[j]$.
- With `nums1` and `nums2` sorted ascending, each row $i$ is sorted in strictly non-increasing order from right to left:
  $$M[i][N - 1] \ge M[i][N - 2] \ge \dots \ge M[i][0]$$
- Furthermore, across rows:
  $$M[N - 1][j] \ge M[N - 2][j] \ge \dots \ge M[0][j]$$

### Key Observation:
Only the top $m = \min(N, K)$ rows (i.e. rows $N - m \dots N - 1$) can ever contribute to the top $K$ combinations. A row $i < N - K$ cannot contain any of the top $K$ sums because there already exist at least $K$ elements across rows $N - K \dots N - 1$ that are strictly greater than or equal to $M[i][N - 1]$.

### Algorithm Steps:
1. **Sort** `nums1` and `nums2` in ascending order.
2. Let $m = \min(N, K)$.
3. **Initialize Max-Heap:** Insert the rightmost element of the top $m$ rows into the heap:
   $$\text{heap.add}(M[i][N - 1], \ i, \ N - 1) \quad \text{for } i \in [N - m, N - 1]$$
   The heap contains at most $m$ elements at any time.
4. **Extract Top $K$ Times:**
   - Extract the maximum element: $(sum, i, j) = \text{heap.poll()}$.
   - Add $sum$ to the result list.
   - If $j - 1 \ge 0$, advance within the **same row** $i$ and push:
     $$(M[i][j - 1], \ i, \ j - 1)$$
5. Return the result list.

### Why No Set is Needed:
Each row $i$ progresses independently from right to left ($j \rightarrow j - 1$). Since each pair belongs to a unique row $i$, **no pair $(i, j)$ can ever be generated more than once**. This completely eliminates hash set overhead and hash collision risks!

---

## 5. Step-by-Step Dry Run

Consider Example 1: `nums1 = [7, 3]`, `nums2 = [1, 6]`, $K = 2$, $N = 2$.

1. Sort arrays:
   - `nums1 = [3, 7]`
   - `nums2 = [1, 6]`
2. $m = \min(2, 2) = 2$.
   - Row $1$ ($i = 1, \text{val} = 7$): $(7 + 6, 1, 1) = (13, 1, 1)$
   - Row $0$ ($i = 0, \text{val} = 3$): $(3 + 6, 0, 1) = (9, 0, 1)$
   - Initial Heap: `[(13, 1, 1), (9, 0, 1)]`
3. Extraction 1:
   - Pop `(13, 1, 1)`. Add `13` to result.
   - Advance row 1: push `(7 + 1, 1, 0) = (8, 1, 0)`.
   - Heap: `[(9, 0, 1), (8, 1, 0)]`.
4. Extraction 2:
   - Pop `(9, 0, 1)`. Add `9` to result.
   - Advance row 0: push `(3 + 1, 0, 0) = (4, 0, 0)`.
5. Loop terminates after collecting $K = 2$ elements.
6. Output: `13 9`.

---

## 6. Edge Cases & Boundary Analysis

1. **$N = 1, K = 1$:**
   - Single pair `nums1[0] + nums2[0]`.
2. **$K = 1$:**
   - Evaluates only the single global maximum `nums1[N-1] + nums2[N-1]`.
3. **Negative Numbers:**
   - Works identically with negative values since ordering is invariant under shifts.
4. **Duplicate Elements:**
   - Handled naturally; identical sums are processed in valid non-increasing order.
5. **Scale ($N = 10^5, K = 10^5$):**
   - Heap size is bounded by $\min(N, K) \le 10^5$.
   - Extraction loop takes $\mathcal{O}(K \log(\min(N, K))) \approx 10^5 \times 17 \approx 1.7 \times 10^6$ operations, executing in under $30\text{ ms}$.

---

## 7. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N \log N + K \log(\min(N, K)))$
  - Sorting `nums1` and `nums2` takes $\mathcal{O}(N \log N)$ time.
  - Heap initialization takes $\mathcal{O}(\min(N, K))$ time.
  - Extracting $K$ elements from a heap of size at most $\min(N, K)$ takes $\mathcal{O}(K \log(\min(N, K)))$ time.
  - Overall time complexity is strictly $\mathcal{O}(N \log N + K \log(\min(N, K)))$.
- **Space Complexity:** $\mathcal{O}(\min(N, K))$
  - The priority queue stores at most $\min(N, K)$ elements simultaneously.
