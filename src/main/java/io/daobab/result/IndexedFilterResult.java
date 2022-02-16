package io.daobab.result;

import java.util.Collection;
import java.util.List;

public class IndexedFilterResult {

    private Collection<Integer> pointers;
    private List<Integer> skippedWhereSteps;

    public IndexedFilterResult(Collection<Integer> eList, List<Integer> skipSteps) {
        this.pointers = eList;
        this.setSkippedWhereSteps(skipSteps);
    }

    public Collection<Integer> getPointers() {
        return pointers;
    }

    public void setPointers(Collection<Integer> pointers) {
        this.pointers = pointers;
    }

    public List<Integer> getSkippedWhereSteps() {
        return skippedWhereSteps;
    }

    public void setSkippedWhereSteps(List<Integer> skippedWhereSteps) {
        this.skippedWhereSteps = skippedWhereSteps;
    }
}
