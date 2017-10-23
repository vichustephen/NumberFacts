package com.seven.zion.numberfacts;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.DigestInputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class YearFragment extends Fragment {

    FloatingActionButton refresh;
    Button search;
    EditText Yyear;
    TextView resp;
    StringRequest stringRequest;
    RequestQueue queue;
    String url;
    TextView counter;

    public YearFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_year,container,false);
        refresh = (FloatingActionButton)view.findViewById(R.id.Yfab);
        Yyear = (EditText)view.findViewById(R.id.Yrnum);
        search = (Button) view.findViewById(R.id.Yrbutton);
        resp = (TextView)view.findViewById(R.id.Yresp);
        counter = (TextView)view.findViewById(R.id.counterY);
        queue = Volley.newRequestQueue(getActivity());
        url = "http://numbersapi.com/";
        refresh.setOnClickListener(new View.OnClickListener() {
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
                int size =0;
                try {
                    size = Integer.parseInt(Yyear.getText().toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                animateTextView(0,size,counter);
                stringRequest = new StringRequest(Request.Method.GET, url + Yyear.getText().toString().trim()
                        + "/year", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resp.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);
                hideKeyboardFrom(getContext(),view);
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

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }


}
