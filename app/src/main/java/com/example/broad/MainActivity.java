package com.example.broad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private NetworkStatusReceiver mNetworkStatusReceiver;
    private ImageView mImageView;
    private TextView mTextView;


    private void initView() {
        mImageView = findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.nuknow);
        mTextView=findViewById(R.id.textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册“网络变化”的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkStatusReceiver networkStatusReceiver = new NetworkStatusReceiver();
        registerReceiver(networkStatusReceiver, intentFilter);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkStatusReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    /**
     * “网络变化”的广播接收器
     */
    private class NetworkStatusReceiver extends BroadcastReceiver {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities == null) {
                Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                mTextView.setText("网络已断开");
                mImageView.setImageResource(R.drawable.p404);
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new     String[]    {Manifest.permission.READ_PHONE_STATE},1);
                    return;
                }
                switch (telephonyManager.getDataNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        Toast.makeText(context, "当前正在使用2G网络", Toast.LENGTH_SHORT).show();
                        mTextView.setText("当前正在使用2G网络");
                        mImageView.setImageResource(R.drawable.p2g);
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        Toast.makeText(context, "当前正在使用3G网络", Toast.LENGTH_SHORT).show();
                        mTextView.setText("当前正在使用3G网络");
                        mImageView.setImageResource(R.drawable.p3g);
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        Toast.makeText(context, "当前正在使用4G网络", Toast.LENGTH_SHORT).show();
                        mTextView.setText("当前正在使用4G网络");
                        mImageView.setImageResource(R.drawable.p4g);
                        break;
                    case TelephonyManager.NETWORK_TYPE_NR:
                        Toast.makeText(context, "当前正在使用5G网络", Toast.LENGTH_SHORT).show();
                        mTextView.setText("当前正在使用5G网络");
                        mImageView.setImageResource(R.drawable.p5g);
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        Toast.makeText(context, "当前网络未知", Toast.LENGTH_SHORT).show();
                        mTextView.setText("当前网络未知");
                        mImageView.setImageResource(R.drawable.nuknow);
                }

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Toast.makeText(context,"当前正在使用WIFI网络",Toast.LENGTH_SHORT).show();
                mTextView.setText("当前正在使用WIFI网络");
                mImageView.setImageResource(R.drawable.wifi);
            }

        }
    }
}