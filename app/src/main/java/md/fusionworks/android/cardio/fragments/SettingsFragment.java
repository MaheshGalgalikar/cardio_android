package md.fusionworks.android.cardio.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.services.ServiceManager;
import md.fusionworks.android.cardio.storage.LocalStorage;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {

    @Bind(R.id.allowSendDataToServerCheckBox)
    CheckBox allowSendDataToServerCheckBox;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        boolean allowSendDataToServer = LocalStorage.from(getActivity()).getSendDataToServer();
        allowSendDataToServerCheckBox.setChecked(allowSendDataToServer);

        allowSendDataToServerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                LocalStorage.from(getActivity()).setSendDataToServer(isChecked);
            }
        });
    }


}
