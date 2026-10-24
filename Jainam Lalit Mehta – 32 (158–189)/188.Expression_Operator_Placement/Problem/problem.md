# Problem 188: Expression Operator Placement

**Difficulty:** Hard | **Tags:** Math, String, Backtracking

## Problem Statement

You are given a string `numeral` made only of the digits `0` to `9`, and an integer `desired`.

Insert the operators `+`, `-`, or `*` between the digits, or leave a position without an operator. Return every resulting expression that evaluates to `desired`.

Two rules apply:

* An operand must not have a leading zero. For example, `02` is not allowed, but `0` on its own is.
* An operand can contain several digits. For example, `12` is one operand, and `1+2` is two operands.

Multiplication has higher precedence than addition and subtraction. The answer may be returned in any order. In the test files, the expressions are listed one per line in lexicographic order.

## Example 1

```text
Input: numeral = "123", desired = 6
Output: ["1*2*3","1+2+3"]
Explanation: Both "1*2*3" and "1+2+3" evaluate to 6.
```

## Example 2

```text
Input: numeral = "0232", desired = 8
Output: ["0+2*3+2","0+2+3*2"]
Explanation: Both "0+2*3+2" and "0+2+3*2" evaluate to 8.
The operand 0 on its own is allowed, but 02 or 023 would have leading zeros.
```

## Example 3

```text
Input: numeral = "02", desired = 0
Output: ["0*2"]
Explanation: 0*2 evaluates to 0. The expression 0+2 evaluates to 2, and 02 has a leading zero.
```

## Constraints

* `1 <= numeral.length <= 10`
* `numeral` contains only digits.
* `-2^31 <= desired <= 2^31 - 1`

## Input Format

Two lines:

1. The string `numeral`.
2. The integer `desired`.

## Output Format

Print each valid expression on its own line, with no spaces. Expressions appear in lexicographic order.

If no expression exists, the output file is empty (zero lines).

For Example 1 the input file is:

```text
123
6
```

and the output file is:

```text
1*2*3
1+2+3
```

## Approach

Build the expression from left to right with depth-first search. At each position, the next operand is a prefix of the remaining digits. Skip any operand with a leading zero, except the single digit `0`.

Keep two running values:

* `total`: the value of the expression so far, with the last term not yet committed.
* `last`: the last term added. When the next operator is `*`, remove that term from `total` and add back the product of `last` and the new operand. This keeps precedence correct without parsing the expression again.

When every digit is used, compare `total` with `desired`.
