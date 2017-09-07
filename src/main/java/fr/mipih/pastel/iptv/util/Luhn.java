package fr.mipih.pastel.iptv.util;

public class Luhn {
	public static boolean Check(String ccNumber) {
		try {
			int sum = 0;
			boolean alternate = false;
			for (int i = ccNumber.length() - 1; i >= 0; i--) {
				int n = Integer.parseInt(ccNumber.substring(i, i + 1));
				if (alternate) {
					n *= 2;
					if (n > 9) {
						n = (n % 10) + 1;
					}
				}
				sum += n;
				alternate = !alternate;
			}
			return (sum % 10 == 0);
		} 
		catch (NumberFormatException nfe) {
			return false;
		}
	}
}