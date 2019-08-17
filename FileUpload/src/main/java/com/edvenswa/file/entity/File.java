package com.edvenswa.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="file")
@NamedQuery(name = "File.fetchByFileName",
query = "SELECT fileName FROM File f WHERE lower(f.fileName) LIKE lower(CONCAT('%',:fileName,'%'))")
public class File {

	@Id
	@Column
    private String fileName;
	
	@Column
    private String fileType;

    @Lob
    private byte[] data;
    
    public File() {
    	
    }
    public File(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


}
