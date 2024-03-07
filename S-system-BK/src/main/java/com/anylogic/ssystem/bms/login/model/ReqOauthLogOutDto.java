package com.anylogic.ssystem.bms.login.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import java.io.Serializable;
import java.lang.String;
import com.anylogic.ssystem.common.model.AnyXCommonVO;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@ApiModel
public class ReqOauthLogOutDto extends AnyXCommonVO implements Serializable {
    @ApiModelProperty("")
    private String client_id;                           //

    @ApiModelProperty("")
    private String extra;                           //

    @ApiModelProperty("")
    private String username;                           //
}

