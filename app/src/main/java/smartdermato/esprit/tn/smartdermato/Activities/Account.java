package smartdermato.esprit.tn.smartdermato.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;

import com.airbnb.lottie.LottieAnimationView;
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
import com.hbb20.CountryCodePicker;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.Entities.Rating;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.AESCrypt;
import smartdermato.esprit.tn.smartdermato.Util.util;
import smartdermato.esprit.tn.smartdermato.databinding.AccountBinding;

import static android.app.Activity.RESULT_OK;

public class Account  extends Fragment implements OnCountryPickerListener {
    private Dialog myDialog,myDialog2;
    private ImageView personalInformation,accountInformation,accountInformationclose,editEmail,editPassword,professionalInformation;
    private CircleImageView user_pic;
    private final  int IMG_RESULT= 1;
    private AccountBinding mBinding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView lastName,firstName,birthday,sexe,phone,username,email,etat,OFN,OFA,CT,CNT,CP,CD;
    private RelativeLayout certification,pfi;
    private LinearLayout detaisCertification,pfd,bar_rating;
    private String imagePath;
    private ImageView imageCountry;
    private String imageName = "vide_pic";
    private Users users;
    private ProgressDialog prgDialog,mDialog;
    private CountryPicker countryPicker;
    private FragmentActivity myContext;
    private RatingBar ratingBar;
    private List<Rating> ratingList;
    private int coff=0;
    private float somme = 0.0f;
    private TextView note,np;
    private RelativeLayout annimationR,allAcount;
    private LottieAnimationView annimation,OK,Failed;
    private static final String TAG = "Account";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.account, container, false);
        verifyStoragePermitions(getActivity());
        Context context = getActivity().getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        preferences = getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);
        editor = preferences.edit();

        bar_rating = root.findViewById(R.id.rellay2);
        annimationR = root.findViewById(R.id.annimation_profile);
        annimation = root.findViewById(R.id.animation_view_res);
        OK = root.findViewById(R.id.animation_view_ok);
        Failed = root.findViewById(R.id.animation_view_res_failed);
        allAcount = root.findViewById(R.id.account_info);

        if(preferences.getString(getString(R.string.role),"").equals("medecin"))
        {
            ratingList = new ArrayList<>();
            getRating(root);
            System.out.println(ratingList);

        }
        else
        {
            bar_rating.setVisibility(View.GONE);
        }


        personalInformation = root.findViewById(R.id.personal_information);
        professionalInformation = root.findViewById(R.id.professional_information);
        accountInformation = root.findViewById(R.id.account_information);
        accountInformationclose = root.findViewById(R.id.account_information_close);
        editEmail = root.findViewById(R.id.edit_email);
        editPassword = root.findViewById(R.id.edit_password);
        user_pic = root.findViewById(R.id.imgView_proPic);
        lastName = root.findViewById(R.id.last_name);
        firstName = root.findViewById(R.id.first_name);
        birthday = root.findViewById(R.id.birthday);
        sexe = root.findViewById(R.id.sexe);
        phone = root.findViewById(R.id.phone);
        username = root.findViewById(R.id.username);
        email = root.findViewById(R.id.email);
        certification = root.findViewById(R.id.cer);
        pfi = root.findViewById(R.id.pfi);
        detaisCertification = root.findViewById(R.id.det_cer);
        pfd = root.findViewById(R.id.pfid);
        etat = root.findViewById(R.id.etat);
        OFN = root.findViewById(R.id.OFN);
        OFA = root.findViewById(R.id.OFA);
        CNT = root.findViewById(R.id.Country);
        CT = root.findViewById(R.id.City);
        CP = root.findViewById(R.id.CP);
        CD = root.findViewById(R.id.date_certification);
        np = root.findViewById(R.id.np);
        System.out.println("Date Of Birthday: "+ preferences.getString(getString(R.string.DateOfBirth),""));
        String LastNameSP = preferences.getString(getString(R.string.LastName),"");
        String FirstName = preferences.getString(getString(R.string.FirstName),"");
        String Sexe = preferences.getString(getString(R.string.Sexe),"");
        String Phone = preferences.getString(getString(R.string.PhoneNumber),"");
        String Username = preferences.getString(getString(R.string.username),"");
        String Email = preferences.getString(getString(R.string.email),"");
        String Role = preferences.getString(getString(R.string.role),"");
        String Birthday = preferences.getString(getString(R.string.DateOfBirth),"");
        String Country = preferences.getString(getString(R.string.Country),"");
        String City = preferences.getString(getString(R.string.City),"");
        String CPT = preferences.getString(getString(R.string.PostalCode),"");
        String OfficeAdress = preferences.getString(getString(R.string.OfficeAddess),"");
        String OfficePhoneN = preferences.getString(getString(R.string.OfficeNumber),"");
        String CertificatDate = preferences.getString(getString(R.string.CertificatDate),"");
        users = new Users();
        users.setId(preferences.getInt(getString(R.string.Id),0));
        users.setUsername(Username);
        users.setEmail(Email);
        users.setPassword(preferences.getString(getString(R.string.password),""));
        users.setLastName(LastNameSP);
        users.setFirstName(FirstName);
        users.setSexe(Sexe);
        users.setPhoneNumber(Phone);
        users.setRole(Role);
        users.setDateOfBirth(Birthday);
        users.setCountry(Country);
        users.setCity(City);
        users.setPostalCode(CPT);
        users.setOfficeAddess(OfficeAdress);
        users.setOfficeNumber(OfficePhoneN);
        users.setCertificatDate(CertificatDate);
        users.setUser_pic(preferences.getString(getString(R.string.user_pic),""));
        users.setCertification(preferences.getString(getString(R.string.certification),""));
        users.setEtatCertification(preferences.getInt(getString(R.string.EtatCertification),0));
        users.setCountryOfficeNumber(preferences.getString(getString(R.string.CountryOfficeNumber),""));
        users.setCountryPhoneNumber(preferences.getString(getString(R.string.CountryPhoneNumber),""));
        System.out.println( LastNameSP+" "+FirstName+" "+Sexe+" "+Phone+" "+Username+" "+Email+" "+Role+" "+Birthday+" "+Country+" "+City+" "+CPT+" "+OfficeAdress+" "+OfficePhoneN+" "+CertificatDate);
//        if(LastNameSP.equals(""))
//        {
//            lastName.setText("");
//        }
//        else
//        {
        if(!users.getUser_pic().equals("vide_pic"))
        {

            Picasso.with(getActivity())
                    .load(util.getDomaneName()+"/Content/Images/"+users.getUser_pic()).into(user_pic);


        }
        if(users.getFirstName().equals("") && users.getLastName().equals("") && users.getRole().equals("patient") )
        {
            np.setText(users.getUsername());
        }
        else if(users.getFirstName().equals("") && users.getLastName().equals("") && users.getRole().equals("medecin") )
        {
            np.setText("Dr. "+users.getUsername());

        }
        else  if(!users.getFirstName().equals("") && !users.getLastName().equals("") && users.getRole().equals("patient") )
        {
            np.setText(users.getFirstName()+" "+users.getLastName());

        }
        else  if(!users.getFirstName().equals("") && !users.getLastName().equals("") && users.getRole().equals("medecin") )
        {
            np.setText("Dr. "+users.getFirstName()+" "+users.getLastName());

        }


        lastName.setText(LastNameSP);
//        }
//        if(FirstName.equals("null"))
//        {
//            firstName.setText("");
//        }
//        else
//        {
        firstName.setText(FirstName);
//        }
//        if(Sexe.equals("null"))
//        {
//            sexe.setText("");
//
//        }
//        else
//        {
        sexe.setText(Sexe);

//        }
//        if(Phone.equals("null"))
//        {
//            phone.setText("");
//
//        }
//        else
//        {
        phone.setText("("+users.getCountryPhoneNumber()+")"+Phone);

//        }
//
//        if(Birthday.equals("null"))
//        {
//            birthday.setText("");
//
//        }
//        else
//        {
        birthday.setText(Birthday);

//        }
//        if(CertificatDate.equals("null"))
//        {
//            CD.setText("");
//
//        }
//        else
//        {

//        }


        username.setText(Username);
        email.setText(Email);
        System.out.println("date of birthday"+preferences.getString(getString(R.string.dateOfBirth),""));
        System.out.println("rolee "+preferences.getString(getString(R.string.role),""));

        if(preferences.getString(getString(R.string.role),"").equals("medecin")){
            certification.setVisibility(View.VISIBLE);
            detaisCertification.setVisibility(View.VISIBLE);
            pfi.setVisibility(View.VISIBLE);
            pfd.setVisibility(View.VISIBLE);
//            if(OfficeAdress.equals("null"))
//            {
//                OFA.setText("");
//
//            }
//            else
//            {
            OFA.setText(OfficeAdress);

//            }
//            if(OfficePhoneN.equals("null"))
//            {
//                OFN.setText("");
//
//            }
//            else
//            {
            OFN.setText("("+users.getCountryOfficeNumber()+")"+OfficePhoneN);

//            }
//            if(Country.equals("null"))
//            {
//                CNT.setText("");
//
//            }
//            else
//            {
            CNT.setText(Country);

//            }
//            if(City.equals("null"))
//            {
//                CT.setText("");
//
//            }
//            else
//            {
            CT.setText(City);

//            }
//
//            if(CPT.equals("null"))
//            {
//                CP.setText("");
//
//            }
//            else
//            {
            CP.setText(CPT);
            CD.setText(CertificatDate);

//            }
            if(preferences.getInt(getString(R.string.EtatCertification),0)==0){
                etat.setText("Any");
                etat.setTextColor(Color.RED);
            }
        }
        personalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupPersonalInformation(users);
            }
        });
        accountInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupAccountInformation();
            }
        });
        accountInformationclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupAccountInformationClose();
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupEditEmail(users);
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ShowPopupEditPassword(users);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        professionalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupProfitionalInformation(users);
            }
        });
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                Intent result = Intent.createChooser(intent,getText(R.string.choose_file));
                startActivityForResult(result,IMG_RESULT);
            }
        });

        return root;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prgDialog.incrementProgressBy(1);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_RESULT)
        {
            if (resultCode == RESULT_OK)
            {
                allAcount.setVisibility(View.GONE);
                annimationR.setVisibility(View.VISIBLE);
                annimation.setVisibility(View.VISIBLE);
                annimation.loop(true);
                annimation.playAnimation();
                Uri uri = data.getData();

                toastMessage(uri.toString());
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
                    UploadService uploadService = APIClient.getClient().create(UploadService.class);



                  /*  mDialog = new ProgressDialog(getActivity());
                    mDialog.setMessage("Please Wait...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
*/

                    uploadService.Upload(photo,description).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                users.setUser_pic(imageName);
                                putUser(users,"upload");
                               /* mDialog.dismiss();
                                toastMessage("Success");
                                prgDialog.setMax(100);
                                prgDialog.setTitle("Upload Image");
                                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                prgDialog.show();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (prgDialog.getProgress()<= prgDialog.getMax())
                                        {
                                            try {
                                                Thread.sleep(200);
                                                handler.sendMessage(handler.obtainMessage());
                                                if (prgDialog.getProgress() == prgDialog.getMax()){

                                                    //NotificationUpload();

                                                    users.setUser_pic(imageName);
                                                    putUser(users,"upload");
                                                    prgDialog.dismiss();
                                                    break;


                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }).start();*/


                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("erreur...........");
                            toastMessage(t.getMessage());
                            // mDialog.dismiss();
                            // prgDialog.dismiss();
                            annimation.pauseAnimation();
                            annimation.setVisibility(View.GONE);
                            Failed.setVisibility(View.VISIBLE);
                            // Failed.loop(true);
                            Failed.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Failed.pauseAnimation();
                                    Failed.setVisibility(View.GONE);
                                    annimationR.setVisibility(View.GONE);
                                    allAcount.setVisibility(View.VISIBLE);


                                }
                            }, 2000);


                        }
                    });


                }catch (Exception e){
                    // mDialog.dismiss();
                    toastMessage("erreur 1....");
                    annimation.pauseAnimation();
                    annimation.setVisibility(View.GONE);
                    Failed.setVisibility(View.VISIBLE);
                    // Failed.loop(true);
                    Failed.playAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Failed.pauseAnimation();
                            Failed.setVisibility(View.GONE);
                            annimationR.setVisibility(View.GONE);
                            allAcount.setVisibility(View.VISIBLE);


                        }
                    }, 2000);


                }

            }
        }
    }

    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),contentUri,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToNext();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private static void  verifyStoragePermitions(Activity activity)
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
    public void ShowPopupPersonalInformation(final Users users) {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.personal_information);
        final EditText fN,lN,b,ph;
        final Spinner s;
        FrameLayout save;
        CountryCodePicker ccp;

        fN = myDialog.findViewById(R.id.f_name);
        lN = myDialog.findViewById(R.id.l_name);
        b = myDialog.findViewById(R.id.birthday);
        s = myDialog.findViewById(R.id.sex);
        ph = myDialog.findViewById(R.id.ph_number);
        save = myDialog.findViewById(R.id.saveInBtn);
        ccp = myDialog.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(ph);
        List<String> list = new ArrayList<>();
        list.add("Sexe");
        list.add("Male");
        list.add("Women");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
//        if(users.getFirstName().equals("null"))
//        {
//            fN.setText("");
//        }
//        else
//        {
        fN.setText(users.getFirstName());

//        }
//        if(users.getLastName().equals("null"))
//        {
//            lN.setText("");
//        }
//        else
//        {
        lN.setText(users.getLastName());
//        }
//        if(users.getDateOfBirth().equals("null"))
//        {
//            b.setText("");
//        }
//        else
//        {
        b.setText(users.getDateOfBirth());
//        }
//        if(users.getPhoneNumber().equals("null"))
//        {
//            ph.setText("");
//        }
//        else
//        {
        toastMessage(users.getCountryPhoneNumber()+users.getPhoneNumber());
        // ccp.setDefaultCountryUsingPhoneCode(Integer.valueOf(users.getCountryPhoneNumber().substring(1,users.getCountryPhoneNumber().length())));
        ccp.setFullNumber(users.getCountryPhoneNumber()+users.getPhoneNumber());

        // ph.setText(users.getPhoneNumber());
//        }


        if(users.getSexe().equals(""))
        {
            s.setSelection(0);
        }
        else if(users.getSexe().equals("Male"))
        {
            s.setSelection(1);
        }
        else
        {
            s.setSelection(2);

        }



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2 = new Dialog(getActivity());
                myDialog2.setContentView(R.layout.pop_date_piker);
                final DatePicker datePicker = (DatePicker) myDialog2.findViewById(R.id.Datepick);

                if(!users.getDateOfBirth().equals("null"))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    Date date = null;
                    try {
                        date = formatter.parse(users.getDateOfBirth());
                        System.out.println(date.getDate()+"   "+date.getMonth()+"   "+date.getDay());

                        // datePicker.updateDate(2018,12,30);
                        int year =Integer.valueOf(formatter.format(date).substring(6,10));
                        System.out.println(year);
                        int day = Integer.valueOf(formatter.format(date).substring(0,2));
                        int month = Integer.valueOf(formatter.format(date).substring(3,5));

                        System.out.println(day);

                        System.out.println(month);
                        if(month == 1)
                        {
                            month = 12;
                            year = year-1 ;
                        }
                        else
                        {

                            month= month-1;
                        }


                        System.out.println(month+"  -----------");


                        datePicker.updateDate(year,month,day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                final Button button = (Button) myDialog2.findViewById(R.id.set);

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
                            b.setText(date);
                            myDialog2.dismiss();


                        }



                        //Toast.makeText(getActivity(), getDateFromDatePicker(datePicker).toString(), Toast.LENGTH_LONG).show();

                        //  Toast.makeText(getActivity(), datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear(),Toast.LENGTH_LONG).show();
                    }
                });
                myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog2.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage(String.valueOf(ccp.isValidFullNumber()));
                if(fN.getText().length() == 0 && lN.getText().length() == 0 && b.getText().length() == 0 && s.getSelectedItem().equals("Sexe") )
                {
                    toastMessage("Check your field please");
                }
                else if (ph.getText().length()!= 0)

                {
                    if( ! ccp.isValidFullNumber())
                    {
                        toastMessage("Check your Phone Number please");

                    }
                    else
                    {
                        users.setFirstName(fN.getText().toString());
                        users.setLastName(lN.getText().toString());
                        users.setDateOfBirth(b.getText().toString());
                        if(!s.getSelectedItem().equals("Sexe"))
                        {
                            users.setSexe(s.getSelectedItem().toString());

                        }
                        users.setCountryPhoneNumber(ccp.getSelectedCountryCodeWithPlus());
                        users.setPhoneNumber(ph.getText().toString());

                        putUser(users,"popup");

                    }
                }
                else
                {
                    users.setFirstName(fN.getText().toString());
                    users.setLastName(lN.getText().toString());
                    users.setDateOfBirth(b.getText().toString());
                    if(!s.getSelectedItem().equals("Sexe"))
                    {
                        users.setSexe(s.getSelectedItem().toString());

                    }
                    users.setCountryPhoneNumber(ccp.getSelectedCountryCodeWithPlus());
                    users.setPhoneNumber(ph.getText().toString());

                    putUser(users,"popup");

                }

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlider;
        myDialog.getWindow().setAttributes(lp);
        myDialog.show();
    }
    public void ShowPopupAccountInformation(){
        editEmail.setVisibility(View.VISIBLE);
        editPassword.setVisibility(View.VISIBLE);
        accountInformation.setVisibility(View.GONE);
        accountInformationclose.setVisibility(View.VISIBLE);
    }
    public void ShowPopupAccountInformationClose(){
        editEmail.setVisibility(View.GONE);
        editPassword.setVisibility(View.GONE);
        accountInformation.setVisibility(View.VISIBLE);
        accountInformationclose.setVisibility(View.GONE);
    }
    public void ShowPopupEditEmail(final Users users) {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.pop_email);
        final EditText NE = myDialog.findViewById(R.id.new_mail);
        NE.setText(users.getEmail());
        FrameLayout save = myDialog.findViewById(R.id.saveInBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(users);
                System.out.println("/"+NE.getText()+"/        /"+users.getEmail()+"/");
                System.out.println(!NE.getText().toString().equals(users.getEmail()));

                if(!NE.getText().toString().equals(users.getEmail()))
                {
                    final String URL = util.getDomaneName() + "/getWithEmail/"+NE.getText().toString()+"/";
                    System.out.println("update email:  "+ URL);

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println(response+" respoce!!!!++++");

                                    try {
                                        toastMessage("This Email is already used!");


                                        //  System.out.println(o.toString()+" respoce!!!!++++");






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
                            users.setEmail(NE.getText().toString());
                            putUser(users,"popup");

                            return;

                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(stringRequest);
                }
                else
                {
                    toastMessage("Check you Email!");
                }
            }
        });



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlider;
        myDialog.getWindow().setAttributes(lp);
        myDialog.show();
    }
    public void ShowPopupEditPassword(final Users users) throws Exception {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.pop_password);
        final EditText AP,NP,CP;
        FrameLayout save;
        AP = (EditText) myDialog.findViewById(R.id.ancienne_pw);
        NP = myDialog.findViewById(R.id.nouveau_pw);
        CP  =myDialog.findViewById(R.id.confirmer_pw);
        save = myDialog.findViewById(R.id.saveInBtn);
        final String originalPassword = AESCrypt.decrypt(users.getPassword());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(originalPassword+"/"+AP.getText().toString()+"/"+NP.getText().toString()+"/"+CP.getText().toString()+"/");

                if(AP.getText().toString().equals(originalPassword) && NP.getText().toString().equals(CP.getText().toString()))
                {
                    try {
                        users.setPassword(AESCrypt.encrypt(NP.getText().toString()));
                        putUser(users,"popup");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    toastMessage("Check your password");

                }
            }
        });



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlider;
        myDialog.getWindow().setAttributes(lp);
        myDialog.show();
    }


    public void ShowPopupProfitionalInformation(final Users users) {
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.profissional_information);
        final EditText OA,C,OPH,CP;
        ImageView pays;
        final Spinner s;
        final FrameLayout save;
        CountryCodePicker ccpO;
        OA = myDialog.findViewById(R.id.oa);
        C = myDialog.findViewById(R.id.city);
        CT = myDialog.findViewById(R.id.Country);
        CP = myDialog.findViewById(R.id.cp);
        OPH =(EditText) myDialog.findViewById(R.id.ph_o_number);
        imageCountry = myDialog.findViewById(R.id.image_country);
        save = myDialog.findViewById(R.id.saveInBtn);
        ccpO = myDialog.findViewById(R.id.ccp_o);
        ccpO.registerCarrierNumberEditText(OPH);
//        if(users.getFirstName().equals("null"))
//        {
//            fN.setText("");
//        }
//        else
//        {
        OA.setText(users.getOfficeAddess());

//        }
//        if(users.getLastName().equals("null"))
//        {
//            lN.setText("");
//        }
//        else
//        {
        C.setText(users.getCity());
//        }
//        if(users.getDateOfBirth().equals("null"))
//        {
//            b.setText("");
//        }
//        else
//        {
        countryPicker = new CountryPicker.Builder().with(getActivity()).listener(this).build();

        CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(myContext.getSupportFragmentManager());

            }
        });
        CT.setText(users.getCountry());
//        }
//        if(users.getPhoneNumber().equals("null"))
//        {
//            ph.setText("");
//        }
//        else
//        {
        // ccpO.setDefaultCountryUsingPhoneCode(Integer.valueOf(users.getCountryOfficeNumber().substring(1,users.getCountryOfficeNumber().length())));
        ccpO.setFullNumber(users.getCountryOfficeNumber()+users.getCountryOfficeNumber());

        //   OPH.setText(users.getOfficeNumber());
        CP.setText(users.getPostalCode());
//        }







        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OA.getText().length() == 0 || C.getText().length() == 0 || CP.getText().length() == 0 || CT.getText().length() == 0 || ! ccpO.isValidFullNumber())
                {
                    toastMessage("Check your field please");
                }
                else
                {
                    users.setOfficeAddess(OA.getText().toString());
                    users.setCountryOfficeNumber(ccpO.getSelectedCountryCodeWithPlus());
                    users.setOfficeNumber(OPH.getText().toString());
                    users.setCountry(CT.getText().toString());
                    users.setPostalCode(CP.getText().toString());
                    users.setCity(C.getText().toString());

                    putUser(users,"popup");

                }

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlider;
        myDialog.getWindow().setAttributes(lp);
        myDialog.show();
    }
    public void putUser(final Users users,String type)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String URI = util.getDomaneName()+"/api/Users/"+users.getId();
        Map<String,Object> paramsUsers = new HashMap<String, Object>();
        paramsUsers.put("Id",users.getId());
        paramsUsers.put("FirstName",users.getFirstName());
        paramsUsers.put("LastName",users.getLastName());
        paramsUsers.put("Email",users.getEmail());
        paramsUsers.put("Username",users.getUsername());
        paramsUsers.put("DateOfBirth",users.getDateOfBirth());
        paramsUsers.put("CountryPhoneNumber",users.getCountryPhoneNumber());
        paramsUsers.put("CountryOfficeNumber",users.getCountryOfficeNumber());

        paramsUsers.put("Sexe",users.getSexe());
        paramsUsers.put("PhoneNumber",users.getPhoneNumber());
        paramsUsers.put("Password",users.getPassword());
        paramsUsers.put("ProfilePic",users.getUser_pic());
        paramsUsers.put("role",users.getRole());
        paramsUsers.put("certification",users.getCertification());
        paramsUsers.put("EtatCertification",users.getEtatCertification());
        paramsUsers.put("City",users.getCity());
        paramsUsers.put("Country",users.getCountry());
        paramsUsers.put("Status",users.getStatus());
        paramsUsers.put("OfficeNumber",users.getOfficeNumber());
        paramsUsers.put("OfficeAdress",users.getOfficeAddess());
        paramsUsers.put("PostalCode",users.getPostalCode());
        paramsUsers.put("Certificatdate",users.getCertificatDate());
        System.out.println("PUT Users: "+new JSONObject (paramsUsers));

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URI, new JSONObject(paramsUsers),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");
                        try {
                            JSONObject o = response;

                            Users Users = new Users();

                            Users.setId(o.getInt("id"));
                            Users.setUsername(o.getString("username"));
                            Users.setPassword(o.getString("password"));
                            Users.setEmail(o.getString("email"));
                            Users.setUser_pic(o.getString("profilePic"));
                            Users.setRole(o.getString("role"));
                            Users.setFirstName(o.getString("firstName"));
                            Users.setLastName(o.getString("lastName"));
                            Users.setSexe(o.getString("sexe"));
                            Users.setPhoneNumber(o.getString("phoneNumber"));
                            Users.setCertification(o.getString("certification"));
                            Users.setEtatCertification(o.getInt("etatCertification"));
                            Users.setDateOfBirth(o.getString("dateOfBirth"));
                            Users.setCountry(o.getString("country"));
                            Users.setCity(o.getString("city"));
                            Users.setPostalCode(o.getString("postalCode"));
                            Users.setOfficeNumber(o.getString("officeNumber"));
                            Users.setOfficeAddess(o.getString("officeAdress"));
                            Users.setCertificatDate(o.getString("certificatdate"));
                            Users.setCountryOfficeNumber(o.getString("countryOfficeNumber"));
                            Users.setCountryPhoneNumber(o.getString("countryPhoneNumber"));




                            editor.putString(getString(R.string.username), Users.getUsername());
                            editor.putString(getString(R.string.role), Users.getRole());
                            editor.putString(getString(R.string.password), Users.getPassword());
                            editor.putString(getString(R.string.email), Users.getEmail());
                            editor.putInt(getString(R.string.Id), Users.getId());
                            editor.putString(getString(R.string.user_pic), Users.getUser_pic());
                            editor.putString(getString(R.string.LastName), Users.getLastName());
                            editor.putString(getString(R.string.FirstName), Users.getFirstName());
                            editor.putString(getString(R.string.DateOfBirth), Users.getDateOfBirth());
                            editor.putString(getString(R.string.Sexe), Users.getSexe());
                            editor.putString(getString(R.string.certification), Users.getCertification());
                            editor.putString(getString(R.string.PhoneNumber), Users.getPhoneNumber());
                            editor.putInt(getString(R.string.EtatCertification), Users.getEtatCertification());
                            editor.putString(getString(R.string.City), Users.getCity());
                            editor.putString(getString(R.string.Country), Users.getCountry());
                            editor.putString(getString(R.string.OfficeAddess), Users.getOfficeAddess());
                            editor.putString(getString(R.string.OfficeNumber), Users.getOfficeNumber());
                            editor.putString(getString(R.string.PostalCode), Users.getPostalCode());
                            editor.putString(getString(R.string.CertificatDate),Users.getCertificatDate());
                            editor.putString(getString(R.string.CountryOfficeNumber),users.getCountryOfficeNumber());
                            editor.putString(getString(R.string.CountryPhoneNumber),users.getCountryPhoneNumber());
                            editor.commit();


                            setData(Users,type);


                            if(type.equals("popup"))
                                myDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        annimation.pauseAnimation();
                        annimation.setVisibility(View.GONE);
                        Failed.setVisibility(View.VISIBLE);
                        // Failed.loop(true);
                        Failed.playAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Failed.pauseAnimation();
                                Failed.setVisibility(View.GONE);
                                annimationR.setVisibility(View.GONE);
                                allAcount.setVisibility(View.VISIBLE);


                            }
                        }, 2000);

                        Log.d(TAG,"Error: "+error +"\nmessage"+error.getMessage());
                        Toast.makeText(getActivity(), " Erreur Services",
                                Toast.LENGTH_SHORT).show();
                        return ;

                    }
                });





        queue.add(stringRequest);
    }
    private void setData(Users users ,String type) {
//        if (users.getLastName().equals("null")) {
//            lastName.setText("");
//        } else {
        lastName.setText(users.getLastName());
//        }
//        if (users.getFirstName().equals("null")) {
//            firstName.setText("");
//        } else {
        firstName.setText(users.getFirstName());
//        }
//        if (users.getSexe().equals("null")) {
//            sexe.setText("");
//
//        } else {
        sexe.setText(users.getSexe());

//        }
//        if (users.getPhoneNumber().equals("null")) {
//            phone.setText("");
//
//        } else {
        phone.setText("("+users.getCountryPhoneNumber()+")"+users.getPhoneNumber());

//        }
//
//        if (users.getDateOfBirth().equals("null")) {
//            birthday.setText("");
//
//        } else {
        birthday.setText(users.getDateOfBirth());

//        }
        if(!users.getUser_pic().equals("vide_pic"))
        {

            Picasso.with(getActivity())
                    .load(util.getDomaneName()+"/Content/Images/"+users.getUser_pic()).into(user_pic);


        }

        if (preferences.getString(getString(R.string.role), "").equals("medecin")) {
            certification.setVisibility(View.VISIBLE);
            detaisCertification.setVisibility(View.VISIBLE);
            pfi.setVisibility(View.VISIBLE);
            pfd.setVisibility(View.VISIBLE);
//            if (users.getOfficeAddess().equals("null")) {
//                OFA.setText("");
//
//            } else {
            OFA.setText(users.getOfficeAddess());

//            }
//            if (users.getPhoneNumber().equals("null")) {
//                OFN.setText("");
//
//            } else {
            OFN.setText("("+users.getCountryOfficeNumber()+")"+users.getOfficeNumber());

//            }
//            if (users.getCountry().equals("null")) {
//                CNT.setText("");
//
//            } else {
            CNT.setText(users.getCountry());

//            }
//            if (users.getCity().equals("null")) {
//                CT.setText("");
//
//            } else {
            CT.setText(users.getCity());

//            }
//
//            if (users.getPostalCode().equals("null")) {
//                CP.setText("");
//
//            } else {
            CP.setText(users.getPostalCode());

//            }
//            if(users.getCertificatDate().equals("null"))
//            {
//                CD.setText("");
//
//            }
//            else
//            {
            CD.setText(users.getCertificatDate());

//            }
        }



        username.setText(users.getUsername());
        email.setText(users.getEmail());
        if(type.equals("upload")){
            annimation.pauseAnimation();
            annimation.setVisibility(View.GONE);
            OK.setVisibility(View.VISIBLE);
            //  OK.loop(true);
            OK.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OK.pauseAnimation();
                    OK.setVisibility(View.GONE);
                    annimationR.setVisibility(View.GONE);
                    allAcount.setVisibility(View.VISIBLE);


                }
            }, 2000);

        }
    }
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    String NOTIFICATION_CHANNEL_ID2 = "EDMTDev";
    NotificationManager notificationManager;

    public void NotificationUpload ()
    {

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        final  int progressMax = 100;
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getActivity(),NOTIFICATION_CHANNEL_ID2)
                .setSmallIcon(R.drawable.smart_demato_logo_mini)
                .setContentTitle("Upload Image")
                .setContentText("Upload in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(progressMax,0,false);
        notificationManager.notify(2,notification.build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                for (int progress =0 ;progress<= progressMax;progress +=10)
                {
                    notification.setProgress(progressMax,progress,false);
                    notificationManager.notify(2,notification.build());
                    SystemClock.sleep(1000);
                }
                notification.setContentText("Upload finished")
                        .setProgress(0,0,false)
                        .setOngoing(false);
                notificationManager.notify(2,notification.build());
                prgDialog.dismiss();
            }
        }).start();


    }
    public void toastMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectCountry(Country country) {
        imageCountry.setImageResource(country.getFlag());
        imageCountry.setVisibility(View.VISIBLE);
        CT.setText(country.getName());

    }
    public void getRating(View root)
    {
        final String URL = util.getDomaneName() + "/api/Ratings/";
        System.out.println("URLLLL:  "+ URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

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
                                if(rating.getMedcinId() == preferences.getInt(getString(R.string.Id),0)){
                                    ratingList.add(rating);
                                    somme = somme + rating.getNote();
                                    coff++;
                                }

                            }

                            toastMessage(String.valueOf(somme)+"   "+String.valueOf(coff));
                            note = root.findViewById(R.id.note);

                            prgDialog = new ProgressDialog(getActivity());
                            ratingBar = root.findViewById(R.id.rating);
                            if(somme == 0.0f && coff == 0)
                            {
                                note.setText(String.valueOf(0.0f));
                                ratingBar.setRating(0.0F);
                            }
                            else
                            {
                                note.setText(String.valueOf(somme/coff));
                                ratingBar.setRating(somme/coff);
                            }
                            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                            stars.getDrawable(2).setColorFilter(Color.parseColor("#F0AF10"), PorterDuff.Mode.SRC_ATOP);

                            ratingBar.setEnabled(false);
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    toastMessage(String.valueOf(rating));

                                }
                            });

//                            System.out.println("database get: "+database.getAllUsers());




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
}
