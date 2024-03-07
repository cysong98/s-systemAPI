/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.com.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;
import java.io.Serializable;
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel("deleteOrganizationDetail")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteOrganizationDetailEleResVO extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("조직 ID") private String orgid;             // 조직 ID
    @ApiModelProperty("설명") private String description;          // 설명
    @ApiModelProperty("홈페이지") private String homepage;         // 홈페이지
    @ApiModelProperty("우편번호") private String zipcode;          // 우편번호
    @ApiModelProperty("주소정보") private String address;          // 주소정보
    @ApiModelProperty("주소상세") private String addressdetail;    // 주소상세
    @ApiModelProperty("전화번호") private String telephone;        // 전화번호
    @ApiModelProperty("FAX") private String fax;                   // FAX
}

