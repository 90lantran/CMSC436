package com.alantran.android.swipe;

import java.util.List;

/**
 * Created by alantran on 5/7/16.
 */
public class Schedule {
    List<Sections> sectionsList;
    int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public Schedule(int num, List<Sections> sectionsList){
        this.num =num;
        this.sectionsList = sectionsList;

    }

}
