// Friendly Gifts
// subarray is good if the next ele differs to the curr by 1.
// so good subarray of len m looks like `(i, i + 1, ..., i + m - 1)` in some order.
// thus for a subarray of len m to be good, all ele must be distinct and `max - min = m - 1`

// now, after concatination of two food subarryas of len m, concatenated array has len 2m and must also be good
// thus, new subarray must contain `(k, k + 1, ..., k + 2m - 1)` exactly once each..
// since, each chosen subarray already contains m consective ele, so it will only work if the two subarrays are disjoint and one contains `(k, k + 1, ..., k + m - 1)` and other contains `(k + m, k + m + 1, ..., k + 2m - 1)`

// so now how to implement this analysis efficiently?
// for every window of len m: we'll check if its good, How?
// - Maintain a max, and min using monotonic dqeuees
// - Maintain frreq counts to detect duplicates
// - window is good iffff, no duplicates and `max - min = m - 1`
// - if a window is good. and its min is k, then subarry is (k, k + 1, ..., k + m - 1). so we store the start posn of such windows

// now, finding two valid windows
// for every possible min k:
// Need one good window representing [k, ..., k+m-1] And another representing [k+m, ..., k+2m-1] because their value sets must be adjacent.
// Now we only need to ensure the two chosen subarrays do not overlap.
// Instead of storing all positions, we keep:
// - minimum start
// - maximum start
// for each block.

// Then if
// max(
//     maxStart[k] - minStart[k+m],
//     maxStart[k+m] - minStart[k]
// ) >= m
// there exists a non-overlapping pair.

import java.util.*;

public class Solution {
    static boolean check(int[] a, int n, int m) {
        int[] freq = new int[n + 1];

        int INF = 1_000_000_000;
        int[] minStart = new int[n + 2];
        int[] maxStart = new int[n + 2];

        Arrays.fill(minStart, INF);
        Arrays.fill(maxStart, -INF);

        Deque<Integer> minDeque = new ArrayDeque<>();
        Deque<Integer> maxDeque = new ArrayDeque<>();

        int duplicates = 0;

        for (int i = 0; i < n; i++) {
            int v = a[i];

            freq[v]++;
            if (freq[v] == 2) duplicates++;

            while (!minDeque.isEmpty() && a[minDeque.peekLast()] >= v) {
                minDeque.pollLast();
            }
            minDeque.addLast(i);

            while (!maxDeque.isEmpty() && a[maxDeque.peekLast()] <= v) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(i);

            if (i >= m) {
                int oldIdx = i - m;
                int oldVal = a[oldIdx];

                if (freq[oldVal] == 2) duplicates--;
                freq[oldVal]--;

                if (!minDeque.isEmpty() && minDeque.peekFirst() == oldIdx) {
                    minDeque.pollFirst();
                }
                if (!maxDeque.isEmpty() && maxDeque.peekFirst() == oldIdx) {
                    maxDeque.pollFirst();
                }
            }

            if (i >= m - 1) {
                int mn = a[minDeque.peekFirst()];
                int mx = a[maxDeque.peekFirst()];

                if (duplicates == 0 && mx - mn == m - 1) {
                    int start = i - m + 1;

                    minStart[mn] = Math.min(minStart[mn], start);
                    maxStart[mn] = Math.max(maxStart[mn], start);
                }
            }
        }

        for (int k = 1; k + m <= n; k++) {
            if (minStart[k] == INF || minStart[k + m] == INF) continue;

            int bestDiff = Math.max(
                    maxStart[k] - minStart[k + m],
                    maxStart[k + m] - minStart[k]
            );

            if (bestDiff >= m) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            int[] a = new int[n];

            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
            }

            int answer = 0;

            for (int m = n / 2; m >= 1; m--) {
                if (check(a, n, m)) {
                    answer = m;
                    break;
                }
            }

            System.out.println(answer);
        }

        sc.close();
    }
}