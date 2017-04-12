package com.bluemoon.components.demo;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Created by Johnson on 2016/8/26.
 */
public class TestConfigure extends ConfigTestElement implements TestBean,
        LoopIterationListener,
        NoConfigMerge,
        TestStateListener,
        SampleListener,
        ThreadListener
{
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private  String fileName="";


    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        System.out.println("循环次数："+iterEvent.getIteration());
    }

    @Override
    public void testStarted() {
        System.out.println("测试开始：TestStateListener.testStarted");

    }

    @Override
    public void testStarted(String host) {

    }


    @Override
    public void testEnded() {
        System.out.println("TestStateListener.testEnded");
    }

    @Override
    public void testEnded(String host) {

    }


    @Override
    public void sampleOccurred(SampleEvent e) {
        System.out.println("sampleOccurred>"+e.getResult().getSampleLabel());
    }

    @Override
    public void sampleStarted(SampleEvent e) {
        System.out.println("sampleStarted");
    }

    @Override
    public void sampleStopped(SampleEvent e) {
        System.out.println("sampleStopped");
    }

    @Override
    public void threadStarted() {
        System.out.println("threadStarted");
    }

    @Override
    public void threadFinished() {
        System.out.println("threadFinished");
    }
}
