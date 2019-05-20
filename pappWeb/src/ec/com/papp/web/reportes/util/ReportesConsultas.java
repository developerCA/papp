package ec.com.papp.web.reportes.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ec.com.papp.reporte.to.S01TO;
import ec.com.papp.web.comun.util.ExcelUtil;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;

public class ReportesConsultas {

	
	public static void generarXcelSueldosparticipes(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			S01TO s01TO=new S01TO();
			s01TO.getId().setEjerciciofiscal(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				s01TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				s01TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				s01TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				s01TO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				s01TO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				s01TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				s01TO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				s01TO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				s01TO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				s01TO.getId().setItemid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("subitemid")!=null)
				s01TO.getId().setSubitemid(Long.valueOf(parameters.get("subitemid")));

			Collection<S01TO> s01tos=UtilSession.reporteServicio.transobtenerS01(s01TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="S1"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
			response.setHeader("Content-Disposition", "attachment; filename="+nombrearchivo+".xls");
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Hoja1");
			Map<String, HSSFCellStyle> styles = ExcelUtil.createStyles(wb);
			HSSFRow row = sheet.createRow((short)0);
			//pestana aportes
			HSSFCell cell = row.createCell(0);
			int fila=0;
			//Titulos de la grilla
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("INSTITUTO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(1);
			cell.setCellValue("ENTIDAD");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(2);
			cell.setCellValue("OBJ.MIDENA");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(3);
			cell.setCellValue("OBJ. FF.AA.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(4);
			cell.setCellValue("UNIDAD");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(5);
			cell.setCellValue("PROGRAMA");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(6);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(7);
			cell.setCellValue("ACTIVIDAD");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(8);
			cell.setCellValue("SUBACTIVDAD");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(9);
			cell.setCellValue("TAREA");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(10);
			cell.setCellValue("SUBTAREA");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(11);
			cell.setCellValue("ITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(12);
			cell.setCellValue("DESCRIPCIONITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(13);
			cell.setCellValue("SUBITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(14);
			cell.setCellValue("CANTON");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(15);
			cell.setCellValue("FUENTE");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(16);
			cell.setCellValue("DENOMINACION");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(17);
			cell.setCellValue("INICIAL");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(18);
			cell.setCellValue("REFORMAS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(19);
			cell.setCellValue("CODIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(20);
			cell.setCellValue("C.FONDOS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(21);
			cell.setCellValue("COMPROMISO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(22);
			cell.setCellValue("DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(23);
			cell.setCellValue("S. COM.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(24);
			cell.setCellValue("S. DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(25);
			cell.setCellValue("POR COMPROMETER C.F (TRÁMITE)");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(26);
			cell.setCellValue("ANTICIPO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(27);
			cell.setCellValue("%DEV.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(28);
			cell.setCellValue("DISPONIBLE");
			cell.setCellStyle(styles.get("titulo"));

			fila++;
			for(S01TO s01to2:s01tos){
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(s01to2.getInstitucioncodigo() +" - " + s01to2.getInstitucionnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(1);
				cell.setCellValue(s01to2.getInstitucionentcodigo() +" - " + s01to2.getInstitucionentnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(2);
				cell.setCellValue(s01to2.getOperativodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(3);
				cell.setCellValue(s01to2.getEstrategicodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(4);
				cell.setCellValue(s01to2.getUnidadcodigo() +" - " + s01to2.getUnidadnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(5);
				cell.setCellValue(s01to2.getProgramacodigo() +" - " + s01to2.getProgramadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(6);
				cell.setCellValue(s01to2.getProyectocodigo() +" - " + s01to2.getProyectodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(7);
				cell.setCellValue(s01to2.getActividadcodigo() +" - " + s01to2.getActividaddescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(8);
				cell.setCellValue(s01to2.getSubactividadcodigo() +" - " + s01to2.getSubactividaddescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(9);
				cell.setCellValue(s01to2.getTareacodigo() +" - " + s01to2.getTarueadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(10);
				cell.setCellValue(s01to2.getSubtareacodigo() +" - " + s01to2.getSubtareadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(11);
				cell.setCellValue(s01to2.getItemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(12);
				cell.setCellValue(s01to2.getItemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(13);
				cell.setCellValue(s01to2.getSubitemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(14);
				cell.setCellValue(s01to2.getDivisiongeograficacodigo().substring(2, 6));
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(15);
				cell.setCellValue(s01to2.getFuentecodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(16);
				cell.setCellValue(s01to2.getSubitemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(17);
				if(s01to2.getValorinicial()!=null)
					cell.setCellValue(s01to2.getValorinicial());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(18);
				if(s01to2.getReforma()!=null)
					cell.setCellValue(s01to2.getReforma());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(19);
				double inicial=s01to2.getValorinicial();
				double vreforma=0.0;
				if(s01to2.getReforma()!=null)
					vreforma=s01to2.getReforma();
				double codificado=s01to2.getValorinicial()+vreforma;
				cell.setCellValue(UtilGeneral.redondear(codificado,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(20);
				if(s01to2.getCertfondos()!=null)
					cell.setCellValue(s01to2.getCertfondos());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(21);
				if(s01to2.getCompromiso()!=null)
					cell.setCellValue(s01to2.getCompromiso());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(22);
				if(s01to2.getDevengo()!=null)
					cell.setCellValue(s01to2.getDevengo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(23);
				double scom=codificado-s01to2.getCompromiso();
				cell.setCellValue(UtilGeneral.redondear(scom,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(24);
				double sdev=scom+s01to2.getCompromiso()-s01to2.getDevengo();
				cell.setCellValue(UtilGeneral.redondear(sdev,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(25);
				if(s01to2.getXcomprometer()!=null)
					cell.setCellValue(s01to2.getXcomprometer());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(26);
				if(s01to2.getValanticipo()!=null)
					cell.setCellValue(s01to2.getValanticipo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(27);
				cell.setCellValue("0.0");
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(28);
				double disponible=codificado-s01to2.getXcomprometer()-s01to2.getCertfondos()-s01to2.getCompromiso();
				cell.setCellValue(UtilGeneral.redondear(disponible,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				fila++;
			}

			row = sheet.createRow((short)fila);
			sheet.setColumnWidth(0, 256*40);
			sheet.setColumnWidth(1, 256*40);
			sheet.setColumnWidth(2, 256*40);
			sheet.setColumnWidth(3, 256*40);
			sheet.setColumnWidth(4, 256*15);
			sheet.setColumnWidth(5, 256*15);
			sheet.setColumnWidth(6, 256*15);
			sheet.setColumnWidth(7, 256*15);
			sheet.setColumnWidth(8, 256*15);
			sheet.setColumnWidth(9, 256*15);
			sheet.setColumnWidth(10, 256*15);
			sheet.setColumnWidth(11, 256*15);
			sheet.setColumnWidth(12, 256*15);
			sheet.setColumnWidth(13, 256*15);
			sheet.setColumnWidth(14, 256*15);
			sheet.setColumnWidth(15, 256*15);
			sheet.setColumnWidth(16, 256*20);
			sheet.setColumnWidth(17, 256*15);
			sheet.setColumnWidth(18, 256*15);
			sheet.setColumnWidth(19, 256*15);
			sheet.setColumnWidth(20, 256*15);
			sheet.setColumnWidth(21, 256*15);
			sheet.setColumnWidth(22, 256*15);
			sheet.setColumnWidth(23, 256*15);
			sheet.setColumnWidth(24, 256*15);
			sheet.setColumnWidth(25, 256*15);
			sheet.setColumnWidth(26, 256*15);
			sheet.setColumnWidth(27, 256*15);
			sheet.setColumnWidth(28, 256*15);
			

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}


	
}
