package com.alantran.android.swipe.objects;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Danny on 4/23/2016.
 */
public class Schedule {
    private ArrayList<HashMap<String, ArrayList<Section>>> schedules; // Class name -> section

    public Schedule(ArrayList<Classes> classes) {
        schedules = new ArrayList<HashMap<String, ArrayList<Section>>>();
        buildSchedules(classes);
    }

    // Build every possible schedule
    private void build(ArrayList<Classes> classes, HashMap<String, ArrayList<Section>> createdSchedule, int index) {
        if (index == classes.size() - 1) {
            Classes c = classes.get(index);

            for (String courseId : c.getSections().keySet()) {
                HashMap<String, ArrayList<Section>> newSchedule = new HashMap<String, ArrayList<Section>>(createdSchedule);
                newSchedule.put(c.getCourseID(), c.getSections().get(courseId));

                if (!hasConflict(newSchedule)) {
                    schedules.add(newSchedule);
                }
            }
        } else {
            Classes c = classes.get(index);
            for (String courseId : c.getSections().keySet()) {
                HashMap<String, ArrayList<Section>> newSchedule = new HashMap<String, ArrayList<Section>>(createdSchedule);
                newSchedule.put(c.getCourseID(), c.getSections().get(courseId));
                build(classes, newSchedule, index + 1);
            }
        }
    }

    private void buildSchedules(ArrayList<Classes> classes) {
        build(classes, new HashMap<String, ArrayList<Section>>(), 0);
    }

    // Checks if a schedule has a conflict
    private boolean hasConflict(HashMap<String, ArrayList<Section>> sections) {
        for (ArrayList<Section> listOfSections : sections.values()) {
            for (Section section : listOfSections) {
                for (ArrayList<Section> listOfOtherSections : sections.values()) {
                    for (Section otherSection : listOfOtherSections) {
                        if (!section.getSectionId().equals(otherSection.getSectionId())) {
                            for (Map.Entry<GregorianCalendar, GregorianCalendar> entry : section.getStartEndTimes().entrySet()) {
                                GregorianCalendar startA = entry.getKey();
                                GregorianCalendar endA = entry.getValue();
                                for (Map.Entry<GregorianCalendar, GregorianCalendar> otherEntry : otherSection.getStartEndTimes().entrySet()) {
                                    GregorianCalendar startB = otherEntry.getKey();
                                    GregorianCalendar endB = otherEntry.getValue();

                                    if (startA.before(endB) && endA.after(startB)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<HashMap<String, ArrayList<Section>>> getSchedules() {
        return schedules;
    }
}
