package com.mp2.bhojanam.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mp2.bhojanam.R;
import com.mp2.bhojanam.activity.AuthActivity;
import com.mp2.bhojanam.database.UserDatabase;


public class GetSchoolDialog extends DialogFragment {


    public GetSchoolDialog(OnSchoolSelected onSchoolSelected,String mobile) {
        // Required empty public constructor
        this.onSchoolSelected = onSchoolSelected;
        this.mobile = mobile;
    }

    private String mobile;
    private OnSchoolSelected onSchoolSelected;
    private Spinner spnSS;
    private ImageView imgSS;
    private TextView txtBack,txtName;
    private ArrayAdapter adpSS;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_school_dialog, container, false);
        spnSS = view.findViewById(R.id.spnSS);
        imgSS = view.findViewById(R.id.imgSS);
        txtBack = view.findViewById(R.id.txtSSBack);
        txtName = view.findViewById(R.id.txtSSName);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);

        txtName.setText("Hello "+sharedPreferences.getString("name","")+",");
        adpSS = ArrayAdapter.createFromResource(getActivity(),R.array.school,R.layout.my_spinner_text);
        spnSS.setAdapter(adpSS);
        spnSS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnSS.getSelectedItemPosition() == 0){
                    ((TextView)view).setTextColor(getResources().getColor(R.color.spinner_light));
                }else{
                    ((TextView)view).setTextColor(getResources().getColor(R.color.spinner_dark));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("School Mandatory!");
                dialog.setMessage("This will result in Log Out.");
                dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), AuthActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create();
                dialog.show();
            }
        });

        imgSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school = spnSS.getSelectedItem().toString().trim();
                if(spnSS.getSelectedItemPosition() == 0){
                    makeErrorToast("Select School",null,"");
                    spnSS.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else{
                    sharedPreferences.edit().putString("school",school).apply();
                    Room.databaseBuilder(getActivity(), UserDatabase.class,"user")
                            .allowMainThreadQueries().build().userDao().insertSchool(school,mobile);
                    onSchoolSelected.onSchoolSelected();
                    dismiss();
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

    public interface OnSchoolSelected{
        void onSchoolSelected();
    }
}
