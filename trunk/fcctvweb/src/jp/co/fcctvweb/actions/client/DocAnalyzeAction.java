package jp.co.fcctvweb.actions.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import jp.co.fcctvweb.actions.BasicJsonAction;
import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.services.MyDocService;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;

public class DocAnalyzeAction extends BasicJsonAction {

	private static final long serialVersionUID = -5339982270779108827L;

	// 72 px/inch
	private static final int PAGE_HEIGHT = 842;
	private static final int PAGE_WIDTH = 595;

	private String type;
	private int fileId;

	private MyDocService myDocService;

	public String execute() {

		FakeFile file = myDocService.getFileById(fileId);
		if (file == null) {
			makeFailure();
			setMsg("");
			return ajaxReturn();
		}
		try {
			if ("doc".equalsIgnoreCase(type)) {
				WordExtractor ex = new WordExtractor(new FileInputStream(
						Config.getUploadDocDir() + file.getFileName()));
				SummaryInformation sum = ex.getSummaryInformation();
				System.out.println(sum.getPageCount());
				int height = sum.getPageCount() * PAGE_HEIGHT;
				int width = PAGE_WIDTH;

				getReply().setValue(new Rect(height, width));

			} else if ("docx".equalsIgnoreCase(type)) {
				WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(Config.getUploadDocDir()
						+ file.getFileName()));
				int height = PAGE_HEIGHT * wordMLPackage.getDocPropsExtendedPart().getJaxbElement().getPages();
				int width = 900;
				getReply().setValue(new Rect(height, width));
			} else if ("pdf".equalsIgnoreCase(type)) {
				PdfReader reader = new PdfReader(new FileInputStream(
						Config.getUploadDocDir() + file.getFileName()));

				if (-1 != reader.getCryptoMode()) {
					// encryped
					// can not parse
				}

				int pageNum = reader.getNumberOfPages();
				int height = 0;
				int width = 0;
				for (int i = 1; i <= pageNum; i++) {
					Rectangle pageSize = reader.getPageSize(i);
					if (width < pageSize.getWidth()) {
						width = (int) Math.ceil(pageSize.getWidth());
					}
					height += pageSize.getHeight();
				}
				height += 10 * pageNum;
				getReply().setValue(new Rect(height, width));
			}

		} catch (FileNotFoundException e) {
			makeFailure();
			e.printStackTrace();
		} catch (IOException e) {
			makeFailure();
			e.printStackTrace();
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			makeFailure();
			e.printStackTrace();
		}

		return ajaxReturn();
	}

	public static class Rect {
		private int height;
		private int width;

		public Rect(int h, int w) {
			this.height = h;
			this.width = w;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}
}
