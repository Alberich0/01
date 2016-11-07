package anywheresoftware.b4a;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;

public class BALayout extends ViewGroup {
    private static float deviceScale;
    private static float scale;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public int left;
        public int top;

        public LayoutParams(int left, int top, int width, int height) {
            super(width, height);
            this.left = left;
            this.top = top;
        }

        public LayoutParams() {
            super(0, 0);
        }

        public HashMap<String, Object> toDesignerMap() {
            HashMap<String, Object> props = new HashMap();
            props.put("left", Integer.valueOf((int) (((float) this.left) / BALayout.scale)));
            props.put("top", Integer.valueOf((int) (((float) this.top) / BALayout.scale)));
            props.put("width", Integer.valueOf((int) (((float) this.width) / BALayout.scale)));
            props.put("height", Integer.valueOf((int) (((float) this.height) / BALayout.scale)));
            return props;
        }

        public void setFromUserPlane(int left, int top, int width, int height) {
            int access$0;
            this.left = (int) (((float) left) * BALayout.scale);
            this.top = (int) (((float) top) * BALayout.scale);
            if (width > 0) {
                access$0 = (int) (((float) width) * BALayout.scale);
            } else {
                access$0 = width;
            }
            this.width = access$0;
            if (height > 0) {
                access$0 = (int) (((float) height) * BALayout.scale);
            } else {
                access$0 = height;
            }
            this.height = access$0;
        }
    }

    static {
        scale = 0.0f;
        deviceScale = 0.0f;
    }

    public BALayout(Context context) {
        super(context);
    }

    public static void setDeviceScale(float scale) {
        deviceScale = scale;
    }

    public static void setUserScale(float userScale) {
        if (Float.compare(deviceScale, userScale) == 0) {
            scale = 1.0f;
        } else {
            scale = deviceScale / userScale;
        }
    }

    public static float getDeviceScale() {
        return deviceScale;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                if (child.getLayoutParams() instanceof LayoutParams) {
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    child.layout(lp.left, lp.top, lp.left + child.getMeasuredWidth(), lp.top + child.getMeasuredHeight());
                } else {
                    child.layout(0, 0, getLayoutParams().width, getLayoutParams().height);
                }
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSize(getLayoutParams().width, widthMeasureSpec), resolveSize(getLayoutParams().height, heightMeasureSpec));
    }
}
