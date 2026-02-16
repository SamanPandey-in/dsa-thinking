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

- TLE, for a large input this will fail immediately.

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

### 2. Use Binary Search: O($n^2$)

- But the array is not sorted, and if we sort it, we lose the relative positioning required by the problem.

- Process from left to right: This allows us to maintain a sorted list of numbers already processed which are to right of current number in original number.

- Binary Search Insertion: for each num, use binary search to find the correct position where it would be inserted in a sorted list. (same as done in LC #35. Search Insert Position) Binary search gives us l which means: there are exactly l numbers smaller than num.

- Since we are processing from right to left, the list is built in reverse, so reverse the list.

```java
class Solution {
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> counts = new ArrayList<>();
        List<Integer> sorted = new ArrayList<>();

        for (int i = nums.length - 1; i >= 0; i--) {
            int idx = binS(sorted, nums[i]);
            counts.add(idx);
            sorted.add(idx, nums[i]);
        }

        Collections.reverse(counts);
        return counts;
    }

    private int binS(List<Integer> sorted, int target) {
        int l = 0;
        int r = sorted.size() - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;
            if (sorted.get(m) < target)
                l = m + 1;
            else
                r = m - 1;
        }

        return l;
    }
}
```

- Time Complexity: O($n^2$)

    - Binary Search: O(log n)
    - List Insertion: O(n)

- But there is still a trap, in worst case the overall complexity will be O($n^2$)

### 3. Per row binary search: O(m+log n)

- Because:
    Each row is sorted and the first element of each row is greater than the last element of the previous row.

- Use the matrix properties to:

    Find the correct row.

    Instead of linear search inside the row → use binary search.

```java
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;
    for (int i = 0; i < m; i++) {
        if (matrix[i][0] <= target && (i == m - 1 || matrix[i + 1][0] > target)) {
            int l = 0, r = n - 1;
            while (l <= r) {
                int mid = l + (r - l) / 2;

                if (matrix[i][mid] == target)
                    return true;
                else if (matrix[i][mid] < target)
                    l = mid + 1;
                else
                    r = mid - 1;
            }
            return false;
        }
    }
    return false;
}
```

### 4. One pass binary search: O(log m*n)
    
- Because:

    Each row is sorted.

    First element of each row > last element of previous row.

    The matrix behaves exactly like a single sorted 1D array.

- Using linked list style, as the up and down rows are sorted sequentially

```java
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length; //rows
    int n = matrix[0].length; //cols

    int left = 0;
    int right = m * n - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        int row = mid / n;
        int col = mid % n;

        if (matrix[row][col] == target) {
            return true;
        } else if (matrix[row][col] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return false;
}
```