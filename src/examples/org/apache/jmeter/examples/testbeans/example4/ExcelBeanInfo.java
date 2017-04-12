package org.apache.jmeter.examples.testbeans.example4;

import org.apache.jmeter.testbeans.BeanInfoSupport;

import java.beans.PropertyDescriptor;

/**
 * Created by Johnson on 2016/8/25.
 */
public class ExcelBeanInfo extends BeanInfoSupport {

    private static final String FILENAME = "fileName";               //$NON-NLS-1$

    public ExcelBeanInfo() {
        super(Excel.class);
        getprop(FILENAME,"Hello World!");

    }
    private PropertyDescriptor getprop(String name) {
        final PropertyDescriptor property = property(name);
        property.setValue(NOT_UNDEFINED, Boolean.FALSE); // Ensure it is not flagged as 'unconfigured'
        return property;
    }

    private PropertyDescriptor getprop(String name, Object deflt) {
        PropertyDescriptor p = property(name);
        p.setValue(DEFAULT, deflt);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        return p;
    }
}
