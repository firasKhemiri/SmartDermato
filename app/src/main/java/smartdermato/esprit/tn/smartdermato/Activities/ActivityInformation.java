package smartdermato.esprit.tn.smartdermato.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.FormulairMedcinBinding;

public class ActivityInformation extends AppCompatActivity implements OnCountryPickerListener {
    private Spinner spinner;
    private EditText lastName,firstName,birthday,phone,CD,OFN,OFA,CT,CNT,CP;
    private String username,password,email,certification,L_N,F_N,B,PH,C_D,OF_A,OF_N,C_P,C_T,CN_T;
    private Intent intent;
    private Users Users;
    private ValueAnimator anim;
    private int widthOriginal = 0;
    private FormulairMedcinBinding mBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Map<String,Object> params = new HashMap<String, Object>();
    private static final String TAG = "ActivityInformation";
    private boolean existe = false;
    private Dialog myDialog;
    private Window window;
    private CountryPicker countryPicker;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulair_medcin);
        window=getWindow();

        if (android.os.Build.VERSION.SDK_INT >= 19)
            window.setStatusBarColor(Color.parseColor("#17A8C2"));
        window.setNavigationBarColor(Color.parseColor("#17A8C2"));

        lastName = (EditText)findViewById(R.id.f_name);
        firstName = (EditText) findViewById(R.id.l_name);
        birthday = (EditText)findViewById(R.id.birthday);
        phone = (EditText)findViewById(R.id.ph_number);
        CD = (EditText)findViewById(R.id.DOFC);
        OFN = (EditText)findViewById(R.id.OFN);
        OFA = (EditText) findViewById(R.id.f_OFA);
     //   CNT = (EditText)findViewById(R.id.Country);
        CT = (EditText)findViewById(R.id.l_City);
        CP = (EditText)findViewById(R.id.CP);
        myDialog = new Dialog(this);

        spinner =  (Spinner)findViewById(R.id.sex);

        intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        certification = intent.getStringExtra("certification");
        Log.d("putExtra",username+"  "+password+"  "+email);

        L_N = lastName.getText().toString();
        F_N = firstName.getText().toString();
        B = birthday.getText().toString();
        PH = phone.getText().toString();
        C_D = CD.getText().toString();
        OF_A = OFA.getText().toString();
        OF_N = OFN.getText().toString();
//        CN_T = CNT.getText().toString();
        C_T = CT.getText().toString();
        C_P = CP.getText().toString();
        mBinding = DataBindingUtil.setContentView(this,R.layout.formulair_medcin);
//        initialize();
        mBinding.ccp.registerCarrierNumberEditText(mBinding.phNumber);
        mBinding.ccpO.registerCarrierNumberEditText(mBinding.OFN);
        List<String> list = new ArrayList<>();
        list.add("Sexe");
        list.add("Male");
        list.add("Women");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.sex.setAdapter(adapter);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences = getSharedPreferences("x", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mBinding.birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setContentView(R.layout.pop_date_piker);
                final DatePicker datePicker = (DatePicker) myDialog.findViewById(R.id.Datepick);
                final Button button = (Button) myDialog.findViewById(R.id.set);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Date date = new Date(datePicker.getDayOfMonth(),datePicker.getMonth(),datePicker.getYear());

                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date cDate = new Date();
                        if(cDate.getYear() - getDateFromDatePicker(datePicker).getYear() < 24)
                        {
                            toastMessage("Check your age");
                        }
                        else
                        {
                            String date = dateFormat.format(getDateFromDatePicker(datePicker));
                            mBinding.birthday.setText(date);
                            myDialog.dismiss();


                        }



                        //Toast.makeText(getActivity(), getDateFromDatePicker(datePicker).toString(), Toast.LENGTH_LONG).show();

                        //  Toast.makeText(getActivity(), datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear(),Toast.LENGTH_LONG).show();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });


        mBinding.DOFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setContentView(R.layout.pop_date_piker);
                final DatePicker datePicker = (DatePicker) myDialog.findViewById(R.id.Datepick);
                final Button button = (Button) myDialog.findViewById(R.id.set);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Date date = new Date(datePicker.getDayOfMonth(),datePicker.getMonth(),datePicker.getYear());

                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date cDate = new Date();
                        if(getDateFromDatePicker(datePicker).compareTo(cDate) == 1)
                        {
                            toastMessage("Check your Date");
                        }
                        else
                        {
                            String date = dateFormat.format(getDateFromDatePicker(datePicker));
                            mBinding.DOFC.setText(date);
                            myDialog.dismiss();

                        }



                        //Toast.makeText(getActivity(), getDateFromDatePicker(datePicker).toString(), Toast.LENGTH_LONG).show();

                        //  Toast.makeText(getActivity(), datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear(),Toast.LENGTH_LONG).show();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });





    }
    public void showCountry(View view){
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();
        setListener();
    }

    private void setListener() {
        mBinding.Country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());

            }
        });
    }


    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    public void load(View view){

        animateButtonWidth();
        fadeOutTextAndSetProgressDialog();
        nextAction();

    }

    public void animateButtonWidth(){


            widthOriginal = mBinding.signInBtn.getMeasuredWidth();
            ValueAnimator anim = ValueAnimator.ofInt(mBinding.signInBtn.getMeasuredWidth(),getFinalWidth());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBinding.signInBtn.getLayoutParams();
                    layoutParams.width = value;
                    mBinding.signInBtn.requestLayout();
                }
            });
            anim.setDuration(250);
            anim.start();




    }

    private void  fadeOutTextAndSetProgressDialog(){

            mBinding.signInText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showProgressDialog();
                }
            }).start();


    }

    private void showProgressDialog(){

            mBinding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            mBinding.progressBar.setVisibility(View.VISIBLE);



    }

    private void nextAction(){
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                //  revealButton();
                fadeOutProgressDialog();
                deleyedStartNextActivity();
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  revealButton(){


            mBinding.signInBtn.setElevation(0f);
            mBinding.revealView.setVisibility(View.VISIBLE);

            int x = mBinding.revealView.getWidth();
            int y = mBinding.revealView.getHeight();
            int startX = (int) (getFinalWidth() / 2 + mBinding.signInBtn.getX());
            int startY = (int) (getFinalWidth() / 2 + mBinding.signInBtn.getY());

            float raduis = Math.max(x, y) * 2.2f;
            Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView, startX, startY, getFinalWidth(), raduis);
            reveal.setDuration(450);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Intent intent = new Intent(ActivityInformation.this, CorpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            reveal.start();

    }
    private  void fadeOutProgressDialog(){
            mBinding.progressBar.animate().alpha(0f).setDuration(200).start();




    }

    private void  deleyedStartNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Next();                // startActivity(new Intent(Activity_SignUp.this, Home.class));
            }
        },100);
    }
    private int getFinalWidth(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }
    private void Next(){
        toastMessage(mBinding.DOFC.getText().toString());
        if(mBinding.lName.getText().length() == 0 || mBinding.fName.getText().length() == 0  || mBinding.DOFC.getText().length() == 0 || mBinding.OFN.getText().length() == 0 || mBinding.fOFA.getText().length() == 0 || mBinding.Country.getText().length() == 0 || mBinding.lCity.getText().length() == 0 || mBinding.CP.getText().length() == 0 || mBinding.sex.getSelectedItem().toString().equals("Sexe") || mBinding.birthday.getText().length() == 0 || ! mBinding.ccpO.isValidFullNumber()){

            toastMessage("Check your field");
            StopAnimBtn();

        }
        else
        {
            if(mBinding.phNumber.getText().length()> 0)
            {


                if( !mBinding.ccp.isValidFullNumber())
                {
                    toastMessage("Check your personnel phone");
                    StopAnimBtn();
                }
            }
            else {
                SingUp();
        toastMessage(String.valueOf( mBinding.ccp.isValidFullNumber()));

            }
        }





    }

    private void SingUp(){


                SingUpRequete();







    }
    public void StopAnimBtn(){
        anim = ValueAnimator.ofInt(mBinding.signInBtn.getMeasuredWidth(),widthOriginal);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.signInBtn.getLayoutParams();
                layoutParams.width = value;
                mBinding.signInBtn.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
        mBinding.signInText.setVisibility(View.VISIBLE);
        mBinding.signInText.animate().alpha(10f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.progressBar.setVisibility(View.INVISIBLE);
                mBinding.progressBar.animate().alpha(10f).setDuration(200).start();



            }
        }).start();

    }
    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
    public void SingUpRequete(){

        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Users";
        params.put("Username",username);
        params.put("Password",password);
        params.put("Email",email);
        params.put("ProfilePic","vide_pic");
        params.put("FirstName",mBinding.fName.getText().toString());
        params.put("LastName",mBinding.lName.getText().toString());

            params.put("DateOfBirth",mBinding.birthday.getText().toString());



        params.put("Sexe",mBinding.sex.getSelectedItem().toString().toString());
//        if(mBinding.phNumber.getText().length()==0 ) {
//            Users users = new Users();
//            users.setPhoneNumber("");
//            params.put("PhoneNumber", users.getPhoneNumber());
//        }
//        else
//        {
            params.put("PhoneNumber", mBinding.phNumber.toString());
            params.put("CountryPhoneNumber", mBinding.ccp.getSelectedCountryCodeWithPlus().toString());

        //}
        params.put("certification",certification);
        params.put("City",mBinding.lCity.getText().toString());
        params.put("Country",mBinding.Country.getText().toString());
        params.put("OfficeNumber",mBinding.OFN.getText().toString());
        params.put("CountryOfficeNumber",mBinding.ccpO.getSelectedCountryCodeWithPlus());
        params.put("OfficeAdress",mBinding.fOFA.getText().toString());
        params.put("PostalCode",mBinding.CP.getText().toString());
        params.put("role","medecin");
        params.put("Status","offline");
        toastMessage("2 "+ mBinding.DOFC.getText().toString());
        params.put("Certificatdate",mBinding.DOFC.getText().toString());
        //  params.put("")
        Log.d(TAG,"Json"+new JSONObject(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
//
                            Users = new Users();

                            Users.setId(response.getInt("Id"));
                            Users.setUsername(response.getString("Username"));
                            Users.setPassword(response.getString("Password"));
                            Users.setEmail(response.getString("Email"));
                            Users.setUser_pic(response.getString("ProfilePic"));
                            Users.setRole(response.getString("role"));
                            Users.setFirstName(response.getString("FirstName"));
                            Users.setLastName(response.getString("LastName"));
                            Users.setSexe(response.getString("Sexe"));
                            Users.setPhoneNumber(response.getString("PhoneNumber"));
                            Users.setCertification(response.getString("certification"));
                            Users.setEtatCertification(response.getInt("EtatCertification"));
                            Users.setDateOfBirth(response.getString("DateOfBirth"));
                            Users.setOfficeAddess(response.getString("OfficeAdress"));
                            Users.setOfficeNumber(response.getString("OfficeNumber"));
                            Users.setCity(response.getString("City"));
                            Users.setCountry(response.getString("Country"));
                            Users.setStatus(response.getString("Status"));
                            Users.setPostalCode(response.getString("PostalCode"));
                            Users.setCertificatDate(response.getString("Certificatdate"));
                            Users.setCountryOfficeNumber(response.getString("CountryOfficeNumber"));
                            Users.setCountryPhoneNumber(response.getString("CountryPhoneNumber"));

                            System.out.println(Users);

                            GETTokenRequete(Users);







                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StopAnimBtn();
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
                        Toast.makeText(ActivityInformation.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }

    private void deleteUser(int id)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Users/"+id;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, URI, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
//



                            preferences.edit().clear().apply();
                            editor.commit();







                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StopAnimBtn();
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
//                        Toast.makeText(Activity_SignUp.this, "Peut estre deja inscrit |OU| Erreur Services",
//                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    public void GETTokenRequete(final Users users)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        final String URL = util.getDomaneName() + "/api/TokenLists";

        System.out.println("URL GET Token:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" responce Get Token!!!!++++");


                        try {
                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);

                                if(o.getString("token").equals(FirebaseInstanceId.getInstance().getToken()) && o.getInt("userToken") == users.getId())
                                {
                                    existe = true;
                                    editor.putInt(getString(R.string.IdToken), o.getInt("id"));
                                    editor.putString(getString(R.string.Token), o.getString("token"));
                                    break;
                                }

                            }
                            System.out.println("Existe: "+existe);
                            System.out.println("TOKEN:  "+FirebaseInstanceId.getInstance().getToken());
                            if (!existe){
                                POSTTokenRequete(users,FirebaseInstanceId.getInstance().getToken(),"IN");
                            }
                            else
                            {

                                NextPage(users);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }








                    }
                }, new com.android.volley.Response.ErrorListener() {
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
                System.out.println(message);
                return;
            }
        });

        queue.add(stringRequest);
    }
    public void POSTTokenRequete(final Users users, String token, String log){

        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/TokenLists";
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("userToken",users.getId());
        paramsToken.put("Token",token);
        paramsToken.put("Log",log);
        System.out.println("Token: "+paramsToken);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
                            System.out.println(response.getInt("id")+"     "+response.getString("token"));
                            editor.putInt(getString(R.string.IdToken), response.getInt("id"));
                            editor.putString(getString(R.string.Token), response.getString("token"));
                            editor.commit();
                            NextPage(users);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StopAnimBtn();
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
                        Toast.makeText(ActivityInformation.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    private void NextPage(Users users)
    {





        editor.putString(getString(R.string.username), Users.getUsername());
        editor.putString(getString(R.string.role), Users.getRole());
        editor.putString(getString(R.string.password), Users.getPassword());
        editor.putString(getString(R.string.email), Users.getEmail());
        editor.putInt(getString(R.string.Id), Users.getId());
        editor.putString(getString(R.string.user_pic),Users.getUser_pic());
        editor.putString(getString(R.string.LastName),Users.getLastName());
        editor.putString(getString(R.string.FirstName),Users.getFirstName());
        editor.putString(getString(R.string.DateOfBirth),Users.getDateOfBirth());
        editor.putString(getString(R.string.Sexe),Users.getSexe());
        editor.putString(getString(R.string.SexePop),"null");
        editor.putString(getString(R.string.PhoneNumber),Users.getPhoneNumber());
        editor.putString(getString(R.string.certification),Users.getCertification());
        editor.putInt(getString(R.string.EtatCertification),Users.getEtatCertification());
        editor.putString(getString(R.string.OfficeNumber),Users.getOfficeNumber());
        editor.putString(getString(R.string.OfficeAddess),Users.getOfficeAddess());
        editor.putString(getString(R.string.PostalCode),Users.getPostalCode());
        editor.putString(getString(R.string.City),Users.getCity());
        editor.putString(getString(R.string.Country),Users.getCountry());
        editor.putString(getString(R.string.CertificatDate),Users.getCertificatDate());
        editor.putString(getString(R.string.CountryOfficeNumber),users.getCountryOfficeNumber());
        editor.putString(getString(R.string.CountryPhoneNumber),users.getCountryPhoneNumber());



        editor.commit();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {


                revealButton();
//                                                        startActivity(new Intent(Activity_Login.this, CorpActivity.class));



            }
        },100);
//        try {
//            signupFRB(Users.getUsername(),Users.getEmail(), AESCrypt.decrypt(Users.getPassword()),Users.getId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectCountry(Country country) {
        mBinding.imageCountry.setImageResource(country.getFlag());
        mBinding.imageCountry.setVisibility(View.VISIBLE);
        mBinding.Country.setText(country.getName());


    }
}
