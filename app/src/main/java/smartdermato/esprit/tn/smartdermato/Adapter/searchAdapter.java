package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import smartdermato.esprit.tn.smartdermato.Activities.ShowProfile;
import smartdermato.esprit.tn.smartdermato.Entities.Subscriber;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

class SearchViewHolder extends RecyclerView.ViewHolder{
    public TextView name,address,email,phone,country;
    public ImageView subscriber;
    public CircleImageView imgDR;
    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.firstname);
        address = (TextView) itemView.findViewById(R.id.address);
        phone = (TextView) itemView.findViewById(R.id.phone);
        email = (TextView) itemView.findViewById(R.id.email);
        country = (TextView) itemView.findViewById(R.id.city);
        subscriber = (ImageView) itemView.findViewById(R.id.btn_subscriber);
        imgDR = (CircleImageView) itemView.findViewById(R.id.imgView_proPic);

    }
}
public class searchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private List<Users> users;
    private List<Subscriber> subscribers;
    private SharedPreferences mPreferences;
    private static final String TAG = "searchAdapter";

    public searchAdapter(Context context, List<Users> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.layout_item,viewGroup,false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences= context.getSharedPreferences("x", Context.MODE_PRIVATE);
        subscribers = new ArrayList<>();
        return new SearchViewHolder(itemView);
    }
    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }
    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder, int i) {
        final Users user = users.get(i);
        System.out.println("adapter "+user);
        if(!user.getUser_pic().equals("vide_pic"))
        {

            Picasso.with(context)
                    .load(util.getDomaneName()+"/Content/Images/"+user.getUser_pic()).into(searchViewHolder.imgDR);


        }
        searchViewHolder.imgDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage(mPreferences.getString(context.getString(R.string.role),""));
                if(mPreferences.getString(context.getString(R.string.role),"").equals("patient") ) {
                    Intent intent = new Intent(context, ShowProfile.class);
                    intent.putExtra("user_id", user.getId());
                    context.startActivity(intent);
                }
            }
        });
        if(user.getStatus().equals("online")){
            searchViewHolder.imgDR.setBorderColorResource(R.color.on);

        }
        else
        {
            searchViewHolder.imgDR.setBorderColorResource(R.color.off);

        }
        searchViewHolder.imgDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowProfile.class);
                intent.putExtra("user_id",user.getId());
                context.startActivity(intent);
            }
        });
        searchViewHolder.name.setText(user.getFirstName()+" "+user.getLastName());
        searchViewHolder.email.setText(user.getEmail());
        searchViewHolder.phone.setText("("+user.getCountryOfficeNumber()+") "+user.getOfficeNumber());
        searchViewHolder.address.setText(user.getOfficeAddess()+", "+user.getCity()+" "+user.getPostalCode());
        searchViewHolder.country.setText(user.getCountry());
        getFollow(user.getId(),searchViewHolder);
        searchViewHolder.subscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("list Subscriber: "+subscribers.size());
                if(subscribers.size() == 0)
                {
                    Subscriber subscriber0 = new Subscriber();
                    subscriber0.setPationId(mPreferences.getInt(context.getString(R.string.Id),0));
                    subscriber0.setMedceinId(user.getId());
                    subscriber0.setEtat(1);
                    System.out.println(subscriber0);

                    postFollow(subscriber0,searchViewHolder);
                }
                else
                {
                    for (Subscriber subscriber : subscribers)
                    {

                        System.out.println("for subscribe: "+subscriber);
                        if(subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id),0) && subscriber.getMedceinId() == user.getId())
                        {
                            System.out.println("for subscribe2: "+subscriber);
                            if(subscriber.getEtat() == 1)
                            {
                                Subscriber subscriber0 = new Subscriber();
                                subscriber0.setPationId(subscriber.getPationId());
                                subscriber0.setMedceinId(subscriber.getMedceinId());
                                subscriber0.setEtat(0);

                                putFollow(subscriber,subscriber0,searchViewHolder);
                                break;
                            }
                            else
                            {

                                Subscriber subscriber0 = new Subscriber();
                                subscriber0.setPationId(subscriber.getPationId());
                                subscriber0.setMedceinId(subscriber.getMedceinId());
                                subscriber0.setEtat(1);

                                putFollow(subscriber,subscriber0,searchViewHolder);
                                break;
                            }
                        }
                        else
                        {
                            Subscriber subscriber0 = new Subscriber();
                            subscriber0.setPationId(mPreferences.getInt(context.getString(R.string.Id),0));
                            subscriber0.setMedceinId(user.getId());
                            subscriber0.setEtat(1);
                            System.out.println(subscriber0);

                            postFollow(subscriber0,searchViewHolder);
                         }
                    }
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    private void putFollow(final Subscriber subscribero, final Subscriber subscriber, final SearchViewHolder searchViewHolder)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            if(subscriber.getEtat()== 1)
                            {
                                searchViewHolder.subscriber.setImageResource(R.drawable.useralready);

                            }
                            else
                            {
                                searchViewHolder.subscriber.setImageResource(R.drawable.userfollow);

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
    private void postFollow(Subscriber subscriber,final SearchViewHolder searchViewHolder)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String URI = util.getDomaneName()+"/api/Subscribers";
        System.out.println(URI);
        Map<String,Object> paramsToken = new HashMap<String, Object>();
        paramsToken.put("pationId",subscriber.getPationId());
        paramsToken.put("medecinId",subscriber.getMedceinId());
        paramsToken.put("etat",subscriber.getEtat());
        System.out.println("Follow: "+new JSONObject(paramsToken));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URI, new JSONObject(paramsToken),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response+" Token Out respoce!!!!++++");

                        try {

                            JSONObject o = response;
                            Subscriber subscriber = new Subscriber();
                            subscriber.setPationId(o.getInt("pationId"));
                            subscriber.setMedceinId(o.getInt("medecinId"));
                            subscriber.setEtat(o.getInt("etat"));
                            subscribers.add(subscriber);
                            if(o.getInt("etat") == 1)
                            {
                                searchViewHolder.subscriber.setImageResource(R.drawable.useralready);

                            }
                            else
                            {
                                searchViewHolder.subscriber.setImageResource(R.drawable.userfollow);

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
    private void getFollow(int idM, final SearchViewHolder searchViewHolder){
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
                                System.out.println("boolean test :  "+String.valueOf(subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id),0)));
                                System.out.println("boolean test0 :  "+String.valueOf(subscriber.getEtat() == 1 && subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id),0)));
                                System.out.println("boolean test1 :  "+ subscriber.getEtat());

                                if(subscriber.getEtat() == 1 && subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id),0) && subscriber.getMedceinId() == idM)
                                {
                                    System.out.println("okkkkkkk");
                                    searchViewHolder.subscriber.setImageResource(R.drawable.useralready);
                                    System.out.println("okkkkkkk22");

                                }
                                if(subscriber.getEtat() == 0 && subscriber.getPationId() == mPreferences.getInt(context.getString(R.string.Id),0) && subscriber.getMedceinId() == idM)
                                {
                                    searchViewHolder.subscriber.setImageResource(R.drawable.userfollow);



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

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);


    }
    public void toastMessage(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
