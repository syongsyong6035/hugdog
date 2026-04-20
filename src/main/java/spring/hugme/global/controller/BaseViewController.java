package spring.hugme.global.controller;

import org.springframework.stereotype.Controller;

/**
 * View Controller의 공통 기능
 */

@Controller
public abstract class BaseViewController {

    public static final String VIEW_PREFIX = "/view";

    // 인스턴스화 방지
    protected BaseViewController() {
    }
}

