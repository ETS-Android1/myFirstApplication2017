//W"HG

package run.example.shilo.finalprogect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static java.lang.Integer.parseInt;


// SurfaceViewExample

public class Game extends AppCompatActivity implements ExampleDialog.ExampleDialogListener, RewardedVideoAdListener {

    boolean StopEnemies = false,MakeAudio = false,MakeVibration = false,PlayedAlredy = false,StartGame = false,b = false,alreadyRevive=false,combackToLife=false;
    private static final String TAG = "Game";
    boolean BigRoad = true;
    int max_health = 4, current_health = 3,turns = 0,level = 0,Moving = 600,Xresume = -999,SaveScore = 0;
    // last plase - to save that two golems dosen't one after other in the same row
    int Score = 0,LastPlce = -100,TimeToStart = 0,NumToShow = 3;
    MediaPlayer passRoad ,Levelup,getfeatures,hit;
    Intent Loosing,g,BackToMain;
    DisplayMetrics dm;
    // choose the game kind (normal,easy,hard)
    String GameKind,ComeFrome;
    MyView v;
    Bitmap golemstone1,golemstone,heart1,heart,beckground,pause,pause1,resume
            ,resume1,beckground1,playerfirst,playersecond,playerthird,playerfirst1,playerthird1,playersecond1;
    Bitmap [] playerpositions;
    ArrayList<Stone> stones;
    ArrayList<Health> hearts;
    ArrayList<Stone> AllGameStones;
    ArrayList<Stone> AllLifeFeatures;
    Sprite sprite;
    int srcY = 0;
    int srcX = 0;
    Rect src ;
    Rect dst ;
    ConstraintLayout c ;
    FrameLayout frm;
    Paint paint,paint1,paint2,paint3,paint4;
    Typeface myfont1 ;
    int xPos ,xPosSpeedUp;
    int yPos ,yPosSpeedUp;
    int width,height;
    int middel;
    double screenInches;
    boolean show_disqualification = false;
    int InvisibilityTurns = 2,StartTurns = 0;
    boolean[] invisibilty;

    // ----------------
    //private AdView mAdView;
    AnimationDrawable startAnimation;
    ImageButton startPic;
    ImageView crown,crown1;
    TextView PlayerScore,PlayerBestScore,explain,LoadingVideo,kind;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        v = new MyView(this);
        setContentView(R.layout.game_layout);
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
        }
        //intents
        BackToMain = new Intent(this, MainActivity.class);
        Loosing = new Intent(this, loosing.class);
        g = new Intent(this,MoreChance.class);
        //

        //display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;

        middel=(width/2);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(width / dm.xdpi, 2);
        double y = Math.pow(height / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        screenInches = screenInches / 5;
        //
        //paints
        myfont1 = Typeface.createFromAsset(getAssets(), "quitemegical.ttf");
        paint=new Paint();
        paint.setColor(Color.parseColor("#C3A0E5FF"));
        paint1 = new Paint();
        paint1.setTextSize((int) (width / 4));
        paint1.setColor(Color.WHITE);
        paint1.setTypeface(myfont1);
        xPosSpeedUp = width / 2 - (int) (paint1.measureText("Speed up") / 2);
        yPosSpeedUp = (int) ((height / 2) - ((paint1.descent() + paint1.ascent()) / 2));
        paint2=new Paint();
        paint2.setTextSize((int) (width / 10));
        paint2.setColor(Color.BLACK);
        paint2.setTypeface(myfont1);
        paint3 = new Paint();
        paint3.setTextSize((int) (width / 9));
        paint3.setColor(Color.BLACK);
        paint3.setTypeface(myfont1);
        paint4=new Paint();
        paint4.setTextSize((int) (width / 6));
        paint4.setColor(Color.BLACK);
        paint4.setTypeface(myfont1);
        xPos = width / 2 - (int) (paint3.measureText("Tap to continue") / 2);
        yPos = (int) ((height/ 2) - ((paint3.descent() + paint3.ascent()) / 2));
        //

        //media
        getfeatures = MediaPlayer.create(this, R.raw.getfeatures);
        passRoad = MediaPlayer.create(this, R.raw.passroad1);
        Levelup = MediaPlayer.create(this, R.raw.levelup1);
        hit = MediaPlayer.create(this, R.raw.hit);
        //

        //Bitmap


        beckground = BitmapPic(R.drawable.skyback12);
        beckground1 = Bitmap.createScaledBitmap(beckground, width, height, false);

        playerfirst = BitmapPic(R.drawable.playerfirst);
        playerfirst1 =Bitmap.createScaledBitmap (playerfirst,(int)(playerfirst.getWidth()*screenInches), (int)(playerfirst.getHeight()*screenInches), false);

        playersecond = BitmapPic(R.drawable.playersecond);
        playersecond1 =Bitmap.createScaledBitmap (playersecond,(int)(playersecond.getWidth()*screenInches), (int)(playersecond.getHeight()*screenInches), false);

        playerthird = BitmapPic(R.drawable.playerthird);
        playerthird1 =Bitmap.createScaledBitmap (playerthird,(int)(playerthird.getWidth()*screenInches), (int)(playerthird.getHeight()*screenInches), false);

        golemstone=BitmapPic(R.drawable.power);
        golemstone1 = Bitmap.createScaledBitmap(golemstone, (int) (golemstone.getWidth() * (screenInches/5)*4), (int) (golemstone.getHeight() * (screenInches/5)*4), false);

        heart = BitmapPic(R.drawable.life);
        heart1 = Bitmap.createScaledBitmap(heart, (int) (heart.getWidth() * screenInches), (int) (heart.getHeight() * screenInches), false);

        pause = BitmapPic(R.drawable.pause);
        pause1 = Bitmap.createScaledBitmap(pause, (int) (pause.getWidth() * screenInches), (int) (pause.getHeight() * screenInches), false);

        resume = BitmapPic(R.drawable.resum);
        resume1 = Bitmap.createScaledBitmap(resume, (int) (resume.getWidth() * screenInches), (int) (resume.getHeight() * screenInches), false);
        //

        c = findViewById(R.id.linearLayout);
        frm = (FrameLayout) findViewById(R.id.frame);
        frm.addView(v);

        src= new Rect(srcX, srcY, srcX + beckground1.getWidth(), srcY + beckground1.getHeight());
        dst = new Rect(0, 0, beckground1.getWidth(), beckground1.getHeight());

        AllGameStones = new ArrayList<>();
        AllLifeFeatures = new ArrayList<>();
        stones = new ArrayList<>();
        hearts = new ArrayList<>();
        playerpositions = new Bitmap[]{playerfirst1, playersecond1, playerthird1, playersecond1};
        invisibilty = new boolean[15];

        GameKind = getIntent().getStringArrayListExtra("kind").get(0);
        ComeFrome = getIntent().getStringArrayListExtra("kind").get(1);
        combackToLife = getIntent().getBooleanExtra("combackToLife", false);


        if (GameKind.equals("Medium")) {
            level = 4;
            max_health = 4;
            current_health = 3;
        } else if (GameKind.equals("Hard")) {
            level = 7;
            max_health = 4;
            current_health = 2;
        }
        if (combackToLife) {
            Score = getIntent().getIntExtra("Score", 0);
            level = getIntent().getIntExtra("level", 0);
            current_health = 0;
            alreadyRevive = true;
        }
        getSettingsData();
        // -----------------------------------------
        //mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        explain=(TextView) findViewById(R.id.textView);
        LoadingVideo=(TextView) findViewById(R.id.textView1);
        crown=(ImageView) findViewById(R.id.crown);
        crown1=(ImageView) findViewById(R.id.crown1);
        kind = (TextView) findViewById(R.id.kind);
        Typeface myfont1 = Typeface.createFromAsset(getAssets(), "quitemegical.ttf");
        kind.setText(GameKind+" Mode");
        kind.setTypeface(myfont1);
        // Use an activity context to get the rewarded video instance.

        startPic = (ImageButton) findViewById(R.id.Btn2);
        startPic.setBackgroundResource(R.drawable.animation2);
        startAnimation = (AnimationDrawable) startPic.getBackground();
        PlayerBestScore = (TextView) findViewById(R.id.PlayerBestScore);
        PlayerScore = (TextView) findViewById(R.id.PlayerScore);
        PlayerScore.setText("Score: " + Score);
        PlayerScore.setTypeface(myfont1);
        PlayerBestScore.setTypeface(myfont1);
        explain.setTypeface(myfont1);
        LoadingVideo.setTypeface(myfont1);
        LoadingVideo.setVisibility(View.VISIBLE);
        startPic.setVisibility(View.GONE);
        crown.setVisibility(View.GONE);
        crown1.setVisibility(View.GONE);
        SaveTheBestScores();
        try{
            if (ComeFrome.equals("MoreChane") && getIntent().getStringArrayListExtra("kind").get(4) != null){
                LoadingVideo.setVisibility(View.GONE);
                explain.setVisibility(View.GONE);
                alreadyRevive = true;
            }}catch (Exception e){}
        //--------------------
    }
    public void SaveTheBestScores(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        String score = "0";
        if (ComeFrome.equals("MoreChane"))
            score = getIntent().getStringArrayListExtra("kind").get(2);
        try{
            PlayerScore.setText("Score:\n" + score);}catch (Exception e){}
        int HighScore = 0;
        if (GameKind.equals("Easy")) {
            //getting preferences
            HighScore = prefs.getInt("easy", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("easy", Score);
                editor.commit();
                g.putExtra("BreakTheBest","true");
            }
        }
        else if (GameKind.equals("Medium")) {
            //getting preferences
            HighScore = prefs.getInt("medium", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("medium", Score);
                editor.commit();
                g.putExtra("BreakTheBest","true");
            }
        }
        else if (GameKind.equals("Hard")) {
            //getting preferences
            HighScore = prefs.getInt("hard", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hard", Score);
                editor.commit();
                g.putExtra("BreakTheBest","true");
            }
        }
        try{
            PlayerBestScore.setText("Best score:\n" +Math.max(Score, HighScore));}
        catch (Exception e){}
    }
    public Bitmap BitmapPic(int res){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), res, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return decodeSampledBitmapFromResource(getResources(),res, imageWidth, imageHeight);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public void getSettingsData(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        MakeVibration=prefs.getBoolean("vibration", true);
        MakeAudio=prefs.getBoolean("audio", true);
    }

    @Override
    public void onBackPressed() {
        if (!ComeFrome.equals("MoreChane"))
            sprite.ResumPause();
    }

    protected void onPause() {
        super.onPause();
        v.pause();
    }

    protected void onResume(){
        super.onResume();
        v.resume();
    }

    @Override
    public void onYesClicked() {
        sprite.ResumPause();
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        if (GameKind.equals("Easy")) {
            //getting preferences
            int HighScore = prefs.getInt("easy", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("easy", Score);
                editor.commit();
            }
        }
        else if (GameKind.equals("Medium")) {
            //getting preferences
            int HighScore = prefs.getInt("medium", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("medium", Score);
                editor.commit();
            }
        }
        else if (GameKind.equals("Hard")) {
            //getting preferences
            int HighScore = prefs.getInt("hard", 0); //0 is the default value
            if (Score > HighScore) {
                //setting preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("hard", Score);
                editor.commit();
            }
        }
        startActivity(BackToMain);
    }

    public void openDialog() {
        ExampleDialog dialog = new ExampleDialog();
        //dialog.show(getSupportFragmentManager(), "example dialog");
        dialog.show(getSupportFragmentManager(),"example dialog");
    }

    public void Continue(View view) {
        if(mRewardedVideoAd.isLoaded())
            mRewardedVideoAd.show();
    }

    public void share(View view) {
        Intent myIntent= new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_SUBJECT,"SimpleCode | Run");
        myIntent.putExtra(Intent.EXTRA_TEXT,"Hey!!!\nI got "+ Score +" points in "+GameKind+" mode in  SkyRun.\nNow let's see you.\nFor download in Android press here https://play.google.com/store/apps/details?id=run.example.shilo.finalprogect \nSimpleCode | SkyRun");
        startActivity(Intent.createChooser(myIntent,"Share:"));
    }

    public void BackToMain(View view) {
        try {
            startActivity(BackToMain);
        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();
        } }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7682417086477843/1642917879",
                new AdRequest.Builder().build());

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        startAnimation.start();
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
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        ComeFrome = "Ad";
        level = parseInt(getIntent().getStringArrayListExtra("kind").get(3));
        Score = parseInt(getIntent().getStringArrayListExtra("kind").get(2));
        current_health = 0;
        stones = new ArrayList<>();
        v.run();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        LoadingVideo.setText("Failed to load the video ");
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public void RestartTheGame(View view) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
        editor.commit();
        g.putExtra("kind", GameKind);
        g.putExtra("StartANewGame", true);
        startActivity(g);
    }
    public void SaveTheCountOfLifeFeature(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CountOfLifeFeature", prefs.getInt("CountOfLifeFeature", 1)+1);
        if (prefs.getInt("CountOfLifeFeature", 1) > 5)
            editor.putInt("CountOfLifeFeature", 5);
        editor.commit();
    }
    public void SaveTheCountOfStones(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("CountOfStones", prefs.getInt("CountOfStones", 1)+1);
        if (prefs.getInt("CountOfStones", 1) > 15)
            editor.putInt("CountOfStones", 15);
        editor.commit();
    }
    public int LifesCount(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        return prefs.getInt("CountOfLifeFeature", 1);
    }
    public int StonesCount(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        return prefs.getInt("CountOfStones", 1);
    }

    //----- my new Class for my obj ------------
    public class MyView extends SurfaceView implements Runnable{
        // usefull vars
        Thread t = null;
        Canvas canvas=null;
        SurfaceHolder holder;
        boolean isItOk = false, SpriteCreated = false;
        public void Dialog(){ openDialog(); }

        public MyView(Context context) {
            super(context);
            holder = getHolder();
            holder.setFixedSize(width, height);
        }

        public void run() {
            Log.d("aaa",ComeFrome + " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            if (ComeFrome.equals("main") || ComeFrome.equals("Ad")){
                c.setVisibility(View.GONE);
            }
            else{
                Score = parseInt(getIntent().getStringArrayListExtra("kind").get(2));
                StartGame = true;
                if (getIntent().getStringArrayListExtra("kind").get(5) != null){
                    crown.setVisibility(View.VISIBLE);
                    crown1.setVisibility(View.VISIBLE);
                }
                // sent to more chance that brack the best score and when back here show the crown. //
                c.setBackgroundResource(R.drawable.b);
                c.setVisibility(View.VISIBLE);

                //loosingaa = true;
            }
            while (isItOk) {
                if (!holder.getSurface().isValid())
                    continue;

                if (!SpriteCreated) {
                    // --------------Creat the player ---------------

                    sprite = new Sprite(MyView.this, playerpositions, Xresume, resume1.getWidth(), resume1.getHeight(), height, width);

                    // -------------- Create the life ---------------

                    for (int i = 0; i < max_health+2; i++) {
                        hearts.add(new Health(MyView.this, heart1, i));
                    }

                    // ------ Restarting the invisibility array -----

                    for (int i = 0; i < invisibilty.length; i++) {
                        invisibilty[i] = (i % 2 == 0);
                    }

                    // --------- create max feature and stunes --------------
                    int count_of_stones = StonesCount(),count_of_LifeFeatures =  LifesCount();
                    Log.e("aaa",count_of_stones +"  "+count_of_LifeFeatures);
                    for (int i =0; i< count_of_stones;i++){
                        AllGameStones.add(new Stone(v, golemstone1, 0, sprite.wight, height, "Stone", level));
                        if (count_of_stones - i <=  count_of_LifeFeatures)
                            AllLifeFeatures.add(new Stone(v, heart1, 0, sprite.wight, height, "LifeFeature", level));
                    }

                    // Make it stop came here
                    SpriteCreated = true;
                }
                if (sprite.HomeButtonPushed.equals("pause"))
                    pause();
                if (StartGame) {
                    // Make an audio of moving
                    if (sprite.MoveThePlayer && MakeAudio) passRoad.start();
                    sprite.MoveThePlayer = false;
                    //-----------------Create a new stones--------------------
                    double num1=(2.5+level/10);
                    if(BigRoad)
                        num1=(2.5+level/7);

                    if ((stones.isEmpty() || stones.get(stones.size() - 1).getY() > (playerfirst1.getHeight() * num1) )&& !StopEnemies){
                        Random rand = new Random();
                        int n, number;
                        // add place to run  ------to ckeck the player is ton stay in the Special plases when the level is down-----
                        if (((level >= 6) && BigRoad) || (sprite.getX() < (sprite.InitialLoc - sprite.wight)) || (sprite.getX() > sprite.InitialLoc + sprite.wight)) {
                            n = rand.nextInt(5);
                            while ((n - 2) == LastPlce)
                                n = rand.nextInt(5);
                            n = n - 2;
                        }
                        // only tree places to run
                        else {
                            n = rand.nextInt(3);
                            while ((n - 1) == LastPlce)
                                n = rand.nextInt(3);
                            n = n - 1;
                        }
                        LastPlce = n;
                        int num = 2 * level;
                        if (num > 20)
                            num = 20;
                        number = rand.nextInt(90 - num);
                        if (number == 0 && current_health <= max_health ){
                            // new Stone(MyView.this, heart1, n, sprite.wight, height, "LifeFeature", level)
                            /* SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("easy", Score);
                editor.commit();*/
                            if (!AllLifeFeatures.isEmpty()){
                                AllLifeFeatures.get(AllLifeFeatures.size()-1).UpdateInitialLoc(n);
                                stones.add(AllLifeFeatures.get(AllLifeFeatures.size()-1));
                                AllLifeFeatures.remove(AllLifeFeatures.get(AllLifeFeatures.size()-1));
                            }else{
                                stones.add(new Stone(MyView.this, heart1, n, sprite.wight, height, "LifeFeature", level));
                                SaveTheCountOfLifeFeature();
                            }
                        }
                        else {
                            if (!AllGameStones.isEmpty()){
                                // new Stone(v, golemstone1, 0, sprite.wight, height, "Stone", level)
                                AllGameStones.get(AllGameStones.size()-1).UpdateInitialLoc(n);
                                stones.add(AllGameStones.get(AllGameStones.size()-1));
                                AllGameStones.remove(AllGameStones.get(AllGameStones.size()-1));
                            }else{
                                stones.add(new Stone(v, golemstone1, n, sprite.wight, height, "Stone", level));
                                SaveTheCountOfStones();
                            }

                        }
                    }
                    // update the violence of the objects
                    for (int i = 0; i < stones.size(); i++) {
                        double lv = stones.get(i).get_level();
                        while (lv < level) {
                            stones.get(i).level_up();
                            lv++;
                        }
                    }
                    // ---remove the out screen objects---
                    for (int i = 0; i < stones.size(); i++) {
                        if (!stones.get(i).InScreen()){
                            stones.get(i).Reset();
                            if (stones.get(i).TAG.equals("LifeFeature"))
                                AllLifeFeatures.add(stones.get(i));
                            else if (stones.get(i).TAG.equals("Stone"))
                                AllGameStones.add(stones.get(i));
                            stones.remove(stones.get(i));
                        }
                    }
                    //----------------------------------------
                    SharedPreferences prefs = getContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    if(GameKind.equals("Medium")&&prefs.getInt("medium", 0)<Score)
                        paint2.setColor(Color.RED);
                    if(GameKind.equals("Hard")&&prefs.getInt("hard", 0)<Score)
                        paint2.setColor(Color.RED);
                    if(GameKind.equals("Easy")&&prefs.getInt("easy", 0)<Score)
                        paint2.setColor(Color.RED);
                    try {
                        synchronized (holder){
                            canvas = holder.lockCanvas();
                            onDraw1(canvas);
                        }
                    } catch(Exception ex) {
                        Log.d(TAG, "Error", ex);
                    } finally {
                        if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                    // check if player get feature
                    check_disqualification();
                    // change the rode
                    // Calculates the score
                    turns += 1;
                    SaveScore += (level) / 3 + 1;
                    if (SaveScore > 7) {
                        SaveScore = 0;
                        Score++;
                    }
                    if (turns % 400 == 0) {
                        StopEnemies = true;
                        level += 1; // save the meters the player go

                    }
                    if (level % 3 == 0)
                        if ((level / 3) % 2 == 0)
                            BigRoad = true;
                        else
                            BigRoad = false;
                    sprite.BigRoad = BigRoad;

                    // the enemy stop until the canvas empty
                    if (StopEnemies && stones.size() <= 0) {
                        StopEnemies = false;
                    }
                } else {
                    sprite.GameStop = true;
                    TimeToStart++;
                    Canvas canvas = holder.lockCanvas();
                    onDraw1(canvas);
                    holder.unlockCanvasAndPost(canvas);
                    if (TimeToStart > 60) {
                        StartGame = true;
                        TimeToStart = 0;
                        NumToShow = 3;
                        sprite.GameStop = false;
                    }
                }
                if (ComeFrome.equals("MoreChane"))
                    isItOk=false;
            }// while
        }//run

        protected void onDraw1(Canvas canvas){
            try {
                // --------------------- draw beckground ------------------

                canvas.drawBitmap(beckground1, src, dst, null);

                //----------------------draw a road------------------------

                double road = 1;
                if (level >= 6 && BigRoad)
                    road = 1.6;
                canvas.drawRect((int) (middel - ((playerfirst.getWidth()) * 1.5 * road + 20) * screenInches), 0, (int) (middel + ((playerfirst.getWidth()) * 1.5 * road + 20) * screenInches), height, paint);

                // ------------------heart-------------------

                for (int i = 0; i < current_health; i++) {
                    hearts.get(i).onDraw(canvas);
                }

                // -------------------golems------------------

                for (int i = 0; i < stones.size(); i++) {
                    if (!StartGame)
                        stones.get(i).GameStop = true;
                    else
                        stones.get(i).GameStop = false;
                    stones.get(i).onDraw(canvas);

                }

                // --------------------player------------------

                if ((!show_disqualification) || StartTurns >= invisibilty.length) {
                    sprite.onDraw(canvas, level);
                    StartTurns = 0;
                    show_disqualification = false;
                } else {
                    if (!invisibilty[StartTurns])
                        sprite.onDraw(canvas, level);
                    InvisibilityTurns -= 1;
                    if (InvisibilityTurns == 0) {
                        StartTurns += 1;
                        InvisibilityTurns = 2;
                    }
                }//else

                //------------------- draw resume pause image -------------------------

                Xresume = canvas.getWidth() - (resume1.getWidth() + width / 100);
                sprite.Xresume = Xresume;
                if (sprite.HomeButtonPushed.equals("resume"))
                    canvas.drawBitmap(resume1, Xresume, width / 100, null);
                else {
                    StartGame = true;
                    canvas.drawBitmap(pause1, Xresume, width / 100, null);
                    canvas.drawText("Tap to continue", xPos, yPos, paint3);
                }

                //---------------------Score--------------------

                int Xpos1 = width / 2 - (int) (paint2.measureText("" + Score) / 2);
                canvas.drawText("" + Score, Xpos1, paint2.measureText("" + 100), paint2);
                //----------------------Level up ----------------
                if (StopEnemies && sprite.HomeButtonPushed.equals("resume")) {
                    canvas.drawText("Speed up", xPosSpeedUp, yPosSpeedUp, paint1);
                    if (MakeAudio && !PlayedAlredy) {
                        Levelup.start();
                        PlayedAlredy = true;
                    }
                } else {
                    PlayedAlredy = false;
                }

                // -------- numers in start --------------------------
                if (!StartGame) {
                    if (TimeToStart >= 20 && TimeToStart <= 40)
                        NumToShow = 2;

                    else if (TimeToStart >= 40 && TimeToStart <= 60)
                        NumToShow = 1;

                    int xPos2 = canvas.getWidth() / 2 - (int) (paint4.measureText("" + NumToShow) / 2);
                    int yPos2 = (int) ((canvas.getHeight() / 2) - ((paint4.descent() + paint3.ascent()) / 2));
                    canvas.drawText("" + NumToShow, xPos2, yPos2, paint4);
                }
            }
            catch(Exception e){new AlertDialog.Builder(getContext()).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();}
        }//onDraw

        public void vibarte (){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else { v.vibrate(400); }
        }

        public void check_disqualification(){
            boolean disqualification = false, getFeature = false;
            for (int i = 0; i < stones.size(); i++) {
                int middle = stones.get(i).getX() + golemstone1.getWidth() / 2;
                if (stones.get(i).getY() + stones.get(i).getHeght() - 10 >= (sprite.getY() + 10) && middle > sprite.getX() && middle < sprite.getX() + sprite.wight) {
                    if (stones.get(i).TAG.equals("Stone") && !show_disqualification) {
                        disqualification = true;
                        if (MakeAudio)
                            hit.start();
                        if (MakeVibration)
                            vibarte();
                    } else if (stones.get(i).TAG.equals("LifeFeature")) {
                        getFeature = true;
                        stones.remove(stones.get(i));
                    }
                }
            }//for
            if (getFeature){
                current_health ++;
                if (current_health > hearts.size())
                    hearts.add(new Health(MyView.this, heart1, hearts.size()));
                if (MakeAudio)
                    getfeatures.start();
            }
            // check if player loos or he had more lifes
            if (disqualification){
                if (current_health > 0){
                    show_disqualification = true;
                    current_health -=1;
                    if (level > 3){

                    }
                }
                else {

                    if (ComeFrome.equals("main")) {
                        // more chance
                        SaveTheBestScores();
                        g.putExtra("kind", GameKind);
                        g.putExtra("Score", Score);
                        g.putExtra("level", level);
                        startActivity(g);
                    }
                    else{
                        SaveTheBestScores();
                        g.putExtra("kind", GameKind);
                        g.putExtra("Score", Score);
                        g.putExtra("level", level);
                        g.putExtra("AlreadyRevive","true");
                        startActivity(g);
                        //regular loosing
                    }
                        /*Loosing.putExtra("Score", Score);
                        Loosing.putExtra("level", level);
                        Loosing.putExtra("alreadyRevive",alreadyRevive);
                        Loosing.putExtra("kind",GameKind);
                        startActivity(Loosing);*/

                }
            }
        }

        public void pause(){
            try{
                isItOk = false;
                while (true){
                    try{
                        t.join();
                    }//try
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }//catch

                    break;
                }//while
                t = null;
            }
            catch (Exception e){}
        }//pause

        public void resume(){
            StartGame = false;
            isItOk = true;
            t = new Thread(this);
            t.start();
        }//resume

    }

}
