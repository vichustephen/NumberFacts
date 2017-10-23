package com.seven.zion.numberfacts;


import android.animation.ValueAnimator;
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

import java.security.spec.ECField;


/**
 * A simple {@link Fragment} subclass.
 */
public class MathFragment extends Fragment {

    FloatingActionButton Mfab;
    EditText Mnum;
    TextView Mresp;
    Button Mbutton;
    String url;
    RequestQueue queue;
    TextView counter;
    StringRequest stringRequest;
    public MathFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_math,container,false);
        Mfab = (FloatingActionButton)view.findViewById(R.id.Mfab);
        Mnum = (EditText) view.findViewById(R.id.Mnum);
        Mresp = (TextView)view.findViewById(R.id.Mresp);
        Mbutton = (Button)view.findViewById(R.id.Mbutton);
        counter = (TextView)view.findViewById(R.id.counterM);
        url = "http://numbersapi.com/";
         queue = Volley.newRequestQueue(getContext());
        Mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Math",url);
                int size =0;
                try {
                    size = Integer.parseInt(Mnum.getText().toString().trim());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                animateTextView(0,size,counter);

               /* long size = 0;
                try
                {
                    size = Long.parseLong(Mnum.getText().toString().trim());
                }
              catch (Exception e)
              {
                  e.printStackTrace();
              }
                if (size < 99999)
                {
                    if (size <=20&&digitTextView.getANIMATION_DURATION()<=20)
                        digitTextView.setAnimationDuration(50);
                    else if (size<=50&&digitTextView.getANIMATION_DURATION()<=50)
                        digitTextView.setAnimationDuration(10);
                    else
                        digitTextView.setAnimationDuration(0);
                }
                digitTextView.setValue((int)size);*/
                stringRequest = new StringRequest(Request.Method.GET, url+ Mnum.getText().toString().trim()+"/math",
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Mresp.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error ! Please Make sure that you have Internet Connection"
                                ,Toast.LENGTH_LONG).show();
                    }
                });
                hideKeyboardFrom(getContext(),view);
                queue.add(stringRequest);
            }
        });
        Mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    queue.add(stringRequest);
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),"Please enter a value",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.shareB) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = Mresp.getText().toString();
            if (shareBody.equals( "Enter some Value")||shareBody.equals("Enter a Date"))
                return super.onOptionsItemSelected(item);
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
