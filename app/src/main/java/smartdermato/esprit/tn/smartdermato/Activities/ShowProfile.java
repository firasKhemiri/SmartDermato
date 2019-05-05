package smartdermato.esprit.tn.smartdermato.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartdermato.esprit.tn.smartdermato.Entities.Rating;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.ShowProfileBinding;

public class ShowProfile extends AppCompatActivity {
    private ShowProfileBinding mBinding;
    private int user_id;
    private Intent intent;
    private Window window;
    private SharedPreferences mPreferences;
    private Boolean isRating = false;
    private Rating rating;
    private List<Rating> ratingList;
    private Float Note_Of_Rating = 0.0f;
    private static final String TAG = "ShowProfile";
    private int coff=0;
    private float somme = 0.0f;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_profile);
        mBinding = DataBindingUtil.setContentView(this,R.layout.show_profile);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreferences= getSharedPreferences("x", Context.MODE_PRIVATE);
        window=this.getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        LayerDrawable stars = (LayerDrawable) mBinding.rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#F0AF10"), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent = getIntent();
        user_id =  intent.getIntExtra("user_id",0);
        getUser();
        GetRating();
        mBinding.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                toastMessage(String.valueOf(rating));
                Note_Of_Rating = rating;


            }
        });
ratingList = new ArrayList<>();
        getRating();





    }
    private void GetRating()
    {
        final String URL = util.getDomaneName() + "/api/Ratings/" +mPreferences.getInt(getString(R.string.Id),0) + "/" + user_id + "/";
        System.out.println("URLLLL:  "+ URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");


                            JSONObject o = response;
                            rating = new Rating();
                            rating.setPationId(o.getInt("pationId"));
                            rating.setMedcinId(o.getInt("medecinId"));
                            rating.setNote(Float.valueOf(o.getString("note")));
                            isRating = true;
                            mBinding.rating.setRating(rating.getNote());



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
                isRating = false;


            }
        });

        RequestQueue queue = Volley.newRequestQueue(ShowProfile.this);
        queue.add(stringRequest);
    }
    private void PutRating(Rating rating)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Ratings/"+mPreferences.getInt(getString(R.string.Id),0)+"/"+user_id;
        System.out.println(URI);
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("pationId",rating.getPationId());
        paramsToken.put("medecinId",rating.getMedcinId());
        paramsToken.put("note",rating.getNote());
        System.out.println("Rating PUT: "+new JSONObject(paramsToken));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" Token Out respoce!!!!++++");


                        JSONObject o = response;
                        Rating rating1 = new Rating();
                        try {
                            rating1.setPationId(o.getInt("pationId"));
                            rating1.setMedcinId(o.getInt("medecinId"));
                            rating1.setNote(Float.valueOf(o.getString("note")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
//                        Toast.makeText(Activity_Login.class, "Peut estre deja inscrit |OU| Erreur Services",
//                                Toast.LENGTH_SHORT).show();
//                        return ;

                    }
                });





        queue.add(stringRequest);

    }
    private void PostRating(Rating rating)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Ratings";
        System.out.println(URI);
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("pationId",rating.getPationId());
        paramsToken.put("medecinId",rating.getMedcinId());
        paramsToken.put("note",rating.getNote());
        System.out.println("Rating Post: "+new JSONObject(paramsToken));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" Token Out respoce!!!!++++");

                        JSONObject o = response;
                        Rating rating1 = new Rating();
                        try {
                            rating1.setPationId(o.getInt("pationId"));
                            rating1.setMedcinId(o.getInt("medecinId"));
                            rating1.setNote(Float.valueOf(o.getString("note")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
//                        Toast.makeText(Activity_Login.class, "Peut estre deja inscrit |OU| Erreur Services",
//                                Toast.LENGTH_SHORT).show();
//                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    private void getUser()
    {
        final String URL = util.getDomaneName() + "/api/Users/"+user_id;
        System.out.println("URLLLL:  "+ URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {



                            JSONObject o = response;



                                Users Users = new Users();

                                Users.setId(o.getInt("Id"));
                                Users.setUsername(o.getString("Username"));
                                Users.setPassword(o.getString("Password"));
                                Users.setEmail(o.getString("Email"));
                                Users.setUser_pic(o.getString("ProfilePic"));
                                Users.setRole(o.getString("role"));
                                Users.setFirstName(o.getString("FirstName"));
                                Users.setLastName(o.getString("LastName"));
                                Users.setSexe(o.getString("Sexe"));
                                Users.setPhoneNumber(o.getString("PhoneNumber"));
                                Users.setCertification(o.getString("certification"));
                                Users.setEtatCertification(o.getInt("EtatCertification"));
                                Users.setDateOfBirth(o.getString("DateOfBirth"));
                                Users.setCountry(o.getString("Country"));
                                Users.setCity(o.getString("City"));
                                Users.setPostalCode(o.getString("PostalCode"));
                                Users.setOfficeNumber(o.getString("OfficeNumber"));
                                Users.setOfficeAddess(o.getString("OfficeAdress"));
                                Users.setCertificatDate(o.getString("Certificatdate"));
                                Users.setCountryOfficeNumber(o.getString("CountryOfficeNumber"));
                                Users.setCountryPhoneNumber(o.getString("CountryPhoneNumber"));
                                System.out.println("get users "+Users);
                            if(!Users.getUser_pic().equals("vide_pic"))
                            {

                                Picasso.with(ShowProfile.this)
                                        .load(util.getDomaneName()+"/Content/Images/"+Users.getUser_pic()).into(mBinding.imgViewProPic);


                            }
                            mBinding.LNFN.setText(Users.getFirstName()+" "+Users.getLastName());
                            mBinding.OFA.setText(Users.getOfficeAddess());
                            mBinding.City.setText(Users.getCity());
                            mBinding.Country.setText(Users.getCountry());
                            mBinding.CP.setText(Users.getPostalCode());
                            mBinding.OFN.setText("("+Users.getCountryOfficeNumber()+")"+Users.getOfficeNumber());
                            mBinding.dateCertification.setText(Users.getCertificatDate());







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

        RequestQueue queue = Volley.newRequestQueue(ShowProfile.this);
        queue.add(stringRequest);

    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        toastMessage("isRating: "+isRating.toString()+"   "+Note_Of_Rating);
//        if (isRating)
//        {
//            rating.setNote(Note_Of_Rating);
//            PutRating(rating);
//        }
//        else if (!isRating && Note_Of_Rating != 0.0f)
//        {
//            rating.setNote(Note_Of_Rating);
//            PostRating(rating);
//        }
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        toastMessage("isRating: "+isRating.toString()+"   "+Note_Of_Rating);
        System.out.println("isRating: "+isRating.toString()+"   "+Note_Of_Rating);
        if (isRating)
        {
            rating.setNote(Note_Of_Rating);
            PutRating(rating);
        }
        else if (!isRating && Note_Of_Rating != 0.0f)
        {
            System.out.println(rating);
            rating =new Rating();
            rating.setPationId(mPreferences.getInt(getString(R.string.Id),0));
            rating.setMedcinId(user_id);
            rating.setNote(Note_Of_Rating);
            PostRating(rating);
        }
    }
    public void getRating()
    {
        final String URL = util.getDomaneName() + "/api/Ratings/";
        System.out.println("URLLLL:  "+ URL);
        ratingList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(" Rating respoce!!!!++++"+response);

                        try {
                            if(ratingList.size()>0)
                                ratingList.clear();

                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                Rating rating = new Rating();
                                rating.setPationId(o.getInt("pationId"));
                                rating.setMedcinId(o.getInt("medecinId"));
                                rating.setNote(Float.valueOf(o.getString("note")));
                                System.out.println(rating);
                                ratingList.add(rating);
                            }
                            System.out.println("Rating List: "+ratingList);
                            for (Rating rating : ratingList)
                            {
                                if(rating.getMedcinId() == user_id)
                                {
                                    somme = somme + rating.getNote();
                                    coff++;
                                }
                            }
//        note = root.findViewById(R.id.note);
//
//        prgDialog = new ProgressDialog(getActivity());
//        ratingBar = root.findViewById(R.id.rating);

//                            System.out.println("database get: "+database.getAllUsers());


                            if(mPreferences.getString(getString(R.string.role),"").equals("medecin"))
                            {
                                mBinding.rating.setEnabled(false);
                                if(somme == 0.0f && coff == 0)
                                {
                                    mBinding.note.setText(String.valueOf(0.0f));
                                    mBinding.rating.setRating(0.0F);
                                }
                                else
                                {
                                    mBinding.note.setText(String.valueOf(somme/coff));
                                    mBinding.rating.setRating(somme/coff);
                                }

                            }
                            else
                            {
                                if(somme == 0.0f && coff == 0)
                                {
                                    mBinding.note.setText(String.valueOf(0.0f));
                                }
                                else
                                {
                                    mBinding.note.setText(String.valueOf(somme/coff));
                                }

                            }


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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
