package org.trams.sicbang.service.implement;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trams.sicbang.model.entity.Attachment;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.entity.Wishlist;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceWishlist;

import java.util.Collection;

/**
 * Created by voncount on 22/04/2016.
 */
@Service
@Transactional
public class ServiceWishlist extends BaseService implements IServiceWishlist {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public Wishlist create(FormWishlist form) {
        String estateId = form.getEstateId();
        String userId = form.getUserId();

        User user = repositoryUser.findOne(Long.parseLong(userId));
        Estate estate = repositoryEstate.findOne(Long.parseLong(estateId));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setEstate(estate);

        if (repositoryWishlist.findOne(form.getSpecification()) != null) {
            throw new ApplicationException(MessageResponse.EXCEPTION_EXISTED);
        }
        logger.info("wishlist.toString() : " + wishlist.getEstate() + ", getCreatedDate :" + wishlist.getCreatedDate() + ", getUser:" + wishlist.getUser().getEmail());
        return repositoryWishlist.save(wishlist);
    }

    @Override
    public Wishlist update(FormWishlist form) {
        return null;
    }

    @Override
    public Page<Wishlist> filter(FormWishlist form) {
        Page<Wishlist> wishlists = repositoryWishlist.findAll(form.getSpecification(), form.getPaging());
        for (Wishlist wishlist : wishlists) {
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", wishlist.getEstate().getId());
            wishlist.getEstate().setAttachments(attachments);
            wishlist.getEstate().setWishlist(true);
//            logger.info("form.getSpecification() getEstateId : "+wishlist.getEstate().getId());
//            logger.info("form.getSpecification() getUserid : "+wishlist.getUser().getId());
        }
        return wishlists;
    }

    @Override
    public void delete(FormWishlist form) {
        Wishlist wishlist = repositoryWishlist.findOne(form.getSpecification());
        if (wishlist != null) {
            wishlist.setIsDelete(1);
        }
    }

    @Override
    public Wishlist findOne(FormWishlist form) {
        return repositoryWishlist.findOne(form.getSpecification());
    }

}
