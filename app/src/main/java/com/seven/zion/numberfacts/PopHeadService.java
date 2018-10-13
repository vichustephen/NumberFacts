package com.seven.zion.numberfacts;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PopHeadService extends Service {

    private View mFloatingView;
    private WindowManager windowManager;
    ImageView collapsedClose;
    ImageView openCloseBtn;
    ImageView reloadView;
    TextView factsView;
    FrameLayout rootView;
    RelativeLayout collapsedView;
    FrameLayout openView;
    StringRequest stringRequest;
    RequestQueue queue;
    Thread thread;
    String url;
    private int PARAM;

    public PopHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget,null);
        collapsedClose = (ImageView)mFloatingView.findViewById(R.id.collapsedCloseBtn);
        openCloseBtn = (ImageView)mFloatingView.findViewById(R.id.openCloseBtn);
        factsView = (TextView)mFloatingView.findViewById(R.id.factsView);
        rootView = (FrameLayout)mFloatingView.findViewById(R.id.root_view);
        collapsedView = (RelativeLayout)mFloatingView.findViewById(R.id.collapsedView);
        reloadView = (ImageView)mFloatingView.findViewById(R.id.replay);
        openView = (FrameLayout)mFloatingView.findViewById(R.id.openView);
        url = "http://numbersapi.com/random";
        queue = Volley.newRequestQueue(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            PARAM = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else
            PARAM = WindowManager.LayoutParams.TYPE_PHONE;
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                PARAM,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatingView,params);
        collapsedClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        openCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openView.setVisibility(View.GONE);
                collapsedView.setVisibility(View.VISIBLE);
            }
        });

        reloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.add(stringRequest);
            }
        });
    thread = new Thread(){
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted())
            {
                try {
                    stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            factsView.setText(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    queue.add(stringRequest);
                    sleep(8000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    };
            thread.start();

        mFloatingView.findViewById(R.id.root_view).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        return  true;

                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int)(event.getRawX()-initialTouchX);
                        int Ydiff = (int)(event.getRawY()-initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10)
                        {
                            if (isViewCollapsed())
                            {
                                collapsedView.setVisibility(View.GONE);
                                openView.setVisibility(View.VISIBLE);
                               // queue.add(stringRequest);
                            }
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int)(event.getRawX()-initialTouchX);
                        params.y = initialY + (int)(event.getRawY()-initialTouchY);

                        windowManager.updateViewLayout(mFloatingView,params);
                        return  true;
                }
                return false;
            }
        });

    }
    public boolean isViewCollapsed()
    {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapsedView).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null)
        {
            windowManager.removeView(mFloatingView);
        }
        thread.interrupt();
    }
}
