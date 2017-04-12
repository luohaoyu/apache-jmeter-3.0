package com.bluemoon.components.demo;

import jodd.util.ArraysUtil;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Johnson on 2016/8/27.
 */
public class ExcelConfigure111Test {
    @Test
    public void filterByLabelName() throws Exception {
        System.out.println();
        String[] lineVals = new String[10];
        String[] t = new String[8];
        Arrays.fill(t,"w");
        Arrays.fill(lineVals,"h");

        String[] tmp = new String[lineVals.length-t.length];
        Arrays.fill(tmp,"");
        lineVals=ArraysUtil.join(lineVals,tmp);
        System.out.println(Arrays.asList(lineVals));
    }

}