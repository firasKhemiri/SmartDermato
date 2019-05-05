package smartdermato.esprit.tn.smartdermato.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smartdermato.esprit.tn.smartdermato.Entities.TokenList;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.AESCrypt;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.ActivityLoginBinding;

public class Activity_Login extends AppCompatActivity {

    private ActivityLoginBinding mBinding;
    private RelativeLayout rellay1, rellay2;
    private Users Users;
    private static final int RequestCode = 746;
    private static final int NumberOfImagesToSelect = 1;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String passwordCrypt,passwordDecr;
    private ValueAnimator anim;
    private int widthOriginal = 0;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private  int TestFirebaselog = 0;
    private Window window;
    private boolean existe = false;
    private static final String TAG = "Activity_Login";



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        window=getWindow();

        if (android.os.Build.VERSION.SDK_INT >= 19)
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        window.setNavigationBarColor(Color.parseColor("#17A8C2"));

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        preferences = getSharedPreferences("x", Context.MODE_PRIVATE);

        String log = preferences.getString(getString(R.string.username),"");
        final String pas = preferences.getString(getString(R.string.password),"");
        final String role = preferences.getString(getString(R.string.role),"");
        try {
            System.out.println(log+"  "+pas+"  "+role+"  "+ AESCrypt.decrypt(pas));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        System.out.println("firebaseUser != null  "+ firebaseUser != null );
//        if (firebaseUser != null) {
//            TestFirebaselog = 1;
//
//
//        }
//        System.out.println("TestFirebaselog:      "+TestFirebaselog);


        if(!log.equals("")&&! pas.equals("") && role.equals("patient"))
            //&& TestFirebaselog == 1)
        {




                    Users Users = new Users();

                    Users.setId(preferences.getInt(getString(R.string.Id),0));
                    Users.setUsername(preferences.getString(getString(R.string.username),""));
                    Users.setPassword(preferences.getString(getString(R.string.password),""));
                    Users.setEmail(preferences.getString(getString(R.string.email),""));
                    Users.setUser_pic(preferences.getString(getString(R.string.user_pic),""));
                    Users.setRole(preferences.getString(getString(R.string.role),""));
                    Users.setFirstName(preferences.getString(getString(R.string.FirstName),""));
                    Users.setLastName(preferences.getString(getString(R.string.LastName),""));
                    Users.setSexe(preferences.getString(getString(R.string.Sexe),""));
                    Users.setPhoneNumber(preferences.getString(getString(R.string.PhoneNumber),""));
                    Users.setCertification(preferences.getString(getString(R.string.certification),""));
                    Users.setEtatCertification(preferences.getInt(getString(R.string.EtatCertification),0));
                    Users.setDateOfBirth(preferences.getString(getString(R.string.DateOfBirth),""));
                    GETTokenRequete(Users,"SHP");



        }
        if(!log.equals("")&&! pas.equals("") && role.equals("medecin") )
            //&& TestFirebaselog == 1 )
        {
            Users Users = new Users();

            Users.setId(preferences.getInt(getString(R.string.Id),0));
            Users.setUsername(preferences.getString(getString(R.string.username),""));
            Users.setPassword(preferences.getString(getString(R.string.password),""));
            Users.setEmail(preferences.getString(getString(R.string.email),""));
            Users.setUser_pic(preferences.getString(getString(R.string.user_pic),""));
            Users.setRole(preferences.getString(getString(R.string.role),""));
            Users.setFirstName(preferences.getString(getString(R.string.FirstName),""));
            Users.setLastName(preferences.getString(getString(R.string.LastName),""));
            Users.setSexe(preferences.getString(getString(R.string.Sexe),""));
            Users.setPhoneNumber(preferences.getString(getString(R.string.PhoneNumber),""));
            Users.setCertification(preferences.getString(getString(R.string.certification),""));
            Users.setEtatCertification(preferences.getInt(getString(R.string.EtatCertification),0));
            Users.setDateOfBirth(preferences.getString(getString(R.string.DateOfBirth),""));
            GETTokenRequete(Users,"SHP");




        }

        checkSheredPrefereces();
    }
    public void  SignUp(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Activity_Login.this, Activity_SignUp.class));
            }
        },200);
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void load(View view){

        animateButtonWidth();
        fadeOutTextAndSetProgressDialog();
        nextAction();


    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mBinding.rellay1.setVisibility(View.VISIBLE);
            mBinding.rellay2.setVisibility(View.VISIBLE);
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void animateButtonWidth(){
        widthOriginal = mBinding.signInBtn.getMeasuredWidth();
        anim = ValueAnimator.ofInt(mBinding.signInBtn.getMeasuredWidth(),getFinalWidth());
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void  fadeOutTextAndSetProgressDialog(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mBinding.signInText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showProgressDialog();
                }
            }).start();
        }
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

                fadeOutProgressDialog();
                deleyedStartNextActivity();
            }
        }, 5000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  revealButton(){
        mBinding.signInBtn.setElevation(0f);
        mBinding.revealView.setVisibility(View.VISIBLE);

        int x = mBinding.revealView.getWidth();
        int y = mBinding.revealView.getHeight();
        int startX = (int) (getFinalWidth()/2 + mBinding.signInBtn.getX());
        int startY = (int) (getFinalWidth()/2 + mBinding.signInBtn.getY());

        float raduis = Math.max(x,y) *1.2f;
        Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView,startX,startY,getFinalWidth(),raduis);
        reveal.setDuration(400);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = null;
                toastMessage(preferences.getString(getString(R.string.role),""));
                if(preferences.getString(getString(R.string.role),"").equals("patient"))
                {
                     intent = new Intent(Activity_Login.this, CorpActivity.class);

                }
                else
                {
                    intent = new Intent(Activity_Login.this, MainActivity.class);

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                StopAnimBtn();
                finish();
            }
        });
        reveal.start();
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private  void fadeOutProgressDialog(){
        mBinding.progressBar.animate().alpha(0f).setDuration(500).start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  deleyedStartNextActivity(){
//        Pix.start(this,                    //Activity or Fragment Instance
//                RequestCode,                //Request code for activity results
//                NumberOfImagesToSelect);    //Number of images to restict selection count
       Login();

    }
    private int getFinalWidth(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
//            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Pix.start(this, RequestCode,NumberOfImagesToSelect);
//                } else {
//                    Toast.makeText(Activity_Login.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  Login(){
        System.out.println("loginnnnnnnnnn   "  +mBinding.email.getText()+"      "+mBinding.password.getText());



        System.out.println("login 222  "  +mBinding.email.getText()+"      "+mBinding.password.getText());


                if (mBinding.email.getText().length() == 0 && mBinding.password.getText().length() == 0) {
                  //  myDialog.dismiss();

                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
                    builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                    builderSingle.setTitle("Login");
                    builderSingle.setMessage("Remplir vos champ ");
                   builderSingle.show();
                    StopAnimBtn();

                 //   mBinding.progressBar.setVisibility(View.GONE);
                } else {
                    String usernameenv;
                    String emailenv;
                    editor = preferences.edit();
                    try {
                        passwordCrypt = AESCrypt.encrypt(mBinding.password.getText().toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", mBinding.email.getText().toString());
                    params.put("password", passwordCrypt);
                    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                    Matcher m = p.matcher(mBinding.email.getText());

                    if (!m.matches()) {
                        emailenv = "vide";
                        usernameenv = mBinding.email.getText().toString();

                    } else {
                        usernameenv = "vide";
                        emailenv = mBinding.email.getText().toString();
                    }
                    System.out.println(R.xml.network_security_config);
                    final String URL = util.getDomaneName() + "/getbyemail/" +emailenv + "/" + usernameenv + "/";
                    System.out.println("URLLLL:  "+ URL);

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println(response+" respoce!!!!++++");

                            try {

                              //  System.out.println(o.toString()+" respoce!!!!++++");


                                        JSONObject o = response;


                                        passwordDecr =  AESCrypt.decrypt(o.getString("Password"));
                                        System.out.println(AESCrypt.decrypt(o.getString("Password"))+" decrrrrrrrrrrrrrrrrrrrrrr");
                                        if (passwordDecr.equals(mBinding.password.getText().toString())) {
                                            Users = new Users();

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
                                            //                                            Users.setCity(o.getString("City"));
//                                            Users.setCountry(o.getString("Country"));

                                            System.out.println("get users "+Users);
                                            GETTokenRequete(Users,"NEW");
                                            System.out.println(o.getString("role"));
                                            System.out.println(Users.getRole());
                                            System.out.println(getWindow().getDecorView().getMeasuredWidth()+" Widthhhhhhh");




                                        } else {
                                            Toast.makeText(Activity_Login.this, "verifier votre Password",
                                                    Toast.LENGTH_SHORT).show();
                                            //  myDialog.dismiss();
                                            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Activity_Login.this);
                                            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                                            builderSingle.setTitle("Login");
                                            builderSingle.setMessage("verifier votre Password ");
                                            builderSingle.show();
                                            StopAnimBtn();
                                            return;
                                        }


                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Activity_Login.this);
                            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                            builderSingle.setTitle("Login");
                            builderSingle.setMessage(message);
                            builderSingle.show();
                            StopAnimBtn();
                            return;

                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);
                    queue.add(stringRequest);


                }


    }
    private void  checkSheredPrefereces()
    {
        // String checkbox = preferences.getString(getString(R.string.checkbox),"False");

        String login2 = preferences.getString(getString(R.string.username),"");
        String password2 = null;
        try {
            password2 = AESCrypt.decrypt(preferences.getString(getString(R.string.password),""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String emil = preferences.getString(getString(R.string.email),"");
        int id = preferences.getInt(getString(R.string.Id),0);
        mBinding.email.setText(login2);
        mBinding.password.setText(password2);


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
    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.main_content,fragment).commit();
    }
    public void GETTokenRequete(final Users users, final String SHP)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        final String URL = util.getDomaneName() + "/api/TokenLists";

        System.out.println("URL GET Token:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" responce Get Token!!!!++++");


                        try {
                            //  System.out.println(o.toString()+" respoce!!!!++++");
                            TokenList tokenList = new TokenList();
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);

                                if(o.getString("token").equals(FirebaseInstanceId.getInstance().getToken()) && o.getInt("userToken") == users.getId())
                                {
                                    existe = true;
                                    tokenList.setId(o.getInt("id"));
                                    tokenList.setToken(o.getString("token"));
                                    tokenList.setUserToken(o.getInt("userToken"));

                                    break;
                                }

                            }
                            System.out.println("Existe: "+existe);
                            System.out.println("TOKEN:  "+FirebaseInstanceId.getInstance().getToken());
                            if (!existe){
                                POSTTokenRequete(users,FirebaseInstanceId.getInstance().getToken(),"IN",SHP);
                            }
                            else
                            {
                                if(SHP.equals("SHP"))
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void run() {
                                            revealButton();
                                            finish();


                                        }
                                    },200);
                                }
                                else {
                                    editor.putInt(getString(R.string.IdToken), tokenList.getId());
                                    editor.putString(getString(R.string.Token), tokenList.getToken());
                                    editor.commit();
                                    PUTTokenRequete(tokenList.getUserToken(),tokenList.getId(),tokenList.getToken(),"IN",users);

                                }


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
    public void POSTTokenRequete(final Users users, String token, String log, final String SHP){

        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/TokenLists";
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("userToken",users.getId());
        paramsToken.put("Token",token);
        paramsToken.put("Log",log);
        System.out.println("Token: "+paramsToken);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
                            editor.putInt(getString(R.string.IdToken), response.getInt("id"));
                            editor.putString(getString(R.string.Token), response.getString("token"));
                            editor.commit();
                            if(SHP.equals("SHP"))
                            {
                                revealButton();
                            }
                            else {
                                NextPage(users);

                            }

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
                        Toast.makeText(Activity_Login.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    private void NextPage(Users users) {

        System.out.println("nextpage"+users);
        if (users.getRole().equals("patient")) {
            editor.putString(getString(R.string.username), users.getUsername());
//            editor.putString(getString(R.string.role), users.getRole());
//            editor.putString(getString(R.string.password), users.getPassword());
//            editor.putString(getString(R.string.email), users.getEmail());
//            editor.putInt(getString(R.string.Id), users.getId());
//            editor.putString(getString(R.string.user_pic), users.getUser_pic());
//            editor.putString(getString(R.string.LastName), users.getLastName());
//            editor.putString(getString(R.string.FirstName), users.getFirstName());
//            editor.putString(getString(R.string.DateOfBirth), users.getDateOfBirth());
//            editor.putString(getString(R.string.Sexe), users.getSexe());
//
//            editor.putString(getString(R.string.PhoneNumber), users.getPhoneNumber());
            editor.putString(getString(R.string.SexePop), "null");
            editor.putString(getString(R.string.username), users.getUsername());
            editor.putString(getString(R.string.role), users.getRole());
            editor.putString(getString(R.string.password), users.getPassword());
            editor.putString(getString(R.string.email), users.getEmail());
            editor.putInt(getString(R.string.Id), users.getId());
            editor.putString(getString(R.string.user_pic), users.getUser_pic());
            editor.putString(getString(R.string.LastName), users.getLastName());
            editor.putString(getString(R.string.FirstName), users.getFirstName());
            editor.putString(getString(R.string.DateOfBirth), users.getDateOfBirth());
            editor.putString(getString(R.string.Sexe), users.getSexe());
            editor.putString(getString(R.string.certification), users.getCertification());
            editor.putString(getString(R.string.PhoneNumber), users.getPhoneNumber());
            editor.putInt(getString(R.string.EtatCertification), users.getEtatCertification());
            editor.putString(getString(R.string.City), users.getCity());
            editor.putString(getString(R.string.Country), users.getCountry());
            editor.putString(getString(R.string.OfficeAddess), users.getOfficeAddess());
            editor.putString(getString(R.string.OfficeNumber), users.getOfficeNumber());
            editor.putString(getString(R.string.PostalCode), users.getPostalCode());
            editor.putString(getString(R.string.CertificatDate),users.getCertificatDate());
            editor.putString(getString(R.string.CountryOfficeNumber),users.getCountryOfficeNumber());
            editor.putString(getString(R.string.CountryPhoneNumber),users.getCountryPhoneNumber());
            editor.commit();
            // myDialog.dismiss();
//            auth = FirebaseAuth.getInstance();
            String txt_email = users.getEmail();
            String txt_password = passwordDecr;
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
//
                    revealButton();


                }
            }, 200);


        }

        if (users.getRole().equals("medecin")) {
//                                                if(getWindow().getDecorView().getMeasuredWidth() >=1280)
//                                                    {
            editor.putString(getString(R.string.username), users.getUsername());
            editor.putString(getString(R.string.role), users.getRole());
            editor.putString(getString(R.string.password), users.getPassword());
            editor.putString(getString(R.string.email), users.getEmail());
            editor.putInt(getString(R.string.Id), users.getId());
            editor.putString(getString(R.string.user_pic), users.getUser_pic());
            editor.putString(getString(R.string.LastName), users.getLastName());
            editor.putString(getString(R.string.FirstName), users.getFirstName());
            editor.putString(getString(R.string.DateOfBirth), users.getDateOfBirth());
            editor.putString(getString(R.string.Sexe), users.getSexe());
            editor.putString(getString(R.string.certification), users.getCertification());
            editor.putString(getString(R.string.PhoneNumber), users.getPhoneNumber());
            editor.putInt(getString(R.string.EtatCertification), users.getEtatCertification());
            editor.putString(getString(R.string.City), users.getCity());
            editor.putString(getString(R.string.Country), users.getCountry());
            editor.putString(getString(R.string.OfficeAddess), users.getOfficeAddess());
            editor.putString(getString(R.string.OfficeNumber), users.getOfficeNumber());
            editor.putString(getString(R.string.PostalCode), users.getPostalCode());
            editor.putString(getString(R.string.CertificatDate),users.getCertificatDate());
            editor.putString(getString(R.string.CountryOfficeNumber),users.getCountryOfficeNumber());
            editor.putString(getString(R.string.CountryPhoneNumber),users.getCountryPhoneNumber());
            editor.putString(getString(R.string.SexePop), "null");


            editor.commit();
            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
//
                    revealButton();


                }
            }, 50);
            // myDialog.dismiss();
//            auth = FirebaseAuth.getInstance();
//            String txt_email =users.getEmail();
//            String txt_password = passwordDecr;
//            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
//                Toast.makeText(Activity_Login.this,"All fileds are required",Toast.LENGTH_SHORT).show();
//
//            }
//
//
//            else {
//
//                auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){

//                        }
//                        else {
//                            Toast.makeText(Activity_Login.this,"Authentification failed!",Toast.LENGTH_SHORT).show();
//
//                        }

//                    }
//                });

//
//        }else {
//                                                    //     myDialog.dismiss();
//                                                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Activity_Login.this);
//                                                    builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
//                                                    builderSingle.setTitle("Login");
//                                                    builderSingle.setMessage("Vous peuvez pas acceder a interface admin seulement avec une tablete ");
//                                                    builderSingle.show();
//                                                    StopAnimBtn();
//                                                }
        }
    }

    public void PUTTokenRequete(int idU, int idT, String token, String log, final Users users){

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
                        NextPage(users);


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

}
