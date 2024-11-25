package com.junyang.utils;

import java.math.BigDecimal;

/**
 * 避免double运算丢失精度
 */
public class DoubleUtil {
    /**
     * 加法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static double addDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).doubleValue();
    }

    public static void main(String[] args) {
        double number1 = 1;
        double number2 = 20.2;
        double number3 = 300.03;
        double result =  number2 + number3;
        System.out.println(result);

        double value =addDoubleArr(number2,number3);
        System.out.printf("\n"+ value);
        double value2 =addDoubleArr(number3);
        System.out.printf("\n"+ value2);
        double value3 =addDoubleArr(0,number1);
        System.out.printf("\n"+ value3);
    }
    public static double addDoubleArr(double... doubles){
       return addDoubles(doubles);
    }

    /**
     *
     * @param doubles double数组
     * @return 将double求和
     */
    public static double addDoubles(double[] doubles) {
        if (doubles.length<1){
            return 0.0;
        }
        BigDecimal result = new BigDecimal(Double.toString(doubles[0]));
        for (int i = 1; i < doubles.length; i++) {
            BigDecimal p1 = new BigDecimal(Double.toString(doubles[i]));
            result = result.add(p1);
        }
        return result.doubleValue();
    }

    /**
     * 减法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static double subDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param m1
     * @param m2
     * @return
     */
    public static double mul(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.multiply(p2).doubleValue();
    }


    /**
     * 除法运算
     *
     * @param m1
     * @param m2
     * @param scale
     * @return
     */
    public static double div(double m1, double m2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
