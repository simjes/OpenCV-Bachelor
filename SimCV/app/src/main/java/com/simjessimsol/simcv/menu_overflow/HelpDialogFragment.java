package com.simjessimsol.simcv.menu_overflow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.simjessimsol.simcv.R;

/**
 * Created by Simen on 09.04.2015.
 * http://developer.android.com/guide/topics/ui/dialogs.html
 * http://developer.android.com/guide/components/fragments.html
 * http://www.androidbegin.com/tutorial/android-dialogfragment-tutorial/
 */
public class HelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title_help)
                .setMessage(R.string.dialog_msg_help)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
