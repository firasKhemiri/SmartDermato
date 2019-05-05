package smartdermato.esprit.tn.smartdermato.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Entities.Consultation;
import smartdermato.esprit.tn.smartdermato.Entities.Model;
import smartdermato.esprit.tn.smartdermato.Entities.Subscriber;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder>{
    private List<Consultation> models;
    private Context context;
    private Dialog myDialog;
    private List<Subscriber> subscribers;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> user_firebases;
    private SharedPreferences mPreferences;

    private static final String TAG = "AdapterHome";
    public AdapterHome(List<Consultation> models, Context context) {
        this.models = models;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_contents,viewGroup,false);
        subscribers = new ArrayList<>();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {




        RequestOptions optionsImg = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.brochure).dontAnimate()
                .error(R.drawable.brochure)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.NORMAL);

        Glide.with(context)
                .load(R.drawable.share)
                .apply(optionsImg)
                .into(viewHolder.btn_share);

        viewHolder.btn_share.setColorFilter(ContextCompat.getColor(context, R.color.getCol2), android.graphics.PorterDuff.Mode.MULTIPLY);



        Glide.with(context)
                .load(R.drawable.time)
                .apply(optionsImg)
                .into(viewHolder.dateImg);
        //viewHolder.dateImg.setColorFilter(ContextCompat.getColor(context, R.color.getCol2), android.graphics.PorterDuff.Mode.MULTIPLY);

        Glide.with(context)
                .load(R.drawable.result)
                .apply(optionsImg)
                .into(viewHolder.resultImg);
       // viewHolder.resultImg.setColorFilter(ContextCompat.getColor(context, R.color.getCol2), android.graphics.PorterDuff.Mode.MULTIPLY);




        final Consultation model = models.get(i);
        System.out.println(model);
        Picasso.with(context)
                .load(util.getDomaneName()+"/Content/Images/"+model.getImagePath()).into(viewHolder.image);

        //viewHolder.image.setImageResource(model.getImage());
        if(model.getType().equals("Not Psoriasis"))
        {
            viewHolder.pourcentage.setText(String.valueOf(100 -Math.round(Double.valueOf(model.getPourcentage().substring(0,model.getPourcentage().length()-1))))+"%");

        }
        else
        {
            viewHolder.pourcentage.setText(String.valueOf(Math.round(Double.valueOf(model.getPourcentage().substring(0,model.getPourcentage().length()-1))))+"%");

        }

        viewHolder.content_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toastMessage("delete");
                customDialog("Delete","You are sure!",model);
                return false;
            }
        });

      // viewHolder.pourcentage.setText(model.getPourcentage());

       // viewHolder.date.setText(model.getDate());
        viewHolder.btn_share.setOnClickListener(v -> {
            myDialog = new Dialog(context);
            ShowPopup(v,model);
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

             ImageView image;
             TextView date,pourcentage;
             ImageView resultImg, dateImg,btn_share;
             RelativeLayout content_view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.date);
            btn_share = itemView.findViewById(R.id.btn_share);
            resultImg = itemView.findViewById(R.id.result_img);
            dateImg = itemView.findViewById(R.id.date_img);
            pourcentage = itemView.findViewById(R.id.pourcentage);
            content_view = itemView.findViewById(R.id.content_hist);




        }
    }
    public void ShowPopup(View v,Consultation model) {

        System.out.println(model);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences= context.getSharedPreferences("x", Context.MODE_PRIVATE);
        myDialog.setContentView(R.layout.pop_share);
        recyclerView = myDialog.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        user_firebases = new ArrayList<>();

        getFollow(model);

        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(context.getApplicationContext(),
                R.anim.slider_down);

        Animation slide_up = AnimationUtils.loadAnimation(context.getApplicationContext(),
                R.anim.slider_up);
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


    private void readUsers(final Consultation model) {
        System.out.println("model2: "+model);
        final String URL = util.getDomaneName() + "/api/Users/";
        System.out.println("URLLLL:  "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {

                           // user_firebases.clear();

                            //  System.out.println(o.toString()+" respoce!!!!++++");

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
                                for (Subscriber subscriber : subscribers) {
                                    if (subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id), 0) && subscriber.getMedceinId() == user.getId()&& subscriber.getEtat() == 1)
                                    {
                                        user_firebases.add(user);

                                    }
                                }
//                                if(user.getId()!= mPreferences.getInt(context.getString(R.string.Id),0)){
//                                    user_firebases.add(user);
//                                }
                            }

                            userAdapter = new UserAdapter(context,user_firebases,false,"Home",model.getDateC().substring(0,10) +" a "+model.getDateC().substring(11,16),model.getImagePath(),model.getId());
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

        RequestQueue queue = Volley.newRequestQueue(context);
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
    private void getFollow(final Consultation model){
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
                            readUsers(model);
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

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);


    }
    public void customDialog(String title, String message, final Consultation modelFeed){
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toastMessage("Supprition annuler");
                    }
                });

        builderSingle.setPositiveButton(
                "Supprimer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: OK Called.");
                        RequestQueue queue =Volley.newRequestQueue(context);
                        String URI =util.getDomaneName()+"/api/Consultations/"+modelFeed.getId();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, URI, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println(response+" respoce!!!!++++");

                                        toastMessage("Superimer");
                                        removeItem( modelFeed);
                                        //setFragment(new FilDactualiteActivity());
//




                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // toastMessage(error.toString());
                                        System.out.println(error+"erreuuuuuuuuuuuurr");
                                        toastMessage("Superimer");
                                        removeItem( modelFeed);
                                        // setFragment(new FilDactualiteActivity());



                                    }
                                });





                        queue.add(stringRequest);


                    }
                });


        builderSingle.show();
    }
    private void  removeItem(Consultation modelFeed){
        int position = models.indexOf(modelFeed);
        models.remove(position);
        notifyItemRemoved(position);
    }
    public void toastMessage(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
