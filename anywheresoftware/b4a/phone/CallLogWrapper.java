package anywheresoftware.b4a.phone;

import android.database.Cursor;
import android.provider.CallLog.Calls;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;
import java.util.HashMap;

@ShortName("CallLog")
public class CallLogWrapper {
    private static final String[] calls_projection;

    @ShortName("CallItem")
    public static class CallItem {
        public static final int TYPE_INCOMING = 1;
        public static final int TYPE_MISSED = 3;
        public static final int TYPE_OUTGOING = 2;
        public String CachedName;
        public int CallType;
        public long Date;
        public long Duration;
        public int Id;
        public String Number;

        public CallItem() {
            this.Id = -1;
            this.CachedName = "";
        }

        CallItem(String number, int id, long duration, int type, long date, String name) {
            String str = "";
            this.Id = -1;
            String str2 = "";
            this.CachedName = str;
            if (number == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = number;
            }
            this.Number = str2;
            this.Id = id;
            this.CallType = type;
            this.Duration = duration;
            this.Date = date;
            if (name == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = name;
            }
            this.CachedName = str2;
        }

        @Hide
        public String toString() {
            return "Id=" + this.Id + ", Number=" + this.Number + ",CachedName=" + this.CachedName + ", Type=" + this.CallType + ", Date=" + this.Date + ", Duration=" + this.Duration;
        }
    }

    static {
        calls_projection = new String[]{"date", "type", "duration", "number", "_id", "name"};
    }

    public List GetAll(int Limit) {
        return getAllCalls(null, null, Limit);
    }

    public CallItem GetById(int Id) {
        List l = getAllCalls("_id = ?", new String[]{String.valueOf(Id)}, 0);
        if (l.getSize() == 0) {
            return null;
        }
        return (CallItem) l.Get(0);
    }

    public List GetSince(long Date, int Limit) {
        return getAllCalls("date >= ?", new String[]{Long.toString(Date)}, Limit);
    }

    private List getAllCalls(String selection, String[] args, int limit) {
        Cursor crsr = BA.applicationContext.getContentResolver().query(Calls.CONTENT_URI, calls_projection, selection, args, "date DESC");
        List l = new List();
        l.Initialize();
        HashMap<String, Integer> m = new HashMap();
        for (int col = 0; col < crsr.getColumnCount(); col++) {
            m.put(crsr.getColumnName(col), Integer.valueOf(col));
        }
        int i = 0;
        while (crsr.moveToNext()) {
            l.Add(new CallItem(crsr.getString(((Integer) m.get("number")).intValue()), crsr.getInt(((Integer) m.get("_id")).intValue()), crsr.getLong(((Integer) m.get("duration")).intValue()), crsr.getInt(((Integer) m.get("type")).intValue()), crsr.getLong(((Integer) m.get("date")).intValue()), crsr.getString(((Integer) m.get("name")).intValue())));
            if (limit > 0) {
                i++;
                if (i >= limit) {
                    break;
                }
            }
        }
        crsr.close();
        return l;
    }
}
