package com.alantran.android.swipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alantran on 5/1/16.
 */
public class Classes {
    private String courseID;
    private String name;
    private String description;
    private String instructor;

    public List<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    private List<Sections> sectionsList = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
