package com.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AndroidApp implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String appLabel;


    private String versionName;
    private String versionCode;
    private String uploadPath;
    private String fileType;

    @Lob
    private byte[] data;

    public AndroidApp(String appLabel, String versionName, String versionCode,String uploadPath, String fileType, byte[] data) {
        this.appLabel = appLabel;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.uploadPath = uploadPath;
        this.fileType = fileType;
        this.data = data;
    }
}
