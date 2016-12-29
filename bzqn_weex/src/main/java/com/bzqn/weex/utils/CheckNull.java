package com.bzqn.weex.utils;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by 陈红建 on 2016/7/4.
 */
public class CheckNull {


    /**
     * 检查一个int是否为0
     * @param params
     * @return
     */
    public static boolean checkZero(int params){
        return params == 0;
    }
    /**
     * 检查一个集合是否是有效的
     * @param list
     * @return
     */
    public static boolean checkListEmpty(List list){
        if (list != null && list.size() > 0){
            return false;
        }
        return true;
    }

    /**
     * 检查一系列字符串,全都不为null return false
     * @param strings
     * @return
     */
    public static boolean checkNullWithAnd(String... strings) {
        for (String str: strings){
            if (TextUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一系列对象是否都不为NULL
     * @param objects
     * @return
     */
    public static boolean checkNullWithAnd(Object... objects) {
        for (Object obj: objects){
            if (obj == null){
                return true;
            }
        }
        return false;
    }

}
