package com.swx.content.model.vo;

import com.swx.content.model.po.CourseBase;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class CourseBaseInfoVO extends CourseBase {


    private String users;

    private String tags;

    private String mt;

    private String st;

    private String mtName;

    private String stName;

    private String grade;

    private String teachmode;

    private String description;

    private String pic;

    private String charge;

    private Float price;

    private Float originPrice;

    private String qq;

    private String wechat;

    private String phone;

    private String validDays;

}
