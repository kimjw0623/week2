package com.example.q.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Tab2gallery extends Fragment {
    private static final String TAG = "MAIN";
    public ArrayList<String> images;
    String mCurrentPhotoPath;

    Uri uri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    int REQUEST_IMAGE_CAPTURE=1;
    GridView gallery;
    ImageAdapter adapter;
    ArrayList<String> ids = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2gallery, container, false);
        gallery = view.findViewById(R.id.galleryView);

        adapter = new ImageAdapter(getActivity(), ids);

        FloatingActionButton btn_fab = view.findViewById(R.id.fab);
        gallery.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        SharedPreferences prefs = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        String UID = prefs.getString("UID", null);
        String url = "http://socrip4.kaist.ac.kr:680/image/user/" + UID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray obj = new JSONArray(response);
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject jo_inside = obj.getJSONObject(i);
                        String name_value = jo_inside.getString("id");
                        ids.add(name_value);
                        //String phone_value = jo_inside.getString("phone");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
        //////////////////////////////////////////////////
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getActivity().getApplicationContext(), Full_Image.class);
                // passing array index
                //i.putExtra("array",images);
                i.putExtra("position", "http://socrip4.kaist.ac.kr:680/image/" + ids.get(position));
                startActivity(i);
            }
        });

        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });

        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    uri = getActivity().getContentResolver()
                                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                    adapter= new ImageAdapter(getActivity(),ids);
                    gallery.setAdapter(adapter);
                }/*
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");if (!TextUtils.isEmpty(imagePath)) {
                    if (NetworkHelper.checkConnection(mContext)) { // 인터넷 연결 체크
                        String ImageUploadURL = "http://socrip4.kaist.ac.kr:580/image/";
                        new ImageUploadTask().execute(ImageUploadURL, imagePath);
                    } else {
                        Toast.makeText(mContext, "인터넷 연결을 확인하세요", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, "먼저 업로드할 파일을 선택하세요", Toast.LENGTH_SHORT).show();
                }
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);​*/
            }
        });
        final android.support.design.widget.FloatingActionButton fab1 = view.findViewById(R.id.fab1);
        fab1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent contactIntent = new Intent(getActivity(),UploadImage.class);
                contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(contactIntent,0);
                return false;
            }
        });
        return view;
    }
    public class ImageAdapter extends BaseAdapter {

        /** The context. */
        public Context context;
        ArrayList<String> ids;
        public ImageAdapter(Context context,ArrayList<String> ids) {
            this.context = context;
            this.ids = ids;
        }

        public int getCount() {
            return ids.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load("http://socrip4.kaist.ac.kr:680/image/"+ids.get(position)).thumbnail(0.1f).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_foreground).centerCrop())
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        public ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }

    }
}
