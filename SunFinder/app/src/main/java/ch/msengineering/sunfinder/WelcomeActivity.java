package ch.msengineering.sunfinder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.value;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.welcomeFAB);
        final EditText editText = (EditText) findViewById(R.id.desiredLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change activity, pass the value from the text view
                Intent myIntent = new Intent(view.getContext(), GeoListActivity.class);
                myIntent.putExtra("desiredLocation", editText.getText().toString()); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });
    }
}
