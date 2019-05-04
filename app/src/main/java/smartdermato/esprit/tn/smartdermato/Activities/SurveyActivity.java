package smartdermato.esprit.tn.smartdermato.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import smartdermato.esprit.tn.smartdermato.Adapter.SurveyMultiViewTypeAdapter;
import smartdermato.esprit.tn.smartdermato.Entities.Question;
import smartdermato.esprit.tn.smartdermato.R;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_list);


        RecyclerView recyclerView = findViewById(R.id.survey_lst);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        ArrayList<Question> items = new ArrayList<>();

        Question item1 = new Question(1,"Does the affected spot usually get red ?", 2);
        Question item2= new Question(2,"Does the affected spot get dry and is there skin scaling, or shedding ?", 1);
        Question item3 = new Question(3,"Does the affected area have definite boders ?", 0);
        Question item4 = new Question(4,"Is there any itching in the surroundings or in the inside of the affected area ?", 1);
        Question item5 = new Question(4,"Are there any skin lesions ?", 1);
        Question item6 = new Question(4,"Are there a well defined area of purple-coloured, itchy, flat-topped papules with interspersed lacy white lines ?", 1);
        Question item7 = new Question(5,"test", 2);

        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);

        SurveyMultiViewTypeAdapter adapterAccueil = new SurveyMultiViewTypeAdapter(items, getApplicationContext());
        recyclerView.setAdapter(adapterAccueil);

        adapterAccueil.notifyDataSetChanged();

    }
}
