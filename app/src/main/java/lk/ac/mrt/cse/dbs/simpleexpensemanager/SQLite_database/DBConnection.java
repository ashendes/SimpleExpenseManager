package lk.ac.mrt.cse.dbs.simpleexpensemanager.SQLite_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 11/19/2017.
 */

public class DBConnection extends SQLiteOpenHelper {

    private static DBConnection db_singleton = null;
    private SQLiteDatabase db = null;
    private static int version = 1;

    private DBConnection(Context context) {
        super(context, DB_Constants.DB_name, null, version);
        db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DB_Constants.table_acc + "("
                + DB_Constants.ac_COL1 + " VARCHAR(10) PRIMARY KEY, "
                + DB_Constants.ac_COL2 + " VARCHAR(20) NOT NULL, "
                + DB_Constants.ac_COL3 + " VARCHAR(20) NOT NULL, "
                + DB_Constants.ac_COL4 + " DOUBLE(10,2));");

        sqLiteDatabase.execSQL("CREATE TABLE " + DB_Constants.table_trans + " ("
                + DB_Constants.tr_COL1 + " VARCHAR(10), "
                + DB_Constants.tr_COL2 + " DATE, "
                + DB_Constants.tr_COL3+ " VARCHAR(20), "
                + DB_Constants.tr_COL4 + " DOUBLE(10,2));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_Constants.table_acc + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_Constants.table_trans + ";");
        version++;
        onCreate(db);
    }

    public static DBConnection getConnection(Context context){
        if (db_singleton == null){
            db_singleton = new DBConnection(context);
        }
        return db_singleton;
    }

    public SQLiteDatabase getDb(){
        return db;
    }
}
