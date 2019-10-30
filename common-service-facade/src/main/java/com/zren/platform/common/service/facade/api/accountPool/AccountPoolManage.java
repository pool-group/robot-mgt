package com.zren.platform.common.service.facade.api.accountPool;

import com.zren.platform.common.service.facade.constants.Common;
import com.zren.platform.common.service.facade.dto.in.accountPool.AccountPoolInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 *  机器人水池配置管理接口
 *
 * @author k.y
 * @version Id: AccountPoolManage.java, v 0.1 2019年02月28日 下午15:59 k.y Exp $
 */
@FeignClient(value= Common.SERVICE_NAME)
public interface AccountPoolManage {

    /**
     * 初始化某个业主/游戏下的房间水池配置信息
     *
     * @param accountPoolInputModelDTOList
     * 请求参数：brand gameId roomId roomType
     *
     * @return
     */
    @PostMapping("/accountPoolManage/create")
    RobotBaseResult create(List<AccountPoolInputModelDTO> accountPoolInputModelDTOList);

    /**
     * 查询某个业主下所有游戏/房间水池配置信息
     *
     * @param accountPoolInputModelDTO
     * 请求参数：brand
     *
     * @return
     */
    @PostMapping("/accountPoolManage/findOne")
    RobotBaseResult findOne(AccountPoolInputModelDTO accountPoolInputModelDTO);

    /**
     * 单个或批量修改业主/游戏下的房间水池配置信息
     *
     * @param accountPoolInputModelDTOLst
     * 请求参数：id brand gameId capital lossCronRule isAble roomType
     *
     * @return
     */
    @PostMapping("/accountPoolManage/edit")
    RobotBaseResult edit(List<AccountPoolInputModelDTO> accountPoolInputModelDTOLst);
}
