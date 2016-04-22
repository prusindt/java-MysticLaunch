/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysticLaunch.util;

import com.mysticcore.util.ImageHelper;
import com.mysticcore.util.IniFile;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import mysticLaunch.bean.AppInfoBean;

/**
 *
 * @author IM01DXP
 */
public class DataHelper {
    private IniFile Ini = null;
    private String FilePath = null;
    private String[] _groups = null;
    //      Map<GroupId, List<Objs in group>>
    private SortedMap<String, List<AppInfoBean>> _data = null;
    private static DataHelper singleton = null;
    
    public static DataHelper singleton() {
        if (singleton == null)
        {  singleton = new DataHelper();
        }
        return singleton;
    }

    private DataHelper() {
       _data = new TreeMap<String, List<AppInfoBean>>();
    }
    
    public void loadData(String filePath)
    {  FilePath = filePath;
        try {
            Ini = new IniFile();
            Ini.load(FilePath);
            //Get a list of all of the groups..
            String groups = Ini.getString("Tabs", "List", null);
            System.out.println("Groups:" + groups);
            _groups = groups.split(",");
            //For each group, load the beans..
            for(String group:_groups)
            {  //Retrieve a List of App=Path Strings
               Map<String, String> iniGroup = Ini.getSection(group);
               //For each Key lookup additional Params..
               List beanList = new ArrayList();
               Set<String> keys = iniGroup.keySet();
               for(String key:keys)
               {  AppInfoBean bean = new AppInfoBean();
                  System.out.println("Loading :" + key);
                  bean.setGroup(group);;
                  bean.setName(key);
                  bean.setPath(iniGroup.get(key));
                  bean.setStartin(Ini.getString(key, "startIn", null));
                  bean.setParms(Ini.getString(key, "parms", null));
                  bean.setIconPath(Ini.getString(key, "iconPath", null));
                  System.out.println("Loading icon..");
                  ImageIcon icon = null;
                  if (bean.getIconPath() == null)
                  {  icon = ImageHelper.getIconEmbeddedFromFile(bean.getPath());
                  }
                  else
                  {  icon = ImageHelper.getIconFromFile(bean.getIconPath());
                  }
                  if (icon != null)
                  {  BufferedImage img = (BufferedImage)((Image) icon.getImage());
                     img = ImageHelper.getScaledInstance(img, 20, 20, RenderingHints.VALUE_INTERPOLATION_BICUBIC , true);
                     icon = new ImageIcon(img);
                  }
                  bean.setIcon(icon);

                  System.out.println("Finished Loading icon..");
                  //Load Bean into results
                  beanList.add(bean);
               }
               _data.put(group, beanList);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DataHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Finished Loading DataHelper.");
    }
    
    
    public List<String> getAliasNames() {
        List<String> result = new ArrayList<>();
        if (_data != null)
        {  for (String key:_data.keySet()) 
           {  result.add(key);
           }
        }
        return result;
    }
    
    public List<String> getEntryNames(String aliasId) {
        List<String> result = new ArrayList<>();
        List<AppInfoBean> lst = getData(aliasId);
        if (lst != null)
        {  for (AppInfoBean bean:lst) 
           {  result.add(bean.getName());
           }
        }
        return result;
    }
    
    public List<AppInfoBean> getData(String groupId) {
        List<AppInfoBean> result = _data.get(groupId);
        if (result != null) Collections.sort(result);
        return result;
    }
    
    public AppInfoBean getBeanInfo(String groupId, String beanName) {
        List<AppInfoBean> lst = getData(groupId);
        if (lst != null)
        for(AppInfoBean bean:lst)
        {  if (bean.getName().equals(beanName))
              return bean;
        }
        return null;
    }
    
    public void removeAlias(String aliasId) {
        
    }
    
    public void addAlias(String aliasId) {
        
    }
    
    public void removeEntry(String aliasId, AppInfoBean entryBean) {
        
    }
    
    public void addEntry(String aliasId, AppInfoBean entryBean) {
        
    }
    
}
