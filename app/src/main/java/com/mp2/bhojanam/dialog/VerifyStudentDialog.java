package com.mp2.bhojanam.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.provider.MediaStore;
import android.util.Log;
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
import com.mp2.bhojanam.database.MealDatabase;
import com.mp2.bhojanam.database.MealEntity;
import com.mp2.bhojanam.database.StudentEntity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class VerifyStudentDialog extends DialogFragment {

    public VerifyStudentDialog(StudentEntity studentEntity,OnStudentVerifed onStudentVerifed) {
        this.studentEntity = studentEntity;
        this.onStudentVerifed = onStudentVerifed;
    }

    private OnStudentVerifed onStudentVerifed;
    private StudentEntity studentEntity;
    private SharedPreferences sharedPreferences;
    private ImageView imgVS,imgSubmit,imgVerify;
    private LinearLayout llImage,llBio;
    private TextView txtName,txtSchool,txtClass,txtVerify;
    private Boolean isImgVerified = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_student_dialog, container, false);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);
        imgVS = view.findViewById(R.id.imgVS);
        imgSubmit = view.findViewById(R.id.imgVSSubmit);
        imgVerify = view.findViewById(R.id.imVSVerify);
        llImage = view.findViewById(R.id.llVSImage);
        llBio = view.findViewById(R.id.llVSBiometrics);
        txtName = view.findViewById(R.id.txtVSName);
        txtSchool = view.findViewById(R.id.txtVSSchool);
        txtClass = view.findViewById(R.id.txtVSClass);
        txtVerify = view.findViewById(R.id.txtVSVerify);

        txtName.setText(studentEntity.getName());
        txtSchool.setText("School : "+sharedPreferences.getString("school",""));
        txtClass.setText("Class : "+studentEntity.getStdClass());
        Picasso.get().load(studentEntity.getImage()).error(R.drawable.avatar).into(imgVS);

        llBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeErrorToast("Feature Not Developed",null,"");
                llBio.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
            }
        });

        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,101);

            }
        });

        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!isImgVerified){
                    makeErrorToast("Please verify image.",null,"");
                    llImage.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else{

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setCancelable(false);
                    dialog.setMessage("Meal Check Verified.");
                    dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onStudentVerifed.onStudentVerified(studentEntity);
                            dismiss();
                        }
                    });
                    dialog.create();
                    dialog.show();
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


    public interface OnStudentVerifed {
        void onStudentVerified(StudentEntity studentEntity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap photo=(Bitmap)data.getExtras().get("data");
        imgVerify.setImageBitmap(photo);
        txtVerify.setText("Student Image Verified");
        txtVerify.setTextColor(getResources().getColor(R.color.accepted));
        isImgVerified = true;
    }
}
