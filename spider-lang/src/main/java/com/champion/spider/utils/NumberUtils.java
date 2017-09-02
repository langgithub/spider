package com.champion.spider.utils;

/**
 * @author xulixiang@champion-credit.com
 * @version 1.0.0
 * @date 2016.5.31
 */
public abstract class NumberUtils {

    public static int compareLong(long o1, long o2) {
        if (o1 < o2) {
            return -1;
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }
}
