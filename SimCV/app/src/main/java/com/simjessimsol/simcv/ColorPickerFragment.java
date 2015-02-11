package com.simjessimsol.simcv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.opencv.core.Scalar;

public class ColorPickerFragment extends DialogFragment {

    private Scalar red = new Scalar(255, 0, 0, 255);
    private Scalar green = new Scalar(0, 255, 0, 255);
    private Scalar blue = new Scalar(0, 0, 255, 255);

    private int newColorToTrack;
    private Scalar newColorToTrackScalar = new Scalar(255, 255, 255, 255);
    private ColorPicker colorPicker;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private OpacityBar opacityBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_dialog, null);
        colorPicker = (ColorPicker) view.findViewById(R.id.colorPickerWheel);
        saturationBar = (SaturationBar) view.findViewById(R.id.saturationBar);
        valueBar = (ValueBar) view.findViewById(R.id.valueBar);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacityBar);

        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
        colorPicker.addOpacityBar(opacityBar);

        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newColorToTrack = colorPicker.getColor();
                        //newColorToTrackScalar = new Scalar(Color.red(newColorToTrack), Color.green(newColorToTrack), Color.blue(newColorToTrack));
                        Log.i("testlol", "farge: " + Color.RED);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public Scalar getNewColorToTrackScalar() {
        return newColorToTrackScalar;
    }
}
