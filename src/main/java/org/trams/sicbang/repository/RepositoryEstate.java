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

    @Query(value = "SELECT * FROM estate e WHERE e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findAllEstate(@Param("page") Integer page,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN city c ON e.city_id=c.id and c.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByCity(@Param("page") Integer page, @Param("search") String city,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN district d ON e.city_id=d.id and d.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByDistrict(@Param("page") Integer page, @Param("search") String district,@Param("type") String type);

    @Query(value = "SELECT * FROM estate e INNER JOIN town t ON e.city_id=t.id and t.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type LIMIT :page, 10", nativeQuery = true)
    List<Estate> findEstateByTown(@Param("page") Integer page, @Param("search") String town,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN city c ON e.city_id=c.id and c.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByCity(@Param("search") String city,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN district d ON e.city_id=d.id and d.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByDistrict(@Param("search") String district,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e INNER JOIN town t ON e.city_id=t.id and t.name like :search AND e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalEstateByTown(@Param("search") String town,@Param("type") String type);

    @Query(value = "SELECT count(e.id) as count FROM estate e WHERE e.is_delete = 0 AND e.estate_type LIKE :type", nativeQuery = true)
    Long totalAllEstate(@Param("type") String type);
}
