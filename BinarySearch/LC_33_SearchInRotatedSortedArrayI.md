# 33. Search in Rotated Sorted Array I

## Description:

There is an integer array nums sorted in ascending order (with distinct values).

Prior to being passed to your function, nums is possibly left rotated at an unknown index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be left rotated by 3 indices and become [4,5,6,7,0,1,2].

Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.

You must write an algorithm with O(log n) runtime complexity.

## Step-by-step plan:

1. Use Linear Search - easy

2. Use Binary Search:
   - Any one side of the mid element has to be sorted

   - Search which side is sorted:

     if left is sorted → check if the target is in left side. If yes - send to left, else right.

     if right is sorted → check if the target is in right side. If yes - send to right, else left.

   - Return the mid when found on mid

   - Else return false

```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length-1;

        // koi ek part will be sorted, either left or right
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target)
                return mid;
            //if left is sorted
            if (nums[low] <= nums[mid]) {
                // if ele is in left part
                if (nums[mid] > target && nums[low] <= target) {
                    high = mid - 1;
                } else { //send to right
                    low = mid + 1;
                }
            }

            // if right is sorted
            else {
                // check if ele is in right part
                if (nums[mid] < target && nums[high] >= target) {
                    low = mid + 1;
                } else { //send to left
                    high = mid - 1;
                }
            }
        }
        return -1;
    }
}
```

- Time Complexity: O(log n)
    - Best: O(1)
    - Average: O(log n)
    - Worst: O(log n)
- Space Complexity: O(1)