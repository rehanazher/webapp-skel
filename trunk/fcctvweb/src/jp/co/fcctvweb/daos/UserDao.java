package jp.co.fcctvweb.daos;

import java.util.Date;
import java.util.List;

import jp.co.fcctvweb.po.User;

public interface UserDao {

	List<User> findAll();

	String delete(String userId);

	String update(User user);

	User findById(String userId);

	User findByRemoteIp(String remoteIp);

	User findByUsernameAndPassword(String username, String password);

	boolean updateLastLoginById(Date loginTime, String userId);

	boolean updateRemoteIpById(String ip, String userId);

	boolean updateLastLoginByUsername(String username, Date loginTime);
}
