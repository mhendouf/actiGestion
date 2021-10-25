package org.sid.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.sid.entity.Acti;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportActiPdf {
	public static ByteArrayInputStream actiPDFreport(List<Acti> actis) {
		Document document = new Document ( );
		ByteArrayOutputStream out = new ByteArrayOutputStream ( );
		try {
			Collections.sort ( actis );
			Image image = Image.getInstance ( "imageacti.png" );
			image.scaleToFit ( 200 , 200 );
			image.setAlignment ( Element.ALIGN_CENTER );
			// image.setAbsolutePosition ( 500 , 100 );
			PdfWriter.getInstance ( document , out );
			document.open ( );
			com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 20 ,
					BaseColor.BLACK );
			Paragraph para = new Paragraph ( "ACTIVITÉS" , font );
			para.setAlignment ( Element.ALIGN_CENTER );
			document.add ( para );
			Date du = new Date ( );
			Date au = new Date ( );
			int i = 0;
			for (Acti s : actis) {
				if (i == 0) {
					du = s.getDate_acti ( );
					au = s.getDate_acti ( );
					i = 1;
				} else {
					if (s.getDate_acti ( ).after ( au )) {
						au = s.getDate_acti ( );
					}
					if (s.getDate_acti ( ).before ( du )) {
						du = s.getDate_acti ( );
					}
				}
			}
			i = 0;
			String pattern = "EEEEE dd/MM/yyyy";
			String patternHeure = "HH'h'mm";
			String patternDu = "dd-MMMM";
			String patternAu = "dd-MMMM-yyyy";
			SimpleDateFormat formaterEnglish = new SimpleDateFormat ( "h:mm a" , Locale.FRENCH );
			SimpleDateFormat simpleDateFormatHeure = new SimpleDateFormat ( patternHeure , Locale.FRENCH );
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( pattern , Locale.FRENCH );
			SimpleDateFormat simpleDateFormatDu = new SimpleDateFormat ( patternDu , Locale.FRENCH );

			SimpleDateFormat simpleDateFormatAu = new SimpleDateFormat ( patternAu , Locale.FRENCH );
			String dateDu = simpleDateFormatDu.format ( du );
			String dateAu = simpleDateFormatAu.format ( au );
			com.itextpdf.text.Font fontdate = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 16 ,
					BaseColor.BLACK );
			Paragraph paraDate = new Paragraph ( " Du " + dateDu + " Au " + dateAu , fontdate );
			paraDate.setAlignment ( Element.ALIGN_CENTER );
			document.add ( paraDate );
			document.add ( image );
			com.itextpdf.text.Font fontIntro = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 16 ,
					BaseColor.BLACK );
			Paragraph paraIntro = new Paragraph ( "Les inscriptions se font auprés des éducat(rices)eurs " ,
					fontIntro );
			Paragraph paraIntroEn = new Paragraph ( "Registrations are made with educators" , fontIntro );
			paraIntro.setAlignment ( Element.ALIGN_CENTER );
			paraIntroEn.setAlignment ( Element.ALIGN_CENTER );
			document.add ( paraIntro );
			document.add ( paraIntroEn );
			document.add ( Chunk.NEWLINE );
			for (Acti s : actis) {

				s.getDate_acti ( ).setHours ( s.getDate_acti ( ).getHours ( ) + 2 );
				s.getDate_acti_fin ( ).setHours ( s.getDate_acti_fin ( ).getHours ( ) + 2 );
				String date = simpleDateFormat.format ( s.getDate_acti ( ) );
				String heure = simpleDateFormatHeure.format ( s.getDate_acti ( ) );
				String heureEn = formaterEnglish.format ( s.getDate_acti ( ) );
				String heureFinEn = formaterEnglish.format ( s.getDate_acti_fin ( ) );
				String heureFin = simpleDateFormatHeure.format ( s.getDate_acti_fin ( ) );
				com.itextpdf.text.Font fonttitre = FontFactory.getFont ( FontFactory.COURIER_BOLD , 16 ,
						BaseColor.ORANGE );
				com.itextpdf.text.Font fonttitreP = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 16 ,
						Font.UNDERLINE );
				fonttitreP.setColor ( BaseColor.BLACK );
				Paragraph paraDat = new Paragraph ( date.toUpperCase ( ) , fonttitreP );

				paraDat.setAlignment ( Element.ALIGN_CENTER );
				Paragraph paraTitreEn = new Paragraph ( heureEn + "-" + heureFinEn + " : " + s.getTitle ( ) ,
						fontIntro );
				Paragraph paraTitre = new Paragraph ( heure + "-" + heureFin + " : " + s.getTitre ( ) , fontIntro );
				paraTitreEn.setAlignment ( Element.ALIGN_CENTER );
				paraTitre.setAlignment ( Element.ALIGN_CENTER );
				document.add ( paraDat );
				document.add ( paraTitre );
				document.add ( paraTitreEn );
				document.add ( Chunk.NEWLINE );
			}
			document.close ( );
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ByteArrayInputStream ( out.toByteArray ( ) );
	}
}
