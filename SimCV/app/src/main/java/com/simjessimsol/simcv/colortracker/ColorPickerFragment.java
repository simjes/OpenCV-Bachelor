package com.simjessimsol.simcv.colortracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.simjessimsol.simcv.R;

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
        colorPicker.addSaturationBar(saturationBar);

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

    @Override
    public void onStart() {
        super.onStart();
        Resources resources = getResources();
        Button positiveButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setBackgroundColor(resources.getColor(R.color.background_material_dark));
        positiveButton.setTextColor(Color.WHITE);

        Button negativeButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setBackgroundColor(resources.getColor(R.color.background_material_dark));
        negativeButton.setTextColor(Color.WHITE);
    }

    private DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int newColorToTrack = colorPicker.getColor();
            int redValNewColor = Color.red(newColorToTrack);
            int greenValNewColor = Color.green(newColorToTrack);
            int blueValNewColor = Color.blue(newColorToTrack);
            Scalar newColorToTrackScalar = new Scalar(redValNewColor, greenValNewColor, blueValNewColor);

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
