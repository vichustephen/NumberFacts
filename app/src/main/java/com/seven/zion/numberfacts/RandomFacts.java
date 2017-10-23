package com.seven.zion.numberfacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
public class RandomFacts extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView resp;
    StringRequest stringRequest;
    RequestQueue queue;
    FloatingActionButton refresh;
    Spinner typeSpinner;
    String url;
    FrameLayout BG;
    public RandomFacts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_random_facts, container, false);
        typeSpinner = (Spinner)view.findViewById(R.id.spinner);
        BG = (FrameLayout)view.findViewById(R.id.BgLayout);
        ArrayAdapter<CharSequence> types = ArrayAdapter.createFromResource(getActivity(),R.array.types,android.R.layout.simple_spinner_item);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(types);
        typeSpinner.setOnItemSelectedListener(this);
        refresh = (FloatingActionButton)view.findViewById(R.id.Rfab);
        resp = (TextView)view.findViewById(R.id.Resp);
        queue = Volley.newRequestQueue(getContext());
         url = "http://numbersapi.com/random";
        //queue.add(stringRequest);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    queue.add(stringRequest);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
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
        if(!item.equals("Random"))
        url = "http://numbersapi.com/random/" + item.toLowerCase();
        else
            url = "http://numbersapi.com/random";
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resp.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error ! Please Make sure that you have Internet Connection"
                        ,Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        //resp.setBackgroundColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
