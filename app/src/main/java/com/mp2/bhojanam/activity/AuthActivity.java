package com.mp2.bhojanam.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mp2.bhojanam.R;
import com.mp2.bhojanam.adapter.AuthFragmentsAdapter;
import com.mp2.bhojanam.fragment.LoginFragment;
import com.mp2.bhojanam.fragment.SignUpFragment;
import com.mp2.bhojanam.util.HeightWrappingViewPager;


import java.util.ArrayList;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    AuthFragmentsAdapter authFragmentsAdapter;
    HeightWrappingViewPager viewPager;
    public TabLayout tabLayout;
    ArrayList<Fragment> frag = new ArrayList<>();
    public LinearLayout llAuth,llIntro;
    public static final String DEVELOPER_KEY = "AIzaSyBw6ndLwZ2dGREZP32jHlbBYaTr2V2k7iY";
    protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
    YouTubeThumbnailView youTubeThumbnailView;
    protected ImageView playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        viewPager = findViewById(R.id.viewPagerAuth);
        tabLayout = findViewById(R.id.tabLayout);
        llIntro = findViewById(R.id.llIntro);

        frag.add(new LoginFragment());
        frag.add(new SignUpFragment());

        authFragmentsAdapter = new AuthFragmentsAdapter(getSupportFragmentManager(), frag);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewPager.setAdapter(authFragmentsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        llAuth = findViewById(R.id.llAuth);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    viewPager.reMeasureCurrentPage(0);
                    llAuth.startAnimation(AnimationUtils.loadAnimation(AuthActivity.this,R.anim.auth_up_in));
                    llIntro.startAnimation(AnimationUtils.loadAnimation(AuthActivity.this,R.anim.slide_up));
                }else{
                    viewPager.reMeasureCurrentPage(1);
                    llAuth.startAnimation(AnimationUtils.loadAnimation(AuthActivity.this,R.anim.auth_down_in));
                    llIntro.startAnimation(AnimationUtils.loadAnimation(AuthActivity.this,R.anim.slide_down));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Login");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Sign Up");

        playButton=findViewById(R.id.btnYoutube_player_auth);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(AuthActivity.this, DEVELOPER_KEY, "hVNgSPDiOSc");
                startActivity(intent);
            }
        });
        relativeLayoutOverYouTubeThumbnailView = findViewById(R.id.relativeLayout_over_youtube_thumbnail_auth);
        youTubeThumbnailView = findViewById(R.id.youtube_thumbnail_auth);

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo("hVNgSPDiOSc");
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}
