package com.mehedicr.tourmate.Model;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TourMateDatabase {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private DatabaseReference expenseRef;
    private DatabaseReference momentRef;
    private FirebaseUser user;

    private String eventKey;
    private String expenseKey;
    private String momentKey;

    public TourMateDatabase(Context context, FirebaseUser user){

        this.context = context;
        this.user = user;
        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Tour Mate");
        rootRef.keepSynced(true);
        //same.. login kora nai, ejonno.. login kora na thakle user null pabe.eta handle koren :'( kmne handle korboh try catch r kothai? ektu kore den na.. dekhi...font boro koren

        userRef = rootRef.child(user.getUid());
        eventRef = userRef.child("Event");
        eventKey = eventRef.push().getKey();
    }

    public Task<Void> addEvent(Event event){
        return eventRef.child(eventKey).setValue(event);
    }

    public Task<Void> updateEvent(String eventKey,Event event){
        return eventRef.child(eventKey).setValue(event);
    }


    public Task<Void> addExpense(String eventKey, String expenseKey, Expense expense){
        expenseRef = eventRef.child(eventKey).child("Expense");
        return expenseRef.child(expenseKey).setValue(expense);
    }

    public Task<Void> addMoment(String eventKey, String momentKey, Moment moment){
        momentRef = eventRef.child(eventKey).child("Moment");
        return momentRef.child(momentKey).setValue(moment);
    }

    public String getNewEventKey(){
        return eventKey;
    }

    public String getNewExpenseKey(String eventKey){
        return  eventRef.child(eventKey).child("Expense").push().getKey();
    }

    public String getNewMomentKey(String eventKey){
        return  eventRef.child(eventKey).child("Moment").push().getKey();
    }

    public List<Expense> getAllExpenseForEvent(String eventKey){
        final List<Expense> expenseList = new ArrayList<>();
        expenseRef = eventRef.child(eventKey).child("Expense");
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData: dataSnapshot.getChildren()){
                    Expense expense = postData.getValue(Expense.class);
                    expenseList.add(expense);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return expenseList;
    }
    public List<Event> getAllEvent(){
        final List<Event> eventList = new ArrayList<>();
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Event event = data.getValue(Event.class);
                    eventList.add(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return eventList;
    }
}
