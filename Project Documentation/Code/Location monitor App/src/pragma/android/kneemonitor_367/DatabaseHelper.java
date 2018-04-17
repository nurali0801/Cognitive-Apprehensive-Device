/** 
 * The code written in this file was intended to serve as an EXAMPLE. You are
 * free to modify and use code in this file. However, the this is also provided 
 * without ANY WARRANTY, EXPRESSED OR IMPLIED, INCLUDING ANY WARRANTIES OR 
 * CONDITIONS OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSES. Users of 
 * the code contained in this file assume the risks and costs of, including but
 * not limited to, any program errors, data loss or interruption of operations.
 *
 * This was written for the Android "Using the SQLite Database with ListView" tutorial at:
 * http://kahdev.wordpress.com/2010/09/27/android-using-the-sqlite-database-with-listview
 */
package pragma.android.kneemonitor_367;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseHelper extends SQLiteOpenHelper {
	
	 public static final String Activity_Info_TABLE_NAME = "activity_info";
	
	 
	public DatabaseHelper(Context context) {
		
		
		super(context, "Child_Activity.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + Activity_Info_TABLE_NAME + "(" 
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, activityname VARCHAR, activitycode VARCHAR)");
		
		
		/*db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Refregerator', 'RF')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Refregerator', 'RF')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Water', 'WT')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is in Kitchen', 'WT')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is using Knife', 'HM')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Refregerator', 'RF')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Refregerator', 'RF')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is near to Water', 'WT')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is in Kitchen', 'WT')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is using Knife', 'HM')");
		
		
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Still', 'SS')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Still', 'SS')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Up', 'SU')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Still', 'SS')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Still', 'SS')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Wiggling', 'WI')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Standing Still', 'SS')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Rolling', 'RO')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Climbing Up', 'CU')");
		db.execSQL("INSERT INTO activity_info (activityname, activitycode) VALUES ('Child is Climbing Down', 'CD')");*/
		
			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Steps to upgrade the database for the new version ...
	}
}

