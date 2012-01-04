package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.po.FakeFile;

public interface FileDao {

	List<FakeFile> findAll();

	boolean insert(FakeFile fakeFile);

	boolean delete(int fakeFileId);

	boolean update(FakeFile fakeFile);

	FakeFile findById(int fakeFileId);

	boolean updateFolderId(int fileId, int folderId);
	
	boolean updateFileName(int fileId, String fileName);
}
