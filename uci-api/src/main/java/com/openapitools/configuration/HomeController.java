package com.openapitools.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
//@CrossOrigin(origins = "http://xxx.xxx.xxx.xxx:xxxx", methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE, RequestMethod.OPTIONS})
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}