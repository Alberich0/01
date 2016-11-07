package anywheresoftware.b4a.objects;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.widget.RemoteViews;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.B4ARunnable;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.ConnectorUtils;
import anywheresoftware.b4a.keywords.Common;
import anywheresoftware.b4a.keywords.LayoutValues;
import java.io.DataInputStream;
import java.util.HashMap;

@ShortName("RemoteViews")
public class RemoteViewsWrapper {
    protected RemoteViews current;
    protected String eventName;
    protected Parcel original;

    /* renamed from: anywheresoftware.b4a.objects.RemoteViewsWrapper.1 */
    class C00281 implements B4ARunnable {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$event;

        C00281(BA ba, String str) {
            this.val$ba = ba;
            this.val$event = str;
        }

        public void run() {
            this.val$ba.raiseEvent(this, this.val$event, new Object[0]);
        }
    }

    @Hide
    public static RemoteViewsWrapper createRemoteViews(BA ba, int id, String layout, String eventName) throws Exception {
        int pos;
        RemoteViews rv = new RemoteViews(BA.packageName, id);
        layout = layout.toLowerCase(BA.cul);
        if (!layout.endsWith(".bal")) {
            layout = new StringBuilder(String.valueOf(layout)).append(".bal").toString();
        }
        String[] cache = BA.applicationContext.getAssets().open(layout);
        DataInputStream din = new DataInputStream(cache);
        int version = ConnectorUtils.readInt(din);
        for (pos = ConnectorUtils.readInt(din); pos > 0; pos = (int) (((long) pos) - cache.skip((long) pos))) {
        }
        cache = null;
        if (version >= 3) {
            cache = new String[ConnectorUtils.readInt(din)];
            for (pos = 0; pos < cache.length; pos++) {
                cache[pos] = ConnectorUtils.readString(din);
            }
        }
        version = ConnectorUtils.readInt(din);
        for (pos = 0; pos < version; pos++) {
            LayoutValues.readFromStream(din);
        }
        loadLayoutHelper(ba, ConnectorUtils.readMap(din, cache), rv);
        din.close();
        ba = new RemoteViewsWrapper();
        ba.original = Parcel.obtain();
        rv.writeToParcel(ba.original, null);
        ba.eventName = eventName.toLowerCase(BA.cul);
        return ba;
    }

    private static void loadLayoutHelper(BA ba, HashMap<String, Object> props, RemoteViews rv) throws Exception {
        int i;
        String str = "_click";
        String eventName = ((String) props.get("eventName")).toLowerCase(BA.cul);
        String name = ((String) props.get("name")).toLowerCase(BA.cul);
        HashMap<String, Object> kids = (HashMap) props.get(":kids");
        if (kids != null) {
            for (i = 0; i < kids.size(); i++) {
                loadLayoutHelper(ba, (HashMap) kids.get(String.valueOf(i)), rv);
            }
        }
        String str2 = "_click";
        if (ba.htSubs.containsKey(new StringBuilder(String.valueOf(eventName)).append(str).toString())) {
            i = Common.getComponentIntent(ba, null);
            str2 = "_click";
            i.putExtra("b4a_internal_event", new StringBuilder(String.valueOf(eventName)).append(str).toString());
            int id = getIdForView(ba, name);
            rv.setOnClickPendingIntent(id, PendingIntent.getService(ba.context, id, i, 134217728));
        }
    }

    protected static int getIdForView(BA ba, String viewName) {
        try {
            return Class.forName(BA.packageName + ".R$id").getField(ba.getClassNameWithoutPackage() + "_" + viewName.toLowerCase(BA.cul)).getInt(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkNull() {
        if (this.original == null) {
            throw new RuntimeException("RemoteViews should be set by calling ConfigureHomeWidget.");
        } else if (this.current == null) {
            this.original.setDataPosition(0);
            this.current = new RemoteViews(this.original);
        }
    }

    public boolean HandleWidgetEvents(BA ba, Intent StartingIntent) {
        String str = "b4a_internal_event";
        if (StartingIntent == null) {
            return false;
        }
        String str2 = "b4a_internal_event";
        if (StartingIntent.hasExtra(str)) {
            str2 = "b4a_internal_event";
            raiseEventWithDebuggingSupport(ba, StartingIntent.getStringExtra(str));
            return true;
        } else if (IntentWrapper.ACTION_APPWIDGET_UPDATE.equals(StartingIntent.getAction())) {
            raiseEventWithDebuggingSupport(ba, this.eventName + "_requestupdate");
            return true;
        } else if (!"android.appwidget.action.APPWIDGET_DISABLED".equals(StartingIntent.getAction())) {
            return false;
        } else {
            raiseEventWithDebuggingSupport(ba, this.eventName + "_disabled");
            return true;
        }
    }

    private void raiseEventWithDebuggingSupport(BA ba, String event) {
        if (BA.debugMode) {
            BA.handler.post(new C00281(ba, event));
        } else {
            ba.raiseEvent(this, event, new Object[0]);
        }
    }

    public void SetText(BA ba, String ViewName, String Text) {
        checkNull();
        this.current.setTextViewText(getIdForView(ba, ViewName), Text);
    }

    public void SetVisible(BA ba, String ViewName, boolean Visible) {
        checkNull();
        this.current.setViewVisibility(getIdForView(ba, ViewName), Visible ? 0 : 4);
    }

    public void SetImage(BA ba, String ImageViewName, Bitmap Image) {
        checkNull();
        this.current.setImageViewBitmap(getIdForView(ba, ImageViewName), Image);
    }

    public void SetTextColor(BA ba, String ViewName, int Color) {
        checkNull();
        this.current.setTextColor(getIdForView(ba, ViewName), Color);
    }

    public void SetTextSize(BA ba, String ViewName, float Size) {
        checkNull();
        this.current.setFloat(getIdForView(ba, ViewName), "setTextSize", Size);
    }

    public void SetProgress(BA ba, String ProgressBarName, int Progress) {
        checkNull();
        this.current.setInt(getIdForView(ba, ProgressBarName), "setProgress", Progress);
    }

    public void UpdateWidget(BA ba) throws ClassNotFoundException {
        checkNull();
        AppWidgetManager.getInstance(ba.context).updateAppWidget(new ComponentName(ba.context, Class.forName(ba.className + "$" + ba.getClassNameWithoutPackage() + "_BR")), this.current);
        this.current = null;
    }
}
