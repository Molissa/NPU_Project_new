package com.example.molissa.npu_project_new;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class EncyclopediaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String url;
    ArrayAdapter<String> Crablist;
    ArrayList<String> list;
    ArrayList<String> crabslist;
    String crab_ID;
    ListView listView;
    String checked;
    TextView Subject_txt;
    boolean searched = false;
    Button search_crab;
    AutoCompleteTextView edit_crab_name;
    String[] crabsSearchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        search_crab = (Button) findViewById(R.id.search_crab_button);
        search_crab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCrabName();
            }
        });
        edit_crab_name = (AutoCompleteTextView) findViewById(R.id.edit_crab_name);
        Subject_txt = (TextView)findViewById(R.id.Subject_txt);
        listView = (ListView) findViewById(R.id.listView);
        crabsSearchList = new String[] {"斑點擬相手蟹","雙齒近相手蟹","平背蜞","字紋弓蟹","方型大額蟹","台灣厚蟹","清白招潮","北方呼喚招潮",
                "長趾股窗蟹","悅目大眼蟹","光手滑面蟹","黑點綠蟹","火紅皺蟹","雙齒毛足蟹","楊氏近扇蟹","遠海梭子蟹","鈍齒短槳蟹","鉅緣清蟳","底棲短槳蟹",
                "欖綠青蟳","粗甲裂額蟹","鈍額曲毛蟹","拳折額蟹","司氏酋婦蟹","平額石扇蟹","莫氏毛刺蟹","小巧毛刺蟹","蛙型蟹","豆形拳蟹",
                "肝葉饅頭蟹","短指和尚蟹","凶狠圓軸蟹"};
        ArrayAdapter adapter = new ArrayAdapter<String>(EncyclopediaActivity.this, android.R.layout.simple_list_item_1,crabsSearchList);
        edit_crab_name.setThreshold(1);
        edit_crab_name.setAdapter(adapter);
        searching();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            if(searched) {
                searching();
                searched = false;
            }
            else
            new AlertDialog.Builder(EncyclopediaActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要關閉圖鑑嗎?")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
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

    protected void searching(){
        RequestQueue queue = Volley.newRequestQueue(EncyclopediaActivity.this);
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20subject_data";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    list = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i++){
                        JSONObject crabs = (JSONObject) response.get(i);
                        list.add(crabs.getString("Subject"));

                        }
                    Crablist = new ArrayAdapter<String>(EncyclopediaActivity.this, android.R.layout.simple_list_item_1,list);
                    listView.setAdapter(Crablist);
                    listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            AbsListView list = (AbsListView)adapterView;
                            int idx = list.getCheckedItemPosition();
                            checked = (String) adapterView.getAdapter().getItem(idx);
                            getCrab();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(json);
    }
    protected void getCrab(){
        searched = true;
        Subject_txt.setText("螃蟹名稱：");
        RequestQueue mqueue = Volley.newRequestQueue(EncyclopediaActivity.this);
        String checked_crab = null;
        try {
            checked_crab = URLEncoder.encode(checked,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20crabs_data%20Where%20Subject=%22"+checked_crab+"%22";
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    crabslist = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i++){
                        JSONObject crabsname = (JSONObject) response.get(i);
                        crabslist.add(crabsname.getString("Name"));
                        crab_ID = crabsname.getString("ID");

                    }
                    Crablist = new ArrayAdapter<String>(EncyclopediaActivity.this, android.R.layout.simple_list_item_1,crabslist);
                    listView.setAdapter(Crablist);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            AbsListView list = (AbsListView)adapterView;
                            int idx = list.getCheckedItemPosition();
                            checked = (String) adapterView.getAdapter().getItem(idx);

                            Intent intent = new Intent();
                            intent.setClass(EncyclopediaActivity.this, EncyclopediaActivity2.class);

                            //new一個Bundle物件，並將要傳遞的資料傳入
                            Bundle bundle = new Bundle();
                            bundle.putString("Name", checked);
                            //將Bundle物件assign給intent
                            intent.putExtras(bundle);
                            //切換Activity
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error:"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        mqueue.add(crabjson);
    }
    protected void searchCrabName() {
        searched = true;
        Intent intent = new Intent();
        intent.setClass(EncyclopediaActivity.this, EncyclopediaActivity2.class);
        checked = edit_crab_name.getText().toString();
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        bundle.putString("Name", checked);
        //將Bundle物件assign給intent
        intent.putExtras(bundle);
        //切換Activity
        startActivity(intent);
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
            Intent it = new Intent(EncyclopediaActivity.this, CameraActivity.class);
            startActivity(it);
            finish();
        }

        else if (id == R.id.nav_camera) {
            Intent it = new Intent(EncyclopediaActivity.this, BiometricsActivity.class);
            startActivity(it);
            finish();// Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_map) {
            Intent it = new Intent(EncyclopediaActivity.this,MapsActivity.class);
            startActivity(it);
            finish();

        } else if (id == R.id.nav_manage) {
            Intent it = new Intent(this, SettingActivity.class);
            startActivity(it);
            finish();
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
}
