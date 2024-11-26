package com.chensoul.security.spring.userlocation;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/location")
public class UserLocationController {
    private final UserLocationService userLocationService;
    private final MessageSource messages;

    @GetMapping(value = "/enable")
    public String enableNewLoc(Locale locale, Model model, @RequestParam("token") String token) {
        final String loc = userLocationService.checkNewLocationToken(token);
        if (loc!=null) {
            model.addAttribute("message", messages.getMessage("message.newLoc.enabled", new Object[]{loc}, locale));
        } else {
            model.addAttribute("message", messages.getMessage("message.error", null, locale));
        }
        return "redirect:/login?lang=" + locale.getLanguage();
    }
}
