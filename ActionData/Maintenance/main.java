package ActionData.Maintenance;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.WebView;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.B4AMenuItem;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.Msgbox;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.keywords.constants.KeyCodes;
import anywheresoftware.b4a.objects.ActivityWrapper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.WebViewWrapper;
import anywheresoftware.b4a.phone.Phone;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import uk.co.martinpearman.b4a.webviewextras.WebViewExtras;

public class main extends Activity implements B4AActivity {
    public static String _url = null;
    static boolean afterFirstLayout = false;
    public static final boolean fullScreen = false;
    public static final boolean includeTitle = true;
    static boolean isFirst;
    public static main mostCurrent;
    public static WeakReference<Activity> previousOne;
    public static BA processBA;
    private static boolean processGlobalsRun;
    public Common __c;
    ActivityWrapper _activity;
    public WebViewExtras _mywebviewextras;
    public Phone _orientation;
    public WebViewWrapper _webview1;
    BA activityBA;
    BALayout layout;
    ArrayList<B4AMenuItem> menuItems;
    private Boolean onKeySubExist;
    private Boolean onKeyUpSubExist;

    private class B4AMenuItemsClickListener implements OnMenuItemClickListener {
        private final String eventName;

        public B4AMenuItemsClickListener(String str) {
            this.eventName = str;
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            main.processBA.raiseEvent(menuItem.getTitle(), this.eventName + "_click", new Object[0]);
            return main.includeTitle;
        }
    }

    private class HandleKeyDelayed implements Runnable {
        int kc;

        private HandleKeyDelayed() {
        }

        public void run() {
            runDirectly(this.kc);
        }

        public boolean runDirectly(int i) {
            Boolean bool = (Boolean) main.processBA.raiseEvent2(main.this._activity, main.fullScreen, "activity_keypress", main.fullScreen, Integer.valueOf(i));
            if (bool == null || bool.booleanValue() == main.includeTitle) {
                return main.includeTitle;
            }
            if (i != 4) {
                return main.fullScreen;
            }
            main.this.finish();
            return main.includeTitle;
        }
    }

    private static class ResumeMessage implements Runnable {
        private final WeakReference<Activity> activity;

        public ResumeMessage(Activity activity) {
            this.activity = new WeakReference(activity);
        }

        public void run() {
            if (main.mostCurrent != null && main.mostCurrent == this.activity.get()) {
                main.processBA.setActivityPaused(main.fullScreen);
                BA.LogInfo("** Activity (main) Resume **");
                main.processBA.raiseEvent(main.mostCurrent._activity, "activity_resume", (Object[]) null);
            }
        }
    }

    private static class WaitForLayout implements Runnable {
        private WaitForLayout() {
        }

        public void run() {
            if (!main.afterFirstLayout && main.mostCurrent != null) {
                if (main.mostCurrent.layout.getWidth() == 0) {
                    BA.handler.postDelayed(this, 5);
                    return;
                }
                main.mostCurrent.layout.getLayoutParams().height = main.mostCurrent.layout.getHeight();
                main.mostCurrent.layout.getLayoutParams().width = main.mostCurrent.layout.getWidth();
                main.afterFirstLayout = main.includeTitle;
                main.mostCurrent.afterFirstLayout();
            }
        }
    }

    public main() {
        this.onKeySubExist = null;
        this.onKeyUpSubExist = null;
        this.__c = null;
        this._mywebviewextras = null;
        this._webview1 = null;
        this._orientation = null;
    }

    static {
        isFirst = includeTitle;
        processGlobalsRun = fullScreen;
        _url = "";
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isFirst) {
            processBA = new BA(getApplicationContext(), null, null, "ActionData.Maintenance", "ActionData.Maintenance.main");
            processBA.loadHtSubs(getClass());
            BALayout.setDeviceScale(getApplicationContext().getResources().getDisplayMetrics().density);
        } else if (previousOne != null) {
            Activity activity = (Activity) previousOne.get();
            if (!(activity == null || activity == this)) {
                BA.LogInfo("Killing previous instance (main).");
                activity.finish();
            }
        }
        mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
        this.layout = new BALayout(this);
        setContentView(this.layout);
        afterFirstLayout = fullScreen;
        BA.handler.postDelayed(new WaitForLayout(), 5);
    }

    private void afterFirstLayout() {
        String str = "activity";
        String str2 = "ActionData.Maintenance.main";
        if (this == mostCurrent) {
            String str3 = "ActionData.Maintenance.main";
            this.activityBA = new BA(this, this.layout, processBA, "ActionData.Maintenance", str2);
            processBA.sharedProcessBA.activityBA = new WeakReference(this.activityBA);
            ViewWrapper.lastId = 0;
            String str4 = "activity";
            this._activity = new ActivityWrapper(this.activityBA, str);
            Msgbox.isDismissing = fullScreen;
            if (BA.shellMode) {
                if (isFirst) {
                    processBA.raiseEvent2(null, includeTitle, "SHELL", fullScreen, new Object[0]);
                }
                r6 = new Object[5];
                str3 = "ActionData.Maintenance.main";
                r6[0] = str2;
                r6[1] = processBA;
                r6[2] = this.activityBA;
                r6[3] = this._activity;
                r6[4] = Float.valueOf(Common.Density);
                processBA.raiseEvent2(null, includeTitle, "CREATE", includeTitle, r6);
                str4 = "activity";
                this._activity.reinitializeForShell(this.activityBA, str);
            }
            initializeProcessGlobals();
            initializeGlobals();
            BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
            processBA.raiseEvent2(null, includeTitle, "activity_create", fullScreen, Boolean.valueOf(isFirst));
            isFirst = fullScreen;
            if (this == mostCurrent) {
                processBA.setActivityPaused(fullScreen);
                BA.LogInfo("** Activity (main) Resume **");
                processBA.raiseEvent(null, "activity_resume", new Object[0]);
                if (VERSION.SDK_INT >= 11) {
                    try {
                        Activity.class.getMethod("invalidateOptionsMenu", new Class[0]).invoke(this, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addMenuItem(B4AMenuItem b4AMenuItem) {
        if (this.menuItems == null) {
            this.menuItems = new ArrayList();
        }
        this.menuItems.add(b4AMenuItem);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this.menuItems == null) {
            return fullScreen;
        }
        Iterator it = this.menuItems.iterator();
        while (it.hasNext()) {
            B4AMenuItem b4AMenuItem = (B4AMenuItem) it.next();
            MenuItem add = menu.add(b4AMenuItem.title);
            if (b4AMenuItem.drawable != null) {
                add.setIcon(b4AMenuItem.drawable);
            }
            if (VERSION.SDK_INT >= 11) {
                try {
                    if (b4AMenuItem.addToBar) {
                        MenuItem.class.getMethod("setShowAsAction", new Class[]{Integer.TYPE}).invoke(add, new Object[]{Integer.valueOf(1)});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            add.setOnMenuItemClickListener(new B4AMenuItemsClickListener(b4AMenuItem.eventName.toLowerCase(BA.cul)));
        }
        return includeTitle;
    }

    public void onWindowFocusChanged(boolean z) {
        String str = "activity_windowfocuschanged";
        super.onWindowFocusChanged(z);
        String str2 = "activity_windowfocuschanged";
        if (processBA.subExists(str)) {
            String str3 = "activity_windowfocuschanged";
            processBA.raiseEvent2(null, includeTitle, str, fullScreen, Boolean.valueOf(z));
        }
    }

    public static Class<?> getObject() {
        return main.class;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.onKeySubExist == null) {
            this.onKeySubExist = Boolean.valueOf(processBA.subExists("activity_keypress"));
        }
        if (this.onKeySubExist.booleanValue()) {
            if (i == 4 && VERSION.SDK_INT >= 18) {
                Runnable handleKeyDelayed = new HandleKeyDelayed();
                handleKeyDelayed.kc = i;
                BA.handler.post(handleKeyDelayed);
                return includeTitle;
            } else if (new HandleKeyDelayed().runDirectly(i)) {
                return includeTitle;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        String str = "activity_keyup";
        if (this.onKeyUpSubExist == null) {
            String str2 = "activity_keyup";
            this.onKeyUpSubExist = Boolean.valueOf(processBA.subExists(str));
        }
        if (this.onKeyUpSubExist.booleanValue()) {
            String str3 = "activity_keyup";
            Boolean bool = (Boolean) processBA.raiseEvent2(this._activity, fullScreen, str, fullScreen, Integer.valueOf(i));
            if (bool == null || bool.booleanValue() == includeTitle) {
                return includeTitle;
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    public void onPause() {
        super.onPause();
        if (this._activity != null) {
            Msgbox.dismiss(includeTitle);
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + this.activityBA.activity.isFinishing() + " **");
            processBA.raiseEvent2(this._activity, includeTitle, "activity_pause", fullScreen, Boolean.valueOf(this.activityBA.activity.isFinishing()));
            processBA.setActivityPaused(includeTitle);
            mostCurrent = null;
            if (!this.activityBA.activity.isFinishing()) {
                previousOne = new WeakReference(this);
            }
            Msgbox.isDismissing = fullScreen;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        previousOne = null;
    }

    public void onResume() {
        super.onResume();
        mostCurrent = this;
        Msgbox.isDismissing = fullScreen;
        if (this.activityBA != null) {
            BA.handler.post(new ResumeMessage(mostCurrent));
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        processBA.onActivityResult(i, i2, intent);
    }

    private static void initializeGlobals() {
        processBA.raiseEvent2(null, includeTitle, "globals", fullScreen, (Object[]) null);
    }

    public static boolean isAnyActivityVisible() {
        int i;
        if (mostCurrent != null) {
            i = 1;
        } else {
            i = 0;
        }
        return i | fullScreen;
    }

    public static String _activity_create(boolean z) throws Exception {
        String str = "";
        mostCurrent._activity.LoadLayout("layoutMain", mostCurrent.activityBA);
        mostCurrent._activity.AddMenuItem("flingScroll test", "FlingScroll");
        mostCurrent._webview1.setHeight(Common.PerYToCurrent(100.0f, mostCurrent.activityBA));
        mostCurrent._webview1.setWidth(Common.PerXToCurrent(100.0f, mostCurrent.activityBA));
        mostCurrent._webview1.LoadUrl(_url);
        WebViewExtras webViewExtras = mostCurrent._mywebviewextras;
        WebViewExtras.addJavascriptInterface(mostCurrent.activityBA, (WebView) mostCurrent._webview1.getObject(), "B4A");
        webViewExtras = mostCurrent._mywebviewextras;
        String str2 = "";
        WebViewExtras.addWebChromeClient(mostCurrent.activityBA, (WebView) mostCurrent._webview1.getObject(), str);
        String str3 = "";
        return str;
    }

    public static boolean _activity_keypress(int i) throws Exception {
        KeyCodes keyCodes = Common.KeyCodes;
        if (i == 4) {
            return includeTitle;
        }
        return fullScreen;
    }

    public static String _activity_pause(boolean z) throws Exception {
        return "";
    }

    public static String _activity_resume() throws Exception {
        mostCurrent._webview1.LoadUrl(_url);
        return "";
    }

    public static String _flingscroll_click() throws Exception {
        WebViewExtras webViewExtras = mostCurrent._mywebviewextras;
        WebViewExtras.flingScroll((WebView) mostCurrent._webview1.getObject(), 0, 500);
        return "";
    }

    public static void initializeProcessGlobals() {
        if (!processGlobalsRun) {
            processGlobalsRun = includeTitle;
            try {
                _process_globals();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String _globals() throws Exception {
        mostCurrent._mywebviewextras = new WebViewExtras();
        mostCurrent._webview1 = new WebViewWrapper();
        mostCurrent._orientation = new Phone();
        return "";
    }

    public static String _process_globals() throws Exception {
        _url = "http://actiondata.gr/Files/maintenance/index.php?hotel_id=20";
        return "";
    }

    public static String _togglewebviewheight() throws Exception {
        mostCurrent._webview1.setHeight(Common.PerXToCurrent(100.0f, mostCurrent.activityBA));
        mostCurrent._webview1.setHeight(Common.PerYToCurrent(100.0f, mostCurrent.activityBA));
        return "";
    }

    public static String _webview1_pagefinished(String str) throws Exception {
        _url = str;
        return "";
    }
}
