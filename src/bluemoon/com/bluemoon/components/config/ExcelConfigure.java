package com.bluemoon.components.config;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Johnson on 2016/10/30.
 */
public class ExcelConfigure extends ConfigTestElement
        implements TestBean, LoopIterationListener, TestStateListener, SampleListener, NoConfigMerge,Cloneable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 240L;

    //gui配置变量
    private transient String fileName;//文件路径

    private transient String sheetName;//表格名称

    private transient String labelName;//标签列

    private transient String enableTags;//可用的标签

    //成员变量
    private Workbook workbook;
    private Sheet sheet;
    private ArrayList<Integer> enableRows ;
    private JMeterVariables threadVars;
    private ArrayList<String> varNames;
    private int varNamesLine = 0;
    private int valueStartLine = varNamesLine + 1;
    private int count = 0;
    private boolean isfirst = true;

//    ExtentReports extentReports;
//    ExtentTest extentTest;

    public ExcelConfigure() {
        super();
    }

    public ArrayList<String> getValuesOf(int row) {
        Cell[] cells = sheet.getRow(row);
        ArrayList<String> varNames = new ArrayList<>();
        for (Cell cell : cells) {
            varNames.add(cell.getContents());
        }
        return varNames;
    }

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        System.out.println("iterationStart:"+this);
//        if(isfirst) {
            init();
//            isfirst=false;
//        }
        //终止循环
        if (enableRows.size() == 0 || count >= enableRows.size()) {
            log.warn("stop loops [End of excel file ]");
            throw new JMeterStopThreadException("End of excel file ");
        }

        int row = enableRows.get(count);
        ArrayList<String> lineValues;
        lineValues = getValuesOf(row);

        for (int i = 0; i < lineValues.size(); i++) {
            threadVars.put(varNames.get(i), lineValues.get(i));
        }

        System.out.println("第" + row + "行：  " + lineValues);

        count++;

    }

    private File resolveFileFromPath(String filename) {
        File base = new File(System.getProperty("user.home"));
        String prefix= JMeterUtils.getProperty("excelconfigure.prefix");
        File f = new File(filename);
        if (f.isAbsolute() || f.exists()) {
            return f;
        }else if (!JOrphanUtils.isBlank(prefix)){//如果设置文件前缀属性
            return new File(prefix,filename);
        } else {
            return new File(base, filename);
        }
    }

    private void init() {
        this.enableRows=new ArrayList<>();
        //初始化sheet
        File file = resolveFileFromPath(fileName);
        try {
            workbook = Workbook.getWorkbook(file);
            if (sheetName == null || sheetName.length() == 0)
                sheet = workbook.getSheet(0);
            else
                sheet = workbook.getSheet(getSheetName());

            final JMeterContext context = getThreadContext();
            threadVars = context.getVariables();
            varNames = getValuesOf(0);

            //初始化第一行作为变量名
            for (String varName : varNames)
                threadVars.put(varName, "");
            log.info("labelName=" + labelName + " enableTags=" + enableTags);
            System.out.println("初始化第一行作为变量名：" + varNames);
            //找到可以用的行数
            ArrayList<String> lineValues;
            ArrayList<Integer> enableRows = new ArrayList<>();
            for (int i = valueStartLine; i < sheet.getRows(); i++) {
                lineValues = getValuesOf(i);
                //如果设置了标签，找到可用的行
                if (labelName!=null&&labelName.length()!=0&& !StringUtils.isEmpty(enableTags)) {
                    int indexOflable = varNames.indexOf(labelName);
                    log.info("indexOflable=" + indexOflable);
                    if (indexOflable != -1) {
                        String tags = lineValues.get(indexOflable);
                        if (tags.contains(enableTags)) {//过滤测试用例
                            enableRows.add(i);
                        }
                    } else {
                        log.error("stop loops [indexOflable=" + indexOflable + "]");
                        throw new JMeterStopThreadException("End of excel file ");
                    }
                } else {
                    enableRows.add(i);
                }
            }
            this.enableRows = enableRows;
            System.out.println("可用行列表：" + this.enableRows);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testStarted() {
//        System.out.println("testStarted()"+this);
//        testStarted(null);
//        init();
    }

    @Override
    public void testStarted(String host) {
        System.out.println("testStarted((String host)");
    }

    @Override
    public void testEnded() {

        System.out.println("testEnded()");
    }

    @Override
    public void testEnded(String host) {
        System.out.println("testEnded(String host)");
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

    public String getEnableTags() {
        return enableTags;
    }

    public void setEnableTags(String enableTags) {
        this.enableTags = enableTags;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Integer> getEnableRows() {
        return enableRows;
    }

    public void setEnableRows(ArrayList<Integer> enableRows) {
        this.enableRows = enableRows;
    }

    @Override
    public void sampleOccurred(SampleEvent e) {
//        System.out.println("sampleOccurred");
        SampleResult sr= e.getResult();
        JMeterVariables jMeterVariables= JMeterContextService.getContext().getVariables();
       String  s= jMeterVariables.get("testcase");
        if(s!=null) {
            sr.setThreadName(s);
        }
    }

    @Override
    public void sampleStarted(SampleEvent e) {
//        System.out.println("sampleStarted");
    }

    @Override
    public void sampleStopped(SampleEvent e) {
//        System.out.println("sampleStopped");
    }
}
