package com.it.aaron.codeflow.test;

class Solution {
    public static int trap(int[] height) {
        int res = 0;
        int len = height.length;
        int[] leftMax = new int[len];
        int[] rightMax = new int[len];
        leftMax[0] = height[0];
        rightMax[len-1] = height[len -1];
        for (int i = 1; i < len; i++) {
            leftMax[i] = Math.max(height[i], leftMax[i-1]);
        }
        for (int i = len - 2; i >= 0;i--) {
            rightMax[i] = Math.max(height[i], rightMax[i+1]);
        }
        for (int i = 1; i < len; i++) {
            int area = Math.min(leftMax[i], rightMax[i]) - height[i];
            res += area;
        }
        return res;
    }

    public static void main(String[] args) {
        trap(new int[]{4,2,0,3,2,5});
    }
}