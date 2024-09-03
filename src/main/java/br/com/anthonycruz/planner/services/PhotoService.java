package br.com.anthonycruz.planner.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import br.com.anthonycruz.planner.config.FileStorageConfig;
import br.com.anthonycruz.planner.exceptions.FileStorageException;
import br.com.anthonycruz.planner.models.Photo;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.PhotoRepository;

@Service
public class PhotoService {
    private final PhotoRepository repository;
    private final Path fileStorageLocation;

    public PhotoService(PhotoRepository repository, FileStorageConfig fileStorageConfig) {
        this.repository = repository;

        Path path = Paths.get(fileStorageConfig.getUploadFolder()).toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where uploaded files will be stored", e);
        }
    }

    public String generateFilename(MultipartFile file, Trip trip) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) throw new FileStorageException("Sorry, filename cannot be null");
        String filename = trip.getId() + StringUtils.cleanPath(originalFilename);
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

    public Photo upload(MultipartFile file, Trip trip) {
        String filename = this.generateFilename(file, trip);
        this.store(file, filename);

        Photo newPhoto = new Photo(file.getOriginalFilename(), trip);
        Photo savedPhoto = this.repository.save(newPhoto);
        return savedPhoto;
    }
}
