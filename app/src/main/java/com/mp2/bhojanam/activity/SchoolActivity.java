package com.mp2.bhojanam.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mp2.bhojanam.dialog.GetSchoolDialog;
import com.mp2.bhojanam.R;
import com.mp2.bhojanam.database.UserDatabase;
import com.mp2.bhojanam.database.UserEntity;
import com.mp2.bhojanam.fragment.SMealTimeFragment;
import com.mp2.bhojanam.fragment.SProfileFragment;
import com.mp2.bhojanam.fragment.SRegisterFragment;

public class SchoolActivity extends AppCompatActivity implements GetSchoolDialog.OnSchoolSelected {

    private SharedPreferences sharedPreferences;
    private UserEntity user;
    private RelativeLayout rlSActivity;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        sharedPreferences = getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);
        rlSActivity = findViewById(R.id.llSActivity);
        bottomNavigationView = findViewById(R.id.myNavigation);
        rlSActivity.setVisibility(View.GONE);

         user = Room.databaseBuilder(this, UserDatabase.class,"user")
        .allowMainThreadQueries().build().userDao().getUserByMobile(sharedPreferences.getString("mobile",""));
        if(!user.getSchool().equals("")){
            loadActivity();
        }else{
            DialogFragment dialogFragment = new GetSchoolDialog(this,sharedPreferences.getString("mobile",""));
            dialogFragment.setCancelable(false);
            dialogFragment.show(getSupportFragmentManager(),"GetSchool");
        }

        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_profile){
                    openHome();
                    return true;
                }else if(item.getItemId() == R.id.nav_mealtime){
                    openMealTime();
                    return true;
                }else{
                    openRegister();
                    return true;
                }
            }
        });
    }

    @Override
    public void onSchoolSelected() {
        loadActivity();
    }

    private void loadActivity() {
        rlSActivity.setVisibility(View.VISIBLE);
        openHome();
    }

    public void openHome(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.frameLayoutUser,new SProfileFragment()).commit();
    }

    public void openMealTime(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.frameLayoutUser,new SMealTimeFragment()).commit();
    }


    public void openRegister(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.frameLayoutUser,new SRegisterFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutUser);

        if(!fragment.getClass().equals(SProfileFragment.class)){
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            openHome();
        }else{
            super.onBackPressed();
        }

    }
}
