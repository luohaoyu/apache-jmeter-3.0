package org.apache.jmeter.examples.testbeans.example4;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnson on 2016/8/25.
 */
public class Excel extends ConfigTestElement implements TestBean,LoopIterationListener,NoConfigMerge,TestListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private  String fileName;

    public Excel() {
        super();
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        log.error(this.fileName+">>>>>>>"+iterEvent.getIteration());
    }


    @Override
    public void addError(Test test, Throwable throwable) {

    }

    @Override
    public void addFailure(Test test, AssertionFailedError assertionFailedError) {

    }

    @Override
    public void endTest(Test test) {

    }

    @Override
    public void startTest(Test test) {

    }
}
