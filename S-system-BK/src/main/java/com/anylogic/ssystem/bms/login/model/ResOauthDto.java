package com.anylogic.ssystem.bms.login.model;

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
import com.anylogic.ssystem.common.model.AnyXCommonVO;

@ApiModel
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResOauthDto extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("") private String access_token;             //
    @ApiModelProperty("") private String jwt;           //
    @ApiModelProperty("") private String expires_in;           //
    @ApiModelProperty("") private String scope;    //
    @ApiModelProperty("") private String token_type;            //
    @ApiModelProperty("") private String refresh_token;            //
}

