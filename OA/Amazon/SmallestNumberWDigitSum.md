# Smallest Number With a given Digit Sum:

**Source and Credits: [Tushar Sharma](https://github.com/TusharSharma77)** 

![Question Image](/OA/assets/SmallestNumberWDigitSum.jpg)

## Approach:

The approach is pretty straightforward. Just do what the question says. 
To get the smallest number:
- Keep the leftmost digits as small as possible.
- Fill digits from right to left with as much value as possible (maximum 9).
- Always reserve at least 1 for the first digit so it never becomes 0.

## Steps/Algorithm:
1. If digitSum > 9 * numberOfDigits, return "-1" (impossible).
2. Reserve 1 for the first digit.
3. Start filling digits from the last position to the second position:
   - Put min(9, remainingSum) in the current position.
   - Put the remaining sum into the first digit.

## Java Implementation:

```java
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int digitSum = sc.nextInt();
        int numberOfDigits = sc.nextInt();

        if (digitSum > 9 * numberOfDigits || digitSum < 1) {
            System.out.println("-1");
            return;
        }

        StringBuilder sb = new StringBuilder();

        // Reserve 1 for the first digit
        digitSum--;

        // Fill from right to left
        for (int i = numberOfDigits - 1; i > 0; i--) {
            int digit = Math.min(9, digitSum);
            sb.append(digit);
            digitSum -= digit;
        }

        // Remaining + reserved 1
        sb.append(digitSum + 1);

        System.out.println(sb.reverse().toString());
    }
}
```

**Time Complexity**: O(n), where n is the number of digits.
**Space Complexity**: O(n), for storing the digits in an array/StringBuilder.