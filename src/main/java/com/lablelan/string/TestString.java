package com.lablelan.string;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class TestString {
    public static long getLocation(Object object) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        // 通过反射获取UnSafe获取对象内存地址
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        Object array[] = {object};
        //返回数组中第一个元素的偏移地址
        long baseOffset = unsafe.arrayBaseOffset(array.getClass());
        //返回数组中一个元素占用的大小
        int scale = unsafe.arrayIndexScale(array.getClass());
        long location = 0;
        switch (scale) {
            // 32位系统
            case 4:
                location = unsafe.getInt(array, baseOffset);
                break;
            // 64位系统
            case 8:
                location = unsafe.getLong(array, baseOffset);
                break;
            default:
                break;
        }
        return location;
    }
    public static void change(String i) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        long addI1 = getLocation(i);
        i = "20";
        long addI2 = getLocation(i);
        // iAddr1和iAddr2不一致！
        System.out.println("change String:" + "addI1: " + addI1 + " addI2: " + addI2);
    }
    public static void change(StringBuilder i) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        long addrSB1 = getLocation(i);
        i.append("0");
        long addrSB2 = getLocation(i);
        System.out.println("change StringBuilder:" + "addrSB1: " + addrSB1 + " addrSB2: " + addrSB2);
    }
    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        String i = "10";
        long addrI1 = getLocation(i);
        System.out.println("main addrI1:" + addrI1 + "," + i.hashCode());
        change(i);
        long addrI2 = getLocation(i);
        // addrI1 和 addrI2 地址一样说明change内部操作对i并没有生效
        System.out.println("main:" + "addrI1 = " + addrI1 + " addrI2 = " + addrI2 + " hashCode:" + i.hashCode());
        StringBuilder sb = new StringBuilder("10");
        long addrSB1 = getLocation(i);
        change(sb);
        long addrSB2 = getLocation(i);
        // addrSB1 和 addrSB2 地址一样 并且sb确实被修改
        System.out.println("main:" + "addrSB1 = " + addrSB1 + " addrSb2 = " + addrSB2);
    }
}
