# 3849. Maximum Bitwise XOR After Rearrangement

## Description:

You are given two binary strings s and t​​​​​​​, each of length n.

You may rearrange the characters of t in any order, but s must remain unchanged.

Return a binary string of length n representing the maximum integer value obtainable by taking the bitwise XOR of s and rearranged t.

## Step-by-step plan: Use Greedy

- Since we can freely rearrange t, we just need to track how many '0's and '1's are available in t, then greedily assign them left-to-right:

- If s[i] = '1' → we prefer a '0' from t (makes XOR bit = 1)
- If s[i] = '0' → we prefer a '1' from t (makes XOR bit = 1)
- If preferred character is exhausted, use the other one (XOR bit = 0)

- Initialize a count for zeros and ones present in t (for checking availability of bits while rearranging).
- For every bit in s from left to right:
    - If it is '0': check if '1' is available in r to rearrange -> if yes, add 1, else add 0. Decrement counter.
    - Else if it is '1': check if '0' is available in r to rearrange -> if yes, add 1, else add 0. Decrement counter.
- Note: we are storing the final XORed answer in the string, not the r string.

```java
class Solution {
    public String maximumXor(String s, String t) {
        // s must be same, r can change
        // ans = bin string s.t max value of int val by taking XOR of s and new r

        // same digits = 0. opp digits = 1
        // rearagne t such that-> count 0s and 1s in t
        // for each posn in s, if s[i]=1, use 0 in t, s[i]=0 use 1

        int n = s.length();

        int ones = 0;

        for (char c : t.toCharArray()) {
            if (c == '1') ones++;
        }
        int zeros = n - ones;

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);

            if (ch == '0') {
                if (ones > 0) {
                    ans.append('1');
                    ones--;
                } else {
                    ans.append('0');
                    zeros--;
                }
            } else {
                if (zeros > 0) {
                    ans.append('1');
                    zeros--;
                } else {
                    ans.append('0');
                    ones--;
                }
            }
        }
        return ans.toString();
    }
}
```

- Time Complexity: O(n) - Same for all cases
- Space Complexity: O(1)