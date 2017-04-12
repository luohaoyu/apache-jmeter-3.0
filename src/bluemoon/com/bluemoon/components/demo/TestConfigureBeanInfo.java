package com.bluemoon.components.demo;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.TestBean;

import java.beans.PropertyDescriptor;

/**
 * Created by Johnson on 2016/8/26.
 */
public class TestConfigureBeanInfo extends BeanInfoSupport {

    public TestConfigureBeanInfo() {
        super(TestConfigure.class);
        PropertyDescriptor p;
        p = property("fileName");
        p.setValue(NOT_EXPRESSION,Boolean.TRUE);
        p.setValue(NOT_UNDEFINED,Boolean.TRUE);
        p.setValue(DEFAULT,"filename");
    }
}
