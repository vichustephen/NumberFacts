package com.seven.zion.numberfacts;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * A simple {@link Fragment} subclass.
 */
public class DateFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    FloatingActionButton Dfab;
    EditText year;
    Button search;
    TextView resp;
    Spinner months;
    DigitTextView digitTextView;
    String itemSelected;
    StringRequest stringRequest;
    RequestQueue queue;
    String url;

    public DateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_date,container,false);
        Dfab = (FloatingActionButton)view.findViewById(R.id.Dfab);
        year = (EditText)view.findViewById(R.id.Dyear);
        search = (Button)view.findViewById(R.id.Dbutton);
        resp = (TextView)view.findViewById(R.id.Dresp);
        months = (Spinner)view.findViewById(R.id.monthSpinner);
        digitTextView = (DigitTextView)view.findViewById(R.id.DdigitTextView);
        url = "http://numbersapi.com/";
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.months,android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        months.setAdapter(monthAdapter);
        months.setOnItemSelectedListener(this);
        queue = Volley.newRequestQueue(getActivity());
        Dfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    queue.add(stringRequest);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Please enter a value",Toast.LENGTH_LONG).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Onclick", url + itemSelected + year.getText()
                        .toString().trim() + "/date");
                int value =0;
                try {
                    value = Integer.parseInt(year.getText().toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (value > 31)
                    Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_LONG).show();
                else if (value == 30 &&itemSelected.equals("02"))
                    Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_LONG).show();
                else {
                    digitTextView.setAnimationDuration(80);
                    digitTextView.setValue(value);
                    stringRequest = new StringRequest(Request.Method.GET, url + itemSelected + "/" + year.getText()
                            .toString().trim() + "/date", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            resp.setText(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error ! Please Make sure that you have Internet Connection"
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(stringRequest);
                    hideKeyboardFrom(getContext(), view);
                }
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.shareB) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = resp.getText().toString();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Fact");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            Toast.makeText(getActivity(), "Share", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("January"))
            itemSelected = "01";
        else if (item.equals("February"))
            itemSelected = "02";
        else if (item.equals("March"))
            itemSelected = "03";
        else if (item.equals("April"))
            itemSelected = "04";
        else if (item.equals("May"))
            itemSelected = "05";
        else if (item.equals("June"))
            itemSelected = "06";
        else if (item.equals("July"))
            itemSelected = "07";
        else if (item.equals("August"))
            itemSelected = "08";
        else if (item.equals("September"))
            itemSelected = "09";
        else if (item.equals("October"))
            itemSelected = "10";
        else if (item.equals("November"))
            itemSelected = "11";
        else
            itemSelected = "12";
        Log.i("DateF",itemSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /*  <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="125dp"
        android:layout_marginStart="125dp"
        android:layout_marginTop="34dp"
        android:text="/"
        android:textAlignment="center"
        android:textSize="30sp" />*/
}
