 package view;
 
 import java.awt.Color;
 import java.awt.EventQueue;
 import java.awt.Font;
 import java.awt.LayoutManager;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JTextField;
 import javax.swing.border.LineBorder;
 
 public class TelaExtrator {
   private JFrame frmExtratorMercadoLivre;
   private JTextField txtArquivo;
   private JLabel txtFeedback;
   private ExtratorMercadoLivre extrator;
   
   public static void main(String[] args) {
     EventQueue.invokeLater(new Runnable() {
           public void run() {
             try {
               TelaExtrator window = new TelaExtrator();
               window.frmExtratorMercadoLivre.setVisible(true);
             } catch (Exception e) {
               e.printStackTrace();
             } 
           }
         });
   }
   
   public TelaExtrator() {
     initialize();
   }
 
   private void initialize() {
     this.frmExtratorMercadoLivre = new JFrame();
     this.frmExtratorMercadoLivre.setTitle("Extrator - Mercado Livre");
     this.frmExtratorMercadoLivre.setBounds(100, 100, 588, 447);
     this.frmExtratorMercadoLivre.setDefaultCloseOperation(3);
     this.frmExtratorMercadoLivre.getContentPane().setLayout((LayoutManager)null);
     
     JLabel lblNewLabel = new JLabel("Local do arquivo");
     lblNewLabel.setBounds(20, 88, 110, 20);
     this.frmExtratorMercadoLivre.getContentPane().add(lblNewLabel);
     
     this.txtArquivo = new JTextField();
     this.txtArquivo.setBounds(132, 88, 272, 20);
     this.frmExtratorMercadoLivre.getContentPane().add(this.txtArquivo);
     this.txtArquivo.setColumns(10);
     
     this.txtFeedback = new JLabel();
     this.txtFeedback.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
     this.txtFeedback.setBounds(35, 228, 509, 144);
     this.txtFeedback.setText("");
     this.frmExtratorMercadoLivre.getContentPane().add(this.txtFeedback);
     
     JButton btnNewButton = new JButton("Pesquisar");
     btnNewButton.setIcon(new ImageIcon(TelaExtrator.class.getResource("/images/build.png")));
     btnNewButton.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e) {
             JFileChooser chooser = new JFileChooser();
             chooser.setFileSelectionMode(0);
             if (chooser.showSaveDialog(null) != 1) {
               File arquivo = chooser.getSelectedFile();
               TelaExtrator.this.txtArquivo.setText(arquivo.getAbsolutePath());
               TelaExtrator.this.extrator = new ExtratorMercadoLivre(
                   arquivo.getAbsolutePath(), 
                   arquivo.getParent(), 
                   arquivo.getName());
             } 
           }
         });
     
     btnNewButton.setBounds(434, 83, 110, 31);
     this.frmExtratorMercadoLivre.getContentPane().add(btnNewButton);
 
     
     JButton btnNewButton_1 = new JButton("Iniciar extração");
     btnNewButton_1.setIcon(new ImageIcon(TelaExtrator.class.getResource("/images/run.png")));
     btnNewButton_1.addActionListener(new ActionListener()
         {
           public void actionPerformed(ActionEvent e) {
             try {
               TelaExtrator.this.extrator.extrairDados(TelaExtrator.this.txtFeedback);
             } catch (Exception e1) {
               e1.printStackTrace();
             } 
           }
         });
     btnNewButton_1.setBounds(174, 139, 210, 40);
     this.frmExtratorMercadoLivre.getContentPane().add(btnNewButton_1);
     
     JLabel lblNewLabel_1 = new JLabel("Mercado Livre - Scrapper");
     lblNewLabel_1.setFont(new Font("Tahoma", 1, 18));
     lblNewLabel_1.setHorizontalAlignment(0);
     lblNewLabel_1.setBounds(84, 21, 401, 31);
     this.frmExtratorMercadoLivre.getContentPane().add(lblNewLabel_1);
   }
 }


