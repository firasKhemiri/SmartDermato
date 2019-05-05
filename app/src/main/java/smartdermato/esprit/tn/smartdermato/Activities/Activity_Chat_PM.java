package smartdermato.esprit.tn.smartdermato.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.ActivityChatBinding;

public class Activity_Chat_PM extends AppCompatActivity{

    private CircleImageView profile_image;
    private TextView Username;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private ActivityChatBinding mBinding;
    SharedPreferences mPreferences;
    private TextView username;
    private CircleImageView profileImage;
    private Window window;
    Map<String,Object> params = new HashMap<String, Object>();
    private static final String TAG = "Activity_Chat_PM";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        window=this.getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreferences= getSharedPreferences("x", Context.MODE_PRIVATE);

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        username = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        username.setText(mPreferences.getString(getString(R.string.username),""));
        if(mPreferences.getString(getString(R.string.user_pic),"").equals("vide_pic"))
        {
            profileImage.setImageResource(R.drawable.userprofile);

        }
        else
        {
            Picasso.with(this)
                    .load(util.getDomaneName()+"/Content/Images/"+mPreferences.getString(getString(R.string.user_pic),"")).into(profileImage);
        }
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User_Firebase user_firebase = dataSnapshot.getValue(User_Firebase.class);
//
//                username.setText(user_firebase.getUsername());
//                if (user_firebase.getImageURL().equals("default"))
//                {
//                    profileImage.setImageResource(R.drawable.userprofile);
//                }
//                else
//                {
//                    Glide.with(getApplicationContext()).load(user_firebase.getImageURL()).into(profileImage);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new UsersFragment(),"Users");
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.logout_x:
//                FirebaseAuth.getInstance().signOut();
//                mPreferences.edit().clear().apply();
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        PUTTokenRequete(mPreferences.getInt(getString(R.string.Id),0),mPreferences.getInt(getString(R.string.IdToken),0),mPreferences.getString(getString(R.string.Token),""),"OUT");

//                        startActivity(new Intent(Activity_Chat_PM.this, Activity_Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                },200);
                return true;

        }
        return false;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    private void  status(String status){
//        reference =FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

       System.out.println("status:    "+status);
        final String URL = util.getDomaneName() + "/api/Users/" +mPreferences.getInt(getString(R.string.Id),0);
        System.out.println("URLLLL:  "+ URL);
        params.put("Id",mPreferences.getInt(getString(R.string.Id),0));
        params.put("Username",mPreferences.getString(getString(R.string.username),""));
        params.put("Password",mPreferences.getString(getString(R.string.password),""));
        params.put("Email",mPreferences.getString(getString(R.string.email),""));
        params.put("ProfilePic",mPreferences.getString(getString(R.string.user_pic),""));
        params.put("FirstName",mPreferences.getString(getString(R.string.FirstName),""));
        params.put("LastName",mPreferences.getString(getString(R.string.LastName),""));
        params.put("Sexe",mPreferences.getString(getString(R.string.Sexe),""));
        params.put("DateOfBirth",mPreferences.getString(getString(R.string.DateOfBirth),""));
        params.put("PhoneNumber",mPreferences.getString(getString(R.string.PhoneNumber),""));
        params.put("certification",mPreferences.getString(getString(R.string.certification),""));
        params.put("EtatCertification",mPreferences.getInt(getString(R.string.EtatCertification),0));
        params.put("role",mPreferences.getString(getString(R.string.role),""));
        params.put("Status",status);
        params.put("City",mPreferences.getString(getString(R.string.City),""));
        params.put("Country",mPreferences.getString(getString(R.string.Country),""));
        params.put("OfficeNumber",mPreferences.getString(getString(R.string.OfficeNumber),""));
        params.put("OfficeAdress",mPreferences.getString(getString(R.string.OfficeAddess),""));
        params.put("PostalCode",mPreferences.getString(getString(R.string.PostalCode),""));
        params.put("CountryOfficeNumber",mPreferences.getString(getString(R.string.CountryOfficeNumber),""));
        params.put("CountryPhoneNumber",mPreferences.getString(getString(R.string.CountryPhoneNumber),""));
        params.put("Certificatdate",mPreferences.getString(getString(R.string.CertificatDate),""));
        Log.d(TAG,"Json"+new JSONObject(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");


                            JSONObject o = response;





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

        RequestQueue queue = Volley.newRequestQueue(Activity_Chat_PM.this);
        queue.add(stringRequest);




    }
    public void PUTTokenRequete(int idU,int idT, String token, String log){

        RequestQueue queue = Volley.newRequestQueue(this);
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
                                    finish();
                                    startActivity(new Intent(Activity_Chat_PM.this, Activity_Login.class));


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
                        Toast.makeText(Activity_Chat_PM.this, "Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onlineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("offlineeeeeeeeeeeeeeeeeeee");
        status("offline");
    }
}

