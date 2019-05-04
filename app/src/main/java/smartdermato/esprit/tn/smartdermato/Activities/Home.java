package smartdermato.esprit.tn.smartdermato.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.util.ArrayList;

import smartdermato.esprit.tn.smartdermato.R;


public class Home extends Fragment {

    private static final int RequestCode = 746;
    private static final int NumberOfImagesToSelect = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.corp_homme, container, false);
        Context context = getActivity().getApplicationContext();
        Pix.start((FragmentActivity) getActivity(),                    //Activity or Fragment Instance
                RequestCode              //Request code for activity results
                );    //Number of images to restict selection count
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Pix.start((FragmentActivity) getActivity(), RequestCode, NumberOfImagesToSelect);
                else {
                    Toast.makeText(getActivity(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
