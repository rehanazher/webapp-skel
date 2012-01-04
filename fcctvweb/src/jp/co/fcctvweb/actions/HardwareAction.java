package jp.co.fcctvweb.actions;

import java.io.File;

public class HardwareAction extends BasicJsonAction {

	private static final long serialVersionUID = 4971651946041852701L;

	public static void main(String[] args) {
		File f = new File("/Users/jamescheung/Documents/src/upload");

		if (f.exists()) {
			long g = 1000000000l;
			System.out.println("free: " + f.getFreeSpace() / g);
			System.out.println("total: " + f.getTotalSpace() / g);
			System.out.println("usable: " + f.getUsableSpace() / g);
		}
	}
}
