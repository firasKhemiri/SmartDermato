package smartdermato.esprit.tn.smartdermato.ImageFilters;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Service.APIClient;
import smartdermato.esprit.tn.smartdermato.Service.UploadService;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class MainActivity extends AppCompatActivity implements ThumbnailCallback {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private Activity activity;
    private RecyclerView thumbListView;
    private ImageView placeHolderImageView;
    SeekBar seekbar, seekbar1;
    TextView textView, textView1;
  //  int drawable;

    Bitmap bitmap;
    Bitmap bitmapFinal;

    private Button submit;

    private String imagePath;
    private String imageName = "vide_pic";

    private final int IMG_RESULT = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_filter_activity_main);
        activity = this;
        camera();
    //    drawable = R.drawable.dog;
    }

    private void initUIWidgets() {
        submit = findViewById(R.id.btn_submit);
        textView = findViewById(R.id.label);
        textView1 = findViewById(R.id.label1);
        seekbar = findViewById(R.id.seekbar);
        seekbar1 = findViewById(R.id.seekbar1);
        thumbListView = findViewById(R.id.thumbnails);
        placeHolderImageView = findViewById(R.id.place_holder_imageview);
       // placeHolderImageView.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), drawable), 640, 640, false));
        placeHolderImageView.setImageBitmap(bitmap);
        initHorizontalList();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                placeHolderImageView.setImageBitmap(changeBitmapContrastBrightness(bitmap, (float) progress / 100f, 1));
                bitmapFinal = changeBitmapContrastBrightness(bitmap, (float) progress / 100f, 1);
                textView.setText("Contrast: " + (float) progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbar.setMax(200);
        seekbar.setProgress(100);


        seekbar1.setMax(200);
        seekbar1.setProgress(100);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                placeHolderImageView.setColorFilter(setBrightness(progress));
                textView1.setText("Brightness: " + (float) progress / 100f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        submit.setOnClickListener(v -> {

            Uri uri = getImageUri(getApplicationContext(),bitmapFinal);

            //toastMessage(imageUri.toString());
            imagePath = getRealPathFromURI(uri);
            System.out.println("imagePath " + imagePath);

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


                uploadService.Upload(photo, description).enqueue(new Callback<ResponseBody>() {
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
//                            animationView.pauseAnimation();
//                            animationView.setVisibility(View.GONE);
//                            animationViewResFailed.setVisibility(View.VISIBLE);
                        //mBinding.animationViewResFailed.loop(true);
//
//                            animationViewResFailed.playAnimation();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    firstAnnimation.setVisibility(View.GONE);
//                                    animationViewResFailed.pauseAnimation();
//                                    animationViewResFailed.setVisibility(View.GONE);
//                                    allCorp.setVisibility(View.VISIBLE);
//                                    clean();
//
//
//                                }
//                            }, 2000);


                    }
                });


            } catch (Exception e) {
                //mDialog.dismiss();
                toastMessage("erreur 1....");

            }

        });
    }

    public File bitmapToFile(Bitmap bmp) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 80, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = openFileOutput("mdroid.png", Context.MODE_WORLD_WRITEABLE);
            fos.write(bArr);
            fos.flush();
            fos.close();

            File mFile = new File(getFilesDir().getAbsolutePath(), "mdroid.png");
            return mFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                ThumbnailsManager.clearThumbs();
                List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

                for (Filter filter : filters) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = thumbImage;
                    thumbnailItem.filter = filter;
                    ThumbnailsManager.addThumb(thumbnailItem);
                }

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        placeHolderImageView.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(bitmap, 640, 640, false)));
        bitmapFinal = filter.processFilter(Bitmap.createScaledBitmap(bitmap, 640,640, false));
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (progress - 100) * 255 / 100;

            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);

        } else {
            int value = (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }


    public Uri imageUri;

    public void camera() {

     //   verifyStoragePermitions(());

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
        startActivityForResult(result, IMG_RESULT);

    }

//    private static void verifyStoragePermitions(Context activity) {
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSION_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//
//            );
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    initUIWidgets();

                } catch (Exception e) {
                    //mDialog.dismiss();
                    toastMessage("erreur 1....");

                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext().getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToNext();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
             /*   animationView.pauseAnimation();
                animationView.setVisibility(View.GONE);
                animationViewResFailed.setVisibility(View.VISIBLE);
                // mBinding.animationViewResFailed.loop(true);
                animationViewResFailed.playAnimation();*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* firstAnnimation.setVisibility(View.GONE);
                        animationViewResFailed.pauseAnimation();
                        animationViewResFailed.setVisibility(View.GONE);
                        allCorp.setVisibility(View.VISIBLE);
                        clean();*/



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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}





