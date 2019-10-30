package com.zren.platform.core.rule.factory;

import com.zren.platform.common.util.constants.ChessCard;
import com.zren.platform.common.util.tool.DataUtil;
import com.zren.platform.core.rule.context.ChessContext;
import com.zren.platform.core.rule.strategy.BasePlayCard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChessCardFactory {


    /**
     * 生成卡牌 ChessCard
     *
     * @param portion 几份牌
     * @param sheets 共有几张牌
     * @param minIndex 最小索引
     * @param maxIndex 最大索引
     * @param chessContext 上下文
     * @param basePlayCard 自定义比牌规则
     */
    public void create(int portion, int sheets, int minIndex, int maxIndex, ChessContext chessContext, BasePlayCard basePlayCard){

        //从指定编号区间中随机取牌
        Integer[] totalIndex=DataUtil.randomNumber(minIndex, maxIndex, sheets);
        //每份有几张牌
        int count=sheets/portion;
        List<int[]> lst=new ArrayList<>();
        for(int i=0;i<portion;i++){
            int[] ig=new int[sheets/portion];
            int index=0;
            for (int j=(i*count);j<(i*count+count);j++){
                ig[index]=totalIndex[j];
                index++;
            }
            lst.add(ig);
        }
        if(null!=basePlayCard){
            //升序排列
            for(int i=0;i<lst.size()-1;i++){//外层循环控制排序趟数
                for(int j=0;j<lst.size()-1-i;j++){//内层循环控制每一趟排序多少次

                    /*
                     * 参数说明：
                     *
                     * flag为true:第一个参数大,  为false:第二个参数大
                     * compareTo: 比牌规则方法
                     */
                    Boolean flag=basePlayCard.compareTo(lst.get(j) ,lst.get(j+1));
                    if(flag){
                        int[] temp=lst.get(j);
                        lst.set(j, lst.get(j+1));
                        lst.set(j+1, temp);
                    }
                }
            }
        }
        //List<int[]>转换为List<String>
        List<String> resultLst=new ArrayList<>();
        for(int i=0;i<lst.size();i++){
            StringBuilder sb=new StringBuilder();
            for(int j=0;j<lst.get(i).length;j++){
                sb.append(lst.get(i)[j]).append(ChessCard.MARK_UNLINE);
            }
            sb.deleteCharAt(sb.length()-1);
            resultLst.add(sb.toString());
        }
        chessContext.setChess(resultLst);
    }

}
