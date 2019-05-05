package smartdermato.esprit.tn.smartdermato.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.peekview.PeekViewActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.Adapter.MessageAsapter;
import smartdermato.esprit.tn.smartdermato.Entities.Chats;
import smartdermato.esprit.tn.smartdermato.Entities.Subscriber;
import smartdermato.esprit.tn.smartdermato.MainActivity;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class MessageActivity extends PeekViewActivity {

    private CircleImageView profile_image;
    private TextView username;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Intent intent;
    private Window window;
    private ImageView btn_send;
    private EditText text_send;
    private MessageAsapter messageAsapter;
    private List<Chats> mchat;
    private RecyclerView recyclerView;
    private RelativeLayout layout_send;
    private ValueEventListener seenListener;
    private List<Subscriber> subscribers;

    //    private APIService apiService;
    private int userid;
    private boolean notify = false;
    private SharedPreferences mPreferences;
    private Map<String,Object> params = new HashMap<String, Object>();
    private static final String TAG = "MessageActivity";
    private int sizeliste =0;
    private SwipeRefreshLayout refresh;
    private Handler handler = new Handler();
    private Handler mHandler,mHandler2;
    private boolean seen = false;
    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        window=this.getWindow();
        window.setStatusBarColor(Color.parseColor("#17A8C2"));
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreferences= getSharedPreferences("x", Context.MODE_PRIVATE);
        subscribers = new ArrayList<>();
        getFollow();
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.mHandler = new Handler();
        this.mHandler2 = new Handler();

        this.mHandler.postDelayed(m_Runnable,4000);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPreferences.getString(getString(R.string.role),"").equals("patient"))
                {
                    startActivity(new Intent(MessageActivity.this,Activity_Chat_PM.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                else
                {
                    startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            }
        });
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));
                seenMessage(userid);

                refresh.setRefreshing(false);
            }
        });
        //   apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        layout_send = findViewById(R.id.layout_send);
        intent = getIntent();
        userid =  intent.getIntExtra("user_firebase_id",0);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        text_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seenMessage(userid);
            }
        });
//        for (Subscriber subscriber : subscribers)
//        {
//            System.out.println( "------------------------------------------------*************");
//            System.out.println(subscriber);
//            System.out.println( subscriber.getPationId() == mPreferences.getInt(getString(R.string.Id), 0));
//            System.out.println( subscriber.getMedceinId() == userid);
//            if (subscriber.getPationId() == mPreferences.getInt(getString(R.string.Id), 0) && subscriber.getMedceinId() == userid&& subscriber.getEtat() == 0)
//            {
//                layout_send.setEnabled(false);
//                layout_send.setBackgroundColor(R.color.light_grey);
//                break;
//
//            }
//        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(mPreferences.getInt(getString(R.string.Id),0),userid,msg);
                }else {
                    Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        getUser(userid);
        //readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User_Firebase user_firebase = dataSnapshot.getValue(User_Firebase.class);
//                username.setText(user_firebase.getUsername());
//                if(user_firebase.getImageURL().equals("default")){
//                    profile_image.setImageResource(R.drawable.userprofile);
//                }
//                else {
//                    Glide.with(getApplicationContext()).load(user_firebase.getImageURL()).into(profile_image);
//                }
//                readMessages(firebaseUser.getUid(),userid,user_firebase.getImageURL());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        seenMessage(userid);
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(MessageActivity.this,"in runnable",Toast.LENGTH_SHORT).show();

            refresh();
            seenMessage(userid);




            MessageActivity.this.mHandler.postDelayed(m_Runnable, 4000);
        }

    };

    private void seenMessage(final int userid){
        final String URL = util.getDomaneName() + "/api/Chats/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                final Chats chats = new Chats();
                                chats.setId(o.getInt("id"));
                                chats.setIsseen(o.getBoolean("isseen"));
                                chats.setMessage(o.getString("message"));
                                chats.setImageName(o.getString("imageName"));
                                chats.setSender(o.getInt("sender"));
                                chats.setReceiver(o.getInt("receiver"));
                                chats.setIdCon(o.getInt("idConsultationP"));


                                System.out.println(chats);

                                if(chats.getReceiver() == mPreferences.getInt(getString(R.string.Id),0) && chats.getSender() == (userid) && !chats.isIsseen() ){
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("isseen",true);
                                    hashMap.put("sender",chats.getSender());
                                    hashMap.put("receiver",chats.getReceiver());
                                    hashMap.put("message",chats.getMessage());
                                    hashMap.put("id",chats.getId());
                                    hashMap.put("imageName",chats.getImageName());
                                    hashMap.put("idConsultationP",chats.getIdCon());

                                    RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
                                    final String URI = util.getDomaneName()+"/api/Chats/"+chats.getId();
                                    Log.d(TAG,"Json"+new JSONObject(hashMap));
                                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URI, new JSONObject(hashMap),
                                            new com.android.volley.Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    System.out.println(response+" !!!!++++");
                                                    try {
                                                        final Chats chatsF = new Chats();
                                                        chatsF.setId(response.getInt("id"));
                                                        chatsF.setIsseen(response.getBoolean("isseen"));
                                                        chatsF.setMessage(response.getString("message"));

                                                        chatsF.setSender(response.getInt("sender"));
                                                        chatsF.setReceiver(response.getInt("receiver"));
                                                        chatsF.setIdCon(response.getInt("idConsultationP"));

                                                        System.out.println(chatsF);
                                                        int index = mchat.indexOf(chats);

                                                        mchat.remove(chats);
                                                        System.out.println("chatF: "+chatsF);
                                                        mchat.add(index,chatsF);
                                                        readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));



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
                                                }
                                            });





                                    queue.add(stringRequest);
                                }
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

                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(stringRequest);
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        seenListener = reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
//                        HashMap<String,Object> hashMap = new HashMap<>();
//                        hashMap.put("isseen",true);
//                        snapshot.getRef().updateChildren(hashMap);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
    private void sendMessage(int sender , final int receiver , String message ){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("imageName","vide");
        hashMap.put("isseen",false);
        hashMap.put("idConsultationP",0);

        RequestQueue queue = Volley.newRequestQueue(this);
        final String URI = util.getDomaneName()+"/api/Chats";
        Log.d(TAG,"Json"+new JSONObject(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(hashMap),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce send msg!!!!++++");
                        try {
//
                            Chats chats = new Chats();
                            chats.setIsseen(response.getBoolean("isseen"));
                            chats.setMessage(response.getString("message"));
                            chats.setSender(response.getInt("sender"));
                            chats.setReceiver(response.getInt("receiver"));
                            chats.setId(response.getInt("id"));
                            chats.setImageName(response.getString("imageName"));
                            chats.setIdCon(response.getInt("idConsultationP"));
                            GetToken(chats);
                            refresh();
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

    private void getUser(final int userid)
    {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String URL = util.getDomaneName() + "/api/Users/"+userid;

        System.out.println("URL GET Users:  "+ URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" responce Get Users!!!!++++");


                        try {
                            //  System.out.println(o.toString()+" respoce!!!!++++");





                            username.setText(response.getString("Username"));
                            if(response.getString("ProfilePic").equals("vide_pic")){
                                profile_image.setImageResource(R.drawable.userprofile);
                            }
                            else {
                                Picasso.with(MessageActivity.this)
                                        .load(util.getDomaneName()+"/Content/Images/"+response.getString("ProfilePic")).into(profile_image);
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
    private void GetToken(final Chats chats)
    {
        final List<String> allToken = new ArrayList<>();
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
            RequestQueue queue = Volley.newRequestQueue(this);

            HashMap<String,Object> data = new HashMap<>();
            HashMap<String,Object> notification = new HashMap<>();
            //getName(chats.getSender());
            data.put("title", mPreferences.getString(getString(R.string.username),""));
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
    String img;
    private void refresh()
    {

        final String URL = util.getDomaneName() + "/api/Chats/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {
                            if (!mchat.isEmpty())
                                mchat.clear();

                            Boolean isseen = false;
                            //  System.out.println(o.toString()+" respoce!!!!++++");
                            JSONArray array = new JSONArray(response);
                            System.out.println("array size "+array.length()+"   size  "+sizeliste);

                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                Chats chats = new Chats();
                                chats.setId(o.getInt("id"));
                                chats.setIsseen(o.getBoolean("isseen"));
                                chats.setMessage(o.getString("message"));
                                chats.setSender(o.getInt("sender"));
                                chats.setReceiver(o.getInt("receiver"));
                                chats.setImageName(o.getString("imageName"));
                                chats.setIdCon(o.getInt("idConsultationP"));


//
                                System.out.println(chats);

                                if (chats.getReceiver() == mPreferences.getInt(getString(R.string.Id), 0) && chats.getSender() == userid ||
                                        chats.getReceiver() == userid && chats.getSender() == mPreferences.getInt(getString(R.string.Id), 0)) {
                                    System.out.println("indexOf  "+ (mchat.indexOf(chats)));
                                    System.out.println("isseen   "+isseen);
                                    System.out.println("getisseen  "+chats.isIsseen());
                                    if(chats.isIsseen() && i == array.length())
                                    {
                                        isseen = true;
                                    }

                                    if(mchat.indexOf(chats)<0)
                                    {
                                        mchat.add(chats);

                                    }

                                }

                            }
                            System.out.println(isseen);
                            System.out.println(sizeliste);
                            System.out.println(array.length());
                            System.out.println(sizeliste != array.length());
                            if(sizeliste != array.length() ){
                                sizeliste = array.length();
//                                    messageAsapter = new MessageAsapter(MessageActivity.this, mchat, img);
////                                    recyclerView.setAdapter(messageAsapter);
////                                    messageAsapter.notifyDataSetChanged();
                                getimage();


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

                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(stringRequest);

    }
    private void readMessages(final int myid, final int userid, final String imageurl){
        mchat = new ArrayList<>();
        final String URL = util.getDomaneName() + "/api/Chats/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {
                            if (!mchat.isEmpty())
                                mchat.clear();
                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            sizeliste = array.length();
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                Chats chats = new Chats();
                                chats.setId(o.getInt("id"));
                                chats.setIsseen(o.getBoolean("isseen"));
                                chats.setMessage(o.getString("message"));
                                chats.setSender(o.getInt("sender"));
                                chats.setReceiver(o.getInt("receiver"));
                                chats.setImageName(o.getString("imageName"));
                                chats.setIdCon(o.getInt("idConsultationP"));

                                if (chats.getReceiver()== myid && chats.getSender() == userid ||
                                        chats.getReceiver()== userid && chats.getSender() == myid ) {
                                    mchat.add(chats);


                                }

                                System.out.println(chats);

                                getimage();

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

                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(stringRequest);

//        reference =FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mchat.clear();
//                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    Chat chat = snapshot.getValue(Chat.class);
//                    System.out.println(chat);
//                    if(chat.getReceiver().equals(myid)&& chat.getSender().equals(userid) ||
//                            chat.getReceiver().equals(userid)&& chat.getSender().equals(myid)){
//                        mchat.add(chat);
//
//                    }
//                    messageAsapter = new MessageAsapter(MessageActivity.this,mchat,imageurl);
//                    recyclerView.setAdapter(messageAsapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void getimage()
    {

        final String URL = util.getDomaneName() + "/api/Users/" +userid;
        System.out.println("URLLLL:  "+ URL);

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");


                            JSONObject o = response;


                            System.out.println(o.getString("ProfilePic"));


                            messageAsapter = new MessageAsapter(MessageActivity.this,mchat,o.getString("ProfilePic"));
                            recyclerView.setAdapter(messageAsapter);
                            messageAsapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
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
                ;
                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(stringRequest);




//
    }
    private void  status(String status){
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("status",status);
//        reference.updateChildren(hashMap);
        System.out.println("status:    "+status);
        final String URL = util.getDomaneName() + "/api/Users/" +mPreferences.getInt(getString(R.string.Id),0);
        System.out.println("URLLLL:  "+ URL);
        params.put("Id",mPreferences.getInt(getString(R.string.Id),0));
        params.put("Username",mPreferences.getString(getString(R.string.username),""));
        params.put("Password",mPreferences.getString(getString(R.string.password),""));
        params.put("Email",mPreferences.getString(getString(R.string.email),""));
        params.put("ProfilePic",mPreferences.getString(getString(R.string.user_pic),""));
        params.put("FirstName",mPreferences.getString(getString(R.string.FirstName),""));
        params.put("LastName",mPreferences.getString(getString(R.string.LastName),""));
        params.put("Sexe",mPreferences.getString(getString(R.string.Sexe),""));
        params.put("DateOfBirth",mPreferences.getString(getString(R.string.DateOfBirth),""));
        params.put("PhoneNumber",mPreferences.getString(getString(R.string.PhoneNumber),""));
        params.put("certification",mPreferences.getString(getString(R.string.certification),""));
        params.put("EtatCertification",mPreferences.getInt(getString(R.string.EtatCertification),0));
        params.put("role",mPreferences.getString(getString(R.string.role),""));
        params.put("Status",status);
        params.put("City",mPreferences.getString(getString(R.string.City),""));
        params.put("Country",mPreferences.getString(getString(R.string.Country),""));
        params.put("OfficeNumber",mPreferences.getString(getString(R.string.OfficeNumber),""));
        params.put("OfficeAdress",mPreferences.getString(getString(R.string.OfficeAddess),""));
        params.put("PostalCode",mPreferences.getString(getString(R.string.PostalCode),""));
        params.put("CountryOfficeNumber",mPreferences.getString(getString(R.string.CountryOfficeNumber),""));
        params.put("CountryPhoneNumber",mPreferences.getString(getString(R.string.CountryPhoneNumber),""));
        params.put("Certificatdate",mPreferences.getString(getString(R.string.CertificatDate),""));
        Log.d(TAG,"Json"+new JSONObject(params));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, URL, new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");


                            JSONObject o = response;





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

                return;

            }
        });

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(stringRequest);
    }
    private void getFollow(){
        final String URL = util.getDomaneName() + "/api/Subscribers/";
        System.out.println("URLLLL:  "+ URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceAsColor")
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

                                toastMessage(String.valueOf(subscriber.getEtat()));

                                System.out.println("Message Subs: "+subscriber);
                                if (subscriber.getPationId() == mPreferences.getInt(getString(R.string.Id), 0) && subscriber.getMedceinId() == userid&& (subscriber.getEtat() == 0 || subscriber.getEtat() == 2))
                                {
                                    text_send.setEnabled(false);
                                    btn_send.setEnabled(false);
                                    text_send.setHint("Disable discussion");


                                }
                                if (subscriber.getMedceinId() == mPreferences.getInt(getString(R.string.Id), 0) && subscriber.getPationId() == userid&& (subscriber.getEtat() == 0 || subscriber.getEtat() == 2))
                                {
                                    text_send.setEnabled(false);
                                    btn_send.setEnabled(false);
                                    text_send.setHint("Disable discussion");


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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));

    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
//        reference.removeEventListener(seenListener);
        status("offline");
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            refresh();
//           // readMessages(mPreferences.getInt(getString(R.string.Id),0),userid,mPreferences.getString(getString(R.string.user_pic),""));
//            seenMessage(userid);
//            handler.postDelayed(this, 500);
//        }
//    };

    //Start
    @Override
    protected void onStart() {
        super.onStart();
        //  handler.postDelayed(runnable, 1000);

    }
}
