package pragma.android.kneemonitor_367;




import com.example.android_child_activity_367.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class Login_activity extends Activity {
			
		Button btnlogin,btnclear;
		EditText txtusername,txtpassword;
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.firstpage);
				try{
					
					btnlogin=(Button)findViewById(R.id.btnlogin);
					btnclear=(Button)findViewById(R.id.btncanel);
					txtusername=(EditText)findViewById(R.id.txtusername);
				  txtpassword=(EditText)findViewById(R.id.txtpassword);
				
				}
				
				catch(Exception ex)
				{
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();

				}
			}
				
			

			public void login(View V)
			{
				
				String s1="admin";
				String s2="admin";
						
					try{
			
				if(s1.equals(txtusername .getText().toString())&&s2.equals(txtpassword.getText().toString()))
					
				{
					

					
					Toast.makeText(getApplicationContext(), "login succefully", Toast.LENGTH_LONG).show();
				
					Intent a1=new Intent(getApplicationContext(),MainActivity.class);
				//	a1.putExtra("ID", "2");
					startActivity(a1);
				}
					
				
				
					
					else
				{
					
					
					
			}
				
					}
					catch(Exception e){
						Toast.makeText(getApplicationContext(), "login falied", Toast.LENGTH_LONG).show();
					}
	}
			
		 public void clear(View V)
		 {
			
				txtpassword.setText("");
				txtusername.setText("");
				Toast.makeText(getApplicationContext(), "data is cleared", Toast.LENGTH_LONG).show();
			 
		 }
		

		
		
		}
		

	
	

