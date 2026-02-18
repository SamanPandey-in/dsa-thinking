# 190. Reverse Bits  

## Description:  

Reverse bits of a given 32-bit unsigned integer.

You are given a 32-bit integer `n`.  
Return the integer obtained after reversing its binary bits.

---

## Step-by-step plan:

### Bit-by-bit reversal (Iterative): O(1)

- Since an integer has exactly 32 bits, we loop exactly 32 times.
- In each iteration:
  
  1. Extract the last (least significant) bit:
     - bit = n & 1
  
  2. Shift result left to make space:
     - result << 1
  
  3. Add extracted bit into result:
     - result = (result << 1) | bit
  
  4. Unsigned right shift n:
     - n = n >>> 1  (This ensures zero is shifted from the left.)

- After 32 iterations, all bits are reversed.

- Time Complexity: O(32) → O(1)
- Space Complexity: O(1)

---

## Code:

```java
public class Solution {
    public int reverseBits(int n) {
        int result = 0;

        for (int i = 0; i < 32; i++) {

            int bit = n & 1;               // Extract the least significant bit

            result = (result << 1) | bit;  // Append the bit to result

            n = n >>> 1;                  // Unsigned right shift
        }

        return result;
    }
}
