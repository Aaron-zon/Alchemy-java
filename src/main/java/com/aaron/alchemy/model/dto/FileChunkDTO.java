package com.aaron.alchemy.model.dto;

import lombok.Data;

@Data
public class FileChunkDTO {
    private String fileName;
    private long start;
    private long end;
    private long total;
    private String chunkName;
    private String fileHash;
    private int index;
    private int chunkNum;
    private String status;
}
