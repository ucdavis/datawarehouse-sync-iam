package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	/**
	 * Converts stacktrace of 'e' to a String.
	 * 
	 * Credit: http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
	 * 
	 * @param e An exception
	 * @return The exception's stacktrace, as a string
	 */
	public static String stacktraceToString(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		return sw.toString();
	}
}
