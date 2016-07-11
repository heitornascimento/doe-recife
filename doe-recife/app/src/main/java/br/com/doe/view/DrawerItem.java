package br.com.doe.view;

/**
 * Created by heitornascimento on 4/20/16.
 */
public class DrawerItem {

    private String count;
    private int icon;
    private String type;

    public DrawerItem() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        DrawerItem drawerItem = (DrawerItem) o;
        return this.type.equals(((DrawerItem) o).getType());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
