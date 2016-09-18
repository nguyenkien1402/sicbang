package org.trams.sicbang.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.common.utils.FileUtils;
import org.trams.sicbang.config.ConfigParams;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormFile;
import org.trams.sicbang.repository.RepositoryAttachment;
import org.trams.sicbang.repository.RepositoryUser;
import org.trams.sicbang.service.IServiceFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by voncount on 5/19/16.
 */
@Service
public class ServiceFile implements IServiceFile {

    @Autowired
    protected ConfigParams configParams;
    @Autowired
    private RepositoryUser repositoryUser;
    @Autowired
    private RepositoryAttachment repositoryAttachment;

    @Override
    public Attachment userUploadAvatar(FormFile form) {
        User user = repositoryUser.findOne(Long.parseLong(form.getOwnerId()));
        if (user == null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }

        List<MultipartFile> attachments = form.getAttachments();
        if (attachments!= null && !attachments.isEmpty()) {
            try {
                MultipartFile file = attachments.get(0);
                String fileRelativePath[], fileUrl, thumbUrl;
                fileRelativePath = FileUtils.uploadImage(file.getInputStream(), configParams.UPLOAD_DIRECTORY, "/user/");
                fileUrl = configParams.BASE_URL + "/public" + fileRelativePath[0];
                thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];

                Attachment attachment;
                if (user.getAvatar() == null) {
                    attachment = new Attachment();
                } else {
                    attachment = repositoryAttachment.findOne(user.getAvatar().getId());
                }
                attachment.setOrigin(fileUrl);
                attachment.setThumbnail(thumbUrl);
                attachment.setTableRef("user");
                attachment.setRowRef(user.getId());

                repositoryAttachment.save(attachment);
                user.setAvatar(attachment);

                return attachment;
            } catch (IOException e) {
                throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
            }

        }
        return null;
    }

}
