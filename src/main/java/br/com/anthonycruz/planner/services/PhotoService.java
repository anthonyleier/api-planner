package br.com.anthonycruz.planner.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.anthonycruz.planner.config.FileStorageConfig;
import br.com.anthonycruz.planner.dtos.PhotoDTO;
import br.com.anthonycruz.planner.exceptions.FileStorageException;
import br.com.anthonycruz.planner.exceptions.PhotoNotFoundException;
import br.com.anthonycruz.planner.models.Trip;

@Service
public class PhotoService {
    private final Path fileStorageLocation;

    public PhotoService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadFolder()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where uploaded files will be stored", e);
        }
    }

    public void createFolder(String folderName) {
        try {
            Files.createDirectories(this.fileStorageLocation.resolve(folderName));
        } catch (Exception e) {
            throw new FileStorageException("Could not create the subdirectory", e);
        }
    }

    public String generateFilename(MultipartFile file, Trip trip) {
        createFolder(trip.getId().toString());

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) throw new FileStorageException("Sorry, filename cannot be null");

        String filename = trip.getId().toString() + "/" + StringUtils.cleanPath(originalFilename);
        if (filename.contains("..")) throw new FileStorageException("Sorry, filename contains invalid path sequence " + filename);

        return filename;
    }

    public void store(MultipartFile file, String filename) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + filename, e);
        }
    }

    public String upload(MultipartFile file, Trip trip) {
        String filename = this.generateFilename(file, trip);
        Path path = this.fileStorageLocation.resolve(filename).normalize();

        if (Files.exists(path)) throw new FileStorageException("Already exists a file with that name: " + file.getOriginalFilename());
        this.store(file, filename);

        return file.getOriginalFilename();
    }

    public Resource load(String filename, Trip trip) {
        try {
            String filepath = trip.getId().toString() + "/" + filename;
            Path path = this.fileStorageLocation.resolve(filepath).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists())
                return resource;
            else
                throw new PhotoNotFoundException("File not found " + filename);
        } catch (Exception e) {
            throw new PhotoNotFoundException("File not found " + filename, e);
        }
    }

    public List<PhotoDTO> getAllPhotosFromTrip(UUID tripId) {
        List<PhotoDTO> photos = new ArrayList<>();
        Path uploadFolder = this.fileStorageLocation.resolve(tripId.toString());

        if (Files.isDirectory(uploadFolder)) {
            try (Stream<Path> filStream = Files.list(uploadFolder)) {
                filStream.filter(Files::isRegularFile).forEach(path -> photos.add(new PhotoDTO(path.getFileName().toString())));
            } catch (IOException e) {
                throw new FileStorageException("Error reading files from directory " + tripId, e);
            }
        }
        return photos;
    }

    public void delete(String filename, Trip trip) {
        try {
            String filepath = trip.getId().toString() + "/" + filename;
            Path path = this.fileStorageLocation.resolve(filepath).normalize();

            if (Files.exists(path))
                Files.delete(path);
            else
                throw new PhotoNotFoundException("File not found " + filename);

        } catch (Exception e) {
            throw new PhotoNotFoundException("File not found " + filename, e);
        }
    }
}
