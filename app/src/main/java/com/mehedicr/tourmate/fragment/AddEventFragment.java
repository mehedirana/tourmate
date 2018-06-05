package com.mehedicr.tourmate.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mehedicr.tourmate.Model.DateConverter;
import com.mehedicr.tourmate.Model.Event;
import com.mehedicr.tourmate.Model.NetworkConnectivity;
import com.mehedicr.tourmate.Model.TourMateDatabase;
import com.mehedicr.tourmate.R;


import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText eventNameEditText;
    private EditText startLocationEditText;
    private EditText destLocationEditText;
    private EditText budgetEditText;
    private TextView startDateTextView;
    private ImageButton startDateButton;
    private Button createEventButton;

    private long createdDate;
    private long departureDate;

    private DateConverter dateConverter;

    private TourMateDatabase database;
    private FirebaseUser user;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eventNameEditText = view.findViewById(R.id.eventNameEditText);
        startLocationEditText = view.findViewById(R.id.startLocationEditText);
        destLocationEditText = view.findViewById(R.id.destLocationEditText);
        budgetEditText = view.findViewById(R.id.budgetEditText);
        startDateTextView = view.findViewById(R.id.startDateTextView);
        startDateButton = view.findViewById(R.id.startDateButton);
        createEventButton = view.findViewById(R.id.createEventButton);

        startDateButton.setOnClickListener(this);
        startDateTextView.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

        dateConverter = new DateConverter();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = new TourMateDatabase(getContext(),user);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDateButton:
            case R.id.startDateTextView:
                chooseStartDate();
                break;
            case R.id.createEventButton:
                createEvent();
                break;
        }
    }

    private void createEvent() {
        double budget;
        if (eventNameEditText.getText().toString().isEmpty()){
            eventNameEditText.setError("Event Name Required");
            eventNameEditText.requestFocus();
            return;
        }
        if (destLocationEditText.getText().toString().isEmpty()){
            destLocationEditText.setError("Destination Location Required");
            destLocationEditText.requestFocus();
            return;
        }
        if (budgetEditText.getText().toString().isEmpty()){
            budget = 0;
        }else {
            budget = Double.parseDouble(budgetEditText.getText().toString().trim());
        }

        String eventName = eventNameEditText.getText().toString().trim();
        String startLocation = startLocationEditText.getText().toString().trim();
        String destLocation = destLocationEditText.getText().toString().trim();
        String eventKey = database.getNewEventKey();
        Event event = new Event(eventKey,eventName,startLocation,destLocation,createdDate,departureDate,budget);
        Task<Void> task = database.addEvent(event);
        if (NetworkConnectivity.isNetworkAvailable(getContext())){
            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getActivity().
                                getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout,new EventListFragment());
                        ft.commit();
                    }else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
            FragmentTransaction ft = getActivity().
                    getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout,new EventListFragment());
            ft.commit();
        }


    }

    private void chooseStartDate() {
        createdDate = dateConverter.getDateInUnix(day+"/"+(month+1)+"/"+year);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,year,month,day);
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        departureDate = dateConverter.getDateInUnix(dayOfMonth+"/"+(month+1)+"/"+year);
        startDateTextView.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }
}
