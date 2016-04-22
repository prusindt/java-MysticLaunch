/*
 * UnEditableTableModel.java
 *
 * Created on September 26, 2007, 9:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mysticLaunch.Rendering;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IM01DXP
 */
public class UnEditableTableModel extends DefaultTableModel { 
    
        private Class[] types = new Class [] {
           java.lang.Object.class, java.lang.String.class, 
        };

        public Class getColumnClass(int columnIndex) {
           return types [columnIndex];
        }
        
        //private String[] columnNames = { "Icon", "Name" };
        private String[] columnNames = { };

        public int getColumnCount() {
            return 2;
            //return columnNames.length;
        }

        public String getColumnName(int col) {
            return null;
            //return columnNames[col];
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
}
