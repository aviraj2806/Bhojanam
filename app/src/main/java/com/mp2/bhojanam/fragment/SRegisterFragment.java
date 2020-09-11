package com.mp2.bhojanam.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mp2.bhojanam.BuildConfig;
import com.mp2.bhojanam.R;
import com.mp2.bhojanam.database.StudentDatabse;
import com.mp2.bhojanam.database.StudentEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;


public class SRegisterFragment extends Fragment {

    public SRegisterFragment() {
        // Required empty public constructor
    }

    private EditText etName,etClass;
    private ImageView imgStudent,imgSubmit;
    private TextView txtSchool,txtIncharge,txtNew;
    SharedPreferences sharedPreferences;
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath;
    private String deviceIdentifier;
    private String uploadedImage = "";
    private LinearLayout llStatus,llReg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_register, container, false);

        etClass = view.findViewById(R.id.etSRClass);
        etName = view.findViewById(R.id.etSRName);
        imgStudent = view.findViewById(R.id.imgSR);
        imgSubmit = view.findViewById(R.id.imgSRSubmit);
        txtSchool = view.findViewById(R.id.txtSRSchool);
        txtIncharge = view.findViewById(R.id.txtSRIncharge);
        llReg = view.findViewById(R.id.llSReg);
        llStatus = view.findViewById(R.id.llSRegStatus);
        txtNew = view.findViewById(R.id.txtSRNew);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE);

        llStatus.setVisibility(View.GONE);

        txtSchool.setText("School : "+sharedPreferences.getString("school",""));
        txtIncharge.setText("In-charge : "+sharedPreferences.getString("name",""));
        imgStudent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    File pictureFile = null;
                    try {
                        pictureFile = getPictureFile();
                    } catch (IOException ex) {
                        Toast.makeText(getActivity(),
                                "Photo file can't be created, please try again",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pictureFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                BuildConfig.APPLICATION_ID+".fileprovider",
                                pictureFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent,REQUEST_PICTURE_CAPTURE);
                    }
                }
            }
        });

        imgSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String stuClass = etClass.getText().toString().trim();

                if(uploadedImage.equals("")){
                    makeErrorToast("Please Capture Image.",null,"");
                    imgStudent.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.shake));
                }else if(name.length()<3){
                    makeErrorToast("Enter Name",etName,"Enter Student Name (Min. 3 characters)");
                }else if(stuClass.isEmpty()){
                    makeErrorToast("Enter class",etClass,"Enter Student Class");
                }else{
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyYY-MM-dd hh:mm:ss");
                    String stamp = simpleDateFormat.format(calendar.getTime());
                    String[] split = stamp.split(" ");

                    StudentEntity studentEntity = new StudentEntity(stamp,sharedPreferences.getString("mobile",""),name,
                            sharedPreferences.getString("school",""),uploadedImage,stuClass);
                    Room.databaseBuilder(getActivity(), StudentDatabse.class,"student")
                            .allowMainThreadQueries().build().studentDao().registerStudent(studentEntity);

                    etName.setText(null);
                    etClass.setText(null);
                    etName.setHintTextColor(getResources().getColor(R.color.spinner_light));
                    etClass.setHintTextColor(getResources().getColor(R.color.spinner_light));
                    imgStudent.setImageURI(null);
                    imgStudent.setImageResource(R.drawable.avatar);
                    uploadedImage = "";

                    llReg.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_out));
                    llReg.setVisibility(View.GONE);
                    llStatus.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_in));
                    llStatus.setVisibility(View.VISIBLE);
                }
            }
        });

        txtNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llStatus.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_out));
                llStatus.setVisibility(View.GONE);
                llReg.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_in));
                llReg.setVisibility(View.VISIBLE);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "BHOJANAM_" + timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists()){
                imgStudent.setImageURI(Uri.fromFile(imgFile));
                addToCloudStorage(saveBitmapToFile(imgFile));
            }
        }
    }

    private void addToCloudStorage(File f) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = deviceIdentifier + picUri.getLastPathSegment();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReference();
        final StorageReference uploadeRef = storageRef.child(cloudFilePath);

        uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener(){
            public void onFailure(@NonNull Exception exception){
                progressDialog.dismiss();
                makeErrorToast("Image Upload Failed.",null,"");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                uploadeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        makeErrorToast("Image uploaded.",null,"");
                        progressDialog.dismiss();
                        uploadedImage = uri.toString();
                    }
                });
            }
        });
    }
    protected synchronized String getInstallationIdentifier() {
        if (deviceIdentifier == null) {
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences(
                    "DEVICE_ID", Context.MODE_PRIVATE);
            deviceIdentifier = sharedPrefs.getString("DEVICE_ID", null);
            if (deviceIdentifier == null) {
                deviceIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("DEVICE_ID", deviceIdentifier);
                editor.commit();
            }
        }
        return deviceIdentifier;
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


}
