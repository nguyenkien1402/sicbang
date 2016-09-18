package org.trams.sicbang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by voncount on 15/04/2016.
 */
@Component
public class ConfigParams {

    @Value("${base.url}")
    public String BASE_URL;
//    @Value("${upload.directory}")
    @Value("${multipart.location}")
    public String UPLOAD_DIRECTORY;

}
