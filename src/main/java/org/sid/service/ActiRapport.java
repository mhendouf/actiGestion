package org.sid.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sid.entity.Acti;
import org.sid.entity.Benevole;
import org.sid.entity.Passage;
import org.sid.repository.PassageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ActiRapport {
	@Autowired
	PassageRepository passageRepository;

	private void GenererTableBenevole(PdfPTable tableActis, String nom) throws DocumentException {
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );

		PdfPCell cell = new PdfPCell ( );
		// cell.setBackgroundColor ( BaseColor.BLUE );
		cell.setPhrase ( new Phrase ( nom , font ) );
		cell.setHorizontalAlignment ( Element.ALIGN_CENTER );
		cell.setCalculatedHeight ( 30 );
		cell.setBackgroundColor ( BaseColor.BLUE );
		tableActis.addCell ( cell );

		tableActis.setWidthPercentage ( 100 );
	}

	private void GenererTablePassageBenevole(PdfPTable tableActis) {
		// tableActis = new PdfPTable ( 2 );
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );
		SimpleDateFormat formater = new SimpleDateFormat ( "MMMM" );
		PdfPCell cell = new PdfPCell ( );
		cell.setPhrase ( new Phrase ( "Mois" , font ) );
		cell.setBackgroundColor ( BaseColor.BLUE );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "Nombre de passage" , font ) );
		cell.setBackgroundColor ( BaseColor.BLUE );
		tableActis.addCell ( cell );
	}

	private void GenererTableInfoPassageBenevole(PdfPTable tableActis, Map<Benevole, Map> mpBene, Document document)
			throws DocumentException {
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );
		SimpleDateFormat formater = new SimpleDateFormat ( "MMMM" );
		Map<String, Integer> mapmois = new HashMap<String, Integer> ( );
		PdfPCell cell = new PdfPCell ( );
		for (Map.Entry mapentry : mpBene.entrySet ( )) {

			tableActis = new PdfPTable ( 1 );
			tableActis.setSpacingBefore ( 20 );
			tableActis.setWidthPercentage ( 100 );
			Benevole b = (Benevole) mapentry.getKey ( );
			GenererTableBenevole ( tableActis , b.getNom ( ) + " " + b.getPrenom ( ) );
			document.add ( tableActis );

			tableActis = new PdfPTable ( 2 );
			// tableActis.setSpacingAfter ( 20 );
			tableActis.setWidthPercentage ( 100 );
			GenererTablePassageBenevole ( tableActis );
			// document.add ( tableActis );
			// tableActis = new PdfPTable ( 2 );

			mapmois = (Map<String, Integer>) mapentry.getValue ( );
			int i = 6;
			for (Map.Entry mapinfo : mapmois.entrySet ( )) {
				String f = formater.format ( new Date ( 1 , i , 2021 ) );
				// System.out.println ( f );
				cell.setPhrase ( new Phrase ( f ) );
				tableActis.addCell ( cell );
				cell.setPhrase ( new Phrase ( mapmois.get ( f ).toString ( ) ) );
				tableActis.addCell ( cell );
				System.out.println ( "Mois : " + f + " --- benevole passage : " + mapmois.get ( f ).toString ( ) );
				if (i == 12)
					i = 1;
				else
					++i;
			}
			i = 1;
			tableActis.setWidthPercentage ( 100 );
			document.add ( tableActis );
			// GenererTablePassageBenevole ( tableActis );
			// document.add ( tableActis );
		}

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

	private void TableBenevole(PdfPTable tableActis) {
		System.out.println ( "tttttttttttttttttttttttttt" );
		com.itextpdf.text.Font font = FontFactory.getFont ( FontFactory.HELVETICA_BOLDOBLIQUE , 12 , BaseColor.WHITE );
		PdfPCell cell = new PdfPCell ( );
		// tableActis = new PdfPTable ( 4 );
		cell.setBackgroundColor ( BaseColor.BLUE );

		cell.setPhrase ( new Phrase ( "Nom" , font ) );
		tableActis.addCell ( cell );

		cell.setPhrase ( new Phrase ( "Prénom" , font ) );
		tableActis.addCell ( cell );

		cell.setPhrase ( new Phrase ( "Numéro Tél" , font ) );
		tableActis.addCell ( cell );

		cell.setPhrase ( new Phrase ( "Email" , font ) );
		tableActis.addCell ( cell );
	}

	private void EcrireTable(PdfPTable tableActis, List<Acti> actis) {
		SimpleDateFormat formater = new SimpleDateFormat ( "dd/MM/yyyy" );
		List lDate = new ArrayList ( );
		for (Acti a : actis) {
			String bene = "";
			for (Benevole b : a.getBenevoles_list ( )) {
				bene = bene + b.getNom ( ) + " " + b.getPrenom ( ) + ", ";
				int done = Integer.parseInt ( a.getIsDone ( ) );
				if (done == 1) {
					// System.out.println ( a.getIsDone ( ) );
					lDate.add ( formater.format ( a.getDate_acti ( ) ) );
				}

			}
			tableActis.addCell ( a.getTitre ( ) );
			tableActis.addCell ( a.getDescription ( ) );
			tableActis.addCell ( formater.format ( a.getDate_acti ( ) ) );
			tableActis.addCell ( bene );
			tableActis.addCell ( a.getNom_animateur ( ) );
			tableActis.addCell ( a.getParticipants ( ) );
		}

	}

	private void EcrireTableBenevole(PdfPTable tableActis, List<Benevole> benevole) {
		SimpleDateFormat formater = new SimpleDateFormat ( "dd/MM/yyyy" );
		List lDate = new ArrayList ( );
		for (Benevole b : benevole) {
			String bene = "";

			tableActis.addCell ( b.getNom ( ) );
			tableActis.addCell ( b.getPrenom ( ) );
			tableActis.addCell ( b.getNum_tel ( ) );
			tableActis.addCell ( b.getMail ( ) );
		}

	}

	private void GenererMapBenevole(Map<Benevole, Map> mpBene, List<Benevole> benevoles, String yearrapport) {
		SimpleDateFormat formater = new SimpleDateFormat ( "MMMM" );
		Map<String, Integer> mapmois = new HashMap<String, Integer> ( );
		int year = new Integer ( yearrapport );
		int mois = 1;
		Date f = null;
		Date dy = new Date ( "1/1/" + yearrapport );
		for (Benevole b : benevoles) {
			// System.out.println ( b.getNom ( ) );
			for (int i = 1; i < 13; ++i) {

				f = new Date ( 1 , i , year );
				// formater.format ( f );
				List<Passage> lp = passageRepository.findNbByMonth ( b.getId ( ) , f , dy );
				mapmois.put ( formater.format ( f ) , lp.size ( ) );

			}

			mpBene.put ( b , mapmois );
			mapmois = new HashMap<String, Integer> ( );
		}

	}

	public ByteArrayInputStream actiPDFreport(List<Acti> actis, String nomresponsable, String nomeduc,
			String yearrapport, List<Benevole> benevoles) {
		Map<Benevole, Map> mpBene = new HashMap<Benevole, Map> ( );
		GenererMapBenevole ( mpBene , benevoles , yearrapport );
		Document document = new Document ( );
		ByteArrayOutputStream out = new ByteArrayOutputStream ( );
		try {
			Image image = Image.getInstance ( "SamuSocial.PNG" );
			image.scaleToFit ( 500 , 500 );
			// image.setWidthPercentage ( 1 );
			image.setAlignment ( Element.ALIGN_CENTER );
			PdfWriter.getInstance ( document , out );
			com.itextpdf.text.Font fontIntro = FontFactory.getFont ( FontFactory.HELVETICA_BOLD , 14 ,
					BaseColor.BLACK );
			com.itextpdf.text.Font fontTitre = FontFactory.getFont ( FontFactory.HELVETICA_BOLD , 16 , Font.UNDERLINE );
			fontTitre.setColor ( BaseColor.BLACK );
			document.open ( );
			document.add ( image );
			Paragraph p = new Paragraph ( "SAMU SOCIAL" , fontIntro );
			p.setSpacingBefore ( 40 );
			document.add ( p );
			document.add ( new Paragraph ( "Boulevard Poincare 68, 170 Bruxelles" , fontIntro ) );
			document.add ( new Paragraph ( "02/5511220-0800/99.340" , fontIntro ) );
			document.add ( new Paragraph ( "info@samusocial.be" , fontIntro ) );
			Paragraph titre = new Paragraph ( "RAPPORT DES ACTIVITÉS CENTRE LOUIZA " + yearrapport , fontTitre );

			titre.setAlignment ( Element.ALIGN_CENTER );
			titre.setSpacingBefore ( 20 );
			document.add ( titre );
			PdfPTable tableActis = new PdfPTable ( 6 );
			// tableActis.setHorizontalAlignment ( 0 );
			tableActis.setSpacingBefore ( 40 );
			titre = new Paragraph ( "TABLEAU DES ACTIVITÉS " , fontTitre );
			titre.setAlignment ( Element.ALIGN_CENTER );
			titre.setSpacingBefore ( 20 );
			document.add ( titre );

			tableActis.setWidthPercentage ( 100 );
			GenererTable ( tableActis );
			EcrireTable ( tableActis , actis );
			tableActis.setSpacingAfter ( 20 );
			document.add ( tableActis );
			document.newPage ( );
			titre = new Paragraph ( "TABLEAU DES BÉNÉVOLES" , fontTitre );
			titre.setAlignment ( Element.ALIGN_CENTER );
			// titre.setSpacingBefore ( 20 );
			titre.setSpacingAfter ( 40 );
			document.add ( titre );

			tableActis = new PdfPTable ( 4 );
			tableActis.setWidthPercentage ( 100 );
			TableBenevole ( tableActis );
			tableActis.setSpacingAfter ( 20 );
			// document.add ( tableActis );
			EcrireTableBenevole ( tableActis , benevoles );
			document.add ( tableActis );

			document.newPage ( );
			titre = new Paragraph ( "TABLEAU DE PRESENCE DES BÉNÉVOLES/MOIS" , fontTitre );
			titre.setAlignment ( Element.ALIGN_CENTER );
			// titre.setSpacingBefore ( 20 );
			titre.setSpacingAfter ( 20 );
			document.add ( titre );
			// GenererTableBenevole ( tableActis , document );
			// tableActis.setWidthPercentage ( 100 );
			// tableActis.setSpacingAfter ( 20 );
			// document.add ( tableActis );
			// GenererTablePassageBenevole ( tableActis );
			// document.add ( tableActis );
			tableActis.setSpacingAfter ( 60 );
			GenererTableInfoPassageBenevole ( tableActis , mpBene , document );
			Paragraph educ = new Paragraph ( "NOM DES ÉDUCATEURS : " + nomeduc , fontIntro );
			Paragraph res = new Paragraph ( "NOM DU RESPONSABLE : " + nomresponsable , fontIntro );
			res.setSpacingBefore ( 40 );
			document.add ( res );
			document.add ( educ );
			// document.add ( new Paragraph ( "ANNÉE : " + yearrapport , fontIntro ) );
			document.close ( );

		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ByteArrayInputStream ( out.toByteArray ( ) );
	}
}
