package com.it.aaron.codeflow.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();
        Set<Character> characters = new HashSet<>();
        integers.add(1);
        integers.get(0);
        methodA();
        methodB();
    }

    public static int  add(int a, int b) {
        int c = a + b;
        return c;
    }
    public static void methodA() {
        methodC();
    }
    public static void methodB() {

    }
    public static void methodC() {
        methodD();
    }
    public static void methodD() {

    }
}
