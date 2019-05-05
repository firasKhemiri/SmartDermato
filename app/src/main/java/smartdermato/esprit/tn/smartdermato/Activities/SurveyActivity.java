package smartdermato.esprit.tn.smartdermato.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import smartdermato.esprit.tn.smartdermato.Adapter.SurveyMultiViewTypeAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Question;
import smartdermato.esprit.tn.smartdermato.R;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_list);

        FrameLayout submit = findViewById(R.id.btn_survey);


        RecyclerView recyclerView = findViewById(R.id.survey_lst);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        ArrayList<Question> items = new ArrayList<>();

        Question item1 = new Question(1,"Does the affected spot usually get red ?", 1,0);
        Question item2= new Question(2,"Does the affected spot get dry and is there skin scaling, or shedding ?", 1,0);
        Question item3 = new Question(3,"Does the affected area have definite boders ?", 1,0);
        Question item4 = new Question(4,"Is there any itching in the surroundings or in the inside of the affected area ?", 1,0);


        Question item5 = new Question(5,"Are there any skin lesions ?", 1,0);
        Question item6 = new Question(6,"Are there any well defined areas of purple-colored, itchy, flat-topped papules with interspersed lacy white lines ?", 1,0);
        Question item7 = new Question(7,"Is there any acne near the infected area ?", 1,0);
        Question item8 = new Question(8,"Does your mouth get dry ?", 1,0);
        Question item9 = new Question(9,"Does the infection happen to exist on your knees, elbows or both ?", 1,0);
        Question item10 = new Question(10,"Does the infection happen to be on the scalp ?", 1,0);
        Question item11 = new Question(11,"Is there any family history of psoriasis ?", 2,0);
        Question item12 = new Question(12,"Does the tissue under the infected area hurt ( muscle pain )?", 1,0);
        Question item13 = new Question(13,"Are there any yellow spots near or on top of thefected spot ?", 1,0);
        Question item14 = new Question(14,"Are there any black or brown, poorly defined spots in the body's folds ( such as the posterior and lateral folds of the neck, the armpits, groin, navel, forehead .. )", 1,0);
        Question item15 = new Question(15,"Are there any white scales ( Calluses ) on the affected area ?", 1,0);
        Question item16 = new Question(16,"Does the the skin on the affected area show impressive growth and dilation ?", 1,0);
        Question item17 = new Question(17,"Is there any hair loss in the affected area ? ", 1,0);
        Question item18 = new Question(18,"Do you get constant headaches from time to time?", 1,0);
        Question item19 = new Question(19,"Does the infected area show abnormal accumulation of fluids ?", 1,0);
        Question item20 = new Question(20,"Does the physical state of the affected area keep on changing ?", 1,0);
        Question item21 = new Question(21,"Is there any horn shaped tissue on the affected area ?", 1,0);
        Question item22 = new Question(21,"Does the affected area takes a lot of time to heal ?", 1,0);


        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);
        items.add(item9);
        items.add(item10);
        items.add(item11);
        items.add(item12);
        items.add(item13);
        items.add(item14);
        items.add(item15);
        items.add(item16);
        items.add(item17);
        items.add(item18);
        items.add(item19);
        items.add(item20);
        items.add(item21);
        items.add(item22);

        SurveyMultiViewTypeAdapter adapterAccueil = new SurveyMultiViewTypeAdapter(items, getApplicationContext());
        recyclerView.setAdapter(adapterAccueil);

        adapterAccueil.notifyDataSetChanged();








        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();

                for (int i = 0; i < size; i++) {
                    int type = recyclerView.getAdapter().getItemViewType(i);



                    Toast.makeText(getApplicationContext(),"pos: "+i+ "test rad: "+adapterAccueil.getResult(i),Toast.LENGTH_SHORT).show();
                    System.out.println("pos: "+i+ "test rad: "+adapterAccueil.getResult(i));

                   /* if (type == SurveyMultiViewTypeAdapter.FIRST_TYPE) {

                        View view = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i)).itemView;
                        RadioGroup radioGroup = view.findViewById(R.id.rad_group);


                        int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();

                        // If nothing is selected from Radio Group, then it return -1
                        if (selectedRadioButtonID != -1) {

                            RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonID);
                            String selectedRadioButtonText = selectedRadioButton.getText().toString();

                            Toast.makeText(getApplicationContext(),"test rad: "+selectedRadioButtonText,Toast.LENGTH_LONG).show();
                        }

                    }

                    else if(type == SurveyMultiViewTypeAdapter.SLIDER_TYPE)
                    {
                        View view = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i)).itemView;
                        SeekBar slider = view.findViewById(R.id.slider);

                        Toast.makeText(getApplicationContext(),"test slider: "+slider.getProgress(),Toast.LENGTH_LONG).show();
                    }*/

                }

            }
        });
    }



}
