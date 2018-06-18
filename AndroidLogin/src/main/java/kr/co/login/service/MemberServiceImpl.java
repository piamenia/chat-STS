package kr.co.login.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.login.dao.MemberDao;
import kr.co.login.domain.Member;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public Member login(HttpServletRequest request) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Member member = new Member();
		member.setEmail(email);
		member.setPassword(password);
		
		return memberDao.login(member);
	}

}
