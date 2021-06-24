package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 链表操作
 */
public class LinkedListCase {
    class ListNode {
        int val;
        ListNode next;


        ListNode(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(val);
            ListNode tmp = next;
            while (tmp != null) {
                sb.append("->").append(tmp.val);
                tmp = tmp.next;
            }
            return sb.toString();
        }
    }

    /**
     * 给你一个链表的头节点 head 和一个整数 val ，请你删除链表中所有满足 Node.val == val 的节点，并返回 新的头节点 。
     */
    @Test
    public void testRemove() {
        ListNode head = new ListNode(0);
        ListNode tmp = head;
        for (int i = 0; i < 10; i++) {
            int j = i;
            if (j < 2 || j == 3 || j == 9) {
                j = 0;
            }

            ListNode listNode = new ListNode(j);
            tmp.next = listNode;
            tmp = listNode;

        }
        List list = getListByListNode(head);
        System.out.println(list);
        ListNode nHead = removeElements(head, 0);
        List list2 = getListByListNode(nHead);
        System.out.println("list2:" + list2);
    }

    private List getListByListNode(ListNode head) {
        List list = new ArrayList();
        ListNode cur = head;
        while (cur != null) {
            list.add(cur.val);
            cur = cur.next;
        }
        return list;
    }

    public ListNode removeElements(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;
        ListNode cur = dummyNode;
        while (cur.next != null) {
            if (cur.next.val == val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }

        return dummyNode.next;
    }

    /**
     * 剑指 Offer 18. 删除链表的节点
     * 给定单向链表的头指针和一个要删除的节点的值，定义一个函数删除该节点。
     * 返回删除后的链表的头节点
     * 示例 1:
     * <p>
     * 输入: head = [4,5,1,9], val = 5
     * 输出: [4,1,9]
     * 解释: 给定你链表中值为5的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9.
     * <p>
     * 示例 2:
     * 输入: head = [4,5,1,9], val = 1
     * 输出: [4,5,9]
     * 解释: 给定你链表中值为1的第三个节点，那么在调用了你的函数之后，该链表应变为 4 -> 5 -> 9.
     * <p>
     * 说明：
     * 题目保证链表中节点的值互不相同
     */
    @Test
    public void test20210601() {

    }

    public ListNode deleteNode(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        if (head.val == val) {
            return head.next;
        }
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.next.val == val) {
                cur.next = cur.next.next;
                return head;
            }
            cur = cur.next;
        }

        return head;
    }


    /**
     * 剑指 Offer 22. 链表中倒数第k个节点
     * 输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
     * <p>
     * 例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。
     * 示例：
     * 给定一个链表: 1->2->3->4->5, 和 k = 2.
     * 返回链表 4->5
     * <p>
     * 分析：
     * 初始化： 前指针 former 、后指针 latter ，双指针都指向头节点 head 。
     * 构建双指针距离： 前指针 former 先向前走 k 步（结束后，双指针 former 和 latter 间相距 k 步）。
     * 双指针共同移动： 循环中，双指针 former 和 latter 每轮都向前走一步，直至 former 走过链表 尾节点 时跳出
     * （跳出后， latter 与尾节点距离为 k-1，即 latter 指向倒数第 k 个节点）。
     * 返回值： 返回 latter 即可
     */
    @Test
    public void test202106201444() {

    }

    public ListNode getKthFromEnd(ListNode head, int k) {
        if (k <= 0) {
            return null;
        }
        ListNode former = head, latter = head;
        //前指针 former 先向前走 k 步
        for (int i = 0; i < k; i++) {
            former = former.next;
            //k>size的情况
            if (i < k - 1 && former == null) {
                return null;
            }
        }

        //双指针共同移动
        while (former != null) {
            former = former.next;
            latter = latter.next;
        }
        return latter;
    }

    public ListNode getKthFromEnd2(ListNode head, int k) {
        if (k <= 0) {
            return null;
        }
        List<ListNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        if (k > list.size()) {
            return null;
        }
        return list.get(list.size() - k);
    }

    /**
     * 剑指 Offer 24. 反转链表
     * 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
     * 示例:
     * 输入: 1->2->3->4->5->NULL
     * 输出: 5->4->3->2->1->NULL
     *
     * 分析：
     * 在遍历链表时，将当前节点的 next 指针改为指向前一个节点。
     * 由于节点没有引用其前一个节点，因此必须事先存储其前一个节点。
     * 在更改引用之前，还需要存储后一个节点。
     * 最后返回新的头引用
     *
     */
    @Test
    public void test202106241217() {
        ListNode head = buildNode(5);
        System.out.println(head);
        System.out.println(reverseList2(head));
    }

    public ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public ListNode reverseList2(ListNode head) {
        if (head == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        ListNode res = null;
        if (!list.isEmpty()) {
            res = new ListNode(list.get(list.size() - 1));
            ListNode tmp = res;
            for (int i = list.size() - 2; i >= 0; i--) {
                tmp.next = new ListNode(list.get(i));
                tmp = tmp.next;

            }
        }
        return res;
    }

    public ListNode buildNode(int n) {
        ListNode head = null;
        if (n < 1) {
            return head;
        }
        head = new ListNode(1);
        ListNode tmp = head;
        for (int i = 2; i <= n; i++) {
            tmp.next = new ListNode(i);
            tmp = tmp.next;
        }

        return head;
    }


}


