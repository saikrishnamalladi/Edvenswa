package com.edvenswa.file.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.edvenswa.file.entity.File;
import com.edvenswa.file.repository.FileRepository;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

	@Autowired
	FileRepository fileRepository;

	public File storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		LOGGER.info("fileName:::"+fileName);
		try {
			File entry = new File(fileName, file.getContentType(), file.getBytes());
			return fileRepository.save(entry);
		} catch (IOException ex) {
			return null;
		}
	}

	public List<String> getFileNames(String fileName) {
		LOGGER.info("getFileNames:::"+fileName);
		return fileRepository.fetchByFileName(fileName);
	}
	
	public File getFile(String fileName) {
		LOGGER.info("getFile:::"+fileName);
		return fileRepository.findByFileName(fileName);
	}

}
