# 315. Count of Smaller Numbers After Self

## Description: 

You are given an m x n integer matrix matrix with the following two properties:

Each row is sorted in non-decreasing order.

The first integer of each row is greater than the last integer of the previous row.

Given an integer target, return true if target is in matrix or false otherwise.

You must write a solution in O(log(m * n)) time complexity.

## Step-by-step plan:

### 1. Use Bruteforce: O($n^2$)

- For every num in nums:
    
    - Scan after num till end and maintain a count of smaller numbers
    - Add count to list and done

- TLE,  for a large input this will fail immediately.

```java
class Solution {
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> counts = new ArrayList<>();
        int n = nums.length;
        for(int i = 0; i<n; i++) {
            int key = nums[i];
            int count = 0;
            for(int j = i+1; j<n; j++){
                if(key> nums[j]){
                    count++;
                }
            }
            counts.add(count);
        }
        return counts;
    }
}
```
