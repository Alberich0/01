package anywheresoftware.b4a.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.DynamicBuilder;
import java.util.HashMap;

@ShortName("Button")
@ActivityObject
public class ButtonWrapper extends TextViewWrapper<Button> {

    /* renamed from: anywheresoftware.b4a.objects.ButtonWrapper.1 */
    class C00191 implements OnTouchListener {
        private boolean down;
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00191(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
            this.down = false;
        }

        public boolean onTouch(View v, MotionEvent event) {
            String str = "_up";
            String str2 = "_down";
            String str3;
            if (event.getAction() == 0) {
                this.down = true;
                str3 = "_down";
                this.val$ba.raiseEventFromUI(ButtonWrapper.this.getObject(), this.val$eventName + str2, new Object[0]);
            } else if (this.down && (event.getAction() == 1 || event.getAction() == 3)) {
                this.down = false;
                str3 = "_up";
                this.val$ba.raiseEventFromUI(ButtonWrapper.this.getObject(), this.val$eventName + str, new Object[0]);
            } else if (event.getAction() == 2) {
                int[] states = v.getDrawableState();
                if (states == null) {
                    return false;
                }
                int i = 0;
                while (i < states.length) {
                    if (states[i] != 16842919) {
                        i++;
                    } else if (this.down) {
                        return false;
                    } else {
                        str3 = "_down";
                        this.val$ba.raiseEventFromUI(ButtonWrapper.this.getObject(), this.val$eventName + str2, new Object[0]);
                        this.down = true;
                        return false;
                    }
                }
                if (this.down) {
                    str3 = "_up";
                    this.val$ba.raiseEventFromUI(ButtonWrapper.this.getObject(), this.val$eventName + str, new Object[0]);
                    this.down = false;
                }
            }
            return false;
        }
    }

    @Hide
    public void innerInitialize(BA ba, String eventName, boolean keepOldObject) {
        if (!keepOldObject) {
            setObject(new Button(ba.context));
        }
        super.innerInitialize(ba, eventName, true);
        if (ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_down").toString()) || ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_up").toString())) {
            ((Button) getObject()).setOnTouchListener(new C00191(ba, eventName));
        }
    }

    @Hide
    public static View build(Object prev, HashMap<String, Object> props, boolean designer, Object tag) throws Exception {
        if (prev == null) {
            prev = ViewWrapper.buildNativeView((Context) tag, Button.class, props, designer);
        }
        TextView v = (TextView) TextViewWrapper.build(prev, props, designer);
        Drawable d = (Drawable) DynamicBuilder.build(prev, (HashMap) props.get("drawable"), designer, null);
        if (d != null) {
            v.setBackgroundDrawable(d);
        }
        if (designer) {
            v.setPressed(((Boolean) props.get("pressed")).booleanValue());
        }
        return v;
    }
}
