package com.example.txtosync;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
 
public class MainActivity extends Activity {
  TextView welcome = null;
 
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   
         
    welcome = (TextView)findViewById(R.id.welcome);
    
    setContentView(R.layout.activity_main);
  }
 
}
