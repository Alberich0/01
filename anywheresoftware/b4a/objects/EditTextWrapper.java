package anywheresoftware.b4a.objects;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ActivityObject;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.RaisesSynchronousEvents;
import anywheresoftware.b4a.BA.ShortName;
import java.util.HashMap;

@ShortName("EditText")
@ActivityObject
public class EditTextWrapper extends TextViewWrapper<EditText> {
    public static final int INPUT_TYPE_DECIMAL_NUMBERS = 12290;
    public static final int INPUT_TYPE_NONE = 0;
    public static final int INPUT_TYPE_NUMBERS = 2;
    public static final int INPUT_TYPE_PHONE = 3;
    public static final int INPUT_TYPE_TEXT = 1;

    /* renamed from: anywheresoftware.b4a.objects.EditTextWrapper.1 */
    class C00211 implements TextWatcher {
        private CharSequence old;
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00211(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public void afterTextChanged(Editable s) {
            BA ba = this.val$ba;
            Object object = EditTextWrapper.this.getObject();
            String str = this.val$eventName + "_textchanged";
            Object[] objArr = new Object[EditTextWrapper.INPUT_TYPE_NUMBERS];
            objArr[EditTextWrapper.INPUT_TYPE_NONE] = this.old;
            objArr[EditTextWrapper.INPUT_TYPE_TEXT] = ((EditText) EditTextWrapper.this.getObject()).getText().toString();
            ba.raiseEvent2(object, false, str, true, objArr);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            this.old = s.toString();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.EditTextWrapper.2 */
    class C00222 implements OnEditorActionListener {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00222(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            this.val$ba.raiseEvent(EditTextWrapper.this.getObject(), this.val$eventName + "_enterpressed", new Object[EditTextWrapper.INPUT_TYPE_NONE]);
            return false;
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.EditTextWrapper.3 */
    class C00233 implements OnFocusChangeListener {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00233(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public void onFocusChange(View v, boolean hasFocus) {
            BA ba = this.val$ba;
            Object object = EditTextWrapper.this.getObject();
            String str = this.val$eventName + "_focuschanged";
            Object[] objArr = new Object[EditTextWrapper.INPUT_TYPE_TEXT];
            objArr[EditTextWrapper.INPUT_TYPE_NONE] = Boolean.valueOf(hasFocus);
            ba.raiseEventFromUI(object, str, objArr);
        }
    }

    @Hide
    public void innerInitialize(BA ba, String eventName, boolean keepOldObject) {
        this.ba = ba;
        if (!keepOldObject) {
            setObject(new EditText(ba.context));
        }
        super.innerInitialize(ba, eventName, true);
        if (ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_textchanged").toString())) {
            ((EditText) getObject()).addTextChangedListener(new C00211(ba, eventName));
        }
        if (ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_enterpressed").toString())) {
            ((EditText) getObject()).setOnEditorActionListener(new C00222(ba, eventName));
        }
        if (ba.subExists(new StringBuilder(String.valueOf(eventName)).append("_focuschanged").toString())) {
            ((EditText) getObject()).setOnFocusChangeListener(new C00233(ba, eventName));
        }
    }

    public void setForceDoneButton(boolean value) {
        if (value) {
            ((EditText) getObject()).setImeOptions(6);
        } else {
            ((EditText) getObject()).setImeOptions(INPUT_TYPE_NONE);
        }
    }

    public void setSingleLine(boolean singleLine) {
        ((EditText) getObject()).setSingleLine(singleLine);
    }

    public void setPasswordMode(boolean value) {
        if (value) {
            ((EditText) getObject()).setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            ((EditText) getObject()).setTransformationMethod(null);
        }
    }

    public int getSelectionStart() {
        return Selection.getSelectionStart(((EditText) getObject()).getText());
    }

    public void setSelectionStart(int value) {
        ((EditText) getObject()).setSelection(value);
    }

    public void SelectAll() {
        Selection.selectAll(((EditText) getObject()).getText());
    }

    public void setInputType(int value) {
        ((EditText) getObject()).setInputType(value);
    }

    public int getInputType() {
        return ((EditText) getObject()).getInputType();
    }

    public void setWrap(boolean value) {
        ((EditText) getObject()).setHorizontallyScrolling(!value);
    }

    public void setHint(String text) {
        ((EditText) getObject()).setHint(text);
    }

    public String getHint() {
        CharSequence c = ((EditText) getObject()).getHint();
        return c == null ? "" : String.valueOf(c);
    }

    public void setHintColor(int Color) {
        ((EditText) getObject()).setHintTextColor(Color);
    }

    public int getHintColor() {
        return ((EditText) getObject()).getCurrentHintTextColor();
    }

    @RaisesSynchronousEvents
    public void setText(Object Text) {
        super.setText(Text);
    }

    @Hide
    public static View build(Object defaultHintColor, HashMap<String, Object> props, boolean designer, Object tag) throws Exception {
        EditText v;
        if (defaultHintColor == null) {
            v = (EditText) ViewWrapper.buildNativeView((Context) tag, EditText.class, props, designer);
        } else {
            v = (EditText) defaultHintColor;
        }
        TextViewWrapper.build(v, props, designer);
        ColorStateList defaultHintColor2 = null;
        if (designer) {
            defaultHintColor2 = (ColorStateList) ViewWrapper.getDefault(v, "hintColor", v.getHintTextColors());
        }
        int hintColor = ((Integer) BA.gm(props, "hintColor", Integer.valueOf(ViewWrapper.defaultColor))).intValue();
        if (hintColor != ViewWrapper.defaultColor) {
            v.setHintTextColor(hintColor);
        } else if (designer) {
            v.setHintTextColor(defaultHintColor2);
        }
        String hint = (String) BA.gm(props, "hint", "");
        if (designer && hint.length() == 0) {
            hint = (String) props.get("name");
        }
        v.setHint(hint);
        hint = (String) props.get("inputType");
        if (hint != null) {
            v.setInputType(((Integer) EditTextWrapper.class.getField("INPUT_TYPE_" + hint).get(INPUT_TYPE_NONE)).intValue());
        }
        boolean singleLine = ((Boolean) props.get("singleLine")).booleanValue();
        v.setSingleLine(singleLine);
        if (designer && singleLine) {
            v.setInputType(true);
        }
        if (((Boolean) props.get("password")).booleanValue()) {
            v.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            v.setTransformationMethod(null);
        }
        v.setHorizontallyScrolling(!((Boolean) BA.gm(props, "wrap", Boolean.valueOf(true))).booleanValue());
        if (((Boolean) BA.gm(props, "forceDone", Boolean.valueOf(false))).booleanValue()) {
            v.setImeOptions(true);
        } else {
            v.setImeOptions(false);
        }
        return v;
    }
}
