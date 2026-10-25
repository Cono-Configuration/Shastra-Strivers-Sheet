# Editorial: Task Scheduler

## 1. Problem Overview

We are given an array of tasks represented by uppercase English letters (`'A'` through `'Z'`) and a cooldown integer $n$. Each CPU interval takes $1$ unit of time. The primary constraint is that any two identical tasks must be separated by at least $n$ intervals. During these intervals, the CPU can execute different tasks or remain idle. We want to find the minimum total CPU intervals required to complete all tasks.

---

## 2. Approach 1: Greedy Simulation using Max-Heap

### Intuition
To minimize idle intervals, we must schedule the most frequent tasks as early as possible. If we delay frequent tasks, we will be forced to place excessive idle intervals at the end of the schedule.

### Algorithm
1. Count the frequencies of each of the $26$ uppercase letters.
2. Insert all positive frequencies into a **Max-Heap**.
3. Process tasks in rounds (or frames) of size $(n + 1)$ CPU intervals:
   - In each round, extract up to $(n + 1)$ highest-frequency tasks from the heap.
   - Decrement each extracted frequency by $1$.
   - If the heap becomes empty and all extracted frequencies drop to $0$, we add the exact number of tasks processed in this final round.
   - Otherwise, we must add the full $(n + 1)$ intervals to our total time (accounting for idle time if fewer than $n + 1$ unique tasks were available).
   - Reinsert all non-zero remaining frequencies back into the Max-Heap.
4. Repeat until the heap is empty.

### Complexity
- **Time Complexity:** $\mathcal{O}(N)$, since each task is pushed and popped a bounded number of times and the heap size never exceeds $26$.
- **Space Complexity:** $\mathcal{O}(1)$, as the heap stores at most $26$ entries.

---

## 3. Approach 2: Mathematical Slot Filling (Optimal)

### Intuition
Instead of simulating interval-by-interval, we can determine the exact minimum intervals algebraically by analyzing the task with the highest frequency (`maxFreq`).

### Derivation
Suppose the maximum frequency of any task is `maxFreq`. Let task `'A'` be one such task.
- The first $(maxFreq - 1)$ occurrences of `'A'` each initiate a block of length $(n + 1)$ (1 interval for `'A'` followed by $n$ cooldown intervals).
- This creates $(maxFreq - 1)$ blocks of size $(n + 1)$:
  $$\text{Base Blocks Length} = (maxFreq - 1) \times (n + 1)$$
- The final occurrence of `'A'` does not require any subsequent cooldown intervals.
- If multiple tasks tie for the maximum frequency (say, `countMax` tasks have frequency equal to `maxFreq`), all of them must appear in the final block.
- Thus, the minimum intervals required by the most frequent tasks alone is:
  $$\text{Required} = (maxFreq - 1) \times (n + 1) + countMax$$

### Handling Remaining Tasks:
- What if there are other tasks with smaller frequencies?
  - These tasks can simply be inserted into the idle slots within the $(maxFreq - 1)$ blocks.
  - If the number of remaining tasks exceeds the available idle slots, we can expand the frames without violating the cooldown constraint (cooldown is at least $n$, so extra spacing is always legal).
  - In that scenario, zero idle intervals are required, and the total intervals needed is simply the total number of tasks $N$.

### Unified Closed-Form Formula:
$$\text{Ans} = \max\left(N, \quad (maxFreq - 1) \times (n + 1) + countMax\right)$$

---

## 4. Step-by-Step Dry Run

### Walkthrough of Example 1:
- `tasks = ["A", "A", "A", "B", "B", "B"]`, $n = 2$, $N = 6$.
- Frequency table:
  - `'A'`: 3
  - `'B'`: 3
- Maximum frequency: `maxFreq = 3`.
- Tasks with maximum frequency: `'A'` and `'B'` $\rightarrow$ `countMax = 2`.
- Apply formula:
  $$\text{Ans} = \max(6, \ (3 - 1) \times (2 + 1) + 2) = \max(6, \ 2 \times 3 + 2) = \max(6, \ 8) = 8$$

Layout visualization:
- Block 1: `A B idle` (length $3$)
- Block 2: `A B idle` (length $3$)
- Block 3: `A B` (length $2$)
- Total length: $3 + 3 + 2 = 8$.

### Walkthrough of Example 2:
- `tasks = ["A", "C", "A", "B", "D", "B"]`, $n = 1$, $N = 6$.
- Frequencies: `'A'`: 2, `'B'`: 2, `'C'`: 1, `'D'`: 1.
- `maxFreq = 2`, `countMax = 2` (`'A'` and `'B'`).
- Apply formula:
  $$\text{Ans} = \max(6, \ (2 - 1) \times (1 + 1) + 2) = \max(6, \ 1 \times 2 + 2) = \max(6, 4) = 6$$
- Since $N = 6 > 4$, all idle slots are filled, requiring exactly $6$ intervals with 0 idle time.

---

## 5. Edge Cases & Boundary Analysis

1. **$n = 0$ (No Cooldown):**
   - With no cooldown restriction, tasks can run back-to-back in any order. The formula yields $\max(N, (maxFreq - 1) \times 1 + countMax) = N$.
2. **All Tasks Identical:**
   - E.g. `N` occurrences of `'A'`: `maxFreq = N, countMax = 1`.
   - $\text{Ans} = (N - 1) \times (n + 1) + 1$.
3. **All Tasks Unique:**
   - Every task has frequency $1$: `maxFreq = 1, countMax = N`.
   - $\text{Ans} = \max(N, 0 + N) = N$.
4. **$n$ Greater than $N$ ($n \ge N$):**
   - Handled seamlessly by the $(maxFreq - 1) \times (n + 1) + countMax$ term.
5. **Maximum Scale Constraints:**
   - $N = 10^5$, $n = 100$.
   - Frequency counting requires a single linear scan of $10^5$ elements.
   - Formula evaluation takes $\mathcal{O}(1)$ time.
   - Runs in $< 5\text{ ms}$ in Java.

---

## 6. Complexity Analysis

- **Time Complexity:** $\mathcal{O}(N)$
  - Counting frequencies of $N$ characters takes $\mathcal{O}(N)$ time.
  - Finding `maxFreq` and `countMax` scans a fixed array of size $26$, taking $\mathcal{O}(1)$ time.
  - Total time complexity is strictly linear, $\mathcal{O}(N)$.
- **Space Complexity:** $\mathcal{O}(1)$
  - Only a constant-size frequency table of length $26$ is maintained.
