package run.example.shilo.finalprogect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

//import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        /*LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        try {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |View.SYSTEM_UI_FLAG_FULLSCREEN);
        }catch (Exception e){}
        builder.setView(view);*/
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Go home?")
                .setMessage("Do you want to go back to the home ?\nThe game will not be saved\n")


                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //listener.onNoClicked();
            }})
                .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onYesClicked();
                    }
                }).show();
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d("ddd","height: "+height+" width: "+width);
        if (height<2000 &&height>1500)
            height=1400;
        double div =(double)(width)/(double)(height);
        Log.d ("ddd","sss"+div);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setTextSize((int)(((height/3)*width)/6000));
        TextView textView1 = (TextView) dialog.findViewById(R.id.textView1);
        textView1.setTextSize((int)(((height/3)*width)/10000));
        Button btn1=(Button) dialog.findViewById(android.R.id.button1);
        btn1.setTextSize((int)((((height/3)*width)/10000)));
        Button btn2=(Button) dialog.findViewById(android.R.id.button2);
        btn2.setTextSize((int)(((height/3)*width)/10000));*/
        return dialog;
    }



    public interface ExampleDialogListener {
        void onYesClicked();
        //void onNoClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ExampleDialogListener");
        }
    }
}