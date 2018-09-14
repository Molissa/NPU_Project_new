package com.example.molissa.npu_project_new;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CameraActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Uri imgUri;
    ImageView imv;
    TextView GPSView;
    EditText diary;
    Button TakePicture;
    Button Picture;
    String LatitudeText;
    String LongitudeText;
    Button shareButton;
    Bitmap bitmap;
    ShareDialog shareDialog;
    String item_name;
    LocationRequest mLocationRequest;
    String IMEI;
    ProgressDialog dialogload;
    BitmapFactory.Options options;
    Spinner spinner;
    String[] crabsSearchList;
    ArrayAdapter adapter;


    public void findview() {
        imv = (ImageView) findViewById(R.id.imageView2);
        TakePicture = (Button) findViewById(R.id.button2);
        Picture = (Button) findViewById(R.id.button3);
        shareButton = (Button) findViewById(R.id.sharebutton);
        diary = (EditText) findViewById(R.id.diary);
        spinner = (Spinner)findViewById(R.id.spinner);
        GPSView = (TextView) findViewById(R.id.GPSView);
    }

    public void setbtnOnClick() {
        TakePicture.setOnClickListener(new BtnOnClick());
        Picture.setOnClickListener(new BtnOnClick());
        shareButton.setOnClickListener(new BtnOnClick());
        crabsSearchList = new String[] {"未知","斑點擬相手蟹","雙齒近相手蟹","平背蜞","字紋弓蟹","方型大額蟹","台灣厚蟹","清白招潮","北方呼喚招潮",
                "長趾股窗蟹","悅目大眼蟹","光手滑面蟹","黑點綠蟹","火紅皺蟹","雙齒毛足蟹","楊氏近扇蟹","遠海梭子蟹","鈍齒短槳蟹","鉅緣清蟳","底棲短槳蟹",
                "欖綠青蟳","粗甲裂額蟹","鈍額曲毛蟹","拳折額蟹","司氏酋婦蟹","平額石扇蟹","莫氏毛刺蟹","小巧毛刺蟹","蛙型蟹","豆形拳蟹",
                "肝葉饅頭蟹","短指和尚蟹","凶狠圓軸蟹"};

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,crabsSearchList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diary.setText("生物名稱："+parent.getSelectedItem().toString()+"\n" + "\n日誌內容：\n");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();

        findview();
        setbtnOnClick();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        shareDialog = new ShareDialog(this);

        createLocationRequest();

        // this part is optional
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(CameraActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束探索日誌嗎?")
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

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //以下是側選單方法
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

        } else if (id == R.id.nav_camera) {
            Intent it = new Intent(CameraActivity.this, BiometricsActivity.class);
            startActivity(it);
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(CameraActivity.this, EncyclopediaActivity.class);
            startActivity(it);
            finish();

        } else if (id == R.id.nav_map) {
            Intent it = new Intent(CameraActivity.this, MapsActivity.class);
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

    //以下是照相與圖庫方法
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);
                    sendBroadcast(it);
                    break;
                case 101:
                    imgUri = converUri(data.getData());
                    break;
            }
            showImg();


        } else {
            Toast.makeText(this, requestCode == 100 ? "拍照失敗" : "選取失敗", Toast.LENGTH_LONG).show();
        }
    }

    protected void showImg() {
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath() , options);
        int iw = options.outWidth;
        int ih = options.outHeight;
        if(iw > 250 || ih > 250){
            options.inSampleSize = 2;
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imgUri.getPath() , options);//將圖檔轉換為bitmap物件
        imv.setImageBitmap(bitmap); //將照片顯示在ImageView中
    }

    Uri converUri(Uri uri) {
        if (uri.toString().substring(0, 7).equals("content")) {
            String[] colName = {MediaStore.MediaColumns.DATA};//宣告要查詢的欄位
            Cursor cursor = getContentResolver().query(uri, colName, null, null, null);//以Uri進行查詢
            assert cursor != null;
            cursor.moveToFirst();
            uri = Uri.parse("file://" + cursor.getString(0));//將路徑轉為Uri
        }
        return uri;
    }

    //GPS位置介面
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Toast.makeText(getApplicationContext(),"請確定手機的GPS定位是否開啟，本功能需要開啟GPS定位",Toast.LENGTH_LONG).show();
            // Blank for a moment...
        } else {
            handleNewLocation(location);
        }
    }

    private void handleNewLocation(Location location) {
        LatitudeText = String.valueOf(location.getLatitude());
        LongitudeText = String.valueOf(location.getLongitude());
        GPSView.setText("目前位置：\n\t\t" + LatitudeText + "," + LongitudeText);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void insert() {
        String url = "http://120.127.16.69/crab/take_order.php";
        RequestQueue queue = Volley.newRequestQueue(CameraActivity.this);
        item_name = diary.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        diary.setText("生物名稱：\n"+"\n日誌內容：\n");
                        Toast.makeText(getApplicationContext(),
                                "資料成功上傳回報!",
                                Toast.LENGTH_SHORT).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "回報失敗", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if(LatitudeText != null && !LatitudeText.equals("") && LongitudeText!= null && !LongitudeText.equals(""))
                {
                    params.put("item_name", item_name);
                    params.put("item_Lat", LatitudeText);
                    params.put("item_Lon", LongitudeText);
                    params.put("item_IMEI",IMEI);
                    addUserPoint();
                }
                else {
                    new AlertDialog.Builder(CameraActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("尚未開啟定位服務，要前往設定頁面啟動定位服務嗎？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(getApplicationContext(),"找不到GPS位址，日誌上傳失敗",Toast.LENGTH_LONG).show();
                                }
                            }).show();
                }
                return params;
            }
        };
        // Adding request to request queue
        queue.add(postRequest);
    }

    public void addUserPoint() {
        String url = "http://120.127.16.69/crab/Add_users_Exp.php";
        RequestQueue queue = Volley.newRequestQueue(CameraActivity.this);
        item_name = diary.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new AlertDialog.Builder(CameraActivity.this)
                                .setTitle("確認視窗")
                                .setMessage("恭喜!您的經驗值增加了500點! 需要到個人檔案確認嗎?")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("確定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent it = new Intent(CameraActivity.this, SettingActivity.class);
                                                startActivity(it);
                                                finish();
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              // TODO Auto-generated method stub
                                             }
                                        }).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("item_IMEI",IMEI);
                params.put("item_Case", String.valueOf(500));

                return params;
            }
        };

        // Adding request to request queue
        queue.add(postRequest);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        dialogload = ProgressDialog.show(CameraActivity.this, "",
                "資料上傳中. 請稍後...", true);
        //Showing the progress dialog
        String UPLOAD_URL = "http://120.127.16.69/crab/upload.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), "照片上傳成功" , Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Toast.makeText(getApplicationContext(), "照片上傳失敗", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                if (image != null){
                    params.put("image", image);
                    insert();
                }
                else {
                    Toast.makeText(getApplicationContext(),"請選擇要上傳的照片",Toast.LENGTH_LONG).show();
                }


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
        dialogload.dismiss();
    }


    //按鈕介面
    private class BtnOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch( v.getId() ) {
                case R.id.button:
                    if (imgUri != null){
                        Intent it = new Intent(Intent.ACTION_SEND);
                        it.setType("image/*");
                        it.putExtra(Intent.EXTRA_STREAM , imgUri);
                        startActivity(Intent.createChooser(it ,"分享到"));
                    }
                    break;
                case R.id.button2:
                    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    String fname = "p" + System.currentTimeMillis()+".jpg"; //利用時間建立不重複的檔案名
                    imgUri = Uri.parse("file://"+dir+"/"+fname);
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //啟動相機
                    camera.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                    startActivityForResult(camera,100);//啟動Intent,傳出圖片


                    break;
                case R.id.button3:
                    Intent it = new Intent(Intent.ACTION_GET_CONTENT); //選取內容
                    it.setType("image/*"); //可選所有類型的圖片
                    startActivityForResult(Intent.createChooser(it, "Select Picture"), 101);


                    break;

                case R.id.sharebutton:
                    new AlertDialog.Builder(CameraActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("您確定要上傳日誌嗎?")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            uploadImage();
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
}
