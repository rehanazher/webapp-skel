package jp.co.fcctvweb.services;

import java.util.List;
import java.util.Map;

import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;
import jp.co.fcctvweb.vo.MyDocNode;

public interface MyDocService {

	boolean addFileToRoot(FakeFile file);
	
	FakeFolder getRootFolder();

	MyDocNode retrieveDocTree();
	
	List<MyDocNode> retrieveDocFlatDir();

	Map<Integer, FakeFile> getFileMap();

	Map<Integer, FakeFolder> getFolderMap();

	FakeFile getFileById(int fileId);
	
	boolean moveFileToFolder(int fileId, int folderId);
}
