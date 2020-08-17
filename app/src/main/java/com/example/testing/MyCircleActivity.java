package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCircleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth auth;
    FirebaseUser user;
    CreateUser createUser;
    ArrayList<CreateUser> namelist;
    DatabaseReference reference, userReference;
    String circleMemberId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namelist = new ArrayList<>();


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namelist.clear();

                if(snapshot.exists())
                {
                    for(DataSnapshot dss : snapshot.getChildren())
                    {
                        circleMemberId = dss.child("Circlememberid").getValue(String.class);

                        userReference.child(circleMemberId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        createUser = snapshot.getValue(CreateUser.class);
                                        namelist.add(createUser);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        Toast.makeText(getApplicationContext(),"database Error",Toast.LENGTH_SHORT).show();



                                    }
                                });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"database Error occured",Toast.LENGTH_SHORT).show();


            }
        });

        adapter = new MembersAdapter(namelist,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
