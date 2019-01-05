package com.example.user.wowrecycle;

import android.Manifest;
import android.app.DatePickerDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class BookingFragment extends DialogFragment {
    private Button btnsetLocation;

    private static int RESULT_LOAD_IMG = 4;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;
    private static int REQUEST_IMAGE_CAPTURE=3;
    private ImageView uploader;
    Bitmap bitmap;
    String imageString;

    private static final String TAG = "BookingFragment";
    private TextView dateData;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    int PLACE_PICKER_REQUEST =1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode==REQUEST_IMAGE_CAPTURE)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                uploader.setImageBitmap(bitmap);

                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            }
        }

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_booking,container,false);

        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        dateData=(TextView)v.findViewById(R.id.datedata);
        dateData.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(getActivity(),mDataSetListener,year,month,day);
                dialog.show();
            }
        });
        btnsetLocation=(Button)v.findViewById((R.id.btnLocation));
        btnsetLocation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = builder.build(getActivity());
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }

        });
        mDataSetListener=new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG,"onDateSet:mm/dd/yyyy:"+month+"/"+dayOfMonth+"/"+"year");
                String date=month+"/"+dayOfMonth+"/"+year;
                dateData.setText(date);
            }

        };

        uploader=(ImageView)v.findViewById(R.id.item_image);
        uploader.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                }

            }
        });

        return v;
    }

}
