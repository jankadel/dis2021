package de.dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}

	//TODO
	public static double readDouble(String label) {
		double ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Double.parseDouble(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Gleitkommazahl an!");
			}
		}
		
		return ret;
		}

	//TODO
	public static Date readDate(String label) {

		java.sql.Date ret = null;
		boolean finished = false;
		
		while(!finished) {
			String line = readString(label);
		
			try {
				 SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
				 java.util.Date date = sdf.parse(line);
				 ret = new Date(date.getTime());
				 finished = true;
			} catch (ParseException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Datum im Format TT.MM.YYYY an.");
			}
			
		}
		
		return ret;
	}
}
