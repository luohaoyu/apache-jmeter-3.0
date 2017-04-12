package org.apache.jmeter.assertions.gui;

import org.apache.jmeter.assertions.JSONAssertionM;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * Created by Johnson on 2016/8/25.
 */
public class JSONAssertionGui extends AbstractAssertionGui {

    private static final long serialVersionUID = 240L;

    /**
     * The constructor.
     */
    public JSONAssertionGui() {
        init();
    }

    /**
     * Returns the label to be shown within the JTree-Component.
     */
    @Override
    public String getLabelResource() {
        return "test_mygui"; // $NON-NLS-1$
    }

    @Override
    public TestElement createTestElement() {
        JSONAssertionM el = new JSONAssertionM();
        modifyTestElement(el);
        return el;
    }


    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement el) {
        configureTestElement(el);
    }

    /**
     * Inits the GUI.
     */
    private void init() {
        setLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));
        setBorder(makeBorder());

        add(makeTitlePanel());
    }
}