package excel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ArquivoGenericoExcel {
	public int IMAGEM;
	public int TITULO;
	public int ISBN;
	public POIFSFileSystem fis;
	private Sheet sheet;
	private Workbook workbook;

	public ArquivoGenericoExcel(String path)
			throws FileNotFoundException, IOException, InvalidFormatException, Exception {
		this.workbook = WorkbookFactory.create(new BufferedInputStream(new FileInputStream(path)));
	}

	public List<String> percorreSheets() {
		int numSheets = this.workbook.getNumberOfSheets();

		ArrayList<String> linhas = new ArrayList<>();
		for (int i = 0; i < numSheets; i++) {
			Sheet sheetAux = this.workbook.getSheetAt(i);
			linhas.addAll(retornaLinhasPalavraChave("MLB", sheetAux));
		}

		return linhas;
	}

	private ArrayList<String> retornaLinhasPalavraChave(String chave, Sheet sheetAux) {
		int num = 0;
		Row row = sheetAux.getRow(0);
		ArrayList<String> linhas = new ArrayList<>();

		while (row != null) {
			String textoLinha = "";
			textoLinha = percorreLinha(row);
			if (textoLinha.startsWith("Código do anúncio") || textoLinha.startsWith(chave)) {
				if (linhas.isEmpty()) { //Primeira linha adicionada
					textoLinha = textoLinha + adicionarNovasColunas("Capas", "Preco");
				}
				linhas.add(textoLinha);
			}
			row = sheetAux.getRow(++num);
		}
		return linhas;
	}

	private String adicionarNovasColunas(String... args) {
		String linhaFinal = "";
		for(String coluna : args) {
			linhaFinal = linhaFinal + ";" + coluna;
		}
		return linhaFinal;
	}

	private String percorreLinha(Row row) {
		int numCell = row.getLastCellNum();
		String linha = "";
		for (int i = 0; i < numCell; i++) {
			if(row.getCell(i) != null)
				linha = linha + row.getCell(i).toString().replace(";", "") + " ;";
			else
				linha = linha + ";";
		}
		return linha;
	}

	private String retornaLinhasPalavraChave(String string) {
		return null;
	}

	private int procuraColuna(String... buscas) throws Exception {
		Iterator<Cell> cellIterator = this.sheet.getRow(0).cellIterator();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			String value = cell.getStringCellValue();
			byte b;
			int i;
			String[] arrayOfString;
			for (i = (arrayOfString = buscas).length, b = 0; b < i;) {
				String busca = arrayOfString[b];
				if (StringUtils.containsIgnoreCase(value, busca))
					return cell.getColumnIndex();
				b++;
			}

		}
		return -1;
	}

	private int primeiraColunaVazia() {
		return this.sheet.getRow(0).getLastCellNum();
	}

	public int retornaNumeroTotalLinhas() {
		return this.sheet.getLastRowNum();
	}

	public String retornaDadoLinhaColuna(int linha, int coluna) {
		if (this.sheet.getRow(linha) != null && this.sheet.getRow(linha).getCell(coluna) != null) {
			this.sheet.getRow(linha).getCell(coluna).setCellType(CellType.STRING);
			if (this.sheet.getRow(linha).getCell(coluna).getStringCellValue() != null)
				return this.sheet.getRow(linha).getCell(coluna).getStringCellValue();
		}
		return "";
	}

	public void gravaDadoLinhaColuna(int linha, int coluna, String dado) throws IOException {
		this.sheet.getRow(linha).createCell(coluna).setCellValue(dado);
	}

	public void registrarCapasPeloIsbnArquivoExterno(String pathArquivo) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(pathArquivo));

		String linha = "";
		while ((linha = reader.readLine()) != null) {
			String[] resultado = linha.split(";");
			int posicaoISBN = (linha.split(";")).length - 2;
			String imagem = resultado[posicaoISBN + 1];
			int linhaISBN = retornaLinhaExpressaoPorColuna(resultado[posicaoISBN], this.ISBN);
			if (linhaISBN != 0) {
				registraCapaExcel(linhaISBN, imagem);
				System.out.print("registrei na linha " + linhaISBN);
			}
		}
		reader.close();
	}

	private int retornaLinhaExpressaoPorColuna(String isbnBuscado, int colunaISBN) {
		for (int i = 1; i < this.sheet.getLastRowNum(); i++) {
			if (this.sheet.getRow(i).getCell(colunaISBN) != null
					&& this.sheet.getRow(i).getCell(colunaISBN).getStringCellValue().equals(isbnBuscado)) {
				System.out.println("achou " + i);
				return i;
			}
		}

		return 0;
	}

	public void registraCapaExcel(int linha, String imagem) throws IOException {
		gravaDadoLinhaColuna(linha, this.IMAGEM, imagem);
	}

	public void registrarCabecalhoImagem(String titulo) throws IOException {
		gravaDadoLinhaColuna(0, this.IMAGEM, titulo);
	}

	public void salvaPlanilha(String path) throws FileNotFoundException, IOException {
		this.workbook.write(new FileOutputStream(path));
	}

	public void registrarCSV(String path) throws IOException {
		BufferedWriter arquivo = new BufferedWriter(new FileWriter(String.valueOf(path) + ".csv"));
		Row row = null;
		for (int i = 0; i <= this.sheet.getLastRowNum(); i++) {
			row = this.sheet.getRow(i);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				arquivo.append(row.getCell(j) + ";");
				System.out.print("\"" + row.getCell(j) + "\";");
			}
			arquivo.append("\n");
			System.out.println();
		}
		arquivo.close();
	}

	public Sheet getSheet() {
		return this.sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}
}
