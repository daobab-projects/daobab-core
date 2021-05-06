package io.daobab.result;

import io.daobab.model.Entity;

import java.util.LinkedList;
import java.util.List;

public class ResultEntitiesWithSkipStepsWrapper<E extends Entity> {

    private List<E> entities;
    private List<Integer> skipSteps;

    public ResultEntitiesWithSkipStepsWrapper(List<E> eList, List<Integer> skipSteps) {
        this.entities = eList == null ? new LinkedList<>() : eList;
        this.setSkipSteps(skipSteps);
    }

    public List<E> getEntities() {
        return entities;
    }

    public void setEntities(List<E> entities) {
        this.entities = entities;
    }

    public List<Integer> getSkipSteps() {
        return skipSteps;
    }

    public void setSkipSteps(List<Integer> skipSteps) {
        this.skipSteps = skipSteps;
    }
}
