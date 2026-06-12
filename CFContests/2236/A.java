// Games on the Train
// for every tower, xi = H - hi
// and 1 <= xi <= k
// so hi + 1 <= H <= hi + k
// kmin = max-h - min-h + 1 because the final height must be strictly greater than every tower
import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();

            int minH = Integer.MAX_VALUE;
            int maxH = Integer.MIN_VALUE;

            for (int i = 0; i < n; i++) {
                int h = sc.nextInt();
                minH = Math.min(minH, h);
                maxH = Math.max(maxH, h);
            }

            System.out.println(maxH - minH + 1);
        }

        sc.close();
    }
}