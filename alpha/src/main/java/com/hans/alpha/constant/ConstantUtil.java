package com.hans.alpha.constant;

/**
 * Created by changelcai on 16/6/8.
 */
public class ConstantUtil {


    public final static int DEFAULT_LISTATTR = ConstantUtil.mergeAttr(
            MotherShipConst.TestType.TYPE_1,
            MotherShipConst.TestType.TYPE_2,
            MotherShipConst.TestType.TYPE_3
    );

    public static boolean hasAttr(int types, int attr) {
        return (types & attr) > 0;
    }

    public static int removeAttr(int types, int attr) {
        return types & (~attr);
    }

    public static int mergeAttr(int... attrs) {
        int result = 0;
        if (attrs != null) {
            for (int attr : attrs) {
                result |= attr;
            }
        }
        return result;
    }




}
