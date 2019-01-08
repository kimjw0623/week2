package com.example.q.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.android.volley.VolleyLog.TAG;

public class Tab1Contacts extends Fragment implements ProfileListAdapter.EventListener {
    public ArrayList<Profile> _profiles_data;
    public ArrayList<Profile> _profiles_show;
    private ProfileListAdapter adapter;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    //private RequestQueue queue;
    private static final String TAG = "MAIN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1contacts, container, false);
        _profiles_data = new ArrayList();
        _profiles_show = new ArrayList();

        adapter = new ProfileListAdapter(getActivity(), this, R.layout.profileview, _profiles_show);

        ListView listView =  view.findViewById(R.id.listview1);
        listView.setAdapter(adapter);

        final EditText listSearch = view.findViewById(R.id.listSearch);
        listSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = listSearch.getText().toString();
                search(text);
            }
        });
        final android.support.design.widget.FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    //anim();
                Intent contactIntent = new Intent(Intent.ACTION_INSERT);
                contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(contactIntent);
                return false;
            }
        });
        final android.support.design.widget.FloatingActionButton fab1 = view.findViewById(R.id.fab1);
        fab1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                post();
                //anim();
                return false;
            }
        });
        /*
        final android.support.design.widget.FloatingActionButton fab2 = view.findViewById(R.id.fab2);
        fab2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                anim();
                Intent contactIntent = new Intent(Intent.ACTION_INSERT);
                contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(contactIntent);
                return false;
            }
        });
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);


/*
        fab.setOnClickListener(view);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);*/

        return view;
    }/*
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab1:
                anim();
                Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab2:
                anim();
                Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;
        }

    }*//*
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }*/
    public void post() {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < _profiles_data.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("name", _profiles_data.get(i).getName());
                sObject.put("phone",_profiles_data.get(i).getPhone());
                jArray.put(sObject);
            }
            obj.put("list",jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue RequestQueue = Volley.newRequestQueue(getContext());
        String url = "http://socrip4.kaist.ac.kr:680/contact";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        //req.setTag(TAG);
        RequestQueue.add(req);
// add the request object to the queue to be executed
    }

    public void reloadContacts() {
        _profiles_data.clear();
        _profiles_show.clear();
        fetchContacts();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadContacts();
    }

    public void fetchContacts() {

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                String phoneNumber = "";
                String email = "";

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }

                    emailCursor.close();
                }

                int lookupKeyIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
                String currentLookupKey = cursor.getString(lookupKeyIndex);
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                long currentId = cursor.getLong(idIndex);
                Uri contactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);

                // Modify the phone number string to look great!
                if (phoneNumber.contains("010") && !phoneNumber.contains("-")) {
                    phoneNumber = new StringBuilder(phoneNumber).insert(3, "-").toString();
                    phoneNumber = new StringBuilder(phoneNumber).insert(phoneNumber.length()-4, "-").toString();
                }

                Profile profile = new Profile(name, phoneNumber, email, contactUri);
                _profiles_data.add(profile);
            }
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://socrip4.kaist.ac.kr:680/contact/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray obj = new JSONArray(response);
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject jo_inside = obj.getJSONObject(i);
                        String name_value = jo_inside.getString("name");
                        String phone_value = jo_inside.getString("phone");
                        //String id_value = jo_inside.getString("id"); //: use id to delete, modify
                        Profile profile1 = new Profile(name_value,phone_value,null,null);
                        _profiles_data.add(profile1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(_profiles_data, new CompareProfile());
                _profiles_show.addAll(_profiles_data);
                adapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //parseVolleyError(error);
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);

        Collections.sort(_profiles_data, new CompareProfile());
        _profiles_show.addAll(_profiles_data);
    }

    public void search(String targetText) {
        _profiles_show.clear();
        targetText = targetText.toLowerCase();

        if (targetText.length() == 0) {
            _profiles_show.addAll(_profiles_data);
        }
        else {
            for (int i = 0 ; i < _profiles_data.size() ; i++) {
                if (_profiles_data.get(i).getName().toLowerCase().contains(targetText) ||
                        _profiles_data.get(i).getPhone().toLowerCase().replace("-","").contains(targetText) ||
                        _profiles_data.get(i).getEmail().toLowerCase().contains(targetText) ) {

                    _profiles_show.add(_profiles_data.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}