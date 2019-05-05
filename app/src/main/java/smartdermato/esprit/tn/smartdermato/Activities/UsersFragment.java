package smartdermato.esprit.tn.smartdermato.Activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Adapter.UserAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Subscriber;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Subscriber> subscribers;
    // private List<User_Firebase> user_firebases;
    private List<Users> user_firebases;
    SharedPreferences mPreferences;
    private SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);
        subscribers = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFollow();
//                readUsers();
                refresh.setRefreshing(false);
            }
        });
        user_firebases = new ArrayList<>();
        getFollow();


        return view;

    }

    private void readUsers() {
        final String URL = util.getDomaneName() + "/api/Users/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {
                            user_firebases.clear();

                            //  System.out.println(o.toString()+" respoce!!!!++++");

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {


                                JSONObject o = array.getJSONObject(i);
                                // JSONObject o = response;

                                //JSONObject Sender = o.get("sender");
                                if(o.getString("role").equals("medecin")) {
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
                                    for (Subscriber subscriber : subscribers) {
                                        if (subscriber.getPationId() == mPreferences.getInt(getString(R.string.Id), 0) && subscriber.getMedceinId() == user.getId()&& subscriber.getEtat() == 1)
                                        {
                                            user_firebases.add(user);

                                        }
                                    }

                                    System.out.println(o.getString("role"));
//                                    if (user.getId() != mPreferences.getInt(getString(R.string.Id), 0)) {
//                                        user_firebases.add(user);
//                                    }
                                }
                            }

                            userAdapter = new UserAdapter(getContext(),user_firebases,false,"Users","","vide",0);
                            recyclerView.setAdapter(userAdapter);
                            userAdapter.notifyDataSetChanged();

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

//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                user_firebases.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    User_Firebase user_firebase = snapshot.getValue(User_Firebase.class);
//
//                    assert user_firebase != null;
//                    assert firebaseUser != null;
//                    if(!user_firebase.getId().equals(firebaseUser.getUid())){
//                        user_firebases.add(user_firebase);
//                    }
//                }
//
//                userAdapter = new UserAdapter(getContext(),user_firebases,false);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
    private void getFollow(){
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




                            }
                            readUsers();
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }

}
