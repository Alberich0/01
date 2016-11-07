package anywheresoftware.b4a.objects.collections;

import anywheresoftware.b4a.AbsObjectWrapper;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.IterableList;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.WarningEngine;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

@ShortName("List")
public class List extends AbsObjectWrapper<java.util.List<Object>> implements IterableList {

    /* renamed from: anywheresoftware.b4a.objects.collections.List.1 */
    class C00351 implements Comparator<Comparable> {
        C00351() {
        }

        public int compare(Comparable o1, Comparable o2) {
            return o2.compareTo(o1);
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.collections.List.2 */
    class C00362 implements Comparator<Object> {
        private final /* synthetic */ boolean val$Ascending;
        private final /* synthetic */ boolean val$caseInsensitive;
        private final /* synthetic */ Field val$f;

        C00362(boolean z, Field field, boolean z2) {
            this.val$caseInsensitive = z;
            this.val$f = field;
            this.val$Ascending = z2;
        }

        public int compare(Object o1, Object o2) {
            try {
                int cmp;
                if (this.val$caseInsensitive) {
                    cmp = String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
                } else {
                    cmp = ((Comparable) this.val$f.get(o1)).compareTo(this.val$f.get(o2));
                }
                return (this.val$Ascending ? 1 : -1) * cmp;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.collections.List.3 */
    class C00373 implements Comparator<Comparable> {
        C00373() {
        }

        public int compare(Comparable o1, Comparable o2) {
            return o1.toString().compareToIgnoreCase(o2.toString());
        }
    }

    /* renamed from: anywheresoftware.b4a.objects.collections.List.4 */
    class C00384 implements Comparator<Comparable> {
        C00384() {
        }

        public int compare(Comparable o1, Comparable o2) {
            return o2.toString().compareToIgnoreCase(o1.toString());
        }
    }

    public void Initialize() {
        setObject(new ArrayList());
    }

    public void Initialize2(List Array) {
        setObject((java.util.List) Array.getObject());
    }

    public void Clear() {
        ((java.util.List) getObject()).clear();
    }

    public void Add(Object item) {
        if (BA.debugMode && ((java.util.List) getObject()).size() > 0) {
            Object prev = Get(getSize() - 1);
            if (!(prev == null || prev != item || (prev instanceof String) || (prev instanceof Number) || (prev instanceof Boolean))) {
                WarningEngine.warn(WarningEngine.SAME_OBJECT_ADDED_TO_LIST);
            }
        }
        ((java.util.List) getObject()).add(item);
    }

    public void AddAll(List List) {
        ((java.util.List) getObject()).addAll((Collection) List.getObject());
    }

    public void AddAllAt(int Index, List List) {
        ((java.util.List) getObject()).addAll(Index, (Collection) List.getObject());
    }

    public void RemoveAt(int Index) {
        ((java.util.List) getObject()).remove(Index);
    }

    public void InsertAt(int Index, Object Item) {
        ((java.util.List) getObject()).add(Index, Item);
    }

    public Object Get(int Index) {
        return ((java.util.List) getObject()).get(Index);
    }

    public void Set(int Index, Object Item) {
        ((java.util.List) getObject()).set(Index, Item);
    }

    public int getSize() {
        return ((java.util.List) getObject()).size();
    }

    public int IndexOf(Object Item) {
        return ((java.util.List) getObject()).indexOf(Item);
    }

    public void Sort(boolean Ascending) {
        if (Ascending) {
            Collections.sort((java.util.List) getObject());
        } else {
            Collections.sort((java.util.List) getObject(), new C00351());
        }
    }

    public void SortType(String FieldName, boolean Ascending) throws SecurityException, NoSuchFieldException {
        sortList(FieldName, Ascending, false);
    }

    public void SortTypeCaseInsensitive(String FieldName, boolean Ascending) throws SecurityException, NoSuchFieldException {
        sortList(FieldName, Ascending, true);
    }

    private void sortList(String FieldName, boolean Ascending, boolean caseInsensitive) throws SecurityException, NoSuchFieldException {
        if (getSize() != 0) {
            Field f = Get(0).getClass().getDeclaredField(FieldName);
            f.setAccessible(true);
            Collections.sort((java.util.List) getObject(), new C00362(caseInsensitive, f, Ascending));
        }
    }

    public void SortCaseInsensitive(boolean Ascending) {
        if (Ascending) {
            Collections.sort((java.util.List) getObject(), new C00373());
        } else {
            Collections.sort((java.util.List) getObject(), new C00384());
        }
    }
}
