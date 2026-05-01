# 1134. Armstrong Number

## Description:

Given an integer N, return true it is an Armstrong number otherwise return false.

An Amrstrong number is a number that is equal to the sum of its own digits each raised to the power of the number of digits.

## Step-by-step plan: 

- Calculate the number of digits in the given number and store it in a variable `n`.
- Initialize a variable `sum` to 0 to keep track of the sum of the digits raised to the power of `n`.
- Create a temporary variable `temp` and assign it the value of the given number.
- Use a while loop to iterate through each digit of the number:
    - Extract the last digit of `temp` using the modulus operator and store it in a variable `digit`.
    - Add the value of `digit` raised to the power of `n` to `sum`.
    - Remove the last digit from `temp` by performing integer division by 10.
- After the loop, compare `sum` with the original number. If they are equal, return true; otherwise, return false.

## Code Implementation:
```java
class Solution {
    public boolean isArmStrong(int num) {
        int n = String.valueOf(num).length();
        int sum = 0;

        int temp = num;
        while (temp > 0) {
            int digit = temp % 10;
            sum += Math.pow(digit, n);
            temp /= 10;
        }
        return sum == num;
    }
}
```

- Time Complexity: O(log10(n) + 1) 
- The number of digits in the number is proportional to log10(n), and we perform a constant amount of work for each digit.
- Space Complexity: O(1)