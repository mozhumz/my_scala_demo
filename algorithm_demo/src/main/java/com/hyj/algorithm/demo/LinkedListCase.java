package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 链表操作
 */
public class LinkedListCase {
    class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
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
     *
     * 输入: head = [4,5,1,9], val = 5
     * 输出: [4,1,9]
     * 解释: 给定你链表中值为5的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9.
     *
     * 示例 2:
     * 输入: head = [4,5,1,9], val = 1
     * 输出: [4,5,9]
     * 解释: 给定你链表中值为1的第三个节点，那么在调用了你的函数之后，该链表应变为 4 -> 5 -> 9.
     *
     * 说明：
     * 题目保证链表中节点的值互不相同
     */
    @Test
    public void test20210601(){

    }

    public ListNode deleteNode(ListNode head, int val) {
        if(head==null){
            return null;
        }
        if(head.val==val){
            return head.next;
        }
        ListNode cur=head;
        while (cur.next!=null){
            if(cur.next.val==val){
                cur.next=cur.next.next;
                return head;
            }
            cur=cur.next;
        }

        return head;
    }
}


