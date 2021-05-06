package io.daobab.result;

import java.util.List;

public class ResultBitBufferPositionWithSkipStepsWrapper {

    private List<Integer> entityPointers;
    private List<Integer> skipSteps;

    public ResultBitBufferPositionWithSkipStepsWrapper(List<Integer> eList, List<Integer> skipSteps) {
        this.entityPointers = eList;
        this.setSkipSteps(skipSteps);
    }

    public List<Integer> getEntityPointers() {
        return entityPointers;
    }

    public void setEntityPointers(List<Integer> entityPointers) {
        this.entityPointers = entityPointers;
    }

    public List<Integer> getSkipSteps() {
        return skipSteps;
    }

    public void setSkipSteps(List<Integer> skipSteps) {
        this.skipSteps = skipSteps;
    }
}
