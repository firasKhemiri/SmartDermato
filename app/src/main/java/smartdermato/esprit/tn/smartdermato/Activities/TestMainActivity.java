package smartdermato.esprit.tn.smartdermato.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import smartdermato.esprit.tn.smartdermato.R;

public class TestMainActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testactivity_main);

        ImageView img = findViewById(R.id.test_img);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.brochure).dontAnimate()
                .error(R.drawable.brochure)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.NORMAL);

        Glide.with(getApplicationContext())
                .load(R.drawable.share)
                .apply(options)
                .into(img);

        img.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.com_facebook_blue), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

}
