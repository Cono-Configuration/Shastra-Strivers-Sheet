# Editorial: Merge K Sorted Lists

## 1. Problem Overview

We are given an array of $K$ singly linked lists, each sorted in non-decreasing order. The objective is to merge all nodes across these $K$ lists into a single consolidated linked list, also arranged in non-decreasing order, and return the head of this merged list.

---

## 2. Brute-Force Approach

### Intuition
The simplest approach ignores the pre-sorted structure of the individual lists:
1. Traverse all $K$ linked lists and extract each node's value into a dynamic array.
2. Sort the entire array using a comparison-based sorting algorithm (e.g., QuickSort, MergeSort, or Dual-Pivot Quicksort).
3. Reconstruct a new singly linked list by creating a sequence of nodes corresponding to the elements of the sorted array.

### Complexity Analysis
- **Time Complexity:** $\mathcal{O}(N \log N)$, where $N$ is the total number of nodes across all $K$ lists. Extracting values takes $\mathcal{O}(N)$, sorting the array takes $\mathcal{O}(N \log N)$, and rebuilding the list takes $\mathcal{O}(N)$.
- **Space Complexity:** $\mathcal{O}(N)$ auxiliary space to store all node values in the array and construct the output nodes.

---

## 3. Divide and Conquer Approach

### Intuition
Instead of merging all lists at once, we can pair up the $K$ lists and merge each pair using the standard two-pointer **Merge Two Sorted Lists** routine ($\mathcal{O}(L_1 + L_2)$ time):
- In Round 1, $K$ lists are merged pairwise into $\lceil K / 2 \rceil$ lists.
- In Round 2, $\lceil K / 2 \rceil$ lists are merged into $\lceil K / 4 \rceil$ lists.
- This continues for $\lceil \log_2 K \rceil$ rounds until only $1$ fully merged list remains.

In every round, every one of the $N$ nodes is visited exactly once, yielding $\mathcal{O}(N \log K)$ total time. While optimal in asymptotic time, it involves repeated list rewiring across multiple passes.

---

## 4. Optimal Approach: Min-Heap / Priority Queue

### Core Intuition
Because every individual linked list is already sorted in non-decreasing order, the overall smallest element among all unmerged nodes **must always reside at the current head of one of the $K$ lists**.

At any moment, there are at most $K$ candidates for the next smallest node. Rather than scanning all $K$ candidates linearly ($\mathcal{O}(K)$ per node, totaling $\mathcal{O}(N \cdot K)$), we can maintain these active candidates in a **Min-Heap (Priority Queue)** of maximum capacity $K$:
1. Querying the global minimum candidate takes $\mathcal{O}(1)$ time (root of the min-heap).
2. Removing the minimum node and inserting its successor from the same list takes $\mathcal{O}(\log K)$ time.

### Algorithm Steps

1. **Edge Case Validation:**
   - If $K = 0$ or all lists are empty (`null`), immediately return `null`.

2. **Initialize Min-Heap:**
   - Create a binary min-heap capable of holding up to $K$ `ListNode` pointers.
   - Insert the head node of each non-empty list into the heap.

3. **Merge Loop:**
   - Maintain a `dummy` node and a `tail` pointer initialized to `dummy`.
   - While the min-heap is not empty:
     - Extract the root node `minNode = heap.peek()`.
     - Append `minNode` to `tail.next`, and update `tail = minNode`.
     - If `minNode.next != null`:
       - Replace the root of the heap with `minNode.next` and perform a single `siftDown(0)` operation (optimizing away a separate poll and push).
     - Otherwise (the list from which `minNode` originated is exhausted):
       - Replace the root with the last element of the heap, reduce the heap size by 1, and `siftDown(0)` if elements remain.

4. **Termination:**
   - Set `tail.next = null` to ensure no dangling pointer remains.
   - Return `dummy.next`.

---

## 5. Dry Run Walkthrough

Consider Example 1:
- List 1: `1 -> 4 -> 5`
- List 2: `1 -> 3 -> 4`
- List 3: `2 -> 6`

### Heap Initialization:
- Push heads into Min-Heap: `[1 (L1), 1 (L2), 2 (L3)]` (size = 3)
- `dummy -> null`, `tail = dummy`

### Iterations:
1. **Extract `1 (L1)`:**
   - Append to result: `tail -> 1 (L1)`
   - Next node is `4 (L1)`. Replace root with `4 (L1)` and sift down:
   - Heap becomes: `[1 (L2), 4 (L1), 2 (L3)]`
2. **Extract `1 (L2)`:**
   - Append to result: `tail -> 1 (L2)`
   - Next node is `3 (L2)`. Replace root with `3 (L2)` and sift down:
   - Heap becomes: `[2 (L3), 4 (L1), 3 (L2)]`
3. **Extract `2 (L3)`:**
   - Append to result: `tail -> 2 (L3)`
   - Next node is `6 (L3)`. Replace root with `6 (L3)` and sift down:
   - Heap becomes: `[3 (L2), 4 (L1), 6 (L3)]`
4. **Extract `3 (L2)`:**
   - Append to result: `tail -> 3 (L2)`
   - Next node is `4 (L2)`. Replace root with `4 (L2)` and sift down:
   - Heap becomes: `[4 (L1), 4 (L2), 6 (L3)]`
5. **Extract `4 (L1)`:**
   - Append to result: `tail -> 4 (L1)`
   - Next node is `5 (L1)`. Replace root with `5 (L1)` and sift down:
   - Heap becomes: `[4 (L2), 5 (L1), 6 (L3)]`
6. **Extract `4 (L2)`:**
   - Append to result: `tail -> 4 (L2)`
   - Next node is `null`. Pop root, move last element `6 (L3)` to root (size = 2), sift down:
   - Heap becomes: `[5 (L1), 6 (L3)]`
7. **Extract `5 (L1)`:**
   - Append to result: `tail -> 5 (L1)`
   - Next node is `null`. Pop root, move `6 (L3)` to root (size = 1).
   - Heap becomes: `[6 (L3)]`
8. **Extract `6 (L3)`:**
   - Append to result: `tail -> 6 (L3)`
   - Next node is `null`. Heap becomes empty (size = 0).

Terminating: `tail.next = null`.
Resulting list: `1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6`.

---

## 6. Edge Cases & Boundary Analysis

1. **$K = 0$ (No lists):**
   - The heap remains empty; the algorithm immediately returns `null` or prints an empty line.
2. **All lists empty ($M_i = 0$ for all $i$):**
   - No nodes are added to the heap; returns `null`.
3. **Mixed empty and non-empty lists:**
   - Only valid heads enter the heap. Empty lists are ignored naturally.
4. **$K = 1$:**
   - The single list is placed in the heap of capacity 1, advancing until exhausted. Runs in $\mathcal{O}(N \log 1) = \mathcal{O}(N)$ time.
5. **Duplicate values across lists:**
   - The min-heap uses `heap[child].val < heap[parent].val` for ordering. Duplicates are handled stably and correctly without stalls.
6. **Negative numbers and large values:**
   - Values in range $[-10^9, 10^9]$ fit comfortably in 32-bit signed integers; standard integer comparisons avoid overflow issues.
7. **Maximum constraints ($N = 10^5$, $K = 10^4$):**
   - Heap operations take at most $\approx \log_2(10000) \approx 14$ comparisons.
   - Total runtime is bounded by $\approx 1.4 \times 10^6$ operations, executing in under $0.05$ seconds in Java.

---

## 7. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N \log K)$
  - Building the initial heap of at most $K$ elements takes $\mathcal{O}(K)$ time.
  - Each of the $N$ nodes is extracted and advanced in the heap at most once. Each heap operation takes $\mathcal{O}(\log K)$ time.
  - Overall time complexity is strictly $\mathcal{O}(N \log K)$.
- **Auxiliary Space Complexity:** $\mathcal{O}(K)$
  - The Min-Heap stores at most $K$ node references simultaneously.
  - Merging is done in-place by rewiring the existing `next` pointers, requiring $\mathcal{O}(1)$ additional memory.
