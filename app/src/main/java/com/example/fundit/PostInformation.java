package com.example.fundit;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class PostInformation extends AppCompatActivity {


    String  hisuid,ptime, myuid, myname, myemail, mydp, uimage, postId, plike, hisdp, hisname, uemail;
    String companyName,companyEmail,companyIndustry,companyFoundedYear,companyWebsite,companySize,companyAbout;
    ImageView picture, image;
    TextView cname,name, time, title, description;
    TextView c_name,c_email,c_industry,c_founded_year,c_website,c_size,c_about;
    ImageButton more;
    Button contactbtn, share;
    LinearLayout profile;
    ImageView imagep;
    ActionBar actionBar;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_information);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Post Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        /*-----------------Post Information-----------------------*/
        postId = getIntent().getStringExtra("pid");
        //recyclerView = findViewById(R.id.recyclecomment);
        picture = findViewById(R.id.pictureco);
        image = findViewById(R.id.pimagetvco);
        name = findViewById(R.id.unameco);
        time = findViewById(R.id.utimeco);
        cname=findViewById(R.id.companyName);

        title = findViewById(R.id.ptitleco);
        myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        description = findViewById(R.id.descriptco);
       // tcomment = findViewById(R.id.pcommenttv);
       // like = findViewById(R.id.plikebco);
        contactbtn = findViewById(R.id.contact);
      /*  comment = findViewById(R.id.typecommet);
        sendb = findViewById(R.id.sendcomment);
        imagep = findViewById(R.id.commentimge);
        share = findViewById(R.id.share);*/
        profile = findViewById(R.id.profilelayoutco);
        progressDialog = new ProgressDialog(this);
        loadPostInfo();
        loadUserInfo();
        hideContactButton();

        //setLikes();
        actionBar.setSubtitle("SignedInAs:" + myemail);


        /*-----------------Company Information-----------------------*/
        c_name=findViewById(R.id.company_name);
        c_email=findViewById(R.id.company_email);
        c_industry=findViewById(R.id.company_industry);
        c_founded_year=findViewById(R.id.company_founded_year);
        c_website=findViewById(R.id.company_website);
        c_size=findViewById(R.id.company_size);
        c_about=findViewById(R.id.company_about);




        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent=new Intent(Intent.ACTION_SEND);

                // Default Subject
                String defaultSubject = "Exploring Collaboration: "+companyName;

                // Default Body
                String defaultBody = "Dear "+hisname+",\n\n" +
                        "I hope this email finds you well. My name is "+myname+", and I am reaching out to express my interest in learning more about your startup, "+companyName+".\n\n" +
                        "I am an investor interested in exploring potential investment opportunities, and I believe that your startup has great potential. I would appreciate the opportunity to discuss this further with you and learn more about your vision and plans for the future.\n\n" +
                        "Please let me know if you would be available for a meeting or a call at your earliest convenience. I am excited about the prospect of potentially working together and contributing to the success of "+companyName+".\n\n" +
                        "Thank you for considering my inquiry. I look forward to hearing from you.\n\n" +
                        "Best regards,\n" +
                        myname+"\n";




                //email Address
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{uemail});
                //subject
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,defaultSubject);
                //body
                emailIntent.putExtra(Intent.EXTRA_TEXT,defaultBody);

                emailIntent.setType("message/rfc822");

                //chck if client email is available
                if(emailIntent.resolveActivity(getPackageManager())!=null)
                {
                    startActivity(Intent.createChooser(emailIntent,"Choose email client:"));
                }
                else
                {
                    Toast.makeText(PostInformation.this,"No email client found: "+uemail,Toast.LENGTH_LONG).show();
                }
            }
        });


        //To open website
        c_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PostInformation.this,Companywebsite.class);
                intent.putExtra("websiteUrl",companyWebsite);
                startActivity(intent);
            }
        });
    }

    private void loadUserInfo() {

        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uid").equalTo(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    myname = dataSnapshot1.child("name").getValue().toString();
                    myname = dataSnapshot1.child("name").getValue().toString();
                    mydp = dataSnapshot1.child("image").getValue().toString();
                    try {
                        Glide.with(PostInformation.this).load(mydp).into(imagep);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = databaseReference.orderByChild("ptime").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String ptitle = getStringValue(dataSnapshot1.child("title"));
                    String descriptions = getStringValue(dataSnapshot1.child("description"));
                    uimage = getStringValue(dataSnapshot1.child("uimage"));
                    hisdp = getStringValue(dataSnapshot1.child("udp"));
                    hisuid = dataSnapshot1.child("uid").getValue().toString();
                    uemail = getStringValue(dataSnapshot1.child("uemail"));
                    hisname = getStringValue(dataSnapshot1.child("uname"));
                    ptime = getStringValue(dataSnapshot1.child("ptime"));
                    plike = getStringValue(dataSnapshot1.child("plike"));
                    String commentcount = getStringValue(dataSnapshot1.child("pcomments"));

                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(ptime));
                    String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    name.setText(hisname);
                    title.setText(ptitle);
                    description.setText(descriptions);
                    //like.setText(plike + " Likes");
                    time.setText(timedate);
                    //tcomment.setText(commentcount + " Comments");

                    if (uimage.equals("noImage")) {
                        image.setVisibility(View.GONE);
                    } else {
                        image.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(PostInformation.this).load(uimage).into(image);
                        } catch (Exception e) {
                            // Handle Glide exception
                        }
                    }

                    try {
                        Glide.with(PostInformation.this).load(hisdp).into(picture);
                    } catch (Exception e) {
                        // Handle Glide exception
                    }
                }
                loadCompanyInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }

            private String getStringValue(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue() != null ? dataSnapshot.getValue().toString() : "";
            }
        });



    }

    private void loadCompanyInfo() {
        //String uid = currentUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Companies");
        Query query = databaseReference.orderByChild("uid").equalTo(hisuid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    companyName=getStringValue(dataSnapshot1.child("companyName"));
                    companyEmail=getStringValue(dataSnapshot1.child("companyEmail"));
                    companyIndustry=getStringValue(dataSnapshot1.child("companyIndustry"));
                    companyFoundedYear=getStringValue(dataSnapshot1.child("foundedYear"));
                    companyWebsite=getStringValue(dataSnapshot1.child("companyWebsite"));
                    companySize=getStringValue(dataSnapshot1.child("companySize"));
                    companyAbout=getStringValue(dataSnapshot1.child("companyAbout"));


                    cname.setText(companyName); //Post Heading
                    c_name.setText(companyName); //Company information
                    c_email.setText(companyEmail);
                    c_industry.setText(companyIndustry);
                    c_founded_year.setText(companyFoundedYear);
                    c_website.setText(companyWebsite);
                    c_size.setText(companySize);
                    c_about.setText(companyAbout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }

            private String getStringValue(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue() != null ? dataSnapshot.getValue().toString() : "";
            }
        });

    }

    private void hideContactButton()
    {
        String uid = currentUser.getUid();
        final String[] userType = new String[1];
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    userType[0] = "" + dataSnapshot1.child("userType").getValue(); // Remove the String declaration
                }

                // Now you can check the userType and replace the fragment.
                if ("Founder".equals(userType[0])) {
                    contactbtn.setVisibility(View.GONE);
                } else if ("Investor".equals(userType[0])) {
                    contactbtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
        return;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
