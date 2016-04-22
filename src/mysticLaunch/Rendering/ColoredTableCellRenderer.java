/*
 * ColoredTableCellRenderer.java
 *
 * Created on September 25, 2007, 10:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mysticLaunch.Rendering;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**

 * @author IM01DXP
 */
public class ColoredTableCellRenderer extends DefaultTableCellRenderer
{
    
    public Component getTableCellRendererComponent
        (JTable table, Object value, boolean selected, boolean focused, int row, int column)
    {
        Color cbWhite = new Color(255,255,255);
        //Color cfBlack = new Color(0,0,0);
        Color cfGreen = new Color(128,192,96);
        Color cfOrange = new Color(255,170,62);
        Color cfBlue = new Color(41,55,58);
        setEnabled(table == null || table.isEnabled()); // see question above
        String rating = (String) table.getValueAt(row, 1);
        //Color myColor = new Color(red, green, blue);
        //setForeground(myColor);
        if (column == 0)
        {  setIcon((Icon)value);
        }
        
        setForeground(cfOrange); 
        setBackground(cfBlue);
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
    }
}