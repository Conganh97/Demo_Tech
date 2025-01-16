package com.demo.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class RemoveRedundant {
    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 3};
        log.info(String.valueOf(removeDuplicates(nums)));
        log.info(Arrays.toString(nums));
    }

    public static int removeDuplicates(int[] nums) {
        int count = 0, current = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                count = 0;
                nums[current++] = nums[i];
            } else {
                count++;
                if (count <= 1) nums[current++] = nums[i];
            }
        }
        return current;
    }
}
