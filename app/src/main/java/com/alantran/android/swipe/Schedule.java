package com.alantran.android.swipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alantran on 5/7/16.
 */
public class Schedule implements Parcelable{
    private List<Sections> sectionsList;
    private int num;

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

    // Parcelable

    protected Schedule(Parcel in) {
        sectionsList = in.createTypedArrayList(Sections.CREATOR);
        num = in.readInt();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(sectionsList);
        dest.writeInt(num);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "sectionsList=" + sectionsList +
                ", num=" + num +
                '}';
    }
}
