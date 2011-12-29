package jp.co.fcctvweb.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.fcctvweb.daos.FileDao;
import jp.co.fcctvweb.daos.FolderDao;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;
import jp.co.fcctvweb.vo.MyDocNode;

public class MyDocServiceImpl implements MyDocService {

	private FileDao fileDao;
	private FolderDao folderDao;

	public boolean addFileToRoot(FakeFile file) {
		List<FakeFolder> root = folderDao.findByParentId(0);
		if (root.isEmpty()) {
			FakeFolder r = new FakeFolder();
			r.setFolderName("root");
			r.setPosition("1");
			r.setParentId(0);
			folderDao.insert(r);
			root = folderDao.findByParentId(0);
		}

		FakeFolder rootNode = root.get(0);
		file.setFolderId(rootNode.getId());
		return fileDao.insert(file);
	}

	public MyDocNode retrieveDocTree() {
		Map<Integer, FakeFolder> folderMap = getFolderMap();
		List<FakeFile> fileList = fileDao.findAll();

		Map<Integer, FakeFile> folderFileMap = new HashMap<Integer, FakeFile>();
		for (FakeFile f : fileList) {
			folderFileMap.put(f.getFolderId(), f);
		}

		List<FakeFolder> rootList = folderDao.findByParentId(0);
		if (rootList.isEmpty()) {
			FakeFolder r = new FakeFolder();
			r.setFolderName("root");
			r.setPosition("1");
			r.setParentId(0);
			folderDao.insert(r);
			rootList = folderDao.findByParentId(0);
		}

		MyDocNode root = new MyDocNode(rootList.get(0));
		for (Entry<Integer, FakeFolder> entry : folderMap.entrySet()) {
			if (entry.getValue().getId() == root.getKey()) {
				
			}
		}

		return null;
	}

	public Map<Integer, FakeFolder> getFolderMap() {
		List<FakeFolder> folderList = folderDao.findAll();
		Map<Integer, FakeFolder> map = new HashMap<Integer, FakeFolder>();
		for (FakeFolder f : folderList) {
			map.put(f.getId(), f);
		}
		return map;
	}

	public Map<Integer, FakeFile> getFileMap() {
		List<FakeFile> fileList = fileDao.findAll();
		Map<Integer, FakeFile> map = new HashMap<Integer, FakeFile>();
		for (FakeFile f : fileList) {
			map.put(f.getId(), f);
		}
		return map;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
}
