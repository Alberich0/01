package anywheresoftware.b4a.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.B4ARunnable;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.keywords.constants.DialogResponse;
import anywheresoftware.b4a.objects.IntentWrapper;
import anywheresoftware.b4a.phone.Phone.PhoneId;
import java.util.HashMap;
import java.util.Map.Entry;
import uk.co.martinpearman.b4a.webviewextras.WebViewExtras;

@ShortName("PhoneEvents")
public class PhoneEvents {
    private BA ba;
    private BroadcastReceiver br;
    private String ev;
    private HashMap<String, ActionHandler> map;

    private abstract class ActionHandler {
        public String action;
        public String event;
        public int resultCode;

        /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.ActionHandler.1 */
        class C00571 implements B4ARunnable {
            private final /* synthetic */ Object[] val$o;

            C00571(Object[] objArr) {
                this.val$o = objArr;
            }

            public void run() {
                PhoneEvents.this.ba.raiseEvent(this, new StringBuilder(String.valueOf(PhoneEvents.this.ev)).append(ActionHandler.this.event).toString(), this.val$o);
            }
        }

        public abstract void handle(Intent intent);

        private ActionHandler() {
        }

        protected void send(Intent intent, Object[] args) {
            Object[] o;
            if (args == null) {
                o = new Object[1];
            } else {
                o = new Object[(args.length + 1)];
                System.arraycopy(args, 0, o, 0, args.length);
            }
            o[o.length - 1] = AbsObjectWrapper.ConvertToWrapper(new IntentWrapper(), intent);
            if (BA.debugMode) {
                BA.handler.post(new C00571(o));
            } else {
                PhoneEvents.this.ba.raiseEvent(this, new StringBuilder(String.valueOf(PhoneEvents.this.ev)).append(this.event).toString(), o);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.1 */
    class C00481 extends ActionHandler {
        C00481() {
            super(null);
            this.event = "_texttospeechfinish";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.2 */
    class C00492 extends ActionHandler {
        C00492() {
            super(null);
            this.event = "_connectivitychanged";
        }

        public void handle(Intent intent) {
            NetworkInfo ni = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            String type = ni.getTypeName();
            String state = ni.getState().toString();
            send(intent, new Object[]{type, state});
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.3 */
    class C00503 extends ActionHandler {
        C00503() {
            super(null);
            this.event = "_userpresent";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.4 */
    class C00514 extends ActionHandler {
        C00514() {
            super(null);
            this.event = "_shutdown";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.5 */
    class C00525 extends ActionHandler {
        C00525() {
            super(null);
            this.event = "_screenon";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.6 */
    class C00536 extends ActionHandler {
        C00536() {
            super(null);
            this.event = "_screenoff";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.7 */
    class C00547 extends ActionHandler {
        C00547() {
            super(null);
            this.event = "_packageremoved";
        }

        public void handle(Intent intent) {
            send(intent, new Object[]{intent.getDataString()});
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.8 */
    class C00558 extends ActionHandler {
        C00558() {
            super(null);
            this.event = "_packageadded";
        }

        public void handle(Intent intent) {
            send(intent, new Object[]{intent.getDataString()});
        }
    }

    /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.9 */
    class C00569 extends ActionHandler {
        C00569() {
            super(null);
            this.event = "_devicestoragelow";
        }

        public void handle(Intent intent) {
            send(intent, null);
        }
    }

    @ShortName("SmsInterceptor")
    public static class SMSInterceptor {
        private BA ba;
        private BroadcastReceiver br;
        private String eventName;

        /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.SMSInterceptor.1 */
        class C00581 extends ContentObserver {
            private final /* synthetic */ Uri val$content;

            C00581(Handler $anonymous0, Uri uri) {
                this.val$content = uri;
                super($anonymous0);
            }

            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Cursor cursor = BA.applicationContext.getContentResolver().query(this.val$content, null, null, null, null);
                if (cursor.moveToNext()) {
                    String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    if (protocol == null && type == 2) {
                        SMSInterceptor.this.ba.raiseEvent(null, new StringBuilder(String.valueOf(SMSInterceptor.this.eventName)).append("_messagesent").toString(), Integer.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                        cursor.close();
                    }
                }
            }
        }

        /* renamed from: anywheresoftware.b4a.phone.PhoneEvents.SMSInterceptor.2 */
        class C00592 extends BroadcastReceiver {
            private final /* synthetic */ BA val$ba;

            C00592(BA ba) {
                this.val$ba = ba;
            }

            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Object[] pduObj = (Object[]) bundle.get("pdus");
                        for (Object obj : pduObj) {
                            SmsMessage sm = SmsMessage.createFromPdu((byte[]) obj);
                            Boolean res = (Boolean) this.val$ba.raiseEvent(SMSInterceptor.this, new StringBuilder(String.valueOf(SMSInterceptor.this.eventName)).append("_messagereceived").toString(), sm.getOriginatingAddress(), sm.getMessageBody());
                            if (res != null && res.booleanValue()) {
                                abortBroadcast();
                            }
                        }
                    }
                }
            }
        }

        public void Initialize(String EventName, BA ba) {
            Initialize2(EventName, ba, 0);
        }

        public void ListenToOutgoingMessages() {
            Uri content = Uri.parse("content://sms");
            BA.applicationContext.getContentResolver().registerContentObserver(content, true, new C00581(new Handler(), content));
        }

        public void Initialize2(String EventName, BA ba, int Priority) {
            this.ba = ba;
            this.eventName = EventName.toLowerCase(BA.cul);
            this.br = new C00592(ba);
            IntentFilter fil = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            fil.setPriority(Priority);
            BA.applicationContext.registerReceiver(this.br, fil);
        }

        public void StopListening() {
            if (this.br != null) {
                BA.applicationContext.unregisterReceiver(this.br);
            }
            this.br = null;
        }
    }

    public PhoneEvents() {
        this.map = new HashMap();
        this.map.put("android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED", new C00481());
        this.map.put("android.net.conn.CONNECTIVITY_CHANGE", new C00492());
        this.map.put("android.intent.action.USER_PRESENT", new C00503());
        this.map.put("android.intent.action.ACTION_SHUTDOWN", new C00514());
        this.map.put("android.intent.action.SCREEN_ON", new C00525());
        this.map.put("android.intent.action.SCREEN_OFF", new C00536());
        this.map.put("android.intent.action.PACKAGE_REMOVED", new C00547());
        this.map.put("android.intent.action.PACKAGE_ADDED", new C00558());
        this.map.put("android.intent.action.DEVICE_STORAGE_LOW", new C00569());
        this.map.put("b4a.smssent", new ActionHandler() {
            {
                this.event = "_smssentstatus";
            }

            public void handle(Intent intent) {
                boolean z;
                String msg = "";
                switch (this.resultCode) {
                    case DialogResponse.POSITIVE /*-1*/:
                        msg = "OK";
                        break;
                    case WebViewExtras.GEOLOCATION_PERMISSION_ALLOW_AND_REMEMBER /*1*/:
                        msg = "GENERIC_FAILURE";
                        break;
                    case WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW /*2*/:
                        msg = "RADIO_OFF";
                        break;
                    case WebViewExtras.GEOLOCATION_PERMISSION_DISALLOW_AND_REMEMBER /*3*/:
                        msg = "NULL_PDU";
                        break;
                    case SmsWrapper.TYPE_OUTBOX /*4*/:
                        msg = "NO_SERVICE";
                        break;
                }
                Object[] objArr = new Object[3];
                if (this.resultCode == -1) {
                    z = true;
                } else {
                    z = false;
                }
                objArr[0] = Boolean.valueOf(z);
                objArr[1] = msg;
                objArr[2] = intent.getStringExtra("phone");
                send(intent, objArr);
            }
        });
        this.map.put("b4a.smsdelivered", new ActionHandler() {
            {
                this.event = "_smsdelivered";
            }

            public void handle(Intent intent) {
                send(intent, new Object[]{intent.getStringExtra("phone")});
            }
        });
        this.map.put("android.intent.action.DEVICE_STORAGE_OK", new ActionHandler() {
            {
                this.event = "_devicestorageok";
            }

            public void handle(Intent intent) {
                send(intent, null);
            }
        });
        this.map.put("android.intent.action.BATTERY_CHANGED", new ActionHandler() {
            {
                this.event = "_batterychanged";
            }

            public void handle(Intent intent) {
                boolean plugged;
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 1);
                if (intent.getIntExtra("plugged", 0) > 0) {
                    plugged = true;
                } else {
                    plugged = false;
                }
                send(intent, new Object[]{Integer.valueOf(level), Integer.valueOf(scale), Boolean.valueOf(plugged)});
            }
        });
        this.map.put("android.intent.action.AIRPLANE_MODE", new ActionHandler() {
            {
                this.event = "_airplanemodechanged";
            }

            public void handle(Intent intent) {
                boolean state = intent.getBooleanExtra("state", false);
                send(intent, new Object[]{Boolean.valueOf(state)});
            }
        });
        for (Entry<String, ActionHandler> e : this.map.entrySet()) {
            ((ActionHandler) e.getValue()).action = (String) e.getKey();
        }
    }

    public void InitializeWithPhoneState(BA ba, String EventName, PhoneId PhoneId) {
        String str = "android.intent.action.PHONE_STATE";
        String str2 = "android.intent.action.PHONE_STATE";
        this.map.put(str, new ActionHandler() {
            {
                this.event = "_phonestatechanged";
            }

            public void handle(Intent intent) {
                String state = intent.getStringExtra("state");
                String incomingNumber = intent.getStringExtra("incoming_number");
                if (incomingNumber == null) {
                    incomingNumber = "";
                }
                send(intent, new Object[]{state, incomingNumber});
            }
        });
        str2 = "android.intent.action.PHONE_STATE";
        str2 = "android.intent.action.PHONE_STATE";
        ((ActionHandler) this.map.get(str)).action = str;
        Initialize(ba, EventName);
    }

    public void Initialize(BA ba, String EventName) {
        this.ba = ba;
        this.ev = EventName.toLowerCase(BA.cul);
        StopListening();
        this.br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {
                    ActionHandler ah = (ActionHandler) PhoneEvents.this.map.get(intent.getAction());
                    if (ah != null) {
                        ah.resultCode = getResultCode();
                        ah.handle(intent);
                    }
                }
            }
        };
        IntentFilter f1 = new IntentFilter();
        IntentFilter f2 = null;
        for (ActionHandler ah : this.map.values()) {
            if (ba.subExists(this.ev + ah.event)) {
                if (ah.action == "android.intent.action.PACKAGE_ADDED" || ah.action == "android.intent.action.PACKAGE_REMOVED") {
                    if (f2 == null) {
                        f2 = new IntentFilter();
                        f2.addDataScheme("package");
                    }
                    f2.addAction(ah.action);
                }
                f1.addAction(ah.action);
            }
        }
        BA.applicationContext.registerReceiver(this.br, f1);
        if (f2 != null) {
            BA.applicationContext.registerReceiver(this.br, f2);
        }
    }

    public void StopListening() {
        if (this.br != null) {
            BA.applicationContext.unregisterReceiver(this.br);
        }
        this.br = null;
    }
}
