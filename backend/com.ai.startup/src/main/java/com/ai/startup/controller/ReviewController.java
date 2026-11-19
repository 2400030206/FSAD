package com.ai.startup.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

@GetMapping("/answers")
public String answers(){

return "Simple explanation why answer is correct.";
}
}
