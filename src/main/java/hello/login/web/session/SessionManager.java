package hello.login.web.session;

import org.apache.catalina.SessionIdGenerator;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore= new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * session Id 생성
     * 세션저장소에 아이디와 값 저장
     * sessionId로응답쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response){

        //id를 생성하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);

    }

    /**
     * 세션조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie ==null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     * @param request
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie!=null){
            sessionStore.remove(sessionCookie.getValue());
        }

    }

    /**
     *
     * @param request
     * @param cookieName
     * @return
     */

    public Cookie findCookie(HttpServletRequest request,String cookieName){
        ;
        if(request.getCookies()==null){
            return null;
        }
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
