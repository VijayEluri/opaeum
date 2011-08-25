package org.nakeduml.runtime.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractFormatter{
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	DecimalFormat realFormat = new DecimalFormat("#0.00000000000000");
	NumberFormat integerFormat = NumberFormat.getIntegerInstance();
	public String formatDateTime(Date dateTime){
		return dateTimeFormat.format(dateTime);
	}
	public Date parseDateTime(String s){
		try{
			return dateTimeFormat.parse(s);
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}
	public String formatReal(Double d){
		return realFormat.format(d);
	}
	public Double parseReal(String s){
		try{
			return realFormat.parse(s).doubleValue();
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}
	public String formatInteger(Integer i){
		return integerFormat.format(i);
	}
	public Integer parseInteger(String s){
		try{
			return integerFormat.parse(s).intValue();
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}
	public String formatString(String s){
		return s;
	}
	public String parseString(String s){
		return s;
	}
	public String formatText(String s){
		return s;
	}
	public String parseText(String s){
		return s;
	}
	public String formatBoolean(Boolean value){
		return value.toString();
	}
	public String formatUnlimitedNatural(Integer value){
		return formatInteger(value);
	}
	public String formatboolean(Boolean value){
		return formatBoolean(value);
	}
	public String formatbyte(Integer value){
		return formatInteger(value);
	}
	public String formatchar(String value){
		return value;
	}
	public String formatdouble(Double value){
		return formatReal(value);
	}
	public String formatfloat(float value){
		return formatReal(new Double(value));
	}
	public String formatint(int value){
		return formatInteger(value);
	}
	public String formatlong(Integer value){
		return formatInteger(value);
	}
	public String formatshort(Integer value){
		return formatInteger(value);
	}
	public Boolean parseBoolean(String value){
		return Boolean.valueOf(value);
	}
	public Integer parseUnlimitedNatural(String value){
		return parseInteger(value);
	}
	public Boolean parseboolean(String value){
		return parseBoolean(value);
	}
	public Integer parsebyte(String value){
		return parseInteger(value);
	}
	public String parsechar(String value){
		return value;
	}
	public Double parsedouble(String value){
		return parseReal(value);
	}
	public float parsefloat(String value){
		return parseReal(value).floatValue();
	}
	public int parseint(String value){
		return parseInteger(value);
	}
	public Integer parselong(String value){
		return parseInteger(value);
	}
	public Integer parseshort(String value){
		return parseInteger(value);
	}
}