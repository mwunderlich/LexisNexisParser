/*************************************************************************************************************
 * LexisNexisParser - A Java wrapper for LexisNexis data.
 * Copyright 2015 Martin Wunderlich
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * 
 * GNU Lesser General Public License (LGPL)
 * 
 * as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. You should have received a copy
 * of the GNU Lesser General Public License along with this library. If not, see http://www.gnu.org/licenses/.
 **************************************************************************************************************/

package com.martinwunderlich.nlp.io;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LexisNexisDocument {

	private int length;
	private String headline;
	private String text;
	private Date loadDate;
	private String publicationType;
	private String language;
	private int journalCode;
	private String publication;
	private String byline;
	private String copyright;
	private Date documentDate;

	private String dateFormat = "MMMMM dd, yyyy";
	private Locale locale = new Locale("en");
	
	public int getLength() {
		return length;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String header) {
		headline = header;
	}

	public Date getDocumentDate() {
		return this.documentDate;
	}

	public Date getLoadDate() {
		return this.loadDate;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getPublicationType() {
		return this.publicationType;
	}

	public int getJournalCode() {
		return this.journalCode;
	}

	public String getCopyright() {
		return this.copyright;
	}

	public String getText() {
		return this.text;
	}
	
	public void setText(String string) {
		this.text = string;
	}

	public String getByline() {
		return this.byline;
	}

	public void appendTextLine(String line) {
		this.text += line;
	}

	public String getPublication() {
		return this.publication;
	}

	public void setDocumentDate(String s) throws ParseException {
		DateFormat df = new SimpleDateFormat(dateFormat, locale);
		this.documentDate = df.parse(s);
	}

	public void setPublication(String s) {
		this.publication = s;
	}

	public void setByline(String s) {
		this.byline = s;
	}

	public void setjournalCode(String s) {
		this.journalCode = Integer.parseInt(s);
	}

	public void setLanguage(String s) {
		this.language = s;
	}

	public void setLoadDate(String s) throws ParseException {
		DateFormat df = new SimpleDateFormat(dateFormat, locale);
		this.loadDate = df.parse(s);
	}

	public void setPublicationType(String s) {
		this.publicationType = s;
	}

	public void setLength(String s) {
		this.length = Integer.parseInt(s);
	}

	public void setCopyright(String s) {
		this.copyright = s;
	}
}
