package se.ifmo.is_lab1.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import se.ifmo.is_lab1.messages.collection.FeedResponse;
import se.ifmo.is_lab1.models.Feed;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.models.enums.Role;
import se.ifmo.is_lab1.repositories.FeedRepository;

import java.io.InputStream;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectStorageService {
    private final StudyGroupService studyGroupService;
    private final FeedRepository feedRepository;
    private final ModelMapper modelMapper;
    @Value("${minio.bucket.name}")
    private String bucketName;
    @Value("${minio.url}")
    private String minioUrl;


    private final MinioClient minioClient;


    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Feed feed = Feed.builder().build();
        try {
            feed = feedRepository.save(Feed.builder().user(user).build());
            String feedName = user.getId() + "/" + feed.getId() + "-" + file.getOriginalFilename();
            String feedUrl = minioUrl + "/" + bucketName + "/" + feedName;
            InputStream inputStream = file.getInputStream();
            Integer batchSize = studyGroupService.uploadBatch(file, user);
            feed = feed.toBuilder()
                    .feedUrl(feedUrl)
                    .batchSize(batchSize)
                    .isSuccessful(true)
                    .build();
            feedRepository.save(feed);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(feedName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (ConnectException e) {
            feed = feed.toBuilder()
                    .feedUrl("")
                    .isSuccessful(false)
                    .user(user)
                    .build();
            feedRepository.save(feed);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Minio is not available!");
        } catch (Exception e) {
            Throwable rootCause = com.google.common.base.Throwables.getRootCause(e);
            if (rootCause instanceof SQLException) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Database is not available ");
            }
            feed = feed.toBuilder()
                    .feedUrl("")
                    .isSuccessful(false)
                    .user(user)
                    .build();
            feedRepository.save(feed);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Failed to upload file: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getImportHistory(Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole() == Role.ADMIN) {
            return feedRepository.findAll(pageable)
                    .map(feed -> modelMapper.map(feed, FeedResponse.class));
        } else {
            return feedRepository.findAllByUser(user, pageable)
                    .map(feed -> modelMapper.map(feed, FeedResponse.class));
        }
    }
}
