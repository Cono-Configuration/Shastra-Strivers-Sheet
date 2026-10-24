import java.util.*;

public class Solution {

    public List<List<String>> splitIntoPalindromes(String inputText) {

        int n = inputText.length();
        boolean[][] isPal = new boolean[n][n];

        for (int right = 0; right < n; right++) {
            for (int left = 0; left <= right; left++) {
                if (inputText.charAt(left) == inputText.charAt(right)
                        && (right - left <= 2 || isPal[left + 1][right - 1])) {
                    isPal[left][right] = true;
                }
            }
        }

        List<List<String>> answer = new ArrayList<>();
        explore(inputText, 0, isPal, new ArrayList<>(), answer);

        return answer;
    }

    private void explore(String inputText, int start, boolean[][] isPal,
                         List<String> current, List<List<String>> answer) {

        if (start == inputText.length()) {
            answer.add(new ArrayList<>(current));
            return;
        }

        for (int end = start; end < inputText.length(); end++) {

            if (isPal[start][end]) {
                current.add(inputText.substring(start, end + 1));
                explore(inputText, end + 1, isPal, current, answer);
                current.remove(current.size() - 1);
            }
        }
    }

}
