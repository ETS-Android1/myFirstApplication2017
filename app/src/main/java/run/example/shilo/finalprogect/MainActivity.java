package run.example.shilo.finalprogect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class    MainActivity extends AppCompatActivity implements SettingsDialog.ExampleDialogListener,View.OnClickListener{
    private AdView mAdView,mAdView1,mAdView2;
    AnimationDrawable man;
    ImageView man_image;
    Intent go;
    TextView Title,bestScore,ContactTitel,Tutorial;
    SettingsDialog exampleDialog;
    public boolean audio = true,vibration = true;
    private static final String TAG = "MainActivity";
    ConstraintLayout layout;
    ConstraintLayout layout1,layout2;
    String lang = "";
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    double widthScreen;
    LeaderboardsClient mLeaderboardsClient;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount mGoogleSignInAccount;
    ArrayList<String> a;
    SharedPreferences prefs;
    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            try {
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception e) {
            }
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            widthScreen = size.x;
            MobileAds.initialize(this, "ca-app-pub-7682417086477843~2562772988");
            mAdView1 = findViewById(R.id.adView1);
            mAdView = findViewById(R.id.adView);
            mAdView2=findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView1.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
            go = new Intent(this, Game.class);
            Title = (TextView) findViewById(R.id.textView);
            Typeface myfont = Typeface.createFromAsset(getAssets(), "font1.otf");
            Title.setTypeface(myfont);
            bestScore = (TextView) findViewById(R.id.textView3);
            ContactTitel = (TextView) findViewById(R.id.textView8);
            Tutorial = (TextView) findViewById(R.id.textView20);
            Typeface myfont1 = Typeface.createFromAsset(getAssets(), "quitemegical.ttf");
            bestScore.setTypeface(myfont1);
            ContactTitel.setTypeface(myfont1);
            Tutorial.setTypeface(myfont1);
            ShowBestScore();
            man_image = (ImageView) findViewById(R.id.image1);
            man_image.setBackgroundResource(R.drawable.animation);
            man = (AnimationDrawable) man_image.getBackground();
            layout = findViewById(R.id.main_layout);
            layout1 = findViewById(R.id.main_layout1);
            layout2 = findViewById(R.id.main_layout2);
            //-------------- Email --------------
            //Initializing the views
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editTextSubject = (EditText) findViewById(R.id.editTextSubject);
            editTextMessage = (EditText) findViewById(R.id.editTextMessage);
            buttonSend = (Button) findViewById(R.id.buttonSend);
            //Adding click listener
            buttonSend.setOnClickListener((View.OnClickListener) this);
            editTextEmail.setText("");
            editTextMessage.setText("");
            editTextSubject.setText("");
            layout.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            StopOpenGame();
            a = new ArrayList<>();
            prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            signIn();
            Log.d("ddd",""+mLeaderboardsClient);*/





    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 7);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 7) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            mLeaderboardsClient = Games.getLeaderboardsClient((Activity) this, mGoogleSignInAccount);
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            Log.d("dddd",""+mLeaderboardsClient);
            Log.d("dddd","eeeee");
            Log.d("dddd",mGoogleSignInAccount+"");
            int score = prefs.getInt("easy", 0);
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore("CgkI_Zjyy4kEEAIQAQ", score);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }
    public void StopOpenGame(){
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        man.start();
    }

    private void sendEmail() {
            String email = editTextEmail.getText().toString().trim();
            String subject = " send from :" + email + " ," + editTextSubject.getText().toString().trim();
            String message = editTextMessage.getText().toString().trim();
            SendMail sm = new SendMail(this, email, subject, message, widthScreen);
            sm.execute();


    }

    @Override
    public void onClick(View v) {
        if (!isValid(editTextEmail.getText().toString())) {
            Toast t = Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) t.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize((int)widthScreen/36);
            t.show();
        }
        else if(editTextMessage.getText().toString().equals("")) {

            Toast t = Toast.makeText(this, "There must be something written in the message.", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) t.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize((int)widthScreen/36);
            t.show();
        }
        else
            sendEmail();
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || email.equals(""))
            return false;
        return pat.matcher(email).matches();
    }

    // can't go Back
    public void onBackPressed(){
        if(layout1.isShown()||layout2.isShown())
            BackToMain(findViewById(android.R.id.content));
        else if (layout.isShown()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    // go to Game Activity easy game
    public void GoToGameEasy(View view) {
        try {
            a.add("Easy");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();;
        }
    }

    // go to Game Activity normal game
    public void GoToGameNormal(View view) {
        try {
            a.add("Medium");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();;
        }
    }

    // go to Game Activity hard game
    public void GoToGameHard(View view) {
        try {
            a.add("Hard");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();
        }
    }

//-----------------------
    public void ShowBestScore(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        bestScore.setText("Best scores :\n" +prefs.getInt("easy", 0)+" | "+ prefs.getInt("medium", 0)+" | "+ prefs.getInt("hard", 0) +"\nGames played : " + prefs.getInt("GamePlayed", 0));
    }

    public void GoToSettings(View view) {
        openDilaog();
    }

    public void openDilaog(){
        exampleDialog = new SettingsDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(boolean audio, boolean vibration) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("audio", audio);
        editor.commit();
        editor.putBoolean("vibration", vibration);
        editor.commit();
    }

    public void GoToEmail(View view) {
        //layout.setVisibility(View.GONE);
        layout.animate()
                .translationY(layout.getHeight())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.GONE);
                        layout1.setVisibility(View.VISIBLE);
                    }
                });

    }

    public void BackToMain(View view) {
        editTextEmail.setText("");
        editTextMessage.setText("");
        editTextSubject.setText("");
        if (layout1.isShown())
            layout1.setVisibility(View.GONE);
        else
            layout2.setVisibility(View.GONE);

        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
        final View activityRootView = findViewById(android.R.id.content);

                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    Log.d("ddd","keyboard is on");
                    View v = activityRootView;
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

        }

    public void GoToTutorial(View view) {
        layout.animate()
                .translationY(layout.getHeight())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                    }
                });
    }




// ----------------------
}
