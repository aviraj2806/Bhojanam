package com.mp2.bhojanam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
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

import com.mp2.bhojanam.R;
import com.mp2.bhojanam.adapter.OnStudentSelected;
import com.mp2.bhojanam.adapter.StudentsAdapter;
import com.mp2.bhojanam.database.MealDatabase;
import com.mp2.bhojanam.database.MealEntity;
import com.mp2.bhojanam.database.StudentDatabse;
import com.mp2.bhojanam.database.StudentEntity;
import com.mp2.bhojanam.dialog.VerifyStudentDialog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class SMealTimeFragment extends Fragment implements OnStudentSelected, VerifyStudentDialog.OnStudentVerifed {

    public SMealTimeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    StudentsAdapter studentsAdapter;
    LinearLayoutManager layoutManager;
    LinearLayout llNo,llComplete;
    SharedPreferences sharedPreferences;
    List<StudentEntity> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_meal_time, container, false);

        recyclerView = view.findViewById(R.id.recyclerStudent);
        layoutManager = new LinearLayoutManager(getActivity());
        llNo = view.findViewById(R.id.llNoStu);
        llComplete = view.findViewById(R.id.llComplete);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);

        llComplete.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        llNo.setVisibility(View.GONE);

         data = Room.databaseBuilder(getActivity(), StudentDatabse.class,"student")
                .allowMainThreadQueries().build().studentDao().getStudentByIncharge(sharedPreferences.getString("mobile",""));
        if(data.isEmpty()){
            Log.d("hello","null");
            llNo.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_up_in));
            llNo.setVisibility(View.VISIBLE);
            llNo.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
        }else{
            studentsAdapter = new StudentsAdapter(getActivity(),data,SMealTimeFragment.this);
            recyclerView.setAdapter(studentsAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_up_in));
            recyclerView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStudentSelected(StudentEntity studentEntity) {
        DialogFragment dialogFragment = new VerifyStudentDialog(studentEntity,SMealTimeFragment.this);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"MealDialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStudentVerified(StudentEntity studentEntity) {
        data.remove(studentEntity);
        studentsAdapter.notifyDataSetChanged();

        if(data.isEmpty()){
            llComplete.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.fadein));
            llComplete.setVisibility(View.VISIBLE);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyYY-MM-dd hh:mm:ss");
        String timeStamp = simpleDateFormat.format(calendar.getTime());
        String[] split = timeStamp.split(" ");

        MealEntity mealEntity = new MealEntity(timeStamp,split[0],studentEntity.getName(),
                sharedPreferences.getString("school",""),sharedPreferences.getString("mobile",""));
        Room.databaseBuilder(Objects.requireNonNull(getActivity()), MealDatabase.class,"meal")
                .allowMainThreadQueries().build().mealDao().insertMeal(mealEntity);

    }
}
