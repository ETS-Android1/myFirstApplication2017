package run.example.shilo.finalprogect;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

//import androidx.appcompat.app.AppCompatActivity;

public class OpenActivity extends AppCompatActivity {
    Timer T;
    int count =0;
    Intent go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){}

        go = new Intent(this,MainActivity.class);
        T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (count >=0)
                            StopOpenGame();
                        count++;
                    }
                });
            }
        }, 500, 500);
        }
        catch (Exception e){}

    }
    public void StopOpenGame(){
        T.cancel();
        startActivity(go);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
