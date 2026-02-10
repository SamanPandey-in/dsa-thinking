# 278. First Bad Version

## Description:

You are a product manager and currently leading a team to develop a new product. Unfortunately, the latest version of your product fails the quality check. Since each version is developed based on the previous version, all the versions after a bad version are also bad.

You are given an API bool isBadVersion(version) which returns whether version is bad. Implement a function to find the first bad version. You should minimize the number of calls to the API.

## Step-by-step plan:

- Versions are ordered: good -> 1st bad -> bad -> bad ..

- Initialize pointers: l=1 (first version), r=n (last version)

- To ensure we narrow down to one element run lower bound binary search while l<r :

- Check if middle version is bad

    If isBadVersion(m) is true:

    - The first bad version is at m or before

    - Move right pointer → r = m

    Else (isBadVersion(m) is false):

    - First bad version must be after m

    - Move left pointer → l = m + 1

- Repeat until l == r: Both pointers converge on the first bad version

```java
public class Solution extends VersionControl {
    public int firstBadVersion(int n) {
        int l = 1, r = n;

        while (l < r) {
            int m = l + (r - l) / 2;

            if (isBadVersion(m)) {
                r = m;
            } else if (!isBadVersion(m)) {
                l = m + 1;
            }
        }
        return l;
    }
}
```

- Time Complexity: O(log n) - Same for all cases
    - Best: The first bad version is found immediately (very early in the search)
    - Average: The first bad version is somewhere in the middle
    - Worst: The first bad version is at the very end (version n)
- Space Complexity: O(1)