package com.proto.mm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proto.mm.service.PosterService;

@Controller
public class PosterController {
	
	@Autowired
	PosterService posterService;
	
	@GetMapping("download")
    public void downLoad(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		posterService.posterDownload(model, request, response);

    }
}


