package com.mp2.bhojanam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mp2.bhojanam.R;
import com.mp2.bhojanam.activity.AuthActivity;
import com.mp2.bhojanam.adapter.NewAdapter;
import com.mp2.bhojanam.database.MealDatabase;
import com.mp2.bhojanam.database.MealEntity;
import com.mp2.bhojanam.database.StudentDatabse;
import com.mp2.bhojanam.database.StudentEntity;
import com.mp2.bhojanam.database.UserDatabase;
import com.mp2.bhojanam.database.UserEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AProfileFragment extends Fragment implements NewAdapter.OnAcademyVideoClick {

    public AProfileFragment() {
        // Required empty public constructor
    }

    private TextView txtName,txtType,txtMobile,txtOut,txtServed,txtRegistered;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NewAdapter newAdapter;
    private ArrayList<String> newList = new ArrayList<>();
    NestedScrollView nsvProfile;
    LinearLayout llDetails;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_profile, container, false);

        txtName = view.findViewById(R.id.txtAPName);
        txtMobile = view.findViewById(R.id.txtAPMobile);
        txtType = view.findViewById(R.id.txtAPType);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);
        txtOut = view.findViewById(R.id.txtAPLogout);
        txtServed = view.findViewById(R.id.txtAPServed);
        txtRegistered = view.findViewById(R.id.txtAPRegistered);
        nsvProfile = view.findViewById(R.id.nsvAreaProfile);
        llDetails = view.findViewById(R.id.llADetails);
        nsvProfile.setVisibility(View.GONE);
        llDetails.setVisibility(View.GONE);


        txtName.setText(sharedPreferences.getString("name",""));
        txtType.setText(sharedPreferences.getString("type",""));
        txtMobile.setText(sharedPreferences.getString("mobile",""));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        String []split = date.split(" ");

        List<MealEntity> mealEntities = Room.databaseBuilder(getActivity(), MealDatabase.class,"meal")
                .allowMainThreadQueries().build().mealDao().getMealsByDate(split[0]);
        List<UserEntity> userEntities = Room.databaseBuilder(getActivity(), UserDatabase.class,"user")
                .allowMainThreadQueries().build().userDao().getUsersByType("School In-charge");

        if(mealEntities.isEmpty()){
            txtServed.setText("Meals Served Today : 0");
        }else{
            txtServed.setText("Meals Served Today : "+mealEntities.size());
        }

        if(userEntities.isEmpty()){
            txtRegistered.setText("Schools Registered : 0");
        }else{
            txtRegistered.setText("Schools Registered : "+userEntities.size());
        }


        recyclerView = view.findViewById(R.id.recyclerNewArea);
        layoutManager = new LinearLayoutManager(getActivity());
        newList.add("QJHHDujRC5g");
        newList.add("Bys_j0L8RHc");
        newList.add("EGkq0ONfEIA");
        newList.add("38KZX3Qsiis");
        newList.add("fzr5LBatqkg");
        newAdapter = new NewAdapter(getActivity(),this,newList);
        recyclerView.setAdapter(newAdapter);
        recyclerView.setLayoutManager(layoutManager);

        nsvProfile.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_up_in));
        nsvProfile.setVisibility(View.VISIBLE);
        llDetails.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_in));
        llDetails.setVisibility(View.VISIBLE);

        txtOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                getActivity().finish();
            }
        });


        return view;
    }

    @Override
    public void onAcademyVideoReady() {

    }
}
