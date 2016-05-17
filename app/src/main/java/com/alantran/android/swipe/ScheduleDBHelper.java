package com.alantran.android.swipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alantran.android.swipe.ScheduleContract.ScheduleEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alantran on 5/16/16.
 */
public class ScheduleDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "schedule.db";
    private static final int DB_VERSION = 2;

    private static final String DROP_SCHEDULE_TABLE
            = "DROP TABLE IF EXISTS " + ScheduleEntry.TABLE_NAME;
    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + ScheduleEntry.TABLE_NAME + " (" +
            ScheduleEntry._ID + " INTEGER PRIMARY KEY," +
            ScheduleEntry.SECTION_ID + " TEXT NOT NULL, " +
            ScheduleEntry.CLASS_TYPE + " TEXT NOT NULL, " +
            ScheduleEntry.DAY + " TEXT NOT NULL, " +
            ScheduleEntry.START_TIME + " TEXT NOT NULL, " +
            ScheduleEntry.END_TIME + " TEXT NOT NULL, " +
            ScheduleEntry.BUILDING + " TEXT NOT NULL, " +
            ScheduleEntry.ROOM + " TEXT NOT NULL, " +
            ScheduleEntry.SCHEDULE_NUMBER + " INTEGER NOT NULL " +
            " );";

    public ScheduleDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SCHEDULE_TABLE);
        onCreate(db);
    }

    public ArrayList<Schedule> getSchedules(){
        Log.i("In getSchedules()", "Retrieve schedules from database");
        ArrayList<Schedule>  result = new ArrayList<>();
        SQLiteDatabase mDb = getReadableDatabase();
        String[] projections = {ScheduleEntry._ID, ScheduleEntry.SECTION_ID,
        ScheduleEntry.CLASS_TYPE, ScheduleEntry.DAY, ScheduleEntry.START_TIME,
        ScheduleEntry.END_TIME, ScheduleEntry.BUILDING, ScheduleEntry.ROOM, ScheduleEntry.SCHEDULE_NUMBER};

        Cursor c = mDb.query(ScheduleEntry.TABLE_NAME, projections, null, null,null, null, null);
        if (c.getCount() == 0 ) return  null;
        int COL_SECTION_ID_INDEX = c.getColumnIndex(ScheduleEntry.SECTION_ID);
        int COL_CLASSTYPE_INDEX = c.getColumnIndex(ScheduleEntry.CLASS_TYPE);
        int COL_DAY_INDEX = c.getColumnIndex(ScheduleEntry.DAY);
        int COL_START_TIME_INDEX = c.getColumnIndex(ScheduleEntry.START_TIME);
        int COL_END_TIME_INDEX = c.getColumnIndex(ScheduleEntry.END_TIME);
        int COL_BUILDING_INDEX = c.getColumnIndex(ScheduleEntry.BUILDING);
        int COL_ROOM_INDEX = c.getColumnIndex(ScheduleEntry.ROOM);
        int COL_SCHEDULE_NUMBER = c.getColumnIndex(ScheduleEntry.SCHEDULE_NUMBER);
        c.moveToFirst();
        Log.i("on getSchedules()", "table size " + c.getCount());

        // read the very first section
        List<Sections> currentList = new ArrayList<>();
        String sectionID = c.getString(COL_SECTION_ID_INDEX);
        String classType = c.getString(COL_CLASSTYPE_INDEX);
        String day = c.getString(COL_DAY_INDEX);
        String startTime = c.getString(COL_START_TIME_INDEX);
        String endTime = c.getString(COL_END_TIME_INDEX);
        String building = c.getString(COL_BUILDING_INDEX);
        String room = c.getString(COL_ROOM_INDEX);
        int scheduleNumber = c.getInt(COL_SCHEDULE_NUMBER);

        Sections currentSection = new Sections(sectionID,classType,day,startTime,endTime, building, room);
        Log.i("in getSchedules()", "First row of tha table is" + currentSection.toString());
        currentList.add(currentSection);
        c.moveToNext();
        for (int i = 2; i <= c.getCount(); i++){
            Log.i("Iterate cursor", "at row" + (i));
            int scheduleNumberNext = c.getInt(COL_SCHEDULE_NUMBER);
            sectionID = c.getString(COL_SECTION_ID_INDEX);
            classType = c.getString(COL_CLASSTYPE_INDEX);
            day = c.getString(COL_DAY_INDEX);
            startTime = c.getString(COL_START_TIME_INDEX);
            endTime = c.getString(COL_END_TIME_INDEX);
            building = c.getString(COL_BUILDING_INDEX);
            room = c.getString(COL_ROOM_INDEX);
            if (scheduleNumber == scheduleNumberNext) {

                currentList.add(new Sections(sectionID,classType,day,startTime,endTime, building, room));
                if (i == c.getCount() - 1){
                    Schedule schedule = new Schedule(scheduleNumber, currentList);
                    Log.i("Iterate by cursor", " Add to result : " + schedule.toString() );
                    result.add(schedule);
                }

            } else {

                Schedule schedule = new Schedule(scheduleNumber, currentList);
                result.add(schedule);
                Log.i("Iterate by cursor", " Add to result : " + schedule.toString());

                currentList = new ArrayList<>();
                currentList.add(new Sections(sectionID,classType,day,startTime,endTime, building, room));
                scheduleNumber = scheduleNumberNext;
            }

            c.moveToNext();

        }
        c.close();
        mDb.close();

        return result;

    }

    public void insertSchedules(ArrayList<Schedule> schedulesList){
        Log.i("insertSchedules()", "");
        SQLiteDatabase mDb = getWritableDatabase();
        mDb.execSQL(DROP_SCHEDULE_TABLE);
        mDb.execSQL(CREATE_SCHEDULE_TABLE);

        for(Schedule schedule : schedulesList) {
            // Create a new map of values, where column names are the keys
            for (Sections section : schedule.getSectionsList()) {
                ContentValues values = new ContentValues();
                values.put(ScheduleEntry.SECTION_ID, section.getSectionID());
                values.put(ScheduleEntry.CLASS_TYPE, section.getClassType());
                values.put(ScheduleEntry.DAY, section.getDays());
                values.put(ScheduleEntry.START_TIME, section.getStartTime());
                values.put(ScheduleEntry.END_TIME, section.getEndTime());
                values.put(ScheduleEntry.BUILDING, section.getBuilding());
                values.put(ScheduleEntry.ROOM, section.getRoom());
                values.put(ScheduleEntry.SCHEDULE_NUMBER, schedule.getNum());
                long newRowId = mDb.insert(ScheduleEntry.TABLE_NAME, null,values);

                Log.i("In insertSchedules() ", "newRowId " + newRowId + "  .Section " + section.toString());
            }


        }
        mDb.close();

    }

}
