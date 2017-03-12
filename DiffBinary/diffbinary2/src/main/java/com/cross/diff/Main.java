package com.cross.diff;

/**
 * @author czl 2017/2/18
 * @Package com.cross.diff
 * @Title: Main
 * @Description: (测试类)
 * Create DateTime: 2017/2/18
 */

public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("please invoke this by old apk path, new apk path and patch path as three arguments");
            System.exit(0);
        }

        DiffHelper.startDiff(args[0], args[1], args[2]);
    }

}

