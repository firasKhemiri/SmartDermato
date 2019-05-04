package smartdermato.esprit.tn.smartdermato.Activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Adapter.UserAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Chats;
import smartdermato.esprit.tn.smartdermato.Entities.User_Firebase;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User_Firebase> user_firebases;
    private List<Users> users;
    SharedPreferences mPreferences;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private SwipeRefreshLayout refresh;
    private List<String> usersList;
    private List<Integer> usersListe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            readChats();
            refresh.setRefreshing(false);
            }
        });
        final String URL = util.getDomaneName() + "/api/Chats/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce chat users!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");
                            usersListe = new ArrayList<>();

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                               // JSONObject o = response;
                                Chats chats = new Chats();
                                chats.setId(o.getInt("id"));
                                chats.setIsseen(o.getBoolean("isseen"));
                                chats.setMessage(o.getString("message"));
                                    chats.setSender(o.getInt("sender"));
                                    chats.setReceiver(o.getInt("receiver"));
//                                JSONArray Receiver = o.getJSONArray("receiver");
//                                for(int j = 0;j < Receiver.length(); i++){
//                                    JSONObject w = Receiver.getJSONObject(i);
//                                    Users usersReceiver = new Users();
//                                    usersReceiver.setId(w.getInt("Id"));
//                                    chats.setReceiver(usersReceiver);
//                                }
//                                JSONArray Sender = o.getJSONArray("sender");
//                                for(int k = 0;k < Sender.length(); i++){
//                                    JSONObject w = Sender.getJSONObject(i);
//                                    Users usersSender = new Users();
//                                    usersSender.setId(w.getInt("Id"));
//                                    chats.setSender(usersSender);
//                                }
//                                System.out.println(chats+"   **-*-*-*-*-*-");
//                                Users usersReceiver = new Users();
//                                JSONObject Receiver = o.getJSONObject("receiver");
//                                usersReceiver.setId(Receiver.getInt("Id"));
//                                chats.setReceiver(usersReceiver);
//                                Users usersSender = new Users();
//                                JSONObject Sender = o.getJSONObject("sender");
//                                usersSender.setId(Sender.getInt("Id"));
//                                chats.setSender(usersSender);
//                                System.out.println(chats);
                                //JSONObject Sender = o.get("sender");
                                System.out.println(chats);

                                System.out.println(mPreferences.getInt(getString(R.string.Id), 0));
                                System.out.println(chats.getSender() == mPreferences.getInt(getString(R.string.Id), 0));
                                if (chats.getSender() == mPreferences.getInt(getString(R.string.Id), 0)) {
                                    System.out.println(chats.getReceiver());
                                    System.out.println(usersListe);
                                    usersListe.add(chats.getReceiver());

                                }
                                if (chats.getReceiver() == mPreferences.getInt(getString(R.string.Id), 0)) {
                                    usersListe.add(chats.getSender());
                                }

                            }
                            System.out.println("   "+usersListe);

                            readChats();


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

//        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        usersList = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if(chat.getSender().equals(fuser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(fuser.getUid())){
//                        usersList.add(chat.getSender());
//                    }
//                }
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;

    }
//    private void updateToken(String token){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token1 = new Token();
//        reference.child(fuser.getUid()).setValue(token1);
//    }
    private void readChats(){
        user_firebases = new ArrayList<>();
        users = new ArrayList<>();
        final String URL = util.getDomaneName() + "/api/Users/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                            //  System.out.println(o.toString()+" respoce!!!!++++");
                            users.clear();

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                Users user = new Users();
                                user.setId(o.getInt("Id"));
                                user.setUsername(o.getString("Username"));
                                user.setPassword(o.getString("Password"));
                                user.setEmail(o.getString("Email"));
                                user.setUser_pic(o.getString("ProfilePic"));
                                user.setRole(o.getString("role"));
                                user.setFirstName(o.getString("FirstName"));
                                user.setLastName(o.getString("LastName"));
                                user.setSexe(o.getString("Sexe"));
                                user.setPhoneNumber(o.getString("PhoneNumber"));
                                user.setCertification(o.getString("certification"));

                                user.setEtatCertification(o.getInt("EtatCertification"));
                                user.setDateOfBirth(o.getString("DateOfBirth"));
                                user.setStatus(o.getString("Status"));
                                System.out.println(user);


                                System.out.println(o.getString("role"));
                                for (int id : usersListe) {
                                    if (user.getId() == id) {
                                        if (users.size() != 0) {
                                            for (Users users1 : users) {
                                                if (user.getId() != users1.getId()) {
                                                    users.add(user);
                                                }
                                            }
                                        } else {
                                            users.add(user);

                                        }

                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        userAdapter = new UserAdapter(getContext(),users,true,"Chats","",0);
                        recyclerView.setAdapter(userAdapter);
                        userAdapter.notifyDataSetChanged();



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

      /*  reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_firebases.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User_Firebase user_firebase = snapshot.getValue(User_Firebase.class);
                    for (String id : usersList){
                        if(user_firebase.getId().equals(id)){
                            if(user_firebases.size() != 0){
                                for(User_Firebase user_firebase1: user_firebases){
                                    if(!user_firebase.getId().equals(user_firebase1.getId())){
                                        user_firebases.add(user_firebase);
                                    }
                                }
                            }
                            else
                            {
                                user_firebases.add(user_firebase);

                            }

                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),user_firebases,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
