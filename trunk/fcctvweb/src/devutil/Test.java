package devutil;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class Test {
	public static void main(String[] args) throws Exception {
		
//		WordExtractor ex = new WordExtractor(new FileInputStream("e:/test.doc"));
//		SummaryInformation sum = ex.getSummaryInformation();
//		System.out.println(sum.getPageCount());
//		
		XWPFDocument docx = new XWPFDocument(POIXMLDocument.openPackage("/Users/jamescheung/Documents/src/upload/doc/1$X-Games.docx")); 
		int pages = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
		System.out.println(pages);
		
//		PdfReader reader = new PdfReader(new FileInputStream("e:/test.pdf"));
//		System.out.println(reader.getNumberOfPages());
//		if (-1 != reader.getCryptoMode()) {
//			
//		}
//		
//		int pageNum = reader.getNumberOfPages();
//		int height = 0;
//		for (int i = 1; i <= pageNum; i++){
//			Rectangle pageSize = reader.getPageSize(i);
//			height += pageSize.getHeight();
//			System.out.println("Page size: " + pageSize.getHeight());
//		}
//		
//		System.out.println("Height: " + height);
	}
}
