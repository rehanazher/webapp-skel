/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.devutil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * do not return directories;
 * 
 * @author James
 * 
 */
public class FilesUtils {

	public static interface FileFilter {
		boolean filter(File file);
	}

	public static List<File> getFiles(String dir) {
		return getFiles(dir, false);
	}

	public static List<File> getFiles(String dir, boolean includeSubFiles) {
		return getFiles(dir, new FileFilter() {
			@Override
			public boolean filter(File arg0) {
				return true;
			}
		}, includeSubFiles);
	}

	public static List<File> getFiles(String dir, FileFilter filter) {
		return getFiles(dir, filter, false);
	}

	public static List<File> getFiles(String dir, FileFilter filter,
			boolean includeSubFiles) {
		List<File> result = new ArrayList<File>();

		File file = new File(dir);

		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for (File sub : subFiles) {
				if (sub.isFile() && filter.filter(sub)) {
					result.add(sub);
				} else if (includeSubFiles) {
					result.addAll(getFiles(sub.getAbsolutePath(), filter,
							includeSubFiles));
				}
			}
		} else {
			if (filter.filter(file)) {
				result.add(file);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		List<File> fileList = getFiles("./src/config/", true);
		for (File f: fileList){
			System.out.println(f.getAbsolutePath());
		}
	}
}
