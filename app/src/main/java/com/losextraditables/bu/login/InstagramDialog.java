package com.losextraditables.bu.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.ButterKnife;
import com.losextraditables.bu.R;
import com.losextraditables.bu.utils.InstagramLogin;

/**
 * Display 37Signals authentication dialog.
 *
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 */
public class InstagramDialog extends Dialog {

  static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
  static final float[] DIMENSIONS_PORTRAIT = {280, 420};
  static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
      ViewGroup.LayoutParams.FILL_PARENT,
      ViewGroup.LayoutParams.FILL_PARENT);
  static final int MARGIN = 4;
  static final int PADDING = 2;

  private String url;
  private OAuthDialogListener authDialogListener;
  private ProgressDialog spinner;
  private WebView webView;
  private LinearLayout content;
  private TextView title;

  @BindString(R.string.instagram_dialog_loading) String loading;

  public InstagramDialog(Context context, String url,
      OAuthDialogListener listener) {
    super(context);
    this.url = url;
    authDialogListener = listener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);

    spinner = new ProgressDialog(getContext());
    spinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
    spinner.setMessage(loading);
    content = new LinearLayout(getContext());
    content.setOrientation(LinearLayout.VERTICAL);
    setUpTitle();
    setUpWebView();

    Display display = getWindow().getWindowManager().getDefaultDisplay();
    final float scale = getContext().getResources().getDisplayMetrics().density;
    float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
        : DIMENSIONS_LANDSCAPE;

    addContentView(content, new FrameLayout.LayoutParams(
        (int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
        * scale + 0.5f)));
    CookieSyncManager.createInstance(getContext());
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.removeAllCookie();
  }

  private void setUpTitle() {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    title = new TextView(getContext());
    title.setText(R.string.instagram_dialog_title);
    title.setTextColor(Color.WHITE);
    title.setTypeface(Typeface.DEFAULT_BOLD);
    title.setBackgroundColor(Color.BLACK);
    title.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
    content.addView(title);
  }

  private void setUpWebView() {
    webView = new WebView(getContext());
    webView.setVerticalScrollBarEnabled(false);
    webView.setHorizontalScrollBarEnabled(false);
    webView.setWebViewClient(new OAuthWebViewClient());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.loadUrl(url);
    webView.setLayoutParams(FILL);
    content.addView(webView);
  }

  private class OAuthWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      if (url.startsWith(InstagramLogin.callbackUrl)) {
        String urls[] = url.split("=");
        authDialogListener.onComplete(urls[1]);
        InstagramDialog.this.dismiss();
        return true;
      }
      return false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
        String description, String failingUrl) {

      super.onReceivedError(view, errorCode, description, failingUrl);
      authDialogListener.onError(description);
      InstagramDialog.this.dismiss();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
      spinner.show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      String title = webView.getTitle();
      if (title != null && title.length() > 0) {
        InstagramDialog.this.title.setText(title);
      }
      spinner.dismiss();
    }
  }

  public interface OAuthDialogListener {
    void onComplete(String accessToken);

    void onError(String error);
  }
}