 package view;
 
 import excel.ArquivoGenericoExcel;
 import java.io.BufferedWriter;
 import java.io.File;
 import java.io.FileWriter;
 import java.util.ArrayList;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
 
 
 
 public class ExtratorMercadoLivre {
   private ArrayList<String> resultados;
   private ArquivoGenericoExcel excel;
   private AutomacaoMercadoLivre automacao;
   private BufferedWriter writer;
   private String path;
   private String parent;
   private String nome;
   
   public ExtratorMercadoLivre(String path, String parent, String nome) {
     this.path = path;
     this.parent = parent;
     this.nome = nome;
   }
 
   private String mudarExtensao(String nome) {
     if (nome.contains(".xls")) {
       return String.valueOf(nome.split(".xls")[0]) + ".csv";
     }
     return nome;
   }
 
   
   public void extrairDados(JLabel text) throws Exception {
     try {
       this.nome = mudarExtensao(this.nome);
       this.writer = new BufferedWriter(new FileWriter(new File(String.valueOf(this.parent) + File.separatorChar + "extracao-" + this.nome)));
       this.resultados = new ArrayList<>();
       this.excel = new ArquivoGenericoExcel(this.path);
     } catch (Exception e) {
       apresentarMensagemErro("Erro ao gerar arquivo, tente novamente");
       e.printStackTrace();
     } 
 
     
     text.setText("Inicializando extração");
     this.automacao = new AutomacaoMercadoLivre();
     int elementos = 0;
     try {
       for (String linha : this.excel.percorreSheets()) {
         this.resultados.add(this.automacao.buscaResultado(linha));
         text.setText("elementos encontrados: " + ++elementos);
       } 
       
       for (String resultado : this.resultados) {
         this.writer.append(String.valueOf(resultado) + System.lineSeparator());
         text.setText("escrevendo item : " + resultado);
       }
     
     } catch (Exception e) {
       apresentarMensagemErro("Erro extraindo dados");
       e.printStackTrace();
     } 
     this.writer.close();
     JOptionPane.showMessageDialog(null, "Extração finalizada", "Finalização OK", 1);
   }
   
   private void apresentarMensagemErro(String msg) {
     JOptionPane.showMessageDialog(null, msg, "Erro", 0);
   }
 }


