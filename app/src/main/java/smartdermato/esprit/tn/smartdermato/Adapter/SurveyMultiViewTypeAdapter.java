package smartdermato.esprit.tn.smartdermato.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import smartdermato.esprit.tn.smartdermato.Entities.Question;
import smartdermato.esprit.tn.smartdermato.R;


public class SurveyMultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Question> dataSet;
    private Context mContext;

    private String host;

    public static final int FIRST_TYPE=0;
    public static final int SLIDER_TYPE=1;
    public static final int RADIO_TYPE=2;


    private int result;


    public static final String TAG = "Comment";



    public class FirstTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView question;
        public RadioButton radYes,radNo;
        public RadioGroup radGroup;


        FirstTypeViewHolder(View itemView) {
            super(itemView);

            this.question = itemView.findViewById(R.id.question_text);
            this.radYes = itemView.findViewById(R.id.radio_yes);
            this.radNo = itemView.findViewById(R.id.radio_no);
            this.radGroup = itemView.findViewById(R.id.rad_group);
        }
    }


    public class RadioTypeViewHolder extends RecyclerView.ViewHolder {

        public TextView question;
        public RadioButton radYes,radNo;
        public RadioGroup radGroup;

        RadioTypeViewHolder(View itemView) {
            super(itemView);

            this.question = itemView.findViewById(R.id.question_text);
            this.radYes = itemView.findViewById(R.id.radio_yes);
            this.radNo = itemView.findViewById(R.id.radio_no);

            this.radGroup = itemView.findViewById(R.id.rad_group);
        }
    }

    public class SliderTypeViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        AppCompatSeekBar slider;


        SliderTypeViewHolder(View itemView) {
            super(itemView);

            this.question = itemView.findViewById(R.id.question_text);
            this.slider = itemView.findViewById(R.id.slider);
        }
    }


    public SurveyMultiViewTypeAdapter(ArrayList<Question> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case FIRST_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
                return new FirstTypeViewHolder(view);
            case RADIO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
                return new RadioTypeViewHolder(view);
            case SLIDER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_slider_item, parent, false);
                return new SliderTypeViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
                return new RadioTypeViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        int i = dataSet.get(position).getType();
        if (i==0) {
            return FIRST_TYPE;
        } else if (i==1){
            return SLIDER_TYPE;
        }
        else
            return RADIO_TYPE;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {

        Question object = dataSet.get(listPosition);

        if (object != null) {

            Question q = dataSet.get(listPosition);

            if (object.getType() == 0) {



                ((FirstTypeViewHolder) holder).question.setText(object.getQuestion());

                ((FirstTypeViewHolder) holder).radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_no:
                                // do operations specific to this selection

                                q.setResponse(0);
                                dataSet.set(listPosition,q);
                            case R.id.radio_yes:
                                // do operations specific to this selection

                                q.setResponse(1);
                                dataSet.set(listPosition,q);
                        }
                    }
                });

            }

            else if (object.getType() == 1) {

                ((SliderTypeViewHolder) holder).question.setText(object.getQuestion());
                ((SliderTypeViewHolder) holder).slider.setProgress(0);
                ((SliderTypeViewHolder) holder).slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        q.setResponse(progress);
                        dataSet.set(listPosition,q);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }

            else {

                ((RadioTypeViewHolder) holder).question.setText(object.getQuestion());



                ((RadioTypeViewHolder) holder).radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.radio_no:
                                q.setResponse(0);
                                dataSet.set(listPosition,q);

                            case R.id.radio_yes:
                                q.setResponse(1);
                                dataSet.set(listPosition,q);
                        }
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    public int getResult(int pos) {
        return dataSet.get(pos).getResponse();
    }




}