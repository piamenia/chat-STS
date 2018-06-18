package kr.co.login.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.login.domain.Member;

@Repository
public class MemberDao {
	@Autowired
	private SqlSession sqlSession;
	
	public Member login(Member member) {
		return sqlSession.selectOne("member.login", member);
	}
}
