package org.apache.jmeter.assertions;


import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.ThreadListener;

import java.io.Serializable;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Created by Johnson on 2016/8/25.
 */
public class JSONAssertionM extends AbstractTestElement implements Serializable, Assertion, ThreadListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    /**
     * Returns the result of the Assertion. Here it checks wether the Sample
     * took to long to be considered successful. If so an AssertionResult
     * containing a FailureMessage will be returned. Otherwise the returned
     * AssertionResult will reflect the success of the Sample.
     */
    @Override
    public AssertionResult getResult(SampleResult response) {
        // no error as default
        AssertionResult result = new AssertionResult(getName());
        String resultData = response.getResponseDataAsString();

        if (resultData.length()==0) {
            return result.setResultForNull();
        }else{

                result.setFailure(true);
                result.setFailureMessage("ResultData is not Json");

        }
        return result;

    }

    @Override
    public void threadStarted() {
    }

    @Override
    public void threadFinished() {
    }
}