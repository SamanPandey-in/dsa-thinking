# 61. Rotate List

## Description: 

Given the head of a linked list, rotate the list to the right by k places.

## Step-by-step plan:

1. First, we need to find the length of the linked list and connect the tail to the head to make it a cycle.
2. Then, we will find the new tail by moving `len - k % len` steps from the head.
3. Finally, we will break the cycle by setting the next of the new tail to null and return the new head.

## Code Implementation:
```java
class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if(head==null || k==0) return head;

        ListNode temp=head;
        int len=1;
        while(temp.next!=null){
            temp=temp.next;
            len++;
        }// reached last node

        temp.next=head;
        k = k % len; // If k is greater than len, rotate only k % len times
        if (k == 0) {
            temp.next=null;
            return head;
        }

        //finding new tail
        ListNode newTail=head;
        for (int i = 1; i < len - k; i++) {
            newTail = newTail.next;
        }

        //disconnect
        ListNode newHead=newTail.next;
        newTail.next=null;
        return newHead;
    }
}
```

- Time Complexity: O(n) for all cases
- Space Complexity: O(1)