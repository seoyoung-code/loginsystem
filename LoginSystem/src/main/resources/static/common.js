//common.js
async function login(mid, mpw){//로그인 함수 분리
	const logindata={mid,mpw}
	const ctype="application/json"
	const res =await fetch("/member/login",{method:"post",headers:{"Content-type":ctype},body:JSON.stringify(logindata)})
	const data= await res.json();
	return data;		
	
}
function logout(logoutui){//로그아웃 함수 분리
	logoutui.attr("href","/member/logout").text("로그아웃")//링크의 /member/logout으로 변경되고 로그아웃을 출력하도록한다.
	logoutui.click(async function(evt){//<a>태그가 클릭을 감지함. 클릭을 하게 된다면 함수 실행
				if($(this).text()=="로그아웃"){
					evt.preventDefault();//<a>의 태그가 값을 전송하면서 해당 페이지를 새로고침하지 않도록 막기위해 사용
					const res= await fetch($(this).attr("href"))///member/logout/일때 서버로 http요청을 보냄. 
					const data= await res.json();//서버를 json을 파싱. data에 저장
					alert(data.message)//서버에서 받은data.message를 표시한다.
					location.href="/";//현재 브라우저의url을 변경하여 /메인페이지로이동
					return false;
				}
			})
	
}