package com.zren.platform.common.service.facade.dto.in.zpoke;

import com.zren.platform.common.service.facade.result.BaseSerializable;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.enums.RoundEnum;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author k.y
 * @version Id: TableMachineDTO.java, v 0.1 2019年10月22日 下午20:57 k.y Exp $
 */
@Data
public class TableMachineDTO extends BaseSerializable {

    private String sid;
    private String brand;
    private Integer gameId = 12;
    private Integer roomId;
    private String tableId;
    private TableStatsEnum tableStatsEnum;
    private List<PlayerMachineModel> playerList;
    private LinkedList<Integer> usableSeatList;
    private LinkedList<Integer> usedSeatList;
    private Integer leaderSeat;
    private Integer smallSeat;
    private Integer bigSeat;
    private Integer currentSeat;
    private RoundEnum round;
    private int[] commonCard;
    private int[] commonOneCard;
    private int[] commonTwoCard;
    private int[] commonThreeCard;
    private AtomicInteger offset;
}