package com.example.fundit;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostInformation extends AppCompatActivity {


    String hisuid, ptime, myuid, myname, myemail, mydp, uimage, postId, plike, hisdp, hisname, uemail;
    ImageView picture, image;
    TextView name, time, title, description, like, tcomment;
    ImageButton more;
    Button contactbtn, share;
    LinearLayout profile;
    EditText comment;
    ImageButton sendb;
    RecyclerView recyclerView;
    List<ModelComment> commentList;
    AdapterComment adapterComment;
    ImageView imagep;
    boolean mlike = false;
    ActionBar actionBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_information);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Post Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        postId = getIntent().getStringExtra("pid");
        //recyclerView = findViewById(R.id.recyclecomment);
        picture = findViewById(R.id.pictureco);
        image = findViewById(R.id.pimagetvco);
        name = findViewById(R.id.unameco);
        time = findViewById(R.id.utimeco);

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
        //setLikes();
        actionBar.setSubtitle("SignedInAs:" + myemail);
       // loadComments();
       /* sendb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });*/
        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent=new Intent(Intent.ACTION_SEND);

                // Default Subject
                String defaultSubject = "Exploring Collaboration: [Your Startup Name]";

                // Default Body
                String defaultBody = "Dear "+hisname+",\n\n" +
                        "I hope this email finds you well. My name is "+myname+", and I am reaching out to express my interest in learning more about your startup, [Your Startup Name].\n\n" +
                        "I am an investor interested in exploring potential investment opportunities, and I believe that your startup has great potential. I would appreciate the opportunity to discuss this further with you and learn more about your vision and plans for the future.\n\n" +
                        "Please let me know if you would be available for a meeting or a call at your earliest convenience. I am excited about the prospect of potentially working together and contributing to the success of [Your Startup Name].\n\n" +
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
       /* like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostInformation.this, PostLikedByActivity.class);
                intent.putExtra("pid", postId);
                startActivity(intent);
            }
        });*/
    }

    /*private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelComment modelComment = dataSnapshot1.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComment = new AdapterComment(getApplicationContext(), commentList, myuid, postId);
                    recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

   /* private void setLikes() {
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(postId).hasChild(myuid)) {
                    likebtn.setText("Liked");
                } else {
                    likebtn.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

  /*
    private void postComment() {
        progressDialog.setMessage("Adding Comment");

        final String commentss = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentss)) {
            Toast.makeText(PostInformation.this, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference datarf = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timestamp);
        hashMap.put("comment", commentss);
        hashMap.put("ptime", timestamp);
        hashMap.put("uid", myuid);
        hashMap.put("uemail", myemail);
        hashMap.put("udp", mydp);
        hashMap.put("uname", myname);
        datarf.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(PostInformation.this, "Added", Toast.LENGTH_LONG).show();
                comment.setText("");
                updatecommetcount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PostInformation.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    /*boolean count = false;

    private void updatecommetcount() {
        count = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (count) {
                    String comments = "" + dataSnapshot.child("pcomments").getValue();
                    int newcomment = Integer.parseInt(comments) + 1;
                    reference.child("pcomments").setValue("" + newcomment);
                    count = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

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
                    // hisuid = dataSnapshot1.child("uid").getValue().toString();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
