/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysticLaunch.ui;

import java.awt.Color;
import mysticLaunch.Rendering.ColoredTableCellRenderer;
import mysticLaunch.Rendering.UnEditableTableModel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import mysticLaunch.bean.AppInfoBean;
import mysticLaunch.util.DataHelper;
import mysticLaunch.util.RunProcess;
import org.jnativehook.GlobalScreen;
import org.jnativehook.SwingDispatchService;

/**
 *
 * @author IM01DXP
 */
public class display extends JFrame {

    private ListSelectionModel listSelectionModel = null;
    private long lastSelectionEvent = 0;
    private DataHelper _dh = null;
    private List<AppInfoBean> _data = null;
    private DlgConfig dlg = null;
    /* Colors
     * Blue 41.55.58
     * Orange 255.170.62
     * Green 128.192.96
     * White.. 
     */
    
class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
 
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();
            
            System.out.println("Event for indexes " + firstIndex + " - " + lastIndex
                          + "; isAdjusting is " + isAdjusting + "; selected indexes:");
            if (lsm.isSelectionEmpty()) {
                System.out.println(" <none>");
            } else {
                // Find out which indexes are selected.
                int row = e.getFirstIndex();
              String name = (String) tblResults.getValueAt(row, 1);
              System.out.println("SLS: " + name + ".");
                   
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        System.out.println(" " + i);
                    }
                }
            }
        }
    }    
    
    
    /**
     * Creates new form display
     */
    public display() {
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        this.setAlwaysOnTop(true);
        this.setType(javax.swing.JFrame.Type.UTILITY);
        this.setUndecorated(true);
        initComponents();
        int width = txtInput.getWidth() + btnConfig.getWidth() + btnExit.getWidth() + 25;
        this.setPreferredSize(new Dimension(width, pnlMain.getHeight()));
        Color cfBlue = new Color(41,55,58);
        Color cfGreen = new Color(128,192,96);
        pnlMain.setBackground(cfBlue);
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createLineBorder(pnlMain.getBackground()));
        tblResults.setBackground(pnlMain.getBackground());
        
        initTable();
        tblResults.setShowHorizontalLines(true);
        tblResults.setShowVerticalLines(false);
        tblResults.setGridColor(cfGreen);
        centerFrame(0,-125);
        //this.addKeyListener(null);
        resetFocus();
        _dh = DataHelper.singleton();
        _dh.loadData("ml.ini");
    }
    
    
    public void resetFocus()
    {  txtInput.requestFocus();        
    }
    
    public @Override void setVisible(boolean vis)
    {
       if (vis) { txtInput.requestFocus(); }
       super.setVisible(vis);
    }
    
    
    public void centerFrame(int offsetX, int offsetY) {
       java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
       int x = (screenSize.width - (int)this.getPreferredSize().getWidth())/2 + offsetX;
       int y = (screenSize.height- (int)this.getPreferredSize().getHeight())/2 + offsetY;
       System.out.println("Centering on [" + x + "," + y + "].");
       setLocation( x,y );
    }    
    
    public String getInput() {
        return txtInput.getText();
    }    
    
    private void initTable() 
    {
       UnEditableTableModel TM = new UnEditableTableModel();
       tblResults.setModel(TM);
       tblResults.setTableHeader(null);
       jScrollPane1.getColumnHeader().setVisible(false);
       tblResults.setRowHeight(25);
        // Setup the Icon column
        int vColIndex = 0;
        TableColumn col = tblResults.getColumnModel().getColumn(vColIndex);
        int width = 25;
        col.setPreferredWidth(width);
        col.setMaxWidth(width);
        col.setMinWidth(width);
 
        // Set the Name column to fill the rest of the table pixels
        vColIndex = 1;
        col = tblResults.getColumnModel().getColumn(vColIndex);
        width = 200;
        col.setPreferredWidth(width);
        col.setMaxWidth(width);
        col.setMinWidth(width);
 
        tblResults.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        tblResults.setDefaultRenderer(java.lang.String.class, new ColoredTableCellRenderer());
        tblResults.setDefaultRenderer(java.lang.Object.class, new ColoredTableCellRenderer());
        
        //Setup the selection Listener
        //listSelectionModel = tblResults.getSelectionModel();
        //listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        //tblResults.setSelectionModel(listSelectionModel);
        
        //Setup Mouse Listener
        MouseListener mouseListener = new MouseAdapter() 
        {  public void mouseClicked(MouseEvent mouseEvent) 
           {  Point origin = mouseEvent.getPoint();
              int row = tblResults.rowAtPoint(origin);
              int column = tblResults.columnAtPoint(origin);
              
              if (mouseEvent.getClickCount() == 2) 
              { if (mouseEvent.getWhen() != lastSelectionEvent)
                {  lastSelectionEvent = mouseEvent.getWhen();
                   processTableSelection(row, column);
                }
              }
           }
        };
        tblResults.addMouseListener(mouseListener);
    
    }

    private void processTableSelection(int x, int y)
    {  String name = (String) tblResults.getValueAt(x, 1);
       AppInfoBean bean = _dh.getBeanInfo(txtInput.getText(), name);
       System.out.println("Selected " + bean.toString() + ".");
       List<String> args = new ArrayList<String>();
       args.add(bean.getPath());
       if (bean.getParms() != null)
         args.add(bean.getParms());
       RunProcess rp = new RunProcess();
       this.setVisible(false);
       rp.execute(args, bean.getStartin());
    }
    
    private void clearTable() {
       DefaultTableModel model = (DefaultTableModel) tblResults.getModel();
       model.setRowCount(0);
    }
    
    
    public void fillTable(List<AppInfoBean> data) {
       clearTable();
       int mainHeight = pnlMain.getHeight();
       int rows = (data == null) ? 0 : data.size();
       int tableHeight = ((rows + 0) * tblResults.getRowHeight());
       int width = txtInput.getWidth() + btnConfig.getWidth() + btnExit.getWidth() + 25;
       Dimension dimApp = new Dimension(width, mainHeight + tableHeight);
       Dimension dimTable = new Dimension(width,tableHeight);
       this.setPreferredSize(dimApp);
       jScrollPane1.setPreferredSize(dimTable);
       jScrollPane1.setMinimumSize(dimTable);
       tblResults.setPreferredSize(dimTable);
       tblResults.setMinimumSize(dimTable);
//       System.err.println("Rows[" + rows + "] =========================");
//       System.err.println("Height should be: [" + mainHeight + "," + tableHeight + "]");
//       System.err.println("jScrollPane1 is:" + jScrollPane1.getHeight());
//       System.err.println("tblResults is:" + tblResults.getHeight());
//       System.err.println("This is:" + this.getHeight());
       if (data != null)
       {  for (AppInfoBean bean:data) {
             addTableRow(bean);
          }
       }
       pack();
     }    
    
    private void addTableRow(AppInfoBean bean) {
       if( tblResults.getModel() instanceof DefaultTableModel )
       {  
          DefaultTableModel model = (DefaultTableModel) tblResults.getModel();
          int cols = model.getColumnCount();
          int rows = model.getRowCount();
          int pos = rows;
          model.insertRow(pos, new Object[cols-1]); //Dunno..
          model.setValueAt(bean.getIcon(), pos, 0); //Image
          model.setValueAt(bean.getName(), pos, 1);
       }
    }          
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        txtInput = new javax.swing.JTextField();
        btnConfig = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();

        setBackground(new java.awt.Color(0, 0, 0));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        pnlMain.setBackground(new java.awt.Color(117, 185, 240));

        txtInput.setPreferredSize(new java.awt.Dimension(150, 20));
        txtInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInputKeyReleased(evt);
            }
        });
        pnlMain.add(txtInput);

        btnConfig.setText("C");
        btnConfig.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConfig.setMargin(new java.awt.Insets(2, 5, 2, 4));
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });
        pnlMain.add(btnConfig);

        btnExit.setText("X");
        btnExit.setAlignmentY(0.0F);
        btnExit.setHideActionText(true);
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setMargin(new java.awt.Insets(2, 5, 2, 4));
        btnExit.setName(""); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        pnlMain.add(btnExit);

        getContentPane().add(pnlMain);

        jScrollPane1.setBackground(new java.awt.Color(0, 153, 0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setAlignmentX(0.1F);
        jScrollPane1.setAlignmentY(0.1F);
        jScrollPane1.setHorizontalScrollBar(null);

        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResults.setAutoscrolls(false);
        tblResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblResults.setShowHorizontalLines(false);
        tblResults.setShowVerticalLines(false);
        tblResults.getTableHeader().setResizingAllowed(false);
        tblResults.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblResults);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnExitActionPerformed

    private void txtInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInputKeyReleased
      int keyCode = evt.getKeyCode();
        boolean isDirty = false;
        
        //Erase Line
        if (keyCode == 27)
        {  System.out.println("Escape Pressed, clearing out search box..");
           if (txtInput.getText().equals(""))
           {  this.setVisible(false);
           }
           else 
           {  isDirty = true; 
              txtInput.setText("");
           }
        }
        //If Config Squiggle
//        else if (input.equals("~")) 
//        {  data = new ArrayList<String>();
//           data.add("About..");
//           data.add("Reload..");
//           data.add("Exit..");
//        }
        //If Blank, Hide Everything..
        if (txtInput.getText().equals("")) 
        {  //Erase data
           _data = null;
           isDirty = true;
           System.out.println("Data Nullified..");
        }
        else {
           List<AppInfoBean> data = _dh.getData(txtInput.getText());
           if (data != null)
           {  _data = data;
              isDirty = true;
              System.out.println("Data Loaded..");
           }
        }
        //Finally reload
        if (isDirty)
        {  fillTable(_data);
           System.out.println("Dirty, Filled Table.");
        }
    }//GEN-LAST:event_txtInputKeyReleased

    
    /**
     * Hide the popup Window if user moves away from it.
     * @param evt 
     */
    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        if ( (dlg != null) && (! dlg.hasFocus() ) ) {}
        else
        {  txtInput.requestFocus();
           this.setVisible(false);
        }
    }//GEN-LAST:event_formWindowLostFocus

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        //Display Config Dialog
        if (dlg == null) { dlg = new DlgConfig(this, true); }
        dlg.centerDialog(0, 0);
        dlg.setVisible(true);
    }//GEN-LAST:event_btnConfigActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnExit;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTable tblResults;
    private javax.swing.JTextField txtInput;
    // End of variables declaration//GEN-END:variables
}
