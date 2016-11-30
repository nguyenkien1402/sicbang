package org.trams.sicbang.service.implement;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.trams.sicbang.common.utils.ConvertUtils;
import org.trams.sicbang.common.utils.FileUtils;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.*;
import org.trams.sicbang.model.enumerate.EstateType;
import org.trams.sicbang.model.enumerate.EstateTypeTrust;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.model.form.*;
import org.trams.sicbang.service.BaseService;
import org.trams.sicbang.service.IServiceEstate;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by voncount on 4/14/2016.
 */
@Service
@Transactional
public class ServiceEstate extends BaseService implements IServiceEstate {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Estate create(FormEstate form) {

        Estate estate = new Estate();
        BeanUtils.copyProperties(form, estate);

        logger.info("estateType : " + form.getEstateType());

        EstateType estateType = EstateType.valueOf(form.getEstateType());
        estate.setEstateType(form.getEstateType());
        //모든 주소를 받아 저장하는걸로 변경

        // generate Estate Code
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String estateCode = simpleDateFormat.format(new Date()) + form.getUserId();
        estate.setEstateCode(estateCode);
        logger.info("estateCode : " + estate.getEstateCode());

        estate.setUser(repositoryUser.findOne(ConvertUtils.toLongNumber(form.getUserId()).get()));

        logger.info("all_addr : " + form.getAll_addr());
        //공통 입력사항
        estate.setLatitude(form.getLatitude());
        estate.setLongitude(form.getLongitude());
        estate.setAll_addr(form.getAll_addr());
        estate.setBusinessZone(form.getBusinessZone());
        estate.setFloor(form.getFloor());
        estate.setParking(ConvertUtils.toBoolean(form.getParking()).get());
        estate.setDepositeCost(ConvertUtils.toDoubleNumber(form.getDepositeCost()).get());
        estate.setRentCost(ConvertUtils.toDoubleNumber(form.getRentCost()).get());
        estate.setPremiumCost(ConvertUtils.toDoubleNumber(form.getPremiumCost()).get());
        System.out.println("city: " +form.getCity());
        System.out.println("district: "+form.getDistrict());
        System.out.println("town: "+form.getTown());
        if(!Strings.isNullOrEmpty(form.getCity())){
            FormCity formCity = new FormCity();
            formCity.setName(form.getCity());
            City city = repositoryCity.findOne(formCity.getSpecification());
            System.out.println("city id: "+city.getId() + "-"+"city name: "+city.getName());
            estate.setCity(city);
        }
        if(!Strings.isNullOrEmpty(form.getDistrict())){
            FormDistrict formDistrict = new FormDistrict();
            formDistrict.setName(form.getDistrict());
            District district = repositoryDistrict.findOne(formDistrict.getSpecification());
            System.out.println("city id: "+district.getId() + "-"+"city name: "+district.getName());
            estate.setDistrict(district);
        }
        if(!Strings.isNullOrEmpty(form.getTown())){
            FormTown formTown = new FormTown();
            formTown.setName(form.getTown());
            Town town = repositoryTown.findOne(formTown.getSpecification());
            System.out.println("city id: "+town.getId() + "-"+"city name: "+town.getName());
            estate.setTown(town);
        }
        User user = repositoryUser.findOne(Long.parseLong(form.getUserId()));
        switch (user.getPermission().getName()){
            case "MEMBERSHIP":
                estate.setTypeTrust(EstateTypeTrust.DIRECT_DEAL.name());
                break;
            default:
                estate.setTypeTrust(EstateTypeTrust.REALTOR.name());
                break;
        }
        //add city,district
//        estate.setCity(repositoryCity.findOne(ConvertUtils.toLongNumber(form.getCity()).get()));
//        estate.setDistrict(repositoryDistrict.findOne(ConvertUtils.toLongNumber(form.getDistrict()).get()));
        logger.info("estateType : " + estateType + "getEstateType : " + form.getEstateType());


        switch (form.getEstateType()) {
            case "STARTUP":
                estate.setOtherCost(ConvertUtils.toDoubleNumber(form.getOtherCost()).get());
                estate.setMonthlyIncome(ConvertUtils.toDoubleNumber(form.getMonthlyIncome()).get());
                estate.setMonthlyTax(ConvertUtils.toDoubleNumber(form.getMonthlyTax()).get());
                estate.setGainRatio(ConvertUtils.toDoubleNumber(form.getGainRatio()).get());
                estate.setArea(ConvertUtils.toDoubleNumber(form.getArea()).get());
                estate.setServiceCost(ConvertUtils.toDoubleNumber(form.getServiceCost()).get());
                estate.setCategory(repositoryCategory.findOne(ConvertUtils.toLongNumber(form.getCategory()).get()));
                estate.setBusinessType(repositoryBusinessType.findOne(ConvertUtils.toLongNumber(form.getBusinessType()).get()));
                break;
            case "VACANT":
                estate.setConstructDate(ConvertUtils.toDate(form.getConstructDate()).get());
                estate.setAvailableDate(ConvertUtils.toDate(form.getAvailableDate()).get());
                estate.setLoan(ConvertUtils.toDoubleNumber(form.getLoan()).get());
                estate.setContractArea(ConvertUtils.toDoubleNumber(form.getContractArea()).get());
                estate.setExclusiveArea(ConvertUtils.toDoubleNumber(form.getExclusiveArea()).get());
                estate.setExclusiveRate(ConvertUtils.toDoubleNumber(form.getExclusiveRate()).get());
                break;
            default:
                throw new ApplicationException(MessageResponse.EXCEPTION_BAD_REQUEST);
        }
        logger.info(form.toString());

        Estate estate1 = repositoryEstate.save(estate);

        List<String> _attachments = form.getAttachments();
        String fileRelativePath[], fileUrl, thumbUrl;
        try {
            if (_attachments != null && !_attachments.isEmpty()) {
                Set<Attachment> attachments = new HashSet<>();
                for (String file : _attachments) {
                    logger.info("file : " + file);
                    byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(file);
                    logger.info("imageBytes : "+imageBytes);
                    fileRelativePath = FileUtils.uploadImage(new ByteArrayInputStream(imageBytes), configParams.UPLOAD_DIRECTORY, "/estate/");
                    fileUrl = configParams.BASE_URL + "/public" + fileRelativePath[0];
                    thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];
                    logger.info("fileurl : " + fileUrl);

                    Attachment attachment = new Attachment();
                    attachment.setOrigin(fileUrl);
                    attachment.setThumbnail(thumbUrl);
                    attachment.setTableRef("estate");
                    attachment.setRowRef(estate1.getId());
                    repositoryAttachment.save(attachment);
//                    attachments.add(attachment);
                }
//                repositoryAttachment.save(attachments);
//                estate.setAttachments(attachments);
//                logger.info("getattach : " + estate.getAttachments().iterator().next().getOrigin());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("error : " + e.getMessage());
            logger.info("error : " + Arrays.toString(e.getStackTrace()));
            throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
        }
        return estate;
    }

    @Override
    public Estate update(FormEstate form) {

        Estate estate = repositoryEstate.findOne(form.getSpecification());
        User user = estate.getUser();

        Optional<Boolean> _isAdvertised = ConvertUtils.toBoolean(form.getIsAdvertised());
        if (_isAdvertised.isPresent() && _isAdvertised.get() && user.getAdvertisedRemain() > 0) {
            user.setAdvertisedRemain(user.getAdvertisedRemain() - 1);
            estate.setAdvertised(_isAdvertised.get());
        }
//
//        for (MultipartFile f : form.getAttachmentFiles()) {
//            try {
//                FileUtils.uploadImage(f.getInputStream(), configParams.UPLOAD_DIRECTORY, "/estate/");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        BeanUtils.copyProperties(form, estate);

        return repositoryEstate.save(estate);
    }

    @Override
    public Estate updateEstateType(FormEstate form, String type) {

        Estate estate = repositoryEstate.findOne(form.getSpecification());
        System.out.println("estate id: " +estate.getId());
        System.out.println("estate name: " +estate.getName());
        estate.setEstateType(type);
        return repositoryEstate.save(estate);
    }

    @Override
    public Estate findOne(FormEstate form) {
        Estate estate = repositoryEstate.findOne(form.getSpecification());
        FormWishlist formWishlist = new FormWishlist();
        if (estate != null) {
            //set iswished
            formWishlist.setEstateId(String.valueOf(estate.getId()));
            formWishlist.setUserId(String.valueOf(estate.getUser().getId()));
            estate.setWishlist(repositoryWishlist.findOne(formWishlist.getSpecification()) != null);
            logger.info("estate : " + estate.getWishlist() + " / setEstateId : " + estate.getId() + " / setUserId : " + estate.getUser().getId());
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", Long.parseLong(form.getEstateId()));

            estate.setAttachments(attachments);
        }
        return estate;
    }

    @Override
    public Page<Estate> filter(FormEstate form) {
        Page<Estate> estates = repositoryEstate.findAll(form.getSpecification(), form.getPaging());
        Iterator<Estate> iterator = estates.iterator();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public Page<Estate> filterAddr(FormEstate form) {
        Page<Estate> estates = repositoryEstate.findAll(form.dongLike(form.getAll_addr()), form.getPaging());
        Iterator<Estate> iterator = estates.iterator();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public Page<Estate> filter(FormEstate form, CustomUserDetail user) {
        Page<Estate> estates = repositoryEstate.findAll(form.getSpecification(), form.getPaging());
        Iterator<Estate> iterator = estates.iterator();
        FormWishlist formWishlist = new FormWishlist();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
            //set iswished
            formWishlist.setEstateId(String.valueOf(estate.getId()));//매물의아이디
            formWishlist.setUserId(String.valueOf(user.getUserId()));//현재 로그인한 사용자의 id
            estate.setWishlist(repositoryWishlist.findOne(formWishlist.getSpecification()) != null);
            logger.info("estatewish : " + estate.getWishlist() + " / setEstateId : " + estate.getId() + " / setUserId : " + estate.getUser().getId() + " / user.getUserId() : "+user.getUserId());

            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public void delete(FormEstate form) {
        Estate estate = repositoryEstate.findOne(form.getSpecification());
        if(estate == null){
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        estate.setIsDelete(1);
    }

    @Override
    public Estate updateEstate(FormEstate form, Estate estate) {
        estate = FormEstate.convertEstateFormToEstate(form,estate);
        List<MultipartFile> multipartFiles = form.getAttachmentFiles();
        if(multipartFiles!=null && !multipartFiles.isEmpty()){
            try {
//                MultipartFile file = multipartFiles.get(0);
//                String fileRelativePath[], fileUrl, thumbUrl;
//                fileRelativePath = FileUtils.uploadImage(file.getInputStream(), configParams.UPLOAD_DIRECTORY, "/estate/");
//                fileUrl = configParams.BASE_URL + "/public" + fileRelativePath[0];
//                thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];
//
//                Attachment attachment = new Attachment();
//                attachment.setOrigin(fileUrl);
//                attachment.setThumbnail(thumbUrl);
//                attachment.setTableRef("estate");
//                attachment.setRowRef(estate.getId());
//                repositoryAttachment.save(attachment);
//                user.setAvatar(attachment);
            } catch (Exception e) {
                throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
            }

        }
        return repositoryEstate.save(estate);
    }

    @Override
    public Integer updateImageEstate(MultipartFile file, Estate estate) {
        try {
            String fileRelativePath[], fileUrl, thumbUrl;
            fileRelativePath = FileUtils.uploadImage(file.getInputStream(), configParams.UPLOAD_DIRECTORY, "/estate/");
            fileUrl = configParams.BASE_URL + "/public" + fileRelativePath[0];
            thumbUrl = configParams.BASE_URL + "/public" + fileRelativePath[1];
            Attachment attachment = new Attachment();
            attachment.setOrigin(fileUrl);
            attachment.setThumbnail(thumbUrl);
            attachment.setTableRef("estate");
            attachment.setRowRef(estate.getId());
            repositoryAttachment.save(attachment);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Estate> filterBy(int pageIndex, String city, String district, String town, String type, String subway) {
        List<Estate> estates = null;
        pageIndex = pageIndex * 10;
        // if 3 field is null, then search all
        city = "%"+city+"%";
        district = "%"+district+"%";
        town = "%"+town+"%";
        subway = "%"+subway+"%";
        type = "%"+type+"%";

        if(Strings.isNullOrEmpty(city) && Strings.isNullOrEmpty(district) && Strings.isNullOrEmpty(town) && Strings.isNullOrEmpty(subway)){
            System.out.println("three field null");
            estates = repositoryEstate.findAllEstate(pageIndex,type);
            System.out.println("total size: "+estates.size());
        }
        if(!Strings.isNullOrEmpty(city)){
            System.out.println("city field not null");
            estates = repositoryEstate.findEstateByCity(pageIndex,city,type);
            System.out.println("city size: "+estates.size());
        }
        if(!Strings.isNullOrEmpty(district)){
            System.out.println("district field not null");
            estates = repositoryEstate.findEstateByDistrict(pageIndex,district,type);
        }
        if(!Strings.isNullOrEmpty(town)){
            System.out.println("town field not null");
            estates = repositoryEstate.findEstateByTown(pageIndex,town,type);
        }
        if(!Strings.isNullOrEmpty(subway)){
            System.out.println("subway field not null");
            estates = repositoryEstate.findEstateBySubway(pageIndex,subway,type);
        }
        Iterator<Estate> iterator = estates.iterator();
        while(iterator.hasNext()){
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate",estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public Long totalEstateFilter(String city, String district, String town, String type, String subway) {
        type = "%"+type+"%";
        city = "%"+city+"%";
        district = "%"+district+"%";
        town = "%"+town+"%";
        subway = "%"+subway+"%";

        Long count = null;
        if(Strings.isNullOrEmpty(city) && Strings.isNullOrEmpty(district) && Strings.isNullOrEmpty(town) && Strings.isNullOrEmpty(subway)){
            count = repositoryEstate.totalAllEstate(type);
            return count;
        }
        if(!Strings.isNullOrEmpty(city)){
            count = repositoryEstate.totalEstateByCity(city,type);
            return count;
        }
        if(!Strings.isNullOrEmpty(district)){
            count = repositoryEstate.totalEstateByDistrict(district,type);
            return count;
        }
        if(!Strings.isNullOrEmpty(town)){
            count = repositoryEstate.totalEstateByTown(town,type);
            return count;
        }
        if(!Strings.isNullOrEmpty(subway)){
            count = repositoryEstate.totalEstateBySubway(subway,type);
            return count;
        }
        return count;
    }

    @Override
    public List<Estate> filterBy(int pageIndex, String city, String district, String town, String type, String subway, String approved) {
        if(city.equals("0"))
            city="";
        if(district.equals("0"))
            district="";
        if(town.equals("0"))
            town ="";
        subway = "%"+subway+"%";
        List<Estate> estates = null;
        pageIndex = pageIndex * 10;
        type = "%"+type+"%";
        city = "%"+city+"%";
        district = "%"+district+"%";
        town = "%"+town+"%";
        // if 3 field is null, then search all
        estates = repositoryEstate.findEstates(pageIndex,city,district,town,type,subway,approved);
        System.out.println("total size: "+estates.size());
        Iterator<Estate> iterator = estates.iterator();
        while(iterator.hasNext()){
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate",estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public Long totalEstateFilter(String city, String district, String town, String type, String subway,String approved) {
        // if 3 field is null, then search all
        if(city.equals("0"))
            city="";
        if(district.equals("0"))
            district="";
        if(town.equals("0"))
            town ="";
        subway = "%"+subway+"%";

        List<Estate> estates = null;
        type = "%"+type+"%";
        city = "%"+city+"%";
        district = "%"+district+"%";
        town = "%"+town+"%";

        Long count = null;
        count = repositoryEstate.totalEstates(city,district,town,type,subway,approved);
        return count;
    }
    @Override
    public List<Estate> filterEstateByType(int pageSize,String typeTrust,String type) {
        List<Estate> estates = repositoryEstate.findEstateByType(pageSize,typeTrust,type);
        Iterator<Estate> iterator = estates.iterator();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
             Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public List<Estate> filterEstateOnMap(FormEstate formEstate) {
        formEstate.setIsApproved("1");
        List<Estate> estates = repositoryEstate.findAll(formEstate.searchOnMap());
        Iterator<Estate> iterator = estates.iterator();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        return estates;
    }

    @Override
    public Integer changeStatus(String id, String status) {
        Estate estate = repositoryEstate.findOne(Long.parseLong(id));
        estate.setIsApproved(Integer.parseInt(status));
        Estate estate_save = repositoryEstate.save(estate);
        if(estate_save != null){
            return 1;
        }
        return 0;
    }
    @Override
    public Estate changeAdvertisedEstate(FormEstate formEstate,String isAdvertised){
        Estate estate = repositoryEstate.findOne(formEstate.getSpecification());
        User user = estate.getUser();
        System.out.println("isAdvertised: "+isAdvertised);
        Optional<Boolean> _isAdvertised = ConvertUtils.toBoolean(isAdvertised);
        if (_isAdvertised.isPresent() && user.getAdvertisedRemain() > 0) {
            estate.setTypeTrust("REALTOR");
            if(_isAdvertised.get()) {
                user.setAdvertisedRemain(user.getAdvertisedRemain() - 1);
                estate.setTypeTrust("TRUSTED_STARTUP");
            }
            estate.setAdvertised(_isAdvertised.get());
        }
        return repositoryEstate.save(estate);
    }

    @Override
    public List<Estate> filterWithZoom(FormEstate formEstate,String zoomLevel) {
        List<Estate> estates = repositoryEstate.findAll(formEstate.getSpecification());
        List<Estate> estateWithZoom = new ArrayList<Estate>();
        Iterator<Estate> iterator = estates.iterator();
        while (iterator.hasNext()) {
            Estate estate = iterator.next();
            Collection<Attachment> attachments = repositoryAttachment.findByReference("estate", estate.getId());
            estate.setAttachments(attachments);
        }
        double latitude = Double.parseDouble(formEstate.getLatitude());
        double longitude = Double.parseDouble(formEstate.getLongitude());

        switch (zoomLevel){
            case "14":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("384"),latitude, longitude);
                break;
            case "13":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("192"),latitude, longitude);
                break;
            case "12":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("96"),latitude, longitude);
                break;
            case "11":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("48"),latitude, longitude);
                break;
            case "10":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("24"),latitude, longitude);
                break;
            case "9":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("12"),latitude, longitude);
                break;
            case "8":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("6"),latitude, longitude);
                break;
            case "7":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("3"),latitude, longitude);
                break;
            case "6":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("1.5"),latitude, longitude);
                break;
            case "5":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("0.75"),latitude, longitude);
                break;
            case "4":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("0.3"),latitude, longitude);
                break;
            case "3":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("0.17"),latitude, longitude);
                break;
            case "2":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("0.09"),latitude, longitude);
                break;
            case "1":
                estateWithZoom = getEstateWithZoomLevel(estates,Double.parseDouble("0.06"),latitude, longitude);
                break;

        }
        return estateWithZoom;
    }

    public List<Estate> getEstateWithZoomLevel(List<Estate> estates, Double zoomLevel, double latitude_1, double longitude_1){
        List<Estate> estateWithZoom = new ArrayList<Estate>();
        for(int i = 0 ; i < estates.size() ; i ++){
            double latitude_2 = Double.parseDouble(estates.get(i).getLatitude());
            double longitude_2 = Double.parseDouble(estates.get(i).getLongitude());
            double dLat = Math.abs(latitude_2 - latitude_1);
            double dLong = Math.abs(longitude_2 - longitude_1);
            int r = 6371;
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(latitude_1) * Math.cos(latitude_2) * Math.sin(dLong/2) * Math.sin(dLong/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double d = r * c;
            if(d > 0 && d < zoomLevel)
                estateWithZoom.add(estates.get(i));
        }
        return estateWithZoom;
    }
}


