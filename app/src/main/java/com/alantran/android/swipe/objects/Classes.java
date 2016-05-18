package com.alantran.android.swipe.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alantran on 4/15/16.
 */

// Making some changes to this class: making parcelable and
// adding sections so there aren't multiple copies of one class
public class Classes implements Parcelable, Serializable {
    private String courseID;
    private String name;
    private String description;
    private int credits;
    private String core;
    private String genEd;
    private HashMap<String, String> instructors; // section id -> instructor
    private HashMap<String, ArrayList<Section>> sections; // section id -> sections (lecture, lab, discussion, etc.)

    public Classes(){
        instructors = new HashMap<String, String>();
        sections = new HashMap<String, ArrayList<Section>>();
    }

    public Classes(String courseID, String name, String description, int credits, String core, String genEd) {
        this.courseID = courseID;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.core = core;
        this.genEd = genEd;
        instructors = new HashMap<String, String>();
        sections = new HashMap<String, ArrayList<Section>>();
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

    public int getCredits() {
        return credits;
    }

    public String getCore() {
        return core;
    }

    public String getGenEd() {
        return genEd;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public void setGenEd(String genEd) {
        this.genEd = genEd;
    }

    public HashMap<String, ArrayList<Section>> getSections() {
        return sections;
    }

    public void addSection(String sectionId, Section section) {
        ArrayList<Section> sectionsList = sections.get(sectionId);
        if (sectionsList == null) { // Section not yet added
            sectionsList = new ArrayList<Section>();
            sectionsList.add(section);
            sections.put(sectionId, sectionsList);
        } else { // Section already added
            sectionsList.add(section);
        }
        if (section.getInstructor() == null || section.getInstructor().length() == 0) {
            instructors.put(sectionId, "TBA");
        } else {
            instructors.put(sectionId, section.getInstructor());
        }
    }

    public String getInstructor() {
        if (instructors.size() == 0) {
            return "TBA";
        }

        ArrayList<String> allInstructors = new ArrayList<>();
        for (String s : instructors.keySet()) {
            String i = instructors.get(s);
            if (i.startsWith("Instructor: ")) {
                i = i.substring(12, i.length());
            }
            if (!allInstructors.contains(i)) {
                allInstructors.add(i);
            }
        }

        StringBuilder s = new StringBuilder();

        for (String i : allInstructors) {
            s.append(i + ", ");
        }
        s.setLength(s.length() - 2);
        return s.toString();
    }

    private Classes(Parcel in) {
        courseID = in.readString();
        name = in.readString();
        description = in.readString();
        credits = in.readInt();

        // Recreate instructors hashmap
        int size = in.readInt();
        instructors = new HashMap<String, String>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                instructors.put(in.readString(), in.readString());
            }
        }

        // Recreate sections hashmap
        size = in.readInt();
        sections = new HashMap<String, ArrayList<Section>>();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String id;
                ArrayList<Section> list = new ArrayList<Section>();
                id = in.readString();
                in.readTypedList(list, Section.CREATOR);
                sections.put(id, list);
            }
        }
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(courseID);
        out.writeString(name);
        out.writeString(description);
        out.writeInt(credits);

        // Write instructors to parcel
        out.writeInt(instructors.size());
        for (Map.Entry<String, String> entry : instructors.entrySet()) {
            out.writeString(entry.getKey());
            out.writeString(entry.getValue());
        }

        // Write sections to parcel
        out.writeInt(sections.size());
        for (Map.Entry<String, ArrayList<Section>> entry : sections.entrySet()) {
            out.writeString(entry.getKey());
            out.writeTypedList(entry.getValue());
        }
    }

    public static final Parcelable.Creator<Classes> CREATOR
            = new Parcelable.Creator<Classes>() {
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        public Classes[] newArray(int size) {
            return new Classes[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return getCourseID() + " - " + getName();
    }

}