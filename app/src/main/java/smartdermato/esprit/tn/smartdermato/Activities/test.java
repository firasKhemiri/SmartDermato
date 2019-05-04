package smartdermato.esprit.tn.smartdermato.Activities;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartdermato.esprit.tn.smartdermato.R;


public class test extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.corp_homme, container, false);

        return root;
    }
}
