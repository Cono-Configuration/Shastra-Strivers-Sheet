# Problem 172: Reverse a Stack

**Difficulty:** Medium | **Tags:** Stack, Recursion

## Problem Statement

A stack of integers named `pile` is given to you. Using recursion, turn the stack upside down so that the element that was at the bottom ends up on top, the element that was on top ends up at the bottom, and every other element is mirrored in the same way.

Only the usual stack operations (`push`, `pop`, `top`/`peek` and `isEmpty`) may be used. Loops of any kind (`for`, `while`, etc.) are forbidden, and so are extra containers such as arrays, lists or queues.

The reversal must be done **in-place**, meaning the original stack itself must hold the reversed order when the function finishes.

In the examples and test files, a stack is written from **bottom to top**, so the last number listed is the top of the stack.

## Example 1

```text
Input: pile = [4, 1, 3, 2]
Output: [2, 3, 1, 4]
```

## Example 2

```text
Input: pile = [10, 20, -5, 7, 15]
Output: [15, 7, -5, 20, 10]
```

## Constraints

The original statement does not list explicit constraints, so the following are used:

* `1 <= pile.size() <= 1000`
* `-10^9 <= pile[i] <= 10^9`

## Input Format

* The first line contains an integer `n`, the number of elements in the stack.
* The second line contains `n` space-separated integers describing the stack from bottom to top.

## Output Format

Print the elements of the reversed stack from bottom to top on a single line, separated by single spaces.

## Approach

Solve it with two recursive helpers.

* `reverseStack`: if the stack is empty, stop. Otherwise remove the top element, recursively reverse the remaining stack, and then place the removed element at the **bottom** of the now-reversed stack.
* `insertAtBottom`: if the stack is empty, push the value. Otherwise remove the top element, recursively insert the value at the bottom, and push the removed element back on top.

The call stack temporarily holds the elements, so no loops or extra data structures are required.
