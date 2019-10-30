//package com.zren.platform.application.robot;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.collect.Lists;
//import com.zren.platform.application.TestCase;
//import com.zren.platform.biz.action.rocketmq.producer.RocketProducer;
//import com.zren.platform.common.service.facade.api.accountPool.AccountPoolManage;
//import com.zren.platform.common.service.facade.api.robot.AIRobotRuleManage;
//import com.zren.platform.common.service.facade.api.robot.RobotPullManage;
//import com.zren.platform.common.service.facade.api.robot.RobotPushManage;
//import com.zren.platform.common.service.facade.dto.in.accountPool.AccountPoolInputModelDTO;
//import com.zren.platform.common.service.facade.dto.in.robotInfo.AIRobotInitInputModelDTO;
//import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotDestroyInputModelDTO;
//import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
//import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
//import com.zren.platform.common.service.facade.result.RobotBaseResult;
//import com.zren.platform.common.util.enums.EnumsCommon;
//import com.zren.platform.intercomm.util.MqTagUtil;
//import org.apache.rocketmq.client.exception.MQBrokerException;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.remoting.exception.RemotingException;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
///**
// * 机器人
// *
// * @author k.y
// * @version Id: RobotTest.java, v 0.1 2018年11月05日 下午15:44 k.y Exp $
// */
//public class RobotTest extends TestCase {
//
//    @Autowired
//    RobotPullManage robotPullManage;
//
//    @Autowired
//    RobotPushManage aiRobotManage;
//
//    @Autowired
//    AIRobotRuleManage robotRuleManage;
//
//    @Autowired
//    RocketProducer rocketProducer;
//
//    @Autowired
//    AccountPoolManage accountPoolManage;
//
//
//    /**
//     * 初始化业主水池配置
//     */
//    @Test
//    public void accountPoolManageCreate(){
//        AccountPoolInputModelDTO dto1=new AccountPoolInputModelDTO();
//        dto1.setBrand("jkeins");
//        dto1.setGameId(198);
//        dto1.setRoomType((byte) 1);
//        dto1.setRoomId(1998);
//        AccountPoolInputModelDTO dto2=new AccountPoolInputModelDTO();
//        dto2.setBrand("dubbe");
//        dto2.setGameId(99);
//        dto2.setRoomType((byte) 2);
//        dto2.setRoomId(999);
//        accountPoolManage.create(Lists.newArrayList(dto1,dto2));
//    }
//
//    /**
//     * 查询业主水池配置
//     */
//    @Test
//    public void accountPoolFindOne(){
//        AccountPoolInputModelDTO dto1=new AccountPoolInputModelDTO();
//        dto1.setBrand("109");
//        dto1.setGameId(20);
//        RobotBaseResult r=accountPoolManage.findOne(dto1);
//        System.out.println(r);
//    }
//
//    /**
//     * 单个或批量修改水池配置
//     */
//    @Test
//    public void accountPoolEdit(){
//        List<AccountPoolInputModelDTO> accountPoolInputModelDTOLst=new ArrayList<>();
//        AccountPoolInputModelDTO dto1=new AccountPoolInputModelDTO();
//        AccountPoolInputModelDTO dto2=new AccountPoolInputModelDTO();
//        /*单个修改业主配置*/
////        dto.setId(21);
////        dto.setBrand("106");
////        dto.setCapital(BigDecimal.valueOf(100000));
////        dto.setLossCronRule("0~5=100|5~15=60|15~25=70|25~35=80|35~50=80");
////        dto.setIsAble((byte) 0);
//
//        /*批量修改业主配置*/
//        dto1.setId(29);
//        dto1.setBrand("default");
//        dto1.setCapital(BigDecimal.valueOf(100000));
//        dto1.setLossCronRule("0~5=100|5~15=60|15~25=70|25~35=80|35~50=80");
//        dto1.setIsAble((byte) 0);
//        dto1.setRoomType((byte) 1);
//        accountPoolInputModelDTOLst.add(dto1);
//
//        dto2.setId(31);
//        dto2.setBrand("default");
//        dto2.setCapital(BigDecimal.valueOf(500000));
//        dto2.setLossCronRule("0~5=100|5~15=60|15~25=70|25~35=80|35~50=80");
//        dto2.setIsAble((byte) 0);
//        dto2.setRoomType((byte) 2);
//        accountPoolInputModelDTOLst.add(dto2);
//        RobotBaseResult r=accountPoolManage.edit(accountPoolInputModelDTOLst);
//        System.out.println(r);
//    }
//
//    @Test
//    public void createRobot(){
//
//        AIRobotInitInputModelDTO dto=new AIRobotInitInputModelDTO();
//        dto.setGameId(EnumsCommon.ZJH.getCode());
//        dto.setRoomId(101);
//        dto.setTableId("999999999");
//        dto.setAgentId(2000);
//        dto.setBrand("106");
//        dto.setClientId(2000);
//        dto.setCount(3);
//        dto.setMinAmount(BigDecimal.valueOf(100));
//        aiRobotManage.createRobot(dto);
//    }
//
//    /**
//     * 发送消息
//     */
//    @Test
//    public void sendMessage(){
//
//        JSONObject object = new JSONObject();
//        object.put("code", 5008);
//        object.put("userId", "2018149276411714301");
//        object.put("username", "kenny");
//        object.put("avatar", "111");
//        object.put("agentId", "111");
//        object.put("clientId", "111");
//        object.put("brand", "111");
//        object.put("device", "111");
//        String tag = MqTagUtil.getTagOutCommond(10, 103, "514134143");
//        try {
//            rocketProducer.sendMessage("cg_zjh_out", tag, object.toJSONString().getBytes());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (RemotingException e) {
//            e.printStackTrace();
//        } catch (MQClientException e) {
//            e.printStackTrace();
//        } catch (MQBrokerException e) {
//            e.printStackTrace();
//        }
//        System.out.println(">>>>>>>>>>>>>>>>>>> ======= Final");
//
//    }
//
//
//    /**
//     * 获取机器人
//     */
//    @Test
//    public void receiveRobotInfo(){
//
//        int count=0;
//        for(int i=0;i<3;i++){
//            RobotInitInputModelDTO dto=new RobotInitInputModelDTO();
//            dto.setGameId(10);
//            dto.setBrand("106");
//            dto.setRoomId(100);
//            dto.setMinAmount(BigDecimal.valueOf(300));
//            dto.setCount(5);
//            RobotBaseResult r=robotPullManage.createRobot(dto);
//            LinkedHashMap model=(LinkedHashMap)r.getResultObj();
//            if(1==(int)model.get("robotry")){
//                count++;
//            }
//        }
//        System.out.println("循环："+100+"次, 命中："+count);
//    }
//
//
//    /**
//     * 回收机器人
//     */
//    @Test
//    public void destroyRobot(){
//
//        List<RobotDestroyInputModelDTO> robotDestroyInputModelList=new ArrayList<>();
//        RobotDestroyInputModelDTO dto1=new RobotDestroyInputModelDTO();
//        RobotDestroyInputModelDTO dto2=new RobotDestroyInputModelDTO();
//        dto1.setBalance(BigDecimal.valueOf(1000));
//        dto1.setUserId(2018149276411714301L);
//        dto1.setBatchId(2019154693442962610L);
//        dto1.setGameId(22);
//        dto1.setBrand("106");
//        dto1.setRoomId(211);
//        dto2.setBalance(BigDecimal.valueOf(2000));
//        dto2.setUserId(2018149276411714302L);
//        dto2.setBatchId(2019154693442962610L);
//        dto2.setGameId(22);
//        dto2.setBrand("106");
//        dto2.setRoomId(211);
//        robotDestroyInputModelList.add(dto1);
//        robotDestroyInputModelList.add(dto2);
//
//        RobotBaseResult r=robotPullManage.destroyRobot(robotDestroyInputModelList);
//        System.out.println(">>>>>>>>>>>>>>>>>>> ======= "+r);
//    }
//
//
//    /**
//     * 获取规则策略
//     */
//    @Test
//    public void createRule(){
//
//        List<Long> userIdlist= new ArrayList<>();
//        userIdlist.add(201814301L);
//        RuleInputModelDTO zjhRuleInputModelDTO=new RuleInputModelDTO();
//        zjhRuleInputModelDTO.setBrand("106");
//        zjhRuleInputModelDTO.setGameId(10);
//        zjhRuleInputModelDTO.setRoomId(100);
//        zjhRuleInputModelDTO.setTableId("999999999");
//        zjhRuleInputModelDTO.setUserIdlist(userIdlist);
//        RobotBaseResult r=robotRuleManage.createRule(zjhRuleInputModelDTO);
//        System.out.println(">>>>>>>>>>>>>>>>>>> ======= "+r);
//
//    }
//
//}
