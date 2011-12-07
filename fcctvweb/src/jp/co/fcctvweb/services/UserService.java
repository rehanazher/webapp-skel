package jp.co.fcctvweb.services;

import jp.co.fcctvweb.po.User;

public interface UserService {

	User userLogin(String username, String password, String remoteIp);

}
