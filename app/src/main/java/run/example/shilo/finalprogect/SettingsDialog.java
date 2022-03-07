package run.example.shilo.finalprogect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatDialogFragment;
//import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class SettingsDialog extends AppCompatDialogFragment {
    private String Title = "";
    public String lang = "en";
    private RadioButton en,he;
    private Switch AudioSwitch;
    private Switch VibratiobSwitch;
    private ExampleDialogListener listener;
    public boolean audio = true,vibration = true;
    private Button ContactBtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Title = "Settings";
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle(Title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyTexts(audio, vibration);
                    }
                });
        en = view.findViewById(R.id.en);
        he = view.findViewById(R.id.he);
        AudioSwitch = view.findViewById(R.id.audio);
        VibratiobSwitch = view.findViewById(R.id.vibration);
        SharedPreferences prefs = getContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        audio=prefs.getBoolean("audio", true);
        vibration=prefs.getBoolean("vibration", true);
        AudioSwitch.setChecked(this.audio);
        VibratiobSwitch.setChecked(this.vibration);

        // -------------------------------
        AudioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                audio = isChecked;

            }
        });
        VibratiobSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibration = isChecked;
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(boolean audio, boolean vibration);
    }

}