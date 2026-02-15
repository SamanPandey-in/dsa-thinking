# 67. Add Binary

## Description: 

Given two binary strings a and b, return their sum as a binary string.

## Step-by-step plan:

- Given Strings are nothing but binary representations of numbers.
- Testcase: 
    - a = "1010", b = "1011"
    - Align 
                
                1010
            +   1011
    - Col by Col:
        - 0+1=1 ->write=1, carry=0
        - 1+1=2 -> write=0, carry=1
        - 0+0+carry -> write=1, carry=0
        - 1+1=2 -> write=0, carry=1
        - Final carry 1 at front
    - Result: "10101"
    
- In this way, we can start adding from the right, keep appending to a string and then reverse it at the end.

- For this, StringBuilder will be perfect. Seting up i and j pointers to track each string's characters, also a carry variable.

- while either of the strings have a digit we haven't processed OR there's a carry leftover:
    - sum is total sum of the current column:
     carry from before + digit from a (if any) + digit from b (if any).

    - We'll use a beautiful trick here: a.charAt(i--) - '0' to convert character '0' and '1' into numeric values 0 and 1.

    - append sum%2 to string, and set carry to sum/2.

- After all digits are processed, reverse the produced string.

## Code: 
```java
public class Solution {
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() -1, carry = 0;
        while (i >= 0 || j >= 0) {
            int sum = carry;
            if (j >= 0) sum += b.charAt(j--) - '0';
            if (i >= 0) sum += a.charAt(i--) - '0';
            sb.append(sum % 2);
            carry = sum / 2;
        }
        if (carry != 0) sb.append(carry);
        return sb.reverse().toString();
    }
}
```

- Time Complexity: O(m+n) - Same for all cases

- Space Complexity: O(Max(m, n))