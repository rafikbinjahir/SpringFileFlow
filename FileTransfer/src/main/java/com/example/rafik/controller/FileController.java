package com.example.rafik.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.rafik.model.FileTransfer;
import com.example.rafik.service.FileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="/filetransfer")
public class FileController {

	  @Autowired
	  private FileService fileService;
	  
	  @PostMapping("/upload")
	    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        String fileType = file.getContentType();

	        // Check if the file is a photo or a PDF
	        if (fileType.startsWith("image/") || fileType.equals("application/pdf")) {
	            return fileService.saveFile(file).getId();
	        } else {
	            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only photos and PDF files are accepted.");
	        }
	    }
	  @GetMapping("/download/{id}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String id, HttpServletRequest request) {
		  FileTransfer fileModel = fileService.getFile(id);
		  String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(fileModel.getName());
	        } catch (Exception ex) {
	            System.out.println("Could not determine file type.");
	        }
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
	                .body(new ByteArrayResource(fileModel.getData()));
	    }
}
