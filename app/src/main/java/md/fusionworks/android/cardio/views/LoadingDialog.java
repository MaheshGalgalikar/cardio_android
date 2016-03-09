package md.fusionworks.android.cardio.views;

import android.app.Dialog;
import android.content.Context;

import md.fusionworks.android.cardio.R;

/**
 * Created by admin on 06.08.2015.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogTheme);

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        setContentView(R.layout.loading_dialog_layout);
    }
}
