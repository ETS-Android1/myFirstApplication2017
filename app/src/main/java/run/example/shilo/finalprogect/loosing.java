package run.example.shilo.finalprogect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;

public class loosing extends AppCompatActivity implements RewardedVideoAdListener {
    private AdView mAdView;
    String GameKind ;
    AnimationDrawable startAnimation;
    ImageButton startPic;
    boolean alreadyRevive=false;
    int Score,GamesCounter = 1,level;
    Intent go,Game;
    ImageView crown,crown1;
    TextView PlayerScore,PlayerBestScore,explain,LoadingVideo,kind;
    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    private RewardedVideoAd mRewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loosing);
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){}
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Score = getIntent().getIntExtra("Score",0);
        level = getIntent().getIntExtra("level",0);
        GameKind=getIntent().getStringExtra("kind");
        alreadyRevive=getIntent().getBooleanExtra("alreadyRevive",false);
        explain=(TextView) findViewById(R.id.textView);
        LoadingVideo=(TextView) findViewById(R.id.textView1);
        crown=(ImageView) findViewById(R.id.crown);
        crown1=(ImageView) findViewById(R.id.crown1);
        PlayerScore = (TextView) findViewById(R.id.PlayerScore);
        kind = (TextView) findViewById(R.id.kind);
        PlayerBestScore = (TextView) findViewById(R.id.PlayerBestScore);
        PlayerScore.setText("Score: " + Score);
        go = new Intent(this,MainActivity.class);
        Game=new Intent(this,Game.class);
        Typeface myfont1 = Typeface.createFromAsset(getAssets(), "quitemegical.ttf");
        kind.setText(GameKind+" Mode");
        kind.setTypeface(myfont1);
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        startPic = (ImageButton) findViewById(R.id.Btn2);
        startPic.setBackgroundResource(R.drawable.animation2);
        startAnimation = (AnimationDrawable) startPic.getBackground();
        PlayerScore.setTypeface(myfont1);
        PlayerBestScore.setTypeface(myfont1);
        explain.setTypeface(myfont1);
        LoadingVideo.setTypeface(myfont1);
        LoadingVideo.setVisibility(View.VISIBLE);
        startPic.setVisibility(View.GONE);
        crown.setVisibility(View.GONE);
        crown1.setVisibility(View.GONE);
        if (alreadyRevive){
            LoadingVideo.setVisibility(View.GONE);
            explain.setVisibility(View.GONE);}
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        PlayerScore.setText("Score:\n" + Score);
        if (GameKind.equals("Easy")) {
            //getting preferences
            int HighScore = prefs.getInt("easy", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("easy", Score);
                editor.commit();
                crown.setVisibility(View.VISIBLE);
                crown1.setVisibility(View.VISIBLE);
            }
            PlayerBestScore.setText("Best score:\n" + Math.max(Score, HighScore) );
        }
        else if (GameKind.equals("Medium")) {
            //getting preferences
            int HighScore = prefs.getInt("medium", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("medium", Score);
                editor.commit();
                crown.setVisibility(View.VISIBLE);
                crown1.setVisibility(View.VISIBLE);
            }
            PlayerBestScore.setText("Best score:\n" +Math.max(Score, HighScore) );
        }
        else if (GameKind.equals("Hard")) {
            //getting preferences
            int HighScore = prefs.getInt("hard", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hard", Score);
                editor.commit();
                crown.setVisibility(View.VISIBLE);
                crown1.setVisibility(View.VISIBLE);
            }
            PlayerBestScore.setText("Best score:\n" +Math.max(Score, HighScore));
        }
        if(!alreadyRevive){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();}
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        startAnimation.start();
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7682417086477843/1642917879",
                new AdRequest.Builder().build());

    }
    @Override
    public void onRewarded(RewardItem reward) {
        Game.putExtra("level",level);
        Game.putExtra("currentLife",0);
        Game.putExtra("Score",Score);
        Game.putExtra("kind",GameKind);
        Game.putExtra("combackToLife",true);
        startActivity(Game);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        LoadingVideo.setText("Failed to load the video "+errorCode);

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (!alreadyRevive) {
            startPic.setVisibility(View.VISIBLE);
            LoadingVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    @Override
    public void onBackPressed() { startActivity(go); }

    public void share(View view) {
        Intent myIntent= new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_SUBJECT,"SimpleCode | Run");
        myIntent.putExtra(Intent.EXTRA_TEXT,"Hey!!!\nI got "+ Score +" points in "+GameKind+" mode in  SkyRun.\nNow let's see you.\nFor download in Android press here https://play.google.com/store/apps/details?id=run.example.shilo.finalprogect \nSimpleCode | SkyRun");
        startActivity(Intent.createChooser(myIntent,"Share:"));
    }

    public void BackToMain(View view) {
        try {
            startActivity(go);
        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();
        } }

    public void Continue(View view) {
        if(mRewardedVideoAd.isLoaded())
            mRewardedVideoAd.show();
    }
}
