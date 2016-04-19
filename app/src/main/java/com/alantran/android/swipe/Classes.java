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
    private String instructor;
    private String days;
    private String building;
    private String room;
    private String classtype;

    public Classes(){

    }

    public Classes(String courseID, String name, String description) {
        this.courseID = courseID;
        this.name = name;
        this.description = description;
    }
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }


    public void setRoom(String room) {
        this.room = room;
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype;
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
                '}';
    }

}
