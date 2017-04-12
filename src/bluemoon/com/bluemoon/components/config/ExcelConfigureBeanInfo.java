package com.bluemoon.components.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.FileEditor;

import java.beans.PropertyDescriptor;

/**
 * Created by Johnson on 2016/8/25.
 */
public class ExcelConfigureBeanInfo extends BeanInfoSupport {
    // These names must agree case-wise with the variable and property names
    static final String FILENAME = "fileName";               //$NON-NLS-1$
    static final String SHEET_NAME = "sheetName";      //$NON-NLS-1$
    static final String LABEL_NAME = "labelName";    //$NON-NLS-1$
    static final String ENABLE_TAGS = "enableTags";             //$NON-NLS-1$


    /**
     * Construct a BeanInfo for the given class.
     */
    public ExcelConfigureBeanInfo() {
        super(ExcelConfigure.class);
        createPropertyGroup("excel_data",new String[]{FILENAME,SHEET_NAME,LABEL_NAME,ENABLE_TAGS});

        PropertyDescriptor p = property(FILENAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setPropertyEditorClass(FileEditor.class);
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(SHEET_NAME);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);


        p = property(LABEL_NAME);
        p.setValue(NOT_UNDEFINED,Boolean.TRUE);
        p.setValue(DEFAULT, "");
        p.setValue(NOT_EXPRESSION, Boolean.TRUE);

        p = property(ENABLE_TAGS);
        p.setValue(NOT_UNDEFINED,Boolean.TRUE);
        p.setValue(DEFAULT,"");
        p.setValue(NOT_EXPRESSION,Boolean.TRUE);


    }
}
