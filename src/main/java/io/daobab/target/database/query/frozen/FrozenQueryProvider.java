package io.daobab.target.database.query.frozen;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.query.base.StatisticQuery;
import io.daobab.target.database.query.DataBaseQueryBase;

import java.util.List;

public interface FrozenQueryProvider extends StatisticQuery, ILoggerBean {

    String getFrozenQuery();

    List<ParameterInjectionPoint> getQueryParametersInjectionPoints();

    @SuppressWarnings("rawtypes")
    DataBaseQueryBase unfreeze();


}
