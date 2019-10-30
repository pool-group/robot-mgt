package com.zren.platform.biz.service.impl.manage;

import com.google.common.collect.Lists;
import com.zren.platform.biz.shared.callback.AbstractOpCallback;
import com.zren.platform.biz.shared.context.EngineContext;
import com.zren.platform.biz.shared.template.impl.BizOpCenterServiceTemplateImpl;
import com.zren.platform.common.dal.po.AccountPoolPO;
import com.zren.platform.common.dal.repository.AccountPoolManageRepository;
import com.zren.platform.common.service.facade.api.accountPool.AccountPoolManage;
import com.zren.platform.common.service.facade.dto.in.accountPool.AccountPoolInputModelDTO;
import com.zren.platform.common.service.facade.dto.out.accountPool.AccountPoolDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.common.util.tool.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class AccountPoolManageImpl implements AccountPoolManage {

    @Autowired
    private BizOpCenterServiceTemplateImpl bizOpCenterServiceTemplate;

    @Autowired
    private AccountPoolManageRepository accountPoolManageRepository;


    /**
     * 初始化创建业主水池默认信息
     *
     * @param accountPoolInputModelDTOList
     * @return
     */
    @Override
    public RobotBaseResult create(@RequestBody final List<AccountPoolInputModelDTO> accountPoolInputModelDTOList) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<AccountPoolInputModelDTO>,Void>(){

            @Override
            public void initContent(EngineContext<List<AccountPoolInputModelDTO>, Void> context) {
                context.setInputModel(accountPoolInputModelDTOList);
            }

            @Override
            public void doProcess(EngineContext<List<AccountPoolInputModelDTO>, Void> context) {
                //新增业主读取brand=666666的默认配置
                List<AccountPoolPO> accountPoolPOLst=accountPoolManageRepository.findByBrand("666666");
                for(AccountPoolInputModelDTO dto:accountPoolInputModelDTOList){
                    for (AccountPoolPO po:accountPoolPOLst){
                        if(po.getRoomType().compareTo(dto.getRoomType())==0){
                            AccountPoolPO paramPO=new AccountPoolPO();
                            BeanUtil.copyPropertiesIgnoreNull(po,paramPO);
                            paramPO.setId(null);
                            paramPO.setBrand(dto.getBrand());
                            paramPO.setGameId(dto.getGameId());
                            paramPO.setRoomId(dto.getRoomId());
                            accountPoolManageRepository.save(paramPO);
                        }
                    }
                }
            }
        });
    }

    /**
     * 查询某个业主下的所有水池配置信息
     *
     * @param accountPoolInputModelDTO
     * @return
     */
    @Override
    public RobotBaseResult findOne(@RequestBody final AccountPoolInputModelDTO accountPoolInputModelDTO) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<AccountPoolInputModelDTO,List<AccountPoolDTO>>(){

            @Override
            public void initContent(EngineContext<AccountPoolInputModelDTO, List<AccountPoolDTO>> context) {
                context.setInputModel(accountPoolInputModelDTO);
            }

            @Override
            public void doProcess(EngineContext<AccountPoolInputModelDTO, List<AccountPoolDTO>> context) {
                List<AccountPoolPO> accountPoolPOLst=null;
                if(accountPoolInputModelDTO.getGameId()==null||accountPoolInputModelDTO.getGameId().compareTo(0)==0){
                    accountPoolPOLst=accountPoolManageRepository.findByBrand(accountPoolInputModelDTO.getBrand());
                }else {
                    accountPoolPOLst=accountPoolManageRepository.findByBrandAndGameId(accountPoolInputModelDTO.getBrand(),accountPoolInputModelDTO.getGameId());
                }
                List<AccountPoolDTO> dtoLst= Lists.newArrayList();
                for (AccountPoolPO po:accountPoolPOLst){
                    AccountPoolDTO dto=new AccountPoolDTO();
                    BeanUtil.copyPropertiesIgnoreNull(po,dto);
                    dtoLst.add(dto);
                }
                context.setOutputModel(dtoLst);
            }
        });
    }

    /**
     * 单个或批量修改业主下的水池配置信息
     *
     * @param accountPoolInputModelDTOLst
     * @return
     */
    @Override
    public RobotBaseResult edit(@RequestBody final List<AccountPoolInputModelDTO> accountPoolInputModelDTOLst) {
        return bizOpCenterServiceTemplate.doBizProcess(new AbstractOpCallback<List<AccountPoolInputModelDTO>,Void>(){

            @Override
            public void initContent(EngineContext<List<AccountPoolInputModelDTO>, Void> context) {
                context.setInputModel(accountPoolInputModelDTOLst);
            }

            @Override
            public void doProcess(EngineContext<List<AccountPoolInputModelDTO>, Void> context) {
                for(AccountPoolInputModelDTO dto:accountPoolInputModelDTOLst){
                    Optional<AccountPoolPO> optional=accountPoolManageRepository.findById(dto.getId());
                    AccountPoolPO po=optional.get();
                    if(StringUtils.isBlank(dto.getBrand())){
                        List<AccountPoolPO> lst=accountPoolManageRepository.findAll();
                        for(AccountPoolPO allPO:lst){
                            if(allPO.getRoomType().compareTo(dto.getRoomType())==0&&allPO.getGameId().compareTo(dto.getGameId())==0){
                                if(po.getCapital().compareTo(dto.getCapital())!=0){
                                    allPO.setCapital(dto.getCapital());
                                    po.setCapital(dto.getCapital());
                                }
                                if(!po.getLossCronRule().equals(dto.getLossCronRule())){
                                    allPO.setLossCronRule(dto.getLossCronRule());
                                    po.setLossCronRule(dto.getLossCronRule());
                                }
                                if(po.getIsAble().compareTo(dto.getIsAble())!=0){
                                    allPO.setIsAble(dto.getIsAble());
                                    po.setIsAble(dto.getIsAble());
                                }
                                if(po.getRtpValue().compareTo(dto.getRtpValue())!=0){
                                    allPO.setRtpValue(dto.getRtpValue());
                                    po.setRtpValue(dto.getRtpValue());
                                }
                            }
                        }
                        accountPoolManageRepository.saveAll(lst);
                    }else {
                        po.setCapital(dto.getCapital());
                        po.setLossCronRule(dto.getLossCronRule());
                        po.setIsAble(dto.getIsAble());
                        po.setRtpValue(dto.getRtpValue());
                    }
                    accountPoolManageRepository.save(po);
                }
            }
        });
    }

}
