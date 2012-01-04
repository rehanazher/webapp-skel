package jp.co.fcctvweb.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.daos.FileDao;
import jp.co.fcctvweb.daos.FolderDao;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;
import jp.co.fcctvweb.vo.MyDocNode;

public class MyDocServiceImpl implements MyDocService {

	private FileDao fileDao;
	private FolderDao folderDao;

	public boolean addFileToRoot(FakeFile file) {
		FakeFolder rootNode = getRootFolder();
		file.setFolderId(rootNode.getId());
		return fileDao.insert(file);
	}

	public MyDocNode retrieveDocTree() {
		List<FakeFile> fileList = fileDao.findAll();
		List<FakeFolder> folderList = folderDao.findAll();


		MyDocNode root = new MyDocNode(getRootFolder());
		root.setRoot(true);
		subListFilter(root, folderList, fileList);
		return root;
	}

	@Override
	public List<MyDocNode> retrieveDocFlatDir() {
		Map<Integer, FakeFolder> folderMap = getFolderMap();
		FakeFolder root = getRootFolder();
		List<MyDocNode> result = new ArrayList<MyDocNode>();
		result.add(new MyDocNode(root));
		result.addAll(buildFlatSubDir(root, folderMap, "/" + root.getFolderName() + "/"));
		return result;
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

	@Override
	public FakeFile getFileById(int fileId) {
		return fileDao.findById(fileId);
	}
	
	@Override
	public FakeFolder getRootFolder() {
		List<FakeFolder> root = folderDao.findByParentId(0);
		if (root.isEmpty()) {
			FakeFolder r = new FakeFolder();
			r.setFolderName("root");
			r.setPosition("1");
			r.setParentId(0);
			folderDao.insert(r);
			root = folderDao.findByParentId(0);
		}

		return root.get(0);
	}
	
	@Override
	public boolean moveFileToFolder(int fileId, int folderId) {
		FakeFile oriFile = fileDao.findById(fileId);
		FakeFolder oriFolder = folderDao.findById(oriFile.getFolderId());
		String oriFileName = oriFile.getFileName().replace(oriFolder.getPosition() + Config.DOC_NAME_SEP, "");
		boolean flag = false;
		if (fileDao.updateFolderId(fileId, folderId)){
			FakeFolder folder = folderDao.findById(folderId);
			String pref = folder.getPosition() + Config.DOC_NAME_SEP;
			File realFile = new File(Config.getUploadDocDir() + oriFile.getFileName());
			if (realFile.isFile()){
				File realDesFile = new File(Config.getUploadDocDir() + pref + oriFileName);
				if (realDesFile.exists()){
					realDesFile.delete();
				}
				
				realFile.renameTo(realDesFile);
				flag = fileDao.updateFileName(fileId, pref + oriFileName);
			}
		}
		return flag;
	}
	
	private void subListFilter(MyDocNode node, List<FakeFolder> folderList,
			List<FakeFile> fileList) {
		FakeFolder refFolder = null;
		for (FakeFolder f : folderList) {
			if (f.getId() == node.getKey()) {
				refFolder = f;
			}
			if (f.getParentId() == node.getKey()) {
				MyDocNode child = new MyDocNode(f);
				node.addChild(child);
				subListFilter(child, folderList, fileList);
			}
		}

		for (FakeFile f : fileList) {
			if (f.getFolderId() == node.getKey()) {
				node.addChild(new MyDocNode(f, refFolder));
			}
		}

		Collections.sort(node.getChildren(), node);
	}
	
	private List<MyDocNode> buildFlatSubDir(FakeFolder parent, Map<Integer, FakeFolder> folderMap, String prefix){
		List<MyDocNode> resultList = new ArrayList<MyDocNode>();
		MyDocNode tmp = null;
		
		Set<Integer> parentIdSet = new HashSet<Integer>();
		for (Entry<Integer, FakeFolder> entry: folderMap.entrySet()){
			parentIdSet.add(entry.getValue().getParentId());
		}
		
		for (Entry<Integer, FakeFolder> entry: folderMap.entrySet()){
			if (entry.getValue().getParentId() == parent.getId()){
				tmp = new MyDocNode(entry.getValue());
				tmp.setFolderName(prefix + tmp.getFolderName() + "/");
				resultList.add(tmp);
				if (parentIdSet.contains(tmp.getKey())){
					resultList.addAll(buildFlatSubDir(entry.getValue(), folderMap, prefix + entry.getValue().getFolderName() + "/"));
				}
			}
		}
		return resultList;
	}
	
	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
	
	
}
