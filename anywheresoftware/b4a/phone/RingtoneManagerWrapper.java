package anywheresoftware.b4a.phone;

import android.content.ContentValues;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.IOnActivityResult;
import anywheresoftware.b4a.objects.streams.File;

@ShortName("RingtoneManager")
public class RingtoneManagerWrapper {
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_NOTIFICATION = 2;
    public static final int TYPE_RINGTONE = 1;
    private IOnActivityResult ion;

    /* renamed from: anywheresoftware.b4a.phone.RingtoneManagerWrapper.1 */
    class C00601 implements IOnActivityResult {
        private final /* synthetic */ BA val$ba;
        private final /* synthetic */ String val$eventName;

        C00601(BA ba, String str) {
            this.val$ba = ba;
            this.val$eventName = str;
        }

        public void ResultArrived(int resultCode, Intent intent) {
            String str = "_pickerresult";
            String uri = null;
            if (resultCode == -1 && intent != null) {
                Uri u = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
                uri = u == null ? "" : u.toString();
            }
            RingtoneManagerWrapper.this.ion = null;
            if (uri != null) {
                BA ba = this.val$ba;
                RingtoneManagerWrapper ringtoneManagerWrapper = RingtoneManagerWrapper.this;
                String str2 = "_pickerresult";
                String str3 = this.val$eventName + str;
                Object[] objArr = new Object[RingtoneManagerWrapper.TYPE_NOTIFICATION];
                objArr[0] = Boolean.valueOf(true);
                objArr[RingtoneManagerWrapper.TYPE_RINGTONE] = uri;
                ba.raiseEvent(ringtoneManagerWrapper, str3, objArr);
                return;
            }
            ba = this.val$ba;
            ringtoneManagerWrapper = RingtoneManagerWrapper.this;
            str2 = "_pickerresult";
            str3 = this.val$eventName + str;
            objArr = new Object[RingtoneManagerWrapper.TYPE_NOTIFICATION];
            objArr[0] = Boolean.valueOf(false);
            objArr[RingtoneManagerWrapper.TYPE_RINGTONE] = "";
            ba.raiseEvent(ringtoneManagerWrapper, str3, objArr);
        }
    }

    public String GetContentDir() {
        return File.ContentDir;
    }

    public String AddToMediaStore(String Dir, String FileName, String Title, boolean IsAlarm, boolean IsNotification, boolean IsRingtone, boolean IsMusic) {
        java.io.File k = new java.io.File(Dir, FileName);
        ContentValues values = new ContentValues();
        values.put("_data", k.getAbsolutePath());
        values.put("title", Title);
        values.put("mime_type", "audio/*");
        values.put("is_ringtone", Boolean.valueOf(IsRingtone));
        values.put("is_notification", Boolean.valueOf(IsNotification));
        values.put("is_alarm", Boolean.valueOf(IsAlarm));
        values.put("is_music", Boolean.valueOf(IsMusic));
        return BA.applicationContext.getContentResolver().insert(Media.getContentUriForPath(k.getAbsolutePath()), values).toString();
    }

    public void SetDefault(int Type, String Uri) {
        RingtoneManager.setActualDefaultRingtoneUri(BA.applicationContext, Type, Uri.parse(Uri));
    }

    public String GetDefault(int Type) {
        Uri u = RingtoneManager.getDefaultUri(Type);
        if (u == null) {
            return "";
        }
        return u.toString();
    }

    public void DeleteRingtone(String Uri) {
        BA.applicationContext.getContentResolver().delete(Uri.parse(Uri), null, null);
    }

    public void ShowRingtonePicker(BA ba, String EventName, int Type, boolean IncludeSilence, String ChosenRingtone) {
        String eventName = EventName.toLowerCase(BA.cul);
        Intent i = new Intent("android.intent.action.RINGTONE_PICKER");
        i.putExtra("android.intent.extra.ringtone.TYPE", Type);
        i.putExtra("android.intent.extra.ringtone.SHOW_SILENT", IncludeSilence);
        if (ChosenRingtone.length() > 0) {
            i.putExtra("android.intent.extra.ringtone.EXISTING_URI", Uri.parse(ChosenRingtone));
        }
        this.ion = new C00601(ba, eventName);
        ba.startActivityForResult(this.ion, i);
    }
}
