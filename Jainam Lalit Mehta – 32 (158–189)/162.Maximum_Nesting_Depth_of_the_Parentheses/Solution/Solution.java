public class Solution {

    public int maxDepth(String s) {

        int current = 0;
        int maxDepth = 0;

        for (int i = 0; i < s.length(); i++) {

            char ch = s.charAt(i);

            if (ch == '(') {
                current++;
                if (current > maxDepth) {
                    maxDepth = current;
                }
            } else if (ch == ')') {
                current--;
            }
        }

        return maxDepth;
    }

}
