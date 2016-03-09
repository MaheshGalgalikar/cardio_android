package md.fusionworks.android.cardio.fragments;

import android.app.Activity;
import android.app.Fragment;

import md.fusionworks.android.cardio.views.LoadingDialog;

/**
 * Created by admin on 14.08.2015.
 */
public class BaseFragment extends Fragment {

    private LoadingDialog loadingDialog;

    protected void showLoadingDialog() {

        if (this.loadingDialog != null) {
            this.loadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {

        if (this.loadingDialog != null) {
            this.loadingDialog.dismiss();
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.loadingDialog = new LoadingDialog(activity);
    }
}
