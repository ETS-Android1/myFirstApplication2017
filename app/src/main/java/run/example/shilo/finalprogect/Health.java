package run.example.shilo.finalprogect;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Health {
    Bitmap heart;
    Game.MyView ov;
    int x,y;
    public Health(Game.MyView myView, Bitmap heart,int NomOfHeart) {
        this.heart = heart;
        this.ov = myView;
        this.x =5 + NomOfHeart*heart.getWidth();
        if (NomOfHeart!=0)
            this.x += 2;
        this.y = 5;
    }//sprite
public void onDraw(Canvas canvas){
    canvas.drawBitmap(heart,x,y,null);
}// onDraw

}
