package com.example.rafik.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.rafik.model.FileTransfer;
import com.example.rafik.repository.FileRepository;

@Service
public class FileService {
	
	@Autowired
	public FileRepository fileRepository;
	
	public FileTransfer saveFile(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    String fileType = file.getContentType();
	    FileTransfer fileModel = new FileTransfer();
	    fileModel.setName(fileName);
	    fileModel.setType(fileType);
	    try {
	        fileModel.setData(file.getBytes());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return fileRepository.insert(fileModel); // Use insert instead of save to generate a new ID
	}
	public FileTransfer getFile(String id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found with id " + id));
    }

}
