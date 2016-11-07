package anywheresoftware.b4a.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper;
import java.io.InputStream;
import java.util.HashMap;
import uk.co.martinpearman.b4a.webviewextras.WebViewExtras;

@ShortName("WebView")
@ActivityObject
public class WebViewWrapper extends ViewWrapper<WebView> {

    /* renamed from: anywheresoftware.b4a.objects.WebViewWrapper.1 */
    class C00331 extends WebViewClient {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00331(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public void onPageFinished(WebView view, String url) {
            this.val$ba.raiseEvent(WebViewWrapper.this.getObject(), this.val$eventName + "_pagefinished", url);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Boolean b = (Boolean) this.val$ba.raiseEvent(WebViewWrapper.this.getObject(), this.val$eventName + "_overrideurl", url);
            if (b != null) {
                return b.booleanValue();
            }
            return false;
        }

        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Object o = this.val$ba.raiseEvent(WebViewWrapper.this.getObject(), this.val$eventName + "_userandpasswordrequired", host, realm);
            if (o == null) {
                handler.cancel();
                return;
            }
            String[] s = (String[]) o;
            handler.proceed(s[0], s[1]);
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.WebViewWrapper.2 */
    class C00342 implements OnTouchListener {
        C00342() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case WebViewExtras.GEOLOCATION_PERMISSION_ALLOW /*0*/:
                case WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER /*1*/:
                    if (!v.hasFocus()) {
                        v.requestFocus();
                        break;
                    }
                    break;
            }
            return false;
        }
    }

    @Hide
    public void innerInitialize(BA ba, String eventName, boolean keepOldObject) {
        if (!keepOldObject) {
            setObject(new WebView(ba.context));
            ((WebView) getObject()).getSettings().setJavaScriptEnabled(true);
            ((WebView) getObject()).getSettings().setBuiltInZoomControls(true);
            ((WebView) getObject()).getSettings().setPluginsEnabled(true);
        }
        super.innerInitialize(ba, eventName, true);
        ((WebView) getObject()).setWebViewClient(new C00331(ba, eventName));
        ((WebView) getObject()).setOnTouchListener(new C00342());
    }

    public void LoadUrl(String Url) {
        ((WebView) getObject()).loadUrl(Url);
    }

    public void LoadHtml(String Html) {
        ((WebView) getObject()).loadDataWithBaseURL("file:///", Html, "text/html", "UTF8", null);
    }

    public void StopLoading() {
        ((WebView) getObject()).stopLoading();
    }

    public BitmapWrapper CaptureBitmap() {
        Picture pic = ((WebView) getObject()).capturePicture();
        BitmapWrapper bw = new BitmapWrapper();
        bw.InitializeMutable(pic.getWidth(), pic.getHeight());
        CanvasWrapper cw = new CanvasWrapper();
        cw.Initialize2((Bitmap) bw.getObject());
        pic.draw(cw.canvas);
        return bw;
    }

    public String getUrl() {
        return ((WebView) getObject()).getUrl();
    }

    public boolean getJavaScriptEnabled() {
        return ((WebView) getObject()).getSettings().getJavaScriptEnabled();
    }

    public void setJavaScriptEnabled(boolean value) {
        ((WebView) getObject()).getSettings().setJavaScriptEnabled(value);
    }

    public void setZoomEnabled(boolean v) {
        ((WebView) getObject()).getSettings().setBuiltInZoomControls(v);
    }

    public boolean getZoomEnabled() {
        return ((WebView) getObject()).getSettings().getBuiltInZoomControls();
    }

    public boolean Zoom(boolean In) {
        if (In) {
            return ((WebView) getObject()).zoomIn();
        }
        return ((WebView) getObject()).zoomOut();
    }

    public void Back() {
        ((WebView) getObject()).goBack();
    }

    public void Forward() {
        ((WebView) getObject()).goForward();
    }

    @Hide
    public static View build(Object prev, HashMap<String, Object> props, boolean designer, Object tag) throws Exception {
        if (prev == null) {
            if (designer) {
                View v = new View((Context) tag);
                InputStream in = ((Context) tag).getAssets().open("webview.jpg");
                BitmapDrawable bd = new BitmapDrawable(in);
                in.close();
                v.setBackgroundDrawable(bd);
                prev = v;
            } else {
                WebView wv = (WebView) ViewWrapper.buildNativeView((Context) tag, WebView.class, props, designer);
                wv.getSettings().setJavaScriptEnabled(((Boolean) props.get("javaScriptEnabled")).booleanValue());
                wv.getSettings().setBuiltInZoomControls(((Boolean) props.get("zoomEnabled")).booleanValue());
                wv.getSettings().setPluginsEnabled(true);
                WebView prev2 = wv;
            }
        }
        ViewWrapper.build(prev, props, designer);
        return (View) prev;
    }
}
