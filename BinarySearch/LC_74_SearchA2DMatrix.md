# 74. Search a 2D Matrix

## Description: 

You are given an m x n integer matrix matrix with the following two properties:

Each row is sorted in non-decreasing order.

The first integer of each row is greater than the last integer of the previous row.

Given an integer target, return true if target is in matrix or false otherwise.

You must write a solution in O(log(m * n)) time complexity.

## Step-by-step plan:

### 1. Use Bruteforce: O(m*n)

- Check every single element in the matrix until you find the target.
- Worst case: target is not present → you scan all m * n elements.
- Best case: target is the first element → O(1).

```java
public boolean searchMatrix(int[][] matrix, int target) {
    for(int r=0; r<matrix.length; r++){
        for(int c=0; c<matrix[r].length; c++){
            if(matrix[r][c]==target) return true;
        }
    }
    return false;
}
```

### 2. Per row linear search: O(m+n)

- Because:
    Each row is sorted and the first element of each row is greater than the last element of the previous row.

- Use the matrix properties to:

    First determine which row could contain the target.

    Then linearly search inside that row.

- Worst case: O(m + n), but technically could still approach O(m*n) depending on how row selection is implemented.

```java
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;
    for (int i = 0; i < m; i++) {
        if (matrix[i][0] <= target && (i == m - 1 || matrix[i + 1][0] > target)) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
            return false;
        }
    }
    return false;
}
```

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