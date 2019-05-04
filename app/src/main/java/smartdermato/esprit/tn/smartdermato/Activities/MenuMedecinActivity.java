package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.DatabaseHelper;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.MenuMedecinActivityBinding;

public class MenuMedecinActivity extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;
    private MenuMedecinActivityBinding mBinding;
    private Window window;
    private FragmentActivity myContext;
    private SharedPreferences mPreferences;
    private DatabaseHelper databaseHelper;
    private static final String TAG = "MenuActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };

    private int[] colors = new int[] {
            0xad000000,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK
    };
    private int[] colors2 = new int[] {
            Color.WHITE,
            Color.WHITE,
            Color.WHITE,
            Color.WHITE
    };
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.menu_medecin_activity,null,false);
        //View view = mBinding.getRoot();
        final View root = inflater.inflate(R.layout.menu_medecin_activity, container, false);
        window=getActivity().getWindow();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);
        navigationView = (BottomNavigationView) root.findViewById(R.id.main_nav);

        getDRS();
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Oussama","Boumaiza","null","20950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","oussama.boumaiza.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Salwa","Laribi","null","50664665","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","salwa.laribi.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Saif Eddin","Boumaiza","null","54950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","saifeddine.boumaiza.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Oussama","Boumaiza","null","20950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","oussama.boumaiza.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Oussama","Boumaiza","null","20950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","oussama.boumaiza.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Oussama","Boumaiza","null","20950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","oussama.boumaiza.1@esprit.tn"));
//        System.out.println("ADDDDDD: "+databaseHelper.addContactMedecin("Oussama","Boumaiza","null","20950389","Rue Du Grand Maghreb","8011","Nabeul","Tunisie","oussama.boumaiza.1@esprit.tn"));
////        frameLayout = (FrameLayout) root.findViewById(R.id.main_fram);

        setFragmentV4( new Fragment_Chat_PM());
        window.setStatusBarColor(Color.parseColor("#19AA8B"));
//
//        window.setNavigationBarColor(Color.parseColor("#e4e4e4"));

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.chat:
                        //getDRS();


                        Menu menu = mBinding.mainNav.getMenu();
//                        setFragment(new Accueil());
                        setFragmentV4( new Fragment_Chat_PM());

                        //setFragment(new Home());
//                        window.setStatusBarColor(Color.parseColor("#e4e4e4"));
//
//                        window.setNavigationBarColor(Color.parseColor("#e4e4e4"));
                        return true;
                    case R.id.dashboard:

                        //  getDRS();

                        setFragmentV4(new SearchContact());
//                        window.setStatusBarColor(Color.parseColor("#e4e4e4"));
//
//                        window.setNavigationBarColor(Color.parseColor("#e4e4e4"));
                        return true;
                    case R.id.account:
                        //  getDRS();

//                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        setFragmentV4(new Account());

//                        window.setStatusBarColor(Color.parseColor("#e4e4e4"));
//
//                        window.setNavigationBarColor(Color.parseColor("#e4e4e4"));
                        return true;
                    case R.id.menu:
                        // getDRS();

                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        setFragmentV4(new MenuPlus());
//                        window.setStatusBarColor(Color.parseColor("#e4e4e4"));
//
//                        window.setNavigationBarColor(Color.parseColor("#e4e4e4"));
                        return true;

                    default:
                        return false;

                }

            }
        });

        return root;
    }
    private void setFragmentV4(androidx.fragment.app.Fragment fragment) {
        FragmentManager fragManager = myContext.getSupportFragmentManager(); //If using fragments from support v4

        FragmentManager fragmentManager = fragManager;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fram, fragment);
        fragmentTransaction.commit();
//        getActivity().getFragmentManager().beginTransaction().replace(R.id.main_fram,fragment).commit();
    }
    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.main_fram,fragment).commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    private void getDRS(){
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.drop();
        databaseHelper = new DatabaseHelper(getActivity());
//          database.addContactMedecin("a","z","a","a","a","a","a","a","a");
        final String URL = util.getDomaneName() + "/api/Users/";
        System.out.println("URLLLL:  "+ URL);
        final List<Users> usersList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {
                            if(usersList.size()>0)
                                usersList.clear();

                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                Users user = new Users();
                                user.setId(o.getInt("Id"));
                                user.setUsername(o.getString("Username"));
                                user.setPassword(o.getString("Password"));
                                user.setEmail(o.getString("Email"));
                                user.setUser_pic(o.getString("ProfilePic"));
                                user.setRole(o.getString("role"));
                                user.setFirstName(o.getString("FirstName"));
                                user.setLastName(o.getString("LastName"));
                                user.setSexe(o.getString("Sexe"));
                                user.setPhoneNumber(o.getString("PhoneNumber"));
                                user.setCertification(o.getString("certification"));
                                user.setEtatCertification(o.getInt("EtatCertification"));
                                user.setDateOfBirth(o.getString("DateOfBirth"));
                                user.setStatus(o.getString("Status"));
                                user.setCertificatDate(o.getString("Certificatdate"));
                                user.setCountry(o.getString("Country"));
                                user.setCity(o.getString("City"));
                                user.setPostalCode(o.getString("PostalCode"));
                                user.setOfficeAddess(o.getString("OfficeAdress"));
                                user.setOfficeNumber(o.getString("OfficeNumber"));
                                user.setCountryOfficeNumber(o.getString("CountryOfficeNumber"));
                                user.setCountryPhoneNumber(o.getString("CountryPhoneNumber"));
                                System.out.println("user service"+user);


                                System.out.println(o.getString("role"));
                                if(user.getId()!= mPreferences.getInt(getString(R.string.Id),0) && user.getRole().equals("medecin")){
                                    if(user.getFirstName().isEmpty())
                                        System.out.println("null bb");
                                    databaseHelper.addContactMedecin(user.getFirstName(),user.getLastName(),user.getUser_pic(),user.getOfficeNumber(),user.getOfficeAddess(),user.getPostalCode(),user.getCity(),user.getCountry(),user.getEmail(),user.getId(),user.getStatus(),user.getCountryOfficeNumber());
                                }
                            }

//                            System.out.println("database get: "+database.getAllUsers());




                        } catch (Exception e) {
                            e.printStackTrace();
                        }








                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }
}
