package org.sid.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.sid.entity.Acti;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ActiRapport {

	private void GenererTable(PdfPTable tableActis) {
		PdfPCell cell = new PdfPCell ( );
		cell.setPhrase ( new Phrase ( "Titre" ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "nom" ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "bene" ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "anim" ) );
		tableActis.addCell ( cell );
		cell.setPhrase ( new Phrase ( "tt" ) );
		tableActis.addCell ( cell );

	}

	public ByteArrayInputStream actiPDFreport(List<Acti> actis, String nomresponsable, String nomeduc,
			String yearrapport) {
		Document document = new Document ( );
		ByteArrayOutputStream out = new ByteArrayOutputStream ( );
		try {
			PdfWriter.getInstance ( document , out );
			document.open ( );
			document.add ( new Paragraph ( "NOM RESPONSABLE : " + nomresponsable ) );
			document.add ( new Paragraph ( "Nom Educ : " + nomeduc ) );
			document.add ( new Paragraph ( "ANNEE : " + yearrapport ) );
			PdfPTable tableActis = new PdfPTable ( 5 );
			// tableActis.setHorizontalAlignment ( 0 );
			tableActis.setSpacingBefore ( 15 );
			tableActis.setWidthPercentage ( 100 );
			GenererTable ( tableActis );
			document.add ( tableActis );
			document.close ( );
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ByteArrayInputStream ( out.toByteArray ( ) );
	}
}
