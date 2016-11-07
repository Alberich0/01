package anywheresoftware.b4a.keywords.constants;

import android.graphics.Typeface;
import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;

@ShortName("Typeface")
public class TypefaceWrapper extends AbsObjectWrapper<Typeface> {
    public static final Typeface DEFAULT;
    public static final Typeface DEFAULT_BOLD;
    public static final Typeface MONOSPACE;
    public static final Typeface SANS_SERIF;
    public static final Typeface SERIF;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_BOLD_ITALIC = 3;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_NORMAL = 0;

    static {
        DEFAULT = Typeface.DEFAULT;
        DEFAULT_BOLD = Typeface.DEFAULT_BOLD;
        SANS_SERIF = Typeface.SANS_SERIF;
        SERIF = Typeface.SERIF;
        MONOSPACE = Typeface.MONOSPACE;
    }

    public static Typeface CreateNew(Typeface Typeface1, int Style) {
        return Typeface.create(Typeface1, Style);
    }

    public static Typeface LoadFromAssets(String FileName) {
        return Typeface.createFromAsset(BA.applicationContext.getAssets(), FileName.toLowerCase(BA.cul));
    }
}
