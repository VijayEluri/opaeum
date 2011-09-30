package org.opeum.javageneration;

import java.io.CharArrayWriter;

import org.opeum.textmetamodel.TextSource;

public class CharArrayTextSource implements TextSource{
	CharArrayWriter writer;
	private char[] charArray;
	public CharArrayTextSource(CharArrayWriter contentWriter){
		this.writer = contentWriter;
	}
	public CharArrayTextSource(char[] charArray){
		this.charArray = charArray;
		// TODO Auto-generated constructor stub
	}
	public char[] toCharArray(){
		if(charArray != null){
			return charArray;
		}else{
			return writer.toCharArray();
		}
	}
	public boolean hasContent(){
		return true;
	}
}
