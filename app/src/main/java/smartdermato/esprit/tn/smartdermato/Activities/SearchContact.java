package smartdermato.esprit.tn.smartdermato.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartdermato.esprit.tn.smartdermato.Adapter.searchAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Users;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.DatabaseHelper;
import smartdermato.esprit.tn.smartdermato.Util.util;

public class SearchContact extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private smartdermato.esprit.tn.smartdermato.Adapter.searchAdapter searchAdapter;
    private MaterialSearchBar materialSearchBar;
    private DatabaseHelper database;
    private SharedPreferences mPreferences;
    private List<Users> usersList = new ArrayList<>();
    private List<String> suggestList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.search_medcin, container, false);
        Context context = getActivity().getApplicationContext();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        mPreferences= getActivity().getSharedPreferences("x", Context.MODE_PRIVATE);

            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_search);
        materialSearchBar = (MaterialSearchBar) root.findViewById(R.id.search_bar);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        database = new DatabaseHelper(getActivity());

        materialSearchBar.setHint("Search doctor contact");

        materialSearchBar.setCardViewElevation(10);
        loadSuggestList();
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    searchAdapter = new searchAdapter(getActivity().getBaseContext(),database.getAllUsers());
                    recyclerView.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
//        getDRS();
        searchAdapter = new searchAdapter(getActivity(),database.getAllUsers());
        recyclerView.setAdapter(searchAdapter);
        return root;
    }
    private void startSearch(String text){
        searchAdapter = new searchAdapter(getActivity(),database.getUserByName(text));
        recyclerView.setAdapter(searchAdapter);

    }
    private void loadSuggestList(){
        suggestList = database.getName();
        materialSearchBar.setLastSuggestions(suggestList);
    }
    public void toastMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }
    private void getDRS(){
//          database.addContactMedecin("a","z","a","a","a","a","a","a","a");
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
                                user.setCertificatDate(o.getString("Certificatdate"));
                                user.setCountry(o.getString("Country"));
                                user.setCity(o.getString("City"));
                                user.setPostalCode(o.getString("PostalCode"));
                                user.setOfficeAddess(o.getString("OfficeAdress"));
                                user.setOfficeNumber(o.getString("OfficeNumber"));
                                System.out.println("user service"+user);


                                System.out.println(o.getString("role"));
                                if(user.getId()!= mPreferences.getInt(getString(R.string.Id),0) && user.getRole().equals("medecin")){
                                    if(user.getFirstName().isEmpty())
                                        System.out.println("null bb");
                                    database.addContactMedecin(user.getFirstName(),user.getLastName(),user.getUser_pic(),user.getOfficeNumber(),user.getOfficeAddess(),user.getPostalCode(),user.getCity(),user.getCountry(),user.getEmail(),user.getId(),user.getStatus(),user.getCountryOfficeNumber());
                                }
                            }

//                            System.out.println("database get: "+database.getAllUsers());




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
    }

}
