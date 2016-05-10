package com.alantran.android.swipe;

/**
 * Created by alantran on 4/15/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Sections implements Parcelable {

    private String sectionID;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String instructor;
    private String days;
    private String building;
    private String room;
    private String classType;
    private Date startTimeSimple;
    private Date endTimeSimple;
    private Integer color = -1;


    @Override
    public String toString() {
        return "Sections{" +
                "sectionID='" + sectionID + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", instructor='" + instructor + '\'' +
                ", building='" + building + '\'' +
                ", color=" + color +
                ", classType='" + classType + '\'' +
                ", days='" + days + '\'' +
                ", room='" + room + '\'' +
                '}';
    }



    public Sections(String courseID, String name, String description) {
        this.sectionID = courseID;
        this.name = name;
        this.description = description;
    }

    public Sections() {

    }

    public String getSectionID() {
        return sectionID;
    }

    public void setSectionID(String sectionID) {
        this.sectionID = sectionID;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
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


    public int overLaps(Sections other) {
        return this.getEndTimeSimple().compareTo(other.getStartTimeSimple());
    }


    // Parcelable part.

    private Sections(Parcel in) {
        sectionID = in.readString();
        name = in.readString();
        description = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        instructor = in.readString();
        days = in.readString();
        building = in.readString();
        room = in.readString();
        classType = in.readString();
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
        dest.writeString(sectionID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(instructor);
        dest.writeString(days);
        dest.writeString(building);
        dest.writeString(room);
        dest.writeString(classType);
        dest.writeInt(color);
    }

    public static final Parcelable.Creator<Sections> CREATOR = new Parcelable.Creator<Sections>() {
        public Sections createFromParcel(Parcel in) {
            return new Sections(in);
        }

        public Sections[] newArray(int size) {
            return new Sections[size];

        }
    };
}
