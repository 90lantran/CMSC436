package com.alantran.android.swipe.com.alantran.android.swipe.objects;

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

    private void buildSchedules(ArrayList<Classes> classes) {
        Random r = new Random(); // TODO: building schedules at random; maybe this isn't the best idea?

        while (schedules.size() < 2) {
            HashMap<String, ArrayList<Section>> newSchedule = new HashMap<String, ArrayList<Section>>();

            for (Classes c : classes) {
                int index = r.nextInt(c.getSections().size());
                String sectionId = c.getSections().keySet().toArray()[index].toString();
                ArrayList<Section> sections = c.getSections().get(c.getSections().keySet().toArray()[r.nextInt(c.getSections().size())]);
                newSchedule.put(c.getCourseID(), sections);
            }

            if (!hasConflict(newSchedule)) {
                schedules.add(newSchedule);
            }
        }
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
