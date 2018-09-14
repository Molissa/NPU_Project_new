package com.example.molissa.npu_project_new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    ImageButton Map;
    ImageButton Search;
    ImageButton Diary;
    ImageButton Encyclopedia;
    String IMEI;
    TextView HelloText;
    boolean result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findview();

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();

    }

    private boolean haveInternet()
    {
        result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            result = false;
        }
        else
        {
            if (!info.isAvailable())
            {
                result =false;
            }
            else
            {
                result = true;
            }
        }

        return result;
    }

    @Override
    public void onStart(){
        super.onStart();
        haveInternet();
        if(!result){
            new AlertDialog.Builder(Main2Activity.this)
                    .setTitle("錯誤視窗")
                    .setMessage("連接網路失敗，請確認您的網路是否連接")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                }
                            }).show();
        }
        else searching();
    }


    protected void findview(){
        Map = (ImageButton) findViewById(R.id.MapButton);
        Search = (ImageButton)findViewById(R.id.SearchButton);
        Diary = (ImageButton)findViewById(R.id.DiaryButton);
        Encyclopedia = (ImageButton)findViewById(R.id.EncyclopediaButton);
        HelloText = (TextView)findViewById(R.id.HelloText);
    }

    protected void searching(){
        RequestQueue queue = Volley.newRequestQueue(Main2Activity.this);
        String url = "http://120.127.16.69/crab/link1.php?ca=select*from`user_data`Where%20IMEI=%22"+IMEI+"%22";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userInfo= (JSONObject) response.get(i);
                        String Username = userInfo.getString("UserName");
                        HelloText.setText(Username+"  歡迎回來！");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AlertDialog.Builder(Main2Activity.this)
                        .setTitle("確認視窗")
                        .setMessage("您還沒有註冊使用者名稱，是否注冊?")
                        .setIcon(R.mipmap.ic_icon)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent it = new Intent(Main2Activity.this, SettingActivity.class);
                                        startActivity(it);
                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                finish();
                                LoginManager.getInstance().logOut();
                                System.exit(0);
                            }
                        }).show();
            }
        });
        queue.add(json);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_diary) {
            Intent it = new Intent(this, CameraActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(it);
        }
        else if (id == R.id.nav_camera) {
            Intent it = new Intent(this, BiometricsActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(it);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(this, EncyclopediaActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(it);
        } else if (id == R.id.nav_map) {
            Intent it = new Intent(this, MapsActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(it);
        } else if (id == R.id.nav_manage) {
            Intent it = new Intent(this, SettingActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(it);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_end) {
            LoginManager.getInstance().logOut();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SearchButton:
                Intent search = new Intent(this, BiometricsActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(search);
                break;
            case R.id.EncyclopediaButton:
                Intent encyclopedia = new Intent(this, EncyclopediaActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(encyclopedia);
                break;
            case R.id.DiaryButton:
                Intent diary = new Intent(this, CameraActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(diary);
                break;
            case R.id.MapButton:
                Intent it = new Intent(this, MapsActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(it);
                break;
        }
    }
}
