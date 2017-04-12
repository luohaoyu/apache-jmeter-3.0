package com.luohaoyu.jmeter.functions;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.util.Collection;
import java.util.List;

/**
 * Created by Johnson on 2017/2/9.
 */
public class Factorial extends AbstractFunction {
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        return null;
    }

    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {

    }

    public String getReferenceKey() {
        return null;
    }

    public List<String> getArgumentDesc() {
        return null;
    }
}
