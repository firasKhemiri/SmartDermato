package smartdermato.esprit.tn.smartdermato.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

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

public class Fragment_Chat_PM extends Fragment {
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


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_chat, container, false);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);

//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        username = root.findViewById(R.id.username);
        profileImage = root.findViewById(R.id.profile_image);
        username.setText(mPreferences.getString(getString(R.string.username),""));
        if(mPreferences.getString(getString(R.string.user_pic),"").equals("vide_pic"))
        {
            profileImage.setImageResource(R.drawable.userprofile);

        }
        else
        {
            Picasso.with(getActivity())
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
        Fragment_Chat_PM.ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new UsersFragment(),"Favoris");
        viewPagerAdapter.addFragment(new PatientFragment(),"Patient");

        ViewPager viewPager = root.findViewById(R.id.view_pager);

        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return root;
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);




    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onlineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("offlineeeeeeeeeeeeeeeeeeee");
        status("offline");
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
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
        public void addFragment(Fragment fragment, String title)
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

}
