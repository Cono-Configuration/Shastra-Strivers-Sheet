# Editorial: 172. Reverse a Stack

## Difficulty

Medium

## Prerequisites

* Stack operations (`push`, `pop`, `peek`, `isEmpty`)
* Recursion
* Understanding of the call stack

## Problem Statement

Reverse a stack of integers in-place using recursion, using only standard stack operations. Loops and extra data structures such as arrays or queues are not allowed.

## Input Format

* The first line contains an integer `n`, the number of elements.
* The second line contains `n` space-separated integers, listed from the bottom of the stack to the top.

## Constraints

* `1 <= n <= 1000`
* `-10^9 <= value <= 10^9`

## Output Format

Print the reversed stack from bottom to top, separated by spaces.

## Explanation & Approach

### Step 1: Reverse by Removing the Top

If the stack is empty, there is nothing to reverse. Otherwise:

1. Pop the top element and remember it.
2. Recursively reverse the remaining stack.
3. Insert the remembered element at the **bottom** of the reversed stack.

The element that was on top becomes the bottom element, and the recursion handles the rest.

### Step 2: Insert at the Bottom Using Recursion

To place a value at the bottom of a stack without extra storage:

1. If the stack is empty, push the value. It is now at the bottom.
2. Otherwise pop the top element and remember it.
3. Recursively insert the value at the bottom.
4. Push the remembered element back.

Each remembered element lives in a recursive call frame, which replaces the need for any auxiliary container.

### Walkthrough

```text
pile (bottom -> top) = [4, 1, 3, 2]
```

* Pop `2`, reverse `[4, 1, 3]`:
  * Pop `3`, reverse `[4, 1]`:
    * Pop `1`, reverse `[4]`:
      * Pop `4`, reverse `[]` which stays `[]`, insert `4` at bottom: `[4]`
    * Insert `1` at bottom: `[1, 4]`
  * Insert `3` at bottom: `[3, 1, 4]`
* Insert `2` at bottom: `[2, 3, 1, 4]`

The result is `[2, 3, 1, 4]`.

## Example

### Input

```text
5
10 20 -5 7 15
```

### Output

```text
15 7 -5 20 10
```

## Java Solution

```java
class Solution {
    public void reverseStack(Stack<Integer> pile) {

        if (pile.isEmpty()) {
            return;
        }

        int topValue = pile.pop();

        reverseStack(pile);

        insertAtBottom(pile, topValue);
    }

    private void insertAtBottom(Stack<Integer> pile, int value) {

        if (pile.isEmpty()) {
            pile.push(value);
            return;
        }

        int topValue = pile.pop();

        insertAtBottom(pile, value);

        pile.push(topValue);
    }
}
```

## Time Complexity

`reverseStack` makes `N` calls, and each one triggers an `insertAtBottom` that takes up to `O(N)` steps.

**Time Complexity: `O(N^2)`**

## Space Complexity

No auxiliary data structure is used. The only extra memory is the recursion call stack, with depth up to `N`.

**Space Complexity: `O(N)`** (recursion stack)

## Key Takeaway

> Pop the top, reverse what is left, then slide the popped element under everything using a second recursion. The call stack plays the role of temporary storage, so the reversal is done in-place with no loops and no extra containers.
