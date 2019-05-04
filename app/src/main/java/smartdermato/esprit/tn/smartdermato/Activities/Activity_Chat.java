package smartdermato.esprit.tn.smartdermato.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import smartdermato.esprit.tn.smartdermato.Entities.ChatMessage;
import smartdermato.esprit.tn.smartdermato.R;

import static android.app.Activity.RESULT_OK;

public class Activity_Chat extends Fragment {
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    private RelativeLayout Activity_Chat;
    private FloatingActionButton fab;
    private ListView listOfMessage;
    private EditText input;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if(item.getItemId() == R.id.menu_sign_out){
          AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  Snackbar.make(Activity_Chat,"You have been signed out.",Snackbar.LENGTH_SHORT).show();
                  getActivity().finish();
              }
          });
      }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE){
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(Activity_Chat,"Successfully signed in.Welcome!",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(Activity_Chat,"We couldn't sign you in Please try again later",Snackbar.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.chat, container, false);
        Activity_Chat = getActivity().findViewById(R.id.content);
                //(RelativeLayout) root.findViewById(R.id.activity_chat);
       // fab = (FloatingActionButton) root.findViewById(R.id.fab_send);
        listOfMessage= (ListView)root.findViewById(R.id.list_of_message);
         input = (EditText) root.findViewById(R.id.text_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("inputtttttt "+input.getText());
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();

        }
        displayChatMessage();
        return root;
    }

    private void displayChatMessage() {


        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                System.out.println("Model: "+ model.getMessageText()+"       "+model.getMessageUser()+"             "+model.getMessageTime());

                TextView messageText,messageUser,messageTime;
                messageText = (TextView)  v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);
                System.out.println("Model: "+ model.getMessageText()+"       "+model.getMessageUser()+"             "+model.getMessageTime());
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessage.setAdapter(adapter);

    }
}
