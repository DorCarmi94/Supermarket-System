package Presentation;

public class CurrStore {

    private static CurrStore currStoreInstance = null;
    private int store_id;

    public static CurrStore getInstance() {
        if(currStoreInstance == null)
            currStoreInstance = new CurrStore();
        return currStoreInstance;
    }

    private CurrStore(){
        store_id = -1;
    }
    public int getStore_id() {
        return store_id;
    }
    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

}
