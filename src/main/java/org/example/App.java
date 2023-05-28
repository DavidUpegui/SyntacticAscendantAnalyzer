package org.example;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

public class App extends JFrame {
    Lexicon anaLex = new Lexicon();
    private JButton btnAnaSintSLR;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JLabel lblResul;
    private JTable tblTokLex;
    private JTextArea txaProgFuente;

    public App() {
        this.initComponents();
    }

    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.btnAnaSintSLR = new JButton();
        this.jScrollPane1 = new JScrollPane();
        this.txaProgFuente = new JTextArea();
        this.jLabel2 = new JLabel();
        this.jScrollPane2 = new JScrollPane();
        this.tblTokLex = new JTable();
        this.lblResul = new JLabel();
        this.setDefaultCloseOperation(3);
        this.setTitle("RECONOCEDOR ASCENDENTE SLR - PARA LA GRAMÁTICA DE ASIGNACIÓN");
        this.setCursor(new Cursor(0));
        this.jLabel1.setText("PROGRAMA FUENTE");
        this.btnAnaSintSLR.setText("ANÁLISIS LÉXICO");
        this.btnAnaSintSLR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                App.this.btnAnaSintSLRActionPerformed(evt);
            }
        });
        this.txaProgFuente.setColumns(20);
        this.txaProgFuente.setRows(5);
        this.jScrollPane1.setViewportView(this.txaProgFuente);
        this.jLabel2.setText("PAREJAS TOKENS-LEXEMAS");
        this.tblTokLex.setModel(new DefaultTableModel(new Object[0][], new String[]{"TOKENS", "LEXEMAS"}) {
            Class[] types = new Class[]{String.class, String.class};
            boolean[] canEdit = new boolean[]{false, false};

            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });
        this.jScrollPane2.setViewportView(this.tblTokLex);
        this.lblResul.setFont(new Font("Tahoma", 0, 14));
        this.lblResul.setText("RESULTADO");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(25, 25, 25).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel1).addComponent(this.jScrollPane1, -1, 239, 32767).addComponent(this.btnAnaSintSLR, -1, -1, 32767)).addGap(18, 18, 32767).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(this.jLabel2).addComponent(this.jScrollPane2, -1, 218, 32767).addComponent(this.lblResul, -1, -1, 32767)).addGap(26, 26, 26)));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(25, 25, 25).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jLabel2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(this.jScrollPane1, -1, 266, 32767).addComponent(this.jScrollPane2, -2, 0, 32767)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.btnAnaSintSLR).addComponent(this.lblResul)).addGap(24, 24, 24)));
        this.pack();
    }

    private void btnAnaSintSLRActionPerformed(ActionEvent evt) {
        this.anaLex.Start();
        if (this.anaLex.Analyze(this.txaProgFuente.getText())) {
            DefaultTableModel modelo = (DefaultTableModel)this.tblTokLex.getModel();
            modelo.setRowCount(0);

            for(int i = 0; i < this.anaLex.noTokens(); ++i) {
                Object[] o = new Object[]{this.anaLex.Tokens()[i], this.anaLex.Lexemas()[i]};
                modelo.addRow(o);
            }
        } else {
            this.lblResul.setText("ERROR LEXICO...");
        }

    }

    public static void main(String[] args) {
        try {
            UIManager.LookAndFeelInfo[] var1 = UIManager.getInstalledLookAndFeels();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                UIManager.LookAndFeelInfo info = var1[var3];
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new App()).setVisible(true);
            }
        });
    }
}
