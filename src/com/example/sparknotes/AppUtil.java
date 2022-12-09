package com.example.sparknotes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class AppUtil {

	public static void writeToFileNoteContent(File noteContent, String title, String date, String content) {
//		try {
//			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(noteContent, true));
//			bufferedWriter.write(title);
//			bufferedWriter.newLine();
//			bufferedWriter.write(date);
//			bufferedWriter.newLine();
//			bufferedWriter.write(content);
//			bufferedWriter.newLine();
//			bufferedWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
		
		try {
			FileOutputStream fos = new FileOutputStream(noteContent);
			StringBuilder sb = new StringBuilder();
			sb.append(title).append("\n").append(date).append("\n").append(content);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ByteArrayInputStream abis = new ByteArrayInputStream(sb.toString().getBytes());
			BufferedInputStream bis = new BufferedInputStream(abis);
			byte[] bys = new byte[8192];
			int len;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys);
				bos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void writeToFileNoteAttach(File attachFile, AttachItem attachItem) throws IOException {
		
		FileInputStream fis = new FileInputStream(attachItem.getPath());
		FileOutputStream out = new FileOutputStream(attachFile, false);
		
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fis, 8192);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out, 8192);
		byte[] bys = new byte[8192];
		int len;
		while ((len = bufferedInputStream.read(bys)) != -1) {
			bufferedOutputStream.write(bys);
			bufferedOutputStream.flush();
		}
		fis.close();
		bufferedInputStream.close();
		out.close();
		bufferedOutputStream.close();
		
	}
	
	public static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            final File[] children = fileToZip.listFiles();
            for (final File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        final FileInputStream fis = new FileInputStream(fileToZip);
        final ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        final byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
	
	
}
