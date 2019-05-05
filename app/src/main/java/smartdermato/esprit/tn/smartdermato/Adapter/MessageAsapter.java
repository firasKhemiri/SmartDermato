package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.Volley;
import com.klinker.android.peekview.PeekViewActivity;
import com.klinker.android.peekview.builder.Peek;
import com.klinker.android.peekview.builder.PeekViewOptions;
import com.klinker.android.peekview.callback.SimpleOnPeek;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.Activities.ShowProfile;
import smartdermato.esprit.tn.smartdermato.Entities.Chats;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class MessageAsapter extends RecyclerView.Adapter<MessageAsapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chats> mChats;
    private String imageurl;
    private int pos = 0;

    private SharedPreferences mPreferences;

    public MessageAsapter(Context mContext, List<Chats> mChats, String imageurl){
        System.out.println("mChats  "+mChats);
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageurl =imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        System.out.println(i);

        if(i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            pos =1;
            return new MessageAsapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            pos =2;
            return new MessageAsapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Chats chat = mChats.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        if(imageurl.equals("vide_pic")){
            viewHolder.profile_image.setImageResource(R.drawable.userprofile);
        }
        else {
            Picasso.with(mContext)
                    .load(util.getDomaneName()+"/Content/Images/"+imageurl).into(viewHolder.profile_image);





        }

        viewHolder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage(mPreferences.getString(mContext.getString(R.string.role),""));
                if(mPreferences.getString(mContext.getString(R.string.role),"").equals("patient") ) {
                    Intent intent = new Intent(mContext, ShowProfile.class);
                    intent.putExtra("user_id", chat.getSender());
                    mContext.startActivity(intent);
                }
//            if(mPreferences.getString(mContext.getString(R.string.role),"").equals("pation") )
//            {
//                viewHolder.profile_image.setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                toastMessage("a");
//
//                                if (chat.getSender() != mPreferences.getInt(mContext.getString(R.string.Id), 0)) {
//                                    toastMessage("b");
//
//                                }
//                                if (chat.getReceiver() != mPreferences.getInt(mContext.getString(R.string.Id), 0)) {
//                                    toastMessage("c");
//                                    Intent intent = new Intent(mContext, ShowProfile.class);
//                                    intent.putExtra("user_id", chat.getReceiver());
//                                    mContext.startActivity(intent);
//                                }
//
//
//                            }
//                        });
//            }
            }
        });
        PeekViewOptions options = new PeekViewOptions();
        options.setBackgroundDim(1f);           // range: 0  - 1  (default is .6)
        options.setHapticFeedback(false);       // default is true

// it may be a good idea to set set these through resources so that you can use different options based on screen size and orientation
        options.setWidthPercent(.4f);           // range: .1 - .9 (default is .6)
        options.setHeightPercent(.4f);          // range: .1 - .9 (default is .5)

// you can also set the size of the PeekView using absolute values, instead of percentages.
// Setting these will override the corresponding percentage value.
// You should use this instead of setting the size of the view from the layout resources, as those get overridden.
        options.setAbsoluteWidth(200);          // 200 DP
        options.setAbsoluteHeight(200);         // 200 DP

// default is false. If you change this to true, it will ignore the width and height percentages you set.
        options.setFullScreenPeek(true);
// default is true. Unless you are going to animate things yourself, i recommend leaving this as true.
        //options.setFadeAnimation(false);
        options.setUseFadeAnimation(false);
// PeekView has the ability to blur the background behind it, instead of just using a simple dark dim.
// If you set a blurred view, then it will invalidate whatever you set as your background dim.
// If you do this, please look at the installation steps for the blur effect, or the app will crash.
        options.setBlurBackground(true);                            // default is true
        options.setBlurOverlayColor(Color.parseColor  ("#99000000"));      // #99000000 default

        //  Peek.into(...).with(options).applyTo(...);
        if(!chat.getImageName().equals("vide"))
        {
            viewHolder.cardView.setVisibility(View.GONE);

            // viewHolder.imageSend.setImageResource(chat.getImageName());


            final List<String> allToken = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(mContext);

            final String URL = util.getDomaneName() + "/api/Consultations/"+chat.getIdCon();

            System.out.println("URL GET Token:  "+ URL);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response+" responce Get Token!!!!++++");


                            try {
                                //  System.out.println(o.toString()+" respoce!!!!++++");
                                viewHolder.cardView.setVisibility(View.VISIBLE);

                                viewHolder.show_message.setText(chat.getMessage());

                                Picasso.with(mContext)
                                        .load(util.getDomaneName()+"/Content/Images/"+chat.getImageName()).into(viewHolder.imageSend);
                                Peek.into(R.layout.image_details, new SimpleOnPeek() {
                                    @Override
                                    public void onInflated(View rootView) {
                                        // rootView is the layout inflated from R.layout.image_peek

                                        JSONObject o = response;
                                        rootView.animate();
                                        try {
                                            ((ImageView) rootView.findViewById(R.id.image))
                                                    .setImageBitmap(getBitmapFromURL(util.getDomaneName()+"/Content/Images/"+chat.getImageName()));
                                            ((TextView) rootView.findViewById(R.id.pso)).setText(o.getString("typeC"));
                                            ((TextView) rootView.findViewById(R.id.pourcentage)).setText(o.getString("pourcentageC"));
                                            ((TextView) rootView.findViewById(R.id.date)).setText(o.getString("date").substring(0,10));
                                            ((TextView) rootView.findViewById(R.id.time)).setText(o.getString("date").substring(11,16));


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).with(options).applyTo((PeekViewActivity) mContext, viewHolder.imageSend);





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
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.show_message.setText("Consultation supprimer");


                    return;
                }
            });

            queue.add(stringRequest);




        }
        else
        {
            viewHolder.cardView.setVisibility(View.GONE);

        }
        if(i == mChats.size()-1){
            if (chat.isIsseen()){

                viewHolder.txt_seen.setText("Seen");
            }
            else if(!chat.isIsseen() && pos == 1) {
                viewHolder.txt_seen.setVisibility(View.VISIBLE);

                viewHolder.txt_seen.setText("Delivered");
            }
        }else {
            viewHolder.txt_seen.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message,txt_seen;
        public CircleImageView profile_image;
        public ImageView imageSend;
        public CardView cardView;

        public ViewHolder( View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            profile_image = itemView.findViewById(R.id.profile_image);
            imageSend = itemView.findViewById(R.id.show_imag);
            cardView = itemView.findViewById(R.id.card_view_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mPreferences =mContext.getSharedPreferences("x",Context.MODE_PRIVATE);

        System.out.println(mChats.get(position).getSender() +" --------- ");
        System.out.println("++++++++++++ "+mPreferences.getInt(mContext.getString(R.string.Id),0) );
        if(mChats.get(position).getSender() == (mPreferences.getInt(mContext.getString(R.string.Id),0))){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
    public static Bitmap getBitmapFromURL(String src) throws IOException {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    public void toastMessage(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

}
