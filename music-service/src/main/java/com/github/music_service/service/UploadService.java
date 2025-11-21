package com.github.music_service.service;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.IIOImage;

import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;


@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final SongRepository songRepository;
    private final UploadRepository uploadRepository;
    private final UserClientService userClientService;

    public Map<String, Long> uploadSong(UploadRequestDto dto) {

        // VALIDACIONES
        if (dto.getSongName() == null || dto.getSongName().isBlank())
            throw new IllegalArgumentException("Song name cannot be empty");

        if (dto.getSongPathBase64() == null || dto.getSongPathBase64().isBlank())
            throw new IllegalArgumentException("Song path cannot be null");

        if (dto.getSongDuration() == null || dto.getSongDuration() <= 0)
            throw new IllegalArgumentException("Invalid song duration");

        if (dto.getUserId() == null)
            throw new IllegalArgumentException("User ID cannot be null");

        // Validar existencia del usuario 
        String nickname = userClientService.getNicknameByUserId(dto.getUserId());
        if (nickname.equals("Desconocido")) {
            throw new IllegalArgumentException("User not found or unavailable in user-service");
        }

        

        // CREAR SONG
        Song song = new Song();
        song.setSongName(dto.getSongName());
        song.setSongDescription(dto.getSongDescription());
        song.setSongDuration(dto.getSongDuration());
        song.setCreationDate(dto.getCreationDate() != null ? dto.getCreationDate() : System.currentTimeMillis());

        song.setSongPathBase64(Base64.getDecoder().decode(dto.getSongPathBase64()));

        if (dto.getCoverArt() != null && !dto.getCoverArt().isBlank()) {
        byte[] original = Base64.getDecoder().decode(dto.getCoverArt());
        byte[] optimized = optimizeCoverArt(original);

    song.setCoverArt(optimized);
}

        Song savedSong = songRepository.save(song);

        // CREAR UPLOAD
        Upload upload = new Upload();
        upload.setUserId(dto.getUserId());
        upload.setSong(savedSong);
        upload.setStateId(1L);
        upload.setUploadDate(System.currentTimeMillis());

        Upload savedUpload = uploadRepository.save(upload);

        Map<String, Long> response = new HashMap<>();
        response.put("songId", savedSong.getIdSong());
        response.put("uploadId", savedUpload.getUploadId());

        return response;
    }

    private byte[] optimizeCoverArt(byte[] originalBytes) {
    try {
        // 1. Convertir byte[] a imagen
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));

        // 2. Redimensionar (máximo 300 px lado mayor)
        BufferedImage resizedImage = Scalr.resize(originalImage, 300);

        // 3. Convertir a JPG y comprimir (70%)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();

        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.7f);

        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new IIOImage(resizedImage, null, null), param);
        writer.dispose();

        return baos.toByteArray();

    } catch (Exception e) {
        throw new RuntimeException("Could not optimize image: " + e.getMessage());
    }
}


}

