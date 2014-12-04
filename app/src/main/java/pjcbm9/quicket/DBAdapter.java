package pjcbm9.quicket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    // Maintenance DB Fields
    public static final String KEY_ROWID_M = "_id";
    public static final int COL_ROWID_M = 0;

    // Main Table Fields
    public static final String KEY_TICKETNAME = "ticketname";
    public static final String KEY_TICKETTYPE = "tickettype";
    public static final String KEY_TICKETLOCATION = "ticketlocation";
    public static final String KEY_TICKETDATE = "ticketdate";
    public static final String KEY_TICKETASSIGNEE = "ticketassignee";
    public static final String KEY_TICKETDESCRIPTION = "ticketdescription";
    public static final String KEY_TICKETCUSTOMER = "ticketcustomer";
    public static final String KEY_STATUS = "ticketstatus";
    public static final String KEY_ID = "ticketid";
    public static final String KEY_COUNTDOWN = "ticketcountdown";
    // Main Table Columns
    public static final int COL_TICKETNAME = 1;
    public static final int COL_TICKETTYPE = 2;
    public static final int COL_TICKETLOCATION = 3;
    public static final int COL_TICKETDATE = 4;
    public static final int COL_TICKETASSIGNEE = 5;
    public static final int COL_TICKETDESCRIPTION = 6;
    public static final int COL_TICKETCUSTOMER = 7;
    public static final int COL_STATUS = 8;
    public static final int COL_ID = 9;
    public static final int COL_COUNTDOWN = 10;
    // Maintenance Table Fields
    public static final String KEY_INDEX = "item_index";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ITEMNAME = "item_name";
    // Maintenance Table Columns
    public static final int COL_INDEX = 1;
    public static final int COL_TYPE = 2;
    public static final int COL_ITEMNAME = 3;
    // All Keys for Main Tables
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TICKETNAME, KEY_TICKETTYPE,KEY_TICKETLOCATION,
            KEY_TICKETDATE,KEY_TICKETASSIGNEE,KEY_TICKETDESCRIPTION,KEY_TICKETCUSTOMER,KEY_STATUS, KEY_ID,KEY_COUNTDOWN};
    // All Keys for Maintenance Table
    public static final String[] ALL_MAINTENANCE_KEYS = new String[] {KEY_ROWID_M, KEY_INDEX, KEY_TYPE,KEY_ITEMNAME};
    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "TicketDataBase";
    public static final String DATABASE_TABLE2 = "mainTable2";
    public static final String DATABASE_TABLE_M = "maintenanceTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 3;
    // Create Main Table
    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE2
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TICKETNAME + " string not null, "
                    + KEY_TICKETTYPE + " string not null, "
                    + KEY_TICKETLOCATION + " string not null, "
                    + KEY_TICKETDATE + " long not null, "
                    + KEY_TICKETASSIGNEE + " string not null, "
                    + KEY_TICKETDESCRIPTION + " string not null, "
                    + KEY_TICKETCUSTOMER + " string not null, "
                    + KEY_STATUS + " string not null, "
                    + KEY_ID + " long not null, "
                    + KEY_COUNTDOWN + " long not null"
                    + ");";
    // Create Maintenance Table
    private static final String DATABASE_M_CREATE_SQL =
            "create table " + DATABASE_TABLE_M
                    + " (" + KEY_ROWID_M + " integer primary key autoincrement, "
                    + KEY_INDEX + " integer not null, "
                    + KEY_TYPE + " string not null, "
                    + KEY_ITEMNAME + " string not null"
                    + ");";
    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    //	Public methods:  ////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }
    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }
    /////////////////////////////////////////////////////////////////////
    //  FOR MAIN TABLE //////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    // Add a new set of values to the database.
    public long insertRow(String ticketName,String ticketType,String ticketLocation,
                          Long ticketDate,String ticketAssignee,String ticketDescription,
                          String ticketCustomer, String ticketStatus, Long ticketId, Long ticketCountdown) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TICKETNAME, ticketName);
        initialValues.put(KEY_TICKETTYPE, ticketType);
        initialValues.put(KEY_TICKETLOCATION, ticketLocation);
        initialValues.put(KEY_TICKETDATE, ticketDate);
        initialValues.put(KEY_TICKETASSIGNEE, ticketAssignee);
        initialValues.put(KEY_TICKETDESCRIPTION, ticketDescription);
        initialValues.put(KEY_TICKETCUSTOMER, ticketCustomer);
        initialValues.put(KEY_STATUS, ticketStatus);
        initialValues.put(KEY_ID, ticketId);
        initialValues.put(KEY_COUNTDOWN, ticketCountdown);
        // Insert it into the database.
        return db.insert(DATABASE_TABLE2, null, initialValues);
    }
    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE2, where, null) != 0;
    }
    // Delete all rows in the main table
    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }
    // Return all data from the maintable.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE2, ALL_KEYS,
                where, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE2, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId,String ticketName,String ticketType,String ticketLocation,
                             long ticketDate,String ticketAssignee,String ticketDescription,
                             String ticketCustomer, String ticketStatus, long ticketId, long ticketCountdown) {
        String where = KEY_ROWID + "=" + rowId;
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TICKETNAME, ticketName);
        newValues.put(KEY_TICKETTYPE, ticketType);
        newValues.put(KEY_TICKETLOCATION, ticketLocation);
        newValues.put(KEY_TICKETDATE, ticketDate);
        newValues.put(KEY_TICKETASSIGNEE, ticketAssignee);
        newValues.put(KEY_TICKETDESCRIPTION, ticketDescription);
        newValues.put(KEY_TICKETCUSTOMER, ticketCustomer);
        newValues.put(KEY_STATUS, ticketStatus);
        newValues.put(KEY_ID, ticketId);
        newValues.put(KEY_COUNTDOWN, ticketCountdown);
        // Insert it into the database.
        return db.update(DATABASE_TABLE2, newValues, where, null) != 0;
    }
    // Insert a new ticket into main table
    public void insertNewTicket(Ticket object) {
        insertRow(object.getTicketName(), object.getTicketType(), object.getIncidentLocation(),
                object.getTicketDate().getTime(), object.getAssignee(), object.getDescription(),
                object.getCustomerName(), object.getStatus().toString(), object.getID(), object.getCountDownDuration());
    }
    // delete ticket from main table
    public void deleteTicket(long id) {
        db.execSQL("DELETE FROM mainTable2 WHERE ticketid='"+ id +"'");
    }
    // update status of ticket in maintable
    public void updateTicketStatus(long ID,String Status){
        db.execSQL("UPDATE mainTable2 SET ticketstatus='"+ Status + "' WHERE ticketid='" + ID + "'");
    }
    // update ticket in maintable
    public void updateTicket(Ticket ticket) {
        db.execSQL("UPDATE mainTable2 SET ticketname='"+ ticket.getTicketName()
                + "',tickettype='" + ticket.getTicketType() + "',ticketlocation='" + ticket.getIncidentLocation()
                + "',ticketdate='" + ticket.getTicketDate().getTime() + "',ticketassignee='" + ticket.getAssignee()
                + "',ticketdescription='" + ticket.getDescription() + "',ticketcustomer='" + ticket.getCustomerName()
                + "',ticketstatus='" + ticket.getStatus().toString() + "' WHERE ticketid='" + ticket.getID()
                + "',ticketcountdown='" + ticket.getCountDownDuration() + "'");
    }
    // find ticket counts in maintable by status
    public int findTicketCountByStatus(Ticket.Status status){
        Cursor cursor = db.rawQuery("SELECT * FROM mainTable2 WHERE ticketstatus='" + status.toString() + "'", null);
        return cursor.getCount();
    }
    // update overdue tickets in maintable
    public void updateTickets() {
        Cursor cursor = db.rawQuery("SELECT * FROM mainTable2 WHERE ticketstatus='" + Ticket.Status.ACTIVE.toString() + "'", null);
        while(cursor.moveToNext()) {
            Date date = new Date(cursor.getLong(COL_TICKETDATE));
            long countdownduration;
            try{
                countdownduration = cursor.getLong(COL_COUNTDOWN);
            }catch(NullPointerException e){
                countdownduration = 86400000*7;
            }catch(IllegalStateException e){
                countdownduration = 86400000*7;
            }
            long DueDate = date.getTime() + (countdownduration);
            long rightNow = Calendar.getInstance().getTimeInMillis();
            if(DueDate <= rightNow){
                db.execSQL("UPDATE mainTable2 SET ticketstatus='"+ Ticket.Status.OVERDUE.toString()
                        + "' WHERE ticketid='" + cursor.getLong(COL_ID) + "'");
            }
        }
    }
    // find tickets in main table by status
    public ArrayList<Ticket> findTicketsByStatus(Ticket.Status status) {
        ArrayList<Ticket> ticketArrayList = new ArrayList<Ticket>();
        Cursor cursor = db.rawQuery("SELECT * FROM mainTable2 WHERE ticketstatus='" + status.toString() + "'", null);
        // iterate through all matches
        while(cursor.moveToNext()) {
            Ticket temp = new Ticket(cursor.getString(COL_TICKETNAME),
                    cursor.getString(COL_TICKETTYPE),
                    cursor.getString(COL_TICKETLOCATION),
                    new Date(cursor.getLong(COL_TICKETDATE)),
                    cursor.getString(COL_TICKETASSIGNEE),
                    cursor.getString(COL_TICKETDESCRIPTION),
                    cursor.getString(COL_TICKETCUSTOMER),
                    Ticket.Status.valueOf(cursor.getString(COL_STATUS)));
            temp.setID(cursor.getLong(COL_ID));
            try{
                temp.setCountDownDuration(cursor.getLong(COL_COUNTDOWN));
            }catch(NullPointerException e){
                temp.setCountDownDuration(86400000*7);
            }catch(IllegalStateException e){
                temp.setCountDownDuration(86400000*7);
            }
            ticketArrayList.add(temp);
        }
        return ticketArrayList;
    }
    public Ticket getTicket(long ticketID){
        Cursor cursor = db.rawQuery("SELECT * FROM mainTable2 WHERE ticketid='" + ticketID + "'", null);
        cursor.moveToNext();
        Ticket temp = new Ticket(cursor.getString(COL_TICKETNAME),
                cursor.getString(COL_TICKETTYPE),
                cursor.getString(COL_TICKETLOCATION),
                new Date(cursor.getLong(COL_TICKETDATE)),
                cursor.getString(COL_TICKETASSIGNEE),
                cursor.getString(COL_TICKETDESCRIPTION),
                cursor.getString(COL_TICKETCUSTOMER),
                Ticket.Status.valueOf(cursor.getString(COL_STATUS)));
        temp.setID(cursor.getLong(COL_ID));
        try{
            temp.setCountDownDuration(cursor.getLong(COL_COUNTDOWN));
        }catch(NullPointerException e){
            temp.setCountDownDuration(86400000*7);
        }catch(IllegalStateException e){
            temp.setCountDownDuration(86400000*7);
        }
        return temp;
    }
    public ArrayList<Ticket> findTicketsByKeyword(String keyword) {
        ArrayList<Ticket> ticketArrayList = new ArrayList<Ticket>();
        Cursor cursor = db.rawQuery("SELECT * FROM mainTable2 WHERE ticketname LIKE '%" + keyword
                + "%' OR tickettype LIKE '%" + keyword + "%' OR ticketlocation LIKE '%" + keyword
                + "%' OR ticketdate LIKE '%" + keyword + "%' OR ticketassignee LIKE '%" + keyword
                + "%' OR ticketdescription LIKE '%" + keyword + "%' OR ticketcustomer LIKE '%" + keyword
                + "%' OR ticketstatus LIKE '%" + keyword + "%' OR ticketid LIKE '%" + keyword + "%'", null);
        while(cursor.moveToNext()) {
            Ticket temp = new Ticket(cursor.getString(COL_TICKETNAME),
                    cursor.getString(COL_TICKETTYPE),
                    cursor.getString(COL_TICKETLOCATION),
                    new Date(cursor.getLong(COL_TICKETDATE)),
                    cursor.getString(COL_TICKETASSIGNEE),
                    cursor.getString(COL_TICKETDESCRIPTION),
                    cursor.getString(COL_TICKETCUSTOMER),
                    Ticket.Status.valueOf(cursor.getString(COL_STATUS)));
            temp.setID(cursor.getLong(COL_ID));
            try{
                temp.setCountDownDuration(cursor.getLong(COL_COUNTDOWN));
            }catch(NullPointerException e){
                temp.setCountDownDuration(86400000*7);
            }catch(IllegalStateException e){
                temp.setCountDownDuration(86400000*7);
            }

            ticketArrayList.add(temp);
        }
        return ticketArrayList;
    }
    /////////////////////////////////////////////////////////////////////
    //  FOR MAINTENANCE TABLE ///////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    // Add a new set of values to the database.
    public long insertMaintenanceRow(MItem item) {
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_INDEX, item.getIndex());
        initialValues.put(KEY_TYPE, item.getType().toString());
        initialValues.put(KEY_ITEMNAME, item.getItemName());
        // Insert it into the database.
        return db.insert(DATABASE_TABLE_M, null, initialValues);
    }
    public void insertMItemArray(ArrayList<MItem> items){
        for(MItem item: items){
            insertMaintenanceRow(item);
        }
    }
    // Delete a row from the database, by rowId (primary key)
    public boolean deleteMaintenanceRow(long rowId) {
        String where = KEY_ROWID_M + "=" + rowId;
        return db.delete(DATABASE_TABLE_M, where, null) != 0;
    }
    public void deleteMaintenanceItemsByType(String type){
        db.execSQL("DELETE FROM maintenanceTable WHERE type='"+ type +"'");
    }
    // return array of maintenance items association with given mtype
    public ArrayList<MItem> findMaintenanceItemsByType(String type) {
        ArrayList<MItem> mItemArray = new ArrayList<MItem>();
        Cursor cursor = db.rawQuery("SELECT * FROM maintenanceTable WHERE type='" + type + "'", null);
        // iterate through all matches
        while(cursor.moveToNext()) {
            MItem temp = new MItem(cursor.getInt(COL_INDEX),
                    MItem.mType.valueOf(cursor.getString(COL_TYPE)),
                    cursor.getString(COL_ITEMNAME));
            mItemArray.add(temp);
        }
        return mItemArray;
    }
    public void updateMItem(MItem item) {
        db.execSQL("UPDATE maintenanceTable SET item_name='"+ item.getItemName()
                + "' WHERE item_index='" + item.getIndex() + "'"
                + "AND type='" + item.getType().toString() + "'");
    }
    public void deleteMItem(MItem item) {
        db.execSQL("DELETE FROM maintenanceTable SET item_name='"+ item.getItemName()
                + "' WHERE item_index='" + item.getIndex() + "'"
                + "AND type='" + item.getType().toString() + "'");
    }
    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_M_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_M);
            // Recreate new database:
            onCreate(_db);
        }
    }
}
