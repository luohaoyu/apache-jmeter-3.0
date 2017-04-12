package com.bluemoon.components.demo;

import jodd.util.ArraysUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.WhileController;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnson on 2016/8/25.
 */
public class ExcelConfigure111 extends ConfigTestElement implements TestBean, LoopIterationListener,TestStateListener ,NoConfigMerge {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    //gui配置变量
    private transient String fileName;

    private transient String sheetName;

    private transient String labelName;

    private transient String enableTags;

//    private transient int initValNameNum;

    //-------------------------------------

    private transient Sheet sheet;

    private transient String[] valNames;

    private transient Map<String, String> vars;

    private transient ArrayList<Integer> enableRows;

    public ExcelConfigure111() {
        super();

    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        TestElement te =iterEvent.getSource();
        log.info("Iterations>>>" + iterEvent.getIteration());
        final JMeterContext context = getThreadContext();
        JMeterVariables threadVars = context.getVariables();
        int iters = iterEvent.getIteration();

        if (getValNames() == null) {
            init(threadVars);
        }

        //将表格中可用行转换成jmeter 变量
        if(te instanceof LoopController){//如果是为配置原件
            iters=iters-1;//配置原件放置位置非配置元素，枚举从1开始
        }

        toJmeterVars(threadVars, enableRows.get(iters));
        if(iters>=(enableRows.size()-1)&&(te instanceof WhileController)) //停止循环(循环计数从零开始，表格行数计数从1开始)#
            stopIterationFor(threadVars);

}


    private Sheet getSheet(String fileName, String sheetName) {
        Sheet sheet = null;
        try {
            if (fileName == null || fileName.length() == 0) {
                log.error("File name is not assigned");
            }
            log.info("File Name>>>>" + fileName);
            FileServer fileServer = FileServer.getFileServer();
            File file = fileServer.getResolvedFile(fileName);
            Workbook workbook = Workbook.getWorkbook(new FileInputStream(file));
            if (sheetName.length() == 0) {
                sheet = workbook.getSheet(0);
            } else {
                sheet = workbook.getSheet(sheetName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sheet;
    }

    /**
     * @param row 指定某一行,从零开始
     * @return 如果指定某一行没有超过sheet中行数，则返回列值数据数组，
     * 其他情况返回空数组
     */
    private String[] ValuesOf(int row) {
        String[] values = {};
        if (sheet == null) {
            log.error("sheet is null");
            return values;
        }
        if (row < 0) {
            log.error("Row number is less than  0");
            return values;
        }
        int sheetRows = sheet.getRows();
        if (row < sheetRows && sheetRows != 0) {
            Cell[] cells = sheet.getRow(row);
            String[] lineVals = new String[cells.length];
            for (int i = 0; i < lineVals.length; i++) {
                lineVals[i] = cells[i].getContents();
            }
            if(lineVals.length<sheet.getRow(0).length){//解决最后一行为空白时候
                String[] tmp = new String[sheet.getRow(0).length-lineVals.length];
                Arrays.fill(tmp,"");
                lineVals=ArraysUtil.join(lineVals,tmp);

            }
            log.info("row="+row+">>>>"+Arrays.asList(lineVals).toString());
            return lineVals;
        }
        log.error("ValuesOf(row)" + row + " greater than sheet.rows=" + sheetRows);
        return values;
    }

    /**
     * @return 如果标签选项不为空或者没有则返回true，否则false
     */
    private boolean hasTags() {
        return (this.enableTags != null && this.enableTags.length() != 0) ;
    }

    private boolean hasLabel() {
        return (this.labelName != null && this.labelName.length() != 0) ;
    }


    public void init(JMeterVariables threadVars) {

        //初始化成员变量sheet和valNames
        this.sheet = getSheet(getFileName(), getSheetName());
        this.valNames = ValuesOf(0);//获取一行的值
        this.vars = toMap(this.valNames);
        threadVars.putAll(this.vars);//添加到线程变量
        this.enableRows = obtainEnableRows();//从第二行开始
        log.info("Initialize the first row of each column as variable names and value is null");
        log.info("value Name:" + Arrays.asList(valNames).toString());
        log.info("enableRows:" + enableRows.toString());

    }

    public void stopIterationFor(JMeterVariables threadVars) {
        threadVars.put("HAS_NEXT", "false");
    }

    public void toJmeterVars(JMeterVariables threadVars, int num) {
        String[] values = ValuesOf(num);//第一行初始化和第二行数据在同一次迭代中完成，所以需要加一
        String[] names = getValNames();
        if (values != null || values.length != 0) {
            if (values.length == names.length) {
                for (int i = 0; i < names.length; i++) {
                    if (i == indexOflabel())
                        continue;
                    threadVars.put(names[i], values[i]);
                }
            }
        }

    }

    private int indexOflabel() {
        if (hasLabel()) {
            return Arrays.asList(valNames).indexOf(getLabelName());
        }
        log.warn("Label Name is not defined");
        return -1;
    }

    private ArrayList<Integer> obtainEnableRows() {
        ArrayList<Integer> tmp = new ArrayList<>();
        int rows = sheet.getRows();
        if (hasTags() && indexOflabel()!=-1) {
            int index = indexOflabel();
            log.info("tag index:"+index);
            for (int i = 1; i < rows; i++) {
                String val = sheet.getCell(index, i).getContents();
                if (Arrays.asList(getEnableTags().split(" ")).contains(val)) {
                    tmp.add(i);
                }
            }
            return tmp;
        }

        for (int i = 1; i < rows; i++) {
            tmp.add(i);
        }
        return tmp;

    }

    private Map<String, String> toMap(String[] valNames) {
        Map<String, String> vars = new HashMap<>();
        if (valNames == null) {
            return null;
        }
        for (String name : valNames) {
            if (hasLabel() && getLabelName().equalsIgnoreCase(name))//LabelName存在，不添加到map
                continue;
            vars.put(name, null);
        }
        return vars;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }


    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public String[] getValNames() {
        return valNames;
    }

    public void setValNames(String[] valNames) {
        this.valNames = valNames;
    }

    public String getEnableTags() {
        return enableTags;
    }

    public void setEnableTags(String enableTags) {
        this.enableTags = enableTags;
    }


    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }
}
