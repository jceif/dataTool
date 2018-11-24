package com.jceif.data.common;




import java.util.Collection;




import org.apache.commons.lang3.StringUtils;


public class StringUtil {
	
	public static String moneyToString(String number){
		return MoneyString.toChinese(number);
	}
	
//	IsEmpty/IsBlank - checks if a String contains text
	public static boolean isEmpty(String str){
		return StringUtils.isEmpty(str);
	}
	public static boolean isBlank(String str){
		return StringUtils.isBlank(str);
	}
	public static String defaultString(String str, String defaultStr) {
		return StringUtils.defaultIfEmpty(str, defaultStr);
	}
//	Trim/Strip - removes leading and trailing whitespace
	public static String trimToEmpty(String str){
		return StringUtils.trimToEmpty(str);
	}
	public static String trimToNull(String str){
		return StringUtils.trimToNull(str);
	}
//	Equals - compares two strings null-safe
	public static boolean equals(String str1,String str2){
		return StringUtils.equals(str1, str2);
	}
	public static boolean equalsIgnoreCase(String str1,String str2){
		return StringUtils.equalsIgnoreCase(str1, str2);
	}
//	startsWith - check if a String starts with a prefix null-safe
	public static boolean startsWith(String str,String prefix){
		return StringUtils.startsWith(str, prefix);
	}
	public static boolean startsWithIgnoreCase(String str,String prefix){
		return StringUtils.startsWithIgnoreCase(str, prefix);
	}
//	endsWith - check if a String ends with a suffix null-safe
	public static boolean endsWith(String str,String suffix){
		return StringUtils.endsWith(str, suffix);
	}
	public static boolean endsWithIgnoreCase(String str,String suffix){
		return StringUtils.endsWithIgnoreCase(str, suffix);
	}
//	IndexOf/LastIndexOf/Contains - null-safe index-of checks
//	IndexOfAny/LastIndexOfAny/IndexOfAnyBut/LastIndexOfAnyBut - index-of any of a set of Strings
//	ContainsOnly/ContainsNone/ContainsAny - does String contains only/none/any of these characters
	public static boolean contains(String str,String searchChar){
		return StringUtils.contains(str, searchChar);
	}
//	Substring/Left/Right/Mid - null-safe substring extractions
	public static String substring(String str,int start){
		return StringUtils.substring(str, start);
	}
	public static String substring(String str,int start,int end){
		return StringUtils.substring(str, start, end);
	}
	public static String left(String str,int len){
		return StringUtils.left(str, len);
	}
	public static String right(String str,int len){
		return StringUtils.right(str, len);
	}
	public static String center(String str,int size){
		return StringUtils.center(str, size);
	}
//	Split/Join - splits a String into an array of substrings and vice versa
	public static String[] split(String str){
		return StringUtils.split(str);
	}
	public static String[] split(String str,String separatorChars){
		return StringUtils.split(str, separatorChars);
	}
	public static String[] split(String str,String separatorChars,int max){
		return StringUtils.split(str, separatorChars, max);
	}
	public static String join(String[] array){
		return StringUtils.join(array);
	}
	public static String join(String[] array,String separator){
		return StringUtils.join(array, separator);
	}
	public static String join(Collection<?> collection,String separator){
		return StringUtils.join(collection, separator);
	}
	public static String join(Collection<?> collection){
		return StringUtils.join(collection,"");
	}
//	Remove/Delete - removes part of a String
	public static String str(String str, String remove){
		return StringUtils.remove(str, remove);
	}
	public static String deleteWhitespace(String str){
		return StringUtils.deleteWhitespace(str);
	}
//	Replace/Overlay - Searches a String and replaces one String with another
	public static String Replace(String text, String searchString, String replacement){
		return StringUtils.replace(text, searchString, replacement);
	}
	public static String replaceOnce(String text, String searchString, String replacement){
		return StringUtils.replaceOnce(text, searchString, replacement);
	}
//	Chomp/Chop - removes the last part of a String
//	LeftPad/RightPad/Center/Repeat - pads a String
	public static String leftPad(String str,int size,String padStr){
		return StringUtils.leftPad(str, size, padStr);
	}
	public static String leftPad(String str,int size){
		return StringUtils.leftPad(str, size);
	}
	public static String rightPad(String str,int size,String padStr){
		return StringUtils.rightPad(str, size, padStr);
	}
	public static String rightPad(String str,int size){
		return StringUtils.rightPad(str, size);
	}
//	UpperCase/LowerCase/SwapCase/Capitalize/Uncapitalize - changes the case of a String
	public static String upperCase(String str){
		return StringUtils.upperCase(str);
	}
	public static String lowerCase(String str){
		return StringUtils.lowerCase(str);
	}
	public static String capitalize(String arg0){
		return StringUtils.capitalize(arg0);
	}
	public static String uncapitalize(String arg0){
		return StringUtils.uncapitalize(arg0);
	}
//	IsAlpha/IsNumeric/IsWhitespace/IsAsciiPrintable - checks the characters in a String
	public static boolean isAlpha(String arg0){
		return StringUtils.isAlpha(arg0);
	}
	public static boolean isNumeric(String arg0){
		return StringUtils.isNumeric(arg0);
	}
	public static boolean isWhitespace(String arg0){
		return StringUtils.isWhitespace(arg0);
	}

}
