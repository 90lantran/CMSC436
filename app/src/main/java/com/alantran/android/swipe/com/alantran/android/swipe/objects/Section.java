package com.alantran.android.swipe.com.alantran.android.swipe.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Danny on 4/23/2016.
 */
public class Section implements Parcelable {
    private String sectionId;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String instructor;
    private String days;
    private String building;
    private String room;
    private String classtype;

    private HashMap<GregorianCalendar, GregorianCalendar> times;

    public Section() {
        times = new HashMap<GregorianCalendar, GregorianCalendar>();
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
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

    public HashMap<GregorianCalendar, GregorianCalendar> getStartEndTimes() {
        return times;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        // TODO: Enforce set endtime after starttime
        // Converting all the time values into time objects for later use
        // 2/1/2016 = A monday

        SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy hh:mma");
        String dayOfMonth = "01"; // Monday

        char[] dayChars = days.toCharArray();
        for (int i = 0; i < dayChars.length; i++) {
            char c = dayChars[i];
            if (c == 'M') { // TODO: Let's make sure saturday or sunday classes aren't a thing
                dayOfMonth = "01";
            } else if (c == 'W') {
                dayOfMonth = "03";
            } else if (c == 'F') {
                dayOfMonth = "05";
            } else {
                char c2 = dayChars[i+1];
                i++;
                if (c2 == 'u') { // tuesday
                    dayOfMonth = "02";
                } else { // thursday
                    dayOfMonth = "04";
                }
            }

            Date start = new Date(0); // unix epoch
            Date end = new Date(0);
            try {
                start = format.parse("02 " + dayOfMonth + " 2016 " + startTime);
                end = format.parse("02 " + dayOfMonth + " 2016 " + endTime);
            } catch (ParseException e) {
                // TODO: fix this
            }
            GregorianCalendar startDate = new GregorianCalendar();
            GregorianCalendar endDate = new GregorianCalendar();
            startDate.setTime(start);
            endDate.setTime(end);
            times.put(startDate, endDate);
        }
        for (char c : days.toCharArray()) {
            if (c == 'M' || c == 'W' || c == 'F') { // TODO: Let's make sure saturday or sunday classes aren't a thing
                Date date = new Date();
            } else {

            }
        }
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

    public String getSectionId() {
        return sectionId;
    }

    // Parcelable
    private Section(Parcel in) {
        sectionId = in.readString();
        name = in.readString();
        description = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        instructor = in.readString();
        days = in.readString();
        building = in.readString();
        room = in.readString();
        classtype = in.readString();

        int size = in.readInt();
        times = new HashMap<GregorianCalendar, GregorianCalendar>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                GregorianCalendar start = new GregorianCalendar();
                GregorianCalendar end = new GregorianCalendar();
                start.setTimeInMillis(in.readLong());
                end.setTimeInMillis(in.readLong());
                times.put(start, end);
            }
        }
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(sectionId);
        out.writeString(name);
        out.writeString(description);
        out.writeString(startTime);
        out.writeString(endTime);
        out.writeString(instructor);
        out.writeString(days);
        out.writeString(building);
        out.writeString(room);
        out.writeString(classtype);

        out.writeInt(times.size());
        for (Map.Entry<GregorianCalendar, GregorianCalendar> entry : times.entrySet()) {
            out.writeLong(entry.getKey().getTimeInMillis());
            out.writeLong(entry.getValue().getTimeInMillis());
        }
    }

    public static final Parcelable.Creator<Section> CREATOR
            = new Parcelable.Creator<Section>() {
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}
