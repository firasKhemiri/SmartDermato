package smartdermato.esprit.tn.smartdermato.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.AESCrypt;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.ActivitySignupBinding;

public class Activity_SignUp extends AppCompatActivity {

    private ActivitySignupBinding mBinding;
    private RelativeLayout rellay1, rellay2;
    private Users Users;
    private String passwordCrypt;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ValueAnimator anim;
    private String role="patient";
    private int widthOriginal = 0;
    private Map<String,Object> params = new HashMap<String, Object>();
    private static final String TAG = "Activity_SignUp";
//    private FirebaseAuth auth;
    private Window window;
    ProgressDialog prgDialog,mDialog;
    private final  int PDF_RESULT= 1;
    String pdfName = "vide_pdf";
    private String pdfPath;
    private boolean uploaded = false;



    private boolean existe = false;
//    private DatabaseReference reference;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        window=getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        window.setNavigationBarColor(Color.parseColor("#17A8C2"));
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prgDialog = new ProgressDialog(this);

        preferences = getSharedPreferences("x", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }
    public void uploadPDF(View view)
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PDF_RESULT)
        {
            if (resultCode == RESULT_OK)
            {
                Uri uri = data.getData();
                System.out.println("URI"+uri.toString());
                toastMessage(uri.toString());
                try {
                    pdfPath = getFilePathFromURI(getBaseContext(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //uri.getPath();
                        //getRealPathFromURI(uri);
                System.out.println("pdfPath "+pdfPath);
                // user_pic.setImageURI(uri);
                try {
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    pdfName = "SmartDermatoPDF-"+n+".pdf";
                    File file = new File(pdfPath);
                    RequestBody pdfContent = RequestBody.create(MediaType.parse("multipart/form-data"),file);

                    MultipartBody.Part photo = MultipartBody.Part.createFormData("photo",file.getName(),pdfContent);
                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"),pdfName);
                    UploadService uploadService = APIClient.getClient().create(UploadService.class);



                    mDialog = new ProgressDialog(this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();


                    uploadService.Upload(photo,description).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                toastMessage("Success");
                                prgDialog.setMax(100);
                                prgDialog.setTitle("Upload Certification");
                                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mDialog.dismiss();
                                prgDialog.show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (prgDialog.getProgress()<= prgDialog.getMax())
                                        {
                                            try {
                                                Thread.sleep(200);
                                                handler2.sendMessage(handler2.obtainMessage());
                                                if (prgDialog.getProgress() == prgDialog.getMax()){
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            prgDialog.dismiss();
                                                            mBinding.Next.setEnabled(true);
                                                            uploaded = true;
                                                        }
                                                    });

                                                    //NotificationUpload();

//                                                    users.setUser_pic(imageName);
//                                                    putUser(users,"upload");
//                                                    prgDialog.dismiss();
//                                                    break;


                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }).start();


                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("erreur..........."+t.getMessage());
                            toastMessage(t.getMessage());
                            mDialog.dismiss();
                            uploaded = false;
                            prgDialog.dismiss();

                        }
                    });


                }catch (Exception e){
                    mDialog.dismiss();
                    toastMessage("erreur 1....");
                    uploaded=false;

                }

            }
        }
    }
    public static String getFilePathFromURI(Context context, Uri contentUri) throws IOException {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File TEMP_DIR_PATH = context.getFilesDir();
            File copyFile = new File(TEMP_DIR_PATH + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }
    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
    public static void copy(Context context, Uri srcUri, File dstFile) throws IOException {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private String getRealPathFromURI(Uri contentUri)
//    {
//        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
//        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
//        String[] proj = new String[]{ mimeType };
//       // String[] proj = {MediaStore.Files.FileColumns.MINE};
//        CursorLoader loader = new CursorLoader(getApplicationContext(),contentUri,proj,null,null,null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = (int) cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);
//        cursor.moveToNext();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        return result;
//    }

    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    public void onCheckboxClicked(View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mBinding.checkboxMeat.isChecked()){
                    mBinding.uploadCertification.setVisibility(View.VISIBLE);
                    mBinding.Next.setVisibility(View.VISIBLE);
                    mBinding.signInBtn.setVisibility(View.GONE);
                  //  mBinding.Next.setEnabled(false);
                    role = "medecin";
                }
                else {
                    mBinding.uploadCertification.setVisibility(View.GONE);
                    mBinding.Next.setVisibility(View.GONE);
                    mBinding.signInBtn.setVisibility(View.VISIBLE);


                    role = "patient";
                }

            }
        },100);
    }
    public void load(View view){

        animateButtonWidth();
        fadeOutTextAndSetProgressDialog();
        toastMessage(role);
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
    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prgDialog.incrementProgressBy(1);
        }
    };

    public void animateButtonWidth(){
        if(role.equals("patient")){

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
        else {

            widthOriginal = mBinding.Next.getMeasuredWidth();
            ValueAnimator anim = ValueAnimator.ofInt(mBinding.Next.getMeasuredWidth(),getFinalWidth());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int value = (Integer) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mBinding.Next.getLayoutParams();
                    layoutParams.width = value;
                    mBinding.Next.requestLayout();
                }
            });
            anim.setDuration(250);
            anim.start();
        }



    }

    private void  fadeOutTextAndSetProgressDialog(){
        if(role.equals("patient")){
            mBinding.signInText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showProgressDialog();
                }
            }).start();
        }else {
            mBinding.signInTextN.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showProgressDialog();
                }
            }).start();
        }

    }

    private void showProgressDialog(){
        if(role.equals("patient")){
            mBinding.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            mBinding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mBinding.progressBarN.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            mBinding.progressBarN.setVisibility(View.VISIBLE);
        }

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
        if(role.equals("patient")) {

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
                    Intent intent = new Intent(Activity_SignUp.this, CorpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            reveal.start();
        }
        else {
            mBinding.Next.setElevation(0f);
            mBinding.revealView.setVisibility(View.VISIBLE);

            int x = mBinding.revealView.getWidth();
            int y = mBinding.revealView.getHeight();
            int startX = (int) (getFinalWidth() / 2 + mBinding.Next.getX());
            int startY = (int) (getFinalWidth() / 2 + mBinding.Next.getY());

            float raduis = Math.max(x, y) * 2.2f;
            Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.revealView, startX, startY, getFinalWidth(), raduis);
            reveal.setDuration(450);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Intent intent = new Intent(Activity_SignUp.this, ActivityInformation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            reveal.start();
        }
    }
    private  void fadeOutProgressDialog(){
        if(role.equals("patient")) {
            mBinding.progressBar.animate().alpha(0f).setDuration(200).start();

        }
        else
        {
            mBinding.progressBarN.animate().alpha(0f).setDuration(200).start();

        }

    }

    private void  deleyedStartNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(role.equals("patient"))
                {
                    SingUp();
                }
                else
                {
                    Next();

                }

               // startActivity(new Intent(Activity_SignUp.this, Home.class));
            }
        },100);
    }
    private int getFinalWidth(){
        return (int) getResources().getDimension(R.dimen.get_width);
    }
    private void Next(){

        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(mBinding.email.getText());
        if(mBinding.username.getText().toString().equals("admin")||mBinding.username.getText().toString().equals("unknown") || mBinding.username.getText().toString().equals("vide"))
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("choisir un autre username ");
            builderSingle.show();
            StopAnimBtnN();

            return ;
        }
         if(mBinding.username.getText().length()==0 ||mBinding.password.getText().length()==0||mBinding.email.getText().length()==0||!m.matches() || !uploaded)
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("verifier vos chanp ");
            builderSingle.show();
            StopAnimBtnN();

            return ;
        }
         if(mBinding.password.getText().length()<6)
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("Password must be at least 6 characters ");
            builderSingle.show();
            StopAnimBtnN();

            return ;
        }
         if(!uploaded)
        {
            toastMessage("upload your certification please");
            StopAnimBtnN();
            return;
        }

            try {
                System.out.println(mBinding.password.getText());
                passwordCrypt = AESCrypt.encrypt(mBinding.password.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String URL = util.getDomaneName() + "/getbyemail/" + mBinding.email.getText() + "/" + mBinding.username.getText() + "/";
            System.out.println("URLLLL:  "+ URL);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    System.out.println(response + "NOM");



                    // myDialog.dismiss();
                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Activity_SignUp.this);
                    builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                    builderSingle.setTitle("Login");
                    builderSingle.setMessage("Verifier votre Username ou Email ");
                    builderSingle.show();
                    StopAnimBtnN();








                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(Activity_SignUp.this, ActivityInformation.class);
                    Log.d("putExtra",mBinding.username.getText()+"  "+passwordCrypt+"  "+mBinding.email.getText());
                    intent.putExtra("username",mBinding.username.getText().toString());
                    intent.putExtra("password",passwordCrypt);
                    intent.putExtra("email",mBinding.email.getText().toString());
                    intent.putExtra("certification",pdfName);
                    startActivity(intent);


                    return;
                }
            });
            RequestQueue queue = Volley.newRequestQueue(Activity_SignUp.this);
            queue.add(stringRequest);







    }

    private void SingUp(){
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(mBinding.email.getText());
        if(mBinding.username.getText().toString().equals("admin") || mBinding.username.getText().toString().equals("vide"))
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("choisir un autre username ");
            builderSingle.show();
            StopAnimBtn();

            return ;
        }
        if(mBinding.username.getText().length()==0 ||mBinding.password.getText().length()==0||mBinding.email.getText().length()==0||!m.matches())
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("verifier vos chanp ");
            builderSingle.show();
            StopAnimBtn();

            return ;
        }
        if(mBinding.password.getText().length()<6)
        {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
            builderSingle.setTitle("Sing Up");
            builderSingle.setMessage("Password must be at least 6 characters ");
            builderSingle.show();
            StopAnimBtn();

            return ;
        }

        try {
            System.out.println(mBinding.password.getText());
            passwordCrypt = AESCrypt.encrypt(mBinding.password.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String URL = util.getDomaneName() + "/getbyemail/" + mBinding.email.getText() + "/" + mBinding.username.getText() + "/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response + "NOM");



                    // myDialog.dismiss();
                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Activity_SignUp.this);
                    builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                    builderSingle.setTitle("Login");
                    builderSingle.setMessage("Verifier votre Username ou Email ");
                    builderSingle.show();
                    StopAnimBtn();








            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Users users = new Users();
                users.setUsername(mBinding.username.getText().toString());
                users.setPassword(passwordCrypt);
                users.setEmail(mBinding.email.getText().toString());
                users.setUser_pic("vide_pic");
                users.setStatus("offline");
                users.setRole(role);
                users.setFirstName("");
                users.setLastName("");
                users.setPhoneNumber("");
                users.setSexe("");
                users.setDateOfBirth("");
                users.setCertification("");
                users.setCertificatDate("");
                users.setCity("");
                users.setCountry("");
                users.setPostalCode("");
                users.setOfficeNumber("");
                users.setOfficeAddess("");
                SingUpRequete(users);
//                auth = FirebaseAuth.getInstance();
//                String txt_username = mBinding.username.getText().toString();
//                String txt_email = mBinding.email.getText().toString();
//                String txt_password = mBinding.password.getText().toString();
//                if(TextUtils.isEmpty(txt_username)||TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
//                    Toast.makeText(Activity_SignUp.this,"All fileds are required ",Toast.LENGTH_SHORT).show();
//                }
//                else if (txt_password.length()<6) {
//                    Toast.makeText(Activity_SignUp.this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                }
                return;
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Activity_SignUp.this);
        queue.add(stringRequest);





    }
    public void StopAnimBtnN(){
        anim = ValueAnimator.ofInt(mBinding.Next.getMeasuredWidth(),widthOriginal);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mBinding.Next.getLayoutParams();
                layoutParams.width = value;
                mBinding.Next.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
        mBinding.signInTextN.setVisibility(View.VISIBLE);
        mBinding.signInTextN.animate().alpha(10f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.progressBarN.setVisibility(View.INVISIBLE);
                mBinding.progressBarN.animate().alpha(10f).setDuration(200).start();



            }
        }).start();

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
    public void SingUpRequete(Users users){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Users";
        params.put("Username",users.getUsername());
        params.put("Password",users.getPassword());
        params.put("Email",users.getEmail());

        params.put("ProfilePic",users.getUser_pic());
        params.put("FirstName",users.getFirstName());
        params.put("LastName",users.getLastName());
        params.put("DateOfBirth",users.getDateOfBirth());

        params.put("Sexe",users.getSexe());
        params.put("PhoneNumber",users.getPhoneNumber());

        params.put("certification",users.getCertification());
        params.put("EtatCertification",users.getEtatCertification());
        params.put("City",users.getCity());
        params.put("Country",users.getCountry());
        params.put("OfficeNumber",users.getOfficeNumber());
        params.put("OfficeAdress",users.getOfficeAddess());
        params.put("PostalCode",users.getPostalCode());
        params.put("Certificatdate",users.getCertificatDate());

        params.put("role",users.getRole());
        params.put("Status",users.getStatus()
        );
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
                        Toast.makeText(Activity_SignUp.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
//    private void signupFRB(final String username, String email, String password, final int idUser){
//
//        auth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            FirebaseUser firebaseUser = auth.getCurrentUser();
//                            assert firebaseUser != null;
//                            String userid = firebaseUser.getUid();
//                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//                            HashMap<String,String> hashMap = new HashMap<>();
//                            hashMap.put("id",userid);
//                            hashMap.put("username",username);
//                            hashMap.put("imageURL","default");
//
//                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//
//                                        new Handler().postDelayed(new Runnable() {
//                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                                            @Override
//                                            public void run() {
//
//
//                                                revealButton();
////                                                        startActivity(new Intent(Activity_Login.this, CorpActivity.class));
//
//
//
//                                            }
//                                        },100);
//                                    }
//                                }
//                            });
//                        }else {
//                            deleteUser(idUser);
//                            Toast.makeText(Activity_SignUp.this,"You can't register with this email or password",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
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
                                    editor.commit();
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
                        Toast.makeText(Activity_SignUp.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    private void NextPage(Users users)
    {




        editor.putString(getString(R.string.CountryOfficeNumber),users.getCountryOfficeNumber());
        editor.putString(getString(R.string.CountryPhoneNumber),users.getCountryPhoneNumber());
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
        if(role.equals("medecin")){
            editor.putString(getString(R.string.certification),Users.getCertification());
            editor.putInt(getString(R.string.EtatCertification),Users.getEtatCertification());
        }

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


}
