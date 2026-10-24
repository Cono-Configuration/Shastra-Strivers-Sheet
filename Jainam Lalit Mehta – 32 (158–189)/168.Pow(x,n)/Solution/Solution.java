public class Solution {

    public double raisePower(double base, int exp) {

        long power = exp;

        if (power < 0) {
            base = 1.0 / base;
            power = -power;
        }

        double result = 1.0;

        while (power > 0) {

            if ((power & 1) == 1) {
                result *= base;
            }

            base *= base;
            power >>= 1;
        }

        return result;
    }

}
