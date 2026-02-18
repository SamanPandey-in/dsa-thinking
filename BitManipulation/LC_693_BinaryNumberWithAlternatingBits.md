# 693. Binary Number with Alternating Bits

## Description: 

Given a positive integer, check whether it has alternating bits: namely, if two adjacent bits will always have different values.

## Step-by-step plan:

- If a number has alternating bits, every adjacent bit is different. (I know it's obvious)

1. Approach 1:
    - When we XOR such a number with itself right shifted by 1, the result beocmes all 1s.

    - And we know that a number with all 1's satisfies a property: **(x & (x+1)) == 0**

    ## Code: 
    ```java
    class Solution {
        public boolean hasAlternatingBits(int n) {
            int x = n ^ (n >> 1);
            return (x & (x + 1)) == 0;
        }
    }
    ```

2. Approach 2:
    - If a number has alternating bits, then no two adjacent bits should both be 1.
      This can be checked using:
      (n & (n >> 1)) == 0
      - If this is true, there are no consecutive 1s.

    - Additionally, for alternating bits, every bit should match the bit two positions ahead.
      This can be verified using:
      (n | (n >> 2)) == n
      - This ensures the alternating pattern continues correctly throughout the number.

    ## Code:
    ```java
    class Solution {
        public boolean hasAlternatingBits(int n) {
            return (n & (n >> 1)) == 0 && (n | (n >> 2)) == n;
        }
    }
    ```


3. Approach 3:
    - First, ensure there are no consecutive 1s, using:
      (n & (n >> 1)) == 0

    - Next, verify that bits spaced two positions apart follow the alternating structure.
      This can be checked using:
      (n & (n >> 2)) == (n >> 2)
      - This guarantees that every bit matches the bit two places to the right,
        maintaining the alternating pattern.

    ## Code:
    ```java
    class Solution {
        public boolean hasAlternatingBits(int n) {
            return (n & (n >> 1)) == 0 && (n & (n >> 2)) == (n >> 2);
        }
    }
    ```

- Time Complexity: O(1) - Same for all cases

- Space Complexity: O(1)