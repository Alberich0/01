package anywheresoftware.b4a.keywords;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.ConnectorUtils;
import anywheresoftware.b4a.DynamicBuilder;
import anywheresoftware.b4a.objects.ActivityWrapper;
import anywheresoftware.b4a.objects.CustomViewWrapper;
import anywheresoftware.b4a.objects.ViewWrapper;
import anywheresoftware.b4a.objects.streams.File;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Hide
public class LayoutBuilder {
    private static double autoscale;
    private static HashMap<String, WeakReference<MapAndCachedStrings>> cachedLayouts;
    private static List<CustomViewWrapper> customViewWrappers;
    private static double screenSize;
    private static BA tempBA;
    private static HashMap<String, Object> viewsToSendInShellMode;

    @Hide
    public interface DesignerTextSizeMethod {
        float getTextSize();

        void setTextSize(float f);
    }

    @Hide
    public static class LayoutHashMap<K, V> extends HashMap<K, V> {
        public V get(Object key) {
            V v = super.get(key);
            if (v != null) {
                return v;
            }
            throw new RuntimeException("Cannot find view: " + key.toString() + "\nAll views in script should be declared.");
        }
    }

    public static class LayoutValuesAndMap {
        public final LayoutValues layoutValues;
        public final HashMap<String, ViewWrapper<?>> map;

        public LayoutValuesAndMap(LayoutValues layoutValues, HashMap<String, ViewWrapper<?>> map) {
            this.layoutValues = layoutValues;
            this.map = map;
        }
    }

    private static class MapAndCachedStrings {
        public final String[] cachedStrings;
        public final HashMap<String, Object> map;

        public MapAndCachedStrings(HashMap<String, Object> map, String[] cachedStrings) {
            this.map = map;
            this.cachedStrings = cachedStrings;
        }
    }

    static {
        cachedLayouts = new HashMap();
        screenSize = 0.0d;
    }

    public static LayoutValuesAndMap loadLayout(String file, BA ba, boolean isActivity, ViewGroup parent, HashMap<String, ViewWrapper<?>> dynamicTable, boolean d4a) throws IOException {
        IOException e;
        Object obj;
        Throwable e2;
        try {
            MapAndCachedStrings mcs;
            int pos;
            int numberOfVariants;
            LayoutValues chosen;
            float distance;
            int i;
            int variantIndex;
            LayoutValues test;
            float testDistance;
            Activity activity;
            Object obj2;
            StringBuilder stringBuilder;
            BA mcs2;
            String str;
            HashMap<String, ViewWrapper<?>> din;
            LayoutValuesAndMap layoutValuesAndMap;
            tempBA = ba;
            if (!d4a) {
                file = file.toLowerCase(BA.cul);
            }
            if (!file.endsWith(".bal")) {
                file = new StringBuilder(String.valueOf(file)).append(".bal").toString();
            }
            WeakReference<MapAndCachedStrings> cl = (WeakReference) cachedLayouts.get(file);
            if (cl != null) {
                mcs = (MapAndCachedStrings) cl.get();
            } else {
                mcs = null;
            }
            DataInputStream din2 = new DataInputStream((InputStream) File.OpenInput(File.getDirAssets(), file).getObject());
            MapAndCachedStrings mcs3 = ConnectorUtils.readInt(din2);
            for (pos = ConnectorUtils.readInt(din2); pos > 0; pos = (int) (((long) pos) - din2.skip((long) pos))) {
            }
            LayoutValues device = (String[]) 0;
            if (mcs3 >= 3) {
                if (mcs != null) {
                    pos = mcs.cachedStrings;
                    ConnectorUtils.readInt(din2);
                    for (mcs3 = null; mcs3 < pos.length; mcs3++) {
                        din2.skipBytes(ConnectorUtils.readInt(din2));
                    }
                    mcs3 = pos;
                    numberOfVariants = ConnectorUtils.readInt(din2);
                    chosen = null;
                    device = Common.GetDeviceLayoutValues(ba);
                    distance = Float.MAX_VALUE;
                    i = 0;
                    variantIndex = 0;
                    while (i < numberOfVariants) {
                        test = LayoutValues.readFromStream(din2);
                        if (chosen != null) {
                            chosen = test;
                            distance = test.calcDistance(device);
                            test = i;
                        } else {
                            testDistance = test.calcDistance(device);
                            if (testDistance >= distance) {
                                chosen = test;
                                distance = testDistance;
                                test = i;
                            } else {
                                test = variantIndex;
                            }
                        }
                        i++;
                        variantIndex = test;
                    }
                    BALayout.setUserScale(chosen.Scale);
                    if (dynamicTable == null) {
                        testDistance = new LayoutHashMap();
                        if (mcs == null) {
                            try {
                                pos = mcs.map;
                            } catch (IOException e3) {
                                e = e3;
                                obj = testDistance;
                                try {
                                    throw e;
                                } catch (Throwable th) {
                                    ba = th;
                                }
                            } catch (Exception e4) {
                                e2 = e4;
                                obj = testDistance;
                                throw new RuntimeException(e2);
                            } catch (Throwable th2) {
                                ba = th2;
                                obj = testDistance;
                                tempBA = null;
                                customViewWrappers = null;
                                throw ba;
                            }
                        }
                        pos = ConnectorUtils.readMap(din2, mcs3);
                        cachedLayouts.put(file, new WeakReference(new MapAndCachedStrings(pos, mcs3)));
                        if (ba.eventsTarget != null) {
                            activity = ba.activity;
                        } else {
                            obj2 = ba.eventsTarget;
                        }
                        stringBuilder = new StringBuilder("variant");
                        loadLayoutHelper(pos, ba, obj2, parent, isActivity, stringBuilder.append(variantIndex).toString(), true, testDistance, d4a);
                        if (BA.shellMode && viewsToSendInShellMode != null) {
                            mcs2 = ba;
                            mcs2.raiseEvent2(null, true, "SEND_VIEWS_AFTER_LAYOUT", true, viewsToSendInShellMode);
                            viewsToSendInShellMode = null;
                        }
                        dynamicTable = testDistance;
                    }
                    din2.close();
                    if (isActivity && parent.getLayoutParams()) {
                        str = file;
                        din = dynamicTable;
                        runScripts(str, chosen, din, parent.getLayoutParams().width, parent.getLayoutParams().height, Common.Density, d4a);
                    } else {
                        str = file;
                        din = dynamicTable;
                        runScripts(str, chosen, din, ba.vg.getWidth(), ba.vg.getHeight(), Common.Density, d4a);
                    }
                    BALayout.setUserScale(1.0f);
                    if (customViewWrappers != null) {
                        for (CustomViewWrapper cvw : customViewWrappers) {
                            cvw.AfterDesignerScript();
                        }
                    }
                    layoutValuesAndMap = new LayoutValuesAndMap(chosen, dynamicTable);
                    tempBA = null;
                    customViewWrappers = null;
                    return layoutValuesAndMap;
                }
                device = new String[ConnectorUtils.readInt(din2)];
                for (mcs3 = null; mcs3 < device.length; mcs3++) {
                    device[mcs3] = ConnectorUtils.readString(din2);
                }
            }
            Object mcs4 = device;
            numberOfVariants = ConnectorUtils.readInt(din2);
            chosen = null;
            device = Common.GetDeviceLayoutValues(ba);
            distance = Float.MAX_VALUE;
            i = 0;
            variantIndex = 0;
            while (i < numberOfVariants) {
                test = LayoutValues.readFromStream(din2);
                if (chosen != null) {
                    testDistance = test.calcDistance(device);
                    if (testDistance >= distance) {
                        test = variantIndex;
                    } else {
                        chosen = test;
                        distance = testDistance;
                        test = i;
                    }
                } else {
                    chosen = test;
                    distance = test.calcDistance(device);
                    test = i;
                }
                i++;
                variantIndex = test;
            }
            BALayout.setUserScale(chosen.Scale);
            if (dynamicTable == null) {
                testDistance = new LayoutHashMap();
                if (mcs == null) {
                    pos = ConnectorUtils.readMap(din2, mcs3);
                    cachedLayouts.put(file, new WeakReference(new MapAndCachedStrings(pos, mcs3)));
                } else {
                    pos = mcs.map;
                }
                if (ba.eventsTarget != null) {
                    obj2 = ba.eventsTarget;
                } else {
                    activity = ba.activity;
                }
                stringBuilder = new StringBuilder("variant");
                loadLayoutHelper(pos, ba, obj2, parent, isActivity, stringBuilder.append(variantIndex).toString(), true, testDistance, d4a);
                mcs2 = ba;
                mcs2.raiseEvent2(null, true, "SEND_VIEWS_AFTER_LAYOUT", true, viewsToSendInShellMode);
                viewsToSendInShellMode = null;
                dynamicTable = testDistance;
            }
            din2.close();
            if (isActivity) {
            }
            str = file;
            din = dynamicTable;
            runScripts(str, chosen, din, ba.vg.getWidth(), ba.vg.getHeight(), Common.Density, d4a);
            BALayout.setUserScale(1.0f);
            if (customViewWrappers != null) {
                while (r20.hasNext()) {
                    cvw.AfterDesignerScript();
                }
            }
            layoutValuesAndMap = new LayoutValuesAndMap(chosen, dynamicTable);
            tempBA = null;
            customViewWrappers = null;
            return layoutValuesAndMap;
        } catch (IOException e5) {
            e = e5;
        } catch (Exception e6) {
            e2 = e6;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void runScripts(java.lang.String r4, anywheresoftware.b4a.keywords.LayoutValues r5, java.util.HashMap<java.lang.String, anywheresoftware.b4a.objects.ViewWrapper<?>> r6, int r7, int r8, float r9, boolean r10) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
        /*
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r10 = "LS_";
        r1.append(r10);
        r10 = 0;
        r0 = r10;
    L_0x000c:
        r10 = r4.length();
        r2 = 4;
        r10 = r10 - r2;
        if (r0 < r10) goto L_0x00b3;
    L_0x0014:
        r4 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = anywheresoftware.b4a.BA.packageName;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = java.lang.String.valueOf(r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4.<init>(r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = ".designerscripts.";
        r4 = r4.append(r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = r1.toString();	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4 = r4.append(r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4 = r4.toString();	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4 = java.lang.Class.forName(r4);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = 0;
        r10 = variantToMethod(r10);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r0 = 4;
        r0 = new java.lang.Class[r0];	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1 = 0;
        r2 = java.util.HashMap.class;
        r0[r1] = r2;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1 = 1;
        r2 = java.lang.Integer.TYPE;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r0[r1] = r2;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1 = 2;
        r2 = java.lang.Integer.TYPE;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r0[r1] = r2;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1 = 3;
        r2 = java.lang.Float.TYPE;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r0[r1] = r2;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r10 = r4.getMethod(r10, r0);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r0 = 0;
        r1 = 4;
        r1 = new java.lang.Object[r1];	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r2 = 0;
        r1[r2] = r6;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r2 = 1;
        r3 = java.lang.Integer.valueOf(r7);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1[r2] = r3;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r2 = 2;
        r3 = java.lang.Integer.valueOf(r8);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1[r2] = r3;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r2 = 3;
        r3 = java.lang.Float.valueOf(r9);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r1[r2] = r3;	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
        r10.invoke(r0, r1);	 Catch:{ NoSuchMethodException -> 0x00df, ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, InvocationTargetException -> 0x00d0 }
    L_0x0074:
        r5 = variantToMethod(r5);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10 = 4;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r0 = 0;
        r1 = java.util.HashMap.class;
        r10[r0] = r1;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r0 = 1;
        r1 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r0] = r1;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r0 = 2;
        r1 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r0] = r1;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r0 = 3;
        r1 = java.lang.Float.TYPE;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r0] = r1;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4 = r4.getMethod(r5, r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r5 = 0;
        r10 = 4;
        r10 = new java.lang.Object[r10];	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r0 = 0;
        r10[r0] = r6;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r6 = 1;
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r6] = r7;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r6 = 2;
        r7 = java.lang.Integer.valueOf(r8);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r6] = r7;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r6 = 3;
        r7 = java.lang.Float.valueOf(r9);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r10[r6] = r7;	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
        r4.invoke(r5, r10);	 Catch:{ ClassNotFoundException -> 0x00dd, SecurityException -> 0x00cb, NoSuchMethodException -> 0x00db, InvocationTargetException -> 0x00d0 }
    L_0x00b2:
        return;
    L_0x00b3:
        r10 = r4.charAt(r0);
        r2 = java.lang.Character.isLetterOrDigit(r10);
        if (r2 == 0) goto L_0x00c5;
    L_0x00bd:
        r1.append(r10);
    L_0x00c0:
        r10 = r0 + 1;
        r0 = r10;
        goto L_0x000c;
    L_0x00c5:
        r10 = "_";
        r1.append(r10);
        goto L_0x00c0;
    L_0x00cb:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00b2;
    L_0x00d0:
        r4 = move-exception;
        r5 = new java.lang.RuntimeException;
        r4 = r4.getCause();
        r5.<init>(r4);
        throw r5;
    L_0x00db:
        r4 = move-exception;
        goto L_0x00b2;
    L_0x00dd:
        r4 = move-exception;
        goto L_0x00b2;
    L_0x00df:
        r10 = move-exception;
        goto L_0x0074;
        */
        throw new UnsupportedOperationException("Method not decompiled: anywheresoftware.b4a.keywords.LayoutBuilder.runScripts(java.lang.String, anywheresoftware.b4a.keywords.LayoutValues, java.util.HashMap, int, int, float, boolean):void");
    }

    public static void setScaleRate(double rate) {
        autoscale = 1.0d + (rate * ((double) ((((float) (tempBA.vg.getWidth() + tempBA.vg.getHeight())) / (750.0f * Common.Density)) - 1.0f)));
        screenSize = 0.0d;
    }

    public static double getScreenSize() {
        if (screenSize == 0.0d) {
            screenSize = (Math.sqrt(Math.pow((double) tempBA.vg.getWidth(), 2.0d) + Math.pow((double) tempBA.vg.getHeight(), 2.0d)) / 160.0d) / ((double) Common.Density);
        }
        return screenSize;
    }

    public static void scaleAll(HashMap<String, ViewWrapper<?>> views) {
        for (ViewWrapper<?> v : views.values()) {
            if (v.IsInitialized() && !(v instanceof ActivityWrapper)) {
                scaleView(v);
            }
        }
    }

    public static void scaleView(ViewWrapper<?> v) {
        v.setLeft((int) (((double) v.getLeft()) * autoscale));
        v.setTop((int) (((double) v.getTop()) * autoscale));
        v.setWidth((int) (((double) v.getWidth()) * autoscale));
        v.setHeight((int) (((double) v.getHeight()) * autoscale));
        if (v instanceof DesignerTextSizeMethod) {
            DesignerTextSizeMethod t = (DesignerTextSizeMethod) v;
            t.setTextSize((float) (((double) t.getTextSize()) * autoscale));
        }
    }

    private static String variantToMethod(LayoutValues lv) {
        String variant;
        CharSequence charSequence = "_";
        if (lv == null) {
            variant = "general";
        } else {
            String str = "_";
            String str2 = "_";
            variant = new StringBuilder(String.valueOf(String.valueOf(lv.Width))).append("x").append(String.valueOf(lv.Height)).append(charSequence).append(BA.NumberToString(lv.Scale).replace(".", charSequence)).toString();
        }
        return "LS_" + variant;
    }

    private static void loadLayoutHelper(HashMap<String, Object> i, BA ba, Object fieldsTarget, ViewGroup parent, boolean root, String currentVariant, boolean firstCall, HashMap<String, ViewWrapper<?>> dynamicTable, boolean d4a) throws Exception {
        View o;
        HashMap<String, Object> variant = (HashMap) i.get(currentVariant);
        if (root || !firstCall) {
            ViewGroup act = root ? parent : null;
            i.put("left", variant.get("left"));
            i.put("top", variant.get("top"));
            i.put("width", variant.get("width"));
            i.put("height", variant.get("height"));
            View o2 = (View) DynamicBuilder.build(act, i, false, parent.getContext());
            if (root) {
                o = o2;
            } else {
                String name = ((String) i.get("name")).toLowerCase(BA.cul);
                String cls = (String) i.get("type");
                if (cls.startsWith(".")) {
                    cls = "anywheresoftware.b4a.objects" + cls;
                }
                ViewWrapper ow = (ViewWrapper) Class.forName(cls).newInstance();
                dynamicTable.put(name, ow);
                Object obj = ow;
                if (ow instanceof CustomViewWrapper) {
                    if (customViewWrappers == null) {
                        customViewWrappers = new ArrayList();
                    }
                    customViewWrappers.add((CustomViewWrapper) ow);
                    cls = (String) i.get("customType");
                    if (cls == null || cls.length() == 0) {
                        throw new RuntimeException("CustomView CustomType property was not set.");
                    }
                    obj = Class.forName(cls).newInstance();
                    CustomViewWrapper cvw = (CustomViewWrapper) ow;
                    cvw.customObject = obj;
                    cvw.props = i;
                    obj = obj;
                }
                if (!d4a) {
                    if (BA.shellMode) {
                        if (viewsToSendInShellMode == null) {
                            viewsToSendInShellMode = new HashMap();
                        }
                        viewsToSendInShellMode.put(name, obj);
                    } else {
                        try {
                            Field field = fieldsTarget.getClass().getField("_" + name);
                            if (field != null) {
                                field.set(fieldsTarget, obj);
                            }
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Field " + name + " was declared with the wrong type.");
                        } catch (NoSuchFieldException e2) {
                        }
                    }
                }
                ow.setObject(o2);
                if (!d4a) {
                    ow.innerInitialize(ba, ((String) i.get("eventName")).toLowerCase(BA.cul), true);
                }
                parent.addView(o2, o2.getLayoutParams());
                o = o2;
            }
        } else {
            View o3 = parent;
            parent.setBackgroundDrawable((Drawable) DynamicBuilder.build(parent, (HashMap) i.get("drawable"), false, null));
            o = o3;
        }
        HashMap parent2 = (HashMap) i.get(":kids");
        if (parent2 != null) {
            for (i = null; i < parent2.size(); i++) {
                BA o4 = ba;
                Object obj2 = fieldsTarget;
                loadLayoutHelper((HashMap) parent2.get(String.valueOf(i)), o4, obj2, (ViewGroup) o, false, currentVariant, false, dynamicTable, d4a);
            }
        }
    }
}
