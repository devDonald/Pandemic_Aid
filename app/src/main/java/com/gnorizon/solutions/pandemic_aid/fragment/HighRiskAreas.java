package com.gnorizon.solutions.pandemic_aid.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.activities.MainActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighRiskAreas extends Fragment {
    private View riskView;
    private WebView webView;
    private SwipeRefreshLayout swipe;
    private KProgressHUD hud;
    private String current_url = "https://reportcovid19.org/riskareas";

    public HighRiskAreas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        riskView = inflater.inflate(R.layout.fragment_high_risk_areas, container, false);
        webView = (WebView) riskView.findViewById(R.id.webview_high_risk);
        swipe = riskView.findViewById(R.id.swipe);


        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Loading High Risk Areas...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadWeb(current_url);
            }
        });


        loadWeb(current_url);
        return riskView;


    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //hud.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if(hud!=null){
//                hud.dismiss();
//            }
        }
    }
    public void loadWeb(String url){

        webView.setWebViewClient(new MyWebViewClient());


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

        //webView.loadUrl("https://reportcovid19.org/riskareas");

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);


        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=15 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        swipe.setRefreshing(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                current_url =url;
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/error.html");
            }

            public  void  onPageFinished(WebView view, String url){
                //Hide the SwipeReefreshLayout
                swipe.setRefreshing(false);
            }

        });
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("High Risk Areas");

    }
}
