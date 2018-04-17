package pragma.android.kneemonitor_367;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.example.android_child_activity_367.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Child_Movement_Graph_activity extends Activity{
	
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN, Color.RED};
	//, Color.BLACK,Color.LTGRAY,Color.TRANSPARENT,Color.YELLOW,Color.WHITE};  
	  
//	private static double[] VALUES = new double[] { 10, 11, 12, 13, 15};  
	  
//	private static String[] NAME_LIST = new String[] { "Fridge", "Socket", "Knife", "Water","Kitchen" }; 
	
	//private static double[] VALUES = new double[50]; 
	  
	//private static String[] NAME_LIST = new String[50];
//	private static String[] str_values = new String[50];
	
	static ArrayList<Integer> ar_values=new ArrayList<Integer>();
	 static ArrayList<String> ar_namelist=new ArrayList<String>();
	  
	private CategorySeries mSeries = new CategorySeries("");  
	  
	private DefaultRenderer mRenderer = new DefaultRenderer();  
	  
	private GraphicalView mChartView; 
	
	DatabaseHelper helper;
//	private SQLiteDatabase database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph1);
		
		
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	     try{
	 		
			 GetChildMovement_info(this);
		
		
		mRenderer.setApplyBackgroundColor(true);  
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));  
		mRenderer.setChartTitleTextSize(20);  
		mRenderer.setLabelsTextSize(25);  
		mRenderer.setLegendTextSize(25);  
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });  
		mRenderer.setZoomButtonsVisible(true);  
	//	mRenderer.setStartAngle(90);  
		  
		for (int i = 0; i < ar_values.size(); i++) {  
			mSeries.add(ar_namelist.get(i) + " " + ar_values.get(i), ar_values.get(i));  
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();  
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);  
			mRenderer.addSeriesRenderer(renderer);  
			}  
		  
		if (mChartView != null) {  
		mChartView.repaint();  
		}  
	     }
			catch(Exception e){
				Toast.makeText(getApplicationContext(), "error" + e.getMessage(), Toast.LENGTH_LONG).show();
			}
	     
  
		}  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	    {        
	        switch (item.getItemId())
	        {
	      
	          case R.id.action_back:
	        	 
	        	  ar_namelist.clear();
	        	  ar_values.clear();
	        	  Intent i=new Intent(getApplicationContext(),MainActivity.class);
	        	  startActivity(i);
	        	  
	        	  
	            default:
	                
		            return super.onOptionsItemSelected(item);
	        }
	    }
		  
		@Override  
		protected void onResume() {  
		super.onResume();  
		if (mChartView == null) {  
		LinearLayout layout = (LinearLayout) findViewById(R.id.piechart);  
		mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);  
		mRenderer.setClickEnabled(true);  
		mRenderer.setSelectableBuffer(10);  
		  
		mChartView.setOnClickListener(new View.OnClickListener() {  
		@Override  
		public void onClick(View v) {  
		SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();  
		  
		if (seriesSelection == null) {  
		Toast.makeText(Child_Movement_Graph_activity.this,"No chart element was clicked",Toast.LENGTH_SHORT).show();  
		} else {  
		Toast.makeText(Child_Movement_Graph_activity.this,"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();  
		}  
		}  
		});  
		
		
		  
		mChartView.setOnLongClickListener(new View.OnLongClickListener() {  
		@Override  
		public boolean onLongClick(View v) {  
		SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();  
		if (seriesSelection == null) {  
		Toast.makeText(Child_Movement_Graph_activity.this,"No chart element was long pressed", Toast.LENGTH_SHORT);  
		return false;   
		} else {  
		Toast.makeText(Child_Movement_Graph_activity.this,"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT);  
		return true;         
		}  
		}  
		});  
		layout.addView(mChartView, new LayoutParams());  
		}  
		else {  
		mChartView.repaint();  
		}  
		}  
		public boolean GetChildMovement_info(Context context) {
			
			
			  
			
	    	helper = new DatabaseHelper(context);
		
		
		SQLiteDatabase db = helper.getReadableDatabase();

		
		//SELECT
		String[] columns = {"activitycode","count(activitycode)"};

		//WHERE clause
		String selection = "activitycode in (?,?,?,?,?)";//?,?,?,?,?)";
	//	String selection = "activity_code=? OR activity_code=? OR activity_code=? OR activity_code=? OR activity_code=?";
	//	String selection1= "username=sandy AND password=sandy";
	//	String selection = "activitycode=?";

		//WHERE clause arguments
		String[] selectionArgs = {"WK","RN","SI","WM","ST"};//,"WI","CR","RO","CU","CD"};
	//	String[] selectionArgs = {"RF"};
		
			
		String str_groupby = "activitycode";
		
		//Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

		Cursor cursor = null;
		try{
		//SELECT _id FROM login WHERE username=uname AND password=pass
		//	Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
		cursor = db.query(DatabaseHelper.Activity_Info_TABLE_NAME, columns, selection, selectionArgs, str_groupby, null,null);
	//	Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
		startManagingCursor(cursor);
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "error1:" + e.getMessage(), Toast.LENGTH_LONG).show();
			/*Toast.makeText(getApplicationContext(), "error1:" + e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "error1:" + e.getMessage(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "error1:" + e.getMessage(), Toast.LENGTH_LONG).show();
			*/
		}
		int numberOfRows = cursor.getCount();
		
			if(numberOfRows <= 0){

			Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
			return false;
		}

		cursor.moveToFirst();
		do{
			//int i=0;
		//	Toast.makeText(getApplicationContext(), "data present", Toast.LENGTH_SHORT).show();
			String str_activity_code=cursor.getString(cursor.getColumnIndex("activitycode"));
		//	Toast.makeText(getApplicationContext(), "Activity code:" + str_activity_code, Toast.LENGTH_SHORT).show();
			ar_namelist.add(str_activity_code);
			String str_value=cursor.getString(cursor.getColumnIndex("count(activitycode)"));
		//	Toast.makeText(getApplicationContext(), "count:" + str_value, Toast.LENGTH_SHORT).show();
			Integer value=Integer.parseInt(str_value);
			ar_values.add(value);
		//	i++;
			}while(cursor.moveToNext());
		
		return true;
	}
		
		 @Override
		 public void onBackPressed() {
		     // This will be called either automatically for you on 2.0
		     // or later, or by the code above on earlier versions of the
		     // platform.
		     return;
		 }
		}  