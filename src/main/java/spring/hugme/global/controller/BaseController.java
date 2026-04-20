package spring.hugme.global.controller;

import org.springframework.stereotype.Controller;

/**
 * API Controller의 기본 클래스 모든 API 컨트롤러는 BaseApiController를 상속받아 /api/v1 prefix를 공통 적용한다.
 */
@Controller
public abstract class BaseController {

    public static final String API_V1 = "/api/v1";

    // 상속용 protected 생성자
    protected BaseController() {
    }

}
