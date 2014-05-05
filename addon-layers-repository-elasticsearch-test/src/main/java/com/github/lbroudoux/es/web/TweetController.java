package com.github.lbroudoux.es.web;

import com.github.lbroudoux.es.domain.Tweet;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tweets")
@Controller
@RooWebScaffold(path = "tweets", formBackingObject = Tweet.class)
public class TweetController {
}
