 package view;
 
 import org.openqa.selenium.By;
 import org.openqa.selenium.WebDriver;
 import selenium.DriverExtrator;
 
 public class AutomacaoMercadoLivre{
	 
   private static final String IMAGEM = "//img[@data-index='0']";
   private static final String URL_ML = "https://produto.mercadolivre.com.br/";
   private DriverExtrator extrator = new DriverExtrator(false, false, false);
   private WebDriver driver = this.extrator.getDriver();
 
   
   public String buscaResultado(String linha) throws InterruptedException {
     if (ehProdutoML(linha)) {
       String produtoNormalizado = retornaProdutoNormalizado(linha);
       this.driver.get("https://produto.mercadolivre.com.br/" + produtoNormalizado);
       this.extrator.waitForLoad();
       if (this.extrator.existeElemento("//img[@data-index='0']")) {
         String imagem = "";
         String preco = "R$ ";

         imagem = this.driver.findElement(By.xpath("//img[@data-index='0']")).getAttribute("src").toString();

         if(extrator.existeElemento("//meta[@itemprop='price']/following-sibling::span[2]")){
           preco = preco + this.driver.findElement(By.xpath("//meta[@itemprop='price']/following-sibling::span[2]")).getText();
         } else {
           preco = preco + "0,00";
         }

         preco = preco.replace("\n", "");
         imagem = imagem != "" ? imagem + ".jpg" : "" ;
         linha = linha + ";" + imagem + ";" + preco;
         System.out.println(imagem);
       } 
     } 
     return linha;
   }
   
   private String retornaProdutoNormalizado(String linha) {
     String[] linhaInteira = linha.split(";");
     String[] codigoMLB = linhaInteira[0].split("MLB");
     return "MLB-" + codigoMLB[1];
   }
   
   private boolean ehProdutoML(String linha) {
     return linha.split(";")[0].contains("MLB");
   }
 }
