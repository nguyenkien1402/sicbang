package org.trams.sicbang.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by Administrator on 2016-06-01.
 */

@Entity
@Getter @Setter
@ToString
@Table(name ="sanggun_dong", indexes = {@Index(name = "dong_idx", columnList ="cityDegree,township,dong_name,ri_name" )})
public class Sanggun{

    private long id;
    private String manage_id;
    private int serial_num;
    @Id
    private String dong_code;
    private String cityDegree;
    private String township;
    private String dong_name;
    private String ri_name;
}
