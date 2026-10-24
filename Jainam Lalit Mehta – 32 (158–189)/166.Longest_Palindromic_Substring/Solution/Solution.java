public class Solution {

    public String findLongestPalindrome(String text) {

        int n = text.length();
        int bestStart = 0;
        int bestLength = 1;

        for (int center = 0; center < n; center++) {

            int oddLength = expand(text, center, center);
            if (oddLength > bestLength) {
                bestLength = oddLength;
                bestStart = center - (oddLength - 1) / 2;
            }

            int evenLength = expand(text, center, center + 1);
            if (evenLength > bestLength) {
                bestLength = evenLength;
                bestStart = center - (evenLength - 2) / 2;
            }
        }

        return text.substring(bestStart, bestStart + bestLength);
    }

    private int expand(String text, int left, int right) {

        while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
            left--;
            right++;
        }

        return right - left - 1;
    }

}
