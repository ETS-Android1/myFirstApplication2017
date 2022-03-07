package run.example.shilo.finalprogect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
public class Sprite implements View.OnTouchListener {
    int x, y, xSpeed, InitialLoc,wight,hight ,ySpeed,level,resumeWight,resumeHight,ScreenHeight,widthScreen;
    public int Xresume;
    public boolean GameStop = false;
    Bitmap [] playerpositions;
    Canvas c;
    Game.MyView ov;
    int currentBitmap = 0,currentSlowBitmap = 0;
    public boolean MoveThePlayer = false,BigRoad = true;
    String HomeButtonPushed="resume";

    public Sprite(Game.MyView myView, Bitmap [] playerpositions,int Xresume,int resumeWight,int resumeHight,int ScreenHeight,int widthScreen) {
        this.playerpositions = playerpositions;
        this.wight = this.playerpositions[0].getWidth();
        this.hight = this.playerpositions[0].getHeight();
        this.ov = myView;
        this.x = (ov.getWidth() - playerpositions[0].getWidth())/2;
        this.InitialLoc = x;
        this.y = ov.getHeight()- playerpositions[0].getHeight();
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.Xresume = Xresume;
        this.resumeWight = resumeWight;
        this.resumeHight = resumeHight;
        this.ScreenHeight = ScreenHeight;
        this.widthScreen = widthScreen;
    }//sprite

    public void update(Canvas canvas) {
        // to sloo down when the bmp is changing
        int TimeToSleep = (3-(ScreenHeight)/1000)*10;
        if (TimeToSleep < 2)
            TimeToSleep = 2;
        try {
            Thread.sleep(TimeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //---------------------------------------
        if (!GameStop) {
            currentBitmap = ++currentBitmap % 4;
            if (currentBitmap %2== 0)
                currentSlowBitmap = ++currentSlowBitmap%4;
            x += xSpeed;
            this.y = ov.getHeight() -  playerpositions[currentSlowBitmap].getHeight();
            this.InitialLoc = (ov.getWidth() - playerpositions[currentSlowBitmap].getWidth())/2;
            this.wight = this.playerpositions[currentSlowBitmap].getWidth();
            this.hight = this.playerpositions[currentSlowBitmap].getHeight();
        }
    }//update

    public void onDraw(Canvas canvas, int level) {
        this.c = canvas;
        this.level = level;
        ov.setOnTouchListener(this);
        update(canvas);
        //int srcY = direction * heght;
        //int srcX = currentSlowFrame * wight;
        //Rect src = new Rect(srcX, srcY, srcX + wight, srcY + heght);
        //Rect dst = new Rect(x, y, x + wight, y + heght);
        canvas.drawBitmap(playerpositions[currentSlowBitmap], x, y, null);
    }//onDraw

    // For the user to escape from the stone
    @Override
    public boolean onTouch(View v, MotionEvent me) {
        switch (me.getAction()) {
            case MotionEvent.ACTION_UP:
                WhereTheTuoch(me);

        }//switch

        return true;
    }
    public boolean getMTP(){return MoveThePlayer;}
    public int getY(){return this.y;}
    public int getX(){return this.x;}
    public int getheght(){return hight;}
    // check where the touch was (left/right)
    public void ResumPause(){
        if (HomeButtonPushed.equals("resume")) {
            HomeButtonPushed = "pause";
            ov.pause();
            Canvas c = ov.holder.lockCanvas();
            ov.onDraw1(c);
            ov.holder.unlockCanvasAndPost(c);
            ov.Dialog();
        }
        else {
            HomeButtonPushed = "resume";
            ov.resume();
        }
    }
    public void WhereTheTuoch(MotionEvent me) {
        MoveThePlayer = false;
        if (me.getX() >= Xresume && me.getX() <= Xresume + resumeWight && me.getY() >=5 && me.getY() <= 5 + resumeHight && HomeButtonPushed.equals("resume")){
            ResumPause();
        }
        else if (HomeButtonPushed.equals("pause")){
            ResumPause();
        }
        else if (GameStop);
        else if (this.x == InitialLoc && me.getX() > InitialLoc - wight && me.getX() < InitialLoc + wight) {
            this.x = InitialLoc;
        }
        // User Touch in left
        else if ((me.getX() < ov.getWidth() / 2 && (this.x >= InitialLoc)) || (this.level >= 6 && me.getX() < this.x && this.x - wight >= InitialLoc - 2 * wight && BigRoad)) {
            this.x = this.x - wight;
            MoveThePlayer = true;
        }
        // User Touch in right
        else if ((me.getX() > ov.getWidth() / 2 && (this.x <= InitialLoc)) || (this.level >= 6 && me.getX() > this.x && this.x + wight <= InitialLoc + 2 * wight && BigRoad)) {
            this.x = this.x + wight;
            MoveThePlayer = true;

        }// if
    }
}
