package com.alantran.android.swipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alantran on 5/1/16.
 */
public class Classes implements Parcelable{
    private String courseID;
    private String name;
    private String description;
    private String instructor;
    private String credit;
    private List<Sections> sectionsList = new ArrayList<>();

    public Classes(){

    }


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

    // Parcelable
    protected Classes(Parcel in) {
        courseID = in.readString();
        name = in.readString();
        description = in.readString();
        instructor = in.readString();
        credit = in.readString();
        sectionsList = in.createTypedArrayList(Sections.CREATOR);
    }

    public static final Creator<Classes> CREATOR = new Creator<Classes>() {
        @Override
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        @Override
        public Classes[] newArray(int size) {
            return new Classes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(instructor);
        dest.writeString(credit);
        dest.writeTypedList(sectionsList);
    }
}
