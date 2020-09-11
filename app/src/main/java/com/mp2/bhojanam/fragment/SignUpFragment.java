package com.mp2.bhojanam.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mp2.bhojanam.R;
import com.mp2.bhojanam.activity.AreaActivity;
import com.mp2.bhojanam.activity.SchoolActivity;
import com.mp2.bhojanam.database.UserDatabase;
import com.mp2.bhojanam.database.UserEntity;

import java.text.SimpleDateFormat;

public class SignUpFragment extends Fragment {

    private EditText etName,etMobile,etPass,etCPass;
    private ImageView imgSU;
    private Spinner spnType;
    ArrayAdapter adpType;
    private LinearLayout llDetails;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        etName = view.findViewById(R.id.etSuName);
        etMobile = view.findViewById(R.id.etSuMobile);
        etPass = view.findViewById(R.id.etSuPass);
        etCPass = view.findViewById(R.id.etSuCPass);
        imgSU = view.findViewById(R.id.imgSU);
        spnType = view.findViewById(R.id.spnSuType);
        llDetails = view.findViewById(R.id.llSignUpDetails);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);

        adpType = ArrayAdapter.createFromResource(getActivity(),R.array.type,R.layout.my_spinner_text);
        spnType.setAdapter(adpType);

        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnType.getSelectedItemPosition() == 0){
                    ((TextView)view).setTextColor(getResources().getColor(R.color.spinner_light));
                }else{
                    ((TextView)view).setTextColor(getResources().getColor(R.color.spinner_dark));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgSU.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final String type = spnType.getSelectedItem().toString().trim();
                final String name = etName.getText().toString().trim();
                final String mobile  = etMobile.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String cpass = etCPass.getText().toString().trim();
                etMobile.clearFocus();
                etName.clearFocus();
                etPass.clearFocus();
                etCPass.clearFocus();

                if(name.isEmpty() || mobile.isEmpty() || pass.isEmpty() || cpass.isEmpty()){
                    makeErrorToast("All fields are mandatory",null,"");
                    llDetails.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }
                else if(spnType.getSelectedItemPosition() == 0){
                    makeErrorToast("Select User Type",null,"");
                    spnType.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else if(name.length()<3){
                    makeErrorToast("Invalid Name",etName,"Name (Min. 3 characters)");
                }else if(mobile.length()<10){
                    makeErrorToast("Invalid Mobile No.",etMobile,"Enter valid 10 digit mobile no.");
                }else if(!pass.equals(cpass)){
                    makeErrorToast("Password Mismatch",etPass,"Password Mismatch");
                    makeErrorToast("Password Mismatch",etCPass,"Password Mismatch");
                }else if(pass.length()<6){
                    makeErrorToast("Invalid Password",etPass,"Password (Min. 6 characters)");
                    etCPass.setHint(null);
                    etCPass.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else{
                    UserEntity userEntity = Room.databaseBuilder(getActivity(), UserDatabase.class,"user")
                            .allowMainThreadQueries().build().userDao().getUserByMobile(mobile);
                    if(userEntity != null){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setCancelable(false);
                        dialog.setMessage("User Already Exists.\nPlease use a different mobile number.");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etPass.setText(null);
                                etName.setText(null);
                                etCPass.setText(null);
                                etMobile.setText(null);
                                spnType.setSelection(0);

                                etMobile.setHint("Mobile No. (10 digits)");
                                etName.setHint("Enter Name");
                                etPass.setHint("Password");
                                etCPass.setHint("Confirm Password");

                                etPass.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                etName.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                etCPass.setHintTextColor(getResources().getColor(R.color.spinner_light));
                                etMobile.setHintTextColor(getResources().getColor(R.color.spinner_light));
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }else{

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyYY-MM-dd hh:mm:ss");
                        String stamp = simpleDateFormat.format(calendar.getTime());

                        UserEntity user = new UserEntity(mobile,stamp,type,name,pass,"");
                        Room.databaseBuilder(getActivity(),UserDatabase.class,"user")
                                .allowMainThreadQueries().build().userDao().insertUser(user);
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setTitle("Sign Up Successful!");
                        progressDialog.setMessage("Redirecting to Home Page");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(mobile,null,"Hello "+name+",\n"+"Thank You for registering with Bhojanam.",null,null);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                sharedPreferences.edit().putString("name", name).apply();
                                sharedPreferences.edit().putString("mobile",mobile).apply();;
                                sharedPreferences.edit().putString("type",type).apply();
                                if(type.equals("School In-charge")){
                                    Intent intent = new Intent(getActivity(), SchoolActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                    getActivity().finish();
                                }else{
                                    Intent intent = new Intent(getActivity(), AreaActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                    getActivity().finish();
                                }
                            }
                        },1000);
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
