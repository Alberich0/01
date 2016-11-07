package anywheresoftware.b4a.keywords;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import android.widget.Toast;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.B4ARunnable;
import anywheresoftware.b4a.BA.DesignerName;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.RaisesSynchronousEvents;
import anywheresoftware.b4a.BA.SubDelegator;
import anywheresoftware.b4a.BA.Version;
import anywheresoftware.b4a.Msgbox;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.keywords.constants.Colors;
import anywheresoftware.b4a.keywords.constants.DialogResponse;
import anywheresoftware.b4a.keywords.constants.Gravity;
import anywheresoftware.b4a.keywords.constants.KeyCodes;
import anywheresoftware.b4a.keywords.constants.TypefaceWrapper;
import anywheresoftware.b4a.objects.B4AException;
import anywheresoftware.b4a.objects.LabelWrapper;
import anywheresoftware.b4a.objects.PanelWrapper;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4a.objects.collections.Map;
import anywheresoftware.b4a.objects.collections.Map.MyMap;
import anywheresoftware.b4a.objects.drawable.BitmapDrawable;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper;
import anywheresoftware.b4a.objects.streams.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;

@Version(3.0f)
@ActivityObject
public class Common {
    public static final Bit Bit;
    public static final String CRLF = "\n";
    public static final Colors Colors;
    public static final DateTime DateTime;
    public static final float Density;
    public static final DialogResponse DialogResponse;
    public static final boolean False = false;
    public static final File File;
    public static final Gravity Gravity;
    public static KeyCodes KeyCodes = null;
    public static final Object Null;
    public static final String QUOTE = "\"";
    public static final Regex Regex;
    public static final String TAB = "\t";
    public static final boolean True = true;
    public static final TypefaceWrapper Typeface;
    public static final double cE = 2.718281828459045d;
    public static final double cPI = 3.141592653589793d;
    private static Random random;

    /* renamed from: anywheresoftware.b4a.keywords.Common.1 */
    class C00091 implements OnMultiChoiceClickListener {
        private final /* synthetic */ Map val$Items;
        private final /* synthetic */ CharSequence[] val$items;

        C00091(Map map, CharSequence[] charSequenceArr) {
            this.val$Items = map;
            this.val$items = charSequenceArr;
        }

        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (isChecked) {
                this.val$Items.Put(this.val$items[which], Boolean.valueOf(Common.True));
            } else {
                this.val$Items.Put(this.val$items[which], Boolean.valueOf(Common.False));
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.keywords.Common.2 */
    class C00102 implements OnMultiChoiceClickListener {
        private final /* synthetic */ List val$result;

        C00102(List list) {
            this.val$result = list;
        }

        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            if (isChecked) {
                this.val$result.Add(Integer.valueOf(which));
                return;
            }
            this.val$result.RemoveAt(this.val$result.IndexOf(Integer.valueOf(which)));
        }
    }

    /* renamed from: anywheresoftware.b4a.keywords.Common.3 */
    class C00113 implements B4ARunnable {
        private final /* synthetic */ Object val$Service;
        private final /* synthetic */ BA val$mine;

        C00113(BA ba, Object obj) {
            this.val$mine = ba;
            this.val$Service = obj;
        }

        public void run() {
            try {
                this.val$mine.context.startService(Common.getComponentIntent(this.val$mine, this.val$Service));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.keywords.Common.4 */
    class C00124 implements Runnable {
        C00124() {
        }

        public void run() {
            Msgbox.isDismissing = Common.False;
        }
    }

    /* renamed from: anywheresoftware.b4a.keywords.Common.5 */
    class C00145 implements Runnable {
        int retries;
        private final /* synthetic */ Object[] val$Arguments;
        private final /* synthetic */ Object val$Component;
        private final /* synthetic */ String val$Sub;
        private final /* synthetic */ BA val$mine;

        /* renamed from: anywheresoftware.b4a.keywords.Common.5.1 */
        class C00131 implements Runnable {
            private final /* synthetic */ Object[] val$Arguments;
            private final /* synthetic */ String val$Sub;
            private final /* synthetic */ BA val$ba;

            C00131(BA ba, String str, Object[] objArr) {
                this.val$ba = ba;
                this.val$Sub = str;
                this.val$Arguments = objArr;
            }

            public void run() {
                this.val$ba.raiseEvent2(null, Common.True, this.val$Sub.toLowerCase(BA.cul), Common.True, this.val$Arguments);
            }
        }

        C00145(BA ba, Object obj, String str, Object[] objArr) {
            this.val$mine = ba;
            this.val$Component = obj;
            this.val$Sub = str;
            this.val$Arguments = objArr;
            this.retries = 5;
        }

        public void run() {
            try {
                BA ba = Common.getComponentBA(this.val$mine, this.val$Component);
                if (ba == null || ba.isActivityPaused()) {
                    if (this.val$Component instanceof B4AClass) {
                        Common.Log("Object context is paused. Ignoring CallSubDelayed: " + this.val$Sub);
                        return;
                    }
                    ComponentName cn = Common.getComponentIntent(this.val$mine, this.val$Component).getComponent();
                    if (cn == null) {
                        Common.Log("ComponentName = null");
                        return;
                    }
                    Class<?> cls = Class.forName(cn.getClassName());
                    Field f = cls.getDeclaredField("mostCurrent");
                    f.setAccessible(Common.True);
                    if (f.get(null) == null && this.retries == 5) {
                        if (Activity.class.isAssignableFrom(cls)) {
                            if (((Boolean) Class.forName(BA.packageName + ".main").getMethod("isAnyActivityVisible", null).invoke(null, null)).booleanValue()) {
                                Common.StartActivity(this.val$mine, this.val$Component);
                            } else {
                                this.retries = 0;
                            }
                        } else if (Service.class.isAssignableFrom(cls)) {
                            Common.Log("startService: " + this.val$Component.toString());
                            Common.StartService(this.val$mine, this.val$Component);
                        }
                    }
                    int i = this.retries - 1;
                    this.retries = i;
                    if (i > 0) {
                        BA.handler.postDelayed(this, 100);
                    } else if (ba != null) {
                        ba.addMessageToPausedMessageQueue("CallSubDelayed - " + this.val$Sub, new C00131(ba, this.val$Sub, this.val$Arguments));
                    } else {
                        BA.addMessageToUninitializeActivity(cn.getClassName(), this.val$Sub.toLowerCase(BA.cul), this.val$Arguments);
                    }
                } else if (BA.shellMode) {
                    ba.raiseEventFromDifferentThread(null, null, 0, this.val$Sub.toLowerCase(BA.cul), Common.True, this.val$Arguments);
                } else {
                    ba.raiseEvent2(null, Common.True, this.val$Sub.toLowerCase(BA.cul), Common.True, this.val$Arguments);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.keywords.Common.6 */
    class C00156 implements B4ARunnable {
        private final /* synthetic */ Runnable val$runnable;

        C00156(Runnable runnable) {
            this.val$runnable = runnable;
        }

        public void run() {
            this.val$runnable.run();
        }
    }

    @Hide
    public interface DesignerCustomView {
        void DesignerCreateView(PanelWrapper panelWrapper, LabelWrapper labelWrapper, Map map);

        void _initialize(BA ba, Object obj, String str);
    }

    static {
        Null = null;
        KeyCodes = null;
        Density = BA.density;
        Colors = null;
        Gravity = null;
        File = null;
        Bit = null;
        Typeface = null;
        DateTime = null;
        DialogResponse = null;
        Regex = null;
    }

    public static String NumberFormat(double Number, int MinimumIntegers, int MaximumFractions) {
        if (BA.numberFormat == null) {
            BA.numberFormat = NumberFormat.getInstance(Locale.US);
        }
        BA.numberFormat.setMaximumFractionDigits(MaximumFractions);
        BA.numberFormat.setMinimumIntegerDigits(MinimumIntegers);
        return BA.numberFormat.format(Number);
    }

    public static String NumberFormat2(double Number, int MinimumIntegers, int MaximumFractions, int MinimumFractions, boolean GroupingUsed) {
        if (BA.numberFormat == null) {
            BA.numberFormat = NumberFormat.getInstance(Locale.US);
        }
        BA.numberFormat.setMaximumFractionDigits(MaximumFractions);
        BA.numberFormat.setMinimumIntegerDigits(MinimumIntegers);
        BA.numberFormat.setMinimumFractionDigits(MinimumFractions);
        BA.numberFormat.setGroupingUsed(GroupingUsed);
        return BA.numberFormat.format(Number);
    }

    public static void Log(String Message) {
        BA.Log(Message);
    }

    public static void LogColor(String Message, int Color) {
        BA.addLogPrefix("c" + Color, Message);
    }

    public static Object Sender(BA ba) {
        return ba.getSender();
    }

    public static boolean Not(boolean Value) {
        return Value ? False : True;
    }

    public static void RndSeed(long Seed) {
        if (random == null) {
            random = new Random(Seed);
        } else {
            random.setSeed(Seed);
        }
    }

    public static int Rnd(int Min, int Max) {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt(Max - Min) + Min;
    }

    public static double Abs(double Number) {
        return Math.abs(Number);
    }

    @Hide
    public static int Abs(int Number) {
        return Math.abs(Number);
    }

    public static double Max(double Number1, double Number2) {
        return Math.max(Number1, Number2);
    }

    @Hide
    public static double Max(int Number1, int Number2) {
        return (double) Math.max(Number1, Number2);
    }

    public static double Min(double Number1, double Number2) {
        return Math.min(Number1, Number2);
    }

    @Hide
    public static double Min(int Number1, int Number2) {
        return (double) Math.min(Number1, Number2);
    }

    public static double Sin(double Radians) {
        return Math.sin(Radians);
    }

    public static double SinD(double Degrees) {
        return Math.sin((Degrees / 180.0d) * cPI);
    }

    public static double Cos(double Radians) {
        return Math.cos(Radians);
    }

    public static double CosD(double Degrees) {
        return Math.cos((Degrees / 180.0d) * cPI);
    }

    public static double Tan(double Radians) {
        return Math.tan(Radians);
    }

    public static double TanD(double Degrees) {
        return Math.tan((Degrees / 180.0d) * cPI);
    }

    public static double Power(double Base, double Exponent) {
        return Math.pow(Base, Exponent);
    }

    public static double Sqrt(double Value) {
        return Math.sqrt(Value);
    }

    public static double ASin(double Value) {
        return Math.asin(Value);
    }

    public static double ASinD(double Value) {
        return (Math.asin(Value) / cPI) * 180.0d;
    }

    public static double ACos(double Value) {
        return Math.acos(Value);
    }

    public static double ACosD(double Value) {
        return (Math.acos(Value) / cPI) * 180.0d;
    }

    public static double ATan(double Value) {
        return Math.atan(Value);
    }

    public static double ATanD(double Value) {
        return (Math.atan(Value) / cPI) * 180.0d;
    }

    public static double ATan2(double Y, double X) {
        return Math.atan2(Y, X);
    }

    public static double ATan2D(double Y, double X) {
        return (Math.atan2(Y, X) / cPI) * 180.0d;
    }

    public static double Logarithm(double Number, double Base) {
        return Math.log(Number) / Math.log(Base);
    }

    public static long Round(double Number) {
        return Math.round(Number);
    }

    public static double Round2(double Number, int DecimalPlaces) {
        double shift = Math.pow(10.0d, (double) DecimalPlaces);
        return ((double) Math.round(Number * shift)) / shift;
    }

    public static double Floor(double Number) {
        return Math.floor(Number);
    }

    public static double Ceil(double Number) {
        return Math.ceil(Number);
    }

    public static int Asc(char Char) {
        return Char;
    }

    public static char Chr(int UnicodeValue) {
        return (char) UnicodeValue;
    }

    @RaisesSynchronousEvents
    public static void DoEvents() {
        Msgbox.sendCloseMyLoopMessage();
        Msgbox.waitForMessage(False, True);
    }

    public static void ToastMessageShow(String Message, boolean LongDuration) {
        Toast.makeText(BA.applicationContext, Message, LongDuration ? 1 : 0).show();
    }

    @RaisesSynchronousEvents
    public static void Msgbox(String Message, String Title, BA ba) {
        String str = "";
        String str2 = "";
        str2 = "";
        Msgbox2(Message, Title, "OK", str, str, null, ba);
    }

    @RaisesSynchronousEvents
    public static int Msgbox2(String Message, String Title, String Positive, String Cancel, String Negative, Bitmap Icon, BA ba) {
        Builder b = new Builder(ba.context);
        Msgbox.DialogResponse dr = new Msgbox.DialogResponse(False);
        b.setTitle(Title).setMessage(Message);
        if (Positive.length() > 0) {
            b.setPositiveButton(Positive, dr);
        }
        if (Negative.length() > 0) {
            b.setNegativeButton(Negative, dr);
        }
        if (Cancel.length() > 0) {
            b.setNeutralButton(Cancel, dr);
        }
        if (Icon != null) {
            BitmapDrawable bd = new BitmapDrawable();
            bd.Initialize(Icon);
            b.setIcon((Drawable) bd.getObject());
        }
        Msgbox.msgbox(b.create(), False);
        return dr.res;
    }

    @RaisesSynchronousEvents
    public static int InputList(List Items, String Title, int CheckedItem, BA ba) {
        Builder b = new Builder(ba.context);
        CharSequence[] items = new CharSequence[Items.getSize()];
        for (int i = 0; i < Items.getSize(); i++) {
            Object o = Items.Get(i);
            if (o instanceof CharSequence) {
                items[i] = (CharSequence) o;
            } else {
                items[i] = String.valueOf(o);
            }
        }
        Msgbox.DialogResponse dr = new Msgbox.DialogResponse(True);
        b.setSingleChoiceItems(items, CheckedItem, dr);
        b.setTitle(Title);
        Msgbox.msgbox(b.create(), False);
        return dr.res;
    }

    @RaisesSynchronousEvents
    public static void InputMap(Map Items, String Title, BA ba) {
        Builder b = new Builder(ba.context);
        CharSequence[] items = new CharSequence[Items.getSize()];
        boolean[] checked = new boolean[Items.getSize()];
        int i = 0;
        for (Entry<Object, Object> e : ((MyMap) Items.getObject()).entrySet()) {
            if (e.getKey() instanceof String) {
                items[i] = (String) e.getKey();
                Object o = e.getValue();
                if (o instanceof Boolean) {
                    checked[i] = ((Boolean) o).booleanValue();
                } else {
                    checked[i] = Boolean.parseBoolean(String.valueOf(o));
                }
                i++;
            } else {
                throw new RuntimeException("Keys must be strings.");
            }
        }
        Msgbox.DialogResponse dr = new Msgbox.DialogResponse(False);
        b.setMultiChoiceItems(items, checked, new C00091(Items, items));
        b.setTitle(Title);
        b.setPositiveButton("Ok", dr);
        Msgbox.msgbox(b.create(), False);
    }

    @RaisesSynchronousEvents
    public static List InputMultiList(List Items, String Title, BA ba) {
        Builder b = new Builder(ba.context);
        CharSequence[] items = new CharSequence[Items.getSize()];
        for (int i = 0; i < Items.getSize(); i++) {
            Object o = Items.Get(i);
            if (o instanceof CharSequence) {
                items[i] = (CharSequence) o;
            } else {
                items[i] = String.valueOf(o);
            }
        }
        Msgbox.DialogResponse dr = new Msgbox.DialogResponse(False);
        List result = new List();
        result.Initialize();
        b.setMultiChoiceItems(items, null, new C00102(result));
        b.setTitle(Title);
        b.setPositiveButton("Ok", dr);
        Msgbox.msgbox(b.create(), False);
        if (dr.res != -1) {
            result.Clear();
        } else {
            result.Sort(True);
        }
        return result;
    }

    public static void ProgressDialogShow(BA ba, String Text) {
        ProgressDialogShow2(ba, Text, True);
    }

    public static void ProgressDialogShow2(BA ba, String Text, boolean Cancelable) {
        ProgressDialogHide();
        Msgbox.pd = new WeakReference(ProgressDialog.show(ba.context, "", Text, True, Cancelable));
    }

    public static void ProgressDialogHide() {
        Msgbox.dismissProgressDialog();
    }

    public static String GetType(Object object) {
        return object.getClass().getName();
    }

    public static int DipToCurrent(int Length) {
        return (int) (Density * ((float) Length));
    }

    public static int PerXToCurrent(float Percentage, BA ba) {
        return (int) ((Percentage / 100.0f) * ((float) ba.vg.getWidth()));
    }

    public static int PerYToCurrent(float Percentage, BA ba) {
        return (int) ((Percentage / 100.0f) * ((float) ba.vg.getHeight()));
    }

    public static boolean IsNumber(String Text) {
        try {
            Double.parseDouble(Text);
            return True;
        } catch (NumberFormatException e) {
            return False;
        }
    }

    public static B4AException LastException(BA ba) {
        B4AException e = new B4AException();
        e.setObject(ba.getLastException());
        return e;
    }

    public static LayoutValues GetDeviceLayoutValues(BA ba) {
        DisplayMetrics dm = BA.applicationContext.getResources().getDisplayMetrics();
        LayoutValues deviceValues = new LayoutValues();
        deviceValues.Scale = dm.density;
        deviceValues.Width = dm.widthPixels;
        deviceValues.Height = dm.heightPixels;
        return deviceValues;
    }

    public static void StartActivity(BA mine, Object Activity) throws ClassNotFoundException {
        Intent i = getComponentIntent(mine, Activity);
        if (mine.context instanceof Activity) {
            i.addFlags(131072);
        } else {
            i.addFlags(268435456);
        }
        mine.context.startActivity(i);
    }

    public static void StartService(BA mine, Object Service) throws ClassNotFoundException {
        if (BA.shellMode) {
            BA.handler.post(new C00113(mine, Service));
            return;
        }
        BA.handler.post(new C00124());
        mine.context.startService(getComponentIntent(mine, Service));
        Msgbox.isDismissing = True;
    }

    public static void StartServiceAt(BA mine, Object Service, long Time, boolean DuringSleep) throws ClassNotFoundException {
        int i;
        AlarmManager am = (AlarmManager) BA.applicationContext.getSystemService("alarm");
        PendingIntent pi = PendingIntent.getService(mine.context, 1, getComponentIntent(mine, Service), 134217728);
        if (DuringSleep) {
            i = 0;
        } else {
            i = 1;
        }
        am.set(i, Time, pi);
    }

    public static void CancelScheduledService(BA mine, Object Service) throws ClassNotFoundException {
        ((AlarmManager) BA.applicationContext.getSystemService("alarm")).cancel(PendingIntent.getService(mine.context, 1, getComponentIntent(mine, Service), 134217728));
    }

    @Hide
    public static Intent getComponentIntent(BA mine, Object component) throws ClassNotFoundException {
        if (component instanceof Class) {
            return new Intent(mine.context, (Class) component);
        }
        if (component == null || component.toString().length() == 0) {
            return new Intent(mine.context, Class.forName(mine.className));
        }
        if (component instanceof String) {
            return new Intent(mine.context, Class.forName(BA.packageName + "." + ((String) component).toLowerCase(BA.cul)));
        }
        return (Intent) component;
    }

    public static void StopService(BA mine, Object Service) throws ClassNotFoundException {
        mine.context.stopService(getComponentIntent(mine, Service));
    }

    public static boolean SubExists(BA mine, Object Object, String Sub) throws IllegalArgumentException, SecurityException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        if (Object == null) {
            return False;
        }
        BA ba = getComponentBA(mine, Object);
        if (ba != null) {
            return ba.subExists(Sub.toLowerCase(BA.cul));
        }
        return False;
    }

    @DesignerName("CallSub")
    @RaisesSynchronousEvents
    public static Object CallSubNew(BA mine, Object Component, String Sub) {
        return CallSub4(False, mine, Component, Sub, null);
    }

    @DesignerName("CallSub2")
    @RaisesSynchronousEvents
    public static Object CallSubNew2(BA mine, Object Component, String Sub, Object Argument) {
        return CallSub4(False, mine, Component, Sub, new Object[]{Argument});
    }

    @DesignerName("CallSub3")
    @RaisesSynchronousEvents
    public static Object CallSubNew3(BA mine, Object Component, String Sub, Object Argument1, Object Argument2) {
        return CallSub4(False, mine, Component, Sub, new Object[]{Argument1, Argument2});
    }

    @Hide
    public static String CallSub(BA mine, Object Component, String Sub) {
        return (String) CallSub4(True, mine, Component, Sub, null);
    }

    @Hide
    public static String CallSub2(BA mine, Object Component, String Sub, Object Argument) {
        return (String) CallSub4(True, mine, Component, Sub, new Object[]{Argument});
    }

    @Hide
    public static String CallSub3(BA mine, Object Component, String Sub, Object Argument1, Object Argument2) {
        return (String) CallSub4(True, mine, Component, Sub, new Object[]{Argument1, Argument2});
    }

    private static Object CallSub4(boolean old, BA mine, Object Component, String Sub, Object[] Arguments) {
        Object obj = null;
        try {
            if (Component instanceof SubDelegator) {
                obj = ((SubDelegator) Component).callSub(Sub, mine.eventsTarget, Arguments);
                if (obj == SubDelegator.SubNotFound) {
                    obj = null;
                } else if (obj == null || !(obj instanceof ObjectWrapper)) {
                    return obj;
                } else {
                    return ((ObjectWrapper) obj).getObject();
                }
            }
            BA ba = getComponentBA(mine, Component);
            if (ba != null) {
                boolean isTargetClass = Component instanceof B4AClass;
                obj = ba.raiseEvent2(mine.eventsTarget, isTargetClass, Sub.toLowerCase(BA.cul), isTargetClass, Arguments);
            }
            if (!old) {
                return (obj == null || !(obj instanceof ObjectWrapper)) ? obj : ((ObjectWrapper) obj).getObject();
            } else {
                Object obj2;
                if (obj == null) {
                    obj2 = "";
                } else {
                    obj2 = obj;
                }
                return String.valueOf(obj2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void CallSubDelayed(BA mine, Object Component, String Sub) {
        CallSubDelayed4(mine, Component, Sub, null);
    }

    public static void CallSubDelayed2(BA mine, Object Component, String Sub, Object Argument) {
        CallSubDelayed4(mine, Component, Sub, new Object[]{Argument});
    }

    public static void CallSubDelayed3(BA mine, Object Component, String Sub, Object Argument1, Object Argument2) {
        CallSubDelayed4(mine, Component, Sub, new Object[]{Argument1, Argument2});
    }

    private static void CallSubDelayed4(BA mine, Object Component, String Sub, Object[] Arguments) {
        Runnable runnable = new C00145(mine, Component, Sub, Arguments);
        if (BA.shellMode) {
            BA.handler.post(new C00156(runnable));
        } else {
            BA.handler.post(runnable);
        }
    }

    public static boolean IsPaused(BA mine, Object Component) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        BA ba = getComponentBA(mine, Component);
        return (ba == null || ba.isActivityPaused()) ? True : False;
    }

    private static BA getComponentBA(BA mine, Object Component) throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        Class<?> c;
        if (Component instanceof Class) {
            c = (Class) Component;
        } else if (Component instanceof B4AClass) {
            return ((B4AClass) Component).getBA();
        } else {
            if (Component == null || Component.toString().length() == 0) {
                return mine;
            }
            c = Class.forName(BA.packageName + "." + ((String) Component).toLowerCase(BA.cul));
        }
        return (BA) c.getField("processBA").get(null);
    }

    public static String CharsToString(char[] Chars, int StartOffset, int Length) {
        return new String(Chars, StartOffset, Length);
    }

    public static String BytesToString(byte[] Data, int StartOffset, int Length, String CharSet) throws UnsupportedEncodingException {
        return new String(Data, StartOffset, Length, CharSet);
    }

    @Hide
    public static List ArrayToList(Object[] Array) {
        List list = new List();
        list.setObject(Arrays.asList(Array));
        return list;
    }

    @Hide
    public static List ArrayToList(int[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Integer.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(long[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Long.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(float[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Float.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(double[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Double.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(boolean[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Boolean.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(short[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Short.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    @Hide
    public static List ArrayToList(byte[] Array) {
        List list = new List();
        Object[] o = new Object[Array.length];
        for (int i = 0; i < Array.length; i++) {
            o[i] = Byte.valueOf(Array[i]);
        }
        list.setObject(Arrays.asList(o));
        return list;
    }

    public static boolean IsBackgroundTaskRunning(BA ba, Object ContainerObject, int TaskId) {
        return BA.isTaskRunning(ContainerObject, TaskId);
    }

    public static BitmapWrapper LoadBitmap(String Dir, String FileName) throws IOException {
        BitmapWrapper bw = new BitmapWrapper();
        bw.Initialize(Dir, FileName);
        return bw;
    }

    public static BitmapWrapper LoadBitmapSample(String Dir, String FileName, int MaxWidth, int MaxHeight) throws IOException {
        BitmapWrapper bw = new BitmapWrapper();
        bw.InitializeSample(Dir, FileName, MaxWidth, MaxHeight);
        return bw;
    }

    public static void Array() {
    }

    public static void If() {
    }

    public static void Try() {
    }

    public static void Catch() {
    }

    public static void Dim() {
    }

    public static void While() {
    }

    public static void Until() {
    }

    public static void For() {
    }

    public static void Type() {
    }

    public static void Return() {
    }

    public static void Sub() {
    }

    public static void Exit() {
    }

    public static void Continue() {
    }

    public static void Select() {
    }

    public static void Is() {
    }

    public static void ExitApplication() {
        System.exit(0);
    }

    public static RemoteViews ConfigureHomeWidget(String LayoutFile, String EventName, int UpdateIntervalMinutes, String WidgetName, boolean CenterWidget) {
        return null;
    }

    public static Object Me(BA ba) {
        return null;
    }
}
