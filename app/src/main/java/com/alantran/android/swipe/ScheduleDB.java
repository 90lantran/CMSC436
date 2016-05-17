package com.alantran.android.swipe;

/**
 * Created by alantran on 5/16/16.
 */
public class ScheduleDB {

    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 1;
    private static final String SCHEDULE_TABLE = "scheduletable";
    private static final String COL_ID = "_id";
    private static final String SECTION_ID = "sectionID";
    private static final String CLASS_TYPE = "classTYpe";
    private static final String DAY = "day";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String BUILDING = "building";
    private static final String ROOM = "room";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ScheduleDB() {}


}
