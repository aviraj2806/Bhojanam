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

import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SProfileFragment extends Fragment implements NewAdapter.OnAcademyVideoClick {

    public SProfileFragment() {
        // Required empty public constructor
    }

    private TextView txtName,txtSchool,txtType,txtMobile,txtOut,txtServed,txtRegistered;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NewAdapter newAdapter;
    private ArrayList<String> newList = new ArrayList<>();
    NestedScrollView nsvProfile;
    LinearLayout llMenu,llDetails;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_profile, container, false);

        txtName = view.findViewById(R.id.txtSPName);
        txtMobile = view.findViewById(R.id.txtSPMobile);
        txtType = view.findViewById(R.id.txtSPType);
        txtSchool = view.findViewById(R.id.txtSPSchool);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);
        txtOut = view.findViewById(R.id.txtSPLogout);
        txtServed = view.findViewById(R.id.txtSPServed);
        txtRegistered = view.findViewById(R.id.txtSPRegistered);
        nsvProfile = view.findViewById(R.id.nsvSchoolProfile);
        llMenu = view.findViewById(R.id.llMenu);
        llDetails = view.findViewById(R.id.llDetails);

        nsvProfile.setVisibility(View.GONE);
        llMenu.setVisibility(View.GONE);
        llDetails.setVisibility(View.GONE);


        txtName.setText(sharedPreferences.getString("name",""));
        txtSchool.setText(sharedPreferences.getString("school",""));
        txtType.setText(sharedPreferences.getString("type",""));
        txtMobile.setText(sharedPreferences.getString("mobile",""));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        String []split = date.split(" ");

        List<MealEntity> mealEntities = Room.databaseBuilder(getActivity(), MealDatabase.class,"meal")
                .allowMainThreadQueries().build().mealDao().getMealsByInchargeAndDate(sharedPreferences.getString("mobile",""),split[0]);
        List<StudentEntity> studentEntities = Room.databaseBuilder(getActivity(), StudentDatabse.class,"student")
                .allowMainThreadQueries().build().studentDao().getStudentByIncharge(sharedPreferences.getString("mobile",""));

        if(mealEntities.isEmpty()){
            txtServed.setText("Meals Served Today : 0");
        }else{
            txtServed.setText("Meals Served Today : "+mealEntities.size());
        }

        if(studentEntities.isEmpty()){
            txtRegistered.setText("Students Registered : 0");
        }else{
            txtRegistered.setText("Students Registered : "+studentEntities.size());
        }


        recyclerView = view.findViewById(R.id.recyclerNew);
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
        llMenu.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_left_in));
        llMenu.setVisibility(View.VISIBLE);
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
