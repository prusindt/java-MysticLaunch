/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysticLaunch.bean;

import javax.swing.Icon;

/**
 *
 * @author IM01DXP
 */
public class AppInfoBean implements Comparable {
    
    private String group = null;
    private String name = null;
    private String path = null;
    private String startin = null;
    private Icon ico = null;
    private String iconPath = null;
    private String parms = null;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStartin() {
        return startin;
    }

    public void setStartin(String startin) {
        this.startin = startin;
    }

    public Icon getIcon() {
        return ico;
    }

    public void setIcon(Icon icon) {
        this.ico = icon;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getParms() {
        return parms;
    }

    public void setParms(String parms) {
        this.parms = parms;
    }
        
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AIB [" + name + "]\n");
        sb.append("Path: " + path + "\n");
        sb.append("Parms: " + parms + "\n");
        sb.append("StartIn: " + startin + "\n");
        sb.append("IconPath: " + iconPath + "\n");
        return sb.toString();
    }

    public int compareTo(Object obj)
    {   AppInfoBean bean = (AppInfoBean) obj;
        return this.name.compareTo(bean.getName());
    }
    
    
}
