package org.apache.jmeter.functions;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Johnson on 2016/10/13.
 */
public class MD5 extends AbstractFunction {
    private static final List<String> desc = new LinkedList<>();

    private static final String KEY = "__MD5"; //$NON-NLS-1$

    private Object[] values;
    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        String pwd=((CompoundVariable)values[0]).execute();
        System.out.println( "input password :"+pwd);
        return DigestUtils.md5Hex(pwd.getBytes());
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters,1);
        values=parameters.toArray();
    }


    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }

    @Test
    public void testMD5(){
        String pwd="qq123123";
        pwd=DigestUtils.md5Hex(pwd.getBytes());
        Assert.assertEquals(pwd,"3fd6ebe43dab8b6ce6d033a5da6e6ac5");
    }
}
