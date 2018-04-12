package com.xander.minitasker.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.xander.minitasker.MiniTasker;
import com.xander.minitasker.Tasker;
import com.xander.minitasker.UiTasker;

public class MainActivity extends AppCompatActivity {

  private TextView text;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    text = findViewById(R.id.text);

    MiniTasker.run(new Tasker() {
      @Override public void run() {
        Log.d("wxy", "i am in Tasker");
        Log.d("wxy", "Thread name:" + Thread.currentThread().getName());
      }
    });

    MiniTasker.runAndCallback(new UiTasker<String>() {
      @Override public String run() {
        Log.d("wxy", "i am in UiTasker");
        Log.d("wxy", "Thread name:" + Thread.currentThread().getName());
        return "I am UiTasker";
      }

      @Override public void callback(String result) {
        Log.d("wxy", "Thread name:" + Thread.currentThread().getName());
        text.setText(result);
      }
    });
  }
}
