package pjcbm9.quicket;

// maintenance item
public class MItem {
    // maintenance types
    public enum mType {
        USERS,
        TYPES,
        LOCATIONS,
    }
    // private class variables
    private int index;
    private mType type;
    private String itemName;
    // constructor
    public MItem(int INDEX, mType TYPE, String ITEMNAME){
        this.index = INDEX;
        this.type = TYPE;
        this.itemName = ITEMNAME;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public mType getType() {
        return type;
    }

    public void setType(mType type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
