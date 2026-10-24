# Problem 184: M-Coloring

**Difficulty:** Medium | **Tags:** Graph, Backtracking

## Problem Statement

You are given a simple undirected graph with `nodeCount` nodes, numbered from `0` to `nodeCount - 1`, and `linkCount` links. You are also given an integer `palette`, the number of shades available.

Decide whether every node can be given one shade from the palette so that no two nodes joined by a link share a shade. You do not have to use all the shades. Return `true` if such an assignment exists, and `false` otherwise.

## Example 1

```text
Input: nodeCount = 4, palette = 3, linkCount = 5,
       links = [(0,1), (1,2), (2,3), (3,0), (0,2)]
Output: true
Explanation: Colour node 0 red, node 1 blue, node 2 green, and node 3 blue.
Every link joins two different shades, and only three shades are used.
```

## Example 2

```text
Input: nodeCount = 3, palette = 2, linkCount = 3,
       links = [(0,1), (1,2), (0,2)]
Output: false
Explanation: Nodes 0, 1, and 2 are all linked to each other. Node 0 and node 1
need different shades, so with only two shades node 2 cannot differ from both.
```

## Constraints

* `2 <= nodeCount <= 20`
* `1 <= linkCount <= nodeCount * (nodeCount - 1) / 2`
* `1 <= palette <= nodeCount`
* Each link is a pair of different nodes, and no link appears twice.

## Input Format

The first line contains three integers: `nodeCount`, `palette`, and `linkCount`, separated by spaces.
Each of the next `linkCount` lines contains one link as two integers `u v`.

## Output Format

Print `true` or `false` on a single line.

For Example 2 the input file is:

```text
3 2 3
0 1
1 2
0 2
```

and the output file is:

```text
false
```

## Approach

Assign shades to nodes one at a time, in order from `0`. For the current node, try each shade. A shade is allowed only if no already-coloured neighbour has it. If a node has no allowed shade, step back and change the shade of the previous node (backtracking). If all nodes get a shade, the answer is `true`. If every option is exhausted, the answer is `false`.
