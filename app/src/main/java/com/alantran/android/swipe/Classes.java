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
    private String credit;
    private List<Sections> sectionsList = new ArrayList<>();

    @Override
    public String toString() {
        return "Classes{" +
                "courseID='" + courseID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", instructor='" + instructor + '\'' +
                ", sectionsList=" + sectionsList +
                '}';
    }

    public List<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(List<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }

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
