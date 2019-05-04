package smartdermato.esprit.tn.smartdermato.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Objects;

import smartdermato.esprit.tn.smartdermato.Entities.Model;
import smartdermato.esprit.tn.smartdermato.R;

public class AdapterHistory extends PagerAdapter {
    private List<Model> models;
    private Context context;
    private Dialog myDialog;


    public AdapterHistory(List<Model> models, Context context) {
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_contents, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time);
        TextView result = view.findViewById(R.id.result);
        ImageButton btn_share = view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(v -> {


            myDialog = new Dialog(context.getApplicationContext());

            ShowPopup(v);


        });
        image.setImageResource(models.get(position).getImage());
        date.setText(models.get(position).getDate());
        time.setText(models.get(position).getTime());

        //TODO change Result
        result.setText("Psoriasis");

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void ShowPopup(View v) {
        myDialog.setContentView(R.layout.pop_share);


        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(context.getApplicationContext(),
                R.anim.slider_down);

        Animation slide_up = AnimationUtils.loadAnimation(context.getApplicationContext(),
                R.anim.slider_up);
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(myDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlider;
        myDialog.getWindow().setAttributes(lp);
        myDialog.show();
    }

    public void toastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
