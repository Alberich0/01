package uk.co.martinpearman.b4a.webviewextras;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.IOnActivityResult;

@Author("Martin Pearman")
@Version(1.4f)
@ActivityObject
@ShortName("WebViewExtras")
public class WebViewExtras {
    public static final int GEOLOCATION_PERMISSION_ALLOW = 0;
    public static final int GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER = 1;
    public static final int GEOLOCATION_PERMISSION_DISALLOW = 2;
    public static final int GEOLOCATION_PERMISSION_DISALLOW_AND_REMEMBER = 3;

    /* renamed from: uk.co.martinpearman.b4a.webviewextras.WebViewExtras.1 */
    class C00621 extends WebChromeClient {
        private IOnActivityResult mIOnActivityResult;
        private final /* synthetic */ String val$EventName;
        private final /* synthetic */ BA val$pBA;

        /* renamed from: uk.co.martinpearman.b4a.webviewextras.WebViewExtras.1.1 */
        class C00611 implements IOnActivityResult {
            private final /* synthetic */ ValueCallback val$pUploadMessage;

            C00611(ValueCallback valueCallback) {
                this.val$pUploadMessage = valueCallback;
            }

            public void ResultArrived(int pResultCode, Intent pIntent) {
                if (pIntent == null) {
                    this.val$pUploadMessage.onReceiveValue(null);
                } else {
                    this.val$pUploadMessage.onReceiveValue(pIntent.getData());
                }
                C00621.this.mIOnActivityResult = null;
            }
        }

        C00621(BA ba, String str) {
            this.val$pBA = ba;
            this.val$EventName = str;
            this.mIOnActivityResult = null;
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage1) {
            BA.Log(consoleMessage1.message() + " in " + consoleMessage1.sourceId() + " (Line: " + consoleMessage1.lineNumber() + ")");
            return true;
        }

        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(2 * estimatedSize);
        }

        public void onGeolocationPermissionsShowPrompt(String pOrigin, Callback pCallback) {
            switch (((Integer) this.val$pBA.raiseEvent(this, new StringBuilder(String.valueOf(this.val$EventName.toLowerCase(BA.cul))).append("_geolocationpermissionsrequest").toString(), new Object[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW])).intValue()) {
                case WebViewExtras.GEOLOCATION_PERMISSION_ALLOW /*0*/:
                    pCallback.invoke(pOrigin, true, false);
                case WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER /*1*/:
                    pCallback.invoke(pOrigin, true, true);
                case WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW /*2*/:
                    pCallback.invoke(pOrigin, false, false);
                case WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW_AND_REMEMBER /*3*/:
                    pCallback.invoke(pOrigin, false, true);
                default:
            }
        }

        public void openFileChooser(ValueCallback<Uri> pUploadMessage, String pAcceptType, String pCapture) {
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            if (pAcceptType.equals("")) {
                pAcceptType = "*/*";
            }
            i.setType(pAcceptType);
            this.mIOnActivityResult = new C00611(pUploadMessage);
            this.val$pBA.startActivityForResult(this.mIOnActivityResult, Intent.createChooser(i, "Select a file"));
        }

        public void openFileChooser(ValueCallback<Uri> pUploadMessage, String pAcceptType) {
            openFileChooser(pUploadMessage, pAcceptType, "");
        }

        public void openFileChooser(ValueCallback<Uri> pUploadMessage) {
            openFileChooser(pUploadMessage, "*/*", "");
        }

        public void onProgressChanged(WebView pWebView, int pNewProgress) {
            BA ba = this.val$pBA;
            String stringBuilder = new StringBuilder(String.valueOf(this.val$EventName.toLowerCase(BA.cul))).append("_progresschanged").toString();
            Object[] objArr = new Object[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER];
            objArr[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW] = Integer.valueOf(pNewProgress);
            ba.raiseEvent(this, stringBuilder, objArr);
        }
    }

    /* renamed from: uk.co.martinpearman.b4a.webviewextras.WebViewExtras.1B4AJavascriptInterface */
    final class AnonymousClass1B4AJavascriptInterface {
        private final BA mBA;
        private int mTaskId;

        public AnonymousClass1B4AJavascriptInterface(BA pBA) {
            this.mTaskId = WebViewExtras.GEOLOCATION_PERMISSION_ALLOW;
            this.mBA = pBA;
        }

        @JavascriptInterface
        public String CallSub(String pSubName, boolean pCallUIThread) {
            String subName2 = pSubName.toLowerCase();
            if (!this.mBA.subExists(subName2)) {
                return "JavascriptInterface error: " + pSubName + " does not exist";
            }
            if (!pCallUIThread) {
                return (String) this.mBA.raiseEvent(this, subName2, new Object[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW]);
            }
            BA ba = this.mBA;
            int i = this.mTaskId;
            this.mTaskId = i + WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER;
            return (String) ba.raiseEventFromDifferentThread(this, this, i, subName2, false, new Object[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW]);
        }

        @JavascriptInterface
        public String CallSub(String pSubName, boolean pCallUIThread, String parameter1) {
            String subName2 = pSubName.toLowerCase();
            if (!this.mBA.subExists(subName2)) {
                return "JavascriptInterface error: " + pSubName + " does not exist";
            }
            Object[] parameters = new Object[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER];
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW] = parameter1;
            if (!pCallUIThread) {
                return (String) this.mBA.raiseEvent(this, subName2, parameters);
            }
            BA ba = this.mBA;
            int i = this.mTaskId;
            this.mTaskId = i + WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER;
            return (String) ba.raiseEventFromDifferentThread(this, this, i, subName2, false, parameters);
        }

        @JavascriptInterface
        public String CallSub(String pSubName, boolean pCallUIThread, String parameter1, String parameter2) {
            String subName2 = pSubName.toLowerCase();
            if (!this.mBA.subExists(subName2)) {
                return "JavascriptInterface error: " + pSubName + " does not exist";
            }
            Object[] parameters = new Object[WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW];
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW] = parameter1;
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER] = parameter2;
            if (!pCallUIThread) {
                return (String) this.mBA.raiseEvent(this, subName2, parameters);
            }
            BA ba = this.mBA;
            int i = this.mTaskId;
            this.mTaskId = i + WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER;
            return (String) ba.raiseEventFromDifferentThread(this, this, i, subName2, false, parameters);
        }

        @JavascriptInterface
        public String CallSub(String pSubName, boolean pCallUIThread, String parameter1, String parameter2, String parameter3) {
            String subName2 = pSubName.toLowerCase();
            if (!this.mBA.subExists(subName2)) {
                return "JavascriptInterface error: " + pSubName + " does not exist";
            }
            Object[] parameters = new Object[WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW_AND_REMEMBER];
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW] = parameter1;
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER] = parameter2;
            parameters[WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW] = parameter3;
            if (!pCallUIThread) {
                return (String) this.mBA.raiseEvent(this, subName2, parameters);
            }
            BA ba = this.mBA;
            int i = this.mTaskId;
            this.mTaskId = i + WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER;
            return (String) ba.raiseEventFromDifferentThread(this, this, i, subName2, false, parameters);
        }
    }

    public static final void addJavascriptInterface(BA pBA, WebView WebView1, String InterfaceName) {
        WebView1.addJavascriptInterface(new AnonymousClass1B4AJavascriptInterface(pBA), InterfaceName);
    }

    public static final void addWebChromeClient(BA pBA, WebView WebView1, String EventName) {
        WebView1.setWebChromeClient(new C00621(pBA, EventName));
    }

    public static final void clearCache(WebView WebView1, boolean IncludeDiskFiles) {
        WebView1.clearCache(IncludeDiskFiles);
    }

    public static final void executeJavascript(WebView WebView1, String JavascriptStatement) {
        WebView1.loadUrl("javascript:" + JavascriptStatement);
    }

    public static void flingScroll(WebView webView1, int vx, int vy) {
        webView1.flingScroll(vx, vy);
    }

    public static int GetContentHeight(WebView WebView1) {
        return WebView1.getContentHeight();
    }

    public static float GetScale(WebView WebView1) {
        return WebView1.getScale();
    }

    public static boolean pageDown(WebView webView1, boolean scrollToBottom) {
        return webView1.pageDown(scrollToBottom);
    }

    public static boolean pageUp(WebView webView1, boolean scrollToTop) {
        return webView1.pageUp(scrollToTop);
    }

    public static boolean zoomIn(WebView webView1) {
        return webView1.zoomIn();
    }

    public static boolean zoomOut(WebView webView1) {
        return webView1.zoomOut();
    }
}
