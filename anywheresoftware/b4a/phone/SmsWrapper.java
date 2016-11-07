package anywheresoftware.b4a.phone;

import android.database.Cursor;
import android.net.Uri;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;
import java.util.HashMap;

@ShortName("SmsMessages")
public class SmsWrapper {
    public static final int TYPE_DRAFT = 3;
    public static final int TYPE_FAILED = 5;
    public static final int TYPE_INBOX = 1;
    public static final int TYPE_OUTBOX = 4;
    public static final int TYPE_QUEUED = 6;
    public static final int TYPE_SENT = 2;
    public static final int TYPE_UNKNOWN = 0;
    private static final String[] projection;

    @ShortName("Sms")
    public static class Sms {
        public String Address;
        public String Body;
        public long Date;
        public int Id;
        public int PersonId;
        public boolean Read;
        public int ThreadId;
        public int Type;

        @Hide
        public Sms(int id, int threadId, int personId, long date, boolean read, int type, String body, String address) {
            this.Id = id;
            this.ThreadId = threadId;
            this.PersonId = personId;
            this.Date = date;
            this.Read = read;
            this.Type = type;
            this.Body = body;
            this.Address = address;
        }

        @Hide
        public String toString() {
            return "Id=" + this.Id + ", ThreadId=" + this.ThreadId + ", PersonId=" + this.PersonId + ", Date=" + this.Date + ", Read=" + this.Read + ", Type=" + this.Type + ", Body=" + this.Body + ", Address=" + this.Address;
        }
    }

    static {
        projection = new String[]{"_id", "thread_id", "address", "read", "type", "body", "person", "date"};
    }

    public List GetByType(int Type) {
        String[] strArr = new String[TYPE_INBOX];
        strArr[0] = String.valueOf(Type);
        return get("type = ?", strArr);
    }

    public List GetByMessageId(int Id) {
        String[] strArr = new String[TYPE_INBOX];
        strArr[0] = String.valueOf(Id);
        return get("_id = ?", strArr);
    }

    public List GetByThreadId(int ThreadId) {
        String[] strArr = new String[TYPE_INBOX];
        strArr[0] = String.valueOf(ThreadId);
        return get("thread_id = ?", strArr);
    }

    public List GetByPersonId(int PersonId) {
        String[] strArr = new String[TYPE_INBOX];
        strArr[0] = String.valueOf(PersonId);
        return get("person = ?", strArr);
    }

    public List GetUnreadMessages() {
        return get("read = 0", null);
    }

    public List GetAll() {
        return get(null, null);
    }

    public List GetAllSince(long Date) {
        String[] strArr = new String[TYPE_INBOX];
        strArr[0] = String.valueOf(Date);
        return get("date >= ?", strArr);
    }

    public List GetBetweenDates(long StartDate, long EndDate) {
        String[] strArr = new String[TYPE_SENT];
        strArr[0] = String.valueOf(StartDate);
        strArr[TYPE_INBOX] = String.valueOf(EndDate);
        return get("date >= ? AND date < ?", strArr);
    }

    private List get(String selection, String[] args) {
        Cursor crsr = BA.applicationContext.getContentResolver().query(Uri.parse("content://sms"), projection, selection, args, "date DESC");
        HashMap<String, Integer> m = new HashMap();
        for (int col = 0; col < crsr.getColumnCount(); col += TYPE_INBOX) {
            m.put(crsr.getColumnName(col), Integer.valueOf(col));
        }
        List l = new List();
        l.Initialize();
        while (crsr.moveToNext()) {
            String personId = crsr.getString(((Integer) m.get("person")).intValue());
            l.Add(new Sms(crsr.getInt(((Integer) m.get("_id")).intValue()), crsr.getInt(((Integer) m.get("thread_id")).intValue()), personId == null ? -1 : Integer.parseInt(personId), crsr.getLong(((Integer) m.get("date")).intValue()), crsr.getInt(((Integer) m.get("read")).intValue()) > 0, crsr.getInt(((Integer) m.get("type")).intValue()), crsr.getString(((Integer) m.get("body")).intValue()), crsr.getString(((Integer) m.get("address")).intValue())));
        }
        crsr.close();
        return l;
    }
}
