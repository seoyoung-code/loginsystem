package com.itg.examp;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itg.examp.dao.MemberDAO;
import com.itg.examp.dto.MemberDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@RestController//기본값으로 데이터 만들기 실시간통신.
@RequestMapping("/member")//주소라우팅 //url주소
public class MemberController {
	@Autowired
	MemberDAO dao;
	@Autowired
	MemberService service;
	@PostMapping("/signup")//mapping 이쪽으로 들어오도록함. 
	public Map<String, Object> signupMember(MemberDTO member) {
		//id는 4글자 이상 pw는 6글자이상으로 입력되도록
		
		System.out.println(member.toString());
		boolean ckid=service.checkId(member.getMid());
		boolean ckpw=service.checkPassword(member.getMpw());
		HashMap<String, Object>hm=new HashMap<>();
		if(!ckid) {
			hm.put("message","아이디를 점검하세요");
			return hm;
		}
		if(!ckpw) {
			hm.put("message", "비밀번호를 점검하세요");
			return hm;
			
		}
		int res=dao.signupMember(member);
		
		if(res==1) {
			member.setMpw("");
			hm.put("message", "회원가입성공");
			hm.put("data", member);
			System.out.println(member.getName()+"회원가입 성공");
		}else {
			hm.put("message","회원가입실패 관리자 문의");
			System.out.println("회원가입실패");
		}
		return hm;
	
		
	}
	@PostMapping("/login")//서버에 리소스 등록, 저장
	public Map signin(HttpServletRequest request, @RequestBody Map<String, String> logindata) {
		
		System.out.println(logindata);
		HashMap<String, Object> hm=new HashMap<>();
		//로그인 검증
		MemberDTO member= dao.signinMember(logindata);
		if(member!=null) {
			HttpSession auth= request.getSession(true);
			auth.setAttribute("mid", member.getMid());//세션에 아이디 설정
			//auth.getAttribute("mid");//세션에서 아이디 출력
			hm.put("message", "로그인성공");
			hm.put("member", member);
		}else {
			hm.put("message", "아이디와 비밀번호를 다시 확인하세요");
		}
		
		return hm;
		
	}
	@GetMapping("/logout")//서버의 리소스 조회
	public HashMap<String, Object> signout(HttpServletRequest request,HttpServletResponse response) {
		HttpSession auth= request.getSession();
		HashMap<String, Object>hm=new HashMap<>();
		hm.put("message",auth.getAttribute("mid")+"님 로그아웃 되었습니다.");
		auth.invalidate();
		
		//response.sendRedirect("/");
		return hm;
		
	}
	@GetMapping("/listview")
	public Map listview(HttpServletRequest request) {
		HttpSession auth= request.getSession();
		HashMap<String, Object>hm= new HashMap<>();
		if(auth==null) {
			hm.put("message", "로그인을 먼저 해주세요");
			
		}else {
			 List<MemberDTO> ll = dao.memberList();
			 System.out.println(auth.getAttribute("mid")+"님이 회원 리스트 요구");
			 hm.put("message", "리스트수신");
			 hm.put("members", ll);
					
		}
		return hm;
	}
	
}