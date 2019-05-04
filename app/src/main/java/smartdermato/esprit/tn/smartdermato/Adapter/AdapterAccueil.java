package smartdermato.esprit.tn.smartdermato.Adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import smartdermato.esprit.tn.smartdermato.Entities.Model;
import smartdermato.esprit.tn.smartdermato.Entities.User_Firebase;
import smartdermato.esprit.tn.smartdermato.R;
import smartdermato.esprit.tn.smartdermato.databinding.ViewContentsBinding;

public class AdapterAccueil extends PagerAdapter {
    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewContentsBinding mBinding;
    private Dialog myDialog;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User_Firebase> user_firebases;
    private ImageView image;
    private TextView date,time;
    private ImageButton btn_share;
    public AdapterAccueil(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_contents,container,false);

        image = view.findViewById(R.id.image);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        btn_share = view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myDialog = new Dialog(context.getApplicationContext());

                ShowPopup(v);


            }
        });
        image.setImageResource(models.get(position).getImage());
        date.setText(models.get(position).getDate());
        time.setText(models.get(position).getTime());
        container.addView(view,0);
    return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
//    public void ShowPopupShare() {
//
//        myDialog = new Dialog(context);
//      //  myDialog = new Dialog(context.getApplicationContext());
//        myDialog.setContentView(R.layout.pop_share);
//        recyclerView = myDialog.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//
//        user_firebases = new ArrayList<>();
//     //   readUsers();
//        //Load animation
//        Animation slide_down = AnimationUtils.loadAnimation(context.getApplicationContext(),
//                R.anim.slider_down);
//
//        Animation slide_up = AnimationUtils.loadAnimation(context.getApplicationContext(),
//                R.anim.slider_up);
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(myDialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.BOTTOM;
//        lp.windowAnimations = R.style.DialogSlider;
//        myDialog.getWindow().setAttributes(lp);
//        myDialog.show();
//
//    }
//    private void readUsers() {
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
//                userAdapter = new UserAdapter(context,user_firebases,false);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void ShowPopup(View v) {
        myDialog.setContentView(R.layout.pop_share);


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

    public void toastMessage(String message){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
}

}
