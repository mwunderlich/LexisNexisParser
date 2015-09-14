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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * A parser to create instances of {@Link LexisNexisDocument}
 * based on documents in given file path.
 * The meta is mapped by on the regexes in config.properties.
 * 
 * @author Martin Wunderlich (martin@wunderlich.com)
 *
 */
public class LexisNexisParser {

	Map<String, Pattern> metaDataMapping = null;
	Properties config = new Properties();
	String propertiesFile = "config.properties";
	Pattern docStartPattern = Pattern.compile("^^\\s+(Dokument|Document) [0-9]+ (von|of) [0-9]+$");	// TODO: Support other languages than EN and DE?
	
	public LexisNexisParser() {
		loadConfiguration();
		this.metaDataMapping = readMetaMappingFromConfig();
	}
	
	private void loadConfiguration() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesFile);
			if(is != null)
				config.load(is);
			else
				throw new FileNotFoundException("Configuration file is missing. Looked for: " + propertiesFile);
		} catch (IOException e) {
			throw new RuntimeException(e);		// wrap and rethrow
		}
	}


	public List<LexisNexisDocument> parse(String filePath) {
		File file = new File(filePath);
		
		return this.parse(file);
	}

	public List<LexisNexisDocument> parse(File parseFile) {
		List<LexisNexisDocument> resultList = new ArrayList<>();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(parseFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		LexisNexisDocument currentDoc = null;
		
		boolean inBody = false;
		boolean afterBody = false;
		boolean inHeader = false;
		boolean afterHeadline = false;
		boolean inCopyright = false;
		
		StringBuilder bodyBuilder = null;
		StringBuilder copyRightBuilder = null;
		
		int emptyHeaderLines = 0; // keeps track of consecutive empty lines in header: two or more of these after the headline mean that the body texts starts (argh).
		
		for(String line : lines) {
			if(line.isEmpty() && !inBody) {
				emptyHeaderLines++;
				continue;	// skip empty lines outside of body
			}
			
			if(isDocumentStart(line)){
				if( currentDoc != null) {
					currentDoc.setText(bodyBuilder.toString());
					currentDoc.setCopyright(copyRightBuilder.toString());
					resultList.add(currentDoc);
				}
				
				// Init
				bodyBuilder = new StringBuilder();
				copyRightBuilder = new StringBuilder();
				currentDoc = new LexisNexisDocument();
				inHeader = true;
				afterHeadline = false;
				inBody = false;
				inCopyright = false;
				emptyHeaderLines = 0;
				
				continue;
			}
			
			
			if(inCopyright || isCopyrightStart(line)) {
				copyRightBuilder.append(line.trim());
				if(!inCopyright)
					inCopyright = true;
				continue;
			}
			
			String type = getMetaLineType(line);
			if(type != null) {
				addMetaLine(line, type, currentDoc);
				emptyHeaderLines = 0;
				if(inBody) {
					inBody = false;
					afterBody = true;
				}
			}
			else {
				if(inBody) {
					if(line.isEmpty())
						bodyBuilder.append("\n");
					else
						bodyBuilder.append(line.trim().replaceAll("\n", " "));
				}
				else if(inHeader && !afterHeadline){ // in header and line is not meta? this must be the headline
					currentDoc.setHeadline(line.trim());
					afterHeadline = true;
					emptyHeaderLines = 0;
				}
				else if(emptyHeaderLines >= 2 && !afterBody){ // first line of body text found
					inBody = true;
					inHeader = false;
					bodyBuilder.append(line.trim().replaceAll("\n", " "));
				}
			}
		}
		
		// Store final doc in list
		if( currentDoc != null) {
			currentDoc.setText(bodyBuilder.toString());
			resultList.add(currentDoc);
		}
		
		return resultList;
	}


	private boolean isCopyrightStart(String line) {
		if(line.contains("            Copyright"))
			return true;
		else
			return false;
	}

	private void addMetaLine(String line, String type, LexisNexisDocument currentDoc) {
		Pattern pattern = metaDataMapping.get(type);
		Matcher m = pattern.matcher(line);
		
		if(! m.matches())
			return;
		
		String matchingGroup = m.group(1).trim();
		try {
			switch(type) {		// TODO MW: not so nice to have this list of types hard-coded; find a better way
				case "publication" : currentDoc.setPublication(matchingGroup); break;
				case "byline" : currentDoc.setByline(matchingGroup); break;
				case "journalCode" : currentDoc.setjournalCode(matchingGroup); break;
				case "language" : currentDoc.setLanguage(matchingGroup); break;
				case "length" : currentDoc.setLength(matchingGroup); break;
				case "loadDate" : currentDoc.setLoadDate(matchingGroup); break;
				case "documentDate" : currentDoc.setDocumentDate(matchingGroup); break;
				case "publicationType" : currentDoc.setPublicationType(matchingGroup); break;
				
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			// just print the error to std out and skip
		}
	}

	private String getMetaLineType(String line) {
		for(String key : metaDataMapping.keySet()){
			Pattern pattern = metaDataMapping.get(key);
			if(pattern.matcher(line).matches())
				return key;
		}
		
		return null;
	}

	private boolean isDocumentStart(String line) {
		return docStartPattern.matcher(line).matches();
	}

	/**
	 * Create a map of pre-compiled regexes. Non-regexes in the properties file
	 * are simply skipped.
	 */
	private Map<String, Pattern> readMetaMappingFromConfig() {
		Map<String, Pattern> mapping = new HashMap<>();
		
		for(Object key : config.keySet()) {
			String regex = config.getProperty((String) key);
			try{
				Pattern pattern = Pattern.compile(regex);
				mapping.put((String) key, pattern);
			}
			catch(Exception ex) {
				// intentionally do nothing
			}
		}
		
		return mapping;
	}
}
