package com.hyj.algorithm.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Palindrome {
    /**
     * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
     * 示例 1:
     * 输入: 121
     * 输出: true
     * 示例 2:
     * 输入: -121
     * 输出: false
     * 解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
     */
    @Test
    public void isPalindrome() {
        System.out.println(isPalindrome(1231321));
        System.out.println(isPalindromeNum(1231321));
    }

    public boolean isPalindrome(int num) {
        if (num < 0) {
            return false;
        }

        List<Integer> list = getArrByNum(num);
        int size = list.size();
        if (size == 1) {
            return true;
        }
        int times = size / 2;
        for (int i = 0; i < times; i++) {
            if (!list.get(i).equals(list.get(size - 1 - i))) {
                return false;
            }
        }


        return true;
    }

    private List<Integer> getArrByNum(int num) {
        List<Integer> list = new ArrayList<>();
        int a = num;
        while (true) {
            list.add(a % 10);
            a /= 10;
            if (a == 0) {
                break;
            }
        }
        return list;
    }

    public boolean isPalindromeNum(int num) {
        if (num < 0) {
            return false;
        }
        if (num < 10) {
            return true;
        }
        int revertNum = num % 10;
        num = num / 10;
        while (num > revertNum) {
            revertNum = revertNum * 10 + num % 10;
            num /= 10;
        }

        return num == revertNum || num == revertNum / 10;
    }

    /**
     * 分析：
     * 首先，我们应该处理一些临界情况。所有负数都不可能是回文，例如：-123 不是回文，因为 - 不等于 3。
     * 所以我们可以对所有负数返回 false。除了 0 以外，所有个位是 0 的数字不可能是回文，因为最高位不等于 0。
     * 所以我们可以对所有大于 0 且个位是 0 的数字返回 false。
     * 现在，让我们来考虑如何反转后半部分的数字。
     * 对于数字 1221，如果执行 1221 % 10，我们将得到最后一位数字 1，要得到倒数第二位数字，
     * 我们可以先通过除以 10 把最后一位数字从 1221 中移除，1221 / 10 = 122，再求出上一步结果除以 10 的余数，
     * 122 % 10 = 2，就可以得到倒数第二位数字。如果我们把最后一位数字乘以 10，再加上倒数第二位数字，1 * 10 + 2 = 12，
     * 就得到了我们想要的反转后的数字。如果继续这个过程，我们将得到更多位数的反转数字。
     * 现在的问题是，我们如何知道反转数字的位数已经达到原始数字位数的一半？
     * 由于整个过程我们不断将原始数字除以 10，然后给反转后的数字乘上 10，所以，当原始数字小于或等于反转后的数字时，
     * 就意味着我们已经处理了一半位数的数字了
     *
     * @param num
     * @return
     */
    public boolean isPalindromeNum2(int num) {
        if (num < 0) {
            return false;
        }
        if (num < 10) {
            return true;
        }
        int r = 0;
        while (num > r) {
            r = r * 10 + num % 10;
            num /= 10;
        }

        return num == r || num == r / 10;
    }

    /**
     * 寻找回文串的问题核心思想是：从中间开始向两边扩散来判断回文串。对于最长回文子串，就是这个意思：
     * <p>
     * for 0 <= i < len(s):
     * 找到以 s[i] 为中心的回文串
     * 更新答案
     * 但是呢，我们刚才也说了，回文串的长度可能是奇数也可能是偶数，如果是 abba这种情况，没有一个中心字符，上面的算法就没辙了。
     * 所以我们可以修改一下：
     * <p>
     * for 0 <= i < len(s):
     * 找到以 s[i] 为中心的回文串
     * 找到以 s[i] 和 s[i+1] 为中心的回文串
     * 更新答案
     * PS：读者可能发现这里的索引会越界，等会会处理。
     * <p>
     * 二、代码实现
     * 按照上面的思路，先要实现一个函数来寻找最长回文串，这个函数是有点技巧的：
     * <p>
     * string palindrome(string& s, int l, int r) {
     * // 防止索引越界
     * while (l >= 0 && r < s.size()
     * && s[l] == s[r]) {
     * // 向两边展开
     * l--; r++;
     * }
     * // 返回以 s[l] 和 s[r] 为中心的最长回文串
     * return s.substr(l + 1, r - l - 1);
     * }
     * 为什么要传入两个指针 l 和 r 呢？因为这样实现可以同时处理回文串长度为奇数和偶数的情况：
     * <p>
     * for 0 <= i < len(s):
     * # 找到以 s[i] 为中心的回文串
     * palindrome(s, i, i)
     * # 找到以 s[i] 和 s[i+1] 为中心的回文串
     * palindrome(s, i, i + 1)
     * 更新答案
     *
     * @param s
     * @param l
     * @param r
     * @return
     */
    public String palindromeStr(String s, int l, int r) {
        // 防止索引越界
        while (l >= 0 && r < s.length()
                && s.charAt(l) == s.charAt(r)) {
            // 向两边展开
            l--;
            r++;
        }
        // 返回以 s[l] 和 s[r] 为中心的最长回文串
        return s.substring(l + 1, r);
    }

    public String longestPalindromeStr(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String res = "";
        for (int i = 0; i < str.length(); i++) {
            //从字符串开头查找回文
            //如果结果为奇数
            String s1 = palindromeStr(str, i, i);
            //如果结果为偶数
            String s2 = palindromeStr(str, i, i + 1);
            //比较长度 取长度最大的
            res = res.length() < s1.length() ? s1 : res;
            res = res.length() < s2.length() ? s2 : res;

        }
        return res;
    }


    @Test
    public void longestPalindromeStr() {

        System.out.println(longestPalindromeStr("ecabaec"));

        System.out.println(longestPalindrome("aaaaa"));
    }

    /**
     * 定义dp[i][j]为从下标i到j的子串是否为回文
     * dp[i][j]=dp[i+1][j-1], if s[i]=s[j]
     * 初始值
     * dp[i][i]=true
     * dp[i][i+1]= (s[i]==s[j])
     *注意：在状态转移方程中，我们是从长度较短的字符串向长度较长的字符串进行转移的，因此一定要注意动态规划的循环顺序
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        String ans = "";
        for (int l = 0; l < n; ++l) {
            for (int i = 0; i + l < n; ++i) {
                int j = i + l;
                if (l == 0) {
                    dp[i][j] = true;
                } else if (l == 1) {
                    dp[i][j] = (s.charAt(i) == s.charAt(j));
                } else {
                    dp[i][j] = (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]);
                }
                if (dp[i][j] && l + 1 > ans.length()) {
                    ans = s.substring(i, i + l + 1);
                }
            }
        }
        return ans;
    }


}
