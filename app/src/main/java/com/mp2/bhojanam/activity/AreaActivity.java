package com.mp2.bhojanam.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mp2.bhojanam.R;
import com.mp2.bhojanam.fragment.AProfileFragment;
import com.mp2.bhojanam.fragment.AStatFragment;

public class AreaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        bottomNavigationView = findViewById(R.id.myNavigationArea);
        bottomNavigationView.setItemIconTintList(null);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_area_profile){
                    openHome();
                    return true;
                }else{
                    openStat();
                    return true;
                }
            }
        });
        openHome();
    }

    public void openHome(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.frameLayoutArea, new AProfileFragment()).commit();
    }

    public void openStat(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                .replace(R.id.frameLayoutArea,new AStatFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutArea);

        if(!fragment.getClass().equals(AProfileFragment.class)){
            bottomNavigationView.setSelectedItemId(R.id.nav_area_profile);
            openHome();
        }else{
            super.onBackPressed();
        }

    }
}
