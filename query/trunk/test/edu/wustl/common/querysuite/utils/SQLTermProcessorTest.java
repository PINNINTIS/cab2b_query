package edu.wustl.common.querysuite.utils;

import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ITerm;
import junit.framework.TestCase;

public class SQLTermProcessorTest extends TermProcessorTest {
    private static final String dateFormat = "yyyy-mm-dd";

    private static final DatabaseSQLSettings mySQLSettings = new DatabaseSQLSettings(DatabaseType.MySQL,
            dateFormat);

    private static final DatabaseSQLSettings oracleSetting = new DatabaseSQLSettings(DatabaseType.Oracle,
            dateFormat);

    public void testGeneralSQL() {
        TermProcessor termProcessor = new TermProcessor(TermProcessor.defaultAliasProvider, mySQLSettings);
        ITerm term = QueryObjectFactory.createTerm();
        term.addOperand(dateLiteral("d1"));
        
    }
}
