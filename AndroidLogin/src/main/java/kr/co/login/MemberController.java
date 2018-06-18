package kr.co.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.co.login.domain.Member;
import kr.co.login.service.MemberService;

@RestController
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public Map<String,Object> login(HttpServletRequest request) {
		Member member = memberService.login(request);
		if(member == null)
			member = new Member();
		Map<String, Object> map = new HashMap<>();
		map.put("member", member);
		return map;
	}
}
