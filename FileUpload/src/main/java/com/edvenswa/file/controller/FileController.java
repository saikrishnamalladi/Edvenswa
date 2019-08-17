package com.edvenswa.file.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edvenswa.file.entity.File;
import com.edvenswa.file.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	
	@Autowired
	FileService fileService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/api/upload")
	public ResponseEntity<?> uploadFile(
			@RequestParam("file") MultipartFile uploadfile) {
		LOGGER.info("uploading file..."+uploadfile.getName());
		if (uploadfile.isEmpty()) {
			return new ResponseEntity("please select a file!", HttpStatus.OK);
		}
		try {
			fileService.storeFile(uploadfile);
			LOGGER.info("File uploaded successfully...");
		} catch (Exception e) {
			LOGGER.error("Exception during file upload:::"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity("Successfully uploaded - " + uploadfile.getOriginalFilename(), new HttpHeaders(),
				HttpStatus.OK);

	}
	
	@PostMapping("/api/search")
	public String searchFiles(@RequestParam("fileName") String fileName) {
		if (fileName.length() == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append("<ul id=\"files-list\">");
		List<String> fileNames= fileService.getFileNames(fileName);
		for (String string : fileNames) {
			builder.append("<li onClick=\"selectFileName('"+string+"');\">"+string+"</li>");
		}
		builder.append("</ul>");
		return builder.toString();
	}
	
	@PostMapping("/api/download")
	public void downloadFile(@RequestParam("fileName") String fileName,HttpServletRequest request,HttpServletResponse response) {
		File file = fileService.getFile(fileName);
		LOGGER.info("file download type..."+file.getFileType());
		LOGGER.info("file download length..."+file.getData().length);
		response.setContentType(file.getFileType());
		response.setHeader("Content-Disposition", "filename="+file.getFileName());
		response.setContentLength(file.getData().length);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(file.getData() , 0, file.getData().length);
		} catch (Exception exc) {
		   exc.printStackTrace();
		} finally {
		    try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	   @GetMapping("/api/downloadFile/{fileName}")
	   public ResponseEntity<ByteArrayResource> downloadFile2(@PathVariable("fileName") String fileName) throws IOException {
		   File file = fileService.getFile(fileName);
			LOGGER.info("file download type..."+file.getFileType());
			LOGGER.info("file download length..."+file.getData().length);
	      ByteArrayResource resource = new ByteArrayResource(file.getData());
	      return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                  "attachment;filename=" + file.getFileName())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.getData().length)
	            .body(resource);
	   }
}
