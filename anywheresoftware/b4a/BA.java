package anywheresoftware.b4a;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import anywheresoftware.b4a.Msgbox.DialogResponse;
import anywheresoftware.b4a.keywords.Common;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

public class BA {
    private static byte[][] _b;
    public static Application applicationContext;
    public static IBridgeLog bridgeLog;
    private static int checkStackTraceEvery50;
    public static final Locale cul;
    public static String debugLine;
    public static int debugLineNum;
    public static boolean debugMode;
    public static float density;
    public static final Handler handler;
    public static NumberFormat numberFormat;
    public static String packageName;
    public static boolean shellMode;
    private static volatile B4AThreadPool threadPool;
    private static HashMap<String, ArrayList<Runnable>> uninitializedActivitiesMessagesDuringPaused;
    public static WarningEngine warningEngine;
    public final Activity activity;
    public final String className;
    public final Context context;
    public final Object eventsTarget;
    public final HashMap<String, Method> htSubs;
    public final BA processBA;
    public Service service;
    public final SharedProcessBA sharedProcessBA;
    public final BALayout vg;

    /* renamed from: anywheresoftware.b4a.BA.1 */
    class C00031 implements UncaughtExceptionHandler {
        final UncaughtExceptionHandler original;

        C00031() {
            this.original = Thread.getDefaultUncaughtExceptionHandler();
        }

        public void uncaughtException(Thread t, Throwable e) {
            BA.printException(e, true);
            if (BA.bridgeLog != null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e2) {
                }
            }
            this.original.uncaughtException(t, e);
        }
    }

    public interface B4ARunnable extends Runnable {
    }

    /* renamed from: anywheresoftware.b4a.BA.2 */
    class C00042 implements B4ARunnable {
        private final /* synthetic */ String val$event;
        private final /* synthetic */ Object[] val$params;
        private final /* synthetic */ Object val$sender;

        C00042(String str, Object obj, Object[] objArr) {
            this.val$event = str;
            this.val$sender = obj;
            this.val$params = objArr;
        }

        public void run() {
            if (BA.this.sharedProcessBA.ignoreEventsFromOtherThreadsDuringMsgboxError) {
                BA.LogInfo("Event: " + this.val$event + ", was ignored.");
            } else if (!BA.this.sharedProcessBA.isService && BA.this.sharedProcessBA.activityBA == null) {
                BA.LogInfo("Reposting event: " + this.val$event);
                BA.handler.post(this);
            } else if (BA.this.sharedProcessBA.isActivityPaused) {
                BA.LogInfo("Ignoring event: " + this.val$event);
            } else {
                BA.this.raiseEvent2(this.val$sender, false, this.val$event, false, this.val$params);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.BA.3 */
    class C00053 implements B4ARunnable {
        private final /* synthetic */ int val$TaskId;
        private final /* synthetic */ Object val$container;
        private final /* synthetic */ String val$event;
        private final /* synthetic */ Object[] val$params;
        private final /* synthetic */ Object val$sender;
        private final /* synthetic */ boolean val$throwErrorIfMissingSub;

        C00053(String str, Object obj, int i, Object obj2, boolean z, Object[] objArr) {
            this.val$event = str;
            this.val$container = obj;
            this.val$TaskId = i;
            this.val$sender = obj2;
            this.val$throwErrorIfMissingSub = z;
            this.val$params = objArr;
        }

        public void run() {
            if (BA.this.sharedProcessBA.ignoreEventsFromOtherThreadsDuringMsgboxError) {
                BA.Log("Event: " + this.val$event + ", was ignored.");
            } else if (!BA.this.sharedProcessBA.isService && BA.this.sharedProcessBA.activityBA == null) {
                BA.Log("Reposting event: " + this.val$event);
                BA.handler.post(this);
            } else if (!BA.this.sharedProcessBA.isActivityPaused) {
                if (this.val$container != null) {
                    BA.markTaskAsFinish(this.val$container, this.val$TaskId);
                }
                BA.this.raiseEvent2(this.val$sender, false, this.val$event, this.val$throwErrorIfMissingSub, this.val$params);
            } else if (BA.this.sharedProcessBA.isService) {
                BA.Log("Ignoring event as service was destroyed.");
            } else {
                BA.this.addMessageToPausedMessageQueue(this.val$event, this);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.BA.4 */
    class C00064 implements Runnable {
        private final /* synthetic */ IOnActivityResult val$i;
        private final /* synthetic */ Intent val$intent;
        private final /* synthetic */ int val$result;

        C00064(IOnActivityResult iOnActivityResult, int i, Intent intent) {
            this.val$i = iOnActivityResult;
            this.val$result = i;
            this.val$intent = intent;
        }

        public void run() {
            try {
                this.val$i.ResultArrived(this.val$result, this.val$intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.BA.5 */
    class C00075 implements OnActivityResultListener {
        C00075(int i, byte[] bArr) throws UnsupportedEncodingException, NameNotFoundException {
            String str = "UTF8";
            if (BA._b == null) {
                BA._b = new byte[4][];
                String str2 = "UTF8";
                BA._b[0] = BA.packageName.getBytes(str);
                str2 = "UTF8";
                BA._b[1] = BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionName.getBytes(str);
                if (BA._b[1].length == 0) {
                    str2 = "UTF8";
                    BA._b[1] = "jsdkfh".getBytes(str);
                }
                BA._b[2] = new byte[]{(byte) BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionCode};
            }
            int value = (i / 7) + 1234;
            BA._b[3] = new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
            for (int __b = 0; __b < 4; __b++) {
                int b = 0;
                while (b < bArr.length) {
                    try {
                        bArr[b] = (byte) (bArr[b] ^ BA._b[__b][b % BA._b[__b].length]);
                        b++;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            return false;
        }
    }

    public @interface ActivityObject {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Author {
        String value();
    }

    public interface B4aDebuggable {
        Object[] debug(int i, boolean[] zArr);
    }

    public interface CheckForReinitialize {
        boolean IsInitialized();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DependsOn {
        String[] values();
    }

    public @interface DesignerName {
        String value();
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DontInheritEvents {
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Events {
        String[] values();
    }

    public @interface Hide {
    }

    public interface IBridgeLog {
        void offer(String str);
    }

    public interface IterableList {
        Object Get(int i);

        int getSize();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Permissions {
        String[] values();
    }

    public @interface Pixel {
    }

    private static class RaiseEventWhenFirstCreate implements Runnable {
        Object[] arguments;
        BA ba;
        String eventName;

        private RaiseEventWhenFirstCreate() {
        }

        public void run() {
            this.ba.raiseEvent2(null, true, this.eventName, true, this.arguments);
        }
    }

    @Target({ElementType.METHOD})
    public @interface RaisesSynchronousEvents {
    }

    public static class SharedProcessBA {
        public WeakReference<BA> activityBA;
        boolean ignoreEventsFromOtherThreadsDuringMsgboxError;
        volatile boolean isActivityPaused;
        public final boolean isService;
        Exception lastException;
        ArrayList<Runnable> messagesDuringPaused;
        int numberOfStackedEvents;
        int onActivityResultCode;
        HashMap<Integer, WeakReference<IOnActivityResult>> onActivityResultMap;
        public Object sender;

        public SharedProcessBA(boolean isService) {
            this.numberOfStackedEvents = 0;
            this.lastException = null;
            this.ignoreEventsFromOtherThreadsDuringMsgboxError = false;
            this.isActivityPaused = true;
            this.onActivityResultCode = 1;
            this.isService = isService;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ShortName {
        String value();
    }

    public interface SubDelegator {
        public static final Object SubNotFound;

        Object callSub(String str, Object obj, Object[] objArr) throws Exception;

        static {
            SubNotFound = new Object();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Version {
        float value();
    }

    public static abstract class WarningEngine {
        public static final int FULLSCREEN_MISMATCH = 1004;
        public static final int OBJECT_ALREADY_INITIALIZED = 1003;
        public static final int SAME_OBJECT_ADDED_TO_LIST = 1002;
        public static final int ZERO_SIZE_PANEL = 1001;

        public abstract void checkFullScreenInLayout(boolean z, boolean z2);

        protected abstract void warnImpl(int i);

        public static void warn(int warning) {
            if (BA.warningEngine != null) {
                BA.warningEngine.warnImpl(warning);
            }
        }
    }

    static {
        debugMode = false;
        shellMode = false;
        density = 1.0f;
        handler = new Handler();
        cul = Locale.US;
        Thread.setDefaultUncaughtExceptionHandler(new C00031());
    }

    public BA(BA otherBA, Object eventTarget, HashMap<String, Method> subs, String className) {
        HashMap hashMap;
        this.vg = otherBA.vg;
        this.eventsTarget = eventTarget;
        if (subs == null) {
            hashMap = new HashMap();
        } else {
            HashMap<String, Method> hashMap2 = subs;
        }
        this.htSubs = hashMap;
        this.processBA = null;
        this.activity = otherBA.activity;
        this.context = otherBA.context;
        this.service = otherBA.service;
        this.sharedProcessBA = otherBA.sharedProcessBA == null ? otherBA.processBA.sharedProcessBA : otherBA.sharedProcessBA;
        this.className = className;
    }

    public BA(Context context, BALayout vg, BA processBA, String packageName, String className) {
        Activity activity;
        boolean isService;
        if (context != null) {
            density = context.getResources().getDisplayMetrics().density;
        }
        if (context == null || !(context instanceof Activity)) {
            activity = null;
        } else {
            activity = (Activity) context;
            applicationContext = activity.getApplication();
        }
        if (context == null || !(context instanceof Service)) {
            isService = false;
        } else {
            isService = true;
            applicationContext = ((Service) context).getApplication();
        }
        if (context != null) {
            packageName = context.getPackageName();
        }
        this.eventsTarget = null;
        this.context = context;
        this.activity = activity;
        this.htSubs = new HashMap();
        this.className = className;
        this.processBA = processBA;
        this.vg = vg;
        if (processBA == null) {
            this.sharedProcessBA = new SharedProcessBA(isService);
        } else {
            this.sharedProcessBA = null;
        }
    }

    public boolean subExists(String sub) {
        if (this.processBA != null) {
            return this.processBA.subExists(sub);
        }
        return this.htSubs.containsKey(sub);
    }

    public Object raiseEvent(Object sender, String event, Object... params) {
        return raiseEvent2(sender, false, event, false, params);
    }

    public Object raiseEvent2(Object sender, boolean allowDuringPause, String event, boolean throwErrorIfMissingSub, Object... params) {
        String str = "Sub ";
        if (this.processBA != null) {
            return this.processBA.raiseEvent2(sender, allowDuringPause, event, throwErrorIfMissingSub, params);
        }
        if (!this.sharedProcessBA.isActivityPaused || allowDuringPause) {
            SharedProcessBA sharedProcessBA;
            SharedProcessBA sharedProcessBA2;
            try {
                sharedProcessBA = this.sharedProcessBA;
                sharedProcessBA.numberOfStackedEvents++;
                this.sharedProcessBA.sender = sender;
                Method m = (Method) this.htSubs.get(event);
                if (m != null) {
                    Object invoke = m.invoke(this.eventsTarget, params);
                    sharedProcessBA2 = this.sharedProcessBA;
                    sharedProcessBA2.numberOfStackedEvents--;
                    return invoke;
                } else if (throwErrorIfMissingSub) {
                    throw new Exception("Sub " + event + " was not found.");
                } else {
                    sharedProcessBA = this.sharedProcessBA;
                    sharedProcessBA.numberOfStackedEvents--;
                    return null;
                }
            } catch (IllegalArgumentException e) {
                throw new Exception("Sub " + event + " signature does not match expected signature.");
            } catch (B4AUncaughtException e2) {
                try {
                    throw e2;
                } catch (Throwable th) {
                    sharedProcessBA2 = this.sharedProcessBA;
                    sharedProcessBA2.numberOfStackedEvents--;
                }
            } catch (Throwable th2) {
                Throwable e3 = th2;
                if (e3 instanceof InvocationTargetException) {
                    e3 = e3.getCause();
                }
                if (e3 instanceof B4AUncaughtException) {
                    if (this.sharedProcessBA.numberOfStackedEvents > 1) {
                        B4AUncaughtException e4 = (B4AUncaughtException) e3;
                    } else {
                        System.out.println("catching B4AUncaughtException");
                        sharedProcessBA = this.sharedProcessBA;
                        sharedProcessBA.numberOfStackedEvents--;
                        return null;
                    }
                } else if (e3 instanceof Error) {
                    Error e5 = (Error) e3;
                } else {
                    boolean z;
                    if (debugMode) {
                        z = false;
                    } else {
                        z = true;
                    }
                    String sub = printException(e3, z);
                    if (this.sharedProcessBA.activityBA == null) {
                        RuntimeException runtimeException = new RuntimeException(e3);
                    } else {
                        ShowErrorMsgbox(e3.toString(), sub);
                        sharedProcessBA = this.sharedProcessBA;
                        sharedProcessBA.numberOfStackedEvents--;
                    }
                }
            }
        } else {
            System.out.println("ignoring event: " + event);
            return null;
        }
    }

    public void ShowErrorMsgbox(String errorMessage, String sub) {
        this.sharedProcessBA.ignoreEventsFromOtherThreadsDuringMsgboxError = true;
        try {
            boolean z;
            LogError(errorMessage);
            Builder builder = new Builder(((BA) this.sharedProcessBA.activityBA.get()).context);
            builder.setTitle("Error occurred");
            builder.setMessage(new StringBuilder(String.valueOf(sub != null ? "An error has occurred in sub:" + sub + Common.CRLF : "")).append(errorMessage).append("\nContinue?").toString());
            DialogResponse dr = new DialogResponse(false);
            builder.setPositiveButton("Yes", dr);
            builder.setNegativeButton("No", dr);
            AlertDialog create = builder.create();
            if (this.sharedProcessBA.numberOfStackedEvents == 1) {
                z = true;
            } else {
                z = false;
            }
            Msgbox.msgbox(create, z);
            if (dr.res == -2) {
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
            this.sharedProcessBA.ignoreEventsFromOtherThreadsDuringMsgboxError = false;
        } catch (Throwable th) {
            this.sharedProcessBA.ignoreEventsFromOtherThreadsDuringMsgboxError = false;
        }
    }

    public static String printException(Throwable e, boolean print) {
        String sub = "";
        for (StackTraceElement ste : e.getStackTrace()) {
            ByteArrayOutputStream out;
            if (ste.getClassName().startsWith(packageName)) {
                sub = new StringBuilder(String.valueOf(ste.getClassName().substring(packageName.length() + 1))).append(ste.getMethodName()).toString();
                if (debugLine != null) {
                    sub = new StringBuilder(String.valueOf(sub)).append(" (B4A line: ").append(debugLineNum).append(")\n").append(debugLine).toString();
                } else {
                    sub = new StringBuilder(String.valueOf(sub)).append(" (java line: ").append(ste.getLineNumber()).append(")").toString();
                }
                if (print) {
                    if (sub.length() > 0) {
                        LogError(sub);
                    }
                    out = new ByteArrayOutputStream();
                    PrintWriter pw = new PrintWriter(out);
                    e.printStackTrace(pw);
                    pw.close();
                    try {
                        LogError(new String(out.toByteArray(), "UTF8"));
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                }
                return sub;
            }
        }
        if (print) {
            if (sub.length() > 0) {
                LogError(sub);
            }
            out = new ByteArrayOutputStream();
            PrintWriter pw2 = new PrintWriter(out);
            e.printStackTrace(pw2);
            pw2.close();
            LogError(new String(out.toByteArray(), "UTF8"));
        }
        return sub;
    }

    public void raiseEventFromUI(Object sender, String event, Object... params) {
        if (this.processBA != null) {
            this.processBA.raiseEventFromUI(sender, event, params);
            return;
        }
        handler.post(new C00042(event, sender, params));
    }

    public Object raiseEventFromDifferentThread(Object sender, Object container, int TaskId, String event, boolean throwErrorIfMissingSub, Object[] params) {
        if (this.processBA != null) {
            return this.processBA.raiseEventFromDifferentThread(sender, container, TaskId, event, throwErrorIfMissingSub, params);
        }
        handler.post(new C00053(event, container, TaskId, sender, throwErrorIfMissingSub, params));
        return null;
    }

    public static void addMessageToUninitializeActivity(String className, String eventName, Object[] arguments) {
        if (uninitializedActivitiesMessagesDuringPaused == null) {
            uninitializedActivitiesMessagesDuringPaused = new HashMap();
        }
        ArrayList<Runnable> list = (ArrayList) uninitializedActivitiesMessagesDuringPaused.get(className);
        if (list == null) {
            list = new ArrayList();
            uninitializedActivitiesMessagesDuringPaused.put(className, list);
        }
        if (list.size() < 20) {
            RaiseEventWhenFirstCreate r = new RaiseEventWhenFirstCreate();
            r.eventName = eventName;
            r.arguments = arguments;
            Log("sending message to waiting queue of uninitialized activity (" + eventName + ")");
            list.add(r);
        }
    }

    public void addMessageToPausedMessageQueue(String event, Runnable msg) {
        String str = ")";
        if (this.processBA != null) {
            this.processBA.addMessageToPausedMessageQueue(event, msg);
            return;
        }
        String str2 = ")";
        Log("sending message to waiting queue (" + event + str);
        if (this.sharedProcessBA.messagesDuringPaused == null) {
            this.sharedProcessBA.messagesDuringPaused = new ArrayList();
        }
        if (this.sharedProcessBA.messagesDuringPaused.size() > 20) {
            str2 = ")";
            Log("Ignoring event (too many queued events: " + event + str);
            return;
        }
        this.sharedProcessBA.messagesDuringPaused.add(msg);
    }

    public void setActivityPaused(boolean value) {
        if (this.processBA != null) {
            this.processBA.setActivityPaused(value);
            return;
        }
        this.sharedProcessBA.isActivityPaused = value;
        if (!value && !this.sharedProcessBA.isService) {
            if (this.sharedProcessBA.messagesDuringPaused == null && uninitializedActivitiesMessagesDuringPaused != null) {
                String cls = this.className;
                this.sharedProcessBA.messagesDuringPaused = (ArrayList) uninitializedActivitiesMessagesDuringPaused.get(cls);
                uninitializedActivitiesMessagesDuringPaused.remove(cls);
            }
            if (this.sharedProcessBA.messagesDuringPaused != null && this.sharedProcessBA.messagesDuringPaused.size() > 0) {
                try {
                    Log("running waiting messages (" + this.sharedProcessBA.messagesDuringPaused.size() + ")");
                    Iterator it = this.sharedProcessBA.messagesDuringPaused.iterator();
                    while (it.hasNext()) {
                        Runnable msg = (Runnable) it.next();
                        if (msg instanceof RaiseEventWhenFirstCreate) {
                            ((RaiseEventWhenFirstCreate) msg).ba = this;
                        }
                        msg.run();
                    }
                } finally {
                    this.sharedProcessBA.messagesDuringPaused.clear();
                }
            }
        }
    }

    public String getClassNameWithoutPackage() {
        return this.className.substring(this.className.lastIndexOf(".") + 1);
    }

    private static void markTaskAsFinish(Object container, int TaskId) {
        if (threadPool != null) {
            threadPool.markTaskAsFinished(container, TaskId);
        }
    }

    public static Future<?> submitRunnable(Runnable runnable, Object container, int TaskId) {
        if (threadPool == null) {
            synchronized (BA.class) {
                if (threadPool == null) {
                    threadPool = new B4AThreadPool();
                }
            }
        }
        if (container instanceof ObjectWrapper) {
            container = ((ObjectWrapper) container).getObject();
        }
        threadPool.submit(runnable, container, TaskId);
        return null;
    }

    public static boolean isTaskRunning(Object container, int TaskId) {
        if (threadPool == null) {
            return false;
        }
        return threadPool.isRunning(container, TaskId);
    }

    public void loadHtSubs(Class<?> cls) {
        for (Method m : cls.getDeclaredMethods()) {
            if (m.getName().startsWith("_")) {
                this.htSubs.put(m.getName().substring(1).toLowerCase(cul), m);
            }
        }
    }

    public boolean isActivityPaused() {
        if (this.processBA != null) {
            return this.processBA.isActivityPaused();
        }
        return this.sharedProcessBA.isActivityPaused;
    }

    public synchronized void startActivityForResult(IOnActivityResult iOnActivityResult, Intent intent) {
        if (this.processBA != null) {
            this.processBA.startActivityForResult(iOnActivityResult, intent);
        } else if (this.sharedProcessBA.activityBA != null) {
            BA aBa = (BA) this.sharedProcessBA.activityBA.get();
            if (aBa != null) {
                if (this.sharedProcessBA.onActivityResultMap == null) {
                    this.sharedProcessBA.onActivityResultMap = new HashMap();
                }
                this.sharedProcessBA.onActivityResultMap.put(Integer.valueOf(this.sharedProcessBA.onActivityResultCode), new WeakReference(iOnActivityResult));
                try {
                    Activity activity = aBa.activity;
                    SharedProcessBA sharedProcessBA = this.sharedProcessBA;
                    int i = sharedProcessBA.onActivityResultCode;
                    sharedProcessBA.onActivityResultCode = i + 1;
                    activity.startActivityForResult(intent, i);
                } catch (ActivityNotFoundException e) {
                    this.sharedProcessBA.onActivityResultMap.remove(Integer.valueOf(this.sharedProcessBA.onActivityResultCode - 1));
                    iOnActivityResult.ResultArrived(0, null);
                }
            }
        }
    }

    public void onActivityResult(int request, int result, Intent intent) {
        if (this.sharedProcessBA.onActivityResultMap != null) {
            WeakReference<IOnActivityResult> wi = (WeakReference) this.sharedProcessBA.onActivityResultMap.get(Integer.valueOf(request));
            if (wi == null) {
                Log("onActivityResult: wi is null");
                return;
            }
            this.sharedProcessBA.onActivityResultMap.remove(Integer.valueOf(request));
            IOnActivityResult i = (IOnActivityResult) wi.get();
            if (i == null) {
                Log("onActivityResult: IOnActivityResult was released");
            } else {
                addMessageToPausedMessageQueue("OnActivityResult", new C00064(i, result, intent));
            }
        }
    }

    public static void Log(String Message) {
        Log.i("B4A", Message == null ? "null" : Message);
        if (Message != null && Message.length() > 4000) {
            LogInfo("Message longer than Log limit (4000). Message was truncated.");
        }
        if (bridgeLog != null) {
            bridgeLog.offer(Message);
        }
    }

    public static void addLogPrefix(String prefix, String message) {
        prefix = "~" + prefix + ":";
        if (message.length() < 3900) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            int i = -1;
            int prev = 0;
            while (true) {
                i = message.indexOf(10, i + 1);
                if (i == -1) {
                    break;
                }
                if (prev == i) {
                    sb.setLength(sb.length() - prefix.length());
                } else {
                    sb.append(message.substring(prev, i + 1)).append(prefix);
                }
                prev = i + 1;
            }
            if (prev < message.length()) {
                sb.append(message.substring(prev));
            } else {
                sb.setLength(sb.length() - prefix.length());
            }
            message = sb.toString();
        }
        Log(message);
    }

    public static void LogError(String Message) {
        addLogPrefix("e", Message);
    }

    public static void LogInfo(String Message) {
        addLogPrefix("i", Message);
    }

    public static boolean parseBoolean(String b) {
        if (b.equals("true")) {
            return true;
        }
        if (b.equals("false")) {
            return false;
        }
        throw new RuntimeException("Cannot parse: " + b + " as boolean");
    }

    public static char CharFromString(String s) {
        if (s == null || s.length() == 0) {
            return '\u0000';
        }
        return s.charAt(0);
    }

    public Object getSender() {
        if (this.processBA != null) {
            return this.processBA.getSender();
        }
        return this.sharedProcessBA.sender;
    }

    public Exception getLastException() {
        if (this.processBA != null) {
            return this.processBA.getLastException();
        }
        return this.sharedProcessBA.lastException;
    }

    public void setLastException(Exception e) {
        while (e != null && e.getCause() != null && (e instanceof Exception)) {
            e = (Exception) e.getCause();
        }
        this.sharedProcessBA.lastException = e;
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> enumType, String name) {
        return Enum.valueOf(enumType, name);
    }

    public static String NumberToString(double value) {
        String s = Double.toString(value);
        if (s.length() > 2 && s.charAt(s.length() - 2) == '.' && s.charAt(s.length() - 1) == '0') {
            return s.substring(0, s.length() - 2);
        }
        return s;
    }

    public static String NumberToString(float value) {
        return NumberToString((double) value);
    }

    public static String NumberToString(int value) {
        return String.valueOf(value);
    }

    public static String NumberToString(long value) {
        return String.valueOf(value);
    }

    public static String NumberToString(Number value) {
        return String.valueOf(value);
    }

    public static double ObjectToNumber(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        return Double.parseDouble(String.valueOf(o));
    }

    public static long ObjectToLongNumber(Object o) {
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        return Long.parseLong(String.valueOf(o));
    }

    public static boolean ObjectToBoolean(Object o) {
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        return parseBoolean(String.valueOf(o));
    }

    public static char ObjectToChar(Object o) {
        if (o instanceof Character) {
            return ((Character) o).charValue();
        }
        return CharFromString(o.toString());
    }

    public static String TypeToString(Object o, boolean clazz) {
        String str = "_";
        try {
            int i = checkStackTraceEvery50 + 1;
            checkStackTraceEvery50 = i;
            if (i % 50 == 0 || checkStackTraceEvery50 < 0) {
                if (Thread.currentThread().getStackTrace().length >= (checkStackTraceEvery50 < 0 ? 20 : 150)) {
                    checkStackTraceEvery50 = -100;
                    return "";
                }
                checkStackTraceEvery50 = 0;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int i2 = 0;
            for (Field f : o.getClass().getDeclaredFields()) {
                String fname = f.getName();
                if (clazz) {
                    if (fname.startsWith("_")) {
                        fname = fname.substring(1);
                        if (fname.startsWith("_")) {
                        }
                    }
                }
                f.setAccessible(true);
                sb.append(fname).append("=").append(String.valueOf(f.get(o)));
                i2++;
                if (i2 % 3 == 0) {
                    sb.append(Common.CRLF);
                }
                sb.append(", ");
            }
            if (sb.length() >= 2) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public static <T> T gm(Map map, Object key, T defValue) {
        T o = map.get(key);
        if (o == null) {
            return defValue;
        }
        return o;
    }

    public static String ObjectToString(Object o) {
        return String.valueOf(o);
    }

    public static int switchObjectToInt(Object test, Object... values) {
        int i;
        if (test instanceof Number) {
            double t = ((Number) test).doubleValue();
            for (i = 0; i < values.length; i++) {
                if (t == ((Number) values[i]).doubleValue()) {
                    return i;
                }
            }
            return -1;
        }
        for (i = 0; i < values.length; i++) {
            if (test.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    public static boolean fastSubCompare(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        for (int i = 0; i < s1.length(); i++) {
            if ((s1.charAt(i) & 223) != (s2.charAt(i) & 223)) {
                return false;
            }
        }
        return true;
    }

    public static String __b(byte[] _b, int i) throws UnsupportedEncodingException, NameNotFoundException {
        OnActivityResultListener o = new C00075(i, _b);
        return new String(_b, "UTF8");
    }
}
