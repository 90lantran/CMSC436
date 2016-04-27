package com.alantran.android.swipe;

/**
 * Created by alantran on 4/15/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Classes implements Parcelable {
    private String courseID;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String instructor;
    private String days;
    private String building;
    private String room;
    private String classtype;
    private Date startTimeSimple;
    private Date endTimeSimple;
    private Integer color = -1;

    public Classes(String courseID, String name, String description) {
        this.courseID = courseID;
        this.name = name;
        this.description = description;
    }

    public Classes() {

    }

    public Date getEndTimeSimple() {
        return endTimeSimple;
    }

    public void setEndTimeSimple(Date endTimeSimple) {
        this.endTimeSimple = endTimeSimple;
    }

    public Date getStartTimeSimple() {
        return startTimeSimple;
    }

    public void setStartTimeSimple(Date startTimeSimple) {
        this.startTimeSimple = startTimeSimple;
    }

    public String getDays() {
        return days;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
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
                "courseID ='" + courseID + '\'' +
                ", name ='" + name + '\'' +
                ", startTimeSimple = " + startTimeSimple +
                ", endTimeSimple = " + endTimeSimple +
                ", color = " + color +
                '}';
    }

    public int overLaps(Classes other) {
        return this.getEndTimeSimple().compareTo(other.getStartTimeSimple());
    }


    // Parcelable part.

    private Classes(Parcel in) {
        courseID = in.readString();
        name = in.readString();
        description = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        instructor = in.readString();
        days = in.readString();
        building = in.readString();
        room = in.readString();
        classtype = in.readString();
        color = in.readInt();
        //private Date startTimeSimple ;
        //private Date endTimeSimple;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(instructor);
        dest.writeString(days);
        dest.writeString(building);
        dest.writeString(room);
        dest.writeString(classtype);
        dest.writeInt(color);
    }

    public static final Parcelable.Creator<Classes> CREATOR = new Parcelable.Creator<Classes>() {
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        public Classes[] newArray(int size) {
            return new Classes[size];

        }
    };
}
