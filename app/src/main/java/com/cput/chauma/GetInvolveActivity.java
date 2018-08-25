package com.cput.chauma;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.shaun.chauma.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Objects;

/**
 * Application form for individuals to apply in order to become
 * volunteer peer educators.
 *
 * @author  Kelvin Van Sittert
 * @version 1.0
 * @since   2018-01-31
 */
public class GetInvolveActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;  //This is the  layout for the navigation bar
    private ActionBarDrawerToggle actionBarDrawerToggle; //This is the button that will be used to show and hide Navigation bar
    private Toolbar toolbar;    //This instance is for the navigation toolbar
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_involve_activity);

        toolbar = findViewById(R.id.navigation_action_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.getInvolveActivityDrawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //removing the title name(in this case is was the app name)

        final NavigationView navigationView = findViewById(R.id.navigationViewLayout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        openActivity("HomeActivity");break;
                    case R.id.clinic:
                        Toast.makeText(getApplicationContext(), "Clinic", Toast.LENGTH_SHORT).show();
                        openActivity("ClinicActivity");break;
                    case R.id.brochure:
                        Toast.makeText(getApplicationContext(), "Brochure", Toast.LENGTH_SHORT).show();
                        openActivity("BrochureActivity");break;
                    case R.id.events:
                        Toast.makeText(getApplicationContext(), "Events", Toast.LENGTH_SHORT).show();
                        openActivity("EventActivity");break;
                    case R.id.faq:
                        Toast.makeText(getApplicationContext(), "FAQ", Toast.LENGTH_SHORT).show();
                        openActivity("FrequentlyAskedQuestionActivity");break;
                    case R.id.getInvolve:
                        Toast.makeText(getApplicationContext(), "GetInvolveActivity", Toast.LENGTH_SHORT).show();
                        openActivity("GetInvolveActivity");break;
                    case R.id.contacts:
                        Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
                        openActivity("ContactActivity");break;
                }
                return true;
            }
        });

        Button apply = findViewById(R.id.btnPeerApply); //to home page
        apply.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   EditText txtPeerEmailAddress = findViewById(R.id.txtPeerEmailAddress);
                   EditText txtPeerName =  findViewById(R.id.txtPeerName);
                   EditText txtPeerSurname =  findViewById(R.id.txtPeerSurname);
                   EditText txtPeerIdNumber =  findViewById(R.id.txtPeerIdNumber);
                   EditText txtPeerContactNumber =  findViewById(R.id.txtPeerContactNumber);
                   EditText txtPeerCourse =  findViewById(R.id.txtPeerCourse);
                   EditText txtPeerYearOfStudy =  findViewById(R.id.txtPeerYearOfStudy);
                   EditText txtPeerStudentNumber =  findViewById(R.id.txtPeerStudentNumber);
                   RadioButton rdbMale =  findViewById(R.id.rdbMale);
                   RadioButton rdbFemale =  findViewById(R.id.rdbFemale);

                   PeerEducator peerEducator = new PeerEducator();
                   peerEducator.ContactNumber = txtPeerContactNumber.getText().toString();
                   peerEducator.Course = txtPeerCourse.getText().toString();
                   peerEducator.EmailAddress = txtPeerEmailAddress.getText().toString();
                   peerEducator.Gender = rdbFemale.isChecked() ? rdbFemale.getText().toString() : rdbMale.isChecked() ? rdbMale.getText().toString() : "Private";
                   peerEducator.IdNumber = txtPeerIdNumber.getText().toString();
                   peerEducator.Name = txtPeerName.getText().toString();
                   peerEducator.Surname = txtPeerSurname.getText().toString();
                   peerEducator.YearOfStudy = txtPeerYearOfStudy.getText().toString();
                   peerEducator.StudentNumber = txtPeerStudentNumber.getText().toString();
                   peerEducator.Password = "";
                   peerEducator.IsAuthorised = false;

                   db
                           .collection("PeerEducator")
                           .document(peerEducator.EmailAddress)
                           .set(peerEducator, SetOptions.merge());
                   openActivity("HomeActivity");

                   SendEmail(peerEducator);
                   Toast.makeText(getApplicationContext(), "Thank you!  Our Coordinator will contact you.", Toast.LENGTH_SHORT).show();
               }
               catch (Exception e){
                   Log.w("Failed Peer Counselor", "Error adding document", e);
               }
           }
       });



    }

    private void SendEmail(PeerEducator peerEducator) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        Log.i("Send email", "");
        String[] TO = {"jarrod.moura@gmail.com"};
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, peerEducator.Name + " wants to be a Peer Educator!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Coordinator.  I am interested in becoming a Peer Educator.  Please can you send me login details.");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished with email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.homeButton){
            Toast.makeText(getApplicationContext(), "Go back Home", Toast.LENGTH_SHORT).show();
            openActivity("HomeActivity");
            finish();
        }else if(item.getItemId() == R.id.loginButton){
            Toast.makeText(getApplicationContext(), "Admin Login", Toast.LENGTH_SHORT).show();
            openActivity("LoginActivity");
            finish();
        }
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void openActivity(String value){
        switch (value){
            case "HomeActivity":
                Intent homeActivity = new Intent(this, HomeActivity.class);
                startActivity(homeActivity);
                finish();break;
            case "ClinicActivity":
                Intent clinicActivity = new Intent(this, ClinicActivity.class);
                startActivity(clinicActivity);
                finish();break;
            case "BrochureActivity":
                Intent brochureActivity = new Intent(this, BrochureActivity.class);
                startActivity(brochureActivity);
                finish();break;
            case "EventActivity":
                Intent eventActivity = new Intent(this, EventActivity.class);
                startActivity(eventActivity);
                finish();break;
            case "FrequentlyAskedQuestionActivity":
                Intent faqActivity = new Intent(this, FrequentlyAskedQuestionActivity.class);
                startActivity(faqActivity);
                finish();break;
            case "GetInvolveActivity":
                Intent getInvolveActivity = new Intent(this, GetInvolveActivity.class);
                startActivity(getInvolveActivity);
                finish();break;
            case "ContactActivity":
                Intent contactActivity = new Intent(this, ContactActivity.class);
                startActivity(contactActivity);
                finish();break;
            case "LoginActivity":
                Intent loginActivity = new Intent(this, LoginActivity.class);
                startActivity(loginActivity);
                finish();break;
        }
    }
}