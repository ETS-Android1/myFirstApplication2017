package run.example.shilo.finalprogect;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Stone{
    public String TAG = "";
    public boolean GameStop = false;
    private int x,y,heght, wight,InitialLoc;
    private int add_level;
    private Bitmap s;
    private Game.MyView ov;
    private double pixelheight;
    private int level ;
    // counstroctor
    public Stone(Game.MyView myView, Bitmap golemstone,int NumOfSkeleton,int wight,double pixelheight,String tag,int level){
        this.ov = myView;
        this.s = golemstone;
        heght = s.getHeight();
        this.wight = wight;
        this.InitialLoc = (ov.getWidth() - golemstone.getWidth()) / 2;
        this.y = -s.getHeight();
        this.x = InitialLoc + NumOfSkeleton*(wight);
        this.add_level =0;
        this.pixelheight=pixelheight;
        this.TAG = tag;
        this.level=level;
    }//Stone
    // if stone in screec
    public boolean InScreen(){
        if (this.y + 40 > ov.getHeight())
            return false;
        return true;
    }
    // return x agr
    public int getX(){
        return this.x;
    }

    public int getY(){ return this.y; }

    public int getHeght(){
        return this.heght;
    }

    public void Reset(){this.y = 0;}
    // move the golem stone
    public void Update(){
        if (this.level<3)
            this.level=2;
        if (level>8)
            this.level=8;
        double step =(pixelheight/50)*(double)level/3;
        this.y+=step;
    }// Update
    // draw the picture
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(s,x,y,null);
        if (!GameStop)
            Update();
    }//onDraw

    public void level_up(){add_level += 10;}

    public double get_level(){return add_level/10;}
    public void UpdateInitialLoc(int NumOfSkeleton){
        this.x = InitialLoc + NumOfSkeleton*(wight);
    }
}
