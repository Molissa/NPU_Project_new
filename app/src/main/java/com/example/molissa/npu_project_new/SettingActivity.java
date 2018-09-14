package com.example.molissa.npu_project_new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener{


    Button ChangeNameButton;
    Button BackButton;
    TextView UserName;
    TextView UserLevel;
    TextView UserExp;
    ListView Rank;
    String item_name;
    String IMEI;
    String url;
    ArrayAdapter<String> DiaryAdapter;
    ArrayList<String> list;
    ArrayAdapter<String> RankAdapter;
    ArrayList<String> ranklist;
    ListView DiaryList;
    String userID;




    private void findview(){
        ChangeNameButton = (Button) findViewById(R.id.ChangeNameButton);
        BackButton = (Button) findViewById(R.id.SettingBackButton);
        UserName = (TextView) findViewById(R.id.UserName);
        UserLevel = (TextView) findViewById(R.id.UserLevel);
        UserExp = (TextView) findViewById(R.id.UserExp);
        DiaryList = (ListView) findViewById(R.id.Diary_list);
        Rank = (ListView) findViewById(R.id.Rank_List);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        findview();
        searching();
        getRank();
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(SettingActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束個人檔案設定嗎?")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
        }
        return true;
    }
    protected void getRank(){
        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
        String Rankurl = "http://120.127.16.69/crab/link1.php?ca=select*from`user_data`ORDER%20BY`Exp`DESC";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, Rankurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ranklist = new ArrayList<String>();
                    for (int i = 0; i < 3; i++) {
                        JSONObject userInfo= (JSONObject) response.get(i);
                        String exp = userInfo.getString("Exp");
                        String UserName = userInfo.getString("UserName");
                        String ShowRank = UserName+"："+exp;
                        ranklist.add(ShowRank);
                    }
                    RankAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1,ranklist);
                    Rank.setAdapter(RankAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(json);
    }

    protected void searching(){
        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
        url = "http://120.127.16.69/crab/link1.php?ca=select*from`user_data`Where%20IMEI=%22"+IMEI+"%22";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject userInfo= (JSONObject) response.get(i);
                            final String Username = userInfo.getString("UserName");
                            final String UserLV = userInfo.getString("Level");
                            final String Exp = userInfo.getString("Exp");
                            userID = userInfo.getString("ID");
                            UserName.setText(Username);
                            UserLevel.setText(UserLV);
                            UserExp.setText(Exp);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                }
                getDiary();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final EditText editname = new EditText(SettingActivity.this);
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("確認視窗")
                        .setMessage("請輸入您的名稱")
                        .setView(editname)
                        .setIcon(R.mipmap.ic_icon)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String YouEditTextValue = editname.getText().toString();
                                        UserName.setText(YouEditTextValue);
                                        insert();
                                    }
                                }).show();
            }
        });
        queue.add(json);
    }

    protected void getDiary(){
        RequestQueue queue = Volley.newRequestQueue(SettingActivity.this);
        String Diaryurl = "http://120.127.16.69/crab/link1.php?ca=select*from`myorder`Where%20IMEI=%22"+IMEI+"%22";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, Diaryurl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        list = new ArrayList<String>();
                        for(int i = 0; i < response.length(); i++){
                            JSONObject diary = (JSONObject) response.get(i);
                            list.add(diary.getString("item"));

                        }
                        DiaryAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1,list);
                        DiaryList.setAdapter(DiaryAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(json);
    }

    public void insert() {
        url = "http://120.127.16.69/crab/User_take_order.php";
        RequestQueue mqueue = Volley.newRequestQueue(SettingActivity.this);
        item_name = UserName.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"註冊成功!",Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "註冊失敗!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                    params.put("item_Name", item_name);
                    params.put("item_IMEI",IMEI);

                return params;
            }
        };
        // Adding request to request queue
        mqueue.add(postRequest);
    }

    public void update() {
        url = "http://120.127.16.69/crab/Change_users_name.php";
        RequestQueue mqueue = Volley.newRequestQueue(SettingActivity.this);
        item_name = UserName.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"更新成功",Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "更新失敗", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("item_Name", item_name);
                params.put("item_IMEI",IMEI);
                params.put("item_ID",userID);

                return params;
            }
        };
        // Adding request to request queue
        mqueue.add(postRequest);
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

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_diary) {
            Intent it = new Intent(this, CameraActivity.class);
            startActivity(it);
            finish();
        }
        else if (id == R.id.nav_camera) {
            Intent it = new Intent(this, BiometricsActivity.class);
            startActivity(it);
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(this, EncyclopediaActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_map) {
            Intent it = new Intent(this, MapsActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_manage) {


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
        switch (view.getId())
        {
            case R.id.ChangeNameButton:
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("確認視窗")
                        .setMessage("確定要更新個人檔案名稱嗎?")
                        .setIcon(R.mipmap.ic_icon)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final EditText editname = new EditText(SettingActivity.this);
                                        new AlertDialog.Builder(SettingActivity.this)
                                                .setTitle("確認視窗")
                                                .setMessage("請輸入您的名稱")
                                                .setView(editname)
                                                .setIcon(R.mipmap.ic_icon)
                                                .setPositiveButton("確定",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String YouEditTextValue = editname.getText().toString();
                                                                UserName.setText(YouEditTextValue);
                                                                update();
                                                            }
                                                        }).show();
                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
                break;
            case R.id.SettingBackButton:
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("確認視窗")
                        .setMessage("確定要結束個人檔案設定嗎?")
                        .setIcon(R.mipmap.ic_icon)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
                break;
        }
    }
}
