package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseRecordDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    final RestClient restClient;

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<CourseRecordDTO> getAllCoursesByUser(Pageable pageable, UUID userId){
        String url =   baseUrlCourse + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");

        log.debug("Request URL:  {}", url);
        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDTO<CourseRecordDTO>> () {});

        } catch (RestClientException e){
            log.error("Error Request RestClient with cause: {}", e.getMessage());
            throw new RuntimeException("Error Request RestClient", e);
        }

    }

}
