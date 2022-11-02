package io.daobab.target;

import io.daobab.error.DaobabException;
import io.daobab.model.ColumnsProvider;
import io.daobab.model.Entity;
import io.daobab.model.EntityAny;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.interceptor.DaobabInterceptor;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.BasicAccessProtector;
import io.daobab.target.statistic.StatisticCollector;
import io.daobab.target.statistic.StatisticCollectorImpl;
import io.daobab.target.statistic.StatisticCollectorProvider;
import io.daobab.target.statistic.StatisticProvider;
import io.daobab.target.statistic.table.StatisticRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public abstract class BaseTarget implements Target, StatisticCollectorProvider, StatisticProvider {

    protected final Entity entityAny = new EntityAny();
    protected Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private Map<Entity, List<DaobabInterceptor>> interceptors;
    private StatisticCollector statistic;
    private boolean statisticEnabled = false;
    private AccessProtector accessProtector;

    private final Map<Class<? extends ColumnsProvider>, List<TableColumn>> columnsCache = new HashMap<>();

    protected BaseTarget() {
        accessProtector = new BasicAccessProtector();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    protected Map<Entity, List<DaobabInterceptor>> getInterceptorsMap() {
        return interceptors;
    }

    public void addInterceptorForAllEntities(DaobabInterceptor interceptor) {
        addInterceptor(interceptor, entityAny);
    }

    private void addInterceptor(DaobabInterceptor interceptor, Entity entity) {
        if (entity == null)
            throw new DaobabException("You cannot add interceptor to null entity. If you want to add interceptor to any Entity into target, use addInterceptorForAllEntities(DaobabInterceptor interceptor) method.");
        if (interceptor == null) throw new DaobabException("You cannot add interceptor which is null");
        List<DaobabInterceptor> interceptors = getInterceptorsMap().get(entity);
        List<DaobabInterceptor> interceptors_for_any_entity = getInterceptorsMap().get(entityAny);

        if (interceptors_for_any_entity.contains(interceptor) && !EntityAny.class.getName().equals(entity.getClass().getName())) {
            getLog().warn(format("Interceptor %s is already added to any entity into target %s", interceptor.getClass().getName(), this.getClass().getName()));
            return;
        }

        boolean firstInterceptor = false;
        if (interceptors == null) {
            interceptors = new ArrayList<>();
            firstInterceptor = true;
        }
        if (interceptors.contains(interceptor)) {
            getLog().warn(format("Interceptor %s already added to a target %s", interceptor.getClass().getName(), this.getClass().getName()));
            return;
        }

        interceptors.add(interceptor);
        getLog().info(format("Interceptor %s added successfully to a target %s", this.getClass().getName(), this.getClass().getName()));

        if (firstInterceptor) {
            getInterceptorsMap().put(entity, interceptors);
        }
    }

    public void addInterceptor(DaobabInterceptor interceptor, Entity... entities) {
        if (entities == null)
            throw new DaobabException("You cannot add interceptor to null entities. If you want to add interceptor to any Entity into target, use addInterceptorForAllEntities(DaobabInterceptor interceptor) method.");
        if (interceptor == null) throw new DaobabException("You cannot add interceptor which is null");
        for (Entity ent : entities) {
            addInterceptor(interceptor, ent);
        }
    }

//    protected List<DaobabInterceptor> getInterceptorsForEntity(Entity entity) {
//        List<DaobabInterceptor> rv = getInterceptorsForEntity(entityAny);
//        if (rv == null) rv = new LinkedList<>();
//        rv.addAll(getInterceptorsForEntity(entity));
//        return rv;
//    }

    protected boolean areInterceptorInUse() {
        return interceptors != null;
    }

    private void handleInterceptorMethod(Entity entity, BiConsumer<Entity, DaobabInterceptor> consumer) {
        if (!areInterceptorInUse()) return;
        if (entity == null) return;
//        for (DaobabInterceptor interceptor : getInterceptorsForEntity(entity)) {
//            consumer.accept(entity, interceptor);
//        }
    }

    protected void beforeInsert(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.beforeInsert(e));
    }

    protected void afterInsert(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.afterInsert(e));
    }

    protected void beforeUpdate(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.beforeUpdate(e));
    }

    protected void afterUpdate(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.afterUpdate(e));
    }

    protected void beforeDelete(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.beforeDelete(e));
    }

    protected void afterDelete(Entity entity) {
        handleInterceptorMethod(entity, (e, i) -> i.afterDelete(e));
    }

    public boolean isTransactionActive() {
        return false;
    }


    public StatisticCollector getStatisticCollector() {
        if (statistic == null) {
            statistic = new StatisticCollectorImpl();
        }
        return statistic;
    }


    @Override
    public boolean isStatisticCollectingEnabled() {
        return statisticEnabled;
    }

    public void enableStatisticCollecting(boolean statisticEnabled) {
        this.statisticEnabled = statisticEnabled;
    }

    @Override
    public AccessProtector getAccessProtector() {
        return accessProtector;
    }

    public void setAccessProtector(AccessProtector accessProtector) {
        this.accessProtector = accessProtector;
    }

    @Override
    public Entities<StatisticRecord> getStatistics() {
        return getStatisticCollector().getTarget();
    }

    public List<TableColumn> getColumnsForTable(final ColumnsProvider entity) {
        return columnsCache.computeIfAbsent(entity.getClass(), e -> entity.columns());
    }
}
