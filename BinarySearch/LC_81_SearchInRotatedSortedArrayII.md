# 81. Search in Rotated Sorted Array II

## Description: 

There is an integer array nums sorted in non-decreasing order (not necessarily with distinct values).

Before being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,4,4,5,6,6,7] might be rotated at pivot index 5 and become [4,5,6,6,7,0,1,2,4,4].

Given the array nums after the rotation and an integer target, return true if target is in nums, or false if it is not in nums.

You must decrease the overall operation steps as much as possible.

## Step-by-step plan:

1. Use Linear Search - easy

2. Use Binary Search:

    - Any one side of the mid element has to be sorted

    - Search which side is sorted:

        if left is sorted → check if the target is in left side. If yes - send to left, else right.
        
        if right is sorted → check if the target is in right side. If yes - send to right, else left.

    - Return the mid when found on mid

    - Else return false

## Initial: Using Binary Search

```java

class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target)
                return true;
            // if left is sorted
            if (nums[low] <= nums[mid]) {

                if (nums[mid] > target && nums[low] <= target) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            // if right is sorted
            } else {
                if (nums[mid] < target && nums[high] >= target) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return false;
    }
}
```

- This fails when duplicates exist at mid-low and mid-high at the same time
- To solve it, we'll use a checker function:
```java
if(nums[low]==nums[mid] && nums[mid]==nums[high]){
    low++;
    high--;
    continue;
}
```

## Final: Using Binary Search w/ checker function
```java
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target)
                return true;

            if(nums[low]==nums[mid] && nums[mid]==nums[high]){
                low++;
                high--;
                continue;
            }
            // if left is sorted
            if (nums[low] <= nums[mid]) {

                if (nums[mid] > target && nums[low] <= target) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            // if right is sorted
            } else {
                if (nums[mid] < target && nums[high] >= target) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return false;
    }
}
```

- Time Complexity: O(n)
    - Best: O(1)
    - Average: O(log n)
    - Worst: O(n)
- Space Complexity: O(1)