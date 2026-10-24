import java.util.*;

public class Solution {

    public List<List<Integer>> listAllSubsets(int[] elements) {

        int n = elements.length;
        int total = 1 << n;

        List<List<Integer>> result = new ArrayList<>(total);

        for (int mask = 0; mask < total; mask++) {

            List<Integer> current = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    current.add(elements[i]);
                }
            }

            result.add(current);
        }

        return result;
    }

}
