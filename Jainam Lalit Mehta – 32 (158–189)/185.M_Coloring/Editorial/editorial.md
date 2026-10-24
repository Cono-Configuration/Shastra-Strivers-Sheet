# Editorial: 184. M-Coloring

## Difficulty

Medium

## Prerequisites

* Graphs (adjacency lists)
* Backtracking
* Pruning a search

## Problem Statement

Given a graph with `nodeCount` nodes and a palette of `palette` shades, decide whether the nodes can be coloured so that no two linked nodes share a shade.

## Input Format

First line: `nodeCount palette linkCount`. Then `linkCount` lines, each with a link `u v`.

## Constraints

* `2 <= nodeCount <= 20`
* `1 <= linkCount <= nodeCount * (nodeCount - 1) / 2`
* `1 <= palette <= nodeCount`

## Output Format

`true` or `false`.

## Explanation & Approach

### Step 1: Build Neighbour Lists

Store each node's neighbours in a list, adding each link in both directions.

### Step 2: Assign Shades Node by Node

Process nodes in order `0, 1, ..., nodeCount - 1`. Keep an array `shade` where `shade[v]` is the shade of node `v`, or `-1` if it is not yet coloured.

### Step 3: Check a Shade

A shade `c` is allowed for node `v` only if no neighbour of `v` already has shade `c`. Neighbours not yet coloured are `-1` and never clash.

### Step 4: Backtrack

If a node has no allowed shade, return `false` for that branch, reset the current node, and try the next option for the previous node.

### Step 5: Base Case

When every node has a shade, the colouring is valid, so return `true`.

### Walkthrough

```text
Example 2: nodes 0,1,2 all linked, palette = 2

node 0 -> shade 0
node 1 -> shade 1   (0 has shade 0)
node 2 -> needs a shade different from 0 and 1: none available
backtrack to node 1 -> no other shade
backtrack to node 0 -> shade 1, node 1 -> shade 0, node 2 -> none available
Result: false
```

## Example

### Input

```text
3 2 3
0 1
1 2
0 2
```

### Output

```text
false
```

## Java Solution

```java
import java.util.*;

class Solution {
    public boolean canShade(int nodeCount, int palette, int[][] links) {
        List<List<Integer>> neighbours = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            neighbours.add(new ArrayList<>());
        }
        for (int[] link : links) {
            neighbours.get(link[0]).add(link[1]);
            neighbours.get(link[1]).add(link[0]);
        }

        int[] shade = new int[nodeCount];
        Arrays.fill(shade, -1);
        return assign(0, nodeCount, palette, neighbours, shade);
    }

    private boolean assign(int node, int nodeCount, int palette, List<List<Integer>> neighbours, int[] shade) {
        if (node == nodeCount) {
            return true;
        }
        for (int colour = 0; colour < palette; colour++) {
            if (!clashes(node, colour, neighbours, shade)) {
                shade[node] = colour;
                if (assign(node + 1, nodeCount, palette, neighbours, shade)) {
                    return true;
                }
                shade[node] = -1;
            }
        }
        return false;
    }

    private boolean clashes(int node, int colour, List<List<Integer>> neighbours, int[] shade) {
        for (int other : neighbours.get(node)) {
            if (shade[other] == colour) {
                return true;
            }
        }
        return false;
    }
}
```

## Time Complexity

In the worst case, every node tries all `palette` shades, so the search can visit up to `palette^nodeCount` partial colourings. Each check costs the node's degree.

**Time Complexity: `O(M^N * N)`**, where `M = palette` and `N = nodeCount`. This is exponential, which is expected for this NP-complete problem. The constraint `N <= 20` keeps it practical.

## Space Complexity

The recursion depth is `N`, and the adjacency lists and shade array use `O(N + E)` space.

**Space Complexity: `O(N + E)`**

## Key Takeaway

> Colour nodes one at a time, reject any shade already used by a neighbour, and backtrack when stuck. The search is exponential in the worst case, but pruning on neighbours keeps it fast for small graphs.
