package ec.com.papp.web.comun.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.lowagie.text.pdf.PdfPageEventHelper;

public class ExcelUtil extends PdfPageEventHelper{
	public static Map<String, HSSFCellStyle> createStyles(HSSFWorkbook wb){
		Map<String, HSSFCellStyle> styles = new HashMap<String, HSSFCellStyle>();
		HSSFCellStyle style = wb.createCellStyle();
        //ESTILO PARA LOS TITULO FISPUCE
		HSSFFont tituloFispuce = wb.createFont();
		//tituloFispuce.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		tituloFispuce.setFontHeightInPoints(new Short("22"));
		tituloFispuce.setFontName("Verdana");
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFont(tituloFispuce);
        styles.put("tTituloFispuce", style);
		//ESTILO PARA LOS SUBTITULOS DEL REPORTE....
        style = wb.createCellStyle();
		HSSFFont tituloFont = wb.createFont();
		tituloFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		tituloFont.setFontHeightInPoints(new Short("9"));
		tituloFont.setFontName("Verdana");
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFont(tituloFont);
        styles.put("treporteTitulo", style);
		//ESTILO PARA LOS SUBTITULOS DEL REPORTE....
        style = wb.createCellStyle();
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(headerFont);
        styles.put("treporte", style);
        //ESTILO PARA LOS VALORES DE LOS TITULOS...
        style = wb.createCellStyle();
        HSSFFont Font1 = wb.createFont();
        Font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setFont(Font1);
        styles.put("vreporte", style);
        //ESTILO PARA LOS TITULOS....
        style = wb.createCellStyle();
		HSSFFont Font2 = wb.createFont();
        Font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font2.setFontHeightInPoints(new Short("8"));
        Font2.setFontName("Verdana"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(Font2);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("titulo", style);
        //ESTILO PARA LOS CONTENIDOS TEXTO....
        style = wb.createCellStyle();
		HSSFFont fontContenido = wb.createFont();
        fontContenido.setFontHeightInPoints(new Short("8"));
        fontContenido.setFontName("Verdana"); 
        style.setFont(fontContenido);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("contenido", style);
        //ESTILO PARA LOS CONTENIDOS NUMEROS....
        style = wb.createCellStyle();
		HSSFFont fontNumeros = wb.createFont();
		fontNumeros.setFontHeightInPoints(new Short("8"));
		fontNumeros.setFontName("Verdana"); 
        style.setFont(fontNumeros);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        DataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00_);(#,##0.00)"));
        styles.put("contenidonumero", style);
        //ESTILO PARA LOS CONTENIDOS NUMEROS EN NEGRITAS....
        style = wb.createCellStyle();
		HSSFFont fontNumerosNeg = wb.createFont();
		fontNumerosNeg.setFontHeightInPoints(new Short("8"));
		fontNumerosNeg.setFontName("Verdana"); 
        fontNumerosNeg.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(fontNumerosNeg);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00_);(#,##0.00)"));
        styles.put("contenidonumeroneg", style);
        //ESTILO PARA LOS CONTENIDOS FECHAS....
        style = wb.createCellStyle();
		HSSFFont fontFecha = wb.createFont();
		fontFecha.setFontHeightInPoints(new Short("8"));
		fontFecha.setFontName("Verdana"); 
        style.setFont(fontFecha);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("dd/mm/yyyy"));
        styles.put("contenidofecha", style);
        
        //Estilos para oficio comite--------------------------------------------------------------------------------
        //ESTILO PARA LOS TITULOS....
        style = wb.createCellStyle();
		HSSFFont oftitulo = wb.createFont();
		oftitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        oftitulo.setFontHeightInPoints(new Short("11"));
        oftitulo.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(oftitulo);
        styles.put("oftitulo", style);
        
      //ESTILO PARA LOS TITULO GRILLA....
        style = wb.createCellStyle();
        HSSFFont ofsubtitulo = wb.createFont();
        ofsubtitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        ofsubtitulo.setFontHeightInPoints(new Short("11"));
        ofsubtitulo.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofsubtitulo);
        style.setBorderBottom(CellStyle.BORDER_MEDIUM);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_MEDIUM);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_MEDIUM);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("ofsubtitulo", style);
  		  		
      //ESTILO PARA CONTENIDO NEGRITA....
        style = wb.createCellStyle();
        HSSFFont ofcontenidon = wb.createFont();
        ofcontenidon.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ofcontenidon.setFontHeightInPoints(new Short("11"));
        ofcontenidon.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidon);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        styles.put("ofcontenidon", style);
        
        //ESTILO PARA CONTENIDO CENTRADO....
        style = wb.createCellStyle();
        HSSFFont ofcontenidoc = wb.createFont();
        ofcontenidoc.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        ofcontenidoc.setFontHeightInPoints(new Short("11"));
        ofcontenidoc.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidoc);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        styles.put("ofcontenidoc", style);

        //ESTILO PARA CONTENIDO DERECHA....
        style = wb.createCellStyle();
        HSSFFont ofcontenidod = wb.createFont();
        ofcontenidod.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        ofcontenidod.setFontHeightInPoints(new Short("11"));
        ofcontenidod.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidod);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        styles.put("ofcontenidod", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS EN NEGRITAS....
        style = wb.createCellStyle();
        HSSFFont ofnumeron = wb.createFont();
        ofnumeron.setFontHeightInPoints(new Short("11"));
        ofnumeron.setFontName("Calibri"); 
        ofnumeron.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(ofnumeron);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00_);(#,##0.00)"));
        styles.put("ofnumeron", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS....
        style = wb.createCellStyle();
        HSSFFont ofnumero = wb.createFont();
        ofnumero.setFontHeightInPoints(new Short("11"));
        ofnumero.setFontName("Calibri"); 
        ofnumero.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(ofnumero);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00_);(#,##0.00)"));
        styles.put("ofnumero", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS EN NEGRITAS $....
        style = wb.createCellStyle();
        HSSFFont ofnumero$n = wb.createFont();
        ofnumero$n.setFontHeightInPoints(new Short("11"));
        ofnumero$n.setFontName("Calibri"); 
        ofnumero$n.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(ofnumero$n);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("$ #,##0.00_);($ #,##0.00)"));
        styles.put("ofnumero$n", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS $....
        style = wb.createCellStyle();
        HSSFFont ofnumero$ = wb.createFont();
        ofnumero$.setFontHeightInPoints(new Short("11"));
        ofnumero$.setFontName("Calibri"); 
        ofnumero$.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(ofnumero$);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());  
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("$ #,##0.00_);($ #,##0.00)"));
        styles.put("ofnumero$", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS EN NEGRITAS $ SIN BORDES....
        style = wb.createCellStyle();
        HSSFFont ofnumero$nsb = wb.createFont();
        ofnumero$nsb.setFontHeightInPoints(new Short("11"));
        ofnumero$nsb.setFontName("Calibri"); 
        ofnumero$nsb.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(ofnumero$nsb);
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("$ #,##0.00_);($ #,##0.00)"));
        styles.put("ofnumero$nsb", style);
        
        //ESTILO PARA LOS CONTENIDOS NUMEROS $ SIN BORDES....
        style = wb.createCellStyle();
        HSSFFont ofnumero$sb = wb.createFont();
        ofnumero$sb.setFontHeightInPoints(new Short("11"));
        ofnumero$sb.setFontName("Calibri"); 
        ofnumero$sb.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(ofnumero$sb);
        style.setDataFormat(format.getFormat("$ #,##0.00_);($ #,##0.00)"));
        styles.put("ofnumero$sb", style);
        
        //ESTILO PARA LOS CONTENIDOS SUBTOTAL EN NEGRITAS $ ....
        style = wb.createCellStyle();
        HSSFFont ofsubtotal = wb.createFont();
        ofsubtotal.setFontHeightInPoints(new Short("11"));
        ofsubtotal.setFontName("Calibri"); 
        ofsubtotal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(ofsubtotal);
		style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("$ #,##0.00_);($ #,##0.00)"));
        styles.put("ofsubtotal", style);
        
        //ESTILO PARA CONTENIDO NEGRITA SIN BORDES....
        style = wb.createCellStyle();
        HSSFFont ofcontenidonsb = wb.createFont();
        ofcontenidonsb.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ofcontenidonsb.setFontHeightInPoints(new Short("11"));
        ofcontenidonsb.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidonsb);
        styles.put("ofcontenidonsb", style);

        //ESTILO PARA CONTENIDO SIN BORDES....
        style = wb.createCellStyle();
        HSSFFont ofcontenidosb = wb.createFont();
        ofcontenidosb.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ofcontenidosb.setFontHeightInPoints(new Short("11"));
        ofcontenidosb.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidosb);
        styles.put("ofcontenidosb", style);
        
      //ESTILO PARA CONTENIDO SIN BORDES DERECHA....
        style = wb.createCellStyle();
        HSSFFont ofcontenidosd = wb.createFont();
        ofcontenidosd.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        ofcontenidosd.setFontHeightInPoints(new Short("11"));
        ofcontenidosd.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidosb);
        styles.put("ofcontenidosd", style);
        

        //ESTILO PARA LOS CONTENIDOS FIRMA ....
        style = wb.createCellStyle();
        HSSFFont ofcontenidosf = wb.createFont();
        ofcontenidosf.setFontHeightInPoints(new Short("11"));
        ofcontenidosf.setFontName("Calibri"); 
        ofcontenidosf.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_JUSTIFY);
        style.setFont(ofcontenidosf);
		style.setBorderTop(CellStyle.BORDER_THIN);
	    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	    styles.put("offirma", style);

        //ESTILO PARA OBSERVACIONES....
	    style = wb.createCellStyle();
        HSSFFont ofobservacion = wb.createFont();
        ofobservacion.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        ofobservacion.setFontHeightInPoints(new Short("11"));
        ofobservacion.setFontName("Calibri"); 
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        style.setFont(ofobservacion);
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());        
        
        styles.put("ofobservacion", style);        
  		return styles;
	}
}
