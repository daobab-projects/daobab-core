package io.daobab.result;

import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class IndexedFilterResult {

    private Integer[] pointers;
    private List<Integer> skippedWhereSteps;

    public IndexedFilterResult(Integer[] eList, List<Integer> skipSteps) {
        this.pointers = eList;
        this.setSkippedWhereSteps(skipSteps);
    }

    public Integer[] getPointers() {
        return pointers;
    }

    public void setPointers(Integer[] pointers) {
        this.pointers = pointers;
    }

    public List<Integer> getSkippedWhereSteps() {
        return skippedWhereSteps;
    }

    public void setSkippedWhereSteps(List<Integer> skippedWhereSteps) {
        this.skippedWhereSteps = skippedWhereSteps;
    }
}
