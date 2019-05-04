package smartdermato.esprit.tn.smartdermato.Service;

import android.content.Context;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Adapter.UserAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class UsersService {

    public List<Users> getUsers(final Context context, final List<Users> users, final int id) {
        final String URL = util.getDomaneName() + "/api/Users/";
        System.out.println("URLLLL:  "+ URL);
        final List<Users> usersList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response+" respoce!!!!++++");

                        try {
                            if(usersList.size()>0)
                            usersList.clear();

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
                                System.out.println("user service"+user);


                                System.out.println(o.getString("role"));
                                if(user.getId()!= id && user.getRole().equals("medecin")){
                                    usersList.add(user);
                                }
                            }




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

        System.out.println("users list "+usersList);
        return usersList;
    }

}
