package ru.spbau.mit.Model;

/**
 * Created by n_buga on 23.12.16.
 */
public class TestXss {
    private static long count = 0;

    public static void main(String[] args) {
        infiniteRecMethod(1);
    }

    private static void infiniteRecMethod(int i) {
        count++;
        System.out.println(count);
        infiniteRecMethod(i);
    }
}
