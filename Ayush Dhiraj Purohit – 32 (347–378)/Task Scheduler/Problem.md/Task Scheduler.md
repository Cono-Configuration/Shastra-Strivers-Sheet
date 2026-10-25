# Task Scheduler

## Problem Statement

You are given an array of CPU `tasks`, each represented by an uppercase English letter from `'A'` to `'Z'`, and a non-negative integer $n$ representing the **cooldown interval** between two identical tasks.

Each CPU interval takes $1$ unit of time, during which the CPU can either:
- Execute any available task, or
- Remain **idle**.

Identical tasks must be separated by at least $n$ intervals. During these cooldown intervals, the CPU can either execute different tasks or remain idle.

Return the **minimum number of CPU intervals** required to complete all given tasks.

---

## Input Format

- The first line contains two space-separated integers $N$ and $n$, denoting the number of tasks and the cooldown interval, respectively.
- The second line contains $N$ space-separated uppercase characters representing the elements of `tasks`.

---

## Output Format

- Print a single integer representing the minimum number of CPU intervals needed to execute all tasks.

---

## Examples

### Example 1

**Input:**
```text
6 2
A A A B B B
```

**Output:**
```text
8
```

**Explanation:**
A valid execution sequence is:
`A -> B -> idle -> A -> B -> idle -> A -> B`
- Cooldown between identical `'A'` tasks: at least $2$ intervals.
- Cooldown between identical `'B'` tasks: at least $2$ intervals.
- Total intervals needed: $8$.

---

### Example 2

**Input:**
```text
6 1
A C A B D B
```

**Output:**
```text
6
```

**Explanation:**
A valid execution sequence is:
`A -> B -> C -> D -> A -> B`
Since $n = 1$, only $1$ interval is required between identical tasks. Other tasks fill the cooldown slots completely, so no idle intervals are needed. Total intervals: $6$.

---

### Example 3

**Input:**
```text
6 3
A A A B B B
```

**Output:**
```text
10
```

**Explanation:**
Since $n = 3$, identical tasks must be separated by at least $3$ intervals.
A valid execution sequence is:
`A -> B -> idle -> idle -> A -> B -> idle -> idle -> A -> B`
Total intervals needed: $10$.

---

### Example 4

**Input:**
```text
3 0
A A B
```

**Output:**
```text
3
```

**Explanation:**
With $n = 0$, there is no cooldown requirement between identical tasks. The CPU can execute them back-to-back without any idle intervals. Total intervals: $3$.

---

## Constraints

- $1 \le N \le 10^5$, where $N$ is the number of tasks.
- $\text{tasks}[i]$ is an uppercase English letter from `'A'` to `'Z'`.
- $0 \le n \le 100$
