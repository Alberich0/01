package anywheresoftware.b4a;

import android.util.Log;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DynamicBuilder {
    public static <T> T build(Object prev, HashMap<String, Object> props, boolean designer, Object tag) {
        String cls = (String) props.get("type");
        try {
            if (cls.startsWith(".")) {
                cls = "anywheresoftware.b4a.objects" + cls;
            }
            return Class.forName(cls).getMethod("build", new Class[]{Object.class, HashMap.class, Boolean.TYPE, Object.class}).invoke(null, new Object[]{prev, props, Boolean.valueOf(designer), tag});
        } catch (InvocationTargetException e) {
            InvocationTargetException ie = e;
            String tt = "B4A";
            if (ie.getCause() instanceof FileNotFoundException) {
                tt = "";
            }
            Log.e(tt, "", ie);
            return null;
        } catch (Exception e2) {
            Log.e("B4A", "", e2);
            return null;
        }
    }
}
