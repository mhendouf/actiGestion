package org.sid.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sid.entity.Acti;
import org.sid.entity.Benevole;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ActiRapport {

	private void GenererTableBenevole(PdfPTable tableActis) {
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );
		PdfPCell cell = new PdfPCell ( );
		cell.setBackgroundColor ( BaseColor.BLUE );
		cell.setPhrase ( new Phrase ( "Titre" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Description" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Date" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Bénévole(s)" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Annimateur(s)" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Participant(e)s" , font ) );
		tableActis.addCell ( cell );
	}

	private void GenererTable(PdfPTable tableActis) {
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );
		PdfPCell cell = new PdfPCell ( );
		cell.setBackgroundColor ( BaseColor.BLUE );
		cell.setPhrase ( new Phrase ( "Titre" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Description" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Date" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Bénévole(s)" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Annimateur(s)" , font ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Participant(e)s" , font ) );
		tableActis.addCell ( cell );
	}

	private void EcrireTable(PdfPTable tableActis, List<Acti> actis) {
		SimpleDateFormat formater = new SimpleDateFormat ( "dd/MM/yyyy" );
		HashMap map = new HashMap ( );
		List lDate = new ArrayList ( );
		for (Acti a : actis) {
			String bene = "";
			for (Benevole b : a.getBenevoles_list ( )) {
				bene = bene + b.getNom ( ) + " " + b.getPrenom ( ) + ", ";
				int done = Integer.parseInt ( a.getIsDone ( ) );
				if (done == 1) {
					System.out.println ( a.getIsDone ( ) );
					lDate.add ( formater.format ( a.getDate_acti ( ) ) );
					map.put ( b.getId ( ) , lDate );
				}

			}
			tableActis.addCell ( a.getTitre ( ) );
			tableActis.addCell ( a.getDescription ( ) );
			tableActis.addCell ( formater.format ( a.getDate_acti ( ) ) );
			tableActis.addCell ( bene );
			tableActis.addCell ( a.getNom_animateur ( ) );
			tableActis.addCell ( a.getParticipants ( ) );
		}
		Iterator it = map.entrySet ( ).iterator ( );
		while (it.hasNext ( )) {
			Map.Entry<Integer, Integer> entry = (Map.Entry) it.next ( );
			System.out.println ( entry.getKey ( ) + " = " + entry.getValue ( ) );
		}
	}

	public ByteArrayInputStream actiPDFreport(List<Acti> actis, String nomresponsable, String nomeduc,
			String yearrapport) {
		Document document = new Document ( );
		ByteArrayOutputStream out = new ByteArrayOutputStream ( );
		try {
			PdfWriter.getInstance ( document , out );
			com.itextpdf.text.Font fontIntro = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 16 ,
					BaseColor.BLACK );
			com.itextpdf.text.Font fontTitre = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 16 ,
					Font.UNDERLINE );
			fontTitre.setColor ( BaseColor.BLACK );
			document.open ( );
			Paragraph titre = new Paragraph ( "RAPPORT DES ACTIVITÉS CENTRE LOUIZA" , fontTitre );

			titre.setAlignment ( Element.ALIGN_CENTER );
			titre.setSpacingAfter ( 20 );
			document.add ( titre );
			document.add ( new Paragraph ( "NOM DU RESPONSABLE : " + nomresponsable , fontIntro ) );
			document.add ( new Paragraph ( "NOM DES ÉDUCATEURS : " + nomeduc , fontIntro ) );
			document.add ( new Paragraph ( "ANNÉE : " + yearrapport , fontIntro ) );
			PdfPTable tableActis = new PdfPTable ( 6 );
			// tableActis.setHorizontalAlignment ( 0 );
			tableActis.setSpacingBefore ( 20 );
			titre = new Paragraph ( "TABLEAU DES ACTIVITÉS " , fontTitre );
			titre.setAlignment ( Element.ALIGN_CENTER );
			titre.setSpacingBefore ( 20 );
			document.add ( titre );
			tableActis.setWidthPercentage ( 100 );
			GenererTable ( tableActis );
			EcrireTable ( tableActis , actis );

			PdfPCell cel = new PdfPCell ( );
			cel.setPhrase ( new Phrase ( "Bene" ) );

			document.add ( tableActis );
			tableActis = new PdfPTable ( 1 );
			tableActis.addCell ( cel );
			document.add ( tableActis );
			document.close ( );
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ByteArrayInputStream ( out.toByteArray ( ) );
	}
}
