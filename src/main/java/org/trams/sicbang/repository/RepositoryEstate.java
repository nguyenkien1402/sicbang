package org.trams.sicbang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Estate;

import java.util.List;

/**
 * Created by voncount on 4/12/16.
 */
public interface RepositoryEstate extends JpaRepository<Estate, Long>, JpaSpecificationExecutor<Estate> {


    @Query(value = "SELECT * FROM estate e where e.city_id like :city and e.district_id like :district" +
            " and e.town_id like :town and e.subway_station like :subway " +
            "and e.estate_type like :type and e.is_approved <= :approved AND e.is_delete = 0 LIMIT :page,10", nativeQuery = true)
    List<Estate> findEstates(@Param("page") Integer page,@Param("city") String city,
                             @Param("district") String district,@Param("town") String town,
                             @Param("type") String type,@Param("subway") String subway,
                             @Param("approved") String approved);

    @Query(value = "SELECT * FROM estate e WHERE e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findAllEstate(@Param("page") Integer page,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN city c ON e.city_id=c.id and c.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByCity(@Param("page") Integer page, @Param("search") String city,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e where e.is_delete = 0 AND e.type_trust = :typeTrust AND e.is_approved = 1 AND e.estate_type like :type ORDER BY RAND() LIMIT 0,:pageSize", nativeQuery = true)
    List<Estate> findEstateByType(@Param("pageSize") Integer pageSize,@Param("typeTrust") String typeTrust,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN district d ON e.district_id=d.id and d.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByDistrict(@Param("page") Integer page, @Param("search") String district,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN town t ON e.town_id=t.id and t.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByTown(@Param("page") Integer page, @Param("search") String town,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e WHERE e.subway_station like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateBySubway(@Param("page") Integer page, @Param("search") String subway,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN city c ON e.city_id=c.id and c.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByCity(@Param("search") String city,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN district d ON e.district_id=d.id and d.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByDistrict(@Param("search") String district,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN town t ON e.town_id=t.id and t.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByTown(@Param("search") String town,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e WHERE e.subway_station like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateBySubway(@Param("search") String subway,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e WHERE e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalAllEstate(@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e where e.city_id like :city and e.district_id like :district" +
            " and e.town_id like :town and e.subway_station like :subway " +
            "and e.estate_type like :type and e.is_approved <= :approved and e.is_delete = 0", nativeQuery = true)
    Long totalEstates(@Param("city") String city,
                             @Param("district") String district,@Param("town") String town,
                             @Param("type") String type,@Param("subway") String subway,
                             @Param("approved") String approved);


}
