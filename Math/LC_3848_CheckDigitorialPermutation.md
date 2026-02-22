# 3848. Check Digitorial Permutation

## Description:

You are given two binary strings s and t​​​​​​​, each of length n.

You may rearrange the characters of t in any order, but s must remain unchanged.

Return a binary string of length n representing the maximum integer value obtainable by taking the bitwise XOR of s and rearranged t.

## Strategy:

- Instead of directly checking the original number, we:
    1. Generate all permutations of its digits
    2. For each valid permutation:
        - Form the number
        - Compute the sum of factorials of its digits
        - Compare both values
- If any permutation satisfies the condition -> return true. Else -> return false.

## Step-by-step plan: Use Permutation Enumeration + Math

- We compute factorials for digits `0-9` once.
- Convert number to Sorted Digit Array so that we generate permutations in lexicographical order and we can use the standard `nextPermutation` technique.
- The classic **Next Permutation Algorithm**:
    - Find first decreasing index from right.
    - Find just larger element.
    - Swap.
    - Reverse suffix.
    - This guarantees visiting each permutation exactly once.
- For each valid permutation:
    - Skip if leading digit is `'0'`
    - Build the integer
    - Compute: sum += factorial[digit]
    - If `sum == number` → return `true`

```java
class Solution {
    public boolean isDigitorialPermutation(int n) {
        //sum of fact of didigits = number
        // if permutation a digitiriol return ture, else false
        
        //store facts of nums 0-9
        int[] fact = new int[10];
        fact[0] = 1;
        for(int i = 1; i <= 9; i++) fact[i] = fact[i-1] * i;

        //convert no to digits arr
        char[] digits = String.valueOf(n).toCharArray();
        Arrays.sort(digits);
        boolean hasNext = true;
        while (hasNext) {
            if (digits[0] != '0') {
                int num = 0;
                int sum = 0;

                for (char c : digits) {
                    int d = c - '0';
                    num = num * 10 + d;
                    sum += fact[d];
                }

                if (sum == num) return true;
            }
            hasNext = nextPermutation(digits);
        }
        return false;
    }
    private boolean nextPermutation(char[] arr) {
        int i = arr.length - 2;
        while (i >= 0 && arr[i] >= arr[i+1]) i--;
        if (i<0) return false;

        int j = arr.length - 1;
        while (arr[j] <= arr[i]) j--;

        swap(arr, i, j);
        reverse(arr, i + 1);
        return true;
    }
    private void swap(char[] arr, int i, int j){
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    private void reverse(char[] arr, int start) {
        int end = arr.length - 1;
        while (start < end) swap(arr, start++, end--);
    }
}
```

- Time Complexity: O(n) - Same for all cases
- Space Complexity: O(1)