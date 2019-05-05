package smartdermato.esprit.tn.smartdermato.Activities;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;
import com.hanks.htextview.typer.TyperTextView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.Adapter.AdapterAccueil;
import smartdermato.esprit.tn.smartdermato.Adapter.AdapterHome;
import smartdermato.esprit.tn.smartdermato.Adapter.HomeMultiViewTypeAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Consultation;
import smartdermato.esprit.tn.smartdermato.Entities.FeedItem;
import smartdermato.esprit.tn.smartdermato.Entities.Model;
import smartdermato.esprit.tn.smartdermato.ImageFilters.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.HomeBinding;
import smartdermato.esprit.tn.smartdermato.databinding.PopTakePhotoBinding;

public class AccueilTest extends Fragment {

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private HomeBinding mBinding;
    private PopTakePhotoBinding ptpBinding;
    private ViewPager viewPager;
    private Dialog myDialogSexe;
    private ImageView btnRoateAv,headAv,bodyAv,basinAv,handLeftAv,handRightAv,LegsAv,headAr,bodyAr,basinAr,handLeftAr,handRightAr,LegsAr;
    private ImageView btnRoateFAv,headFAv,bodyFAv,basinFAv,handLeftFAv,handRightFAv,LegsFAv,headFAr,bodyFAr,basinFAr,handLeftFAr,handRightFAr,LegsFAr;
    private String dateP;
    private Map<String, Object> params = new HashMap<String, Object>();
    private RelativeLayout corpHommeAv,corpHommeAr,corpFemmeAv,corpFemmeAr;
    private String Sexe = "Homme";
    private int rotate = 0;
    private final  int IMG_RESULT= 1;
    private final static int REQUEST_CODE_2 = 2;

    private String imageName = "vide_pic";
    private SharedPreferences mPreferences;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private LottieAnimationView animationViewRes,animationView,animationViewResult,animationViewResFailed;
    private RelativeLayout firstAnnimation,result,allCorp;
    private TyperTextView textResult,poucentage;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private SharedPreferences.Editor editor;
    private List<Consultation> consultationList;
    private AdapterHome adapterAccueil;
    private static final int RequestCode = 746;
    private static final int NumberOfImagesToSelect = 1;
    private static final String TAG = "Accueil";
    
    

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.home, container, false);
        final Context context = getActivity();


        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        consultationList = new ArrayList<>();

        adapterAccueil = new AdapterHome(consultationList,getActivity());

        recyclerView.setAdapter(adapterAccueil);
        adapterAccueil.notifyDataSetChanged();



        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);
        editor = mPreferences.edit();
        FloatingActionButton takePicture = root.findViewById(R.id.fab);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        TextView username = root.findViewById(R.id.username);
        CircleImageView profileImage = root.findViewById(R.id.profile_image);
        ImageButton btn_chat = root.findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {

                        startActivity(new Intent(context, Activity_Chat_PM.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    }
                },200);
            }
        });

        username.setText(mPreferences.getString(getString(R.string.username),""));
                if (mPreferences.getString(getString(R.string.user_pic),"").equals("vide_pic"))
                {
                    profileImage.setImageResource(R.drawable.userprofile);
                }
                else
                {


                        Picasso.with(getActivity())
                                .load(util.getDomaneName()+"/Content/Images/"+mPreferences.getString(getString(R.string.user_pic),"")).into(profileImage);



                }
        takePicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(Objects.equals(mPreferences.getString(getString(R.string.SexePop), ""), "null"))
                {
                    showPoP();
                }else
                {
                    ShowPopup();
                }

            }
        });

        Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG).show();

        List<Model> models = new ArrayList<>();
        models.add(new Model(R.drawable.brochure,"19/02/2019","12h:59"));
        models.add(new Model(R.drawable.sticker,"15-02-2019","20h:09"));
        models.add(new Model(R.drawable.poster,"19/01/2019","10h:50"));
        models.add(new Model(R.drawable.namecard,"02-12-2018","00h:00"));

        ArrayList<FeedItem> items = new ArrayList<>();

        FeedItem item1 = new FeedItem(1,"test",10,false,false,"","tester","",10,10,"23");
        FeedItem item2 = new FeedItem(2,"test",10,true,true,"","tester","",11,10,"23");
        FeedItem item3 = new FeedItem(3,"test",10,false,false,"","tester","",10,10,"23");
        FeedItem item4 = new FeedItem(4,"test",10,true,true,"","tester","",12,10,"23");

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);

        HomeMultiViewTypeAdapter adapterAccueil = new HomeMultiViewTypeAdapter(items, getActivity());
        recyclerView.setAdapter(adapterAccueil);



        adapterAccueil.notifyDataSetChanged();



        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ShowPopup() {
        Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.pop_take_photo);
        myDialog.setCanceledOnTouchOutside(false);
        btnRoateAv = myDialog.findViewById(R.id.btn_roate_av);
        corpHommeAv = myDialog.findViewById(R.id.corp_homme_av);
        corpHommeAr = myDialog.findViewById(R.id.corp_homme_ar);
        corpFemmeAv = myDialog.findViewById(R.id.corp_femme_av);
        corpFemmeAr = myDialog.findViewById(R.id.corp_femme_ar);
        headAv = myDialog.findViewById(R.id.head_av);
        headAr = myDialog.findViewById(R.id.head_ar);
        bodyAv = myDialog.findViewById(R.id.body_av);
        bodyAr = myDialog.findViewById(R.id.body_ar);
        basinAv = myDialog.findViewById(R.id.basin_av);
        basinAr = myDialog.findViewById(R.id.basin_ar);
        handLeftAv = myDialog.findViewById(R.id.hand_left_av);
        handLeftAr = myDialog.findViewById(R.id.hand_left_ar);
        handRightAv = myDialog.findViewById(R.id.hand_right_av);
        handRightAr = myDialog.findViewById(R.id.hand_right_ar);
        LegsAv = myDialog.findViewById(R.id.Legs_av);
        LegsAr = myDialog.findViewById(R.id.Legs_ar);
        headFAv = (ImageView) myDialog.findViewById(R.id.head_f_av);
        headFAr = (ImageView) myDialog.findViewById(R.id.head_f_ar);
        bodyFAv = (ImageView) myDialog.findViewById(R.id.body_f_av);
        bodyFAr = (ImageView) myDialog.findViewById(R.id.body_f_ar);
        basinFAv = (ImageView) myDialog.findViewById(R.id.basin_f_av);
        basinFAr = (ImageView) myDialog.findViewById(R.id.basin_f_ar);
        handLeftFAv = (ImageView) myDialog.findViewById(R.id.hand_left_f_av);
        handLeftFAr = (ImageView) myDialog.findViewById(R.id.hand_left_f_ar);
        handRightFAv = (ImageView) myDialog.findViewById(R.id.hand_right_f_av);
        handRightFAr = (ImageView) myDialog.findViewById(R.id.hand_right_f_ar);
        LegsFAv = (ImageView) myDialog.findViewById(R.id.Legs_f_av);
        LegsFAr = (ImageView) myDialog.findViewById(R.id.Legs_f_ar);
        animationView = myDialog.findViewById(R.id.animation_view);
        animationViewRes = myDialog.findViewById(R.id.animation_view_res);
        animationViewResult = myDialog.findViewById(R.id.animation_view_result);
        animationViewResFailed = myDialog.findViewById(R.id.animation_view_res_failed);
        firstAnnimation = myDialog.findViewById(R.id.first_annimation);
        allCorp = myDialog.findViewById(R.id.all_corp);
        result = myDialog.findViewById(R.id.result);
        textResult = myDialog.findViewById(R.id.text_result);
        poucentage = myDialog.findViewById(R.id.poucentage);

        getPoP();

        btnRoateAv.setOnClickListener(this::rotate);
        headAv.setOnClickListener(this::head);
        headAr.setOnClickListener(this::head);
        bodyAv.setOnClickListener(v -> body());
        bodyAr.setOnClickListener(v -> body());
        basinAv.setOnClickListener(this::basin);
        basinAr.setOnClickListener(this::basin);
        handLeftAv.setOnClickListener(this::hand);
        handLeftAr.setOnClickListener(this::hand);
        handRightAv.setOnClickListener(this::hand);
        handRightAr.setOnClickListener(this::hand);
        LegsAv.setOnClickListener(this::legs);
        LegsAr.setOnClickListener(this::legs);
        headFAv.setOnClickListener(this::head);
        headFAr.setOnClickListener(this::head);
        bodyFAv.setOnClickListener(v -> body());
        bodyFAr.setOnClickListener(v -> body());
        basinFAv.setOnClickListener(this::basin);
        basinFAr.setOnClickListener(this::basin);
        handLeftFAv.setOnClickListener(this::hand);
        handLeftFAr.setOnClickListener(this::hand);
        handRightFAv.setOnClickListener(this::hand);
        handRightFAr.setOnClickListener(this::hand);
        LegsFAv.setOnClickListener(this::legs);
        LegsFAr.setOnClickListener(this::legs);
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }




    private void getPoP()
    {
        switch (Objects.requireNonNull(mPreferences.getString(getString(R.string.SexePop), ""))) {
            case "Femme":
                Sexe = mPreferences.getString(getString(R.string.SexePop), "");
                corpFemmeAv.setVisibility(View.VISIBLE);
                corpHommeAv.setVisibility(View.GONE);
                break;
            case "Homme":
                Sexe = mPreferences.getString(getString(R.string.SexePop), "");
                break;
            default:
                break;
        }
    }

    private void showPoP(){
        myDialogSexe = new Dialog(Objects.requireNonNull(getActivity()));

        myDialogSexe.setContentView(R.layout.pop_sexe);
        myDialogSexe.setCanceledOnTouchOutside(false);
        FrameLayout Homme,Femme;
        Homme = myDialogSexe.findViewById(R.id.hommeInBtn);
        Femme = myDialogSexe.findViewById(R.id.FemmeInBtn);
        Homme.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                sexeHomme(v);
            }
        });
        Femme.setOnClickListener(this::sexeFemme);



        //  mBindingPS = DataBindingUtil.setContentView(this, R.layout.pop_sexe);

        Objects.requireNonNull(myDialogSexe.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogSexe.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void  sexeHomme(View view)
    {
        System.out.println("sexeH");
        Sexe = "Homme";
        editor.putString(getString(R.string.SexePop),Sexe).apply();

        myDialogSexe.dismiss();

        ShowPopup();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void  sexeFemme(View view)
    {
        System.out.println("sexeF");

        Sexe = "Femme";
        editor.putString(getString(R.string.SexePop),Sexe).apply();
        myDialogSexe.dismiss();
//       corpFemmeAv.setVisibility(View.VISIBLE);
//       corpHommeAv.setVisibility(View.GONE);

        ShowPopup();

    }
    private void rotate(View view){
        System.out.println("------------------------------------"+rotate);
        if(rotate == 0 && Sexe.equals("Homme")){
            System.out.println("------------------------------------A");
            corpHommeAv.setVisibility(View.GONE);
            corpHommeAr.setVisibility(View.VISIBLE);
            btnRoateAv.setImageResource(R.drawable.ic_rotate_right_black_24dp);
            //   mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));


            rotate = 1;
        }else if (rotate == 1 && Sexe.equals("Homme")) {

            corpHommeAv.setVisibility(View.VISIBLE);
            corpHommeAr.setVisibility(View.GONE);
            btnRoateAv.setImageResource(R.drawable.ic_rotate_left_black_24dp);
            System.out.println("------------------------------------B");
            // mBinding.headAr.setColorFilter(Color.parseColor("#0057717A"));
            rotate = 0;
        }else if(rotate == 0 && Sexe.equals("Femme")){
            corpFemmeAv.setVisibility(View.GONE);
            corpFemmeAr.setVisibility(View.VISIBLE);
            btnRoateAv.setImageResource(R.drawable.ic_rotate_right_black_24dp);
            // mBinding.headAv.setColorFilter(Color.parseColor("#0057717A"));
            rotate = 1;
        }else if(rotate == 1 && Sexe.equals("Femme")){
            corpFemmeAv.setVisibility(View.VISIBLE);
            corpFemmeAr.setVisibility(View.GONE);
            btnRoateAv.setImageResource(R.drawable.ic_rotate_left_black_24dp);
            rotate = 0;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void head(View view){
        String[] arrayName = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if(rotate == 0 && Sexe.equals("Homme")){
            headAv.setColorFilter(Color.parseColor("#90F0AF10"));
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();
        }
        else if(rotate == 1 && Sexe.equals("Homme")) {
            headAr.setColorFilter(Color.parseColor("#90F0AF10"));
            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }else   if(rotate == 0 && Sexe.equals("Femme")){
            headFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();
        }
        else if(rotate == 1 && Sexe.equals("Femme")) {
            headFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void body(){
        String[] arrayName = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if(rotate == 0 && Sexe.equals("Homme")){
            bodyAv.setColorFilter(Color.parseColor("#90F0AF10"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
        else if(rotate == 1 && Sexe.equals("Homme")) {
            bodyAr.setColorFilter(Color.parseColor("#90F0AF10"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }else if(rotate == 0 && Sexe.equals("Femme")){
            bodyFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            headFAv.setColorFilter(Color.parseColor("#0057717A"));
            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        }
        else if(rotate == 1 && Sexe.equals("Femme")) {
            bodyFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            headFAr.setColorFilter(Color.parseColor("#0057717A"));
            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void basin(View view) {
        String[] arrayName = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0 && Sexe.equals("Homme")) {
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#90F0AF10"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Homme")) {
            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#90F0AF10"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }else   if (rotate == 0 && Sexe.equals("Femme")) {
            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            headFAv.setColorFilter(Color.parseColor("#0057717A"));
            basinFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Femme")) {
            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            headFAr.setColorFilter(Color.parseColor("#0057717A"));
            basinFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void legs(View view) {
        String[] arrayName = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0 && Sexe.equals("Homme")) {
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Homme")) {
            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();


        }else  if (rotate == 0 && Sexe.equals("Femme")) {
            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            headFAv.setColorFilter(Color.parseColor("#0057717A"));
            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();

        } else  if (rotate == 1 && Sexe.equals("Femme")) {
            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            headFAr.setColorFilter(Color.parseColor("#0057717A"));
            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            camera();


        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hand(View view) {
        String[] arrayName = {"Visage", "Oreille", "Cou", "Cuir chevelu"};
        if (rotate == 0  && Sexe.equals("Homme")) {
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#90F0AF10"));
            handRightAv.setColorFilter(Color.parseColor("#90F0AF10"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else if (rotate == 1  && Sexe.equals("Homme")) {
            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#90F0AF10"));
            handRightAr.setColorFilter(Color.parseColor("#90F0AF10"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        } else  if (rotate == 0  && Sexe.equals("Femme")) {
            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
            headFAv.setColorFilter(Color.parseColor("#0057717A"));
            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            handRightFAv.setColorFilter(Color.parseColor("#90F0AF10"));
            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
            camera();

        } else if (rotate == 1  && Sexe.equals("Femme")) {
            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
            headFAr.setColorFilter(Color.parseColor("#0057717A"));
            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            handRightFAr.setColorFilter(Color.parseColor("#90F0AF10"));
            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
            camera();


        }
    }
    private Uri imageUri;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void camera(){


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
        startActivityForResult(result, IMG_RESULT);

    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //prgDialog.incrementProgressBy(1);
        }
    };

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    private void showDialog(final String msg, final Context context,
                            final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCamera()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(cameraIntent, IMG_RESULT);

            } else {


                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
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
                            //mBinding.animationViewRes.pauseAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animationViewRes.setVisibility(View.GONE);
                                    firstAnnimation.setVisibility(View.GONE);
                                    result.setVisibility(View.VISIBLE);
                                    //window.setStatusBarColor(Color.parseColor("#57717A"));

                                    //mBinding.animationViewResult.loop(true);
                                    animationViewResult.playAnimation();
                                    toastMessage(response.substring(0,1));
                                    System.out.println("subbbbb: "+response.substring(1,2));
                                    System.out.println("subbbbb2: "+response.substring(14,response.length()-5));
                                    System.out.println("subbbbb3: "+response.substring(3,12));
                                    if(response.substring(1,2).equals("1"))
                                    {
                                        // mBinding.textResult.setOnClickListener(new  );
                                        textResult.setAnimationListener(new AnimationListener() {
                                            @Override
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
                                        });
                                        textResult.animateText(response.substring(3,12));
                                        // mBinding.textResult.setText(response.substring(3,12));


                                    }
                                    else

                                    {
                                       textResult.animateText(response.substring(3,16));

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
                        allCorp.setVisibility(View.VISIBLE);
                        clean();



                    }
                }, 2000);

                return;

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());

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

    private void clean() {
        bodyAr.setColorFilter(Color.parseColor("#0057717A"));
        headAr.setColorFilter(Color.parseColor("#0057717A"));
        basinAr.setColorFilter(Color.parseColor("#0057717A"));
        handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
        handRightAr.setColorFilter(Color.parseColor("#0057717A"));
        LegsAr.setColorFilter(Color.parseColor("#0057717A"));
        bodyAv.setColorFilter(Color.parseColor("#0057717A"));
        headAv.setColorFilter(Color.parseColor("#0057717A"));
        basinAv.setColorFilter(Color.parseColor("#0057717A"));
        handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
        handRightAv.setColorFilter(Color.parseColor("#0057717A"));
        LegsAv.setColorFilter(Color.parseColor("#0057717A"));


        bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
        headFAr.setColorFilter(Color.parseColor("#0057717A"));
        basinFAr.setColorFilter(Color.parseColor("#0057717A"));
        handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
        handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
        LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
        bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
        headFAv.setColorFilter(Color.parseColor("#0057717A"));
        basinFAv.setColorFilter(Color.parseColor("#0057717A"));
        handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
        handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
        LegsFAv.setColorFilter(Color.parseColor("#0057717A"));
    }


    private void  verifyStoragePermitions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent(getContext(), MainActivity.class);
                i.setData( data.getData());

                //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(i, REQUEST_CODE_2);

               /* Intent i = new Intent(getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             //   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                // mBinding.animationView.setAnimation(String.valueOf(R.raw.loading));
                //window.setStatusBarColor(Color.parseColor("#19AA8B"));

                allCorp.setVisibility(View.GONE);
                //rellay2.setVisibility(View.GONE);
               firstAnnimation.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                //  mBinding.animationView.setBackgroundColor(R.color.gradStart);
                //  mBinding.animationView.setBackgroundColor(R.color.gradStop);
                animationView.loop(true);
                animationView.playAnimation();
                Uri uri = data.getData();

                //toastMessage(imageUri.toString());
                String imagePath = getRealPathFromURI(uri);
                System.out.println("imagePath " + imagePath);
                // user_pic.setImageURI(uri);*
                try {
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    imageName = "SmartDermatoIMG-" + n + ".png";
                    File file = new File(imagePath);
                    RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContent);
                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), imageName);



                    UploadService uploadService = APIClient.getClient().create(UploadService.class);



                   /* mDialog = new ProgressDialog(this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();*/


                  /*  uploadService.Upload(photo, description).enqueue(new Callback<ResponseBody>() {
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
                            //mDialog.dismiss();
                           // prgDialog.dismiss();
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
                                    allCorp.setVisibility(View.VISIBLE);
                                    clean();


                                }
                            }, 2000);


                        }
                    });


                } catch (Exception e) {
                    //mDialog.dismiss();
                    toastMessage("erreur 1....");

                }*/

            } else {
                clean();
            }

      /*  if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
//            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));


//            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
//            headFAr.setColorFilter(Color.parseColor("#0057717A"));
//            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
//            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
//            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
//            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
//            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
//            headFAv.setColorFilter(Color.parseColor("#0057717A"));
//            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
//            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
//            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
//            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));


        }else {

            bodyAr.setColorFilter(Color.parseColor("#0057717A"));
            headAr.setColorFilter(Color.parseColor("#0057717A"));
            basinAr.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAr.setColorFilter(Color.parseColor("#0057717A"));
            handRightAr.setColorFilter(Color.parseColor("#0057717A"));
            LegsAr.setColorFilter(Color.parseColor("#0057717A"));
            bodyAv.setColorFilter(Color.parseColor("#0057717A"));
            headAv.setColorFilter(Color.parseColor("#0057717A"));
            basinAv.setColorFilter(Color.parseColor("#0057717A"));
            handLeftAv.setColorFilter(Color.parseColor("#0057717A"));
            handRightAv.setColorFilter(Color.parseColor("#0057717A"));
            LegsAv.setColorFilter(Color.parseColor("#0057717A"));

//            bodyFAr.setColorFilter(Color.parseColor("#0057717A"));
//            headFAr.setColorFilter(Color.parseColor("#0057717A"));
//            basinFAr.setColorFilter(Color.parseColor("#0057717A"));
//            handLeftFAr.setColorFilter(Color.parseColor("#0057717A"));
//            handRightFAr.setColorFilter(Color.parseColor("#0057717A"));
//            LegsFAr.setColorFilter(Color.parseColor("#0057717A"));
//            bodyFAv.setColorFilter(Color.parseColor("#0057717A"));
//            headFAv.setColorFilter(Color.parseColor("#0057717A"));
//            basinFAv.setColorFilter(Color.parseColor("#0057717A"));
//            handLeftFAv.setColorFilter(Color.parseColor("#0057717A"));
//            handRightFAv.setColorFilter(Color.parseColor("#0057717A"));
//            LegsFAv.setColorFilter(Color.parseColor("#0057717A"));

        }*/
        }
        if (requestCode == REQUEST_CODE_2) {
            clean();
        }

        }
    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext().getApplicationContext(),contentUri,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToNext();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
        public void toastMessage(String message){
            Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
        }
    }

