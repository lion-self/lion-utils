package com.lion.utils;

import org.junit.Test;

import java.net.URL;


public class UnitTestUtilTest {

    @Test
    public void createMockImpl() {
        URL url = ClassLoader.getSystemClassLoader().getResource("com/lion/formatter");
        System.out.println(url.getPath());
    }
}