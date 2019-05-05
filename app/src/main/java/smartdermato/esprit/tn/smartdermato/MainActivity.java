package smartdermato.esprit.tn.smartdermato;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import smartdermato.esprit.tn.smartdermato.Activities.MenuActivity;
import smartdermato.esprit.tn.smartdermato.Activities.MenuMedecinActivity;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences = getSharedPreferences("x", Context.MODE_PRIVATE);
        toastMessage(preferences.getString(getString(R.string.role),""));

       if(preferences.getString(getString(R.string.role),"").equals("patient"))
       {
           setFragment(new MenuActivity());

      }
     else
       {
          setFragment(new MenuMedecinActivity());

        }

//        setFragment(new MenuActivity());

    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }
}
