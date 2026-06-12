// Tatar TV Show
// Each move flips/inverts exactly 2 bits, at i and i + k -> so no. of ones changes by -2, 0, 2... Thus parity of number of ones doesnt change
// so for each r in [0, k - 1], the number of 1 bits at positions r, r + k, r + 2k, ... must be even
// if for some r, the number of 1 bits is odd, then it's impossible to make all bits 0

// so each chain must consist even number of 1s..
// eg. n = 7, k = 3, s = 1234567 (posns)
// the posns form these chains: (1, 4, 7), (2, 5), (3, 6)
// so the number of 1s in each chain must be even
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            String s = sc.next();

            boolean ok = true;

            for (int r = 0; r < k; r++) {
                int ones = 0;

                for (int pos = r; pos < n; pos += k) {
                    if (s.charAt(pos) == '1') {
                        ones++;
                    }
                }

                if ((ones & 1) == 1) {
                    ok = false;
                    break;
                }
            }

            System.out.println(ok ? "YES" : "NO");
        }

        sc.close();
    }
}