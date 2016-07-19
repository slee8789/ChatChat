package leesc.chatchat.db;

import java.util.ArrayList;

public class DataObserver implements DataChange{
    private static volatile DataObserver mObserver;
    private ArrayList<Object> mDataArray = new ArrayList<Object>();
    static class DataObserverHolder {
        static final DataObserver holder = new DataObserver();
    }

    public static DataObserver getInstance() {
        if (mObserver == null) {
            synchronized (DataObserverHolder.class) {
                if (mObserver == null) {
                    mObserver = DataObserverHolder.holder;
                }
            }
        }
        return mObserver;
    }
    
    public void addObserver(Object data) {
        mDataArray.add(data);
    }
    
    public void removeObserver(Object data) {
        mDataArray.remove(data);
    }

    @Override
    public void onChange() {
        for (int i = 0 ; i < mDataArray.size(); i++) {
            DataChange dc = (DataChange) mDataArray.get(i);
            dc.onChange();
        }
    }
}
