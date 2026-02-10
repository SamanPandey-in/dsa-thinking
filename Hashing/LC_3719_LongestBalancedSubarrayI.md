# 3719. Longest Balanced Subarray I

## Description: 

You are given an integer array nums.

A subarray is called balanced if the number of distinct even numbers in the subarray is equal to the number of distinct odd numbers.

Return the length of the longest balanced subarray.

## Step-by-step plan:

- Loop over every possible starting index i

- Create two hash sets:

    one for distinct even numbers
    
    one for distinct odd numbers
    
    Expand the subarray from i to j

- For each new element:

    if it’s even → add to even set
    
    if it’s odd → add to odd set
    
    If both sets have the same size, update the answer with the subarray length (j - i + 1)

- Return the maximum length found

## Code: 
```java
class Solution {
    public int longestBalanced(int[] nums) {
        int n = nums.length;
        Set<Integer> odd = new HashSet<>();
        Set<Integer> even = new HashSet<>();
        int max = 0;
        for (int i = 0; i < n; i++) {
            odd.clear();
            even.clear();

            for (int j = i; j < n; j++) {
                int num = nums[j];
                if ((num & 1) == 1)
                    odd.add(num);
                else
                    even.add(num);

                if (odd.size() == even.size())
                    max = Math.max(max, j - i + 1);
            }
        }
        return max;
    }
}
```

- Time Complexity: O($n^2$) - Same for all cases

    - The outer loop runs n times

    - For each i, the inner loop can run up to n - i times

    - All subarrays are fully explored

    - No early termination is possible because: A longer balanced subarray might appear later

    - Worst-case scenario: Any input (even all identical numbers) still forces full traversal.
- Space Complexity: O(n)