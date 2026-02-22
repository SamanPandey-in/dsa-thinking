# 868. Binary Gap

## Description: 

Given a positive integer n, find and return the longest distance between any two adjacent 1's in the binary representation of n. If there are no two adjacent 1's, return 0.

Two 1's are adjacent if there are only 0's separating them (possibly no 0's). The distance between two 1's is the absolute difference between their bit positions. For example, the two 1's in "1001" have a distance of 3.

## Step-by-step plan:

- The maximum distance between two consecutive `1`s in its binary representation.
- Distance is counted as: number of zeros between them + 1
- If the number contains fewer than two `1`s → answer is `0`.

1. Convert integer `n` to its binary representation.
2. Traverse the binary string from left to right.
3. Count zeros between consecutive `1`s.
4. Track the maximum distance found.

```java
class Solution {
    public int binaryGap(int n) {
        String bin = Integer.toBinaryString(n);
        if (bin.indexOf('1') == bin.lastIndexOf('1')) return 0;

        int zeros = 0;
        int maxGap = 0;

        for(char c : bin.toCharArray()) {
            if (c=='0') zeros++;
            else if (c == '1') {
                maxGap = Math.max(maxGap, zeros+1);
                zeros = 0;
            }
        }
        return maxGap;
        
    }
}
```

- Time Complexity: O(n) - Same for all cases

- Space Complexity: O(n)