package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.queryobject.ITerm;
import edu.wustl.common.querysuite.queryobject.impl.CustomFormula;
import junit.framework.TestCase;

public class CustomFormulaProcessorTest extends TestCase {
    private CustomFormulaProcessor customFormulaProcessor;

    private CustomFormula customFormula;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        customFormulaProcessor = new CustomFormulaProcessor();
    }
}
