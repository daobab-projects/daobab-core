package io.daobab.target.database.connection;

import io.daobab.statement.function.FunctionWhispererH2;
import io.daobab.target.database.MockDataBase;
import io.daobab.target.database.meta.MetaDataTables;
import io.daobab.test.dao.SakilaTables;

class TestMetaimplements implements SakilaTables, FunctionWhispererH2, MetaDataTables {

    private MockDataBase db = new MockDataBase();

}
