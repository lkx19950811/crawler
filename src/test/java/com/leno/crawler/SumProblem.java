package com.leno.crawler;

import org.junit.Test;

import java.util.*;

/**
 * @author leon
 * @date 2018-07-10 10:46
 * @desc
 */
public class SumProblem {
    @Test
    public void testThreeSum(){
        int[] temp = createNewInt(10000);
        System.out.println(threeSum(temp,1500));
    }

    /**
     * 找出3个数相加为源值的组合
     * @param nums 数组
     * @param target 源值
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums,int target) {
        List<List<Integer>> ret = new ArrayList<>();

        if (nums == null || nums.length < 3)
            return ret;
        int len = nums.length;
        Arrays.sort(nums);
        // 注意，对于 num[i]，寻找另外两个数时，只要从 i+1 开始找就可以了。
        // 这种写法，可以避免结果集中有重复，因为数组是排好序的，
        //所以当一个数被放到结果集中的时候，其后面和它相等的直接被跳过。
        for (int i = 0; i < len; i++) {
            // 避免重复！！！！
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            // 往后找，避免重复
            int begin = i + 1;
            int end = len - 1;
            while (begin < end) {
                int sum = nums[i] + nums[begin] + nums[end];
                if (sum == target) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[begin]);
                    list.add(nums[end]);
                    ret.add(list);
                    begin++;
                    end--;
                    // 避免重复！！！！
                    while (begin < end && nums[begin] == nums[begin - 1])
                        begin++;
                    while (begin < end && nums[end] == nums[end + 1])
                        end--;
                } else if (sum > 0)
                    end--;
                else
                    begin++;
            }
        }
        return ret;
    }

    /**
     * 产生随机int数组
     * @param size
     * @return
     */
    public int[] createNewInt(int size){
        int[] temp = new int[size];
        for (int i=0;i<size;i++){
            temp[i] = new Random().nextInt(100000);
        }
        return temp;
    }

}
