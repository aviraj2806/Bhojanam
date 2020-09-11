package com.mp2.bhojanam.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mp2.bhojanam.R;
import com.mp2.bhojanam.activity.AreaActivity;
import com.mp2.bhojanam.activity.AuthActivity;
import com.mp2.bhojanam.activity.SchoolActivity;
import com.mp2.bhojanam.database.UserDatabase;
import com.mp2.bhojanam.database.UserEntity;


public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    private ImageView imLogin;
    private EditText etMobile,etPass;
    private LinearLayout llDetails;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etMobile = view.findViewById(R.id.etLMobile);
        etPass = view.findViewById(R.id.etLPass);
        imLogin = view.findViewById(R.id.imgLogin);
        llDetails = view.findViewById(R.id.llLoginDetails);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);

        imLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = etMobile.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                etMobile.clearFocus();
                etPass.clearFocus();

                if(mobile.isEmpty() || pass.isEmpty()){
                    makeErrorToast("All fields are mandatory!",null,"");
                    llDetails.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else {
                    UserEntity check = Room.databaseBuilder(getActivity(), UserDatabase.class,"user")
                            .allowMainThreadQueries().build().userDao().getUserByMobile(mobile);
                    if(check == null){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false);
                        dialog.setMessage("User Not Registered.\nPlease Sign Up.");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etPass.setText(null);
                                etMobile.setText(null);
                                etMobile.setHint("Mobile No. (10 digits)");
                                etPass.setHint("Password");
                                etPass.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                etMobile.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                ((AuthActivity)getActivity()).tabLayout.selectTab(((AuthActivity)getActivity()).tabLayout.getTabAt(1));
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etPass.setText(null);
                                etMobile.setText(null);
                                etMobile.setHint("Mobile No. (10 digits)");
                                etPass.setHint("Password");
                                etPass.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                etMobile.setHintTextColor(getResources().getColor(R.color.spinner_light));
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }else{
                        if(check.getPass().equals(pass)) {
                            if (check.getType().equals("School In-charge")) {
                                sharedPreferences.edit().putString("name",check.getName()).apply();
                                sharedPreferences.edit().putString("mobile",check.getMobile()).apply();;
                                sharedPreferences.edit().putString("type",check.getType()).apply();
                                Intent intent = new Intent(getActivity(), SchoolActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                getActivity().finish();
                            } else {
                                sharedPreferences.edit().putString("name",check.getName()).apply();
                                sharedPreferences.edit().putString("mobile",check.getMobile()).apply();;
                                sharedPreferences.edit().putString("type",check.getType()).apply();
                                Intent intent = new Intent(getActivity(), AreaActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                getActivity().finish();
                            }
                        }else{
                            makeErrorToast("Invalid Password",etPass,"Invalid Password");
                        }
                    }
                }
            }
        });

        return view;
    }

    public void makeErrorToast(String text, EditText editText, String hint){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.toast,null);
        Toast toast = new Toast(getActivity());
        TextView textView = view.findViewById(R.id.toast_text);
        textView.setText(text);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        if(editText != null) {
            editText.setText(null);
            editText.setHint(hint);
            editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
            editText.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
            editText.clearFocus();
        }
    }
}
