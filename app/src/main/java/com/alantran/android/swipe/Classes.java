package com.alantran.android.swipe;

/**
 * Created by alantran on 4/15/16.
 */
public class Classes {
   private  String courseID;
    private String name;
    private String description;
    private String startTime;
    private String endTime;

    public Classes(){

    }

    public Classes(String courseID, String name, String description) {
        this.courseID = courseID;
        this.name = name;
        this.description = description;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "courseID='" + courseID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
