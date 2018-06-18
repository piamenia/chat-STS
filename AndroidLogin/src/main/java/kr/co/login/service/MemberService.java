package kr.co.login.service;

import javax.servlet.http.HttpServletRequest;

import kr.co.login.domain.Member;

public interface MemberService {
	public Member login(HttpServletRequest request);
}
