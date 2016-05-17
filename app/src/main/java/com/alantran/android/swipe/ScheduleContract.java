package com.alantran.android.swipe;

import android.provider.BaseColumns;

/**
 * Created by alantran on 5/16/16.
 */
public class ScheduleContract {

    public static final class ScheduleEntry implements BaseColumns{
        public static final String TABLE_NAME = "schedule_table";

        public static final String SECTION_ID = "section_id";
        public static final String CLASS_TYPE = "class_type";
        public static final String DAY = "day";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String BUILDING = "building";
        public static final String ROOM = "room";
        public static final String SCHEDULE_NUMBER = "schedule_number";
    }

}
