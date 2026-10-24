public class Solution {

    public String removeOuterParentheses(String S) {

        StringBuilder result = new StringBuilder();
        int depth = 0;

        for (char ch : S.toCharArray()) {

            if (ch == '(') {
                if (depth > 0) {
                    result.append(ch);
                }
                depth++;
            } else {
                depth--;
                if (depth > 0) {
                    result.append(ch);
                }
            }
        }

        return result.toString();
    }

}

