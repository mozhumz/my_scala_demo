package com.hyj.algorithm.demo.niuke;
import java.util.Scanner;
import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String line=in.nextLine();
            String[]arr=line.split(" ");
            int n=Integer.parseInt(arr[0]);
            int len=arr.length;
            int k=Integer.parseInt(arr[len-1]);
            int start=1;
            int wlen=len-3;
            String x=arr[len-2];
            //存储符合的兄弟单词
            ArrayList<String> list =new ArrayList();
            for(int i=start;i<start+wlen;i++){
                if(check(x,arr[i])){
                    list.add(arr[i]);
                }
            }
            Collections.sort(list);
            if(!list.isEmpty()){
                System.out.println(list.size());
                System.out.println(list.get(k-1));
            }

        }
    }
    //判断是否是兄弟单词
    public static boolean check(String x,String word){
        if(x.equals(word)||x.length()!=word.length()){
            return false;
        }
        Map<Character,Integer> wMap=new HashMap();
        setMapByStr(wMap,word);

        for(char ch:x.toCharArray()){
            int count= wMap.getOrDefault(ch,0);
            if(count==0){
                return false;
            }
            wMap.put(ch,--count);

        }

        return true;
    }

    public static void setMapByStr(Map<Character,Integer> xMap,String x){
        for(char ch:x.toCharArray()){
            int c=xMap.getOrDefault(ch,0);
            xMap.put(ch,++c);
        }
    }
}
//import java.util.Scanner;
//
///**
// * 查找两个字符串a,b中的最长公共子串
// * @author Cshuang
// *https://segmentfault.com/a/1190000007963594
// */
//public class Main {
//    public static void main(String[] args) {
////        Scanner in = new Scanner(System.in);
////        while (in.hasNextLine()){
////        }
//        String str1="abcdef";
//        String str2="cdof";
//        System.out.println(lonComSub(str1,str2));
//    }
//
//    private static String lonComSub(String a, String b) {
//        int aLen=a.length()+1;
//        int bLen=b.length()+1;//子所以要限制长度+1，方便c[0][0]
//        int end=0;
//        int max=0;
//        if(aLen>bLen){//将较短的字符串放在前面
//            int temp;
//            temp=aLen;
//            aLen=bLen;
//            bLen=temp;
//
//            String s;
//            s=a;
//            a=b;
//            b=s;
//        }
//        //事实上真正计数的从c[1][1]开始
//        int[][] c=new int[aLen][bLen];
//
//        for (int i = 1; i < aLen; i++) {
//            for (int j = 1; j < bLen; j++) {
//                if(a.charAt(i-1)==b.charAt(j-1)){
//                    c[i][j] = c[i-1][j-1]+1;
//                }else{
//                    c[i][j]=0;
//                }
//                if(c[i][j]>max){
//                    end=i;
//                    max=c[i][j];
//                }
//            }
//        }
//        return a.substring(end-max,end);
//    }
//}

// 注意类名必须为 Main, 不要有任何 package xxx 信息
//public class Main {
//    public static void main(String[] args) {
////        Scanner in = new Scanner(System.in);
////        // 注意 hasNext 和 hasNextLine 的区别
////        while (in.hasNextLine()) { // 注意 while 处理多个 case
////            String str1=in.nextLine();
////            String str2=in.nextLine();
////            int len1=str1.length();
////            int len2=str2.length();
////            if(len1>len2){
////                getMaxSubStr(str2,str1,len2,len1);
////            } else{
////                getMaxSubStr(str1,str2,len1,len2);
////            }
////
////        }
//
//        String str="ab9abc";
//        String sub="abc";
//        System.out.println(containStr(str,sub));
//    }
//
//    public static String getMaxSubStr(String minStr,String maxStr,
//                                      int minLen,int maxLen){
//        //长度分别从len到1
//        for(int i=0;i<minLen;i++){
//            //长度为minLen-i时 找到每种子串的组合
//            for(int j=0;j<i+1;j++){
//                String subStr=minStr.substring(j,j+minLen-i);
//                if(containStr(maxStr,subStr)){
//                    System.out.println(subStr);
//                    return subStr;
//                }
//            }
//        }
//
//        return null;
//    }
//
//    //判断是否包含子串
//    public static boolean containStr(String str,String subStr){
//        char[]strs=str.toCharArray();
//        int subLen=subStr.length();
//        for(int i=0;i<=strs.length-subLen;i++){
//            if(strs[i]==subStr.charAt(0)){
//                boolean flag=true;
//                for(int j=1;j<subLen&&i+j<strs.length;j++){
//                    if(strs[i+j]!=subStr.charAt(j)){
//                        flag=false;
//                        break;
//                    }
//                }
//                if(flag){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
