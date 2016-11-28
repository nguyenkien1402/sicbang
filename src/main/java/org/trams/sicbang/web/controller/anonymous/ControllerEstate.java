package org.trams.sicbang.web.controller.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.exception.FormError;
import org.trams.sicbang.model.form.FormEstate;
import org.trams.sicbang.model.form.FormRecent;
import org.trams.sicbang.model.form.FormWishlist;
import org.trams.sicbang.service.implement.ServiceAuthorized;
import org.trams.sicbang.web.controller.AbstractController;

import java.util.*;

/**
 * Created by voncount on 15/04/2016.
 */
@Controller(value = "anonymousControllerEstate")
@RequestMapping(value = "/estate")
public class ControllerEstate extends AbstractController {

    final String BASE_URL = "/estate/";
    final String BASE_TEMPLATE = "web/content/estate/";
    final String BASE_TEMPLATE_BROKER = "web/content/broker";

    /**
     * Filter
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(ModelMap map) {
        initPage(map);
        map.put("estateType","%%");
        return BASE_TEMPLATE + "map-all";
    }


    /**
     * Redirect to map-all
     * @param map
     * @param formEstate
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String goEstatePage(ModelMap map,@ModelAttribute FormEstate formEstate) {
        initPage(map);
        map.put("estateType","%%");
        map.put("redirect","true");
        map.put("city",formEstate.getCity());
        map.put("district",formEstate.getDistrict());
        map.put("town",formEstate.getTown());
        map.put("subway",formEstate.getSubwayStation());
        map.put("registryNo",formEstate.getEstateCode());
        return BASE_TEMPLATE + "map-all";
    }


    // init all model in map page
    private void initPage(ModelMap map){

        List<BusinessType> businessTypes = serviceBusinessType.findAll();
        List<BusinessType> eatery = new ArrayList<>();
        List<BusinessType> restaurants =new ArrayList<>();
        List<BusinessType> liquors = new ArrayList<>();

        for(BusinessType tmp : businessTypes){
            switch (tmp.getCategory().getId().toString()) {
                case "1": eatery.add(tmp); break;
                case "2": restaurants.add(tmp); break;
                case "3": liquors.add(tmp); break;
            }
        }

        map.put("eatery",eatery);
        map.put("restaurants",restaurants);
        map.put("liquors",liquors);
    }

    /**
     * Detail
     * @param estateId
     * @param map
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String detail(
            @PathVariable(value = "id") String estateId, ModelMap map) {

        FormEstate estateForm = new FormEstate();
        estateForm.setIsApproved("1");
        estateForm.setEstateId(estateId);
        System.out.println(estateId);
        Authentication auth = serviceAuthorized.isAuthenticated();
        Estate estate = serviceEstate.findOne(estateForm);
        Collection<Attachment> listAttach = estate.getAttachments();
        System.out.println("list attach: " +listAttach.size());
        map.put("attachments", listAttach);
        map.put("estate",estate);
        map.put("sizeattach", listAttach.size());
        //if logged as member
        if(auth != null){

            User user = getUserSession();
            FormWishlist formWishlist = new FormWishlist();
            formWishlist.setUserId(user.getId().toString());
            formWishlist.setEstateId(estateId);
            Wishlist wishlist = serviceWishlist.findOne(formWishlist);
            if(wishlist != null){
               map.put("isWishList","true");
            }else{
                map.put("isWishList","false");
            }
            map.put("user",user);
            map.put("memberId",user.getId());
            FormRecent formRecent = new FormRecent();
            formRecent.setUserId(user.getId().toString());
            formRecent.setEstateId(estateId);
            Recent recent = serviceRecent.findOne(formRecent);
            System.out.println(user.getType());
            if(recent != null){
            serviceRecent.update(formRecent);
            }else{
            serviceRecent.create(formRecent);
            }
            if(user.getId() == estate.getUser().getId()){
                if(user.getPermission().getName().equals("TRUSTED_BROKER")){ // if estate is premium.
                    return BASE_TEMPLATE_BROKER +"/broker-content-premium";
                }
                // estate isn't premium.
                System.out.println("go for broker-content");
                map.put("broker_type",user.getPermission().getName());
                return BASE_TEMPLATE_BROKER +"/broker-content";
            }

        }
        // anonymous
        if(estate.getEstateType().equals("STARTUP")){
            System.out.println("go for startup");
            return BASE_TEMPLATE +"/detail-startup";
        }else{
            System.out.println("go for vacant");
            return BASE_TEMPLATE +"/detail-vacant";
        }

    }
    /**
     * Update
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String update(
            @PathVariable(value = "id") String id,
            ModelMap map) {

        return "redirect:" + BASE_URL;
    }

    /**
     * Delete
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    public String delete(
            @PathVariable(value = "id") String id,
            ModelMap map) {
        return "redirect:" + BASE_URL + id;
    }

    /**
     * Create
     * @param form
     * @return
     */
    @RequestMapping(value = "/create", method = {RequestMethod.GET, RequestMethod.POST})
    public Object create(
            @ModelAttribute FormEstate form
    ) {
        switch (getRequestMethod()) {
            case POST:
                // TODO validate form
                FormError error = null;
                if (error != null) {
                    return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
                }
                // TODO call create service
                return new ResponseEntity(HttpStatus.OK);
            case GET:
            default:
                return BASE_TEMPLATE + "create";
        }
    }
    /**
     * Search estate
     * @param formEstate
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity searchEstate(@ModelAttribute FormEstate formEstate){
        if(!formEstate.getTown().equals("")){
            formEstate.setCity(null);
            formEstate.setDistrict(null);
        }
        else if(!formEstate.getDistrict().equals("")){
            formEstate.setCity(null);
            formEstate.setTown(null);
        }
        List<Estate> estates = serviceEstate.filterBy(0,formEstate.getCity(),formEstate.getDistrict(),formEstate.getTown(),formEstate.getEstateType(),formEstate.getSubwayStation(),"1");

        return new ResponseEntity(estates,HttpStatus.OK);
    }

    /**
     * Search By Business Type
     * @param formEstate
     * @return
     */
    @RequestMapping(value = "/search/business", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity searchEstateByBusiness(@ModelAttribute FormEstate formEstate){
        if(formEstate.getBusinessType().equals("")){
            formEstate.setBusinessType(null);
        }
        if(formEstate.getSubwayStation().equals("")){
            formEstate.setSubwayStation(null);
        }
        if(formEstate.getEstateCode().equals("")){
            formEstate.setEstateCode(null);
        }

        //If subway station not null, search by subway.
        if(formEstate.getSubwayStation() != null){
            formEstate.setCity(null);
            formEstate.setDistrict(null);
            formEstate.setTown(null);
            formEstate.setEstateCode(null);
        }else if(formEstate.getEstateCode() != null){ // If Registry No not null, search by Registry No.
            formEstate.setCity(null);
            formEstate.setDistrict(null);
            formEstate.setTown(null);
            formEstate.setSubwayStation(null);
        }
        else{ // search by City,District,Town
            if(formEstate.getCity().equals("")){
                formEstate.setCity(null);
            }
            if(formEstate.getDistrict().equals("")){
                formEstate.setDistrict(null);
            }
            if(formEstate.getTown().equals("")){
                formEstate.setTown(null);
            }
        }
        formEstate.setIsApproved("1");
        List<Estate> estates = null;

        /*
         - Check estate type, if estateType = %%, search all estate not by estateType.
        */
        if(formEstate.getEstateType().equals("%%")){
            formEstate.setEstateType(null);
            if(formEstate.getBusinessType()!= null){
                estates = serviceEstate.filterEstateOnMap(formEstate);
                formEstate.setBusinessType(null);
                formEstate.setEstateType("VACANT");
                List<Estate> estate2 = serviceEstate.filterEstateOnMap(formEstate);
                estates.addAll(estate2);
            }else{
                estates = serviceEstate.filterEstateOnMap(formEstate);
            }
            return new ResponseEntity(estates,HttpStatus.OK);
        }else if(formEstate.getEstateType().equals("VACANT")){ // if estateType == Vacant, search estate by VACANT
            formEstate.setBusinessType(null);
            formEstate.setCategory(null);
        }
        // Search Estate by StartUp
        estates = serviceEstate.filterEstateOnMap(formEstate);
        return new ResponseEntity(estates,HttpStatus.OK);
    }

    /**
     * Get top 10 estate per type when load map-all page
     * @param formEstate
     * @return
     */
    @RequestMapping(value = "/map", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity estateMap(@ModelAttribute FormEstate formEstate){

        List<Estate> trusted = serviceEstate.filterEstateByType(10,"TRUSTED_STARTUP",formEstate.getEstateType());
        List<Estate> broker = serviceEstate.filterEstateByType(10,"REALTOR",formEstate.getEstateType());
        List<Estate> member = serviceEstate.filterEstateByType(10,"DIRECT_DEAL",formEstate.getEstateType());

        Map<String,List> estates = new HashMap<>();
        estates.put("trusted",trusted);
        estates.put("broker",broker);
        estates.put("member",member);
        return new ResponseEntity(estates,HttpStatus.OK);
    }

    /**
     *
     * Get all city
     * @return
     */
    @RequestMapping(value = "/getAllCity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity getAllCity(){
        List<City> cities = new ArrayList();
        try {
           cities = serviceLocation.findAllCity();
        }catch(Exception e) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(cities,HttpStatus.OK);
    }
    /**
     *
     * Get all town
     * @Param districtId
     * @return
     */
    @RequestMapping(value = "/getAllTown/{districtId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity getAllTown(@PathVariable(value = "districtId") int id){
        List<Town> towns = serviceLocation.findAllTown(id);
        return new ResponseEntity(towns,HttpStatus.OK);
    }
    /**
     * redirect to startup map
     * @param map
     * @return
     */
    @RequestMapping(value="/startup",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String estateStartup(ModelMap map) {
        initPage(map);
        map.put("estateType","STARTUP");
        return BASE_TEMPLATE + "map-all";
    }

    /**
     * redirect to vacant map
     * @param map
     * @return
     */
    @RequestMapping(value="/vacant",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String estateVacant(ModelMap map) {
        initPage(map);
        map.put("estateType","VACANT");
        return BASE_TEMPLATE + "map-all";
    }

    @RequestMapping(value = "/search/getDataDragEndOrZoomChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity getDataWhenDragEnd(@ModelAttribute FormEstate formEstate,int zoomLevel){
        List<Estate> estates = new ArrayList<>();
        if(formEstate.getEstateType().equals("%%")){
            formEstate.setEstateType(null);
            if(formEstate.getBusinessType()!= null){
                estates = serviceEstate.filterEstateOnMap(formEstate);
                formEstate.setBusinessType(null);
                formEstate.setEstateType("VACANT");
                List<Estate> estate2 = serviceEstate.filterEstateOnMap(formEstate);
                estates.addAll(estate2);
            }else{
                estates = serviceEstate.filterEstateOnMap(formEstate);
            }
            estates = filterEstateByRadius(estates,formEstate.getLongitude(),formEstate.getLatitude(),zoomLevel);
            return new ResponseEntity(estates,HttpStatus.OK);
        }else if(formEstate.getEstateType().equals("VACANT")){ // if estateType == Vacant, search estate by VACANT
            formEstate.setBusinessType(null);
            formEstate.setCategory(null);
        }
        // Search Estate by StartUp
        estates = serviceEstate.filterEstateOnMap(formEstate);
        estates = filterEstateByRadius(estates,formEstate.getLongitude(),formEstate.getLatitude(),zoomLevel);
        return new ResponseEntity(estates,HttpStatus.OK);
    }

    private List<Estate> filterEstateByRadius(List<Estate> estates,String longitude,String latitude,int zoomLevel){
        List<Estate> estateFilter = new ArrayList<>();
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        double radius = getDistanceByZoomLevel(zoomLevel);
        for(Estate estate: estates){
            double lat2 = Double.parseDouble(estate.getLatitude());
            double lng2 = Double.parseDouble(estate.getLongitude());
            if(getDistanceFromLatLonInKm(lat,lng,lat2,lng2) <= radius){
                estateFilter.add(estate);
            }
        }
        return estateFilter;
    }

    private double getDistanceFromLatLonInKm(double lat1,double lng1,double lat2,double lng2) {
        long R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    private double getDistanceByZoomLevel(int zoomLevel){
        switch (zoomLevel){
            case 1: return 0.3;
            case 2: return 0.45;
            case 3: return 0.75;
            case 4: return 1.5;
            case 5: return 3;
            case 6: return 6;
            case 7: return 10;
            case 8: return 20;
            case 9: return 50;
            case 10: return 80;
            case 11: return 140;
            case 12: return 288;
            case 13: return 400;
            case 14: return 500;
            default:return 0;
        }
    }
}
