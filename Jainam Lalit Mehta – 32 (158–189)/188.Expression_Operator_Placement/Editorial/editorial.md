# Editorial: 188. Expression Operator Placement

## Difficulty

Hard

## Prerequisites

* Backtracking (depth-first search)
* Operator precedence
* Handling `long` arithmetic

## Problem Statement

Given a digit string and a desired value, return every expression formed by inserting `+`, `-`, `*`, or nothing between the digits that evaluates to the desired value. Operands cannot have leading zeros, but a lone `0` is allowed.

## Input Format

Line 1: the digit string. Line 2: the desired value.

## Constraints

* `1 <= numeral.length <= 10`
* `-2^31 <= desired <= 2^31 - 1`

## Output Format

One expression per line, in lexicographic order. An empty file means no expression exists.

## Explanation & Approach

### Step 1: Choose Operands

At each position, the next operand is a prefix of the remaining digits. If the current digit is `0`, the only valid operand is `0` itself.

### Step 2: Choose an Operator

For every operand after the first, try `+`, `-`, and `*`.

### Step 3: Track the Value Correctly

Let `total` be the value so far, excluding the last term, and `last` be the last term.

* `+ operand`: `total + operand`, and the new last term is `operand`.
* `- operand`: `total - operand`, and the new last term is `-operand`. Storing the sign in the last term means a later `*` multiplies it correctly.
* `* operand`: the new last term is `last * operand`, and the new total is `total - last + last * operand`.

Subtracting `last` removes the old term, and adding the product puts back the combined term. This handles precedence without building a parse tree.

### Step 4: Base Case

When every digit is used, compare `total` with `desired`. If they match, record the expression.

### Step 5: Sort

Sorting the results once at the end gives the lexicographic order used in the test files.

### Walkthrough

```text
numeral = "0232", desired = 8

"0"
├── "0+2"  total=2, last=2
│   ├── "0+2*3"  total=2-2+6=6, last=6
│   │   └── "0+2*3+2"  total=8  ✔
│   └── "0+2+3"  total=5
│       └── "0+2+3*2"  total=5-3+6=8  ✔
└── ...
```

## Example

### Input

```text
123
6
```

### Output

```text
1*2*3
1+2+3
```

## Java Solution

```java
import java.util.*;

public class Solution {

    public List<String> buildExpressions(String numeral, int desired) {
        List<String> expressions = new ArrayList<>();
        extend(expressions, numeral, desired, 0, 0, 0, new StringBuilder());
        Collections.sort(expressions);
        return expressions;
    }

    private void extend(List<String> expressions, String numeral, long desired, int position,
                        long total, long last, StringBuilder path) {
        if (position == numeral.length()) {
            if (total == desired) {
                expressions.add(path.toString());
            }
            return;
        }

        for (int end = position + 1; end <= numeral.length(); end++) {
            if (end > position + 1 && numeral.charAt(position) == '0') {
                break;
            }

            long operand = Long.parseLong(numeral.substring(position, end));
            int length = path.length();

            if (position == 0) {
                path.append(operand);
                extend(expressions, numeral, desired, end, operand, operand, path);
                path.setLength(length);
            } else {
                path.append('+').append(operand);
                extend(expressions, numeral, desired, end, total + operand, operand, path);
                path.setLength(length);

                path.append('-').append(operand);
                extend(expressions, numeral, desired, end, total - operand, -operand, path);
                path.setLength(length);

                long product = last * operand;
                path.append('*').append(operand);
                extend(expressions, numeral, desired, end, total - last + product, product, path);
                path.setLength(length);
            }
        }
    }
}
```

## Time Complexity

With `N` digits there are at most `N - 1` gaps. Each gap has 4 choices (no operator, `+`, `-`, `*`), so the search visits at most `4^(N-1)` operator choices. Each complete expression costs `O(N)` to build.

**Time Complexity: `O(4^N * N)`**, plus an `O(k log k)` sort of the `k` results.

## Space Complexity

The recursion depth is at most `N`, and the current expression uses `O(N)` space. The output is not counted.

**Space Complexity: `O(N)`**

## Key Takeaway

> Track the last term separately so `*` can be applied by undoing that term. Skip operands with leading zeros, but allow a lone `0`, and sort the answer for a deterministic order.
