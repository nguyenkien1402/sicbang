package org.trams.sicbang.service;

import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.form.FormFile;

/**
 * Created by voncount on 5/19/16.
 */
public interface IServiceFile {

    Attachment userUploadAvatar(FormFile form);

}
