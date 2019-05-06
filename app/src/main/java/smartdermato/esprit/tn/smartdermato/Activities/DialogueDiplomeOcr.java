package smartdermato.esprit.tn.smartdermato.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;

import smartdermato.esprit.tn.smartdermato.BuildConfig;
import smartdermato.esprit.tn.smartdermato.R;

public class DialogueDiplomeOcr extends AppCompatDialogFragment {

    LinearLayout importimage;
    LinearLayout opencamera;

    private static final int FILE_image_REQUEST_CODE = 123;
    private static final int take_photo_REQUEST_CODE = 124;
    private static final int REQUEST_WRITE_PERMISSION = 20;




    private String cameraFilePath;

    private Uri imageUri;

    Bitmap bitmap;
    Map<String,Boolean> dictDoctor  = new HashMap<String, Boolean>();
    Map<String,Boolean> dictDoctorligne  = new HashMap<String, Boolean>();

    public void setDictDoctorligne() {

        this.dictDoctorligne.put("diplôme d'état de docteur en médecine ",false) ;
        this.dictDoctorligne.put("diplome d'état de docteur en medecine ",false) ;
        this.dictDoctorligne.put("diplome du doctorat en médecine",false) ;

    }

    public void setDictDoctor() {
        this.dictDoctor.put("medecine",false) ;
        this.dictDoctor.put("diplome",false) ;
        this.dictDoctor.put("doctorat",false) ;
        this.dictDoctor.put("médecine",false) ;
        this.dictDoctor.put("specialiste",false) ;
        this.dictDoctor.put("specialisees",false) ;
        this.dictDoctor.put("docteur",false) ;
        this.dictDoctor.put("diplôme",false) ;
   }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialoue_ocr,null);

        importimage=(LinearLayout) view.findViewById(R.id.dialog_import_image);
        opencamera=(LinearLayout) view.findViewById(R.id.dialog_open_camera);

setDictDoctor();
setDictDoctorligne();

        importimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, FILE_image_REQUEST_CODE);
            }
        });


        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
           // takePicture();
            }
        });

        builder.setView(view).setTitle("Import diplome ").setNegativeButton("concel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return  builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void takePicture() {

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, take_photo_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("********************************************");
        System.out.println("fil activity  result");
        System.out.println("********************************************");
        System.out.println("********************************************");
        //na7iw super khaterna fi fragment
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
        if (requestCode == FILE_image_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri path = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);

                StringBuilder imageText=getTextFromImage(getView());
                int iligne = 0;
                int iwords = 0 ;

                for (Map.Entry<String, Boolean> entry : dictDoctorligne.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue())
                    {
                       iligne ++ ;
                    }
                }

                for (Map.Entry<String, Boolean> entry : dictDoctor.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue())
                    {
                       iwords ++ ;
                    }
                }

                if  ((iligne > dictDoctorligne.size()/2) || ( iwords > dictDoctor.size()/3))
                {

                    Activity_SignUp.isDoctor = true;
                      dismiss();
                    Toast.makeText(getContext(), "verification success ", Toast.LENGTH_LONG).show();

                }
                else {
                    Activity_SignUp.isDoctor = false ;
                     // dismiss();
                    Toast.makeText(getContext(), "verification failed", Toast.LENGTH_LONG).show();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == take_photo_REQUEST_CODE && resultCode == Activity.RESULT_OK && Uri.parse(cameraFilePath) != null) {
            Uri path = Uri.parse(cameraFilePath);
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);

                StringBuilder imageText=getTextFromImage(getView());
                int iligne = 0;
                int iwords = 0 ;

                for (Map.Entry<String, Boolean> entry : dictDoctorligne.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue())
                    {
                        iligne ++ ;
                    }
                }

                for (Map.Entry<String, Boolean> entry : dictDoctor.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    if (entry.getValue())
                    {
                        iwords ++ ;
                    }
                }

                if  ((iligne > dictDoctorligne.size()/2) || ( iwords > dictDoctor.size()/3))
                {

                    Activity_SignUp.isDoctor = true;
                    dismiss();
                    Toast.makeText(getContext(), "verification success ", Toast.LENGTH_LONG).show();

                }
                else {
                    Activity_SignUp.isDoctor = false ;
                    // dismiss();
                    Toast.makeText(getContext(), "verification failed", Toast.LENGTH_LONG).show();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StringBuilder getTextFromImage(View v)
    {



        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

        if (!textRecognizer.isOperational())
        {

        }
        else
        {
            Frame frame= new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items =textRecognizer.detect(frame);

            StringBuilder sb=new StringBuilder();
            String blocks = "";
            String lines = "";
            String words = "";

            for (int i=0;i<items.size();i++)
            {
                TextBlock myItem=items.valueAt(i);
                sb.append(i+" : "+myItem.getValue());
                sb.append("\n");

                TextBlock tBlock = items.valueAt(i);
                blocks = blocks + tBlock.getValue() + "\n" + "\n";
                for (Text line : tBlock.getComponents()) {

                    for (Map.Entry<String, Boolean> entry : dictDoctorligne.entrySet()) {
                        System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                        if (entry.getKey().equalsIgnoreCase(line.getValue()))
                        {
                            dictDoctorligne.put(entry.getKey(),true);
                        }
                    }
                    //extract scanned text lines here
                    lines = lines + line.getValue() + "\n";
                    for (Text element : line.getComponents()) {
                        //extract scanned text words here
                        for (Map.Entry<String, Boolean> entry : dictDoctor.entrySet()) {
                            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                            if (entry.getKey().equalsIgnoreCase(element.getValue()))
                            {
                                dictDoctor.put(entry.getKey(),true);
                            }
                        }
                        words = words + element.getValue() + ", ";
                    }
                }

            }


            System.out.println("**************************text*****************************");
            System.out.println(sb.toString());
            System.out.println("******************************************************");
            System.out.println("**************************block*****************************");
            System.out.println(blocks);
            System.out.println("******************************************************");
            System.out.println("**************************lines*****************************");
            System.out.println(lines);
            System.out.println("******************************************************");
            System.out.println("**************************words*****************************");
            System.out.println(words);
            System.out.println("******************************************************");
            return sb;
        }

        return null;
    }
}

