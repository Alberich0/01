package anywheresoftware.b4a.objects;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import java.util.HashMap;

@ShortName("SeekBar")
@ActivityObject
public class SeekBarWrapper extends ViewWrapper<SeekBar> {

    /* renamed from: anywheresoftware.b4a.objects.SeekBarWrapper.1 */
    class C00291 implements OnSeekBarChangeListener {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00291(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.val$ba.raiseEventFromUI(SeekBarWrapper.this.getObject(), this.val$eventName + "_valuechanged", Integer.valueOf(progress), Boolean.valueOf(fromUser));
        }

        public void onStartTrackingTouch(SeekBar arg0) {
        }

        public void onStopTrackingTouch(SeekBar arg0) {
        }
    }

    @Hide
    public void innerInitialize(BA ba, String eventName, boolean keepOldObject) {
        if (!keepOldObject) {
            setObject(new SeekBar(ba.context));
        }
        super.innerInitialize(ba, eventName, true);
        if (ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_valuechanged").toString())) {
            ((SeekBar) getObject()).setOnSeekBarChangeListener(new C00291(ba, eventName));
        }
    }

    public int getMax() {
        return ((SeekBar) getObject()).getMax();
    }

    public void setMax(int value) {
        ((SeekBar) getObject()).setMax(value);
    }

    public int getValue() {
        return ((SeekBar) getObject()).getProgress();
    }

    public void setValue(int value) {
        ((SeekBar) getObject()).setProgress(value);
    }

    @Hide
    public static View build(Object prev, HashMap<String, Object> props, boolean designer, Object tag) throws Exception {
        if (prev == null) {
            prev = ViewWrapper.buildNativeView((Context) tag, SeekBar.class, props, designer);
        }
        SeekBar v = (SeekBar) ViewWrapper.build(prev, props, designer);
        int oldMax = v.getMax();
        v.setMax(((Integer) props.get("max")).intValue());
        if (v.getMax() != oldMax) {
            v.setProgress(-1);
        }
        v.setProgress(((Integer) props.get("value")).intValue());
        return v;
    }
}
