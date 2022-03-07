package run.example.shilo.finalprogect;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MoreChance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        Intent go = new Intent(this,Game.class);
        ArrayList<String> a = new ArrayList<>();
        a.add(getIntent().getStringExtra("kind"));
        boolean StartANewGame = getIntent().getBooleanExtra("StartANewGame",false);
        if (StartANewGame){
            a.add("main");
        }
        else{
            a.add("MoreChane");
            a.add(""+getIntent().getIntExtra("Score",0));
            a.add(""+getIntent().getIntExtra("level",0));
            a.add(getIntent().getStringExtra("AlreadyRevive"));
            a.add(getIntent().getStringExtra("BreakTheBest"));
        }
        go.putExtra("kind",a);
        startActivity(go);
    }
}