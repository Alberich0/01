package anywheresoftware.b4a.phone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.ContactMethods;
import android.provider.Contacts.People;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.collections.List;
import anywheresoftware.b4a.objects.collections.Map;
import anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper;
import anywheresoftware.b4a.objects.streams.File.InputStreamWrapper;
import java.io.InputStream;
import java.util.HashMap;

@ShortName("Contacts")
public class ContactsWrapper {
    private static final String[] people_projection;

    @ShortName("Contact")
    public static class Contact {
        public static final int EMAIL_CUSTOM = 0;
        public static final int EMAIL_HOME = 1;
        public static final int EMAIL_MOBILE = 4;
        public static final int EMAIL_OTHER = 3;
        public static final int EMAIL_WORK = 2;
        public static final int PHONE_CUSTOM = 0;
        public static final int PHONE_FAX_HOME = 5;
        public static final int PHONE_FAX_WORK = 4;
        public static final int PHONE_HOME = 1;
        public static final int PHONE_MOBILE = 2;
        public static final int PHONE_OTHER = 7;
        public static final int PHONE_PAGER = 6;
        public static final int PHONE_WORK = 3;
        public String DisplayName;
        public int Id;
        public long LastTimeContacted;
        public String Name;
        public String Notes;
        public String PhoneNumber;
        public boolean Starred;
        public int TimesContacted;

        public Contact() {
            this.PhoneNumber = "";
            this.Id = -1;
        }

        Contact(String displayName, String phoneNumber, boolean starred, int id, String notes, int timesContacted, long lastTimeContacted, String name) {
            String str = "";
            String str2 = "";
            this.PhoneNumber = str;
            this.Id = -1;
            if (displayName == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = displayName;
            }
            this.DisplayName = str2;
            if (phoneNumber == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = phoneNumber;
            }
            this.PhoneNumber = str2;
            this.Starred = starred;
            this.Id = id;
            if (notes == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = notes;
            }
            this.Notes = str2;
            this.TimesContacted = timesContacted;
            this.LastTimeContacted = lastTimeContacted;
            if (name == null) {
                str2 = "";
                str2 = str;
            } else {
                str2 = name;
            }
            this.Name = str2;
        }

        public BitmapWrapper GetPhoto() {
            if (this.Id == -1) {
                throw new RuntimeException("Contact object should be set by calling one of the Contacts methods.");
            }
            Uri u = Uri.withAppendedPath(ContentUris.withAppendedId(People.CONTENT_URI, (long) this.Id), "photo");
            ContentResolver contentResolver = BA.applicationContext.getContentResolver();
            String[] strArr = new String[PHONE_HOME];
            strArr[PHONE_CUSTOM] = "data";
            Cursor crsr = contentResolver.query(u, strArr, null, null, null);
            BitmapWrapper bw = null;
            if (crsr.moveToNext()) {
                byte[] b = crsr.getBlob(PHONE_CUSTOM);
                if (b != null) {
                    InputStreamWrapper isw = new InputStreamWrapper();
                    isw.InitializeFromBytesArray(b, PHONE_CUSTOM, b.length);
                    bw = new BitmapWrapper();
                    bw.Initialize2((InputStream) isw.getObject());
                }
            }
            crsr.close();
            return bw;
        }

        public Map GetEmails() {
            if (this.Id == -1) {
                throw new RuntimeException("Contact object should be set by calling one of the Contacts methods.");
            }
            Uri u = Uri.withAppendedPath(ContentUris.withAppendedId(People.CONTENT_URI, (long) this.Id), "contact_methods");
            ContentResolver contentResolver = BA.applicationContext.getContentResolver();
            String[] strArr = new String[PHONE_WORK];
            strArr[PHONE_CUSTOM] = "data";
            strArr[PHONE_HOME] = "type";
            strArr[PHONE_MOBILE] = "kind";
            Cursor crsr = contentResolver.query(u, strArr, "kind = 1", null, null);
            Map m = new Map();
            m.Initialize();
            while (crsr.moveToNext()) {
                m.Put(crsr.getString(PHONE_CUSTOM), Integer.valueOf(crsr.getInt(PHONE_HOME)));
            }
            crsr.close();
            return m;
        }

        public Map GetPhones() {
            if (this.Id == -1) {
                throw new RuntimeException("Contact object should be set by calling one of the Contacts methods.");
            }
            Uri u = Uri.withAppendedPath(ContentUris.withAppendedId(People.CONTENT_URI, (long) this.Id), "phones");
            ContentResolver contentResolver = BA.applicationContext.getContentResolver();
            String[] strArr = new String[PHONE_MOBILE];
            strArr[PHONE_CUSTOM] = "number";
            strArr[PHONE_HOME] = "type";
            Cursor crsr = contentResolver.query(u, strArr, null, null, null);
            Map m = new Map();
            m.Initialize();
            while (crsr.moveToNext()) {
                m.Put(crsr.getString(PHONE_CUSTOM), Integer.valueOf(crsr.getInt(PHONE_HOME)));
            }
            crsr.close();
            return m;
        }

        @Hide
        public String toString() {
            return "DisplayName=" + this.DisplayName + ", PhoneNumber=" + this.PhoneNumber + ", Starred=" + this.Starred + ", Id=" + this.Id + ", Notes=" + this.Notes + ", TimesContacted=" + this.TimesContacted + ", LastTimeContacted=" + this.LastTimeContacted + ", Name=" + this.Name;
        }
    }

    static {
        people_projection = new String[]{"times_contacted", "number", "last_time_contacted", "display_name", "name", "notes", "starred", "_id"};
    }

    public List GetAll() {
        return getAllContacts(null, null);
    }

    public List FindByName(String Name, boolean Exact) {
        String str = "%";
        if (Exact) {
            return getAllContacts("name = ?", new String[]{Name});
        }
        String[] strArr = new String[1];
        String str2 = "%";
        str2 = "%";
        strArr[0] = Name + str;
        return getAllContacts("name LIKE ?", strArr);
    }

    public List FindByMail(String Email, boolean Exact) {
        String sel;
        String args;
        ContentResolver cr = BA.applicationContext.getContentResolver();
        if (Exact) {
            sel = " = ?";
            args = Email;
        } else {
            sel = " LIKE ?";
            args = "%" + Email + "%";
        }
        Cursor crsr = cr.query(ContactMethods.CONTENT_EMAIL_URI, new String[]{"person", "data"}, "data" + sel, new String[]{args}, null);
        StringBuilder sb = new StringBuilder();
        while (crsr.moveToNext()) {
            for (int i = 0; i < crsr.getColumnCount(); i++) {
                sb.append(crsr.getString(0)).append(",");
            }
        }
        int count = crsr.getCount();
        crsr.close();
        if (count == 0) {
            List l = new List();
            l.Initialize();
            return l;
        }
        sb.setLength(sb.length() - 1);
        return getAllContacts("_id IN (" + sb.toString() + ")", null);
    }

    public Contact GetById(int Id) {
        List l = getAllContacts("_id = ?", new String[]{String.valueOf(Id)});
        if (l.getSize() == 0) {
            return null;
        }
        return (Contact) l.Get(0);
    }

    private List getAllContacts(String selection, String[] args) {
        Cursor crsr = BA.applicationContext.getContentResolver().query(People.CONTENT_URI, people_projection, selection, args, null);
        List l = new List();
        l.Initialize();
        HashMap<String, Integer> m = new HashMap();
        for (int col = 0; col < crsr.getColumnCount(); col++) {
            m.put(crsr.getColumnName(col), Integer.valueOf(col));
        }
        while (crsr.moveToNext()) {
            l.Add(new Contact(crsr.getString(((Integer) m.get("display_name")).intValue()), crsr.getString(((Integer) m.get("number")).intValue()), crsr.getInt(((Integer) m.get("starred")).intValue()) > 0, crsr.getInt(((Integer) m.get("_id")).intValue()), crsr.getString(((Integer) m.get("notes")).intValue()), crsr.getInt(((Integer) m.get("times_contacted")).intValue()), crsr.getLong(((Integer) m.get("last_time_contacted")).intValue()), crsr.getString(((Integer) m.get("name")).intValue())));
        }
        crsr.close();
        return l;
    }
}
