package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.po.FakeFolder;

public interface FolderDao {

	List<FakeFolder> findAll();

	boolean insert(FakeFolder fakeFolder);

	boolean delete(int fakeFolderId);

	boolean update(FakeFolder fakeFolder);

	FakeFolder findById(int fakeFolderId);

	List<FakeFolder> findByParentId(int parentId);
}
