package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.Activities.MessageActivity;
import smartdermato.esprit.tn.smartdermato.Entities.Chats;
import smartdermato.esprit.tn.smartdermato.Entities.Subscriber;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    //private List<User_Firebase> mUser_firebases;
    private List<Users> mUser_firebases;
    private boolean ischat;
    private String from,dateTime;
    private int image;
    private List<Subscriber> subscribers;
    private SharedPreferences mPreferences;
    private static final String TAG = "UserAdapter";

    public UserAdapter(Context mContext,List<Users> mUser_firebases,boolean ischat,String from,String dateTime,int image){
        this.mContext = mContext;
        this.mUser_firebases = mUser_firebases;
        this.ischat = ischat;
        this.from = from;
        this.dateTime = dateTime;
        this.image = image;

        System.out.println("image: "+image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences= mContext.getSharedPreferences("x", Context.MODE_PRIVATE);
        subscribers = new ArrayList<>();
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       // final User_Firebase user_firebase =mUser_firebases.get(i);
        final Users user_firebase =mUser_firebases.get(i);
        System.out.println(user_firebase);
        viewHolder.username.setText(user_firebase.getUsername());
        if(user_firebase.getUser_pic().equals("vide_pic")){
            viewHolder.profile_image.setImageResource(R.drawable.userprofile);
        }
        else {
            Picasso.with(mContext)
                    .load(util.getDomaneName()+"/Content/Images/"+user_firebase.getUser_pic()).into(viewHolder.profile_image);

        }
        getFollow(user_firebase.getId(),viewHolder);

        if (from.equals("Patients"))
        {
            viewHolder.block.setVisibility(View.VISIBLE);
            viewHolder.block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        for (Subscriber subscriber : subscribers)
                        {

                            System.out.println("for subscribe: "+subscriber);
                            if(subscriber.getMedceinId() == mPreferences.getInt(mContext.getString(R.string.Id),0) && subscriber.getPationId() == user_firebase.getId())
                            {
                                System.out.println("for subscribe2: "+subscriber);
                                if(subscriber.getEtat() == 2)
                                {
                                    Subscriber subscriber0 = new Subscriber();
                                    subscriber0.setPationId(subscriber.getPationId());
                                    subscriber0.setMedceinId(subscriber.getMedceinId());
                                    subscriber0.setEtat(1);

                                    putFollow(subscriber,subscriber0,viewHolder);
                                    break;
                                }
                                else
                                {

                                    Subscriber subscriber0 = new Subscriber();
                                    subscriber0.setPationId(subscriber.getPationId());
                                    subscriber0.setMedceinId(subscriber.getMedceinId());
                                    subscriber0.setEtat(2);

                                    putFollow(subscriber,subscriber0,viewHolder);
                                    break;
                                }
                            }


                    }


                }
            });
        }

        if(ischat){
            if(user_firebase.getStatus().equals("online")){
                System.out.println(user_firebase);
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
                viewHolder.profile_image.setBorderColorResource(R.color.on);
            }else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
                viewHolder.profile_image.setBorderColorResource(R.color.off);
            }
        }else {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
            viewHolder.profile_image.setBorderColor(Color.parseColor("#bfbfbf"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from.equals("Home")){
                    System.out.println("image2: "+image);
                    sendMessage(mPreferences.getInt(mContext.getString(R.string.Id),0),user_firebase.getId(),"Cette image est prise le "+dateTime,image);
                }
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("user_firebase_id",user_firebase.getId());
                mContext.startActivity(intent);
            }
        });



    }

    private void putFollow(final Subscriber subscribero, final Subscriber subscriber, final ViewHolder searchViewHolder)
    {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final String URI = util.getDomaneName()+"/api/Subscribers/"+subscriber.getPationId()+"/"+subscriber.getMedceinId();
        System.out.println(URI);
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("pationId",subscriber.getPationId());
        paramsToken.put("medecinId",subscriber.getMedceinId());
        paramsToken.put("etat",subscriber.getEtat());
        System.out.println("Follow: "+new JSONObject(paramsToken));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" Token Out respoce!!!!++++");

                        try {
                            int index = subscribers.indexOf(subscribero);

                            System.out.println(index);

                            JSONObject o = response;
                            Subscriber subscriber = new Subscriber();
                            subscriber.setPationId(o.getInt("pationId"));
                            subscriber.setMedceinId(o.getInt("medecinId"));
                            subscriber.setEtat(o.getInt("etat"));
                            System.out.println("put sub: "+subscriber);
                            subscribers.remove(index);
                            subscribers.add(index,subscriber);
                            if(subscriber.getEtat()== 2)
                            {
                                searchViewHolder.block.setBackgroundResource(R.drawable.userblock);

                            }
                            else
                            {
                                searchViewHolder.block.setBackgroundResource(R.drawable.userblock2);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



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
    private void getFollow(int idM, final ViewHolder searchViewHolder){
        final String URL = util.getDomaneName() + "/api/Subscribers/";
        System.out.println("URLLLL:  "+ URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(subscribers.size()>0)
                            subscribers.clear();
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);

                                Subscriber subscriber = new Subscriber();
                                subscriber.setPationId(o.getInt("pationId"));
                                subscriber.setMedceinId(o.getInt("medecinId"));
                                subscriber.setEtat(o.getInt("etat"));

                                System.out.println("get: "+subscriber);
                                subscribers.add(subscriber);
                                System.out.println("boolean test :  "+String.valueOf(subscriber.getMedceinId() == mPreferences.getInt(mContext.getString(R.string.Id),0)));
                                System.out.println("boolean test0 :  "+String.valueOf(subscriber.getEtat() == 2 && subscriber.getMedceinId() == mPreferences.getInt(mContext.getString(R.string.Id),0)&& subscriber.getPationId() == idM));
                                System.out.println("boolean test1 :  "+ subscriber.getEtat());

                                if(subscriber.getEtat() == 2 && subscriber.getMedceinId() == mPreferences.getInt(mContext.getString(R.string.Id),0) && subscriber.getPationId() == idM)
                                {
                                    System.out.println("okkkkkkk");
                                    searchViewHolder.block.setBackgroundResource(R.drawable.userblock);
                                    System.out.println("okkkkkkk22");

                                }
                                else if (subscriber.getEtat() == 1 && subscriber.getMedceinId() == mPreferences.getInt(mContext.getString(R.string.Id),0) && subscriber.getPationId() == idM)
                                    {
                                    searchViewHolder.block.setBackgroundResource(R.drawable.userblock2);



                                }


                            }
                        } catch (JSONException e) {
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

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return mUser_firebases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public CircleImageView profile_image,img_on,img_off;
        public Button block;
        public ViewHolder( View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            block = itemView.findViewById(R.id.block);

        }
    }
    private void sendMessage(int sender , final int receiver , String message,int imageName ){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("imageName",imageName);
        hashMap.put("isseen",false);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final String URI = util.getDomaneName()+"/api/Chats";
        Log.d(TAG,"Json"+new JSONObject(hashMap));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(hashMap),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce send msg!!!!++++");
                        try {
//

                            Chats chats = new Chats();
                            chats.setMessage(response.getString("message"));
                            chats.setSender(response.getInt("sender"));
                            chats.setReceiver(response.getInt("receiver"));
                            chats.setId(response.getInt("id"));
                            GetToken(chats);
                            // readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
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





        queue.add(stringRequest);
//        reference.child("Chats").push().setValue(hashMap);

//        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
//                .child(firebaseUser.getUid())
//                .child(userid);
//        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists())
//                {
//                    chatRef.child("id").setValue(userid);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        final String msg = message;
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User_Firebase user_firebase = dataSnapshot.getValue(User_Firebase.class);
//                if(notify){
//                    sendNotification(receiver,user_firebase.getUsername(),msg);
//
//                }
//                notify = false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
    private void GetToken(final Chats chats)
    {
        final List<String> allToken = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(mContext);

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

                                if(o.getString("log").equals("IN") && o.getInt("userToken") == chats.getReceiver())
                                {
                                    allToken.add(o.getString("token"));
                                }

                            }
                            System.out.println(allToken);
                            SendNotification(chats,allToken);
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


    private void SendNotification(Chats chats,List<String> allToken){



        for (String token :allToken){
            System.out.println(chats);
            System.out.println(token);
            RequestQueue queue = Volley.newRequestQueue(mContext);

            HashMap<String,Object> data = new HashMap<>();
            HashMap<String,Object> notification = new HashMap<>();
            //getName(chats.getSender());
            data.put("title", mPreferences.getString(mContext.getString(R.string.username),""));
            data.put("content",chats.getMessage());
            data.put("user",chats.getSender());
            notification.put("data",new JSONObject(data));
            notification.put("to",token);
            Log.d(TAG,"Json"+new JSONObject(notification));
            String url = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(notification),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            Log.d("Response", response+" respoce send Notification!!!!++++");
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.d("ERROR","error => "+error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "key=AAAAFmjBvLY:APA91bFMaqkxngJweh2z20jZJixqUYrImqFxL3_zlyEHHlakl64H6rNHa2T4Qs5AINj8uUVsTeONsnRvLlUfxp5MNBnyJOPJ0VfviO64yQIx0rsvajTpc4MK0GPLVb_Z48zf6nCea2YR");

                    return params;
                }
            };
            queue.add(stringRequest);
        }


    }

}
