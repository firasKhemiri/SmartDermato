package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdermato.esprit.tn.smartdermato.Entities.Consultation;
import smartdermato.esprit.tn.smartdermato.Entities.FeedItem;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.Util.util;


public class HomeMultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FeedItem> dataSet;
    private Context mContext;
    private SharedPreferences mPreferences;

    private String host;

    private static final int TEXT_TYPE=0;
    private static final int IMAGE_TYPE=1;
    private static final int FIRST_TYPE=2;


    public static final String TAG = "Comment";







    private class FirstTypeViewHolder extends RecyclerView.ViewHolder {

        private AdapterHome adapterAccueil;
        private RecyclerView recyclerView;
        private LinearLayoutManager manager;

        private List<Consultation> models;

        FirstTypeViewHolder(View itemView) {
            super(itemView);



        }
    }


    private class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType, username, likes,date;
        ImageButton addLike,addComment;
        CardView post;
        CircleImageView profimg;


        TextTypeViewHolder(View itemView) {
            super(itemView);
            mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mPreferences = mContext.getSharedPreferences("x", Context.MODE_PRIVATE);
            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);
            this.addLike = itemView.findViewById(R.id.add_like);
            this.likes = itemView.findViewById(R.id.likes);
            this.addComment = itemView.findViewById(R.id.add_comment);
            this.post = itemView.findViewById(R.id.post_card);

            this.profimg = itemView.findViewById(R.id.profimg);


            this.date = itemView.findViewById(R.id.date);



            RequestOptions optionsImg = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.brochure).dontAnimate()
                    .error(R.drawable.brochure)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.NORMAL);


            post.setOnLongClickListener(v -> {
                CharSequence[] mine = new CharSequence[]{"Supprimer", "Modifier"};
                CharSequence[] notMine = new CharSequence[]{"Signaler"};
                CharSequence[] options;
                final FeedItem post =  dataSet.get(getAdapterPosition());

                if (post.getIdme() == post.getUser_id())
                {
                    options = mine;
                }
                else{
                    options=notMine;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if (which ==0 && post.getIdme() == post.getUser_id())
                        {
                            //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                            //   new Services().DeletePost(post.getId(),mContext);


                            dataSet.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                        }
                        else if(which == 1 && post.getIdme() == post.getUser_id())
                        {
                            // Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                       /*     Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                            intent.putExtra("is_picture",post.is_picture());
                            intent.putExtra("post_name",post.getPost_name());
                            intent.putExtra("post_id",post.getId());
                            intent.putExtra("cat_id",post.getCategory().getId());
                            intent.putExtra("cat_name",post.getCategory().getName());
                            intent.putExtra("cat_pic",post.getCategory().getPic_url());
                            mContext.startActivity(intent);*/
                        }


                    }
                });
                builder.show();
                return true;
            });


            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post = dataSet.get(getAdapterPosition());

                    //   Toast.makeText(mContext,post.getUser_id()+" ",Toast.LENGTH_LONG).show();

                    if (post.getUser_id() == post.getIdme()) {
                    /*    Intent intent = new Intent(mContext, ProfileViewPager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/
                    }
                    else {
                       /* Intent intent = new Intent(mContext, UserViewPager.class)
                                .putExtra("userid", post.getUser_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/
                    }

                }
            });

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem feedItem = dataSet.get(getAdapterPosition());

                    if (!feedItem.isLiked()) {
                        int i = feedItem.getId();
                        //  new Services().AddLike(String.valueOf(i), mContext);
                        feedItem.setLiked(true);

                        Glide.with(mContext)
                                .load(R.drawable.liked)
                                .apply(optionsImg)
                                .into(addLike);

                        feedItem.setNum_likes(feedItem.getNum_likes()+1);

                        notifyItemChanged(getAdapterPosition());

                        //  notifyDataSetChanged();
                    } else {
                        int i = feedItem.getId();
                        //     new Services().Unlike(String.valueOf(i), mContext);
                        feedItem.setLiked(false);

                        Glide.with(mContext)
                                .load(R.drawable.not_liked)
                                .apply(optionsImg)
                                .into(addLike);

                        feedItem.setNum_likes(feedItem.getNum_likes()-1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();

                    }
                }
            });

            addComment.setOnClickListener(v -> {


            });
        }
    }

    private class ImageTypeViewHolder extends RecyclerView.ViewHolder {



        RequestOptions optionsImg = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.brochure).dontAnimate()
                .error(R.drawable.brochure)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.NORMAL);




        TextView txtType, username,likes,date;
        ImageView image;
        ImageButton addLike,addComment;
        CardView post;

        CircleImageView profimg;

        ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = itemView.findViewById(R.id.postname);
            this.username = itemView.findViewById(R.id.username);
            this.image = itemView.findViewById(R.id.thumbnail);
            this.addLike = itemView.findViewById(R.id.add_like);
            this.likes = itemView.findViewById(R.id.likes);
            this.addComment = itemView.findViewById(R.id.add_comment);
            this.profimg = itemView.findViewById(R.id.profimg);
            this.date = itemView.findViewById(R.id.date);


            this.post = itemView.findViewById(R.id.post_card);



            //      l=dataSet.get(getAdapterPosition()).getNum_likes();



            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem post = dataSet.get(getAdapterPosition());

                    //   Toast.makeText(mContext, post.getUser_id() + " ", Toast.LENGTH_LONG).show();

                    if (post.getUser_id() == post.getIdme()) {
                  /*      Intent intent = new Intent(mContext, ProfileViewPager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/
                    }
                    else {
                   /*     Intent intent = new Intent(mContext, UserViewPager.class)
                                .putExtra("userid", post.getUser_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);*/
                    }

                }
            });

            addLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem feedItem = dataSet.get(getAdapterPosition());

                    if (!feedItem.isLiked()) {
                        int i = feedItem.getId();
                        //    new Services().AddLike(String.valueOf(i), mContext);
                        feedItem.setLiked(true);

                            Glide.with(mContext)
                                    .load(R.drawable.liked)
                                    .apply(optionsImg)
                                    .into(addLike);



                        feedItem.setNum_likes(feedItem.getNum_likes()+1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();
                    } else {
                        int i = feedItem.getId();
                        //    new Services().Unlike(String.valueOf(i), mContext);
                        feedItem.setLiked(false);

                        Glide.with(mContext)
                                .load(R.drawable.not_liked)
                                .apply(optionsImg)
                                .into(addLike);
                        feedItem.setNum_likes(feedItem.getNum_likes()-1);

                        notifyItemChanged(getAdapterPosition());
                        //notifyDataSetChanged();

                    }
                }
            });


          /*  image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transition(v , dataSet.get(getAdapterPosition()).getPic_url() );
                }
            });*/



            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence[] mine = new CharSequence[]{"Supprimer", "Modifier"};
                    CharSequence[] notMine = new CharSequence[]{"Signaler"};
                    CharSequence[] options;
                    final FeedItem post =  dataSet.get(getAdapterPosition());

                    if (post.getIdme() == post.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]

                            if (which ==0 && post.getIdme() == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                //           new Services().DeletePost(post.getId(),mContext);


                                dataSet.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                            }
                            else if(which == 1 && post.getIdme() == post.getUser_id())
                            {
                       /*         Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                                intent.putExtra("is_picture",post.is_picture());
                                intent.putExtra("thumbnail",post.getPic_url());
                                intent.putExtra("post_name",post.getPost_name());
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("cat_id",post.getCategory().getId());
                                intent.putExtra("cat_name",post.getCategory().getName());
                                intent.putExtra("cat_pic",post.getCategory().getPic_url());
                                mContext.startActivity(intent);*/
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });



            post.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CharSequence[] mine = new CharSequence[]{"Supprimer", "Modifier"};
                    CharSequence[] notMine = new CharSequence[]{"Signaler"};
                    CharSequence[] options;
                    final FeedItem post =  dataSet.get(getAdapterPosition());

                    if (post.getIdme() == post.getUser_id())
                    {
                        options = mine;
                    }
                    else{
                        options=notMine;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]

                            if (which ==0 && post.getIdme() == post.getUser_id())
                            {
                                //    Toast.makeText(mContext,"it's " +which,Toast.LENGTH_LONG).show();

                                //  new Services().DeletePost(post.getId(),mContext);


                                dataSet.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), dataSet.size());

                            }
                            else if(which == 1 && post.getIdme() == post.getUser_id())
                            {
                         /*       Intent intent = new Intent(mContext, UpdatePhotoActual.class);
                                intent.putExtra("is_picture",post.is_picture());
                                intent.putExtra("thumbnail",post.getPic_url());
                                intent.putExtra("post_name",post.getPost_name());
                                intent.putExtra("post_id",post.getId());
                                intent.putExtra("cat_id",post.getCategory().getId());
                                intent.putExtra("cat_name",post.getCategory().getName());
                                intent.putExtra("cat_pic",post.getCategory().getPic_url());
                                mContext.startActivity(intent);*/
                            }


                        }
                    });
                    builder.show();
                    return true;
                }
            });





            addComment.setOnClickListener(v -> {

            });
        }
    }


    public HomeMultiViewTypeAdapter(ArrayList<FeedItem> data, Context context) {
        this.dataSet = data;
        this.mContext = context;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case FIRST_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_history_listview, parent, false);
                return new FirstTypeViewHolder(view);
            case TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_row_no_pic, parent, false);
                return new TextTypeViewHolder(view);
            case IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_row_pic, parent, false);
                return new ImageTypeViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_row_no_pic, parent, false);
                return new TextTypeViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return FIRST_TYPE;

        else {
            boolean i = dataSet.get(position).is_picture();
            if (!i) {
                return TEXT_TYPE;
            } else {
                return IMAGE_TYPE;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {

        FeedItem object = dataSet.get(listPosition);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.brochure).dontAnimate()
                .error(R.drawable.brochure)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.NORMAL);



        if (listPosition == 0) {

            ((FirstTypeViewHolder) holder).recyclerView = ((FirstTypeViewHolder) holder).itemView.findViewById(R.id.recycler_view);
            ((FirstTypeViewHolder) holder).manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            ((FirstTypeViewHolder) holder).recyclerView.setLayoutManager(((FirstTypeViewHolder) holder).manager);
            ((FirstTypeViewHolder) holder).recyclerView.setHasFixedSize(true);


            ((FirstTypeViewHolder) holder).models = new ArrayList<>();
         /*   ((FirstTypeViewHolder) holder).models.add(new Model(R.drawable.brochure, "19/02/2019", "12h:59"));
            ((FirstTypeViewHolder) holder).models.add(new Model(R.drawable.sticker, "15-02-2019", "20h:09"));
            ((FirstTypeViewHolder) holder).models.add(new Model(R.drawable.poster, "19/01/2019", "10h:50"));
            ((FirstTypeViewHolder) holder).models.add(new Model(R.drawable.namecard, "02-12-2018", "00h:00"));*/
            ((FirstTypeViewHolder) holder).adapterAccueil = new AdapterHome(((FirstTypeViewHolder) holder).models, mContext);
            ((FirstTypeViewHolder) holder).recyclerView.setAdapter(((FirstTypeViewHolder) holder).adapterAccueil);
            ((FirstTypeViewHolder) holder).adapterAccueil.notifyDataSetChanged();
            getConsultation((FirstTypeViewHolder) holder);

        }


        else {

            if (object != null) {
                if (!object.is_picture()) {
                    ((TextTypeViewHolder) holder).txtType.setText(object.getPost_name());
                    ((TextTypeViewHolder) holder).username.setText(object.getUsername());
                    ((TextTypeViewHolder) holder).likes.setText(String.valueOf(object.getNum_likes()));

                util s = new util();
                try {
                    ((TextTypeViewHolder) holder).date.setText(s.convertDate(object.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                    FeedItem item = dataSet.get(listPosition);
                    if (item.isLiked())
                    {
                        Glide.with(mContext)
                                .load(R.drawable.liked)
                                .apply(options)
                                .into(((TextTypeViewHolder) holder).addLike);
                    }


                    else
                    {
                        Glide.with(mContext)
                                .load(R.drawable.not_liked)
                                .apply(options)
                                .into(((TextTypeViewHolder) holder).addLike);
                    }


                    Glide.with(mContext)
                            .load(host + "/" + object.getPhotoprof())
                            .apply(options)
                            .into(((TextTypeViewHolder) holder).profimg);




                    Glide.with(mContext)
                            .load(R.drawable.comment)
                            .apply(options)
                            .into(((TextTypeViewHolder) holder).addComment);

                } else {

                    if (!object.getPost_name().equals("")) {
                        ((ImageTypeViewHolder) holder).txtType.setText(object.getPost_name());
                        ((ImageTypeViewHolder) holder).txtType.setVisibility(View.VISIBLE);
                    } else {
                        ((ImageTypeViewHolder) holder).txtType.setVisibility(View.GONE);
                    }
                    ((ImageTypeViewHolder) holder).username.setText(object.getUsername());
                    ((ImageTypeViewHolder) holder).likes.setText(String.valueOf(object.getNum_likes()));



                    util s = new util();
                    try {
                        ((ImageTypeViewHolder) holder).date.setText(s.convertDate(object.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    FeedItem item = dataSet.get(listPosition);
                    if (item.isLiked())
                    {
                        Glide.with(mContext)
                                .load(R.drawable.liked)
                                .apply(options)
                                .into(((ImageTypeViewHolder) holder).addLike);
                    }


                    else
                    {
                        Glide.with(mContext)
                                .load(R.drawable.not_liked)
                                .apply(options)
                                .into(((ImageTypeViewHolder) holder).addLike);
                    }

                    Glide.with(mContext)
                            .load(host + "/" + object.getPic_url())
                            .apply(options)
                            .into(((ImageTypeViewHolder) holder).image);


                    Glide.with(mContext)
                            .load(host + "/" + object.getPhotoprof())
                            .apply(options)
                            .into(((ImageTypeViewHolder) holder).profimg);


                    Glide.with(mContext)
                            .load(R.drawable.comment)
                            .apply(options)
                            .into(((ImageTypeViewHolder) holder).addComment);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



  /*  private void transition(View view,String img) {
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(mContext, "21+ only, keep out", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, SingleImage.class);
            intent.putExtra("image", img);
          /*  ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext, view ,"");*/

        /*    ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, view, "");
            mContext.startActivity(intent);
        }
    }
*/
        public void getConsultation( RecyclerView.ViewHolder holder)
        {
            final String URL = util.getDomaneName() + "/api/Consultations";
            System.out.println("URLLLL:  "+ URL);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    System.out.println("Consultation: "+response );
                    try {

                        if (response.length() == 0 ) {

                            final androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                            builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                            builderSingle.setTitle("Home");
                            builderSingle.setMessage("Vide");
                            builderSingle.show();

                        } else {

                            if(((FirstTypeViewHolder) holder).models.size()>0)
                                ((FirstTypeViewHolder) holder).models.clear();
                            JSONArray array = new JSONArray(response);
                            for (int i = array.length() - 1; i >= 0; i--) {


                                JSONObject o = array.getJSONObject(i);
                                Consultation consultation = new Consultation();
                                consultation.setId(o.getInt("id"));
                                consultation.setDateC(o.getString("date"));
                                consultation.setImagePath(o.getString("imageName"));
                                consultation.setIdUser(o.getInt("patient"));
                                consultation.setType(o.getString("typeC"));
                                consultation.setPourcentage(o.getString("pourcentageC"));
                                if(consultation.getIdUser() == mPreferences.getInt(mContext.getString(R.string.Id),0))
                                {
                                    ((FirstTypeViewHolder) holder).models.add(consultation);
                                    ((FirstTypeViewHolder) holder).adapterAccueil.notifyDataSetChanged();

                                }
                            }

                        }
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
                    final androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    builderSingle.setIcon(R.drawable.ic_assignment_late_black_24dp);
                    builderSingle.setTitle("Login");
                    builderSingle.setMessage(message);
                    builderSingle.show();
                    return;

                }
            });

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(stringRequest);


        }

}