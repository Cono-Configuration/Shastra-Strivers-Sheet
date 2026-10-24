public class Solution {

    public int beautySum(String text) {

        int n = text.length();
        int total = 0;

        for (int start = 0; start < n; start++) {

            int[] freq = new int[26];

            for (int end = start; end < n; end++) {

                freq[text.charAt(end) - 'a']++;

                int highest = 0;
                int lowest = Integer.MAX_VALUE;

                for (int count : freq) {
                    if (count > 0) {
                        highest = Math.max(highest, count);
                        lowest = Math.min(lowest, count);
                    }
                }

                total += highest - lowest;
            }
        }

        return total;
    }

}
