package com.martinwunderlich.nlp.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.martinwunderlich.nlp.io.LexisNexisDocument;
import com.martinwunderlich.nlp.io.LexisNexisParser;

public class LexisNexisParserTest {

	String testDoksFilePathIsraelPost = "src/test/resources/IsraelPostTestDocs.txt";
	String testDoksFilePathKigaliNewTimes = "src/test/resources/KigaliNewTimesTestDocs.txt";
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void testIsarelPost() throws ParseException {
		LexisNexisParser parser = new LexisNexisParser();
		File testDoksFileIsraelPost = new File(testDoksFilePathIsraelPost).getAbsoluteFile();
		
		List<LexisNexisDocument> lnDocs = parser.parse(testDoksFileIsraelPost);
		
		assertEquals("There should be 10 documents in the list.", lnDocs.size(), 10);
		
		LexisNexisDocument firstDoc = lnDocs.get(0);
		assertNotNull("The first document must not be Null.", firstDoc);
		
		// Check meta data
		assertEquals("The document's publication is incorrect", firstDoc.getPublication() ,"Jpost.com (The Jerusalem Post online edition)");
		
		assertEquals("The document should have a length of 1876 words.", firstDoc.getLength(), 1876);
		
		assertEquals("The document's headline does not match.", firstDoc.getHeadline(), "Analysis: Can the ICC go after settlements as war crimes?");
		
		assertEquals("The document's load-date should be April 1st 2015.", firstDoc.getDocumentDate(), df.parse("2015-04-01"));
		
		assertEquals("The document's load-date should be April 1st 2015.", firstDoc.getLoadDate(), df.parse("2015-04-01"));

		assertEquals("The document's language should be English.", firstDoc.getLanguage(),"ENGLISH");

		assertEquals("The document's publication type should be English.", firstDoc.getPublicationType(),"Web Publication");
		
		assertEquals("The document's journal code should be 1492.", firstDoc.getJournalCode(), 1492);
		
		assertTrue("The document's copyright notice is incorrect.", firstDoc.getCopyright().contains("Copyright 2015 The Jerusalem Post."));
		assertTrue("The document's copyright notice is incorrect.", firstDoc.getCopyright().contains("Provided by Syndigate Media Inc."));
		assertTrue("The document's copyright notice is incorrect.", firstDoc.getCopyright().contains("All Rights Reserved"));
		
		assertNotNull("The document's text must not be Null.", firstDoc.getText());
		assertFalse("The document's text must not be empty.", firstDoc.getText().isEmpty());
	}
	
	@Test
	public void testKigaliNewTimes() throws ParseException {
		LexisNexisParser parser = new LexisNexisParser();
		File testDoksFileKigaliNewTimes = new File(testDoksFilePathKigaliNewTimes).getAbsoluteFile();
		List<LexisNexisDocument> lnDocs = parser.parse(testDoksFileKigaliNewTimes);
		
		assertEquals("There should be 5 documents in the list.", lnDocs.size(), 5);
		
		LexisNexisDocument firstDoc = lnDocs.get(0);
		assertNotNull("The first document must not be Null.", firstDoc);
		
		// Check meta data
		assertEquals("The document's publication is incorrect", firstDoc.getPublication() ,"The New Times (Kigali)");
		
		assertEquals("The document should have a length of 462 words.", firstDoc.getLength(), 462);
		
		assertEquals("The document's headline does not match.", firstDoc.getHeadline(), "Why Parables? [opinion]");
		
		assertEquals("The document's headline does not match.", firstDoc.getByline(), "Daniel Ledama");
		
		assertEquals("The document's load-date should be April 1st 2015.", firstDoc.getDocumentDate(), df.parse("2014-12-21"));

		assertEquals("The document's load-date should be April 1st 2015.", firstDoc.getLoadDate(), df.parse("2014-12-22"));
		
		assertEquals("The document's language should be English.", firstDoc.getLanguage(),"ENGLISH");

		assertEquals("The document's publication type should be English.", firstDoc.getPublicationType(),"Newspaper");
		
		assertTrue("The document's copyright notice is incorrect.", firstDoc.getCopyright().contains("Copyright 2014 AllAfrica Global Media."));
		assertTrue("The document's copyright notice is incorrect.", firstDoc.getCopyright().contains("All Rights Reserved"));
		
		assertNotNull("The document's text must not be Null.", firstDoc.getText());
		assertFalse("The document's text must not be empty.", firstDoc.getText().isEmpty());
	}
}
