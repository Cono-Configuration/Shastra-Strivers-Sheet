import java.util.*;

public class Solution {

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
