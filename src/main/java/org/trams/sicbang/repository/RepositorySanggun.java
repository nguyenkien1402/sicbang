package org.trams.sicbang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trams.sicbang.model.entity.Sanggun;
import org.trams.sicbang.model.entity.Town;

import java.util.List;

/**
 * Created by voncount on 5/12/16.
 */

public interface RepositorySanggun extends JpaRepository<Sanggun, Long>, JpaSpecificationExecutor<Sanggun> {
    //
//    @Query(value = "SELECT s FROM sanggun_dong AS s WHERE s.cityDegree LIKE %:cityDegree%")
//    Page<Sanggun> searchWithCity(@Param("cityDegree") String cityDegree);
    @Query(value = "select distinct s.township from sanggun_dong s where s.city_degree = ?1", nativeQuery = true)
    List<String> findTownList(@Param("cityDegree") String cityDegree);

    List<Sanggun> findDistinctSanggunBycityDegree(String cityDegree);

    @Query(value = "select concat(s.dong_name,' ',s.ri_name) from sanggun_dong s where s.city_degree = ?1 and s.township = ?2", nativeQuery = true)
    List<String> findDongList(String city_degree, String township);
}
