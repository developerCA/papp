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
import org.apache.poi.ss.util.CellRangeAddress;

import ec.com.papp.reporte.to.Cabs01TO;
import ec.com.papp.reporte.to.P01TO;
import ec.com.papp.reporte.to.P02TO;
import ec.com.papp.reporte.to.P03TO;
import ec.com.papp.reporte.to.P04TO;
import ec.com.papp.reporte.to.P04pacTO;
import ec.com.papp.reporte.to.P05TO;
import ec.com.papp.reporte.to.S01TO;
import ec.com.papp.reporte.to.S03TO;
import ec.com.papp.web.comun.util.ExcelUtil;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;

public class ReportesConsultas {

	
	public static void generarS01plano(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,23)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,23)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("S-1 SEG. A LA EJECUCIÓN PAP - SUBITEM");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,23)); 
			fila++;

			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("INSTITUCION");
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
			cell.setCellValue("CORRELATIVO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(14);
			cell.setCellValue("PRESTAMO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(15);
			cell.setCellValue("SUBITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(16);
			cell.setCellValue("CANTON");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(17);
			cell.setCellValue("FUENTE");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(18);
			cell.setCellValue("DENOMINACION");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(19);
			cell.setCellValue("INICIAL");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(20);
			cell.setCellValue("REFORMAS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(21);
			cell.setCellValue("CODIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(22);
			cell.setCellValue("C.FONDOS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(23);
			cell.setCellValue("COMPROMISO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(24);
			cell.setCellValue("DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(25);
			cell.setCellValue("S. COM.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(26);
			cell.setCellValue("S. DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(27);
			cell.setCellValue("POR COMPROMETER C.F (TRÁMITE)");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(28);
			cell.setCellValue("ANTICIPO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(29);
			cell.setCellValue("%DEV.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(30);
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
				cell.setCellValue(s01to2.getEstrategicodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(3);
				cell.setCellValue(s01to2.getOperativodescripcion());
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
				cell.setCellValue(s01to2.getOrganismocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(14);
				cell.setCellValue(s01to2.getOrganismoprestamocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(15);
				cell.setCellValue(s01to2.getSubitemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(16);
				cell.setCellValue(s01to2.getDivisiongeograficacodigo().substring(2, 6));
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(17);
				cell.setCellValue(s01to2.getFuentecodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(18);
				cell.setCellValue(s01to2.getSubitemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(19);
				if(s01to2.getValorinicial()!=null)
					cell.setCellValue(s01to2.getValorinicial());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(20);
				if(s01to2.getReforma()!=null)
					cell.setCellValue(s01to2.getReforma());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(21);
				double inicial=s01to2.getValorinicial();
				double vreforma=0.0;
				if(s01to2.getReforma()!=null)
					vreforma=s01to2.getReforma();
				double codificado=s01to2.getValorinicial()+vreforma;
				cell.setCellValue(UtilGeneral.redondear(codificado,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(22);
				if(s01to2.getCertfondos()!=null)
					cell.setCellValue(s01to2.getCertfondos());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(23);
				if(s01to2.getCompromiso()!=null)
					cell.setCellValue(s01to2.getCompromiso());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(24);
				if(s01to2.getDevengo()!=null)
					cell.setCellValue(s01to2.getDevengo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(25);
				double scom=codificado-s01to2.getCompromiso();
				cell.setCellValue(UtilGeneral.redondear(scom,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(26);
				double sdev=scom+s01to2.getCompromiso()-s01to2.getDevengo();
				cell.setCellValue(UtilGeneral.redondear(sdev,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(27);
				if(s01to2.getXcomprometer()!=null)
					cell.setCellValue(s01to2.getXcomprometer());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(28);
				if(s01to2.getValanticipo()!=null)
					cell.setCellValue(s01to2.getValanticipo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(29);
				if(s01to2.getDevengo()!=null){
					double dev= (s01to2.getDevengo()/codificado)*100;
					cell.setCellValue(UtilGeneral.redondear(dev, 2));
				}
				else
					cell.setCellValue("0.0");
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(30);
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
			sheet.setColumnWidth(16, 256*15);
			sheet.setColumnWidth(17, 256*15);
			sheet.setColumnWidth(18, 256*20);
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
			sheet.setColumnWidth(29, 256*15);
			sheet.setColumnWidth(30, 256*15);
			

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static void generarS02plano(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
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

			Collection<S01TO> s01tos=UtilSession.reporteServicio.transObtenerS02plana(s01TO);
			System.out.println("numero de registros: " + s01tos.size());
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="S2"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,23)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,23)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("S-2 SEG. A LA EJECUCIÓN PAP - ITEM");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,23)); 
			fila++;

			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("INSTITUTO por INSTITUCIÓN (UDAF)");
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
			cell.setCellValue("ITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(9);
			cell.setCellValue("DENOMINACION");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(10);
			cell.setCellValue("CORRELATIVO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(11);
			cell.setCellValue("PRESTAMO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(12);
			cell.setCellValue("CANTON");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(13);
			cell.setCellValue("FUENTE");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(14);
			cell.setCellValue("INICIAL");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(15);
			cell.setCellValue("REFORMAS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(16);
			cell.setCellValue("CODIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(17);
			cell.setCellValue("C.FONDOS");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(18);
			cell.setCellValue("COMPROMISO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(19);
			cell.setCellValue("DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(20);
			cell.setCellValue("S. COM.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(21);
			cell.setCellValue("S. DEVENGO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(22);
			cell.setCellValue("POR COMPROMETER C.F (TRÁMITE)");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(23);
			cell.setCellValue("ANTICIPO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(24);
			cell.setCellValue("%DEV.");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(25);
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
				cell.setCellValue(s01to2.getEstrategicodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(3);
				cell.setCellValue(s01to2.getOperativodescripcion());
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
				cell.setCellValue(s01to2.getItemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(9);
				cell.setCellValue(s01to2.getItemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(10);
				System.out.println("s01to2.getOrganismocodigo(): "+ s01to2.getOrganismocodigo());
				cell.setCellValue(s01to2.getOrganismocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(11);
				System.out.println("s01to2.getOrganismoprestamocodigo(): " +s01to2.getOrganismoprestamocodigo());
				cell.setCellValue(s01to2.getOrganismoprestamocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(12);
				cell.setCellValue(s01to2.getDivisiongeograficacodigo().substring(2, 6));
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(13);
				cell.setCellValue(s01to2.getFuentecodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(14);
				if(s01to2.getValorinicial()!=null)
					cell.setCellValue(s01to2.getValorinicial());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(15);
				if(s01to2.getReforma()!=null)
					cell.setCellValue(s01to2.getReforma());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(16);
				double inicial=s01to2.getValorinicial();
				double vreforma=0.0;
				if(s01to2.getReforma()!=null)
					vreforma=s01to2.getReforma();
				double codificado=s01to2.getValorinicial()+vreforma;
				cell.setCellValue(UtilGeneral.redondear(codificado,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(17);
				if(s01to2.getCertfondos()!=null)
					cell.setCellValue(s01to2.getCertfondos());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(18);
				if(s01to2.getCompromiso()!=null)
					cell.setCellValue(s01to2.getCompromiso());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(19);
				if(s01to2.getDevengo()!=null)
					cell.setCellValue(s01to2.getDevengo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(20);
				double scom=codificado-s01to2.getCompromiso();
				cell.setCellValue(UtilGeneral.redondear(scom,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(21);
				double sdev=scom+s01to2.getCompromiso()-s01to2.getDevengo();
				cell.setCellValue(UtilGeneral.redondear(sdev,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(22);
				if(s01to2.getXcomprometer()!=null)
					cell.setCellValue(s01to2.getXcomprometer());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(23);
				if(s01to2.getValanticipo()!=null)
					cell.setCellValue(s01to2.getValanticipo());
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(24);
				if(s01to2.getDevengo()!=null){
					double dev= (s01to2.getDevengo()/codificado)*100;
					cell.setCellValue(UtilGeneral.redondear(dev, 2));
				}
				else
					cell.setCellValue("0.0");
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(25);
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
			sheet.setColumnWidth(16, 256*15);
			sheet.setColumnWidth(17, 256*15);
			sheet.setColumnWidth(18, 256*15);
			sheet.setColumnWidth(19, 256*15);
			sheet.setColumnWidth(20, 256*15);
			sheet.setColumnWidth(21, 256*15);
			sheet.setColumnWidth(22, 256*15);
			sheet.setColumnWidth(23, 256*15);
			sheet.setColumnWidth(24, 256*15);
			sheet.setColumnWidth(25, 256*15);
			

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static void generarS03(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			S03TO s03TO=new S03TO();
			s03TO.getId().setEjerciciofiscal(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				s03TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				s03TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				s03TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				s03TO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				s03TO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				s03TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				s03TO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				s03TO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				s03TO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				s03TO.getId().setItemid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("subitemid")!=null)
				s03TO.getId().setSubitemid(Long.valueOf(parameters.get("subitemid")));

			Collection<S03TO> s03tos=UtilSession.reporteServicio.transObtenerS03(s03TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="S3"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,55)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,55)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("S-3 SEG. A LA EJECUCIÓN PAP - PLANIFICADO Vs EJECUTADO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(2,2,0,55)); 
			fila++;

			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,19)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(20);
			cell.setCellValue("ENERO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,20,22)); 
			cell = row.createCell(23);
			cell.setCellValue("FEBRERO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,23,25)); 
			cell = row.createCell(26);
			cell.setCellValue("MARZO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,26,28)); 
			cell = row.createCell(29);
			cell.setCellValue("ABRIL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,29,31)); 
			cell = row.createCell(32);
			cell.setCellValue("MAYO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,32,34)); 
			cell = row.createCell(35);
			cell.setCellValue("JUNIO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,35,37)); 
			cell = row.createCell(38);
			cell.setCellValue("JULIO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,38,40)); 
			cell = row.createCell(41);
			cell.setCellValue("AGOSTO");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,41,43)); 
			cell = row.createCell(44);
			cell.setCellValue("SEPTIEMBRE");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,44,46)); 
			cell = row.createCell(47);
			cell.setCellValue("OCTUBRE");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,47,49)); 
			cell = row.createCell(50);
			cell.setCellValue("NOVIEMBRE");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,50,52)); 
			cell = row.createCell(53);
			cell.setCellValue("DICIEMBRE");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(4,4,53,55)); 
			fila++;

			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("INSTITUCION");
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
			cell.setCellValue("CORRELATIVO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(14);
			cell.setCellValue("PRESTAMO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(15);
			cell.setCellValue("SUBITEM");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(16);
			cell.setCellValue("CANTON");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(17);
			cell.setCellValue("FUENTE");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(18);
			cell.setCellValue("DENOMINACION");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(19);
			cell.setCellValue("CODIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(20);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(21);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(22);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(23);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(24);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(25);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(26);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(27);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(28);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(29);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(30);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(31);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(32);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(33);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(34);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(35);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(36);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(37);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(38);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(39);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(40);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(41);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(42);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(43);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(44);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(45);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(46);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(47);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(48);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(49);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(50);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(51);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(52);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(53);
			cell.setCellValue("PLANIFICADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(54);
			cell.setCellValue("COMPROMISO APROBADO");
			cell.setCellStyle(styles.get("titulo"));
			cell = row.createCell(55);
			cell.setCellValue("DEVENGO APROBADO");
			cell.setCellStyle(styles.get("titulo"));

			fila++;
			double gasto=0.0;
			double cyd=0.0;
			double total=0.0;
			double compromiso=0.0;
			double reversion=0.0;
			for(S03TO s03to:s03tos){
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(s03to.getInstitucioncodigo() +" - " + s03to.getInstitucionnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(1);
				cell.setCellValue(s03to.getInstitucionentcodigo() +" - " + s03to.getInstitucionentnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(2);
				cell.setCellValue(s03to.getEstrategicodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(3);
				cell.setCellValue(s03to.getOperativodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(4);
				cell.setCellValue(s03to.getUnidadcodigo() +" - " + s03to.getUnidadnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(5);
				cell.setCellValue(s03to.getProgramacodigo() +" - " + s03to.getProgramadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(6);
				cell.setCellValue(s03to.getProyectocodigo() +" - " + s03to.getProyectodescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(7);
				cell.setCellValue(s03to.getActividadcodigo() +" - " + s03to.getActividaddescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(8);
				cell.setCellValue(s03to.getSubactividadcodigo() +" - " + s03to.getSubactividaddescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(9);
				cell.setCellValue(s03to.getTareacodigo() +" - " + s03to.getTarueadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(10);
				cell.setCellValue(s03to.getSubtareacodigo() +" - " + s03to.getSubtareadescripcion());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(11);
				cell.setCellValue(s03to.getItemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(12);
				cell.setCellValue(s03to.getItemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(13);
				cell.setCellValue(s03to.getOrganismocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(14);
				cell.setCellValue(s03to.getOrganismoprestamocodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(15);
				cell.setCellValue(s03to.getSubitemcodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(16);
				cell.setCellValue(s03to.getDivisiongeograficacodigo().substring(2, 6));
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(17);
				cell.setCellValue(s03to.getFuentecodigo());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(18);
				cell.setCellValue(s03to.getSubitemnombre());
				cell.setCellStyle(styles.get("contenido"));
				cell = row.createCell(19);
				double vreforma=0.0;
				if(s03to.getReforma()!=null)
					vreforma=s03to.getReforma();
				double codificado=s03to.getValorinicial()+vreforma;
				cell.setCellValue(UtilGeneral.redondear(codificado,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(20);
				if(s03to.getEneroda()!=null)
					cell.setCellValue(s03to.getEneroda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(21);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisoenero()!=null)
					compromiso=s03to.getCompromisoenero();
				if(s03to.getReversionenero()!=null)
					reversion=s03to.getReversionenero();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(22);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastoenero()!=null)
					gasto=s03to.getGastoenero();
				if(s03to.getCydenero()!=null)
					cyd=s03to.getCydenero();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(23);
				if(s03to.getFebreroda()!=null)
					cell.setCellValue(s03to.getFebreroda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(24);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisofebrero()!=null)
					compromiso=s03to.getCompromisofebrero();
				if(s03to.getReversionfebrero()!=null)
					reversion=s03to.getReversionfebrero();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(25);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastofebrero()!=null)
					gasto=s03to.getGastofebrero();
				if(s03to.getCydfebrero()!=null)
					cyd=s03to.getCydfebrero();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(26);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisomarzo()!=null)
					compromiso=s03to.getCompromisomarzo();
				if(s03to.getReversionmarzo()!=null)
					reversion=s03to.getReversionmarzo();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(27);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisomarzo()!=null)
					compromiso=s03to.getCompromisomarzo();
				if(s03to.getReversionmarzo()!=null)
					reversion=s03to.getReversionmarzo();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(28);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastomarzo()!=null)
					gasto=s03to.getGastomarzo();
				if(s03to.getCydmarzo()!=null)
					cyd=s03to.getCydmarzo();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(29);
				if(s03to.getAbrilda()!=null)
					cell.setCellValue(s03to.getAbrilda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(30);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisoabril()!=null)
					compromiso=s03to.getCompromisoabril();
				if(s03to.getReversionabril()!=null)
					reversion=s03to.getReversionabril();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(31);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastoabril()!=null)
					gasto=s03to.getGastoabril();
				if(s03to.getCydabril()!=null)
					cyd=s03to.getCydabril();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(32);
				if(s03to.getMayoda()!=null)
					cell.setCellValue(s03to.getMayoda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(33);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisomayo()!=null)
					compromiso=s03to.getCompromisomayo();
				if(s03to.getReversionmayo()!=null)
					reversion=s03to.getReversionmayo();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(34);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastomayo()!=null)
					gasto=s03to.getGastomayo();
				if(s03to.getCydmayo()!=null)
					cyd=s03to.getCydmayo();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(35);
				if(s03to.getJunioda()!=null)
					cell.setCellValue(s03to.getJunioda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(36);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisojunio()!=null)
					compromiso=s03to.getCompromisojunio();
				if(s03to.getReversionjunio()!=null)
					reversion=s03to.getReversionjunio();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(37);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastojunio()!=null)
					gasto=s03to.getGastojunio();
				if(s03to.getCydjunio()!=null)
					cyd=s03to.getCydjunio();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(38);
				if(s03to.getJulioda()!=null)
					cell.setCellValue(s03to.getJulioda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(39);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisojulio()!=null)
					compromiso=s03to.getCompromisojulio();
				if(s03to.getReversionjulio()!=null)
					reversion=s03to.getReversionjulio();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(40);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastojulio()!=null)
					gasto=s03to.getGastojulio();
				if(s03to.getCydjulio()!=null)
					cyd=s03to.getCydjulio();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(41);
				if(s03to.getAgostoda()!=null)
					cell.setCellValue(s03to.getAgostoda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(42);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisoagosto()!=null)
					compromiso=s03to.getCompromisoagosto();
				if(s03to.getReversionagosto()!=null)
					reversion=s03to.getReversionagosto();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(43);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastoagosto()!=null)
					gasto=s03to.getGastoagosto();
				if(s03to.getCydagosto()!=null)
					cyd=s03to.getCydagosto();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(44);
				if(s03to.getSeptiembreda()!=null)
					cell.setCellValue(s03to.getSeptiembreda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(45);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisoseptiembre()!=null)
					compromiso=s03to.getCompromisoseptiembre();
				if(s03to.getReversionseptiembre()!=null)
					reversion=s03to.getReversionseptiembre();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(46);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastoseptiembre()!=null)
					gasto=s03to.getGastoseptiembre();
				if(s03to.getCydseptiembre()!=null)
					cyd=s03to.getCydseptiembre();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(47);
				if(s03to.getOctubreda()!=null)
					cell.setCellValue(s03to.getOctubreda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(48);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisooctubre()!=null)
					compromiso=s03to.getCompromisooctubre();
				if(s03to.getReversionoctubre()!=null)
					reversion=s03to.getReversionoctubre();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(49);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastooctubre()!=null)
					gasto=s03to.getGastooctubre();
				if(s03to.getCydoctubre()!=null)
					cyd=s03to.getCydoctubre();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(50);
				if(s03to.getNoviembreda()!=null)
					cell.setCellValue(s03to.getNoviembreda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(51);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisonoviembre()!=null)
					compromiso=s03to.getCompromisonoviembre();
				if(s03to.getReversionnoviembre()!=null)
					reversion=s03to.getReversionnoviembre();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(52);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastonoviembre()!=null)
					gasto=s03to.getGastonoviembre();
				if(s03to.getCydnoviembre()!=null)
					cyd=s03to.getCydnoviembre();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));

				cell = row.createCell(53);
				if(s03to.getDiciembreda()!=null)
					cell.setCellValue(s03to.getDiciembreda());
				else
					cell.setCellValue(0.0);
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(54);
				compromiso=0.0;
				reversion=0.0;
				total=0.0;
				if(s03to.getCompromisodiciembre()!=null)
					compromiso=s03to.getCompromisodiciembre();
				if(s03to.getReversiondiciembre()!=null)
					reversion=s03to.getReversiondiciembre();
				total=compromiso-reversion;
				cell.setCellValue(UtilGeneral.redondear(total,2));
				cell.setCellStyle(styles.get("contenidonumero"));
				cell = row.createCell(55);
				gasto=0.0;
				cyd=0.0;
				total=0.0;
				if(s03to.getGastodiciembre()!=null)
					gasto=s03to.getGastodiciembre();
				if(s03to.getCyddiciembre()!=null)
					cyd=s03to.getCyddiciembre();
				total=gasto+cyd;
				cell.setCellValue(UtilGeneral.redondear(total,2));
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
			sheet.setColumnWidth(16, 256*15);
			sheet.setColumnWidth(17, 256*15);
			sheet.setColumnWidth(18, 256*20);
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
			sheet.setColumnWidth(29, 256*15);
			sheet.setColumnWidth(30, 256*15);
			sheet.setColumnWidth(31, 256*15);
			sheet.setColumnWidth(32, 256*15);
			sheet.setColumnWidth(33, 256*15);
			sheet.setColumnWidth(34, 256*15);
			sheet.setColumnWidth(35, 256*15);
			sheet.setColumnWidth(36, 256*15);
			sheet.setColumnWidth(37, 256*15);
			sheet.setColumnWidth(38, 256*15);
			sheet.setColumnWidth(39, 256*15);
			sheet.setColumnWidth(40, 256*15);
			sheet.setColumnWidth(41, 256*15);
			sheet.setColumnWidth(42, 256*15);
			sheet.setColumnWidth(43, 256*15);
			sheet.setColumnWidth(44, 256*15);
			sheet.setColumnWidth(45, 256*15);
			sheet.setColumnWidth(46, 256*15);
			sheet.setColumnWidth(47, 256*15);
			sheet.setColumnWidth(48, 256*15);
			sheet.setColumnWidth(49, 256*15);
			sheet.setColumnWidth(50, 256*15);
			sheet.setColumnWidth(51, 256*15);
			sheet.setColumnWidth(52, 256*15);
			sheet.setColumnWidth(53, 256*15);
			sheet.setColumnWidth(54, 256*15);
			sheet.setColumnWidth(55, 256*15);
			
			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static void generarP01(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P01TO p01TO=new P01TO();
			p01TO.getId().setNivelactividadejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p01TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p01TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p01TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("actividadid")!=null)
				p01TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));

			Collection<P01TO> p01tos=UtilSession.reporteServicio.transObtenerP01(p01TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P1"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,12)); 
			fila++;
			if(p01tos.size()>0){
				p01TO=(P01TO)p01tos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("P-1 MATRIZ PLANIFICACIÓN ESTRATÉGICA");
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,12)); 
				fila++;
				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue("Reporte");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(12);
				cell.setCellValue("P-01");
				cell.setCellStyle(styles.get("titulo"));
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue("PAAP Año: ");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(12);
				cell.setCellValue(p01TO.getEjerciciofiscalanio());
				cell.setCellStyle(styles.get("contenido"));
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue(" Fecha:  ");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(12);
				cell.setCellValue(UtilGeneral.parseDateToString(new Date()));
				cell.setCellStyle(styles.get("contenido"));
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(" Institucion:  ");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(1);
				System.out.println("institucion: " + p01TO.getInstitucionnombre());
				cell.setCellValue(p01TO.getInstitucionnombre());
				cell.setCellStyle(styles.get("contenido"));
				fila++;
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("BASE LEGAL INSTITUCIONAL");
				cell.setCellStyle(styles.get("tituloizquierda"));
				sheet.addMergedRegion(new CellRangeAddress(7,7,0,12)); 
				fila++;
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(p01TO.getBaselegal());
				System.out.println("base legal: " + p01TO.getBaselegal());
				cell.setCellStyle(styles.get("contenido"));
				sheet.addMergedRegion(new CellRangeAddress(8,8,0,12)); 
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("VISION INSTITUCIONAL");
				cell.setCellStyle(styles.get("tituloizquierda"));
				sheet.addMergedRegion(new CellRangeAddress(9,9,0,12)); 
				fila++;
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(p01TO.getVision());
				cell.setCellStyle(styles.get("contenido"));
				sheet.addMergedRegion(new CellRangeAddress(10,10,0,12)); 
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("MISION INSTITUCIONAL");
				cell.setCellStyle(styles.get("tituloizquierda"));
				sheet.addMergedRegion(new CellRangeAddress(11,11,0,12)); 
				fila++;
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue(p01TO.getMision());
				cell.setCellStyle(styles.get("contenido"));
				sheet.addMergedRegion(new CellRangeAddress(12,12,0,12)); 
				fila++;

				
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("OBJETIVO PLAN  NACIONAL");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(1);
				cell.setCellValue("OBJETIVO MIDENA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(2);
				cell.setCellValue("OBJETIVO FF.AA.");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(3);
				cell.setCellValue("OBJETIVO FUERZAS");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(4);
				cell.setCellValue("PROGRAMA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(5);
				cell.setCellValue("ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(6);
				cell.setCellValue("INDICADOR ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(7);
				cell.setCellValue("FÓRMULA ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(8);
				cell.setCellValue("META DESCRIPCIÓN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(9);
				cell.setCellValue("META PLANIFICADA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(10);
				cell.setCellValue("META AJUSTADA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(11);
				cell.setCellValue(" MONTO PLANIFICADO ");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(12);
				cell.setCellValue(" MONTO AJUSTADO ");
				cell.setCellStyle(styles.get("titulo"));
	
				fila++;
				for(P01TO p01to2:p01tos){
					row = sheet.createRow((short)fila);
					cell = row.createCell(0);
					cell.setCellValue(p01to2.getPlancodigo() +" - " + p01to2.getPlandescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(1);
					cell.setCellValue(p01to2.getEstrategicocodigo() +" - " + p01to2.getEstrategicodescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(2);
					cell.setCellValue(p01to2.getOperativocodigo() + " - " + p01to2.getOperativodescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(3);
					cell.setCellValue(p01to2.getFuerzacodigo() + " - " + p01to2.getFuerzadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(4);
					cell.setCellValue(p01to2.getProgramacodigo() +" - " + p01to2.getProgramadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(5);
					cell.setCellValue(p01to2.getActividadcodigo() +" - " + p01to2.getActividaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(6);
					cell.setCellValue(p01to2.getIndicadoractividad());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(7);
					cell.setCellValue(p01to2.getFormulaactividad());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(8);
					cell.setCellValue(p01to2.getMetadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(9);
					if(p01to2.getMetaplanificada()!=null)
						cell.setCellValue(p01to2.getMetaplanificada());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(10);
					if(p01to2.getMetaaprobada()!=null)
						cell.setCellValue(p01to2.getMetaaprobada());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(11);
					if(p01to2.getSumaplanificada()!=null)
						cell.setCellValue(p01to2.getSumaplanificada());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(12);
					if(p01to2.getMetaplanificada()!=null)
						cell.setCellValue(p01to2.getSumaajustada());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					fila++;
				}
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

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}
	
	public static void generarS01bloque(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			Cabs01TO cabs01to=new Cabs01TO();
			cabs01to.getId().setEjerciciofiscal(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				cabs01to.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				cabs01to.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				cabs01to.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				cabs01to.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				cabs01to.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				cabs01to.setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				cabs01to.setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				cabs01to.setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				cabs01to.setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				cabs01to.setItemid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("subitemid")!=null)
				cabs01to.setSubitemid(Long.valueOf(parameters.get("subitemid")));
			Collection<Cabs01TO> s01tos=UtilSession.reporteServicio.transobtenerS01bloques(cabs01to);
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}
	public static void generarP02(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P02TO p02TO=new P02TO();
			p02TO.getId().setEjerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p02TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p02TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p02TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				p02TO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				p02TO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				p02TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				p02TO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				p02TO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				p02TO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				p02TO.getId().setItemid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("tipo")!=null){
				p02TO.getId().setActividadunidadacumtipo(parameters.get("tipo"));
				if(parameters.get("tipo").equals("P"))
					p02TO.getId().setActividadunidadacumid(1);
				else
					p02TO.getId().setActividadunidadacumid(2);
			}

			Collection<P02TO> p02tos=UtilSession.reporteServicio.transObtenerP02(p02TO);
//			System.out.println("getActividadunidadacumid: " + p02TO.getId().getActividadunidadacumid());
//			System.out.println("getActividadunidadacumtipo: " + p02TO.getId().getActividadunidadacumtipo());
//			System.out.println("getEjerciciofiscalid: " + p02TO.getId().getEjerciciofiscalid());
//			System.out.println("getInstitucionid: " + p02TO.getId().getInstitucionid());
//			System.out.println("getInstitucionentid: " + p02TO.getId().getInstitucionentid());
//			System.out.println("getUnidadid: " + p02TO.getId().getUnidadid());
//			System.out.println("p02tos: " + p02tos.size());
//			for(P02TO p02to2:p02tos){
//				System.out.println("item: " + p02to2.getItemnombre() + " monto: " + p02to2.getId().getMontoacumulado() + " mayo " + p02to2.getMayoc());
//			}
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P2"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,12)); 
			fila++;
			
			if(p02tos.size()>0){
				p02TO=(P02TO)p02tos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("P2- MATRIZ PAP - ITEM");
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,12)); 
				fila++;
				
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("OBJETIVO PLAN NACIONAL");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(1);
				cell.setCellValue("OBJETIVO MIDENA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(2);
				cell.setCellValue("OBJETIVO FF.AA.");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(3);
				cell.setCellValue("OBJETIVO FUERZAS");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(4);
				cell.setCellValue("INSTITUCIÓN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(5);
				cell.setCellValue("ENTIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(6);
				cell.setCellValue("UNIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(7);
				cell.setCellValue("PROGRAMA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(8);
				cell.setCellValue("PROYECTO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(9);
				cell.setCellValue("ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(10);
				cell.setCellValue("SUBACTIVDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(11);
				cell.setCellValue("TAREA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(12);
				cell.setCellValue("SUBTAREA");
				cell.setCellStyle(styles.get("titulo"));
	
				cell = row.createCell(13);
				cell.setCellValue("ITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(14);
				cell.setCellValue("DESCRIPCION ITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(15);
				cell.setCellValue("GEOGRÁFICO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(16);
				cell.setCellValue("FUENTE");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(17);
				cell.setCellValue("ORGANISMO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(18);
				cell.setCellValue("PRÉSTAMO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(19);
				cell.setCellValue("MONTO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(20);
				cell.setCellValue("ENE COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(21);
					cell.setCellValue("ENE DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(22);
				cell.setCellValue("FEB COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(23);
					cell.setCellValue("FEB DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(24);
				cell.setCellValue("MAR COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(25);
					cell.setCellValue("MAR DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(26);
				cell.setCellValue("ABR COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(27);
					cell.setCellValue("ABR DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(28);
				cell.setCellValue("MAY COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(29);
					cell.setCellValue("MAY DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(30);
				cell.setCellValue("JUN COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(31);
					cell.setCellValue("JUN DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(32);
				cell.setCellValue("JUL COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(33);
					cell.setCellValue("JUL DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(34);
				cell.setCellValue("AGO COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(35);
					cell.setCellValue("AGO DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(36);
				cell.setCellValue("SEP COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(37);
					cell.setCellValue("SEP DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(38);
				cell.setCellValue("OCT COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(39);
					cell.setCellValue("OCT DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(40);
				cell.setCellValue("NOV COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(41);
					cell.setCellValue("NOV DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(42);
				cell.setCellValue("DIC COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(43);
					cell.setCellValue("DIC DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				fila++;
				System.out.println("p02s: " + p02tos.size());
				for(P02TO p02:p02tos){
					row = sheet.createRow((short)fila);
					System.out.println("p02: " + p02);
					if(p02!=null){
						cell = row.createCell(0);
						cell.setCellValue(p02.getPlancodigo()+"-"+p02.getPlandescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(1);
						cell.setCellValue(p02.getEstrategicocodigo()+"-"+p02.getEstrategicodescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p02.getOperativocodigo()+"-"+p02.getOperativodescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(3);
						cell.setCellValue(p02.getObjetivocodigo()+"-"+p02.getFuerzadescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(4);
						cell.setCellValue(p02.getInstitucioincodigo() +" - " + p02.getInstitucionnombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(5);
						cell.setCellValue(p02.getEntidadcodigo() +" - " + p02.getEntidadnombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(6);
						cell.setCellValue(p02.getUnidadcodigo() + " - " + p02.getUnidadnombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(7);
						cell.setCellValue(p02.getProgramacodigo() + " - " + p02.getProgramadescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(8);
						cell.setCellValue(p02.getProyectocodigo() + " - " + p02.getNombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(9);
						cell.setCellValue(p02.getActividadcodigo() + " - " + p02.getActividaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(10);
						cell.setCellValue(p02.getSubactividadcodigo() + " - " + p02.getSubactividaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(11);
						cell.setCellValue(p02.getTareaunidadcodigo() + " - " + p02.getTareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(12);
						cell.setCellValue(p02.getSubtareaunidadcodigo() + " - " + p02.getSubtareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(13);
						cell.setCellValue(p02.getItemcodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(14);
						cell.setCellValue(p02.getItemnombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(15);
						cell.setCellValue(p02.getDivisiongeograficacodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(16);
						cell.setCellValue(p02.getFuentefinanciamientocodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(17);
						cell.setCellValue(p02.getOrganismocodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(18);
						cell.setCellValue(p02.getOrganismoprestamocodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(19);
	//					System.out.println("monto: " + p02.getId().getMontoacumulado());
						if(p02.getId().getMontoacumulado()!=null)
							cell.setCellValue(p02.getId().getMontoacumulado());
						else{
							cell.setCellValue(0.0);
							p02.getId().setMontoacumulado(0.0);
							}
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(20);
						if(p02.getEneroc()!=null && p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getEneroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(21);
							if(p02.getEnerod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getEnerod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(22);
						if(p02.getFebreroc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getFebreroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(23);
							if(p02.getFebrerod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getFebrerod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(24);
						if(p02.getMarzoc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getMarzoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(25);
							if(p02.getMarzod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getMarzod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(26);
						if(p02.getAbrilc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getAbrilc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(27);
							if(p02.getAbrild()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getAbrild());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(28);
						if(p02.getMayoc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getMayoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(29);
							if(p02.getMayod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getMayod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(30);
						if(p02.getJunioc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getJunioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(31);
							if(p02.getJuniod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getJuniod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(32);
						if(p02.getJulioc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getJulioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(33);
							if(p02.getJuliod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getJuliod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(34);
						if(p02.getAgostoc()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getAgostoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(35);
							if(p02.getAgostod()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getAgostod());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(36);
						if(p02.getSeptiembrec()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getSeptiembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(37);
							if(p02.getSeptiembred()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getSeptiembred());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(38);
						if(p02.getOctubrec()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getOctubrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(39);
							if(p02.getOctubred()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getOctubred());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(40);
						if(p02.getNoviembrec()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getNoviembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(41);
							if(p02.getNoviembred()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getNoviembred());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						cell = row.createCell(42);
						if(p02.getDiciembrec()!=null &&  p02.getId().getMontoacumulado()>0)
							cell.setCellValue(p02.getDiciembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						if(parameters.get("tipo").equals("A")){
							cell = row.createCell(43);
							if(p02.getDiciembred()!=null &&  p02.getId().getMontoacumulado()>0)
								cell.setCellValue(p02.getDiciembred());
							else
								cell.setCellValue(0.0);
							cell.setCellStyle(styles.get("contenidonumero"));
						}
						fila++;
					}
				}
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
			sheet.setColumnWidth(16, 256*15);
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
			sheet.setColumnWidth(29, 256*15);
			sheet.setColumnWidth(30, 256*15);
			sheet.setColumnWidth(31, 256*15);
			sheet.setColumnWidth(32, 256*15);
			sheet.setColumnWidth(33, 256*15);
			sheet.setColumnWidth(34, 256*15);
			sheet.setColumnWidth(35, 256*15);
			sheet.setColumnWidth(36, 256*15);
			sheet.setColumnWidth(37, 256*15);
			sheet.setColumnWidth(38, 256*15);
			sheet.setColumnWidth(39, 256*15);
			sheet.setColumnWidth(40, 256*15);
			sheet.setColumnWidth(41, 256*15);
			sheet.setColumnWidth(42, 256*15);
			sheet.setColumnWidth(43, 256*15);

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}
	
	public static void generarP04pac(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P04pacTO p04pacTO=new P04pacTO();
			p04pacTO.getId().setEjerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p04pacTO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p04pacTO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p04pacTO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				p04pacTO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				p04pacTO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				p04pacTO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				p04pacTO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				p04pacTO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				p04pacTO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				p04pacTO.getId().setItemunidadid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("fuente")!=null)
				p04pacTO.getId().setFuentefinanciamientoid(Long.valueOf(parameters.get("fuente")));
			if(parameters.get("tipo")!=null){
				p04pacTO.setSubitemunidadacumtipo(parameters.get("tipo"));
				if(p04pacTO.getSubitemunidadacumtipo().equals("A"))
					p04pacTO.setSubitemunidadacumid(2L);
				else
					p04pacTO.setSubitemunidadacumid(1L);

			}

			
			Collection<P04pacTO> p04pactos=UtilSession.reporteServicio.transObtenerP04pac(p04pacTO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P4pac"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,12)); 
			fila++;
			if(p04pactos.size()>0){
				p04pacTO=(P04pacTO)p04pactos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("P-4 MATRIZ RESUMEN PAP - METAS - PRESUPUESTO");
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,12)); 
				fila++;
				
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("AÑO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(1);
				cell.setCellValue("INSTITUCIÓN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(2);
				cell.setCellValue("ENTIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(3);
				cell.setCellValue("UNIDAD EJECUTORAS");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(4);
				cell.setCellValue("PROGRAMA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(5);
				cell.setCellValue("SUBPROGRAMA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(6);
				cell.setCellValue("PROYECTO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(7);
				cell.setCellValue("ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(8);
				cell.setCellValue("OBRA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(9);
				cell.setCellValue("GEOGRÁFICO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(10);
				cell.setCellValue("REGLÓN/ITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(11);
				cell.setCellValue("RENGLÓN AUXILIAR");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(12);
				cell.setCellValue("FUENTE");
				cell.setCellStyle(styles.get("titulo"));
	
				cell = row.createCell(13);
				cell.setCellValue("ORGANISMO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(14);
				cell.setCellValue("PRÉSTAMO CORRELATIVO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(15);
				cell.setCellValue("CÓDIGO CPC A NIVEL 8 - CÓDIGO SUBITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(16);
				cell.setCellValue("TIPO COMPRA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(17);
				cell.setCellValue("DETALLE DEL PRODUCTO - NOMBRE SUBITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(18);
				cell.setCellValue("CANTIDAD ANUAL");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(19);
				cell.setCellValue("UNIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(20);
				cell.setCellValue("COSTO AJUSTADO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(21);
				cell.setCellValue("CUATRIMESTRE 1");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(22);
				cell.setCellValue("CUATRIMESTRE 2");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(23);
				cell.setCellValue("CUATRIMESTRE 3");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(24);
				cell.setCellValue("TIPO PRODUCTO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(25);
				cell.setCellValue("CATÁLOGO ELECTRÓNICO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(26);
				cell.setCellValue("PROCEDIMIENTO SUGERIDO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(27);
				cell.setCellValue("FONDO BID");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(28);
				cell.setCellValue("NÚMERO PRÉSTAMO BID");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(29);
				cell.setCellValue("NÚMERO PROYECTO BID");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(30);
				cell.setCellValue("TIPO DE RÉGIMEN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(31);
				cell.setCellValue("TIPO PRESUPUESTO");
				cell.setCellStyle(styles.get("titulo"));

				fila++;
				for(P04pacTO p04pac:p04pactos){
					row = sheet.createRow((short)fila);
					cell = row.createCell(0);
					cell.setCellValue(p04pac.getEjerciciofiscalanio());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(1);
					cell.setCellValue(p04pac.getInstitucioncodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(2);
					cell.setCellValue(p04pac.getInstitucionentcodigo() +" - " + p04pac.getInstitucionentnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(3);
					cell.setCellValue(p04pac.getUnidadcodigopresup() + " - " + p04pac.getUnidadnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(4);
					cell.setCellValue(p04pac.getProgramacodigo() +" - " + p04pac.getProgramadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(5);
					cell.setCellValue(p04pac.getSubprogramacodigo() +" - " + p04pac.getSubprogramadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(6);
					cell.setCellValue(p04pac.getProyectocodigo() + " - " + p04pac.getProyectonombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(7);
					cell.setCellValue(p04pac.getActividadcodigo() + " - " + p04pac.getActividaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(8);
					cell.setCellValue(p04pac.getObracodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(9);
					cell.setCellValue(p04pac.getDivisiongeograficacodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(10);
					cell.setCellValue(p04pac.getItemcodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(11);
					cell.setCellValue("0000000");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(12);
					cell.setCellValue(p04pac.getFuentefinanciamientocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(13);
					cell.setCellValue(p04pac.getOrganismoprestamocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(14);
					cell.setCellValue(p04pac.getOrganismocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(15);
					cell.setCellValue(p04pac.getSubitemcodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(16);
					if(p04pac.getItemtipobien()!=null){
						if(p04pac.getItemtipobien().equals("T"))
							cell.setCellValue("Sin Tipo");
						else if(p04pac.getItemtipobien().equals("B"))
							cell.setCellValue("Bien");
						else if(p04pac.getItemtipobien().equals("S"))
							cell.setCellValue("Servicio");
						else if(p04pac.getItemtipobien().equals("O"))
							cell.setCellValue("Obra");
						else if(p04pac.getItemtipobien().equals("C"))
							cell.setCellValue("Consultora");
					}
					else
						cell.setCellValue("Sin Tipo");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(17);
					cell.setCellValue(p04pac.getSubitemnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(18);
					cell.setCellValue("1");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(19);
					cell.setCellValue("UNIDAD");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(20);
					cell.setCellValue(p04pac.getSubitemunidadacumvalor());
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(21);
					if(p04pac.getPrimer()!=null)
						cell.setCellValue(p04pac.getPrimer());
					else
						cell.setCellValue(0.00);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(22);
					if(p04pac.getSegundo()!=null)
						cell.setCellValue(p04pac.getSegundo());
					else
						cell.setCellValue(0.00);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(23);
					if(p04pac.getTercer()!=null)
						cell.setCellValue(p04pac.getTercer());
					else
						cell.setCellValue(0.00);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(24);
					cell.setCellValue(p04pac.getTipoproductonombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(25);
					cell.setCellValue("NO");
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(26);
					cell.setCellValue(p04pac.getProcedimientonombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(27);
					cell.setCellValue("N/A");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(28);
					cell.setCellValue("N/A");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(29);
					cell.setCellValue("N/A");
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(30);
					cell.setCellValue(p04pac.getTiporegimennombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(31);
					if(p04pac.getProyectocodigo().equals("000"))
						cell.setCellValue("PERMANENTE");
					else
						cell.setCellValue("NO PERMANENTE");
					cell.setCellStyle(styles.get("contenido"));
					fila++;
				}
			}
			row = sheet.createRow((short)fila);
			sheet.setColumnWidth(0, 256*15);
			sheet.setColumnWidth(1, 256*15);
			sheet.setColumnWidth(2, 256*40);
			sheet.setColumnWidth(3, 256*15);
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
			sheet.setColumnWidth(16, 256*15);
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
			sheet.setColumnWidth(29, 256*15);
			sheet.setColumnWidth(30, 256*15);
			sheet.setColumnWidth(31, 256*15);

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static void generarP03(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P03TO p03TO=new P03TO();
			p03TO.getId().setEjerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p03TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p03TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p03TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				p03TO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				p03TO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				p03TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				p03TO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				p03TO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				p03TO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("itemid")!=null)
				p03TO.getId().setItemid(Long.valueOf(parameters.get("itemid")));
			if(parameters.get("subitemid")!=null)
				p03TO.getId().setSubitemid(Long.valueOf(parameters.get("subitemid")));
			if(parameters.get("tipo")!=null)
				p03TO.setSubitemunidadacumtipo(parameters.get("tipo"));

			
			Collection<P03TO> p03tos=	UtilSession.reporteServicio.transObtenerP03(p03TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P3"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,12)); 
			fila++;
			
			if(p03tos.size()>0){
				p03TO=(P03TO)p03tos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("P-3 MATRIZ PAP - SUBITEM");
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,12)); 
				fila++;
				
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("OBJETIVO PLAN NACIONAL");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(1);
				cell.setCellValue("OBJETIVO MIDENA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(2);
				cell.setCellValue("OBJETIVO FF.AA.");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(3);
				cell.setCellValue("OBJETIVO FUERZAS");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(4);
				cell.setCellValue("INSTITUCIÓN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(5);
				cell.setCellValue("ENTIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(6);
				cell.setCellValue("UNIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(7);
				cell.setCellValue("PROGRAMA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(8);
				cell.setCellValue("PROYECTO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(9);
				cell.setCellValue("ACTIVIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(10);
				cell.setCellValue("SUBACTIVDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(11);
				cell.setCellValue("TAREA");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(12);
				cell.setCellValue("SUBTAREA");
				cell.setCellStyle(styles.get("titulo"));
	
				cell = row.createCell(13);
				cell.setCellValue("ITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(14);
				cell.setCellValue("DESCRIPCION ITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(15);
				cell.setCellValue("GEOGRÁFICO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(16);
				cell.setCellValue("FUENTE");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(17);
				cell.setCellValue("ORGANISMO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(18);
				cell.setCellValue("PRÉSTAMO");
				cell.setCellStyle(styles.get("titulo"));

				cell = row.createCell(19);
				cell.setCellValue("SUBITEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(20);
				cell.setCellValue("DENOMINACION SUBÍTEM");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(21);
				cell.setCellValue("INICIAL");
				cell.setCellStyle(styles.get("titulo"));

				cell = row.createCell(22);
				cell.setCellValue("ENE COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(23);
					cell.setCellValue("ENE DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(24);
				cell.setCellValue("FEB COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(25);
					cell.setCellValue("FEB DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(26);
				cell.setCellValue("MAR COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(27);
					cell.setCellValue("MAR DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(28);
				cell.setCellValue("ABR COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(29);
					cell.setCellValue("ABR DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(30);
				cell.setCellValue("MAY COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(31);
					cell.setCellValue("MAY DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(32);
				cell.setCellValue("JUN COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(33);
					cell.setCellValue("JUN DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(34);
				cell.setCellValue("JUL COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(35);
					cell.setCellValue("JUL DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(36);
				cell.setCellValue("AGO COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(37);
					cell.setCellValue("AGO DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(38);
				cell.setCellValue("SEP COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(39);
					cell.setCellValue("SEP DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(40);
				cell.setCellValue("OCT COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(41);
					cell.setCellValue("OCT DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(42);
				cell.setCellValue("NOV COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(43);
					cell.setCellValue("NOV DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				cell = row.createCell(44);
				cell.setCellValue("DIC COM.");
				cell.setCellStyle(styles.get("titulo"));
				if(parameters.get("tipo").equals("A")){
					cell = row.createCell(45);
					cell.setCellValue("DIC DEV.");
					cell.setCellStyle(styles.get("titulo"));
				}
				fila++;
				System.out.println("filas: " + p03tos.size());
				for(P03TO p03:p03tos){
					row = sheet.createRow((short)fila);
					cell = row.createCell(0);
					cell.setCellValue(p03.getPlancodigo()+"-"+p03.getPlandescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(1);
					cell.setCellValue(p03.getEstrategicocodigo()+"-"+p03.getEstrategicodescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(2);
					cell.setCellValue(p03.getOperativocodigo()+"-"+p03.getOperativodescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(3);
					cell.setCellValue(p03.getObjetivocodigo()+"-"+p03.getFuerzadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(4);
					cell.setCellValue(p03.getInstitucioincodigo() +" - " + p03.getInstitucionnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(5);
					cell.setCellValue(p03.getEntidadcodigo() +" - " + p03.getEntidadnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(6);
					cell.setCellValue(p03.getUnidadcodigo() + " - " + p03.getUnidadnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(7);
					cell.setCellValue(p03.getProgramacodigo() + " - " + p03.getProgramadescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(8);
					cell.setCellValue(p03.getProyectocodigo() + " - " + p03.getNombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(9);
					cell.setCellValue(p03.getActividadcodigo() + " - " + p03.getActividaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(10);
					cell.setCellValue(p03.getSubactividadcodigo() + " - " + p03.getSubactividaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(11);
					cell.setCellValue(p03.getTareaunidadcodigo() + " - " + p03.getTareaunidaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(12);
					cell.setCellValue(p03.getSubtareaunidadcodigo() + " - " + p03.getSubtareaunidaddescripcion());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(13);
					cell.setCellValue(p03.getItemcodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(14);
					cell.setCellValue(p03.getItemnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(15);
					cell.setCellValue(p03.getDivisiongeograficacodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(16);
					cell.setCellValue(p03.getFuentefinanciamientocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(17);
					cell.setCellValue(p03.getOrganismocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(18);
					cell.setCellValue(p03.getOrganismoprestamocodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(19);
					cell.setCellValue(p03.getSubitemcodigo());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(20);
					cell.setCellValue(p03.getSubitemnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(21);
					cell.setCellValue(p03.getPresupuesto());
					//cell.setCellValue(p03.getActividadunidadpresupajust());
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(22);
					if(p03.getEneroc()!=null)
						cell.setCellValue(p03.getEneroc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(23);
						if(p03.getEnerod()!=null)
						cell.setCellValue(p03.getEnerod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(24);
					if(p03.getFebreroc()!=null)
					cell.setCellValue(p03.getFebreroc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(25);
						if(p03.getFebrerod()!=null)
						cell.setCellValue(p03.getFebrerod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(26);
					if(p03.getMarzoc()!=null)
					cell.setCellValue(p03.getMarzoc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(27);
						if(p03.getMarzod()!=null)
						cell.setCellValue(p03.getMarzod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(28);
					if(p03.getAbrilc()!=null)
					cell.setCellValue(p03.getAbrilc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(29);
						if(p03.getAbrild()!=null)
						cell.setCellValue(p03.getAbrild());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(30);
					if(p03.getMayoc()!=null)
					cell.setCellValue(p03.getMayoc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(31);
						if(p03.getMayod()!=null)
						cell.setCellValue(p03.getMayod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(32);
					if(p03.getJunioc()!=null)
					cell.setCellValue(p03.getJunioc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(33);
						if(p03.getJuniod()!=null)
						cell.setCellValue(p03.getJuniod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(34);
					if(p03.getJulioc()!=null)
					cell.setCellValue(p03.getJulioc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(35);
						if(p03.getJuliod()!=null)
						cell.setCellValue(p03.getJuliod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(36);
					if(p03.getAgostoc()!=null)
					cell.setCellValue(p03.getAgostoc());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					cell = row.createCell(37);
					if(parameters.get("tipo").equals("A")){
						if(p03.getAgostod()!=null)
						cell.setCellValue(p03.getAgostod());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(38);
					if(p03.getSeptiembrec()!=null)
					cell.setCellValue(p03.getSeptiembrec());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(39);
						if(p03.getSeptiembred()!=null)
						cell.setCellValue(p03.getSeptiembred());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(40);
					if(p03.getOctubrec()!=null)
					cell.setCellValue(p03.getOctubrec());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(41);
						if(p03.getOctubred()!=null)
						cell.setCellValue(p03.getOctubred());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(42);
					if(p03.getNoviembrec()!=null)
					cell.setCellValue(p03.getNoviembrec());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(43);
						if(p03.getNoviembred()!=null)
						cell.setCellValue(p03.getNoviembred());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					cell = row.createCell(44);
					if(p03.getDiciembrec()!=null)
					cell.setCellValue(p03.getDiciembrec());
					else
						cell.setCellValue(0.0);
					cell.setCellStyle(styles.get("contenidonumero"));
					if(parameters.get("tipo").equals("A")){
						cell = row.createCell(45);
						if(p03.getDiciembred()!=null)
							cell.setCellValue(p03.getDiciembred());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
					}
					fila++;
				}
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
			sheet.setColumnWidth(16, 256*15);
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
			sheet.setColumnWidth(29, 256*15);
			sheet.setColumnWidth(30, 256*15);
			sheet.setColumnWidth(31, 256*15);
			sheet.setColumnWidth(32, 256*15);
			sheet.setColumnWidth(33, 256*15);
			sheet.setColumnWidth(34, 256*15);
			sheet.setColumnWidth(35, 256*15);
			sheet.setColumnWidth(36, 256*15);
			sheet.setColumnWidth(37, 256*15);
			sheet.setColumnWidth(38, 256*15);
			sheet.setColumnWidth(39, 256*15);
			sheet.setColumnWidth(40, 256*15);
			sheet.setColumnWidth(41, 256*15);
			sheet.setColumnWidth(42, 256*15);
			sheet.setColumnWidth(43, 256*15);

			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static void generarP05(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P05TO p05TO=new P05TO();
			p05TO.getId().setEjerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p05TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p05TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p05TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));

			
			Collection<P05TO> p05tos=UtilSession.reporteServicio.transObtenerP05(p05TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P5"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,12)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,12)); 
			fila++;
			
			if(p05tos.size()>0){
				p05TO=(P05TO)p05tos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("P-5 MATRIZ RESUMEN EOD - UE" + p05TO.getEjerciciofiscalanio());
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,12)); 
				fila++;
				
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("AÑO");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(1);
				cell.setCellValue("INSTITUCIÓN");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(2);
				cell.setCellValue("ENTIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(3);
				cell.setCellValue("UNIDAD");
				cell.setCellStyle(styles.get("titulo"));
				cell = row.createCell(4);
				cell.setCellValue("PRESUPUESTO");
				cell.setCellStyle(styles.get("titulo"));

				fila++;
				for(P05TO p05:p05tos){
					row = sheet.createRow((short)fila);
					cell = row.createCell(0);
					cell.setCellValue(p05.getEjerciciofiscalanio());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(1);
					cell.setCellValue(p05.getInstitucioncodigo() + " - "+p05.getInstitucionnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(2);
					cell.setCellValue(p05.getInstitucionentcodigo() + " - " + p05.getInstitucionentnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(3);
					cell.setCellValue(p05.getUnidadcodigopresup() + " - " + p05.getUnidadnombre());
					cell.setCellStyle(styles.get("contenido"));
					cell = row.createCell(4);
					if(parameters.get("tipo").equals("P"))
						cell.setCellValue(p05.getSumaplanificada());
					else	
						cell.setCellValue(p05.getSumaajustada());
					cell.setCellStyle(styles.get("contenidonumero"));
					fila++;
				}
			}
			row = sheet.createRow((short)fila);
			sheet.setColumnWidth(0, 256*10);
			sheet.setColumnWidth(1, 256*40);
			sheet.setColumnWidth(2, 256*40);
			sheet.setColumnWidth(3, 256*40);
			sheet.setColumnWidth(4, 256*15);
			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}
	
	public static void generarP04(HttpServletRequest request, HttpServletResponse response,Map<String, String> parameters) throws MyException {
		try{
			P04TO p04TO=new P04TO();
			p04TO.getId().setEjerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("institucionid")!=null)
				p04TO.getId().setInstitucionid(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("institucionentid")!=null)
				p04TO.getId().setInstitucionentid(Long.valueOf(parameters.get("institucionentid")));
			if(parameters.get("unidadid")!=null)
				p04TO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("programaid")!=null)
				p04TO.getId().setProgramaid(Long.valueOf(parameters.get("programaid")));
			if(parameters.get("proyectoid")!=null)
				p04TO.getId().setProyectoid(Long.valueOf(parameters.get("proyectoid")));
			if(parameters.get("actividadid")!=null)
				p04TO.getId().setActividadid(Long.valueOf(parameters.get("actividadid")));
			if(parameters.get("subactividadid")!=null)
				p04TO.getId().setSubactividadid(Long.valueOf(parameters.get("subactividadid")));
			if(parameters.get("tareaunidadid")!=null)
				p04TO.getId().setTareaunidadid(Long.valueOf(parameters.get("tareaunidadid")));
			if(parameters.get("subtareaunidadid")!=null)
				p04TO.getId().setSubtareaunidadid(Long.valueOf(parameters.get("subtareaunidadid")));
			if(parameters.get("tipo")!=null)
				p04TO.setSubtareaunidadacumtipo(parameters.get("tipo"));

			Collection<P04TO> p04tos=UtilSession.reporteServicio.transObtenerP04(p04TO);
			response.setContentType("application/vnd.ms-excel");
			Calendar fecha = new GregorianCalendar();
			fecha.setTime(new Date());
			int mes=fecha.get(Calendar.MONTH)+1;
			String nombrearchivo="P4"+fecha.get(Calendar.YEAR)+fecha.get(Calendar.DATE)+mes;
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
			cell.setCellValue("COMANDO CONJUNTO DE LAS FF.AA.");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,19)); 
			fila++;
			row = sheet.createRow((short)fila);
			cell = row.createCell(0);
			cell.setCellValue("ESTADO MAYOR INSTITUCIONAL");
			cell.setCellStyle(styles.get("treporteTitulo"));
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,19)); 
			fila++;
			if(p04tos.size()>0){
				P04TO p04to2=(P04TO)p04tos.iterator().next();
				row = sheet.createRow((short)fila);
				cell = row.createCell(0);
				cell.setCellValue("MATRIZ RESUMEN PROGRAMACIÓN ANUAL DE LA PLANIFICACIÓN  - " + p04to2.getEjerciciofiscalanio());
				cell.setCellStyle(styles.get("treporteTitulo"));
				sheet.addMergedRegion(new CellRangeAddress(2,2,0,19)); 
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue("Reporte");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(12);
				cell.setCellValue("P-04");
				cell.setCellStyle(styles.get("tituloizquierda"));
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue("PAAP Año: ");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(12);
				cell.setCellValue(p04to2.getEjerciciofiscalanio());
				cell.setCellStyle(styles.get("tituloizquierda"));
				fila++;

				row = sheet.createRow((short)fila);
				cell = row.createCell(11);
				cell.setCellValue(" Fecha:  ");
				cell.setCellStyle(styles.get("tituloizquierda"));
				cell = row.createCell(12);
				cell.setCellValue(UtilGeneral.parseDateToString(new Date()));
				cell.setCellStyle(styles.get("tituloizquierda"));
				fila++;
				boolean cabecera=true;
				for(P04TO p04to3:p04tos){
					if(p04to2.getId().getActividadid().longValue()==p04to3.getId().getActividadid().longValue() &&
							p04to2.getId().getInstitucionentid().longValue()==p04to3.getId().getInstitucionentid().longValue() &&
							p04to2.getId().getObjesid().longValue()==p04to3.getId().getObjesid().longValue() &&
							p04to2.getId().getObjfuid().longValue()==p04to3.getId().getObjfuid().longValue() &&
							p04to2.getId().getObjopid().longValue()==p04to3.getId().getObjopid().longValue() &&
							p04to2.getId().getObjpnid().longValue()==p04to3.getId().getObjpnid().longValue() &&
							p04to2.getId().getUnidadid().longValue()==p04to3.getId().getUnidadid().longValue() &&
							p04to2.getId().getProgramaid().longValue()==p04to3.getId().getProgramaid().longValue() &&
							p04to2.getId().getProyectoid().longValue()==p04to3.getId().getProyectoid().longValue()){
						if(cabecera){
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("EJERCICIO FISCAL:  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getEjerciciofiscalanio());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("ENTIDAD OPERATIVA DESCONCENTRADA (EOD):  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getEntidadcodigo());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getEntidadnombre());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("UNIDAD EJECUTORA (UE)  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getUnidadcodigo());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getUnidadnombre());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("RESPONSABLE DE LA ACTIVIDAD:  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getGrado());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getResponsable());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("NOMBRE DEL PROGRAMA:  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getProgramacodigo());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getProgramadescripcion());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("NOMBRE DEL PROYECTO:  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getProyectocodigo());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getProyectonombre());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("NOMBRE DE LA ACTIVIDAD:  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getActividadcodigo());
							cell.setCellStyle(styles.get("contenido"));
							cell = row.createCell(2);
							cell.setCellValue(p04to3.getActividaddescripcion());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("OBJETIVO ESTRATEGICO INSTITUCIONAL (MIDENA)  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getEstrategicodescripcion());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("OBJETIVO OPERATIVO DE FF.AA.  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getOperativodescripcion());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("OBJETIVO FUERZAS  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getFuerzadescripcion());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("OBJETIVO GENERAL DE LA ACTIVIDAD  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getActividadunidadobjetivogeneral());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("OBJETIVO/S ESPECIFICO/S DE LA ACTIVIDAD  ");
							cell.setCellStyle(styles.get("tituloizquierda"));
							cell = row.createCell(1);
							cell.setCellValue(p04to3.getActividadunidadobjetivoespec());
							cell.setCellStyle(styles.get("contenido"));
							fila++;
							row = sheet.createRow((short)fila);
							cell = row.createCell(0);
							cell.setCellValue("SUBACTIVIDAD");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(1);
							cell.setCellValue("TAREA");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(2);
							cell.setCellValue("SUBTAREA");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(3);
							cell.setCellValue("DESCRIPCIÓN DE LA META");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(4);
							cell.setCellValue("DETALLE META  MENSUALIZADA");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(5);
							cell.setCellValue("UNIDAD MEDIDA");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(6);
							cell.setCellValue("TOTAL META");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(7);
							cell.setCellValue("ENE");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(8);
							cell.setCellValue("FEB");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(9);
							cell.setCellValue("MAR");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(10);
							cell.setCellValue("ABR");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(11);
							cell.setCellValue("MAY");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(12);
							cell.setCellValue("JUN");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(13);
							cell.setCellValue("JUL");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(14);
							cell.setCellValue("AGO");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(15);
							cell.setCellValue("SEP");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(16);
							cell.setCellValue("OCT");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(17);
							cell.setCellValue("NOV");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(18);
							cell.setCellValue("DIC");
							cell.setCellStyle(styles.get("titulo"));
							cell = row.createCell(19);
							cell.setCellValue("PRESUPUESTO");
							cell.setCellStyle(styles.get("titulo"));
							fila++;
							cabecera=false;
						}					
						
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue(p04to3.getSubactividadcodigo() +" - " + p04to3.getSubactividaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(1);
						cell.setCellValue(p04to3.getTareaunidadcodigo() +" - " + p04to3.getTareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to3.getSubtareaunidadcodigo() + " - " + p04to3.getSubtareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(3);
						cell.setCellValue(p04to3.getSubtareaunidadacummetadesc());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(4);
						cell.setCellValue(p04to3.getMetamensualizada());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(5);
						cell.setCellValue(p04to3.getUnidadmedidanombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(6);
						cell.setCellValue(p04to3.getSubtareaunidadacummetavalor());
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(7);
						if(p04to3.getEneroc()!=null)
							cell.setCellValue(p04to3.getEneroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(8);
						if(p04to3.getFebreroc()!=null)
							cell.setCellValue(p04to3.getFebreroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(9);
						if(p04to3.getMarzoc()!=null)
							cell.setCellValue(p04to3.getMarzoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(10);
						if(p04to3.getAbrilc()!=null)
							cell.setCellValue(p04to3.getAbrilc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(11);
						if(p04to3.getMayoc()!=null)
							cell.setCellValue(p04to3.getMayoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(12);
						if(p04to3.getJunioc()!=null)
							cell.setCellValue(p04to3.getJunioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(13);
						if(p04to3.getJulioc()!=null)
							cell.setCellValue(p04to3.getJulioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(14);
						if(p04to3.getAgostoc()!=null)
							cell.setCellValue(p04to3.getAgostoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(15);
						if(p04to3.getSeptiembrec()!=null)
							cell.setCellValue(p04to3.getSeptiembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(16);
						if(p04to3.getOctubrec()!=null)
							cell.setCellValue(p04to3.getOctubrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(17);
						if(p04to3.getNoviembrec()!=null)
							cell.setCellValue(p04to3.getNoviembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(18);
						if(p04to3.getDiciembrec()!=null)
							cell.setCellValue(p04to3.getDiciembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(19);
						if(p04to3.getPresupuesto()!=null)
							cell.setCellValue(p04to3.getPresupuesto());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
	
						fila++;
					}
					else{
						p04to2=(P04TO)p04to3;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("EJERCICIO FISCAL:  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getEjerciciofiscalanio());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("ENTIDAD OPERATIVA DESCONCENTRADA (EOD):  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getEntidadcodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to2.getEntidadnombre());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("UNIDAD EJECUTORA (UE)  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getUnidadcodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to2.getUnidadnombre());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("RESPONSABLE DE LA ACTIVIDAD:  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getResponsable());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to3.getGrado());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("NOMBRE DEL PROGRAMA:  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getProgramacodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to2.getProgramadescripcion());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("NOMBRE DEL PROYECTO:  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getProyectocodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to2.getProyectonombre());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("NOMBRE DE LA ACTIVIDAD:  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getActividadcodigo());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to2.getActividaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("OBJETIVO ESTRATEGICO INSTITUCIONAL (MIDENA)  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getEstrategicodescripcion());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("OBJETIVO OPERATIVO DE FF.AA.  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getOperativodescripcion());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("OBJETIVO FUERZAS  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getFuerzadescripcion());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("OBJETIVO GENERAL DE LA ACTIVIDAD  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getActividadunidadobjetivogeneral());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("OBJETIVO/S ESPECIFICO/S DE LA ACTIVIDAD  ");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue(p04to2.getActividadunidadobjetivoespec());
						cell.setCellStyle(styles.get("contenido"));
						fila++;
						cabecera=false;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue("SUBACTIVIDAD");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(1);
						cell.setCellValue("TAREA");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(2);
						cell.setCellValue("SUBTAREA");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(3);
						cell.setCellValue("DESCRIPCIÓN DE LA META");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(4);
						cell.setCellValue("DETALLE META  MENSUALIZADA");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(5);
						cell.setCellValue("UNIDAD MEDIDA");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(6);
						cell.setCellValue("TOTAL META");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(7);
						cell.setCellValue("ENE");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(8);
						cell.setCellValue("FEB");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(9);
						cell.setCellValue("MAR");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(10);
						cell.setCellValue("ABR");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(11);
						cell.setCellValue("MAY");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(12);
						cell.setCellValue("JUN");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(13);
						cell.setCellValue("JUL");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(14);
						cell.setCellValue("AGO");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(15);
						cell.setCellValue("SEP");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(16);
						cell.setCellValue("OCT");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(17);
						cell.setCellValue("NOV");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(18);
						cell.setCellValue("DIC");
						cell.setCellStyle(styles.get("titulo"));
						cell = row.createCell(19);
						cell.setCellValue("PRESUPUESTO");
						cell.setCellStyle(styles.get("titulo"));
						fila++;
						row = sheet.createRow((short)fila);
						cell = row.createCell(0);
						cell.setCellValue(p04to3.getSubactividadcodigo() +" - " + p04to3.getSubactividaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(1);
						cell.setCellValue(p04to3.getTareaunidadcodigo() +" - " + p04to3.getTareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(2);
						cell.setCellValue(p04to3.getSubtareaunidadcodigo() + " - " + p04to3.getSubtareaunidaddescripcion());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(3);
						cell.setCellValue(p04to3.getSubtareaunidadacummetadesc());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(4);
						cell.setCellValue(p04to3.getMetamensualizada());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(5);
						cell.setCellValue(p04to3.getUnidadmedidanombre());
						cell.setCellStyle(styles.get("contenido"));
						cell = row.createCell(6);
						cell.setCellValue(p04to3.getSubtareaunidadacummetavalor());
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(7);
						if(p04to3.getEneroc()!=null)
							cell.setCellValue(p04to3.getEneroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(8);
						if(p04to3.getFebreroc()!=null)
							cell.setCellValue(p04to3.getFebreroc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(9);
						if(p04to3.getMarzoc()!=null)
							cell.setCellValue(p04to3.getMarzoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(10);
						if(p04to3.getAbrilc()!=null)
							cell.setCellValue(p04to3.getAbrilc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(11);
						if(p04to3.getMayoc()!=null)
							cell.setCellValue(p04to3.getMayoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(12);
						if(p04to3.getJunioc()!=null)
							cell.setCellValue(p04to3.getJunioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(13);
						if(p04to3.getJulioc()!=null)
							cell.setCellValue(p04to3.getJulioc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(14);
						if(p04to3.getAgostoc()!=null)
							cell.setCellValue(p04to3.getAgostoc());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(15);
						if(p04to3.getSeptiembrec()!=null)
							cell.setCellValue(p04to3.getSeptiembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(16);
						if(p04to3.getOctubrec()!=null)
							cell.setCellValue(p04to3.getOctubrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(17);
						if(p04to3.getNoviembrec()!=null)
							cell.setCellValue(p04to3.getNoviembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(18);
						if(p04to3.getDiciembrec()!=null)
							cell.setCellValue(p04to3.getDiciembrec());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
						cell = row.createCell(19);
						if(p04to3.getPresupuesto()!=null)
							cell.setCellValue(p04to3.getPresupuesto());
						else
							cell.setCellValue(0.0);
						cell.setCellStyle(styles.get("contenidonumero"));
	
						fila++;
						
					}
					fila++;
				}
			}
			row = sheet.createRow((short)fila);
			sheet.setColumnWidth(0, 256*40);
			sheet.setColumnWidth(1, 256*40);
			sheet.setColumnWidth(2, 256*40);
			sheet.setColumnWidth(3, 256*40);
			sheet.setColumnWidth(4, 256*40);
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
			sheet.setColumnWidth(16, 256*15);
			sheet.setColumnWidth(17, 256*15);
			sheet.setColumnWidth(18, 256*15);
			sheet.setColumnWidth(19, 256*15);
			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			throw new MyException(e);
		}
	}


}
