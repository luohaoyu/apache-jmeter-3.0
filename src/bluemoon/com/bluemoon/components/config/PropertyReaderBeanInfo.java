package com.bluemoon.components.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;

import java.beans.PropertyDescriptor;

/**
 * 加载属性文件
 * 可以使用相对路径
 * Created by Johnson on 2016/10/31.
 */
public class PropertyReaderBeanInfo extends BeanInfoSupport {

    private static final String PROPERTY_FILE_PATH = "propFilePath";

    public PropertyReaderBeanInfo() {
        super(PropertyReader.class);
        PropertyDescriptor p = this.property(PROPERTY_FILE_PATH);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
    }
}
