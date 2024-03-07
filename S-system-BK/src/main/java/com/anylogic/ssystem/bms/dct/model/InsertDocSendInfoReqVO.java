/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.model;

import com.anylogic.ssystem.common.model.AnyXCommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class InsertDocSendInfoReqVO extends AnyXCommonVO implements Serializable {
    // bms_dct_doc_sendinfopath
    @ApiModelProperty(value = "")
    @Size(max=35)
    @NotEmpty
    private String docid;                        //

    @ApiModelProperty(value = "")
    @Size(max=1)
    @NotEmpty
    private String docgubun;                     //

    @ApiModelProperty(value = "")
    @Size(max=1)
    @NotEmpty
    private String tempflag;                     //

    @ApiModelProperty(value = "")
    @Size(max=256)
    private String sendorgname;                  //

    @ApiModelProperty(value = "")
    @Size(max=100)
    private String sendname;                     //

    @ApiModelProperty(value = "")
    @Size(max=200)
    private String campaignhead;                 //

    @ApiModelProperty(value = "")
    @Size(max=200)
    private String campaigntail;                 //

    @ApiModelProperty(value = "")
    @Size(max=14)
    private String senddt;                       //

    @ApiModelProperty(value = "")
    @Size(max=4000)
    private String sendopinion;                  //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String senderid;                     //

    @ApiModelProperty(value = "")
    @Size(max=100)
    private String sendername;                   //

    @ApiModelProperty(value = "")
    @Size(max=100)
    private String senderdutyname;               //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String senderpositionid;             //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String sendergradeid;                //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String senderdeptid;                 //

    @ApiModelProperty(value = "")
    @Size(max=256)
    private String senderdeptname;               //

    @ApiModelProperty(value = "")
    @Size(max=500)
    private String senderdeptnamedesc;           //

    @ApiModelProperty(value = "")
    @Size(max=256)
    private String address;                      //

    @ApiModelProperty(value = "")
    @Size(max=100)
    private String zipcode;                      //

    @ApiModelProperty(value = "")
    @Size(max=200)
    private String homepage;                     //

    @ApiModelProperty(value = "")
    @Size(max=100)
    private String phonenumber;                  //

    @ApiModelProperty(value = "")
    @Size(max=50)
    private String faxnumber;                    //

    @ApiModelProperty(value = "")
    @Size(max=200)
    private String email;                        //

    @ApiModelProperty(value = "")
    @Size(max=2000)
    private String recvdisplayname;              //

    @ApiModelProperty(value = "")
    @Size(max=1)
    private String recvdisplayflag;              //

    @ApiModelProperty(value = "")
    @Size(max=500)
    private String viadeptname;                  //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String enfrecvrange;                 //

    @ApiModelProperty(value = "")
    @Size(max=1)
    private String sendtype;                     //

    @ApiModelProperty(value = "")
    @Size(max=5)
    private String sealsigntype;                 //

    @ApiModelProperty(value = "")
    @Size(max=20)
    private String sealsignname;                 //

    @ApiModelProperty(value = "")
    @Size(max=35)
    private String sendorgid;                    //

    @ApiModelProperty(value = "")
    @Size(max=14)
    private String faxsndreqdt;                  //

    @ApiModelProperty(value = "")
    @Size(max=1)
    private String circdocflag;                  //

    @ApiModelProperty(value = "")
    @Size(max=1)
    private String distctrlflag;                 //

    @ApiModelProperty(value = "")
    @Size(max=1)
    private String distplflag;                   //

}

