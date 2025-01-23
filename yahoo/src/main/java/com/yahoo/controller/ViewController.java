package com.yahoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/template")
  public String getChartPage() {
      return "chart";
  }
}
