package com.imageservice.controller;


import com.imageservice.model.ImageModel;
import io.swagger.v3.oas.annotations.Operation;
import com.imageservice.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@Tag(name = "Изображения", description = "Сервис для обработки и хранения закодированных изображений")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/{id}")
    @Operation(summary = "Сохрание изображения", description = "Вызываемый метод кодирует изображение и записывает его в бд вместе с id связанного работника")
    public ResponseEntity<ImageModel> saveImage(@PathVariable String id, @RequestParam("image") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.saveImageFile(Long.valueOf(id), file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение изображения", description = "Вызываемый метод раскодирует и возвращает изображение")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] image = imageService.getImageFile(id);
        if (image != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Метод удаления изображения")
    public ResponseEntity<Void> deleteWorker(@PathVariable("id") Long id) {
        imageService.deleteImageByWorkerId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение изображения")
    public ResponseEntity<ImageModel> editImage(@PathVariable String id, @RequestParam("image") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.editImage(Long.valueOf(id), file));
    }
}
