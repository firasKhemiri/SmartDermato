package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import smartdermato.esprit.tn.smartdermato.ImageFilters.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class AddPost extends AppCompatActivity {
    private SharedPreferences preferences;

    public static final String TAG = "Upload Image";

    private int PICK_IMAGE_REQUEST = 100;

    private EditText name_post;
    private final  int IMG_RESULT= 1;
    private String dateP;
    private String nom;

    private String host;
    private Bitmap decoded;
    private Window window;
    private String imageName = "vide_pic";
    private String imagePath;
    private Map<String, Object> params = new HashMap<String, Object>();



    private LottieAnimationView animationViewRes,animationView,animationViewResult,animationViewResFailed;
    private RelativeLayout firstAnnimation,result,all;
    private LinearLayout r;
    private boolean isPicture;

    public static final int POST_CAT_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_publication);
        window=getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        animationView = findViewById(R.id.animation_view);
        animationViewRes = findViewById(R.id.animation_view_res);
        animationViewResult = findViewById(R.id.animation_view_result);
        animationViewResFailed = findViewById(R.id.animation_view_res_failed);
        firstAnnimation = findViewById(R.id.first_annimation);
        all = findViewById(R.id.alla);
        r = findViewById(R.id.r);
        result = findViewById(R.id.result);



        ImageView buttonLoadImage = findViewById(R.id.pic_change);
        Button valider = findViewById(R.id.valider);
        name_post = findViewById(R.id.nam_post);



        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFileChooser();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nom = String.valueOf(name_post.getText());
                if (isPicture)
                {
                    UploadImage();
                    //AjoutPub(decoded, nom, new Date());

                   /* Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    Intent result = Intent.createChooser(intent,getText(R.string.choose_file));
                    startActivityForResult(result,IMG_RESULT);*/
                }
                else
                {
                 //  AjoutPubNoPic(nom, new Date());
                }
            }
        });
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_RESULT)
        {
            if (resultCode == RESULT_OK)
            {


            }
        }

        if (requestCode == POST_CAT_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                // here the part where I get my selected date from the saved variable in the intent and the displaying it.
                Bundle bundle = data.getExtras();
                assert bundle != null;
                String name = bundle.getString("cat_name", "error");
                String pic = bundle.getString("cat_img", "error");
                int id = bundle.getInt("cat_id", 0);


                //   Toast.makeText(view.getContext(), " " + post_name, Toast.LENGTH_LONG).show();
            }
        }



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ImageView imageView = findViewById(R.id.photup);
            Uri uri = data.getData();
            //toastMessage(imageUri.toString());
            imagePath = getRealPathFromURI(uri);
            Uri fileUri = data.getData();
            String selectedFilePath = getPath(fileUri);
            Log.i(TAG, " File path : " + selectedFilePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), fileUri);


                ByteArrayOutputStream out = new ByteArrayOutputStream();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(fileUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(filePath);
                long taille = file.length()/1024;


                //  int taille = bitmap.getByteCount()/1024;
                // int taille = (bitmap.getRowBytes() * bitmap.getHeight())/1024;
//                Toast.makeText(view.getContext()," "+taille,Toast.LENGTH_LONG).show();


                if(taille>=7000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                  Toast.makeText(view.getContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=5800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //               Toast.makeText(view.getContext()," 1 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=4800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                Toast.makeText(view.getContext()," 2 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=3800) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                 Toast.makeText(view.getContext()," 3 "+taille,Toast.LENGTH_LONG).show();
                }


                else if(taille>=3000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 55, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                    Toast.makeText(view.getContext()," 4 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=2000) {

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                 Toast.makeText(view.getContext()," 5 "+taille,Toast.LENGTH_LONG).show();

                }


                else if(taille>=1000) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                    Toast.makeText(view.getContext()," 6 "+taille,Toast.LENGTH_LONG).show();

                }

                else if(taille>=500) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                     Toast.makeText(view.getContext()," 7 "+taille,Toast.LENGTH_LONG).show();
                }

                else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    imageView.setImageBitmap(decoded);
                    //                 Toast.makeText(view.getContext()," 5 "+taille,Toast.LENGTH_LONG).show();
                }

                imageView.setVisibility(View.VISIBLE);
                isPicture = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(
                getApplicationContext(),contentUri,proj,null,null,null);
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
    public void UploadImage()
    {
        r.setVisibility(View.GONE);
        firstAnnimation.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
        window.setStatusBarColor(Color.parseColor("#17A8C2"));

        //  mBinding.animationView.setBackgroundColor(R.color.gradStart);
        //  mBinding.animationView.setBackgroundColor(R.color.gradStop);
        animationView.loop(true);
        animationView.playAnimation();

        System.out.println("imagePath " + imagePath);

        try {
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            imageName = "SmartDermatoIMG-" + n + ".png";
               /* Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                intent.putExtra("imagePath", imagePath);
                intent.putExtra("imageName", imageName);
                startActivityForResult(intent, REQUEST_CODE_2);*/
            File file = new File(imagePath);
            RequestBody photoContent = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContent);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), imageName);



            UploadService uploadService = APIClient.getClient().create(UploadService.class);





            uploadService.Upload(photo, description).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        animationView.pauseAnimation();
                        animationView.setVisibility(View.GONE);
                        animationViewRes.setVisibility(View.VISIBLE);
                        //mBinding.animationViewRes.loop(true);

                        animationViewRes.playAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //  animationViewRes.setVisibility(View.GONE);
                                firstAnnimation.setVisibility(View.GONE);
                                animationViewRes.setVisibility(View.GONE);

                                r.setVisibility(View.VISIBLE);
                                Post();
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





                            }
                        }, 2000);

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

        }

    }
    private void Post() {

        RequestQueue queue = Volley.newRequestQueue(AddPost.this);
        final String URI = util.getDomaneName()+"/api/Publications";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date  = new Date();
        dateP = dateFormat.format(date);

        System.out.println(dateP.substring(0,10));
        System.out.println(dateP.substring(11,16));
        params.put("Contenu",name_post.getText());
        params.put("DateCreate",dateP);
        params.put("Is_image",isPicture);
        params.put("Path_image",imageName);
        params.put("nom_user",preferences.getString(getString(R.string.username),""));
        params.put("user_image",preferences.getString(getString(R.string.user_pic),""));
        params.put("Createur",preferences.getInt(getString(R.string.Id),0));


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

                                    startActivity(new Intent(AddPost.this, smartdermato.esprit.tn.smartdermato.MainActivity.class));
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
                        Toast.makeText(AddPost.this, "Erreur Services",
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
                                startActivity(new Intent(AddPost.this, MenuActivity.class));




                            }
                        }, 2000);

                        return ;

                    }
                });





        queue.add(stringRequest);
    }


}
