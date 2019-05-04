package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class MenuPlus extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    private Window window;
    SharedPreferences mPreferences ;
    CircleImageView circleImageView;
    private static final String TAG = "MenuPlus";

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.menuplus_activity, container, false);
        Context context = getActivity().getApplicationContext();
        navigationView = (NavigationView) root.findViewById(R.id.nav);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x",Context.MODE_PRIVATE);

        View hView =  navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) hView.findViewById(R.id.imgView_proPic);
        if(!mPreferences.getString(getString(R.string.user_pic),"").equals("vide_pic")){
            Picasso.with(getActivity())
                    .load(util.getDomaneName()+"/Content/Images/"+mPreferences.getString(getString(R.string.user_pic),"")).into(circleImageView);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ApplySharedPref")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
//                    case R.id.compte:
//
//
//                        Menu menu = navigationView.getMenu();
//
//                        setFragment(profil_activity);
//                        // window.setStatusBarColor(Color.parseColor("#e4e4e4"));
//
//                        //window.setNavigationBarColor(Color.parseColor("#e4e4e4"));
//                        return true;
                    case R.id.logout:
                        PUTTokenRequete(mPreferences.getInt(getString(R.string.Id),0),mPreferences.getInt(getString(R.string.IdToken),0),mPreferences.getString(getString(R.string.Token),""),"OUT");

//                         setFragment(logInActivity);
//                        getFragmentManager().beginTransaction().disallowAddToBackStack().add(R.id.container, new Activity_Login()).commit();
//                        new Handler().postDelayed(new Runnable() {
//                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                            @Override
//                            public void run() {
////                                  getActivity().finish();
//                                startActivity(new Intent(getActivity(), Activity_Login.class));
//
//
//                            }
//                        },200);




                        // window.setStatusBarColor(Color.parseColor("#e4e4e4"));

                        //window.setNavigationBarColor(Color.parseColor("#e4e4e4"));

                        return true;
                    case R.id.contact:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "oussama.boumaiza.1@esprit.tn" });
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Smart Dermato");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(intent, ""));
                        return true;
                        case R.id.chat_PM:
//                        setFragment(new Activity_Chat());
                            new Handler().postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                  //  getActivity().finish();
                                    startActivity(new Intent(getActivity(), Activity_Chat_PM.class));


                                }
                            },200);

                            return true;
                    default:
                        return false;

                }

            }

        });
        return root;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    public void PUTTokenRequete(int idU,int idT, String token, String log){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String URI = util.getDomaneName()+"/api/TokenLists/"+idT;
        System.out.println(URI);
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("id",idT);
        paramsToken.put("userToken",idU);
        paramsToken.put("Token",token);
        paramsToken.put("Log",log);
        System.out.println("Token: "+new JSONObject(paramsToken));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" Token Out respoce!!!!++++");
                        try {

                            new Handler().postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    mPreferences.edit().clear().apply();
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), Activity_Login.class));


                                }
                            },200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
                        Toast.makeText(getActivity(), "Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }

}
