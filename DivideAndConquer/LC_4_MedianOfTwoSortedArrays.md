# 4. Median of Two Sorted Arrays  

## Description:  

Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.

The overall run time complexity should be O(log (m+n)).

## Step-by-step plan:

### 1. Combine + Sort (Bruteforce): O((m+n) log (m+n))

- Combine both arrays into a single array of size m + n.
- Sort the combined array.
- Find the median from the sorted array.

- Time Complexity:
  - Copying: O(m + n)
  - Sorting: O((m + n) log (m + n))
  - Overall: O((m+n) log (m+n))

- Space Complexity: O(m + n)

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //Getting size
        int n = nums1.length;
        int m = nums2.length;

        //combining arrays
        int[] comb = new int[n + m];
        int p = 0;
        for (int i = 0; i < n; i++) {
            comb[p++] = nums1[i];
        }
        for (int i = 0; i < m; i++) {
            comb[p++] = nums2[i];
        }

        //sorting array
        Arrays.sort(comb);
        int len = comb.length; //len of sorted arr

        //if even len, give avg of mid two ele
        //if odd len, give mid ele
        if (len % 2 == 0) {
            int mid1 = comb[len / 2 - 1];
            int mid2 = comb[len / 2];
            return (mid1 + mid2) / 2.0;
        } else {
            return (double) comb[len / 2];
        }
    }
}
```

### 2. Two Pointer Merge (Optimized Linear): O(m+n)

- Since both arrays are already sorted:
    - Use two pointers i and j.
    - Merge elements like in Merge Sort.
    - Stop once we reach the middle of the merged array.
- Keep track of the last two elements while merging.
- Compute median directly without storing full merged array.

- Time Complexity: O(m + n)

- Space Complexity: O(1)

```java
public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int i = 0, j = 0;
        int median1 = 0, median2 = 0;

        for (int count = 0; count < (len1 + len2) / 2 + 1; count++) {
            median2 = median1;

            if (i < len1 && j < len2) {
                if (nums1[i] > nums2[j]) {
                    median1 = nums2[j];
                    j++;
                } else {
                    median1 = nums1[i];
                    i++;
                }
            } 
            else if (i < len1) {
                median1 = nums1[i];
                i++;
            } 
            else {
                median1 = nums2[j];
                j++;
            }
        }

        if ((len1 + len2) % 2 == 1) {
            return (double) median1;
        } else {
            return (median1 + median2) / 2.0;
        }
    }
}
```
