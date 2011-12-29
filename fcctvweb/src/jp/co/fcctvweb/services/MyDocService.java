package jp.co.fcctvweb.services;

import java.util.Map;

import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;
import jp.co.fcctvweb.vo.MyDocNode;

public interface MyDocService {

	boolean addFileToRoot(FakeFile file);

	MyDocNode retrieveDocTree();

	Map<Integer, FakeFile> getFileMap();

	Map<Integer, FakeFolder> getFolderMap();

}
