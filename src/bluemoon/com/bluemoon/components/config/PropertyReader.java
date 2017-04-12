package com.bluemoon.components.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Johnson on 2016/10/31.
 */
public class PropertyReader extends ConfigTestElement implements TestBean, TestStateListener,NoConfigMerge {

    private static final long serialVersionUID = 240L;
    private static final Logger log = LoggingManager.getLoggerForClass();
    private String propFilePath;

    public PropertyReader() {
       super();
    }

    @Override
    public void testStarted() {
       testStarted(null);
    }
    private File resolveFileFromPath(String filename) {
        File base = new File(System.getProperty("user.home"));
        File f = new File(filename);
        if (f.isAbsolute() || f.exists()) {
            return f;
        } else {
            return new File(base, filename);
        }
    }
    @Override
    public void testStarted(String host) {
        log.info("Property file reader - loading the properties from " + this.propFilePath);
        if(!StringUtils.isEmpty(this.propFilePath)) {
            try {
                File file=resolveFileFromPath(this.propFilePath);
//                System.out.println(file.getAbsolutePath());
                JMeterUtils.getJMeterProperties().load(new FileInputStream(file));
            } catch (FileNotFoundException var3) {
                log.error(var3.getMessage());
            } catch (IOException var4) {
                log.error(var4.getMessage());
            }
        }
    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }

    public String getPropFilePath() {
        return propFilePath;
    }

    public void setPropFilePath(String propFilePath) {
        this.propFilePath = propFilePath;
    }
}
