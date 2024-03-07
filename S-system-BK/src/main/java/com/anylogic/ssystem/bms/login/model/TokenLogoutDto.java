package com.anylogic.ssystem.bms.login.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;

import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class TokenLogoutDto extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("")
    private String accessToken;                           //

    @ApiModelProperty("")
    private String userAccount;                           //
}

