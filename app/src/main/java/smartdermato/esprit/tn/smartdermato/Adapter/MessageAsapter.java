package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.peekview.PeekViewActivity;
import com.klinker.android.peekview.builder.Peek;
import com.klinker.android.peekview.builder.PeekViewOptions;
import com.klinker.android.peekview.callback.SimpleOnPeek;
import com.squareup.picasso.Picasso;

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
    if(chat.getImageName()>0)
    {
        viewHolder.cardView.setVisibility(View.VISIBLE);
        viewHolder.imageSend.setImageResource(chat.getImageName());
        Peek.into(R.layout.image_details, new SimpleOnPeek() {
            @Override
            public void onInflated(View rootView) {
                // rootView is the layout inflated from R.layout.image_peek
                ((ImageView) rootView.findViewById(R.id.image))
                        .setImageDrawable(mContext.getResources().getDrawable(chat.getImageName()));
            }
        }).applyTo((PeekViewActivity) mContext, viewHolder.imageSend);

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
    public void toastMessage(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

}
