package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.Adapter.SurveyMultiViewTypeAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Question;
import smartdermato.esprit.tn.smartdermato.ImageFilters.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.SurveyListBinding;

public class SurveyActivity extends AppCompatActivity {
    private Window window;
    private LottieAnimationView animationViewRes,animationView,animationViewResult,animationViewResFailed;
    private RelativeLayout firstAnnimation,result,r,all;
    private SurveyListBinding mBinding;
    private String imagePath;
    private String imageName = "vide_pic";
    private int pourcentage;
    private int type;
    private Intent intent;
    private Dialog myDialog;
    private String dateP;
    private Map<String, Object> params = new HashMap<String, Object>();
    private SharedPreferences mPreferences;

    private static final String TAG = "SurveyActivity";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_list);
        mBinding = DataBindingUtil.setContentView(this,R.layout.survey_list);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreferences = getSharedPreferences("x", Context.MODE_PRIVATE);
        window= getWindow();
        intent = getIntent();
        pourcentage = intent.getIntExtra("pourcentage",0);
        toastMessage(String.valueOf(pourcentage));
        type = intent.getIntExtra("type",0);
      //  imagePath = intent.getStringExtra("imagePath");
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        animationView = findViewById(R.id.animation_view);
        animationViewRes = findViewById(R.id.animation_view_res);
        animationViewResult = findViewById(R.id.animation_view_result);
        animationViewResFailed = findViewById(R.id.animation_view_res_failed);
        firstAnnimation = findViewById(R.id.first_annimation);
        result = findViewById(R.id.result);
        all = findViewById(R.id.alla);

        r = findViewById(R.id.r);

        RecyclerView recyclerView = findViewById(R.id.survey_lst);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        ArrayList<Question> items = new ArrayList<>();

        Question item1 = new Question(1,"Does the affected spot usually get red ?", 2);
        Question item2= new Question(2,"Does the affected spot get dry and is there skin scaling, or shedding ?", 1);
        Question item3 = new Question(3,"Does the affected area have definite boders ?", 0);
        Question item4 = new Question(4,"Is there any itching in the surroundings or in the inside of the affected area ?", 1);
        Question item5 = new Question(4,"Are there any skin lesions ?", 1);
        Question item6 = new Question(4,"Are there a well defined area of purple-coloured, itchy, flat-topped papules with interspersed lacy white lines ?", 1);
        Question item7 = new Question(5,"test", 2);

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);

        SurveyMultiViewTypeAdapter adapterAccueil = new SurveyMultiViewTypeAdapter(items, getApplicationContext());
        recyclerView.setAdapter(adapterAccueil);

        adapterAccueil.notifyDataSetChanged();
        mBinding.btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.setVisibility(View.GONE);
                if(type == 0)
                {
                    all.setBackgroundColor(Color.parseColor("#17A8C2"));
                    myDialog = new Dialog(SurveyActivity.this);

                    myDialog.setContentView(R.layout.pop_result_analyse);

                    myDialog.setCanceledOnTouchOutside(false);
                    Button ok = myDialog.findViewById(R.id.ok);
                    RelativeLayout contentR = myDialog.findViewById(R.id.rall);
                    contentR.setBackgroundResource(R.drawable.resultat_negative);

                    TextView pourcentageT = myDialog.findViewById(R.id.pourcentaget);
                    String pourc = String.valueOf(pourcentage);
                    //PostConsultation(response.substring(3,16) ,pourc);
                    pourcentageT.setText(String.valueOf(pourcentage)+"%");
                    //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostConsultation("Not Psoriasis" ,pourc);

                        }
                    });
                    Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();

                }
                else
                {
                    all.setBackgroundColor(Color.parseColor("#17A8C2"));
                    myDialog = new Dialog(SurveyActivity.this);

                    myDialog.setContentView(R.layout.pop_result_analyse);

                    myDialog.setCanceledOnTouchOutside(false);
                    Button ok = myDialog.findViewById(R.id.ok);
                    RelativeLayout contentR = myDialog.findViewById(R.id.rall);
                    contentR.setBackgroundResource(R.drawable.resultat_positive);

                    TextView pourcentageT = myDialog.findViewById(R.id.pourcentaget);
                    String pourc = String.valueOf(pourcentage);
                    //PostConsultation(response.substring(3,16) ,pourc);
                    pourcentageT.setText(String.valueOf(pourcentage)+"%");
                    //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostConsultation("Not Psoriasis" ,pourc);

                        }
                    });
                    Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                }

             /*   firstAnnimation.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                window.setStatusBarColor(Color.parseColor("#17A8C2"));

                //  mBinding.animationView.setBackgroundColor(R.color.gradStart);
                //  mBinding.animationView.setBackgroundColor(R.color.gradStop);
                animationView.loop(true);
                animationView.playAnimation();

                System.out.println("imagePath " + imagePath);*/

               /* try {

                    File file = new File(imagePath);
                    RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContent);
                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), imageName);



                    UploadService uploadService = APIClient.getClient().create(UploadService.class);



               /* mDialog = new ProgressDialog(this);
                mDialog.setMessage("Please Wait...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();*/


                   /* uploadService.Upload(photo, description).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                AnalyserImages();


                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("erreur...........");
                            toastMessage(t.getMessage());
                            animationView.pauseAnimation();
                            animationView.setVisibility(View.GONE);
                            animationViewResFailed.setVisibility(View.VISIBLE);
                            //mBinding.animationViewResFailed.loop(true);

                            animationViewResFailed.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    firstAnnimation.setVisibility(View.GONE);
                                    animationViewResFailed.pauseAnimation();
                                    animationViewResFailed.setVisibility(View.GONE);
                                    r.setVisibility(View.VISIBLE);



                                }
                            }, 2000);

//                            }, 2000);


                        }
                    });


                } catch (Exception e) {
                    //mDialog.dismiss();
                    toastMessage("erreur 1....");

                }*/
            }
        });

    }
    public void AnalyserImages()
    {
        final String URL = util.getDomaneName() + "/api/Analyse/"+imageName+"/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" responce Analyse !!!!++++");

                        try {

                            animationView.pauseAnimation();
                            animationView.setVisibility(View.GONE);
                            animationViewRes.setVisibility(View.VISIBLE);
                            //mBinding.animationViewRes.loop(true);

                            animationViewRes.playAnimation();
//                            animationView.pauseAnimation();
//                            animationView.setVisibility(View.GONE);
//                            animationViewRes.setVisibility(View.VISIBLE);
//                            //mBinding.animationViewRes.loop(true);
//
//                            animationViewRes.playAnimation();
                            //mBinding.animationViewRes.pauseAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //  animationViewRes.setVisibility(View.GONE);
                                    firstAnnimation.setVisibility(View.GONE);
                                    all.setBackgroundColor(Color.parseColor("#17A8C2"));


                                    //result.setVisibility(View.VISIBLE);
                                    //window.setStatusBarColor(Color.parseColor("#57717A"));

                                    //mBinding.animationViewResult.loop(true);
                                    //animationViewResult.playAnimation();
//                                    animationViewRes.setVisibility(View.GONE);
//                                    firstAnnimation.setVisibility(View.GONE);
//                                    result.setVisibility(View.VISIBLE);
                                    //window.setStatusBarColor(Color.parseColor("#57717A"));

                                    //mBinding.animationViewResult.loop(true);
//                                    animationViewResult.playAnimation();
                                    toastMessage(response.substring(0,1));
                                    System.out.println("subbbbb: "+response.substring(1,2));
                                    System.out.println("subbbbb2: "+response.substring(14,response.length()-5));
                                    System.out.println("subbbbb3: "+response.substring(3,12));
                                    if(response.substring(1,2).equals("1"))
                                    {

                                        Toast.makeText(getApplicationContext(),"psoriasis",Toast.LENGTH_LONG).show();
                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                        Date date  = new Date();
                                        dateP = dateFormat.format(date);
                                        myDialog = new Dialog(SurveyActivity.this);

                                        myDialog.setContentView(R.layout.pop_result_analyse);
                                        myDialog.setCanceledOnTouchOutside(false);
                                        Button ok = myDialog.findViewById(R.id.ok);
                                        RelativeLayout contentR = myDialog.findViewById(R.id.rall);
                                        contentR.setBackgroundResource(R.drawable.resultat_positive);

                                        TextView pourcentage = myDialog.findViewById(R.id.pourcentaget);
                                        String pourc = response.substring(14,response.length()-5);
                                        //PostConsultation(response.substring(3,12) ,pourc);
                                        pourcentage.setText(String.valueOf(Math.round(Double.valueOf(response.substring(14,response.length()-6))))+"%");

                                        //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                PostConsultation(response.substring(3,12) ,pourc);

                                            }
                                        });
                                        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                        // mBinding.textResult.setOnClickListener(new  );
//                                        textResult.setAnimationListener(new AnimationListener() {
                                          /*  @Override
                                            public void onAnimationEnd(HTextView hTextView) {
                                                toastMessage("end");
                                                poucentage.setAnimationListener(new AnimationListener() {
                                                    @Override
                                                    public void onAnimationEnd(HTextView hTextView) {
                                                        // rellay2.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                                poucentage.animateText(response.substring(14,response.length()-5));
                                            }
                                        });*/
                                        //  textResult.animateText(response.substring(3,12));
                                        // mBinding.textResult.setText(response.substring(3,12));


                                    }
                                    else
                                    {

                                        Toast.makeText(getApplicationContext(),"not psoriasis",Toast.LENGTH_LONG).show();

                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                        Date date  = new Date();
                                        dateP = dateFormat.format(date);

                                        myDialog = new Dialog(SurveyActivity.this);

                                        myDialog.setContentView(R.layout.pop_result_analyse);

                                        myDialog.setCanceledOnTouchOutside(false);
                                        Button ok = myDialog.findViewById(R.id.ok);
                                        RelativeLayout contentR = myDialog.findViewById(R.id.rall);
                                        contentR.setBackgroundResource(R.drawable.resultat_negative);

                                        TextView pourcentage = myDialog.findViewById(R.id.pourcentaget);
                                        String pourc = response.substring(18,response.length()-5);
                                        //PostConsultation(response.substring(3,16) ,pourc);
                                        pourcentage.setText(String.valueOf(100 -Math.round(Double.valueOf(response.substring(18,response.length()-6))))+"%");
                                        //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);
                                        ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                PostConsultation(response.substring(3,16) ,pourc);

                                            }
                                        });
                                        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                        // textResult.animateText(response.substring(3,16));

                                      /*  mBinding.textResult.setText(response.substring(3,16));
                                        mBinding.poucentage.setText(response.substring(18,response.length()-5));*/

                                    }



                                }
                            }, 2000);


                            //   JSONArray array = new JSONArray(response);

                            System.out.println(response);
                            toastMessage(response);


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
                toastMessage(message);
                animationView.pauseAnimation();
                animationView.setVisibility(View.GONE);
                animationViewResFailed.setVisibility(View.VISIBLE);
                // mBinding.animationViewResFailed.loop(true);
                animationViewResFailed.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        firstAnnimation.setVisibility(View.GONE);
                        animationViewResFailed.pauseAnimation();
                        animationViewResFailed.setVisibility(View.GONE);
                        r.setVisibility(View.VISIBLE);




                    }
                }, 2000);

                return;

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });



        queue.add(stringRequest);

        // queue.add(stringRequest);
    }

    private void PostConsultation(String type ,String porcentage) {

        RequestQueue queue = Volley.newRequestQueue(SurveyActivity.this);
        final String URI = util.getDomaneName()+"/api/Consultations";
        System.out.println(dateP.substring(0,10));
        System.out.println(dateP.substring(11,16));
        params.put("imageName",imageName);
        params.put("date",dateP);
        params.put("typeC",type);
        params.put("pourcentageC",porcentage);
        params.put("patient",mPreferences.getInt(getString(R.string.Id),0));


        //  params.put("")
        Log.d(TAG,"Json"+new JSONObject(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
//
                            toastMessage("Merci :)");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    startActivity(new Intent(SurveyActivity.this, smartdermato.esprit.tn.smartdermato.MainActivity.class));
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
                        Toast.makeText(SurveyActivity.this, "Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        toastMessage(error.toString());
                        animationView.pauseAnimation();
                        animationView.setVisibility(View.GONE);
                        animationViewResFailed.setVisibility(View.VISIBLE);
                        // mBinding.animationViewResFailed.loop(true);
                        animationViewResFailed.playAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstAnnimation.setVisibility(View.GONE);
                                animationViewResFailed.pauseAnimation();
                                animationViewResFailed.setVisibility(View.GONE);
                                r.setVisibility(View.VISIBLE);
                                startActivity(new Intent(SurveyActivity.this, MenuActivity.class));




                            }
                        }, 2000);

                        return ;

                    }
                });





        queue.add(stringRequest);
    }

    public void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
