package com.simjessimsol.simcv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.opencv.core.Scalar;

public class ColorPickerFragment extends DialogFragment {

    private String colorToChange;
    private ColorPicker colorPicker;
    private Drawtivity drawtivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_dialog, null);
        colorPicker = (ColorPicker) view.findViewById(R.id.colorPickerWheel);
        SaturationBar saturationBar = (SaturationBar) view.findViewById(R.id.saturationBar);
        ValueBar valueBar = (ValueBar) view.findViewById(R.id.valueBar);
        OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacityBar);

        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
        colorPicker.addOpacityBar(opacityBar);

        colorToChange = getArguments().getString("color");
        drawtivity = (Drawtivity) getActivity();

        switch (colorToChange) {
            case "red":
                colorPicker.setOldCenterColor(Color.RED);
                break;
            case "green":
                colorPicker.setOldCenterColor(Color.GREEN);
                break;
            case "blue":
                colorPicker.setOldCenterColor(Color.BLUE);
                break;
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.confirm, positiveClick);
        builder.setNegativeButton(R.string.cancel, negativeClick);
        return builder.create();
    }

    private DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int newColorToTrack = colorPicker.getColor();
            int redValNewColor = Color.red(newColorToTrack);
            int greenValNewColor = Color.green(newColorToTrack);
            int blueValNewColor = Color.blue(newColorToTrack);
            int alphaValNewColor = Color.alpha(newColorToTrack);
            Scalar newColorToTrackScalar = new Scalar(redValNewColor, greenValNewColor, blueValNewColor, alphaValNewColor);

            switch (colorToChange) {
                case "red":
                    drawtivity.setColorToDrawFromRed(newColorToTrackScalar);
                    break;
                case "green":
                    drawtivity.setColorToDrawFromGreen(newColorToTrackScalar);
                    break;
                case "blue":
                    drawtivity.setColorToDrawFromBlue(newColorToTrackScalar);
                    break;
            }
        }
    };

    private DialogInterface.OnClickListener negativeClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
}
