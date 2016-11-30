package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.form.FormEstate;

import java.util.List;

/**
 * Created by voncount on 4/13/2016.
 */
public interface IServiceEstate {

    /**
     * Create
     *
     * @param form
     * @return
     */
    Estate create(FormEstate form);

    /**
     * Update
     *
     * @param form
     * @return
     */
    Estate update(FormEstate form);

    /**
     * Find one
     *
     * @param form
     * @return
     */
    Estate findOne(FormEstate form);

    /**
     * Filter
     *
     * @param form
     * @return
     */
    Page<Estate> filter(FormEstate form);

    /**
     * @param form
     * @param user
     * @return
     */
    Page<Estate> filter(FormEstate form, CustomUserDetail user);

    Page<Estate> filterAddr(FormEstate formEstate);

    /**
     * Delete
     * @param form
     */
    void delete(FormEstate form);

    /*
    * Update
    * @Param form
    */
    Estate updateEstateType(FormEstate form, String type);

    /*
    * Update Estate
     */
    Estate updateEstate(FormEstate form, Estate estate);

    Integer updateImageEstate(MultipartFile file, Estate estate);

//    List<Estate> filterByCity(int pageIndex, String name);
//    List<Estate> filterByDistrict(int pageIndex, String name);
//    List<Estate> filterByTown(int pageIndex, String name);
//    List<Estate> filterBySubway(int pageIndex, String name);

    List<Estate> filterBy(int pageIndex, String city, String district, String town, String type,String subway);

    Long totalEstateFilter(String city, String district, String town, String type, String subway);

    List<Estate> filterEstateByType(int pageSize,String typeTrust,String type);

    List<Estate> filterEstateOnMap(FormEstate form);

    List<Estate> filterBy(int pageIndex, String city, String district, String town, String type,String subway, String approved);

    Long totalEstateFilter(String city, String district, String town, String type, String subway, String approved);

//    Long totalEstateFilterByCity(String name);
//    Long totalEstateFilterByDistrict(String name);
//    Long totalEstateFilterByTown(String name);
//    Long totalEstateFilterBySubway(String name);

    Integer changeStatus(String id, String status);

    Estate changeAdvertisedEstate(FormEstate formEstate,String isAdvertised);

    List<Estate> filterWithZoom(FormEstate formEstate,String zoomLevel);
}
