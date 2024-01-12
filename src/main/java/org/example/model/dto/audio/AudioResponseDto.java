package org.example.model.dto.audio;

import lombok.Data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Data
public class AudioResponseDto {
    private byte[] audioData;

}
