package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;

import java.io.File;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.CorpHommeBinding;
import smartdermato.esprit.tn.smartdermato.databinding.PopSexeBinding;

public class CorpActivity extends AppCompatActivity {
   private CorpHommeBinding mBinding;
   private PopSexeBinding mBindingPS;
    RelativeLayout corpHommeAv,corpHommeAr;
    ImageView btnRoateAr,btnRoateAv;
    private Window window;

    private final  int IMG_RESULT= 1;
    private final static int REQUEST_CODE_2 = 2;

    ProgressDialog prgDialog,mDialog;
    private String imagePath;
    private String imageName = "vide_pic";
    Dialog myDialog;
    String Sexe = "";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    public int rotate = 0;
    private static final int RequestCode = 746;
    private static final int NumberOfImagesToSelect = 1;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corp_homme);
        mBinding = DataBindingUtil.setContentView(this, R.layout.corp_homme);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreferences= getSharedPreferences("x", Context.MODE_PRIVATE);
        editor = mPreferences.edit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prgDialog = new ProgressDialog(this);
        window=getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));

        if(mPreferences.getString(getString(R.string.SexePop),"").equals("Femme")){
            Sexe = mPreferences.getString(getString(R.string.SexePop),"");
            mBinding.corpFemmeAv.setVisibility(View.VISIBLE);
            mBinding.corpHommeAv.setVisibility(View.GONE);
        }else if(mPreferences.getString(getString(R.string.SexePop),"").equals("Homme"))
        {
            Sexe = mPreferences.getString(getString(R.string.SexePop),"");
        }
        else
        {
            showPoP();
        }
        final View parent = (View) mBinding.headAv.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {

                final Rect rect = new Rect();
                mBinding.headAv.getHitRect(rect);
                rect.top -= 500;    // increase top hit area
                rect.left -= 500;   // increase left hit area
                rect.bottom -= 500; // increase bottom hit area
                rect.right -= 500;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , mBinding.headAv));
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showPoP(){
        myDialog = new Dialog(this);

        myDialog.setContentView(R.layout.pop_sexe);
        myDialog.setCanceledOnTouchOutside(false);

      //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public  void  sexeHomme(View view)
    {
        System.out.println("sexeH");
        Sexe = "Homme";
        editor.putString(getString(R.string.SexePop),Sexe).apply();

        myDialog.dismiss();

    }
    public  void  sexeFemme(View view)
    {
        System.out.println("sexeF");

        Sexe = "Femme";
        editor.putString(getString(R.string.SexePop),Sexe).apply();
        myDialog.dismiss();
        mBinding.corpFemmeAv.setVisibility(View.VISIBLE);
        mBinding.corpHommeAv.setVisibility(View.GONE);

    }
    public void rotate(View view){
        System.out.println("------------------------------------"+rotate);
        if(rotate == 0 && Sexe.equals("Homme")){
            System.out.println("------------------------------------A");
             mBinding.corpHommeAv.setVisibility(View.GONE);
             mBinding.corpHommeAr.setVisibility(View.VISIBLE);
            mBinding.btnRoateAv.setImageResource(R.drawable.ic_rotate_right_black_24dp);
         //   mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));


            rotate = 1;
        }else if (rotate == 1 && Sexe.equals("Homme")) {

           mBinding.corpHommeAv.setVisibility(View.VISIBLE);
           mBinding.corpHommeAr.setVisibility(View.GONE);
            mBinding.btnRoateAv.setImageResource(R.drawable.ic_rotate_left_black_24dp);
            System.out.println("------------------------------------B");
           // mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            rotate = 0;
        }else if(rotate == 0 && Sexe.equals("Femme")){
            mBinding.corpFemmeAv.setVisibility(View.GONE);
            mBinding.corpFemmeAr.setVisibility(View.VISIBLE);
            mBinding.btnRoateAv.setImageResource(R.drawable.ic_rotate_right_black_24dp);
           // mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            rotate = 1;
        }else if(rotate == 1 && Sexe.equals("Femme")){
            mBinding.corpFemmeAv.setVisibility(View.VISIBLE);
            mBinding.corpFemmeAr.setVisibility(View.GONE);
            mBinding.btnRoateAv.setImageResource(R.drawable.ic_rotate_left_black_24dp);
            rotate = 0;
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void head(View view){
        String arrayName[]={"Visage","Oreille","Cou","Cuir chevelu"};
        if(rotate == 0 && Sexe.equals("Homme")){
            mBinding.headAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();
        }
        else if(rotate == 1 && Sexe.equals("Homme")) {
            mBinding.headAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }else   if(rotate == 0 && Sexe.equals("Femme")){
            mBinding.headFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();
        }
        else if(rotate == 1 && Sexe.equals("Femme")) {
            mBinding.headFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void body(View view){
        String arrayName[]={"Visage","Oreille","Cou","Cuir chevelu"};
        if(rotate == 0 && Sexe.equals("Homme")){
            mBinding.bodyAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
        else if(rotate == 1 && Sexe.equals("Homme")) {
            mBinding.bodyAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }else if(rotate == 0 && Sexe.equals("Femme")){
            mBinding.bodyFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.headFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
        else if(rotate == 1 && Sexe.equals("Femme")) {
            mBinding.bodyFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.headFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void basin(View view) {
        String arrayName[] = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0 && Sexe.equals("Homme")) {
            mBinding.bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Homme")) {
            mBinding.bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }else   if (rotate == 0 && Sexe.equals("Femme")) {
            mBinding.bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Femme")) {
            mBinding.bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void legs(View view) {
        String arrayName[] = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0 && Sexe.equals("Homme")) {
            mBinding.bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAv.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Homme")) {
            mBinding.bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsAr.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();


        }else  if (rotate == 0 && Sexe.equals("Femme")) {
            mBinding.bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Femme")) {
            mBinding.bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.LegsFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();


        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hand(View view) {
        String arrayName[] = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0  && Sexe.equals("Homme")) {
            mBinding.bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handRightAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else if (rotate == 1  && Sexe.equals("Homme")) {
            mBinding.bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handRightAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        } else  if (rotate == 0  && Sexe.equals("Femme")) {
            mBinding.bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handRightFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else if (rotate == 1  && Sexe.equals("Femme")) {
            mBinding.bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.headFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            mBinding.handLeftFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.handRightFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            mBinding.LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    public void skip(View view){
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                // revealButton();
                //startActivity(new Intent(Activity_Login.this, Home.class));


                startActivity(new Intent(CorpActivity.this, MainActivity.class));
                finish();
            }
        },100);

    }
    public Uri imageUri;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void camera(){

      /*  Pix.start(this,                    //Activity or Fragment Instance
                RequestCode              //Request code for activity results
        );    //Number of images to restict selection count*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Intent result = Intent.createChooser(intent,getText(R.string.choose_file));
        startActivityForResult(result,IMG_RESULT);

       /* if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(cameraIntent, IMG_RESULT);

        }*/


    }
    @Override

    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(cameraIntent, IMG_RESULT);

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prgDialog.incrementProgressBy(1);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_RESULT)
        {
            if (resultCode == RESULT_OK)
            {
                Intent i = new Intent(this, smartdermato.esprit.tn.smartdermato.ImageFilters.MainActivity.class);
                i.setData( data.getData());

                //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(i, REQUEST_CODE_2);
               // mBinding.animationView.setAnimation(String.valueOf(R.raw.loading));
             /*   window.setStatusBarColor(Color.parseColor("#19AA8B"));

                mBinding.allCorp.setVisibility(View.GONE);
                mBinding.rellay2.setVisibility(View.GONE);
                mBinding.firstAnnimation.setVisibility(View.VISIBLE);
                mBinding.animationView.setVisibility(View.VISIBLE);
              //  mBinding.animationView.setBackgroundColor(R.color.gradStart);
              //  mBinding.animationView.setBackgroundColor(R.color.gradStop);
                mBinding.animationView.loop(true);
                mBinding.animationView.playAnimation();
                Uri uri = data.getData();

                //toastMessage(imageUri.toString());
                imagePath = getRealPathFromURI(uri);
                System.out.println("imagePath "+imagePath);
                // user_pic.setImageURI(uri);
                try {
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    imageName = "SmartDermatoIMG-"+n+".png";
                    File file = new File(imagePath);
                    RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"),file);

                    MultipartBody.Part photo = MultipartBody.Part.createFormData("photo",file.getName(),photoContent);
                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"),imageName);
                    UploadService uploadService = APIClient.getClient().create(UploadService.class);*/



                   /* mDialog = new ProgressDialog(this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();*/


                   /* uploadService.Upload(photo,description).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                AnalyserImages();



                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("erreur...........");
                            toastMessage(t.getMessage());
                            //mDialog.dismiss();
                            prgDialog.dismiss();
                            mBinding.animationView.pauseAnimation();
                            mBinding.animationView.setVisibility(View.GONE);
                            mBinding.animationViewResFailed.setVisibility(View.VISIBLE);
                            //mBinding.animationViewResFailed.loop(true);

                            mBinding.animationViewResFailed.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.firstAnnimation.setVisibility(View.GONE);
                                    mBinding.animationViewResFailed.pauseAnimation();
                                    mBinding.animationViewResFailed.setVisibility(View.GONE);
                                    mBinding.allCorp.setVisibility(View.VISIBLE);
                                    clean();



                                }
                            }, 2000);


                        }
                    });


                }catch (Exception e){
                    mDialog.dismiss();
                    toastMessage("erreur 1....");

                }*/

            }
            else {
               clean();

            }
        }
        if (requestCode == REQUEST_CODE_2) {
            clean();
        }
    }
    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this.getApplicationContext(),contentUri,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToNext();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    private void setFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.main_fram,fragment).commit();
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

                            mBinding.animationView.pauseAnimation();
                            mBinding.animationView.setVisibility(View.GONE);
                            mBinding.animationViewRes.setVisibility(View.VISIBLE);
                            //mBinding.animationViewRes.loop(true);

                            mBinding.animationViewRes.playAnimation();
                            //mBinding.animationViewRes.pauseAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.animationViewRes.setVisibility(View.GONE);
                                    mBinding.firstAnnimation.setVisibility(View.GONE);
                                    mBinding.result.setVisibility(View.VISIBLE);
                                    window.setStatusBarColor(Color.parseColor("#57717A"));

                                    //mBinding.animationViewResult.loop(true);
                                    mBinding.animationViewResult.playAnimation();
                                    toastMessage(response.substring(0,1));
                                    System.out.println("subbbbb: "+response.substring(1,2));
                                    System.out.println("subbbbb2: "+response.substring(14,response.length()-5));
                                    System.out.println("subbbbb3: "+response.substring(3,12));
                                    if(response.substring(1,2).equals("1"))
                                    {
                                       // mBinding.textResult.setOnClickListener(new  );
                                       mBinding.textResult.setAnimationListener(new AnimationListener() {
                                           @Override
                                           public void onAnimationEnd(HTextView hTextView) {
                                               toastMessage("end");
                                               mBinding.poucentage.setAnimationListener(new AnimationListener() {
                                                   @Override
                                                   public void onAnimationEnd(HTextView hTextView) {
                                                       mBinding.rellay2.setVisibility(View.VISIBLE);
                                                       //PostCinsultation();
                                                   }
                                               });
                                               mBinding.poucentage.animateText(response.substring(14,response.length()-5));
                                           }
                                       });
                                        mBinding.textResult.animateText(response.substring(3,12));
                                       // mBinding.textResult.setText(response.substring(3,12));


                                    }
                                    else

                                    {
                                        mBinding.textResult.animateText(response.substring(3,16));

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
                mBinding.animationView.pauseAnimation();
                mBinding.animationView.setVisibility(View.GONE);
                mBinding.animationViewResFailed.setVisibility(View.VISIBLE);
               // mBinding.animationViewResFailed.loop(true);
                mBinding.animationViewResFailed.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.firstAnnimation.setVisibility(View.GONE);
                        mBinding.animationViewResFailed.pauseAnimation();
                        mBinding.animationViewResFailed.setVisibility(View.GONE);
                        mBinding.allCorp.setVisibility(View.VISIBLE);
                        clean();



                }
                }, 2000);

                return;

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);

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

    /*private void PostCinsultation() {

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









                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
                        Toast.makeText(Activity_SignUp.this, "Peut estre deja inscrit |OU| Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }*/

    public void clean()
    {
        mBinding.bodyAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.basinAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handRightAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.LegsAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.bodyAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.basinAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handRightAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.LegsAv.setColorFilter(Color.parseColor("#0057717A"));


        mBinding.bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.headFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.basinFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.headFAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.basinFAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
        mBinding.LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
    }
   /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Pix.start(this, RequestCode, NumberOfImagesToSelect);
                else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }*/
}
